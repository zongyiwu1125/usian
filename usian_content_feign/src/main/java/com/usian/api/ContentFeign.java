package com.usian.api;

import com.usian.pojo.TbContent;
import com.usian.pojo.TbContentCategory;
import com.usian.utils.PageResult;
import com.usian.vo.BigADVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "usian-content-service")
public interface ContentFeign {

    @RequestMapping("/content/selectContentCategoryByParentId")
    public List<TbContentCategory> selectContentCategoryByParentId(@RequestParam(value = "id",defaultValue = "0") Long id);

    @RequestMapping("/content/insertContentCategory")
    void insertContentCategory(@RequestBody TbContentCategory tbContentCategory);

    @RequestMapping("/content/deleteContentCategoryById")
    void deleteContentCategoryById(@RequestParam("categoryId") Long categoryId);

    @RequestMapping("/content/updateContentCategory")
    void updateContentCategory(@RequestBody TbContentCategory tbContentCategory);


    //根据分类id查询内容
    @RequestMapping("/content/selectTbContentAllByCategoryId")
    PageResult selectTbContentAllByCategoryId(@RequestParam(name = "page",defaultValue = "1")Integer page,@RequestParam(name = "rows",defaultValue = "100")Integer rows, @RequestParam(name = "categoryId") Long categoryId);

    //添加分类中的内容
    @RequestMapping("/content/insertTbContent")
    void insertTbContent(@RequestBody TbContent tbContent);

    //删除内容
    @RequestMapping("/content/deleteContentByIds")
    void deleteContentByIds(@RequestParam Long ids);

    //查询大广告内容
    @RequestMapping("/content/selectFrontendContentByAD")
    public List<BigADVo> selectFrontendContentByAD();
}
