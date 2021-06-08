package com.usian.dto;

import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemDesc;

import java.io.Serializable;

public class ItemDto implements Serializable {
    private TbItem item;
    private TbItemDesc tbItemDesc;
    private String itemCategoryName;

    public TbItem getItem() {
        return item;
    }

    public void setItem(TbItem item) {
        this.item = item;
    }

    public TbItemDesc getTbItemDesc() {
        return tbItemDesc;
    }

    public void setTbItemDesc(TbItemDesc tbItemDesc) {
        this.tbItemDesc = tbItemDesc;
    }

    public String getItemCategoryName() {
        return itemCategoryName;
    }

    public void setItemCategoryName(String itemCategoryName) {
        this.itemCategoryName = itemCategoryName;
    }
}
