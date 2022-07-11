package com.mwz.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mwz.crm.base.BaseService;
import com.mwz.crm.dao.CustomerMapper;
import com.mwz.crm.dao.CustomerOrderMapper;
import com.mwz.crm.utils.AssertUtil;
import com.mwz.crm.vo.Customer;
import com.mwz.crm.vo.CustomerOrder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: yuuki
 * @Date: 2021-06-11 - 06 - 15:19
 * @Description: com.mwz.crm.service
 * @version: 1.0
 */
@Service
public class CustomerOrderService extends BaseService<CustomerOrder,Integer> {
    @Resource
    private CustomerOrderMapper customerOrderMapper;

    /**
     * 订单列表
     * @param cusId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Map<String, Object> queryByParams(Integer cusId) {
        Map<String, Object> map = new HashMap<>();
        List<CustomerOrder> customerOrderList=customerOrderMapper.selectAllByParams(cusId);
        PageInfo<CustomerOrder> pageInfo=new PageInfo<>(customerOrderList);
        map.put("code",0);
        map.put("msg","ok");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }
}
