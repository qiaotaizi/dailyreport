package com.jaiz.dailyreport.models.fillers;

import com.jaiz.dailyreport.annotations.FillerName;

/**
 * 日报内容填充物
 */
public class ReportFiller extends TemplateFiller {

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


}
