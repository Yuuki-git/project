package com.mwz.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mwz.crm.base.BaseService;
import com.mwz.crm.dao.CustomerLossMapper;
import com.mwz.crm.dao.CustomerServeMapper;
import com.mwz.crm.exceptions.NoLoginException;
import com.mwz.crm.query.CustomerServeQuery;
import com.mwz.crm.utils.AssertUtil;
import com.mwz.crm.utils.CookieUtil;
import com.mwz.crm.vo.Customer;
import com.mwz.crm.vo.CustomerServe;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: yuuki
 * @Date: 2021-06-12 - 06 - 11:36
 * @Description: com.mwz.crm.service
 * @version: 1.0
 */
@Service
public class CustomerServeService extends BaseService<CustomerServe,Integer> {
    @Resource
    private CustomerServeMapper customerServeMapper;


    /**
     * 查询
     * @param customerServeQuery
     * @return
     */
    public Map<String, Object> queryCustomerServeByParams(CustomerServeQuery customerServeQuery) {
        Map<String, Object> map=new HashMap<>();
        List<CustomerServe> customerServeList=customerServeMapper.queryByParams(customerServeQuery);
        PageHelper.startPage(customerServeQuery.getPage(),customerServeQuery.getLimit());
        PageInfo<CustomerServe> pageInfo=new PageInfo<>(customerServeList);
        map.put("code",0);
        map.put("msg","ok");
        map.put("count",pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }

    /**
     * 添加
     * @param customerServe
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCustomer(CustomerServe customerServe, HttpServletRequest request) {
        checkParams(customerServe);
        customerServe.setIsValid(1);
        customerServe.setCreateDate(new Date());
        customerServe.setUpdateDate(new Date());
        customerServe.setState("fw_001");
        customerServe.setCreatePeople(CookieUtil.getCookieValue(request,"userName"));
        AssertUtil.isTrue(customerServeMapper.insertSelective(customerServe)<1,"添加操作失败");
    }

    private void checkParams(CustomerServe customerServe) {
        AssertUtil.isTrue(StringUtils.isBlank(customerServe.getCustomer()),"客户名称不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServiceRequest()),"客户概要不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(customerServe.getOverview()),"服务内容不能为空");

    }

    /**
     * 添加
     * @param customerServe
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCustomer(CustomerServe customerServe) {
        Integer id=customerServe.getId();
        AssertUtil.isTrue(id==null||customerServeMapper.selectByPrimaryKey(id)==null,"待更新记录不存在");
        checkParams(customerServe);
        customerServe.setUpdateDate(new Date());
        if (customerServe.getState().equals("fw_001")) {
            AssertUtil.isTrue(customerServe.getCustomer() == null, "客户名称不能为空!");
            AssertUtil.isTrue(customerServeMapper.updateByPrimaryKeySelective(customerServe) < 1,"更新失败!");
        } else if (customerServe.getState().equals("fw_002")){
            customerServe.setAssigner(customerServe.getAssigner());
            customerServe.setAssignTime(new Date());
        } else if (customerServe.getState().equals("fw_003")){
            customerServe.setServiceProce(customerServe.getServiceProce());
            customerServe.setServiceProcePeople(customerServe.getServiceProcePeople());
            customerServe.setServiceProceTime(new Date());
        } else if (customerServe.getState().equals("fw_004")){
            customerServe.setServiceProceResult(customerServe.getServiceProceResult());
            customerServe.setMyd(customerServe.getMyd());
        }
        AssertUtil.isTrue(customerServeMapper.updateByPrimaryKeySelective(customerServe)<1,"更新操作失败");
    }
    /**
     * 服务状态更新操作
     * @param state
     * @param id
     * @param flag
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateState(String state, Integer id, String flag) throws NoLoginException {

        CustomerServe customerServe = selectByPrimaryKey(id);
        int i = Integer.parseInt(state.substring(state.length() - 1));
        if(flag.equals("0")){
            int temp = i +1;
            customerServe.setState("fw_00"+temp);
        } else if(flag.equals("1")){
            int temp = i -1;
            customerServe.setState("fw_00"+temp);
        } else {
            throw new NoLoginException();
        }
        customerServe.setUpdateDate(new Date());

        AssertUtil.isTrue(customerServeMapper.updateByPrimaryKeySelective(customerServe) < 1,"服务状态更新失败!");
    }
}

