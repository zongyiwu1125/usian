package com.usian.controller;

import com.usian.api.ItemFeign;
import com.usian.pojo.TbItem;
import com.usian.utils.PageResult;
import com.usian.utils.Result;
import com.usian.vo.TbItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/backend/item")
public class ItemController {
    @Autowired
    private ItemFeign itemFeign;

    @RequestMapping("/selectItemInfo")
    public Result selectItemInfo(@RequestParam("itemId")Long itemId){
        try {
            TbItem tbItem = itemFeign.selectItemInfo(itemId);
            return Result.ok(tbItem);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    @GetMapping("/selectTbItemAllByPage")
    public Result selectTbItemAllByPage(@RequestParam(value = "page",defaultValue = "1")Integer page,@RequestParam(value = "rows",defaultValue = "2")Integer rows){
        try {
            PageResult pageResult=itemFeign.selectTbItemAllByPage(page,rows);
            return Result.ok(pageResult);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @RequestMapping("/insertTbItem")
    public Result insertTbItem(TbItemVo tbItemVo){
        try {
            itemFeign.insertTbItem(tbItemVo);
            return Result.ok();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @RequestMapping("/preUpdateItem")
    public Result preUpdateItem(Long itemId){
        try {
            Map<String,Object> map =itemFeign.preUpdateItem(itemId);
            return Result.ok(map);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @RequestMapping("/updateTbItem")
    public Result updateTbItem(TbItemVo tbItemVo){
        try {
            itemFeign.updateTbItem(tbItemVo);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("修改失败");
    }

    @RequestMapping("/deleteItemById")
    public Result deleteItemById(Long itemId){
        try {
            itemFeign.deleteItemById(itemId);
            return Result.ok();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }



}
