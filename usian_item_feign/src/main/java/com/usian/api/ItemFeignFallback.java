package com.usian.api;

import com.usian.pojo.*;
import com.usian.utils.PageResult;
import com.usian.vo.ICResultDataVo;
import com.usian.vo.TbItemVo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ItemFeignFallback implements ItemFeign {
    @Override
    public TbItemDesc selectItemDescByItemId(Long itemId) {
        return null;
    }

    @Override
    public TbItemParamItem selectTbItemParamItemByItemId(Long itemId) {
        return null;
    }

    @Override
    public TbItem selectItemInfo(Long itemId) {
        return null;
    }

    @Override
    public ICResultDataVo selectItemCategoryAll() {
        return null;
    }

    @Override
    public PageResult selectTbItemAllByPage(Integer page, Integer rows) {
        return null;
    }

    @Override
    public List<TbItemCat> selectItemCategoryByParentId(Long id) {
        return null;
    }

    @Override
    public void insertTbItem(TbItemVo tbItemVo) {

    }

    @Override
    public TbItemParam selectItemParamByItemCatId(Long itemCatId) {
        return null;
    }

    @Override
    public Map<String, Object> preUpdateItem(Long itemId) {
        return null;
    }

    @Override
    public void deleteItemById(Long itemId) {

    }

    @Override
    public void updateTbItem(TbItemVo tbItemVo) {

    }

    @Override
    public PageResult selectItemParamAll(Integer page, Integer rows) {
        return null;
    }

    @Override
    public void insertItemParam(TbItemParam tbItemParam) {

    }

    @Override
    public void deleteItemParamById(Long id) {

    }
}
