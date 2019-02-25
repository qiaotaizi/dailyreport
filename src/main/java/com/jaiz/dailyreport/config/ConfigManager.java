package com.jaiz.dailyreport.config;


import com.jaiz.dailyreport.annotations.ConfigCover;
import com.jaiz.dailyreport.exceptions.NullNecessaryConfigException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

public class ConfigManager {

    /**
     * 构造方法私有化
     */
    private ConfigManager() {
    }

    /**
     * jar包位置
     */
    private static final String JAR_DIR = Config.class.getResource("/").getPath();

    /**
     * 读取外置配置文件的位置和顺序
     */
    private static final String[] CONFIG_DIRS = {
            System.getProperty("user.home") + File.separator,
            System.getProperty("user.home") + File.separator + "Documents" + File.separator,
            System.getProperty("user.home") + File.separator + "Documents" + File.separator + "dailyReport" + File.separator,
            JAR_DIR};

    /**
     * 配置文件的文件名
     */
    private static final String CONFIG_FILE_NAME = "dailyReport.properties";

    /**
     * 全局配置实例
     */
    private static Config instance=new Config();

    /**
     * 单例模式配置管理器
     */
    public static Config getInstance() {
        return instance;
    }

    /**
     * 配置初始化方法
     */
    public static void configInit() throws NullNecessaryConfigException {
        System.out.println("jar包目录" + JAR_DIR);
        //查找配置文件dailyReport.properties
        //用文件中的配置替代Config默认配置
        //dailyReport.properties可能存在的位置(按照读取先后顺序):
        //操作系统~目录->~目录/Documents->~目录/Documents/dailyReport->jar包跟所在目录
        //后读取到的配置将覆盖之前读取到的配置
        for (String dirStr : CONFIG_DIRS) {
            File propertyFile = new File(dirStr + CONFIG_FILE_NAME);
            if (propertyFile.exists()) {
                Properties properties = new Properties();
                FileInputStream in = null;
                try {
                    in = new FileInputStream(propertyFile);
                    properties.load(in);
                    coverConfig(properties);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        //所有配置覆盖完毕
        //检查是否存在未覆盖的必须填写的项
        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field f : fields) {
            try {
                ConfigCover annotation = f.getAnnotation(ConfigCover.class);
                if (annotation.necessary() && f.get(instance) == null) {
                    //必填且未填
                    //根据是否读取jira数据来进行判断
                    if (instance.readDataFromJira) {
                        //读取jira数据,所有未填写的必填项均检查
                        throw new NullNecessaryConfigException(annotation.value() + "尚未设定");
                    } else if (!annotation.value().startsWith("jira.")) {
                        //不读取jira数据,只检查非jira.开头的必填项
                        throw new NullNecessaryConfigException(annotation.value() + "尚未设定");
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 使用properties中的配置覆盖代码中的配置
     *
     * @param properties
     */
    private static void coverConfig(Properties properties) {
        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field f : fields) {
            ConfigCover annotation = f.getAnnotation(ConfigCover.class);
            String propertyName = annotation.value();
            String propertyValue = properties.getProperty(propertyName);
            if (propertyValue != null) {
                try {

                    if (f.getType() == int.class) {
                        f.setInt(instance, Integer.parseInt(propertyValue));
                    } else if (f.getType() == boolean.class) {
                        f.setBoolean(instance, Boolean.parseBoolean(propertyValue));
                    } else if (f.getType() == byte.class) {
                        f.setByte(instance, Byte.parseByte(propertyValue));
                    } else if (f.getType() == char.class) {
                        f.setChar(instance, propertyValue.charAt(0));
                    } else if (f.getType() == double.class) {
                        f.setDouble(instance, Double.parseDouble(propertyValue));
                    } else if (f.getType() == float.class) {
                        f.setFloat(instance, Float.parseFloat(propertyValue));
                    } else if (f.getType() == long.class) {
                        f.setLong(instance, Long.parseLong(propertyValue));
                    } else if (f.getType() == short.class) {
                        f.setShort(instance, Short.parseShort(propertyValue));
                    } else {
                        f.set(instance, propertyValue);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Config c = ConfigManager.getInstance();
        System.out.println(c.jiraLogin);
        System.out.println(c.jiraLoginUsername);
        System.out.println(c.jiraLoginPassword);
        System.out.println(c.readDataFromJira);
    }
}
