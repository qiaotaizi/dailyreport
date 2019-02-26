package com.jaiz.dailyreport.models.fillers;

import com.jaiz.dailyreport.annotations.FillerName;

public class CodeReviewFiller extends TemplateFiller {

    @FillerName("date01")
    private String date01;

    @FillerName("date02")
    private String date02;

    @FillerName("date03")
    private String date03;

    @FillerName("date04")
    private String date04;

    @FillerName("date05")
    private String date05;

    public String getDate01() {
        return date01;
    }

    public void setDate01(String date01) {
        this.date01 = date01;
    }

    public String getDate02() {
        return date02;
    }

    public void setDate02(String date02) {
        this.date02 = date02;
    }

    public String getDate03() {
        return date03;
    }

    public void setDate03(String date03) {
        this.date03 = date03;
    }

    public String getDate04() {
        return date04;
    }

    public void setDate04(String date04) {
        this.date04 = date04;
    }

    public String getDate05() {
        return date05;
    }

    public void setDate05(String date05) {
        this.date05 = date05;
    }
}
