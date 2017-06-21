package com.example.as.ico;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by as on 2017/6/21.
 */
//@Target(ElementType.METHOD)表示注解的位置   ElementType.METHOD表示需要注解时方法
    //@Retention(RetentionPolicy.RUNTIME)表示该注解什么时候运行
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckNet {
    String value();
}
