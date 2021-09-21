package com.mwz.crm.service;

import com.github.pagehelper.PageInfo;
import com.mwz.crm.base.BaseService;
import com.mwz.crm.dao.CustomerLossMapper;
import com.mwz.crm.dao.CustomerReprieveMapper;
import com.mwz.crm.utils.AssertUtil;
import com.mwz.crm.vo.CustomerLoss;
import com.mwz.crm.vo.CustomerReprieve;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: yuuki
 * @Date: 2021-06-12 - 06 - 10:00
 * @Description: com.mwz.crm.service
 * @version: 1.0
 */
@Service
public class CustomerReprieveService extends BaseService<CustomerReprieve,Integer> {
    @Resource
    private CustomerReprieveMapper customerReprieveMapper;

    @Resource
    private CustomerLossMapper customerLossMapper;

    public Map<String, Object> queryCustomerReprieve(Integer lossId) {
        Map<String,Object> map=new HashMap<>();
        List<CustomerReprieve> customerReprieveList=customerReprieveMapper.queryByParams(lossId);
        PageInfo<CustomerReprieve> pageInfo=new PageInfo<>(customerReprieveList);
        map.put("code",0);
        map.put("msg","ok");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 更新
     * @param customerReprieve
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCustomerReprieve(CustomerReprieve customerReprieve) {
        Integer id=customerReprieve.getId();
        AssertUtil.isTrue(customerReprieveMapper.selectByPrimaryKey(id)==null||id==null,"待更新记录不存在");
        AssertUtil.isTrue(StringUtils.isBlank(customerReprieve.getMeasure()),"措施不能为空");
        customerReprieve.setUpdateDate(new Date());
        customerReprieve.setIsValid(1);
        AssertUtil.isTrue(customerReprieveMapper.updateByPrimaryKeySelective(customerReprieve)<1,"更新操作失败");
    }

    /**
     * 添加
     * @param customerReprieve
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCustomerReprieve(CustomerReprieve customerReprieve) {
        AssertUtil.isTrue(StringUtils.isBlank(customerReprieve.getMeasure()),"措施不能为空");
        customerReprieve.setCreateDate(new Date());
        customerReprieve.setUpdateDate(new Date());
        customerReprieve.setIsValid(1);
        AssertUtil.isTrue(customerReprieveMapper.insertSelective(customerReprieve)<1,"添加操作失败");
    }

    /**
     * 删除多行
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteById(Integer[] ids) {
        AssertUtil.isTrue(ids==null||ids.length<1,"待更新记录不存在");

        AssertUtil.isTrue(customerReprieveMapper.selectCountByIdS(ids)<ids.length,"待更新记录不存在");
        AssertUtil.isTrue(customerReprieveMapper.deleteById(ids)<ids.length,"删除操作失败");


    }

    /**
     * 客户流失
     * @param customerLoss
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCustomerLossState(CustomerLoss customerLoss) {
        Integer id=customerLoss.getId();
        AssertUtil.isTrue(customerLossMapper.selectByPrimaryKey(id)==null,"暂缓客户不存在");
        AssertUtil.isTrue(StringUtils.isBlank(customerLoss.getLossReason()),"流失原因不能为空");
        customerLoss.setState(1);
        customerLoss.setConfirmLossTime(new Date());
        customerLoss.setUpdateDate(new Date());
        AssertUtil.isTrue(customerLossMapper.updateByPrimaryKeySelective(customerLoss)<1,"确认流失失败");
    }
}
