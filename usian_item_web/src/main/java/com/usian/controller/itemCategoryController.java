package com.usian.controller;

import com.usian.api.ItemFeign;
import com.usian.pojo.TbItemCat;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/backend/itemCategory")
public class itemCategoryController {
    @Autowired
    private ItemFeign itemFeign;

    @PostMapping("/selectItemCategoryByParentId")
    public Result selectItemCategoryByParentId(@RequestParam(value = "id",defaultValue = "0")Long id){
        try {
            List<TbItemCat> list=itemFeign.selectItemCategoryByParentId(id);
            return Result.ok(list);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
