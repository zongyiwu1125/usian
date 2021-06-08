package com.usian.mapper;

import com.usian.pojo.TbItemParam;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TbItemParamMapper extends Mapper<TbItemParam> {
    @Select("select * from tb_item_param order by created desc")
    List<TbItemParam> selectItemParamAll();
}