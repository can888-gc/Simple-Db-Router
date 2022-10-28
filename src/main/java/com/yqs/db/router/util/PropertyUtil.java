package com.yqs.db.router.util;

import org.springframework.core.env.Environment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * 属性处理的工具类
 * @JueJin: https://juejin.cn/user/3096662251156334
 * @author 盐汽水
 */
public class PropertyUtil {

    /**
     * <link>此标记为SpringBoot的大版本号如：1.x和2.x</link>
     * 在加载属性的时候SpringBoot1.x和2.x使用的方法有些差异
     * 在获取属性的时候根据此版本号来区分代码
     */
    private static int springBootVersion = 1;

    static {
        try{
            Class.forName("org.springframework.boot.bind.RelaxedPropertyResolver");
        }catch (ClassNotFoundException e) {
            springBootVersion = 2;
        }
    }

    /**
     * Spring Boot 1.x和2.x使用的加载属性的代码有差异在这里区分
     * @param environment
     * @param path
     * @param clazz
     * @return
     * @param <T>
     */
    public static <T> T handle(Environment environment,String path,Class<T> clazz){
        switch (springBootVersion){
            case 1:
                return (T)v1(environment,path);
            case 2:
                return (T)v2(environment,path,clazz);
        }
        return null;
    }

    /**
     * Spring Boot 1.x使用的方法
     * @param environment
     * @param path
     * @return
     */
    private static Object v1(final Environment environment,final String path){
        return null;
    }

    /**
     * Spring Boot 2.x使用的方法
     * @param environment
     * @param path
     * @return
     */
    private static Object v2(final Environment environment,final String path,final Class<?> targetClass){
        try {
            Class<?> binderClass = Class.forName("org.springframework.boot.context.properties.bind.Binder");
            Method getMethod = binderClass.getDeclaredMethod("get", Environment.class);
            Method bindMethod = binderClass.getDeclaredMethod("bind", String.class, Class.class);
            Object bindObject = getMethod.invoke(null, environment);
            String prefixParam = path.endsWith(".") ? path.substring(0,path.length() - 1) : path;
            Object bindResultObject = bindMethod.invoke(bindObject, prefixParam, targetClass);
            Method resultGetMethod = bindResultObject.getClass().getDeclaredMethod("get");
            return resultGetMethod.invoke(bindResultObject);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
