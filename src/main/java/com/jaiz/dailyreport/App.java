package com.jaiz.dailyreport;

import com.jaiz.dailyreport.config.ConfigManager;
import com.jaiz.dailyreport.exceptions.NullNecessaryConfigException;
import com.jaiz.dailyreport.models.ReportGenerator;

import java.io.File;
import java.io.IOException;

/**
 * 程序入口,在MF文件中配置
 * Main-Class: com.jaiz.dailyreport.App
 * @author graci
 *
 */
public class App {
	public static void main(String[] args) {

		//初始化配置项
		try {
			ConfigManager.configInit();
		} catch (NullNecessaryConfigException e) {
			System.out.println(e.getMessage());
			return;
		}

		ReportGenerator generator = new ReportGenerator();
		File tar;
		try {
			//生成文件
			tar=generator.genReport();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("日志文件无法创建");
			return;
		}
		// 生成完毕打开文件
		if (tar != null) {
			final Runtime rt = Runtime.getRuntime();
			try {
				rt.exec(ConfigManager.getInstance().openReportApp+" " + tar.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("文件打开失败");
			}
		}

	}
}
