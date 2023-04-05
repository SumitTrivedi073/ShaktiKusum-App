package com.shaktipumplimited.shaktikusum.bean;

/**
 * Created by Administrator on 12/30/2016.
 */
public class ItemNameBean {
    public String item_id, item_name;

    public ItemNameBean() {}

    public ItemNameBean(String item_id_text,
                        String item_name_text) {
        item_id = item_id_text;
        item_name = item_name_text;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }
}
