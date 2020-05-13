package com.test.imitate.spring.annotation;


import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TResultMapping {

    String value() default "";

}
