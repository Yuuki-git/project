package com.mwz.crm.service;

import com.github.pagehelper.PageInfo;
import com.mwz.crm.base.BaseMapper;
import com.mwz.crm.base.BaseService;
import com.mwz.crm.dao.OrderDetailsMapper;
import com.mwz.crm.vo.OrderDetails;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: yuuki
 * @Date: 2021-06-11 - 06 - 17:02
 * @Description: com.mwz.crm.service
 * @version: 1.0
 */
@Service
public class OrderDetailsService extends BaseService<OrderDetails,Integer> {
    @Resource
    private OrderDetailsMapper orderDetailsMapper;

    public Map<String, Object> queryOrderDetail(Integer orderId) {
        Map<String,Object> map=new HashMap<>();
        List<OrderDetails> orderDetailsList=orderDetailsMapper.queryOrderDetail(orderId);
        PageInfo<OrderDetails> pageInfo=new PageInfo<>(orderDetailsList);
        map.put("code",0);
        map.put("msg","ok");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    public Integer querySumById(Integer id) {
        Integer[] sum =orderDetailsMapper.querySumById(id);
        Integer sum1=0;
        for(Integer sums:sum) {
            sum1=sums+sum1;
        }
        return sum1;
    }
}
