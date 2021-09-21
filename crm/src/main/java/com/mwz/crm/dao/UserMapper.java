package com.mwz.crm.dao;

import com.mwz.crm.base.BaseMapper;
import com.mwz.crm.vo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User,Integer> {

    //通过用户名查询对象
    public User queryUserByUserName(String userName);
    // 查询所有的销售人员
    List<Map<String,Object>> queryAllSales();
}