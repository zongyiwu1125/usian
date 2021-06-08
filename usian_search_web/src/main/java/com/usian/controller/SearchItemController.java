package com.usian.controller;

import com.usian.api.SearchItemFeign;
import com.usian.utils.JsonUtils;
import com.usian.utils.Result;
import com.usian.vo.ItemEsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/frontend/searchItem")
public class SearchItemController {

    @Autowired
    private SearchItemFeign searchItemFeign;

    @RequestMapping("/importAll")
    public Result importAll(){
        try {
            searchItemFeign.importAll();
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("导入失败");
    }

    @RequestMapping("/list")
    public String list(String q){
        List<ItemEsVo> list=searchItemFeign.list(q);
        return JsonUtils.objectToJson(list);
    }

}
