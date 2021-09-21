package com.mwz.crm.dao;

import com.mwz.crm.base.BaseMapper;
import com.mwz.crm.query.CustomerQuery;
import com.mwz.crm.vo.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerMapper extends BaseMapper<Customer,Integer> {

    List<Customer> queryByParams(CustomerQuery customerQuery);

    Customer queryByCustomerName(String name);

    List<Map<String, Object>> queryCountMake();

    List<Map<String, Object>> queryCustomerContributionByParams(CustomerQuery customerQuery);
}