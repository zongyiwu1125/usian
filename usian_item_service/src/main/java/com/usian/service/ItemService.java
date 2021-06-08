package com.usian.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.dto.ItemDto;
import com.usian.mapper.TbItemCatMapper;
import com.usian.mapper.TbItemDescMapper;
import com.usian.mapper.TbItemMapper;
import com.usian.mapper.TbItemParamItemMapper;
import com.usian.pojo.*;
import com.usian.util.RedisClient;
import com.usian.utils.IDUtils;
import com.usian.utils.PageResult;
import com.usian.vo.TbItemVo;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemService {
    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private TbItemDescMapper tbItemDescMapper;

    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Autowired
    private TbItemParamItemMapper itemParamItemMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RedisClient redisClient;



    public TbItem selectItemInfo(Long itemId) {
        TbItem item = (TbItem) redisClient.get("ITEM_"+itemId);
        if (item!=null){
            return item;
        }

        item = tbItemMapper.selectByPrimaryKey(itemId);
        if (item==null){
            redisClient.set("ITEM_"+itemId,new TbItem());
        }else {
            redisClient.set("ITEM_"+itemId,item);
        }
        return item;
    }

    public PageResult selectTbItemAllByPage(Integer page, Integer rows) {
        PageHelper.startPage(page,rows);
        List<TbItem> list = tbItemMapper.selectAllOrderByCreated();
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        PageResult pageResult = new PageResult();
        pageResult.setPageIndex(pageInfo.getPageNum());
        pageResult.setTotalPage(pageInfo.getPages());
        pageResult.setResult(pageInfo.getList());
        return pageResult;
    }

    @Transactional
    public void insertTbItem(TbItemVo tbItemVo) {
        //1.添加item表
        long itemId = IDUtils.genItemId();
        Date now = new Date();

        tbItemVo.setId(itemId);
        tbItemVo.setCreated(now);
        tbItemVo.setStatus((byte)1);
        tbItemMapper.insertSelective(tbItemVo);

        //2.添加item_desc表
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setItemDesc(tbItemVo.getDesc());
        tbItemDesc.setCreated(now);
        tbItemDescMapper.insertSelective(tbItemDesc);

        //3.添加item_param_item表
        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        tbItemParamItem.setItemId(itemId);
        tbItemParamItem.setCreated(now);
        tbItemParamItem.setParamData(tbItemVo.getItemParams());

        ItemDto itemDto = new ItemDto();
        itemDto.setItem(tbItemVo);
        itemDto.setTbItemDesc(tbItemDesc);
        TbItemCat tbItemCat = itemCatMapper.selectByPrimaryKey(tbItemVo.getCid());
        itemDto.setItemCategoryName(tbItemCat.getName());

        amqpTemplate.convertAndSend("item_exchange","item.insert",itemDto);

        tbItemParamItemMapper.insertSelective(tbItemParamItem);
    }

    public Map<String, Object> preUpdateItem(Long itemId) {
        HashMap<String, Object> map = new HashMap<>();
        //先查item

        TbItem tbItem1 = tbItemMapper.selectByPrimaryKey(itemId);//查询出的item

        TbItemCat tbItemCat1 = tbItemCatMapper.selectByPrimaryKey(tbItem1.getCid());//查询出的itemCat

        //再查itemDesc
        TbItemDesc tbItemDesc1 = tbItemDescMapper.selectByPrimaryKey(itemId);//查询出的itemDesc

        //再查itemParamItem
        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        tbItemParamItem.setItemId(itemId);
        TbItemParamItem tbItemParamItem1 = tbItemParamItemMapper.selectOne(tbItemParamItem);//查询出的tbItemParamItem

        map.put("itemCat",tbItemCat1);
        map.put("item",tbItem1);
        map.put("itemDesc",tbItemDesc1);
        map.put("itemParamItem",tbItemParamItem1);
        return map;
    }

    @Transactional
    public void deleteItemById(Long itemId) {
        TbItem tbItem = new TbItem();
        tbItem.setId(itemId);
        tbItem.setStatus((byte)3);
        tbItemMapper.updateByPrimaryKeySelective(tbItem);

        tbItemDescMapper.deleteByPrimaryKey(itemId);

        tbItemParamItemMapper.deleteByItemId(itemId);

        amqpTemplate.convertAndSend("item_exchange","item.delete",itemId+"");

    }

    @Transactional
    public void updateTbItem(TbItemVo tbItemVo) {
        Date nowUpdate = new Date();
        tbItemVo.setUpdated(nowUpdate);
        //修改商品
        tbItemMapper.updateByPrimaryKeySelective(tbItemVo);

        //修改描述
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemDesc(tbItemVo.getDesc());
        tbItemDesc.setItemId(tbItemVo.getId());
        tbItemDescMapper.updateByPrimaryKeySelective(tbItemDesc);

        //修改主题
        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        tbItemParamItem.setItemId(tbItemVo.getId());
        tbItemParamItem.setParamData(tbItemVo.getItemParams());
        tbItemParamItemMapper.updateByPrimaryKeySelective(tbItemParamItem);
    }


    public TbItemParamItem selectTbItemParamItemByItemId(Long itemId) {
        TbItemParamItem itemParamItem = (TbItemParamItem) redisClient.get("ITEM_PARAM_ITEM"+itemId);
        if (itemParamItem!=null){
            return itemParamItem;
        }

        itemParamItem = itemParamItemMapper.selectByItemId(itemId);
        if (itemParamItem==null){
            redisClient.set("ITEM_PARAM_ITEM_"+itemId,new TbItemParamItem());
        }else {
            redisClient.set("ITEM_PARAM_ITEM_"+itemId,itemParamItem);
        }
        return itemParamItem;
    }

    public TbItemDesc selectItemDescByItemId(Long itemId) {
        TbItemDesc itemDesc = (TbItemDesc) redisClient.get("ITEM_DESC"+itemId);
        if (itemDesc!=null){
            return itemDesc;
        }

        itemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
        if (itemDesc==null){
            redisClient.set("ITEM_DESC_"+itemId,new TbItemDesc());
        }else {
            redisClient.set("ITEM_DESC_"+itemId,itemDesc);
        }
        return itemDesc;
    }

    public void updateItemNumByItemId(Map<String, Integer> map) {
        for (String itemId : map.keySet()) {
            Integer num = map.get(itemId);
            tbItemMapper.updateItemNumByItemId(itemId,num);
        }

    }
}
