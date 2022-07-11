package com.mwz.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mwz.crm.base.BaseService;
import com.mwz.crm.dao.CustomerMapper;
import com.mwz.crm.query.CustomerQuery;
import com.mwz.crm.utils.AssertUtil;
import com.mwz.crm.utils.PhoneUtil;
import com.mwz.crm.vo.Customer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author: yuuki
 * @Date: 2021-06-10 - 06 - 16:50
 * @Description: com.mwz.crm.service
 * @version: 1.0
 */
@Service
public class CustomerService extends BaseService<Customer,Integer> {
    @Resource
    private CustomerMapper customerMapper;

    /**
     * 查询
     * @param customerQuery
     * @return
     */
    public Map<String, Object> queryByParams(CustomerQuery customerQuery) {
        Map<String,Object> map=new HashMap<>();
        List<Customer> customerList=customerMapper.queryByParams(customerQuery);
        PageHelper.startPage(customerQuery.getPage(),customerQuery.getLimit());
        PageInfo<Customer> pageInfo=new PageInfo<>(customerList);
        map.put("code",0);
        map.put("msg","ok");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 添加操作
     * 1.参数校验
     * 2.设置默认值
     * 3.执行操作
     * @param customer
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCustomer(Customer customer) {
        checkCustomerParams(customer.getName(),customer.getPhone(),customer.getFr(),customer.getLevel());
        Customer temp=customerMapper.queryByCustomerName(customer.getName());
        AssertUtil.isTrue(null!=temp,"客户已存在");
        customer.setIsValid(1);
        customer.setState(0);
        customer.setCreateDate(new Date());
        customer.setUpdateDate(new Date());
        customer.setKhno("KH"+System.currentTimeMillis());
        AssertUtil.isTrue(customerMapper.insertSelective(customer)<1,"添加操作失败");
    }

    private void checkCustomerParams(String name,String phone, String fr, String level) {

        AssertUtil.isTrue(StringUtils.isBlank(name),"客户名称不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"联系电话不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"电话格式不正确");
        AssertUtil.isTrue(StringUtils.isBlank(fr),"法人不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(level),"等级不能为空");

    }

    /**
     * 更新操作
     * @param customer
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCustomer(Customer customer) {
        Integer id=customer.getId();
        AssertUtil.isTrue(id==null||customerMapper.selectByPrimaryKey(id)==null,"待更新对象不存在");
        checkCustomerParams(customer.getName(),customer.getPhone(),customer.getFr(),customer.getLevel());
        customer.setUpdateDate((new Date()));
        AssertUtil.isTrue(customerMapper.updateByPrimaryKeySelective(customer)<1,"更新操作失败");
    }

    /**
     * 删除操作
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCustomer(Integer[] ids) {
        AssertUtil.isTrue(ids==null ||ids.length<1,"待更新记录不存在");
        AssertUtil.isTrue(customerMapper.deleteBatch(ids)<ids.length,"更新操作失败");
    }
    /**
     * 客户贡献查询
     * @param customerQuery
     * @return
     */
    public Map<String,Object> queryCustomerContributionByParams(CustomerQuery customerQuery){
        Map<String,Object> map = new HashMap<>();

        PageHelper.startPage(customerQuery.getPage(),customerQuery.getLimit());

        PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(customerMapper.queryCustomerContributionByParams(customerQuery));


        map.put("code",0);
        map.put("mag","ok");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());

        return map;
    }

    /**
     * 客户构成查询
     * @return
     */
    public Map<String,Object> countCustomerMake(){
        Map<String,Object> map = new HashMap<>();


        PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(customerMapper.queryCountMake());


        map.put("code",0);
        map.put("mag","ok");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());

        return map;
    }
}
