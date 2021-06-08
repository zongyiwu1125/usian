package com.usian.mapper;

import com.usian.pojo.TbItemParamItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface TbItemParamItemMapper extends Mapper<TbItemParamItem> {
    @Delete("delete from tb_item_param_item where item_id=#{itemId}")
    void deleteByItemId(@Param("itemId") Long itemId);

    @Select("select * from tb_item_param_item where item_id=#{itemId}")
    TbItemParamItem selectByItemId(@Param("itemId")Long itemId);
}