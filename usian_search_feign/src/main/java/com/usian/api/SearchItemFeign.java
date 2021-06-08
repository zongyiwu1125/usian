package com.usian.api;

import com.usian.vo.ItemEsVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "usian-search-service")
public interface SearchItemFeign {

    @RequestMapping("/searchItem/importAll")
    public void importAll();

    @RequestMapping("/searchItem/list")
    List<ItemEsVo> list(@RequestParam("q") String q);
}
