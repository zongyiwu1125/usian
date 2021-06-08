package com.usian.service;

import com.usian.constant.RedisConstantVo;
import com.usian.mapper.TbItemCatMapper;
import com.usian.pojo.TbItemCat;
import com.usian.util.RedisClient;
import com.usian.vo.ICResultDataVo;
import com.usian.vo.ItemCategoryDefVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCategoryService {
    @Autowired
    private TbItemCatMapper tbItemCatMapper;
    @Autowired
    private RedisClient redisClient;

    public List<TbItemCat> selectItemCategoryByParentId(Long id) {
        return tbItemCatMapper.selectItemCategoryByParentId(id);
    }

    public ICResultDataVo selectItemCategoryAll() {
        ICResultDataVo allResultVo = new ICResultDataVo();

        //先查询缓存 缓存中有直接返回
        List list = (List) redisClient.get(RedisConstantVo.ITEM_CATEGORY_KEY);
        if (list!=null){
            allResultVo.setData(list);
            return allResultVo;
        }

        list = selectItemCategoryDefVoByParentId(0L);
        //查询分类及下边的子类
        allResultVo.setData(list);

        redisClient.set(RedisConstantVo.ITEM_CATEGORY_KEY,list);

        return allResultVo;
    }

    public List selectItemCategoryDefVoByParentId(Long parentId){
        //根据父id查询
        List<TbItemCat> itemCats = selectItemCategoryByParentId(parentId);

        //存放转换后的分类
        List itemNewCats=new ArrayList<>();

        itemCats.forEach(tbItemCat -> {
            //判断当前分类下是否还有子类
            if (!tbItemCat.getIsParent()){
                itemNewCats.add(tbItemCat.getName());
            }else {
                //存放某一个分类
                ItemCategoryDefVo defVo = new ItemCategoryDefVo();
                //查询当前分类下所有的子类
                List itemCategoryDefVo = selectItemCategoryDefVoByParentId(tbItemCat.getId());
                defVo.setN(tbItemCat.getName());
                defVo.setI(itemCategoryDefVo);
                itemNewCats.add(defVo);
            }
        });
        return itemNewCats;
    }

}
