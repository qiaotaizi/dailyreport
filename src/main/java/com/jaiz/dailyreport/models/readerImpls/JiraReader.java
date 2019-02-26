package com.jaiz.dailyreport.models.readerImpls;

import com.alibaba.fastjson.JSONArray;
import com.jaiz.dailyreport.config.ConfigManager;
import com.jaiz.dailyreport.models.*;
import com.jaiz.dailyreport.models.fillers.ReportFiller;
import com.jaiz.dailyreport.models.fillers.ReportFillerFactory;
import com.jaiz.utils.DateTimeUtil;
import com.jaiz.utils.GlobalConstant;
import com.jaiz.utils.HttpUtil;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class JiraReader implements ReportDataSourceReader {

    private static final String COOKIE_NAME = "JSESSIONID";

    @Override
    public ReportFiller read() {
        System.out.println("登录jira...");
        String sessionId = jiraLoginSessionId();
        System.out.println("获取jira用户数据...");
        JiraUserVO userInfo = jiraForUser(sessionId);
        System.out.println("获取jira任务日程...");
        List<JiraMissionVO> missions = jiraCalendarMission(sessionId, userInfo.getId());
        Calendar today = Calendar.getInstance();
        Calendar nextWorkDay = Calendar.getInstance();
        //推断下个工作日的时间
        if (today.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            //若今天是周五,下个工作日为当天加3天,即下周一
            nextWorkDay.add(Calendar.DATE, 3);
        } else {
            //若今天不是周无,下个工作日为明天
            nextWorkDay.add(Calendar.DATE, 1);
        }
        List<String> achievementList = new ArrayList<>();
        List<String> todoList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date todayDate = today.getTime();
        Date nextWorkDayDate = nextWorkDay.getTime();
        for (JiraMissionVO mission : missions) {
            try {
                mission.setStartDate(sdf.parse(mission.getStart() + " 00:00:00"));
                mission.setEndDate(sdf.parse(mission.getEnd() + " 00:00:00"));
            } catch (ParseException e) {
                e.printStackTrace();
                continue;
            }
            //任务长度
            Integer missionDuration = calMissionDuration(mission);
            //当前任务是否今日正在进行
            if (missionInProgress(mission, todayDate)) {
                //若正在进行
                //计算已完成的百分比(精确到个位,向下取整)
                Integer progress = calMissionProgress(mission, todayDate, missionDuration);
                //拼接字符串,加入正在进行任务列表
                achievementList.add(concatMissionStr(achievementList.size(), mission, progress));
            }
            //当前任务是否下个工作日日正在进行
            if (missionInProgress(mission, nextWorkDayDate)) {
                //若正在进行
                //计算当前任务的长度,并计算下个工作日将完成的百分比(向下取整)
                Integer progress = calMissionProgress(mission, nextWorkDayDate, missionDuration);
                //拼接字符串,加入将要进行任务列表
                todoList.add(concatMissionStr(todoList.size(), mission, progress));
            }
        }
        String[] achievements = achievementList.toArray(new String[achievementList.size()]);
        String[] problems = {"1.无"};
        String[] todo = todoList.toArray(new String[todoList.size()]);
        String date = today.get(Calendar.YEAR) + "年" + (today.get(Calendar.MONTH) + 1) + "月" + today.get(Calendar.DATE)
                + "日";
        ReportFiller filler = ReportFillerFactory.createReportFiller(
                achievements, problems, todo, date
        );
        return filler;
    }

    /**
     * 拼接任务字符串
     *
     * @param size
     * @param mission
     * @param progress
     * @return
     */
    private String concatMissionStr(int size, JiraMissionVO mission, Integer progress) {
        return (size + 1) + "." + mission.getTitle() + " http://jira.ttpai.cn/browse/" + mission.getId() + " " + progress + "%";
    }

    /**
     * 计算任务进度
     *
     * @param mission
     * @param targetDate
     * @param missionDuration
     * @return 如30% 返回30
     */
    private Integer calMissionProgress(JiraMissionVO mission, Date targetDate, Integer missionDuration) {
        int progressedDay = calDayCountBetweenTwoTimeStampJumpWeekends(mission.getStartDate().getTime(), targetDate.getTime());
        return (int) Math.floor(progressedDay * 100.0 / missionDuration);
    }

    /**
     * 计算任务历时(单位:天)
     *
     * @param mission
     * @return
     */
    private int calMissionDuration(JiraMissionVO mission) {
        return calDayCountBetweenTwoTimeStampJumpWeekends(mission.getStartDate().getTime(), mission.getEndDate().getTime());
    }

    /**
     * 计算两个时间戳之间相差的天数,跳过周末
     * 不满一天按一天计
     * 参数为毫秒级时间戳
     *
     * @param ts1 起始时间
     * @param ts2 终止时间
     * @return
     */
    private int calDayCountBetweenTwoTimeStampJumpWeekends(long ts1, long ts2) {
        int i = 0;
        long oneDaySecondMillis = 24 * 60 * 60 * 1000l;//一天长度
        for (; ts1 < ts2; ts1 += oneDaySecondMillis) {
            if (!DateTimeUtil.isWeekend(ts1)) {
                i++;
            }
        }
        return i;
    }

    /**
     * 判断任务在指定日期是否正在进行
     *
     * @param mission
     * @param date
     * @return
     */
    private boolean missionInProgress(JiraMissionVO mission, Date date) {
        return date.compareTo(mission.getStartDate()) >= 0 && date.compareTo(mission.getEndDate()) <= 0;
    }

    /**
     * 获取sessionId代表的用户信息
     *
     * @param sessionId
     * @return
     */
    private JiraUserVO jiraForUser(String sessionId) {
        BasicClientCookie cookie = new BasicClientCookie(COOKIE_NAME, sessionId);
        cookie.setDomain("jira.ttpai.cn");
        cookie.setPath("/");
        String result = HttpUtil.getCookie(ConfigManager.getInstance().jiraCalendarForUserUrl, cookie);
        List<JiraUserVO> users = JSONArray.parseArray(result, JiraUserVO.class);
        //获取代表本人的用户信息
        for (JiraUserVO user : users) {
            if (user.getName().startsWith(ConfigManager.getInstance().reporterName)) {
                return user;
            }
        }
        //未找到的话,返回首个用户
        return users.get(0);
    }

    /**
     * 获取本月1号到下月1号的任务表
     *
     * @param sessionId
     * @param userId
     * @return
     */
    private List<JiraMissionVO> jiraCalendarMission(String sessionId, Integer userId) {
        //获取查询时间的起止
        Date start = DateTimeUtil.firstDayOfThisMonth();
        Date end = DateTimeUtil.firstDayOfNextMonth();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = sdf.format(start);
        String endDate = sdf.format(end);
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("start", startDate);
        paramMap.put("end", endDate);
        BasicClientCookie cookie = new BasicClientCookie(COOKIE_NAME, sessionId);
        cookie.setDomain("jira.ttpai.cn");
        cookie.setPath("/");
        String result = HttpUtil.getMapCookie(ConfigManager.getInstance().jiraCalendarJsonRequestUrl + userId, paramMap, cookie);
        return JSONArray.parseArray(result, JiraMissionVO.class);
    }

    /**
     * 发送登录http请求,并返回SessionId
     *
     * @return
     */
    private String jiraLoginSessionId() {
        String osUserName = ConfigManager.getInstance().jiraLoginUsername;
        String osPassWord = ConfigManager.getInstance().jiraLoginPassword;
        String login = ConfigManager.getInstance().jiraLogin;
        String loginUrl = ConfigManager.getInstance().jiraLoginUrl;

        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpPost post = new HttpPost(loginUrl);
        List<NameValuePair> pairs = new ArrayList<>();
        NameValuePair userNamePair = new BasicNameValuePair("os_username", osUserName);
        NameValuePair passwordPair = new BasicNameValuePair("os_password", osPassWord);
        NameValuePair loginPair = new BasicNameValuePair("login", login);
        pairs.add(userNamePair);
        pairs.add(passwordPair);
        pairs.add(loginPair);
        String sessionId = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(pairs, GlobalConstant.DEFAULT_CHARSET));
            client.execute(post);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        List<Cookie> cookieList = cookieStore.getCookies();
        for (Cookie cookie : cookieList) {
            if (COOKIE_NAME.equals(cookie.getName())) {
                sessionId = cookie.getValue();
            }
        }
        return sessionId;
    }
}
