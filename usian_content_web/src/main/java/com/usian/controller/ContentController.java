package com.usian.controller;

import com.usian.api.ContentFeign;
import com.usian.pojo.TbContent;
import com.usian.pojo.TbContentCategory;
import com.usian.utils.PageResult;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/backend/content")
public class ContentController {
    @Autowired
    private ContentFeign contentFeign;

    @RequestMapping("/selectContentCategoryByParentId")
    public Result selectContentCategoryByParentId(@RequestParam(value = "id",defaultValue = "0") Long id){
        try {
            List<TbContentCategory> tbContentCategory=contentFeign.selectContentCategoryByParentId(id);
            return Result.ok(tbContentCategory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("查询失败");
    }

    @RequestMapping("/insertContentCategory")
    public Result insertContentCategory(TbContentCategory tbContentCategory){
        try {
            contentFeign.insertContentCategory(tbContentCategory);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("添加失败");
    }

    @RequestMapping("/deleteContentCategoryById")
    public Result deleteContentCategoryById(@RequestParam("categoryId") Long categoryId){
        try {
            contentFeign.deleteContentCategoryById(categoryId);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("删除失败");
    }

    @RequestMapping("/updateContentCategory")
    public Result updateContentCategory(TbContentCategory tbContentCategory){
        try {
            contentFeign.updateContentCategory(tbContentCategory);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("修改失败");
    }




    //根据分类id查询内容
    @RequestMapping("/selectTbContentAllByCategoryId")
    public Result selectTbContentAllByCategoryId(@RequestParam(name = "page",defaultValue = "1")Integer page,@RequestParam(name = "rows",defaultValue = "100")Integer rows, @RequestParam(name = "categoryId") Long categoryId){
        try {
            PageResult pageResult =contentFeign.selectTbContentAllByCategoryId(page,rows,categoryId);
            return Result.ok(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("查询失败");
    }

    //添加分类中的内容
    @RequestMapping("/insertTbContent")
    public Result insertTbContent(TbContent tbContent){
        try {
            contentFeign.insertTbContent(tbContent);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("添加失败");
    }

    //删除内容
    @RequestMapping("/deleteContentByIds")
    public Result deleteContentByIds(@RequestParam Long ids){
        try {
            contentFeign.deleteContentByIds(ids);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("删除失败");
    }
}
