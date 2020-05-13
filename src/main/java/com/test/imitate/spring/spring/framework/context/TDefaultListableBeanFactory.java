package com.test.imitate.spring.spring.framework.context;

import com.test.imitate.spring.spring.framework.beans.TBeanDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TDefaultListableBeanFactory extends TAbstractAppliactionContext {

    protected final Map<String, TBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String,TBeanDefinition>();


}
