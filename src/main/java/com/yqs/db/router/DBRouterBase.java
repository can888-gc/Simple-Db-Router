package com.yqs.db.router;

/**
 * @author 盐汽水
 */
public class DBRouterBase {

    private String tbIdx;

    public String getTbIdx() {
        return DBContextHolder.getTBKey();
    }
}
