package com.mwz.crm.dao;

import com.mwz.crm.base.BaseMapper;
import com.mwz.crm.vo.CustomerReprieve;

import java.util.List;

public interface CustomerReprieveMapper extends BaseMapper<CustomerReprieve,Integer> {

    List<CustomerReprieve> queryByParams(Integer lossId);

    Integer deleteById(Integer[] ids);

    Integer selectCountByIdS(Integer[] ids);
}