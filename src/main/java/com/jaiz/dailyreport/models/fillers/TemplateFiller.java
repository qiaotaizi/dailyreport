package com.jaiz.dailyreport.models.fillers;

import com.jaiz.dailyreport.annotations.FillerName;
import com.jaiz.utils.GlobalConstant;

import java.io.*;
import java.lang.reflect.Field;

/**
 * 模板填充
 */
public class TemplateFiller {

    /**
     * 将内容填充至日报模板
     *
     * @param reportFile
     * @param templateFileFullName
     * @param appendMode           是否开启附加模式
     */
    public void fill(File reportFile, String templateFileFullName, boolean appendMode) {
        //获取模板文件
        File templateFile = new File(templateFileFullName);
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(templateFile), GlobalConstant.DEFAULT_CHARSET));
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(reportFile,appendMode),GlobalConstant.DEFAULT_CHARSET));
            String line;
            //逐行读取
            //将可替换的值写入
            //将该行写入新的日报文件
            while ((line = reader.readLine()) != null) {
                Field[] fields = this.getClass().getDeclaredFields();
                for (Field f : fields) {
                    FillerName fillerName = f.getAnnotation(FillerName.class);
                    String fillStr;
                    if (!f.isAccessible()) {
                        f.setAccessible(true);
                    }
                    if (f.getType() == String.class) {
                        //String
                        fillStr = (String) f.get(this);
                    } else {
                        //String[]
                        fillStr = String.join(fillerName.joinSpliter(), (String[]) f.get(this));
                    }
                    line = line.replace("${" + fillerName.value() + "}", fillStr);
                }
                writer.write(line + "\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
