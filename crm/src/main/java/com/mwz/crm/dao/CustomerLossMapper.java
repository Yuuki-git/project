package com.mwz.crm.dao;

import com.mwz.crm.base.BaseMapper;
import com.mwz.crm.query.CustomerLossQuery;
import com.mwz.crm.vo.CustomerLoss;

import java.util.List;
import java.util.Map;

public interface CustomerLossMapper extends BaseMapper<CustomerLoss,Integer> {

    List<CustomerLoss> queryByParams(CustomerLossQuery customerLossQuery);

    List<Map<String, Object>> queryCustomerCountLossList();
}