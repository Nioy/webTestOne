package com.test.imitate.spring.spring.framework.context;

import com.test.imitate.spring.spring.framework.beans.TBeanDefinition;
import com.test.imitate.spring.spring.framework.beans.TBeanPostProcessor;
import com.test.imitate.spring.spring.framework.beans.TBeanWrapper;
import com.test.imitate.spring.spring.framework.core.TBeanFactory;
import com.test.imitate.spring.spring.framework.support.TBeanDefinitionReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class TApplicationContext extends TDefaultListableBeanFactory implements TBeanFactory {

    private String[] configLocations;
    private TBeanDefinitionReader reader;

    private Map<String,Object> singletonBeanCacheMap = new HashMap<String, Object>();
    private Map<String,TBeanWrapper> beanWrapperMap = new ConcurrentHashMap<String, TBeanWrapper>();

    private Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>();
    private Map<String,TBeanWrapper> factoryBeanInstance = new ConcurrentHashMap<String, TBeanWrapper>();

    public TApplicationContext(String... configLocations){
        this.configLocations = configLocations;
        try {
            refresh();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 在此加载配置文件
     * @throws Exception
     */
    @Override
    public void refresh() throws Exception{

        //1、定位，定位配置文件
        reader = new TBeanDefinitionReader(configLocations);
        //2、加载，加载配置文件，扫描相关类，将配置信息封装成beandefinition
        List<TBeanDefinition> beanDefinitions =  reader.loadBeanDefinitions();
        //3、注册，将配置信息放到容器中（伪IoC容器）
        doRegisterBeanDefinition(beanDefinitions);
        //4、把不是延时加载的类，提前初始化
        doAutoWrited();
    }

    private void doAutoWrited() {
        for (Map.Entry<String, TBeanDefinition> entrySet : super.beanDefinitionMap.entrySet()) {
            String beanName = entrySet.getKey();
            if(!entrySet.getValue().isLazyInit()){
                try {
                    getBean(beanName);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void doRegisterBeanDefinition(List<TBeanDefinition> beanDefinitions) throws Exception {
        if(beanDefinitions==null){return;}
        for(TBeanDefinition beanDefinition:beanDefinitions){
            if(super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())){
                throw new Exception("the "+beanDefinition.getFactoryBeanName()+" is exists!");
            }
            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(),beanDefinition);
        }
    }

    @Override
    public Object getBean(String beanName) throws Exception {
        TBeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
        try{
            TBeanPostProcessor beanPostProcessor = new TBeanPostProcessor();
            instantiateBean(beanDefinition);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private Object instantiateBean(TBeanDefinition beanDefinition) {
        Object instance = null;
        String className = beanDefinition.getBeanClassName();
        try{
            if(this.singletonBeanCacheMap.containsKey(className)){
                instance = this.singletonBeanCacheMap.get(className);
            }else{
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();
                this.singletonBeanCacheMap.put(beanDefinition.getFactoryBeanName(),instance);
            }
            return instance;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object getBean(Class<?> beanClass) throws Exception {
        return getBean(beanClass.getName());
    }

    public String[] getBeanDefinitionNames(){
        return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }

    public int getBeanDefinitionCnt(){
        return this.beanDefinitionMap.size();
    }

    public Properties getConfig(){
        return this.reader.getConfig();
    }
}
