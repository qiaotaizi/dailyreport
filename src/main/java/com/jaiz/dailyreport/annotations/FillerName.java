package com.jaiz.dailyreport.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 模板填充物名
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FillerName {

    String value();

    /**
     * 对于String[]类型的填充物,需要设置连接符
     * @return
     */
    String joinSpliter() default "";

}
