package com.mwz.crm.controller;

import com.mwz.crm.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: yuuki
 * @Date: 2021-06-12 - 06 - 16:53
 * @Description: com.mwz.crm.controller
 * @version: 1.0
 */
@RequestMapping("report")
@Controller
public class CustomerReportController extends BaseController {
    @RequestMapping("0")
    public String index(){
        return "customerCount/customerCount";
    }
    @RequestMapping("1")
    public String index1() {
        return "customerCount/countCustomerMake";
    }
    @RequestMapping("3")
    public String index3() {
        return "customerCount/customerCountLoss";
    }
}