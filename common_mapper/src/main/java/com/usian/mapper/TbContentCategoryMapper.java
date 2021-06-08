package com.usian.mapper;

import com.usian.pojo.TbContentCategory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TbContentCategoryMapper extends Mapper<TbContentCategory> {
    @Delete("delete from tb_content_category where parent_id=#{id}")
    void deleteContentCategoryByParentId(@Param("id") Long categoryId);

    @Select("select * from tb_content_category where parent_id=#{id} and status=1 order by sort_order,name")
    List<TbContentCategory> selectContentCategoryByParentId(@Param("id") Long parentId);

    @Select("SELECT * FROM tb_content_category a,tb_content_category b WHERE a.parent_id=b.parent_id AND b.id=#{id} AND a.status=1")
    List<TbContentCategory> selectContentCategoryByParentIdAndStatus(@Param("id") Long categoryId);

    @Update("UPDATE tb_content_category a SET is_parent=0 WHERE a.id=(SELECT parent_id FROM (SELECT * FROM tb_content_category) b WHERE b.id =#{id})")
    void updateIsParentByParentId(@Param("id") Long categoryId);

}