package com.jaiz.dailyreport.models;

import com.jaiz.dailyreport.config.ConfigManager;
import com.jaiz.dailyreport.config.Templates;
import com.jaiz.dailyreport.models.fillers.ReportFiller;
import com.jaiz.dailyreport.models.readerImpls.JiraReader;
import com.jaiz.dailyreport.models.readerImpls.PreDealReader;

import java.io.*;
import java.util.Arrays;
import java.util.Calendar;

public class ReportGenerator {

    /**
     * 日报生成方法
     *
     * @throws IOException
     */
    public File genReport() throws IOException {
        // 确认模板文件是否存在
        confirmTemplateFiles();
        // 确认目标文件夹的存在
        confirmTargetDir();
        // 删除目标文件夹下14天前产生的.dr文件
        deleteExpiredReports();
        // 在目标文件夹下创建当天的日报文件
        return createNewRep();

    }

    /**
     * 确认模板文件是否存在
     * 如果不存在,在指定位置生成一套
     */
    private void confirmTemplateFiles() {
        //日报模板
        confirmTemplateFile(ConfigManager.getInstance().reportTemplateFile, Templates.DEFAULT_REPORT_TEMPLATE);
        //codeReview模板
        confirmTemplateFile(ConfigManager.getInstance().codeReviewTemplateFile, Templates.DEFAULT_CODE_REVIEW_TEMPLATE);
        //如果从preDeal中读取日报数据,检查preDeal模板文件是否存在
        if (!ConfigManager.getInstance().readDataFromJira) {
            confirmTemplateFile(ConfigManager.getInstance().preDealFile, Templates.DEFAULT_PREDEAL_TEMPLATE);
        }
    }

    /**
     * 保证单个模板文件及其父目录的存在
     *
     * @param reportTemplateFile
     * @param defaultReportTemplate
     */
    private void confirmTemplateFile(String reportTemplateFile, String defaultReportTemplate) {
        File templateFile = new File(reportTemplateFile);
        if (!templateFile.getParentFile().exists()) {
            templateFile.getParentFile().mkdirs();
        }
        if (!templateFile.exists()) {
            FileWriter writer = null;
            try {
                templateFile.createNewFile();
                writer = new FileWriter(templateFile);
                writer.write(defaultReportTemplate);
                writer.flush();
            } catch (IOException e) {
                System.out.println(templateFile.getName() + "输出失败");
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 创建当天的日报
     *
     * @throws IOException
     */
    private File createNewRep() throws IOException {

        ReportDataSourceReader dataSourceReader;
        if (ConfigManager.getInstance().readDataFromJira) {
            //分析jira
            System.out.println("今天将根据jira数据生成日报...");
            dataSourceReader = new JiraReader();
        } else {
            // 分析预处理文档
            System.out.println("今天将根据预处理文件生成日报...");
            dataSourceReader = new PreDealReader();
        }
        ReportFiller filler = dataSourceReader.read();
        if(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY){
            //周二抄送人附加code review对象
            String[] ccCopy=Arrays.copyOf(filler.getEmailCcRecievers(),filler.getEmailCcRecievers().length+1);
            ccCopy[ccCopy.length-1]=ConfigManager.getInstance().codeReviewReciever;
            filler.setEmailCcRecievers(ccCopy);
        }
        String newFileName = filler.getReportDate() + "日报.dr";
        File reportFile = new File(ConfigManager.getInstance().reportOutputPosition + newFileName);
        if (reportFile.exists()) {
            reportFile.delete();
        }
        System.out.println("输出日报...");
        reportFile.createNewFile();
        filler.fill(reportFile, ConfigManager.getInstance().reportTemplateFile, false);
        return reportFile;
    }

    /**
     * 删除过期的日报
     *
     * @param
     */
    private void deleteExpiredReports() {
        //仅获取尾缀为dr的文件
        File targetDir = new File(ConfigManager.getInstance().reportOutputPosition);
        File[] reps = targetDir.listFiles(
                //lambda表达式取代匿名内部类语法
                pathname -> pathname.getName().endsWith(".dr")
        );
        // 系统当前时间
        long curr = System.currentTimeMillis();
        for (File rep : reps) {
            // 该文件最后一次修改
            long lastModified = rep.lastModified();
            if (curr - lastModified >= ConfigManager.getInstance().reportRemainTime * 24 * 60 * 60 * 1000l) {
                // 判定为超期
                // 删除
                rep.delete();
            }
        }
    }

    /**
     * 确认目标目录的存在
     *
     * @throws IOException
     */
    private void confirmTargetDir() throws IOException {
        File docDir = new File(ConfigManager.getInstance().reportOutputPosition);
        // 是否存在我的文档
        if (!docDir.exists()) {
            docDir.mkdirs();
        }
    }

}
