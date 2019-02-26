package com.jaiz.dailyreport.models;

import com.jaiz.dailyreport.config.ConfigManager;
import com.jaiz.dailyreport.models.fillers.CodeReviewFiller;
import com.jaiz.dailyreport.models.fillers.CodeReviewFillerFactory;

import java.io.File;

/**
 * 为日报附加code review模板
 */
public class CodeReviewAppender {


    /**
     * 附加code review模板
     *
     * @param reportFile
     */
    public void appendCodeReviewContent(File reportFile) {
        CodeReviewFiller filler = CodeReviewFillerFactory.createCodeReviewFiller();
        System.out.println("附加code review...");
        filler.fill(reportFile, ConfigManager.getInstance().codeReviewTemplateFile, true);
    }
}
