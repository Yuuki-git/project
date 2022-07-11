package com.mwz.crm.query;

import com.mwz.crm.base.BaseQuery;

/**
 * @author: yuuki
 * @Date: 2021-06-12 - 06 - 11:30
 * @Description: com.mwz.crm.query
 * @version: 1.0
 */
public class CustomerServeQuery extends BaseQuery {
    private String customerName;
    private String serveType;
    private String state;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getServeType() {
        return serveType;
    }

    public void setServeType(String serveType) {
        this.serveType = serveType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
