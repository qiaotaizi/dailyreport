package com.jaiz.dailyreport.models.readerImpls;

import com.jaiz.dailyreport.config.ConfigManager;
import com.jaiz.dailyreport.models.PreDealContent;
import com.jaiz.dailyreport.models.ReportDataSourceReader;
import com.jaiz.dailyreport.models.ReportFiller;
import com.jaiz.dailyreport.models.ReportFillerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PreDealReader implements ReportDataSourceReader {
    @Override
    public ReportFiller read() {
        PreDealContent pre = analysePreDealContent();
        // 预处理文档内容填充至自己的位置
        if (pre.getAchievement().size() == 0) {
            pre.getAchievement().set(0,"1.xxxxA jira地址链接A");
        }
        if (pre.getProblem().size() == 0) {
            pre.getProblem().set(0,"1.无");
        }
        if (pre.getTodo().size() == 0) {
            pre.getAchievement().set(0,"1.xxxxA jira地址链接A");
        }
        Calendar curr = Calendar.getInstance();
        String date = curr.get(Calendar.YEAR) + "年" + (curr.get(Calendar.MONTH) + 1) + "月" + curr.get(Calendar.DATE)
                + "日";
        ReportFiller filler= ReportFillerFactory.getReportFiller(
                pre.getAchievement().toArray(new String[pre.getAchievement().size()]),
                pre.getProblem().toArray(new String[pre.getProblem().size()]),
                pre.getTodo().toArray(new String[pre.getTodo().size()]),
                date
        );
        return filler;
    }

    /**
     * 分析预处理文档
     *
     * @return
     * @throws IOException
     */
    private PreDealContent analysePreDealContent() {
        PreDealContent result = new PreDealContent();
        List<String> achievement = new ArrayList<>();
        List<String> problem = new ArrayList<>();
        List<String> todo = new ArrayList<>();
        File prefile = new File(ConfigManager.getInstance().preDealFile);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(prefile), "UTF-8"));

            String line;
            List<String> target = null;
            while ((line = reader.readLine()) != null) {
                line=line.trim();
                if ("".equals(line)) {
                    // 空行不处理
                    continue;
                }
                char char0 = line.charAt(0);
                switch (char0) {
                    case 'a':// 成果
                        target = achievement;
                        continue;
                    case 'b':// 问题
                        target = problem;
                        continue;
                    case 'c':// 计划
                        target = todo;
                        continue;
                }
                target.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        result.setAchievement(achievement);
        result.setProblem(problem);
        result.setTodo(todo);
        return result;
    }
}
