package com.mwz.crm.dao;

import com.mwz.crm.base.BaseMapper;
import com.mwz.crm.vo.Dic;

import java.util.List;
import java.util.Map;

public interface DicMapper extends BaseMapper<Dic,Integer> {

    List<Dic> queryByParam(String dicName);

    Integer deleteById(Integer id);

    // List<Map<String, Object>> selectDicByName(String dicName);
}