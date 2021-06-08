package com.usian.api;

import com.usian.pojo.*;
import com.usian.utils.PageResult;
import com.usian.vo.ICResultDataVo;
import com.usian.vo.TbItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//@FeignClient(value = "usian-item-service")
//@FeignClient(value = "usian-item-service",fallback = ItemFeignFallback.class)
@FeignClient(value = "usian-item-service",fallbackFactory = ItemFeignFallbackFactory.class)
public interface ItemFeign {


    //查询商品描述
    @RequestMapping("/item/selectItemDescByItemId")
    TbItemDesc selectItemDescByItemId(@RequestParam("itemId") Long itemId);

    //查询商品参数值
    @RequestMapping("/item/selectTbItemParamItemByItemId")
    TbItemParamItem selectTbItemParamItemByItemId(@RequestParam("itemId")Long itemId);

    //根据id查询商品信息
    @RequestMapping("/item/selectItemInfo")
    TbItem selectItemInfo(@RequestParam("itemId") Long itemId);

    //查询前台左侧商品分类
    @RequestMapping("/itemCategory/selectItemCategoryAll")
    public ICResultDataVo selectItemCategoryAll();


    //查询全部商品信息（分页）
    @GetMapping("/item/selectTbItemAllByPage")
    PageResult selectTbItemAllByPage(@RequestParam("page") Integer page, @RequestParam("rows") Integer rows);

    //查询商品类别
    @PostMapping("/itemCategory/selectItemCategoryByParentId")
    List<TbItemCat> selectItemCategoryByParentId(@RequestParam("id") Long id);

    //添加
    @RequestMapping("/item/insertTbItem")
    void insertTbItem(@RequestBody TbItemVo tbItemVo);

    //根据类目id查询类目模块
    @GetMapping("/itemParam/selectItemParamByItemCatId/{itemCatId}")
    public TbItemParam selectItemParamByItemCatId(@PathVariable("itemCatId") Long itemCatId);

    @RequestMapping("/item/preUpdateItem")
    Map<String, Object> preUpdateItem(@RequestParam Long itemId);

    @RequestMapping("/item/deleteItemById")
    void deleteItemById(@RequestParam Long itemId);

    @RequestMapping("/item/updateTbItem")
    void updateTbItem(@RequestBody TbItemVo tbItemVo);

    @RequestMapping("/itemParam/selectItemParamAll")
    public PageResult selectItemParamAll(@RequestParam(value = "page",defaultValue = "1")Integer page, @RequestParam(value = "rows",defaultValue = "2")Integer rows);

    @RequestMapping("/itemParam/insertItemParam")
    void insertItemParam(@RequestBody TbItemParam tbItemParam);

    @RequestMapping("/itemParam/deleteItemParamById")
    void deleteItemParamById(@RequestParam Long id);


}
