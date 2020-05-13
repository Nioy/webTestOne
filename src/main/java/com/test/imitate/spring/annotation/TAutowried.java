package com.test.imitate.spring.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TAutowried {

    String values() default "";

}
