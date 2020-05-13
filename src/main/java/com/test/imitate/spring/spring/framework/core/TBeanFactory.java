package com.test.imitate.spring.spring.framework.core;

public interface TBeanFactory {

    Object getBean(String beanName) throws Exception;

    public Object getBean(Class<?> beanClass) throws Exception;

}
