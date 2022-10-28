package com.yqs.db.router;

import com.yqs.db.router.annotation.DBRouter;
import com.yqs.db.router.dynamic.DynamicDataSource;
import com.yqs.db.router.strategy.IDBRouterStrategy;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;


/**
 * 数据路由切面
 * @author 盐汽水
 */
@Aspect
public class DBRouterJoinPoint {

    private Logger logger = LoggerFactory.getLogger(DBRouterJoinPoint.class);
    private DBRouterConfig dbRouterConfig;

    private IDBRouterStrategy dbRouterStrategy;


    public DBRouterJoinPoint(DBRouterConfig dbRouterConfig, IDBRouterStrategy dbRouterStrategy) {
        this.dbRouterConfig = dbRouterConfig;
        this.dbRouterStrategy = dbRouterStrategy;
    }

    /**
     * 定义切入点
     * 当在方法或者是类上加上DBRouter注解的时候，切面生效
     */
    @Pointcut("@annotation(com.yqs.db.router.annotation.DBRouter)")
    public void aopPoint(){}

    @Around("aopPoint() && @annotation(dbRouter)")
    public Object doRouter(ProceedingJoinPoint jp, DBRouter dbRouter) throws Throwable{
        //计算分到某个库或表的字段
        String fieldKey = dbRouter.key();

        if(StringUtils.isBlank(fieldKey) && StringUtils.isBlank(dbRouterConfig.getRouterKey()) ){
            logger.error("annotation DBRouter key is null");
            throw new RuntimeException("annotation DBRouter key is null");
        }
        fieldKey = StringUtils.isNotBlank(fieldKey) ? fieldKey : dbRouterConfig.getRouterKey();
        //路由属性
        String dbKeyAttr = getAttrValue(fieldKey,jp.getArgs());
        dbRouterStrategy.doRouter(dbKeyAttr);
        try{

            return jp.proceed();
        }finally {
            dbRouterStrategy.clear();
        }
    }

    private String getAttrValue(String attr, Object[] args) {
        if (1 == args.length) {
            Object arg = args[0];
            if(arg instanceof String){
                return arg.toString();
            }
        }

        String filedValue = null;
        for(Object arg : args){
            try{
                if(StringUtils.isNotBlank(filedValue)){
                    break;
                }
                filedValue = BeanUtils.getProperty(arg,attr);
            }catch (Exception e){
                logger.error("获取路由属性值失败 attr：{}", attr, e);
            }
        }
        return filedValue;
    }
}
