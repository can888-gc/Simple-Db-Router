package com.yqs.db.router.strategy.impl;

import com.yqs.db.router.DBContextHolder;
import com.yqs.db.router.DBRouterConfig;
import com.yqs.db.router.strategy.IDBRouterStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通过Hash的方式设置分到某个表某个库
 */
public class DBRouterStrategyHashCode implements IDBRouterStrategy {

    private Logger logger = LoggerFactory.getLogger(DBRouterStrategyHashCode.class);

    private DBRouterConfig dbRouterConfig;

    public DBRouterStrategyHashCode(DBRouterConfig dbRouterConfig) {
        this.dbRouterConfig = dbRouterConfig;
    }

    @Override
    public void doRouter(String dbKeyAttr) {
        //总表数
        int size = dbRouterConfig.getDbCount() * dbRouterConfig.getTbCount();

        int idx = (size - 1) & (dbKeyAttr.hashCode() ^ (dbKeyAttr.hashCode()) >>> 16);

        int dbIdx = idx / dbRouterConfig.getTbCount() + 1;
        int tbIdx = idx - dbRouterConfig.getTbCount() * (dbIdx - 1);

        DBContextHolder.setDbKey(String.format("%02d",dbIdx));
        DBContextHolder.setTbKey(String.format("%03d",tbIdx));

        logger.debug("数据库路由 dbIdx：{} tbIdx：{}",  dbIdx, tbIdx);
    }

    @Override
    public void setDBKey(int dbIdx) {
        DBContextHolder.setDbKey(String.format("%02d",dbIdx));
    }

    @Override
    public void setTBKey(int tbIdx) {
        DBContextHolder.setTbKey(String.format("%03d",tbIdx));
    }

    @Override
    public int dbCount() {
        return dbRouterConfig.getDbCount();
    }

    @Override
    public int tbCount() {
        return dbRouterConfig.getTbCount();
    }

    @Override
    public void clear() {
        DBContextHolder.clearDBKey();
        DBContextHolder.clearTBKey();
    }
}
