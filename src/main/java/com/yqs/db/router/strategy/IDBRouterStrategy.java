package com.yqs.db.router.strategy;

/**
 * 路由策略
 * @author 盐汽水
 */
public interface IDBRouterStrategy {

    /**
     * 路由计算
     * @param dbKeyAttr
     */
    void doRouter(String dbKeyAttr);

    /**
     * 手动设置分库路由
     * @param dbIdx
     */
    void setDBKey(int dbIdx);

    /**
     * 手动设置分表路由
     * @param tbIdx
     */
    void setTBKey(int tbIdx);

    /**
     * 获取分库数
     * @return
     */
    int dbCount();

    /**
     * 获取分表库
     * @return
     */
    int tbCount();

    /**
     * 清除路由
     */
    void clear();
}
