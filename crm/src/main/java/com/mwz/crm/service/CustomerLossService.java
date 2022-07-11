package com.mwz.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mwz.crm.base.BaseService;
import com.mwz.crm.dao.CustomerLossMapper;
import com.mwz.crm.query.CustomerLossQuery;
import com.mwz.crm.vo.CustomerLoss;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: yuuki
 * @Date: 2021-06-12 - 06 - 9:00
 * @Description: com.mwz.crm.service
 * @version: 1.0
 */
@Service
public class CustomerLossService extends BaseService<CustomerLoss,Integer> {
    @Resource
    private CustomerLossMapper customerLossMapper;

    /**
     * 查询
     * @param customerLossQuery
     * @return map
     */
    public Map<String, Object> queryByParams(CustomerLossQuery customerLossQuery) {
        Map<String, Object> map =new HashMap<>();
        List<CustomerLoss> customerLossList=customerLossMapper.queryByParams(customerLossQuery);
        PageHelper.startPage(customerLossQuery.getPage(),customerLossQuery.getLimit());
        PageInfo<CustomerLoss> pageInfo=new PageInfo<>(customerLossList);
        map.put("code",0);
        map.put("msg","ok");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 客户流失分析
     * @return
     */
    public Map<String, Object> queryCustomerCountLossList() {
        Map<String, Object> map =new HashMap<>();
        List<Map<String, Object>> customerLossList=customerLossMapper.queryCustomerCountLossList();
        PageInfo<Map<String, Object>> pageInfo=new PageInfo<>(customerLossList);
        map.put("code",0);
        map.put("msg","ok");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }
}
