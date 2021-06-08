package com.usian.mapper;

import com.usian.pojo.TbItemCat;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TbItemCatMapper extends Mapper<TbItemCat> {
    //@Select("select * from tb_item_cat where parent_id=#{id}")
    List<TbItemCat> selectItemCategoryByParentId(@Param("id") Long id);
}
