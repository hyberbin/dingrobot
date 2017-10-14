package org.jplus.robot;

import com.alibaba.fastjson.JSON;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hyberbin on 2016/10/30.
 */
@Service
public class SpringContextUtil implements ApplicationContextAware {

    /**
     * 设置为静态变量
     */
    private static ApplicationContext applicationContext;
    private static final Map<String,Object> beanMap=new HashMap<>();

    /**
     * 获取ApplicationContext.
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {

        SpringContextUtil.applicationContext = applicationContext;
    }

    public static void initSpelVars(){
        String[] definitionNames = applicationContext.getBeanDefinitionNames();
        for(String name:definitionNames){
            if(!name.contains(".")){
                beanMap.put(name,applicationContext.getBean(name));
            }
        }
        System.out.println("加载SpelVars:"+beanMap.size());
    }

    /**
     * 根据bean name获取bean
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    /**
     * 根据bean name获取bean，在spring容器初始化过程中使用
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name, ApplicationContext applicationContext) {
        return (T) applicationContext.getBean(name);
    }

    /**
     * 根据bean class获取bean,如果多个符合则取第一个
     */
    public static <T> T getBean(Class<T> clazz) {
        Map<String, T> beanMaps = applicationContext.getBeansOfType(clazz);
        if (beanMaps != null && !beanMaps.isEmpty()) {
            return beanMaps.values().iterator().next();
        } else {
            return null;
        }
    }

    /**
     * 根据bean class获取bean,如果多个符合则取第一个
     */
    public static <C> Collection<C> getBeans(Class<C> clazz) {
        Map<String, C> beanMaps = applicationContext.getBeansOfType(clazz);
        if (beanMaps != null && !beanMaps.isEmpty()) {
            return beanMaps.values();
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * 根据bean class获取bean,如果多个符合则取第一个，在spring容器初始化过程中使用
     */
    public static <T> T getBean(Class<T> clazz, ApplicationContext applicationContext) {
        Map<String, T> beanMaps = applicationContext.getBeansOfType(clazz);
        if (beanMaps != null && !beanMaps.isEmpty()) {
            return beanMaps.values().iterator().next();
        } else {
            return null;
        }
    }

    public static Map<String,Object> getBeanMap(){
        return beanMap;
    }
}