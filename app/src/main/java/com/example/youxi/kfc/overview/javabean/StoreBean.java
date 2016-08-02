package com.example.youxi.kfc.overview.javabean;

/**
 * Created by youxi on 2016-7-19.
 */
public class StoreBean {


    /**
     * store_name : kfc
     * bitmap_url : www.kfc.com/xx.png
     * store_self_url : www.kfc.com/
     */

    private String store_name;
    private String bitmap_url;
    private String store_self_url;

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getBitmap_url() {
        return bitmap_url;
    }

    public void setBitmap_url(String bitmap_url) {
        this.bitmap_url = bitmap_url;
    }

    public String getStore_self_url() {
        return store_self_url;
    }

    public void setStore_self_url(String store_self_url) {
        this.store_self_url = store_self_url;
    }
}
