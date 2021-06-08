package com.usian.controller;

import com.usian.pojo.TbItemCat;
import com.usian.service.ItemCategoryService;
import com.usian.vo.ICResultDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/itemCategory")
public class ItemCategoryController {
    @Autowired
    private ItemCategoryService itemCategoryService;


    @PostMapping("/selectItemCategoryByParentId")
    public List<TbItemCat> selectItemCategoryByParentId(@RequestParam("id") Long id){
        return itemCategoryService.selectItemCategoryByParentId(id);
    }

    //查询首页左侧分类
    @RequestMapping("/selectItemCategoryAll")
    public ICResultDataVo selectItemCategoryAll(){
        return itemCategoryService.selectItemCategoryAll();
    }

}
