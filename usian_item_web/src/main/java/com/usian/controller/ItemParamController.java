package com.usian.controller;

import com.usian.api.ItemFeign;
import com.usian.pojo.TbItemParam;
import com.usian.utils.PageResult;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/backend/itemParam")
public class ItemParamController {

    @Autowired
    private ItemFeign itemFeign;

    @GetMapping("/selectItemParamByItemCatId/{itemCatId}")
    public Result selectItemParamByItemCatId(@PathVariable("itemCatId") Long itemCatId){
        try {
            TbItemParam tbItemParam=itemFeign.selectItemParamByItemCatId(itemCatId);
            return Result.ok(tbItemParam);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @RequestMapping("/selectItemParamAll")
    public Result selectItemParamAll(@RequestParam(value = "page",defaultValue = "1")Integer page, @RequestParam(value = "rows",defaultValue = "2")Integer rows){
        try {
            PageResult pageResult=itemFeign.selectItemParamAll(page,rows);
            return Result.ok(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("查询失败");
    }

    @RequestMapping("/insertItemParam")
    public Result insertItemParam(TbItemParam tbItemParam){
        try {
            itemFeign.insertItemParam(tbItemParam);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("添加失败");
    }

    @RequestMapping("/deleteItemParamById")
    public Result deleteItemParamById(@RequestParam Long id){
        try {
            itemFeign.deleteItemParamById(id);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("删除失败");
    }

}
