package com.mwz.crm.controller;

import com.mwz.crm.base.BaseController;
import com.mwz.crm.base.ResultInfo;
import com.mwz.crm.dao.CustomerMapper;
import com.mwz.crm.query.CustomerQuery;
import com.mwz.crm.service.CustomerService;
import com.mwz.crm.vo.Customer;
import com.mwz.crm.vo.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author: yuuki
 * @Date: 2021-06-10 - 06 - 16:52
 * @Description: com.mwz.crm.controller
 * @version: 1.0
 */
@RequestMapping("customer")
@Controller
public class CustomerController extends BaseController {

    @Resource
    private CustomerService customerService;

    @RequestMapping("index")
    public String toCustomer(){
        return "customer/customer";
    }
    @RequestMapping("customerList")
    @ResponseBody
    public Map<String,Object> queryCustomer(CustomerQuery customerQuery){
        return customerService.queryByParams(customerQuery);
    }
    @RequestMapping("toAddAndUpdatePage")
    public String toAddCustomer(Integer id, HttpServletRequest request){
        Customer customer= customerService.selectByPrimaryKey(id);
        request.setAttribute("customer",customer);
        return "customer/addAndUpdate";
    }
    @PostMapping("addCustomer")
    @ResponseBody
    public ResultInfo addCustomer(Customer customer){
        customerService.addCustomer(customer);
        return success("添加操作成功");
    }
    @PostMapping("updateCustomer")
    @ResponseBody
    public ResultInfo updateCustomer(Customer customer){
        customerService.updateCustomer(customer);
        return success("更新操作成功");
    }
    @PostMapping("deleteCustomer")
    @ResponseBody
    public ResultInfo deleteCustomer(Integer[] ids){
        customerService.deleteCustomer(ids);
        return success("删除操作成功");
    }
    @RequestMapping("toCustomerOrderPage")
    public String CustomerOrderPage(Integer customerId,HttpServletRequest request){
        Customer customer =customerService.selectByPrimaryKey(customerId);
        request.setAttribute("customer",customer);
        return "customerOrder/customerOrder";
    }
    /**
     * 客户贡献查询
     * @param customerQuery
     * @return
     */
    @RequestMapping("queryCustomerContributionByParams")
    @ResponseBody
    public Map<String,Object> queryCustomerContributionByParams(CustomerQuery customerQuery){

        return customerService.queryCustomerContributionByParams(customerQuery);
    }


    /**
     * 客户构成查询
     * @return
     */
    @RequestMapping("countCustomerMake")
    @ResponseBody
    public Map<String,Object> countCustomerMake(){
        return customerService.countCustomerMake();
    }

}
