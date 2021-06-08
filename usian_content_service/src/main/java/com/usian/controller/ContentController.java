package com.usian.controller;

import com.usian.pojo.TbContent;
import com.usian.pojo.TbContentCategory;
import com.usian.service.ContentService;
import com.usian.utils.PageResult;
import com.usian.vo.BigADVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/content")
public class ContentController {
    @Autowired
    private ContentService contentService;

    @RequestMapping("/selectContentCategoryByParentId")
    public List<TbContentCategory> selectContentCategoryByParentId(@RequestParam(value = "id",defaultValue = "0") Long id){
        return contentService.selectContentCategoryByParentId(id);
    }

    @RequestMapping("/insertContentCategory")
    public void insertContentCategory(@RequestBody TbContentCategory tbContentCategory){
        contentService.insertContentCategory(tbContentCategory);
    }

    @RequestMapping("/deleteContentCategoryById")
    public void deleteContentCategoryById(@RequestParam("categoryId") Long categoryId){
        contentService.deleteContentCategoryById(categoryId);
    }

    @RequestMapping("/updateContentCategory")
    void updateContentCategory(@RequestBody TbContentCategory tbContentCategory){
        contentService.updateContentCategory(tbContentCategory);
    }

    //根据分类id查询内容
    @RequestMapping("/selectTbContentAllByCategoryId")
    PageResult selectTbContentAllByCategoryId(@RequestParam(name = "page",defaultValue = "1")Integer page,@RequestParam(name = "rows",defaultValue = "100")Integer rows, @RequestParam(name = "categoryId") Long categoryId){
        return contentService.selectTbContentAllByCategoryId(page,rows,categoryId);
    }

    //添加分类中的内容
    @RequestMapping("/insertTbContent")
    void insertTbContent(@RequestBody TbContent tbContent){
        contentService.insertTbContent(tbContent);
    }

    //删除内容
    @RequestMapping("/deleteContentByIds")
    void deleteContentByIds(@RequestParam Long ids){
        contentService.deleteContentByIds(ids);
    }

    //查询大广告内容
    @RequestMapping("/selectFrontendContentByAD")
    public List<BigADVo> selectFrontendContentByAD(){
        return contentService.selectFrontendContentByAD();
    }

}
