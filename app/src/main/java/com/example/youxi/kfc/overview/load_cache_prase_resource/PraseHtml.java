package com.example.youxi.kfc.overview.load_cache_prase_resource;

import com.example.youxi.kfc.MyApplication;
import com.example.youxi.kfc.overview.javabean.StoreBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by youxi on 2016-7-20.
 */
public  class PraseHtml {
    StoreBean[] hot_bean;
    StoreBean[] phone_bean;

    public void handleStringForHomePage(String s) {
        int indexHot = s.indexOf("热门优惠券");
        int indexPhone = s.indexOf("可手机出示的优惠券");
        int indexPrint = s.indexOf("需打印的优惠券");
        System.out.println(indexHot+"/"+indexPhone+"/"+indexPrint);

        String string_hot_collection = s.substring(indexHot, indexPhone);
        System.out.println("hot---------------"+string_hot_collection);
        String string_phone_collection = s.substring(indexPhone, indexPrint);
        System.out.println("phone---------------"+string_phone_collection);

        int num_hot_times = getStringFragmentTimes(string_hot_collection, "<a");
        int num_phone_times = getStringFragmentTimes(string_phone_collection, "<a");
        System.out.println(""+num_hot_times+"/"+num_phone_times);

        hot_bean = new StoreBean[num_hot_times];
        phone_bean = new StoreBean[num_phone_times];
        //用jsoup解析一个片段
        Document doc_hot = Jsoup.parseBodyFragment(string_hot_collection);
        Document doc_phone = Jsoup.parseBodyFragment(string_phone_collection);
        forStroreBean(num_hot_times, hot_bean, doc_hot);
        forStroreBean(num_phone_times, phone_bean, doc_phone);
    }

    private void forStroreBean(int times, StoreBean[] collection, Document doc) {
        for (int i = 0; i < times; i++) {
            //<a href="/mdl/" title="麦当劳优惠券"><img src="http://static.5ikfc.com/m/brand/mdl.gif" alt="麦当劳" />麦当劳</a>
            Element link = doc.select("a").get(i);
            StoreBean storeBean = new StoreBean();
            storeBean.setStore_name(link.text());
            storeBean.setBitmap_url(link.select("img").attr("src"));
            storeBean.setStore_self_url(MyApplication.MAIN_WEB_COP + link.attr("href"));
            collection[i] = storeBean;
        }
    }

    public static int getStringFragmentTimes(String s, String find_s) {
        int j = 0;
        System.out.println(s.length());
        for (int i = 0; i < s.length(); i++) {
            //System.out.println(s.indexOf(find_s,i));
            if (s.indexOf(find_s, i) >= 0&&s.indexOf(find_s,i)!=s.indexOf(find_s,i-1)||(s.indexOf(find_s,i)>=0)&i==0) {
                j++;
            }
        }
        return j;
    }
    public static void main(String[] args){
        System.out.println(getStringFragmentTimes("abcxxxxxabcgfagfaasabcfgsgdsabcabc","abc")+"");
    }
}
