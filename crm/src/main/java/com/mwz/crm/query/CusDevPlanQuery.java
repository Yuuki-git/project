package com.mwz.crm.query;

import com.mwz.crm.base.BaseQuery;

/**
 * @author: yuuki
 * @Date: 2021-06-01 - 06 - 10:31
 * @Description: com.mwz.crm.query
 * @version: 1.0
 */
public class CusDevPlanQuery extends BaseQuery {
    public Integer getSaleChanceId() {
        return saleChanceId;
    }

    public void setSaleChanceId(Integer saleChanceId) {
        this.saleChanceId = saleChanceId;
    }

    private Integer saleChanceId; //  营销机会主键

}
