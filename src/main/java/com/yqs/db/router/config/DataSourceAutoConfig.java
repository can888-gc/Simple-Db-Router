package com.yqs.db.router.config;

import com.yqs.db.router.DBRouterConfig;
import com.yqs.db.router.DBRouterJoinPoint;
import com.yqs.db.router.dynamic.DynamicDataSource;
import com.yqs.db.router.dynamic.DynamicMybatisPlugin;
import com.yqs.db.router.strategy.IDBRouterStrategy;
import com.yqs.db.router.strategy.impl.DBRouterStrategyHashCode;
import com.yqs.db.router.util.PropertyUtil;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 数据库路由自动配置类
 * @author 盐汽水
 */
public class DataSourceAutoConfig implements EnvironmentAware {

    /**
     * 数据库列表的分割符
     */
    private static final String DATA_SOURCE_SPLIT_CHAR = ",";

    /**
     * 分库数量
     */
    private int dbCount;

    /**
     * 分表数量
     */
    private int tbCount;

    /**
     * 路由字段
     */
    private String routerKey;

    /**
     * 数据源配置组
     */
    private Map<String, Map<String, Object>> dataSourceMap = new HashMap<>();
    /**
     * 默认数据源配置
     */
    private Map<String, Object> defaultDataSourceConfig;

    @Bean(name = "db-router-point")
    @ConditionalOnMissingBean
    public DBRouterJoinPoint point(DBRouterConfig dbRouterConfig, IDBRouterStrategy dbRouterStrategy){
        return new DBRouterJoinPoint(dbRouterConfig,dbRouterStrategy);
    }

    @Bean
    public DBRouterConfig dbRouterConfig(){
        return new DBRouterConfig(dbCount,tbCount,routerKey);
    }

    @Bean
    public Interceptor plugin(){
        return new DynamicMybatisPlugin();
    }

    @Bean
    public IDBRouterStrategy dbRouterStrategy(DBRouterConfig dbRouterConfig){
        return new DBRouterStrategyHashCode(dbRouterConfig);
    }

    @Bean
    public TransactionTemplate transactionTemplate(DataSource dataSource){
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);

        TransactionTemplate transactionTemplate = new TransactionTemplate();
        transactionTemplate.setTransactionManager(dataSourceTransactionManager);
        transactionTemplate.setPropagationBehaviorName("PROPAGATION_REQUIRED");
        return transactionTemplate;
    }

    @Bean
    public DataSource dataSource(){
        Map<Object,Object> targetDataSource = new HashMap<>();
        for(String dbInfo : dataSourceMap.keySet()){
            Map<String, Object> objMap = dataSourceMap.get(dbInfo);
            targetDataSource.put(dbInfo,new DriverManagerDataSource(
                    objMap.get("url").toString(),
                    objMap.get("username").toString(),
                    objMap.get("password").toString()
            ));
        }
        //设置默认数据源
        DynamicDataSource defaultDataSource = new DynamicDataSource();
        defaultDataSource.setTargetDataSources(targetDataSource);
        defaultDataSource.setDefaultTargetDataSource(new DriverManagerDataSource(
                defaultDataSourceConfig.get("url").toString(),
                defaultDataSourceConfig.get("username").toString(),
                defaultDataSourceConfig.get("password").toString()
        ));

        return defaultDataSource;
    }

    @Override
    public void setEnvironment(Environment environment) {
        String prefix = "db-router.db.datasource.";
        //获取数据库的数量
        dbCount = Integer.valueOf(environment.getProperty(prefix + "dbCount"));
        //获取表的数量
        tbCount = Integer.valueOf(environment.getProperty(prefix + "tbCount"));
        //获取出数据源列表
        String dataSource = environment.getProperty(prefix + "list");
        //如果数据源列表是空的，则停止往下执行
        assert dataSource != null;

        //获取数据源的配置
        for(String dbInfo : dataSource.split(DATA_SOURCE_SPLIT_CHAR)){
            Map<String, Object> dataSourceProps = PropertyUtil.handle(environment, prefix + dbInfo, Map.class);
            dataSourceMap.put(dbInfo,dataSourceProps);
        }
        //获取出默认的数据源
        String defaultDataSource = environment.getProperty(prefix + "default");
        defaultDataSourceConfig = PropertyUtil.handle(environment,prefix + defaultDataSource, Map.class);
    }
}
