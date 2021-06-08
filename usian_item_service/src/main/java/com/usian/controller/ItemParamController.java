package com.usian.controller;

import com.usian.pojo.TbItemParam;
import com.usian.service.ItemParamService;
import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/itemParam")
public class ItemParamController {

    @Autowired
    private ItemParamService itemParamService;

    @GetMapping("/selectItemParamByItemCatId/{itemCatId}")
    public TbItemParam selectItemParamByItemCatId(@PathVariable("itemCatId") Long itemCatId){
        return itemParamService.selectItemParamByItemCatId(itemCatId);
    }

    @RequestMapping("/selectItemParamAll")
    public PageResult selectItemParamAll(@RequestParam(value = "page",defaultValue = "1")Integer page, @RequestParam(value = "rows",defaultValue = "2")Integer rows){
        return itemParamService.selectItemParamAll(page,rows);
    }

    @RequestMapping("/insertItemParam")
    void insertItemParam(@RequestBody TbItemParam tbItemParam){
        itemParamService.insertItemParam(tbItemParam);
    }

    @RequestMapping("/deleteItemParamById")
    void deleteItemParamById(@RequestParam Long id){
        itemParamService.deleteItemParamById(id);
    }
}
