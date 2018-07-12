package com.jaiz.dailyreport;

import java.io.File;
import java.io.IOException;

/**
 * Hello world!
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
