package com.example.youxi.kfc.overview.load_cache_prase_resource;

import android.content.ContentValues;
import android.content.Context;
import android.provider.DocumentsContract;
import android.util.Log;
import android.widget.TextView;

import com.example.youxi.kfc.MyApplication;
import com.example.youxi.kfc.overview.javabean.StoreBean;
import com.example.youxi.kfc.overview.load_cache_prase_resource.cache_home_page_stores.MyDatabaseHelper;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpMethod;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by youxi on 2016-7-19.
 */
public  class LoadResource {
    private Retrofit retrofit;
    private LoadService loadService;
    private interface LoadService {
        @GET("")
        Observable<String> getResources(@Url String url);
    }

    private LoadResource() {
        OkHttpClient.Builder client=new OkHttpClient.Builder();
        client.connectTimeout(MyApplication.DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        retrofit = new Retrofit.Builder()
                .client(client.build())
                .baseUrl(MyApplication.MAIN_WEB)//http://m.5ikfc.com/
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(StringConverterFactory.create())
                .build();
        loadService=retrofit.create(LoadService.class);
    }

    private static class SingletonHolder{
        private static final LoadResource INSRANCE=new LoadResource();
    }

    //获取单例
    public static LoadResource getInstance(){return SingletonHolder.INSRANCE;}

    public static void getLoadResource(Subscriber<StoreBean> subscriber) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.MAIN_WEB)//http://m.5ikfc.com/
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(StringConverterFactory.create())
                .build();
        System.out.println("start");
        LoadService loadService = retrofit.create(LoadService.class);

        loadService.getResources("")
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<StoreBean>>() {
                    @Override
                    public Observable<StoreBean> call(String s) {
                        PraseHtml praseHtml = new PraseHtml();
                        praseHtml.handleStringForHomePage(s);
                        return Observable.from(praseHtml.hot_bean);
                    }
                })
                 //AndroidSchedulers.mainThread()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
             /*   .subscribe(new Subscriber<StoreBean>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("完成");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(e.toString());
                    }

                    @Override
                    public void onNext(StoreBean storeBean) {
                        //通过SQLite缓存结构化数据
                        MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(null, "myDict.db3", 1);
                        SqlBrite sqlBrite = SqlBrite.create();
                        BriteDatabase db = sqlBrite.wrapDatabaseHelper(myDatabaseHelper, Schedulers.io());
                        ContentValues values = new ContentValues();
                        values.put("store_name", storeBean.getStore_name());
                        values.put("store_picture", storeBean.getBitmap_url());
                        values.put("store_url", storeBean.getStore_self_url());
                        db.insert("dict", values);
                        System.out.println(storeBean.getBitmap_url());
                        *//*System.out.println(storeBean.getBitmap_url());
                        System.out.println(storeBean.getStore_name());
                        System.out.println(storeBean.getStore_self_url());*//*
                    }
                });*/
    }


    public static void main(String[] args) {
       // getLoadResource();
    }

    /**
     * 自定义Converter实现RequestBody到String的转换
     */
    public static class StringConverter implements Converter<ResponseBody, String> {

        public static final StringConverter INSTANCE = new StringConverter();

        @Override
        public String convert(ResponseBody value) throws IOException {
            return value.string();
        }
    }

    /**
     * 用于向Retrofit提供StringConverter
     */
    public static class StringConverterFactory extends Converter.Factory {

        public static final StringConverterFactory INSTANCE = new StringConverterFactory();

        public static StringConverterFactory create() {
            return INSTANCE;
        }

        // 我们只关实现从ResponseBody 到 String 的转换，所以其它方法可不覆盖
        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            if (type == String.class) {
                return StringConverter.INSTANCE;
            }
            //其它类型我们不处理，返回null就行
            return null;
        }
    }
}
