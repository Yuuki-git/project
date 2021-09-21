package com.mwz.crm.controller;

import com.mwz.crm.base.BaseController;
import com.mwz.crm.base.ResultInfo;
import com.mwz.crm.exceptions.NoLoginException;
import com.mwz.crm.query.CustomerQuery;
import com.mwz.crm.query.CustomerServeQuery;
import com.mwz.crm.service.CustomerServeService;
import com.mwz.crm.service.DicService;
import com.mwz.crm.vo.CustomerServe;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author: yuuki
 * @Date: 2021-06-12 - 06 - 11:41
 * @Description: com.mwz.crm.controller
 * @version: 1.0
 */
@Controller
@RequestMapping("customerServe")
public class CustomerServeController extends BaseController {
    @Resource
    private CustomerServeService customerServeService;

    @Resource
    private DicService dicService;

    @RequestMapping("index/1")
    public String toCustomerServePage(){
        return "customerServe/customerServe";
    }
    @RequestMapping("index/2")
    public String toCustomerServeAssignerPage(){
        return "customerServe/customerServeAssigner";
    }

    @RequestMapping("index/4")
    public String toCustomerServeBackPage(){
        return "customerServe/customerServeBack";
    }
    @RequestMapping("index/3")
    public String toCustomerServeHandlePage(){
        return "customerServe/customerServeHandle";
    }
    @RequestMapping("index/5")
    public String toCustomerServeArchivePage(){
        return "customerServe/customerServeArchive";
    }
    @RequestMapping("customerServesList")
    @ResponseBody
    public Map<String,Object> queryCustomerServeList(CustomerServeQuery customerServeQuery){
        return customerServeService.queryCustomerServeByParams(customerServeQuery);
    }
    // @RequestMapping("selectDicByName")
    // @ResponseBody
    // public List<Map<String,Object>> selectDicByName(String dicName){
    //     return dicService.selectDicByName(dicName);
    // }
    @RequestMapping("toAddAndUpdatePage")
    public String toAddAndUpdatePage(Integer id, HttpServletRequest request){
        CustomerServe customerServe=customerServeService.selectByPrimaryKey(id);
        request.setAttribute("customerServe",customerServe);
        return "customerServe/addAndUpdate";
    }
    @PostMapping("addCustomer")
    @ResponseBody
    public ResultInfo addCustomer(CustomerServe customerServe,HttpServletRequest request){
        customerServeService.addCustomer(customerServe,request);
        return success("添加成功");
    }
    @PostMapping("updateCustomer")
    @ResponseBody
    public ResultInfo updateCustomer(CustomerServe customerServe){
        customerServeService.updateCustomer(customerServe);
        return success("更新成功");
    }
    @RequestMapping("updateState")
    @ResponseBody
    public ResultInfo updateState(String state,Integer id,String fg) throws NoLoginException {
        customerServeService.updateState(state,id,fg);
        return success("ok");

    }
}
