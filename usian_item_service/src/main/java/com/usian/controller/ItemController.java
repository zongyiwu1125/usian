package com.usian.controller;

import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemDesc;
import com.usian.pojo.TbItemParamItem;
import com.usian.service.ItemService;
import com.usian.utils.PageResult;
import com.usian.vo.TbItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/selectItemDescByItemId")
    public TbItemDesc selectItemDescByItemId(@RequestParam("itemId")Long itemId){
        return itemService.selectItemDescByItemId(itemId);
    }

    @RequestMapping("/selectTbItemParamItemByItemId")
    public TbItemParamItem selectTbItemParamItemByItemId(@RequestParam("itemId")Long itemId){
        return itemService.selectTbItemParamItemByItemId(itemId);
    }

    @RequestMapping("/selectItemInfo")
    public TbItem selectItemInfo(@RequestParam("itemId")Long itemId){
        TbItem tbItem=itemService.selectItemInfo(itemId);
        return tbItem;
    }

    @GetMapping("/selectTbItemAllByPage")
    public PageResult selectTbItemAllByPage(@RequestParam("page") Integer page,@RequestParam("rows") Integer rows){
        return itemService.selectTbItemAllByPage(page,rows);
    }

    @RequestMapping("/insertTbItem")
    public void insertTbItem(@RequestBody TbItemVo tbItemVo){
        itemService.insertTbItem(tbItemVo);
    }

    @RequestMapping("/preUpdateItem")
    Map<String, Object> preUpdateItem(@RequestParam Long itemId){
        return itemService.preUpdateItem(itemId);
    }

    @RequestMapping("/updateTbItem")
    void updateTbItem(@RequestBody TbItemVo tbItemVo){
        itemService.updateTbItem(tbItemVo);
    }

    @RequestMapping("/deleteItemById")
    public void deleteItemById(@RequestParam Long itemId){
        itemService.deleteItemById(itemId);
    }
}
