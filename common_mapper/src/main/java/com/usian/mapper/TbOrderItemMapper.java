package com.usian.mapper;

import com.usian.pojo.TbOrderItem;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TbOrderItemMapper extends Mapper<TbOrderItem> {

    void addOrderItem(@Param("orderItemList") List<TbOrderItem> orderItemList);

}