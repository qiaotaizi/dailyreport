package com.jaiz.dailyreport.models;

import com.jaiz.dailyreport.models.fillers.ReportFiller;

public interface ReportDataSourceReader {

    ReportFiller read();

}
