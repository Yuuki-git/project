package com.mwz.crm.dao;

import com.mwz.crm.base.BaseMapper;
import com.mwz.crm.query.CustomerServeQuery;
import com.mwz.crm.vo.CustomerServe;

import java.util.List;

public interface CustomerServeMapper extends BaseMapper<CustomerServe,Integer> {

    List<CustomerServe> queryByParams(CustomerServeQuery customerServeQuery);
}