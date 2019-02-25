package com.jaiz.dailyreport.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用外置配置文件覆盖代码内的配置
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigCover {

    /**
     * 配置名称
     *
     * @return
     */
    String value();

    /**
     * 是否必须在配置文件中明确定义
     *
     * @return
     */
    boolean necessary() default false;
}
