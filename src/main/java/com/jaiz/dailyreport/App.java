package com.jaiz.dailyreport;

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

		ReportGenerator generator = new ReportGenerator();
		try {
			generator.genReport();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("日志文件无法创建");
			return;
		}
		// 生成完毕打开文件
		File tar = generator.getTargetFile();
		if (tar != null) {
			final Runtime rt = Runtime.getRuntime();
			try {
				rt.exec("notepad " + tar.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("文件打开失败");
			}
		}

	}
}
