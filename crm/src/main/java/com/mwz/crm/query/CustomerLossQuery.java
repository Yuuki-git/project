package com.mwz.crm.query;

import com.mwz.crm.base.BaseQuery;

/**
 * @author: yuuki
 * @Date: 2021-06-12 - 06 - 9:12
 * @Description: com.mwz.crm.query
 * @version: 1.0
 */
public class CustomerLossQuery extends BaseQuery {
    private String customerNo;

    private String customerName;

    private  Integer state;

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
