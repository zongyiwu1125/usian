package com.usian.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.constant.ContentConstant;
import com.usian.constant.RedisConstantVo;
import com.usian.mapper.TbContentCategoryMapper;
import com.usian.mapper.TbContentMapper;
import com.usian.pojo.TbContent;
import com.usian.pojo.TbContentCategory;
import com.usian.util.RedisClient;
import com.usian.utils.PageResult;
import com.usian.vo.BigADVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentService {
    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;
    @Autowired
    private TbContentMapper tbContentMapper;
    @Autowired
    private RedisClient redisClient;

    public List<TbContentCategory> selectContentCategoryByParentId(Long id) {
        return tbContentCategoryMapper.selectContentCategoryByParentId(id);
    }

    @Transactional
    public void insertContentCategory(TbContentCategory tbContentCategory) {
        //当前系统时间
        Date createdOrUpdated = new Date();
        //添加分类
        //设置默认值
        tbContentCategory.setStatus(1);
        tbContentCategory.setSortOrder(1);
        tbContentCategory.setIsParent(false);
        tbContentCategory.setCreated(createdOrUpdated);
        tbContentCategoryMapper.insertSelective(tbContentCategory);

        //修改分类对应的父
        //判断分类是否为父  不是父修改成父
        TbContentCategory father = tbContentCategoryMapper.selectByPrimaryKey(tbContentCategory.getParentId());

        if (!father.getIsParent()){
            father.setIsParent(true);
            father.setUpdated(createdOrUpdated);
            tbContentCategoryMapper.updateByPrimaryKeySelective(father);
        }
    }

    public void deleteContentCategoryById(Long categoryId) {
        Date now = new Date();
        //修改子节点状态检查子节点下是否还有子节点
        List<TbContentCategory> childList = selectContentCategoryByParentId(categoryId);
        if (childList!=null&&childList.size()>0){
            return;//有字节点不能修改
        }
        //没有节点修改子节点
        TbContentCategory tbContentCategory = new TbContentCategory();
        tbContentCategory.setId(categoryId);
        tbContentCategory.setStatus(0);
        tbContentCategory.setUpdated(now);
        tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);

        //修改后检查子节点对应的父还有没有活着的子节点
        List<TbContentCategory> childListLife = tbContentCategoryMapper.selectContentCategoryByParentIdAndStatus(categoryId);
        for (TbContentCategory contentCategory : childListLife) {
            System.out.println(contentCategory.toString());
        }
        if (childListLife==null || childListLife.size()==0){
            //父没有任何活着的字节点    把is_parent 改为false
            tbContentCategoryMapper.updateIsParentByParentId(categoryId);
        }
    }

    //修改
    public void updateContentCategory(TbContentCategory tbContentCategory) {
        tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
    }

    public PageResult selectTbContentAllByCategoryId(Integer page,Integer rows, Long categoryId) {
        PageHelper.startPage(page,rows);
        List<TbContent> list = tbContentMapper.selectTbContentAllByCategoryId(categoryId);
        PageInfo<TbContent> pageInfo = new PageInfo<>(list);
        PageResult pageResult = new PageResult();
        pageResult.setPageIndex(pageInfo.getPageNum());
        pageResult.setTotalPage(pageInfo.getPages());
        pageResult.setResult(pageInfo.getList());
        return pageResult;
    }

    public void insertTbContent(TbContent tbContent) {
        Date now = new Date();
        tbContent.setCreated(now);
        tbContentMapper.insertSelective(tbContent);
    }

    public void deleteContentByIds(Long ids) {
        tbContentMapper.deleteByPrimaryKey(ids);
    }

    public List<BigADVo> selectFrontendContentByAD() {
        //获取缓存信息 有缓存直接返回
        List<BigADVo> o = (List<BigADVo>) redisClient.get(RedisConstantVo.CONTENT_KEY);
        if (o!=null){
            return o;
        }
        //根据大广告id查询内容
        List<TbContent> oldContentList = tbContentMapper.selectTbContentAllByCategoryId(ContentConstant.CATEGORY_ID);
        //存放新的内容显示
        List<BigADVo> newContentList=new ArrayList<>();
        //将每一个内容信息转换为
        oldContentList.forEach(tbContent -> {
            //存储新的内容对象
            BigADVo bigADVo = new BigADVo();
            //设置值
            bigADVo.setAlt(tbContent.getContent());
            bigADVo.setHeight(ContentConstant.HEIGNT);
            bigADVo.setHeightB(ContentConstant.HEIGNT_B);
            bigADVo.setHref(tbContent.getUrl());
            bigADVo.setSrc(tbContent.getPic());
            bigADVo.setSrcB(tbContent.getPic2());
            bigADVo.setWidth(ContentConstant.WIDTH);
            bigADVo.setWidthB(ContentConstant.WIDTH_B);
            //存入集合
            newContentList.add(bigADVo);
        });

        redisClient.set(RedisConstantVo.CONTENT_KEY,newContentList);

        return newContentList;
    }
}
