package com.mwz.crm.controller;

import com.mwz.crm.base.BaseController;
import com.mwz.crm.service.CustomerOrderService;
import com.mwz.crm.service.CustomerService;
import com.mwz.crm.service.OrderDetailsService;
import com.mwz.crm.vo.Customer;
import com.mwz.crm.vo.CustomerOrder;
import com.mwz.crm.vo.OrderDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author: yuuki
 * @Date: 2021-06-11 - 06 - 17:04
 * @Description: com.mwz.crm.controller
 * @version: 1.0
 */
@Controller
@RequestMapping("orderDetail")
public class OrderDetailsController extends BaseController {
        @Resource
        private OrderDetailsService orderDetailsService;
        @Resource
        private CustomerOrderService customerOrderService;
        @RequestMapping("toOrderDetailPage")
        public String toOrderDetailPage(Integer id, HttpServletRequest request){
                CustomerOrder customer=customerOrderService.selectByPrimaryKey(id);
                request.setAttribute("customer",customer);
                Integer sum=orderDetailsService.querySumById(id);
                request.setAttribute("sum",sum);
                return "customerOrder/orderDetail";
        }
        @RequestMapping("orderDetailList")
        @ResponseBody
        public Map<String, Object> queryOrderDetail(Integer orderId){
            return orderDetailsService.queryOrderDetail(orderId);
        }
}
