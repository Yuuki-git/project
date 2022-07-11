package com.mwz.crm.dao;

import com.mwz.crm.base.BaseMapper;
import com.mwz.crm.vo.CustomerOrder;

import java.util.List;

public interface CustomerOrderMapper extends BaseMapper<CustomerOrder,Integer> {

    List<CustomerOrder> selectAllByParams(Integer cusId);
}