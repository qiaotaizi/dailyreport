package com.jaiz.dailyreport.config;

import com.jaiz.dailyreport.annotations.ConfigCover;

import java.io.File;

/**
 * 日报生成默认配置
 * 任何属性都必须配置@ConfigCover注解
 * 基本数据类型的配置项不要使用其装箱类型
 * 如:int类型不要使用Integer来取代
 */
public class Config {

    /**
     * 使用什么命令打开生成的日报文件
     */
    @ConfigCover("open.report.app")
    public String openReportApp = "notepad";

    /**
     * jira登录os_username参数
     */
    @ConfigCover(value = "jira.login.username", necessary = true)
    public String jiraLoginUsername;

    /**
     * jira登录os_password参数
     */
    @ConfigCover(value = "jira.login.password", necessary = true)
    public String jiraLoginPassword;

    /**
     * jira登录os_password参数login参数
     */
    @ConfigCover(value = "jira.login", necessary = true)
    public String jiraLogin;

    /**
     * jira登录url
     */
    @ConfigCover("jira.login.url")
    public String jiraLoginUrl = "http://jira.ttpai.cn/login.jsp";

    /**
     * jira登陆后获取用户信息url
     */
    @ConfigCover("jira.calendar.for.user.url")
    public String jiraCalendarForUserUrl = "http://jira.ttpai.cn/rest/mailrucalendar/1.0/calendar/forUser";


    /**
     * jira获取日历json信息url
     * 最后需要拼接jiraCalendarForUserUrl获取的userId
     */
    @ConfigCover("jira.calendar.json.request.url")
    public String jiraCalendarJsonRequestUrl = "http://jira.ttpai.cn/rest/mailrucalendar/1.0/calendar/events/";

    /**
     * 日报生成位置
     * 默认为user.home/Documents/dailyReport/
     */
    @ConfigCover("report.output.position")
    public String reportOutputPosition = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "dailyReport" + File.separator;

    /**
     * 日报模板文件位置
     * 默认为默认为user.home/Documents/dailyReport/dailyReport.template
     */
    @ConfigCover("report.template.position")
    public String reportTemplateFile = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "dailyReport" + File.separator + "dailyReport.template";

    /**
     * codeReview模板文件位置
     * 默认为默认为user.home/Documents/dailyReport/codeReview.template
     */
    @ConfigCover("code.review.template.position")
    public String codeReviewTemplateFile = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "dailyReport" + File.separator + "codeReview.template";

    /**
     * 日报存留天数
     */
    @ConfigCover("report.remain.time")
    public int reportRemainTime = 14;

    /**
     * 是否开启从jira读取日报数据
     */
    @ConfigCover("read.data.from.jira")
    public boolean readDataFromJira = false;

    /**
     * 预处理文件位置
     */
    @ConfigCover("pre.deal.file.position")
    public String preDealFile = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "dailyReport" + File.separator + "PreDeal.pre";

    /**
     * 报告人姓名,最好和jira中的姓名相一致
     */
    @ConfigCover(value = "reporter.name", necessary = true)
    public String reporterName;

    /**
     * 部门名称
     */
    @ConfigCover(value = "department.name", necessary = true)
    public String departmentName;

    /**
     * 收件人邮箱地址,多个值时用,分割
     */
    @ConfigCover(value = "email.receivers", necessary = true)
    public String emailReceivers;

    /**
     * 抄送人邮箱地址,多个值时用,分割
     */
    @ConfigCover(value = "email.cc.recievers", necessary = true)
    public String emailCcRecievers;

    /**
     * codeReview对象邮箱地址
     */
    @ConfigCover(value = "code.review.reciever", necessary = true)
    public String codeReviewReciever;

}
