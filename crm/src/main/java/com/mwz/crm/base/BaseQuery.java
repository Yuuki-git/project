package com.mwz.crm.base;


public class BaseQuery {
    private Integer page=1; // 当前页
    private Integer limit=10; // 每页显示的条数

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
