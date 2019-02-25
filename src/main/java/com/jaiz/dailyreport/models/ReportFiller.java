package com.jaiz.dailyreport.models;

import com.jaiz.dailyreport.annotations.FillerName;
import com.jaiz.dailyreport.config.ConfigManager;

import java.io.*;
import java.lang.reflect.Field;

/**
 * 日报内容填充物
 */
public class ReportFiller {

    /**
     * 收件人
     */
    @FillerName(value = "email.recievers", joinSpliter = ",")
    private String[] emailRecievers;

    /**
     * 抄送人
     */
    @FillerName(value = "email.cc.recievers", joinSpliter = ",")
    private String[] emailCcRecievers;

    /**
     * 部门名称
     */
    @FillerName("department.name")
    private String departmentName;

    /**
     * 报告日期
     */
    @FillerName("report.date")
    private String reportDate;

    /**
     * 报告人姓名
     */
    @FillerName("reporter.name")
    private String reporterName;

    /**
     * 当天工作成果
     */
    @FillerName(value = "today.achievements", joinSpliter = "\n")
    private String[] todayAchievements;

    /**
     * 碰到问题&解决方案
     */
    @FillerName(value = "problems.solutions", joinSpliter = "\n")
    private String[] problemsSolutions;

    /**
     * 明天工作计划
     */
    @FillerName(value = "tomorrow.targets", joinSpliter = "\n")
    private String[] tomorrowTargets;

    public String[] getEmailRecievers() {
        return emailRecievers;
    }

    public void setEmailRecievers(String[] emailRecievers) {
        this.emailRecievers = emailRecievers;
    }

    public String[] getEmailCcRecievers() {
        return emailCcRecievers;
    }

    public void setEmailCcRecievers(String[] emailCcRecievers) {
        this.emailCcRecievers = emailCcRecievers;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String[] getTodayAchievements() {
        return todayAchievements;
    }

    public void setTodayAchievements(String[] todayAchievements) {
        this.todayAchievements = todayAchievements;
    }

    public String[] getProblemsSolutions() {
        return problemsSolutions;
    }

    public void setProblemsSolutions(String[] problemsSolutions) {
        this.problemsSolutions = problemsSolutions;
    }

    public String[] getTomorrowTargets() {
        return tomorrowTargets;
    }

    public void setTomorrowTargets(String[] tomorrowTargets) {
        this.tomorrowTargets = tomorrowTargets;
    }

    /**
     * 将内容填充至日报模板
     * @param reportFile
     */
    public void fill(File reportFile) {
        //获取模板文件
        File templateFile=new File(ConfigManager.getInstance().reportTemplateFile);
        BufferedReader reader=null;
        BufferedWriter writer=null;
        try {
            reader=new BufferedReader(new FileReader(templateFile));
            writer=new BufferedWriter(new FileWriter(reportFile));
            String line;
            while ((line=reader.readLine())!=null){
                Field[] fields=this.getClass().getDeclaredFields();
                for(Field f:fields){
                    FillerName fillerName=f.getAnnotation(FillerName.class);
                    String fillStr;
                    if(f.getType()==String.class){
                        //String
                        f.get(this);
                    }else{
                        //String[]
                    }
                    // TODO
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(writer!=null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //逐行读取
        //将可替换的值写入
        //将该行写入新的日报文件
    }
}
