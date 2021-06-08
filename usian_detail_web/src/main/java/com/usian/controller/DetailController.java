package com.usian.controller;

import com.usian.api.ItemFeign;
import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemDesc;
import com.usian.pojo.TbItemParamItem;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/frontend/detail")
public class DetailController {

    @Autowired
    private ItemFeign itemFeign;

    //查询商品描述
    @RequestMapping("/selectItemDescByItemId")
    public Result selectItemDescByItemId(Long itemId){
        try {
            TbItemDesc itemDesc=itemFeign.selectItemDescByItemId(itemId);
            return Result.ok(itemDesc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("查询失败");
    }

    //查询商品参数值
    @RequestMapping("/selectTbItemParamItemByItemId")
    public Result selectTbItemParamItemByItemId(Long itemId){
        try {
            TbItemParamItem itemParamItem=itemFeign.selectTbItemParamItemByItemId(itemId);
            return Result.ok(itemParamItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("查询失败");
    }

    //查询商品信息
    @RequestMapping("/selectItemInfo")
    public Result selectItemInfo(Long itemId){
        try {
            TbItem tbItem=itemFeign.selectItemInfo(itemId);
            return Result.ok(tbItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("查询失败");
    }
}
