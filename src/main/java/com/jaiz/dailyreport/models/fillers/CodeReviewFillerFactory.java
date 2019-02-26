package com.jaiz.dailyreport.models.fillers;

import java.util.Calendar;

public class CodeReviewFillerFactory {

    public static CodeReviewFiller createCodeReviewFiller() {
        CodeReviewFiller filler = new CodeReviewFiller();
        //今天:周二
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        //周一
        filler.setDate05(calendarToString(c));
        //上周五
        c.add(Calendar.DATE, -3);
        filler.setDate04(calendarToString(c));
        //上周四
        c.add(Calendar.DATE, -1);
        filler.setDate03(calendarToString(c));
        //上周三
        c.add(Calendar.DATE, -1);
        filler.setDate02(calendarToString(c));
        //上周二
        c.add(Calendar.DATE, -1);
        filler.setDate01(calendarToString(c));
        return filler;
    }

    /**
     * 按照MM.dd输出
     *
     * @param c
     * @return
     */
    private static String calendarToString(Calendar c) {
        return (c.get(Calendar.MONTH) + 1) + "." + c.get(Calendar.DAY_OF_MONTH);
    }

}
