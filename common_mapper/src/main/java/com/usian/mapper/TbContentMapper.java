package com.usian.mapper;

import com.usian.pojo.TbContent;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TbContentMapper extends Mapper<TbContent> {

    @Select("select * from tb_content where category_id=#{categoryId} order by created")
    List<TbContent> selectTbContentAllByCategoryId(@Param("categoryId") Long categoryId);
}