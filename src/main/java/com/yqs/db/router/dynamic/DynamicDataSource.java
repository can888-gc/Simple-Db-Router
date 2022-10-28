package com.yqs.db.router.dynamic;

import com.yqs.db.router.DBContextHolder;
import com.yqs.db.router.strategy.impl.DBRouterStrategyHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author 盐汽水
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private Logger logger = LoggerFactory.getLogger(DynamicDataSource.class);

    /**
     * 当在open connection时会执行此方法
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        logger.info("切换数据源:{}","db" + DBContextHolder.getDBKey());
        return "db" + DBContextHolder.getDBKey();
    }
}
