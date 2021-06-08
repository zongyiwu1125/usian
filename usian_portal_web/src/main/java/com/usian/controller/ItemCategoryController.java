package com.usian.controller;

import com.usian.api.ItemFeign;
import com.usian.utils.Result;
import com.usian.vo.ICResultDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/frontend/itemCategory")
public class ItemCategoryController {
    @Autowired
    private ItemFeign itemFeign;

    @RequestMapping("/selectItemCategoryAll")
    public Result selectItemCategoryAll(){
        try {
            ICResultDataVo allResultVo=itemFeign.selectItemCategoryAll();
            return Result.ok(allResultVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("查询失败");
    }

}
