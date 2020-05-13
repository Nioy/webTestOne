package com.test.imitate.spring.spring.framework.support;

import com.test.imitate.spring.spring.framework.beans.TBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TBeanDefinitionReader {

    private List<String> registryBeanClass = new ArrayList<String>();
    private Properties properties = new Properties();
    private final String SCAN_PACKAGE = "scanPackage";

    public TBeanDefinitionReader(String... locations){
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(locations[0].replace("classpath:", ""));
        try {
            properties.load(is);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        doScanner(properties.getProperty(SCAN_PACKAGE));
    }

    private void doScanner(String property) {
        URL url = this.getClass().getClassLoader().getResource("/" + property.replace("\\.", "/"));
        File classPath = new File(url.getFile());
        for(File file:classPath.listFiles()){
            if(file.isDirectory()){
                doScanner(property+"."+file.getName());
            }else{
                if(!file.getName().endsWith(".class")){continue;}
                String className = property+"."+file.getName().replace(".class","");
                registryBeanClass.add(className);
            }
        }
    }


    public Properties getConfig(){
        return this.properties;
    }

    public List<TBeanDefinition> loadBeanDefinitions() {
        if(registryBeanClass==null||registryBeanClass.size()<=0){return null;}
        List<TBeanDefinition> result = new ArrayList<TBeanDefinition>();
        try {
            for(String className:registryBeanClass){
                Class<?> clazz = Class.forName(className);
                if(clazz.isInterface()){
                    continue;
                }
                TBeanDefinition beanDefinition = doCreateBeanDefinition(toLowerFirstCase(clazz.getSimpleName()), clazz.getName());
                result.add(beanDefinition);
                Class<?>[] interfaces = clazz.getInterfaces();
                for(Class<?> i:interfaces){
                    result.add(doCreateBeanDefinition(i.getName(),clazz.getName()));
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    private TBeanDefinition doCreateBeanDefinition(String factoryBeanName, String beanClassName) {
        TBeanDefinition beanDefinition = new TBeanDefinition();
        beanDefinition.setFactoryBeanName(factoryBeanName);
        beanDefinition.setBeanClassName(beanClassName);
        return beanDefinition;
    }

    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
