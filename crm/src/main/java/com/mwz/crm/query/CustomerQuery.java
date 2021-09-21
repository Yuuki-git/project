package com.mwz.crm.query;

import com.mwz.crm.base.BaseQuery;

import java.util.Date;

/**
 * @author: yuuki
 * @Date: 2021-06-10 - 06 - 16:58
 * @Description: com.mwz.crm.query
 * @version: 1.0
 */
public class CustomerQuery extends BaseQuery {
    private String customerName;
    private String customerNo;
    private String level;
    private String minMoney;
    private String maxMoney;

    public String getMinMoney() {
        return minMoney;
    }

    public void setMinMoney(String minMoney) {
        this.minMoney = minMoney;
    }

    public String getMaxMoney() {
        return maxMoney;
    }

    public void setMaxMoney(String maxMoney) {
        this.maxMoney = maxMoney;
    }

    // public Date getMaxDate() {
    //     return maxDate;
    // }
    //
    // public void setMaxDate(Date maxDate) {
    //     this.maxDate = maxDate;
    // }
    //
    // public Date getMinDate() {
    //     return minDate;
    // }
    //
    // public void setMinDate(Date minDate) {
    //     this.minDate = minDate;
    // }

    private String maxDate;
    private String minDate;

    public String getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(String maxDate) {
        this.maxDate = maxDate;
    }

    public String getMinDate() {
        return minDate;
    }

    public void setMinDate(String minDate) {
        this.minDate = minDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
