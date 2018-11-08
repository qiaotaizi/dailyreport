package com.jaiz.dailyreport;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReportGenerator {

	private final String partSepa = "----------------------------------------------------------------------------------------------------------------------"+System.lineSeparator();

	/**
	 * 日报目标生成位置
	 */
	private File targetDir;

	/**
	 * 最终生成的文件
	 */
	private File targetFile;

	public File getTargetFile() {
		return targetFile;
	}

	/**
	 * 默认日报过期可删除时间(天)
	 */
	private int defaultExpireDay = 14;

	/**
	 * 日报生成方法
	 *
	 * @throws IOException
	 */
	public void genReport() throws IOException {
		// 确认目标文件夹的存在
		confirmTargetDir();
		// 删除目标文件夹下14天前产生的文件
		deleteExpiredReports(defaultExpireDay);
		// 在目标文件夹下创建当天的日报文件
		createNewRep();

	}

	/**
	 * 创建当天的日报
	 * 
	 * @throws IOException
	 */
	private void createNewRep() {

		// 分析预处理文档
		PreDealContent pre = analysePreDealContent();
		// 预处理文档内容填充至自己的位置
		String achievement = "1.xxxxA jira地址链接A" + System.lineSeparator();
		String problem = "1.无" + System.lineSeparator();
		if (pre.getAchievement().size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (String ach : pre.getAchievement()) {
				sb.append(ach).append(System.lineSeparator());
			}
			achievement = sb.toString();
		}
		String todo = achievement;
		if (pre.getProblem().size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (String pro : pre.getProblem()) {
				sb.append(pro).append(System.lineSeparator());
			}
			problem = sb.toString();
		}
		if (pre.getTodo().size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (String td : pre.getTodo()) {
				sb.append(td).append(System.lineSeparator());
			}
			todo = sb.toString();
		}

		Calendar curr = Calendar.getInstance();
		String date = curr.get(Calendar.YEAR) + "年" + (curr.get(Calendar.MONTH) + 1) + "月" + curr.get(Calendar.DATE)
				+ "日";
		String newFileName = date + "日报模板.dr";
		targetFile = new File(targetDir.getAbsolutePath() + File.separator + newFileName);
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(targetFile));
			out.write("收件人：" + System.lineSeparator() + "lingyun.shan@ttpai.cn");
			out.write(System.lineSeparator());
			out.write(System.lineSeparator());
			out.write("抄送：" + System.lineSeparator() + "xiqiang.zhao@ttpai.cn,miao.jiang@ttpai.cn");
			out.write(System.lineSeparator());
			out.write(System.lineSeparator());
			out.write("主题："+System.lineSeparator()+"平台技术部日报 - " + date + " - 姜志恒");
			out.write(System.lineSeparator());
			out.write(System.lineSeparator());
			out.write("内容：");
			out.write(System.lineSeparator() + System.lineSeparator());
			out.write("平台技术部日报 - " + date + " - 姜志恒");
			out.write(System.lineSeparator());
			out.write(partSepa);
			out.write("当天工作成果");
			out.write(System.lineSeparator());
			out.write(achievement);
			out.write(partSepa);
			out.write("碰到问题&解决方案");
			out.write(System.lineSeparator());
			out.write(problem);
			out.write(partSepa);
			out.write("明天工作计划");
			out.write(System.lineSeparator());
			out.write(todo);
			out.write(System.lineSeparator());
			out.write("//Code Review:");
			out.write(System.lineSeparator());
			out.write("//\t1.项目：{项目名称，git的项目名称}");
			out.write(System.lineSeparator());
			out.write(
					"//\t类：{类的完整路径，如：src/main/java/com/ttpai/api/boss/successauction/service/BossSuccessAuctionService.java}");
			out.write(System.lineSeparator());
			out.write(
					"//\tphabricator地址：{code view地址，如：http://phabricator.ttpai.cn/R8:44ac33fa47d94c349669476bd065d871b956d69f#inline-107}");
			out.write(System.lineSeparator());
			out.write("//\t 问题描述：{问题的简单描述}");
			out.write(System.lineSeparator());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 分析预处理文档
	 * 
	 * @return
	 * @throws IOException
	 */
	private PreDealContent analysePreDealContent() {
		PreDealContent result = new PreDealContent();
		List<String> achievement = new ArrayList<>();
		List<String> problem = new ArrayList<>();
		List<String> todo = new ArrayList<>();
		File prefile = new File(targetDir.getAbsolutePath() + File.separator + "PreDeal.pre");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(prefile), "UTF-8"));

			String line;
			List<String> target = null;
			while ((line = reader.readLine()) != null) {
				line=line.trim();
				if ("".equals(line)) {
					// 空行不处理
					continue;
				}
				char char0 = line.charAt(0);
				switch (char0) {
				case 'a':// 成果
					target = achievement;
					continue;
				case 'b':// 问题
					target = problem;
					continue;
				case 'c':// 计划
					target = todo;
					continue;
				}
				target.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		result.setAchievement(achievement);
		result.setProblem(problem);
		result.setTodo(todo);
		return result;
	}

	/**
	 * 删除过期的日报
	 *
	 * @param expireDay
	 */
	private void deleteExpiredReports(int expireDay) {
		//仅获取尾缀为dr的文件
		File[] reps = targetDir.listFiles(
				//lambda表达式取代匿名内部类语法
				pathname -> pathname.getName().endsWith(".dr")
		);
		// 系统当前时间
		long curr = System.currentTimeMillis();
		for (File rep : reps) {
			// 该文件最后一次修改
			long lastModified = rep.lastModified();
			if (curr - lastModified >= expireDay * 24 * 60 * 60 * 1000l) {
				// 判定为超期
				// 删除
				rep.delete();
			}
		}
	}

	/**
	 * 确认目标目录的存在 第一默认位置为系统用户目录\\Documents\\dailyReport 第二默认位置为用户目录\\dailyReport
	 * 
	 * @throws IOException
	 */
	private void confirmTargetDir() throws IOException {
		// 系统换行符
		String sep = File.separator;
		// 用户目录
		String userHome = System.getProperty("user.home");
		// 用户目录下的Documents目录
		String docDir = "Documents";
		// 生成日报的目录
		String repDir = "dailyReport";
		File docDir_ = new File(userHome + sep + docDir);
		// 是否存在我的文档
		if (docDir_.exists()) {
			// 存在,以其下的dailyReport为目标目录
			targetDir = new File(userHome + sep + docDir + sep + repDir);
			if (!targetDir.exists()) {
				targetDir.mkdir();
			}
		} else {
			// 不存在,以home下的dailyReport为目标目录
			targetDir = new File(userHome + sep + repDir);
			if (!targetDir.exists()) {
				targetDir.mkdir();
			}
		}
	}

}
