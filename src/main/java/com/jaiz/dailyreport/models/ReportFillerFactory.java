package com.jaiz.dailyreport.models;

import com.jaiz.dailyreport.config.Config;
import com.jaiz.dailyreport.config.ConfigManager;

public class ReportFillerFactory {

    /**
     * reportFiller实例化工厂方法
     * @param todayAchievements
     * @param problemsSolutions
     * @param tomorrowTargets
     * @param reportDate
     * @return
     */
    public final static ReportFiller getReportFiller(
            String[] todayAchievements,
            String[] problemsSolutions,
            String[] tomorrowTargets,
            String reportDate){
        ReportFiller filler=new ReportFiller();
        filler.setDepartmentName(ConfigManager.getInstance().departmentName);
        filler.setEmailCcRecievers(ConfigManager.getInstance().emailCcRecievers.split(","));
        filler.setEmailRecievers(ConfigManager.getInstance().emailReceivers.split(","));
        filler.setProblemsSolutions(problemsSolutions);
        filler.setReportDate(reportDate);
        filler.setReporterName(ConfigManager.getInstance().reporterName);
        filler.setTodayAchievements(todayAchievements);
        filler.setTomorrowTargets(tomorrowTargets);
        return filler;
    }

}
