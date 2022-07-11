package com.mwz.miaoshadao.mapper;

import com.mwz.miaoshadao.dao.StockOrder;
import com.mwz.miaoshadao.dao.StockOrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StockOrderMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(StockOrder record);

    int insertSelective(StockOrder record);

    StockOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StockOrder record);

    int updateByPrimaryKey(StockOrder record);
}