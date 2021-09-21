package com.mwz.crm.dao;

import com.mwz.crm.base.BaseMapper;
import com.mwz.crm.vo.OrderDetails;

import java.util.List;

public interface OrderDetailsMapper extends BaseMapper<OrderDetails,Integer> {

    List<OrderDetails> queryOrderDetail(Integer orderId);

    Integer[] querySumById(Integer id);
}