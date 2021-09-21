package com.mwz.crm.controller;

import com.mwz.crm.base.BaseController;
import com.mwz.crm.service.CustomerOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author: yuuki
 * @Date: 2021-06-11 - 06 - 15:21
 * @Description: com.mwz.crm.controller
 * @version: 1.0
 */
@Controller
@RequestMapping("customerOrder")
public class CustomerOrderController extends BaseController {
    @Resource
    private CustomerOrderService customerOrderService;
    @RequestMapping("customerOrderList")
    @ResponseBody
    public Map<String, Object> customerOrderList(Integer cusId){
        return customerOrderService.queryByParams(cusId);
    }
}
