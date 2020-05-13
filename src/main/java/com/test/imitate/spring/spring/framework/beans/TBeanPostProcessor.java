package com.test.imitate.spring.spring.framework.beans;

public class TBeanPostProcessor {

    //前置处理器
    public Object postProcessBeforeInitialization(Object bean,String beanName) throws Exception{
        return bean;
    }

    //后置处理器
    public Object postProcessAfterInitialization(Object bean,String beanName) throws  Exception{
        return bean;
    }

}
