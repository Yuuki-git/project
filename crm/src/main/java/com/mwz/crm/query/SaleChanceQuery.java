package com.mwz.crm.query;

import com.mwz.crm.base.BaseQuery;
import com.mwz.crm.vo.SaleChance;

/**
 * @author: yuuki
 * @Date: 2021-05-29 - 05 - 10:55
 * @Description: com.mwz.crm.query
 * @version: 1.0
 */
public class SaleChanceQuery extends BaseQuery{
    //营销机会管理 查询
    private String customerName; // 客户名称
    private String createMan; // 创始人
    private Integer state; // 状态
    // 客户开发计划 查询
    private String devResult;
    private Integer assignMan;

    public String getDevResult() {
        return devResult;
    }

    public void setDevResult(String devResult) {
        this.devResult = devResult;
    }

    public Integer getAssignMan() {
        return assignMan;
    }

    public void setAssignMan(Integer assignMan) {
        this.assignMan = assignMan;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
