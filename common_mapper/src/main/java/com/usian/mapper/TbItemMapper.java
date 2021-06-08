package com.usian.mapper;

import com.usian.pojo.TbItem;
import com.usian.vo.ItemEsVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TbItemMapper extends Mapper<TbItem> {
    @Select("SELECT i.*,c.name cname FROM tb_item i,tb_item_cat c WHERE i.status=1 AND i.cid=c.id ORDER BY created DESC")
    List<TbItem> selectAllOrderByCreated();

    List<ItemEsVo> importAll();

    void updateItemNumByItemId(@Param("itemId") String itemId,@Param("num") Integer num);
}