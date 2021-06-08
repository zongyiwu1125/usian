package com.usian.controller;

import com.usian.service.SearchItemService;
import com.usian.vo.ItemEsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/searchItem")
public class SearchItemController {
    @Autowired
    private SearchItemService searchItemService;

    @RequestMapping("/importAll")
    public void importAll(){
        searchItemService.importAll();
    }

    @RequestMapping("/list")
    List<ItemEsVo> list(@RequestParam("q") String q){
        return searchItemService.list(q);
    }
}
