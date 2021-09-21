package com.mwz.crm.controller;

import com.mwz.crm.base.BaseController;
import com.mwz.crm.base.ResultInfo;
import com.mwz.crm.query.CustomerLossQuery;
import com.mwz.crm.service.CustomerLossService;
import com.mwz.crm.vo.CustomerLoss;
import com.mwz.crm.vo.CustomerReprieve;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author: yuuki
 * @Date: 2021-06-12 - 06 - 9:01
 * @Description: com.mwz.crm.controller
 * @version: 1.0
 */
@Controller
@RequestMapping("customerLoss")
public class CustomerLossController extends BaseController {

    @Resource
    private CustomerLossService customerLossService;

    @RequestMapping("index")
    public String toCustomerLossPage(){
        return "customerLoss/customerLoss";
    }
    @RequestMapping("customerLossList")
    @ResponseBody
    public Map<String,Object> queryCustomer(CustomerLossQuery customerLossQuery){
        return customerLossService.queryByParams(customerLossQuery);
    }
    @RequestMapping("customerLossData")
    public String toCustomerLossDataPage(Integer id, HttpServletRequest request){
        CustomerLoss customerLoss=customerLossService.selectByPrimaryKey(id);
        request.setAttribute("customerLoss",customerLoss);
        return "customerLoss/customerLossData";
    }
    @RequestMapping("customerCountLossList")
    @ResponseBody
    public Map<String,Object> queryCustomerCountLossList(){
        return customerLossService.queryCustomerCountLossList();
    }
}
