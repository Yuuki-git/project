package com.mwz.crm.controller;

import com.mwz.crm.base.BaseController;
import com.mwz.crm.base.ResultInfo;
import com.mwz.crm.service.CustomerReprieveService;
import com.mwz.crm.vo.CustomerLoss;
import com.mwz.crm.vo.CustomerReprieve;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author: yuuki
 * @Date: 2021-06-12 - 06 - 9:59
 * @Description: com.mwz.crm.controller
 * @version: 1.0
 */
@Controller
@RequestMapping("customerReprieve")
public class CustomerReprieveController extends BaseController{
    @Resource
    private CustomerReprieveService customerReprieveService;
    @RequestMapping("customerReprieveList")
    @ResponseBody
    public Map<String,Object> queryCustomerReprieveList(Integer lossId){
        return customerReprieveService.queryCustomerReprieve(lossId);
    }
    @PostMapping("addCustomerReprieve")
    @ResponseBody
    public ResultInfo addCustomerReprieve(CustomerReprieve customerReprieve){
        customerReprieveService.addCustomerReprieve(customerReprieve);
        return success("添加操作成功");

    }

    @PostMapping("updateCustomerReprieve")
    @ResponseBody
    public ResultInfo updateCustomerReprieve(CustomerReprieve customerReprieve){
        customerReprieveService.updateCustomerReprieve(customerReprieve);
        return success("更新操作成功");

    }
    @PostMapping("deleteCustomerReprieve")
    @ResponseBody
    public ResultInfo deleteCustomerReprieve(Integer[] ids){
        customerReprieveService.deleteById(ids);
        return success("删除成功");
    }
    @RequestMapping("updateCustomerLossState")
    @ResponseBody
    public ResultInfo updateCustomerLossState(CustomerLoss customerLoss){
            customerReprieveService.updateCustomerLossState(customerLoss);
            return success("客户已流失");
    }

}
