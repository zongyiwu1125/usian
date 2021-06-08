package com.usian.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.mapper.TbItemParamMapper;
import com.usian.pojo.TbItemParam;
import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemParamService {
    @Autowired
    private TbItemParamMapper tbItemParamMapper;
    public TbItemParam selectItemParamByItemCatId(Long itemCatId) {
        TbItemParam tbItemParam = new TbItemParam();
        tbItemParam.setItemCatId(itemCatId);
        return tbItemParamMapper.selectOne(tbItemParam);
    }

    public PageResult selectItemParamAll(Integer page, Integer rows) {
        PageHelper.startPage(page,rows);
        List<TbItemParam> list = tbItemParamMapper.selectItemParamAll();
        PageInfo<TbItemParam> pageInfo = new PageInfo<>(list);
        PageResult pageResult = new PageResult();
        pageResult.setPageIndex(pageInfo.getPageNum());
        pageResult.setTotalPage(pageInfo.getPages());
        pageResult.setResult(pageInfo.getList());
        return pageResult;
    }

    public void insertItemParam(TbItemParam tbItemParam) {
        tbItemParamMapper.insertSelective(tbItemParam);
    }

    public void deleteItemParamById(Long id) {
        tbItemParamMapper.deleteByPrimaryKey(id);
    }

}
