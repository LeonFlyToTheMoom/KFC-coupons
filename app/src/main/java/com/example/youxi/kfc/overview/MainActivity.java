package com.example.youxi.kfc.overview;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.youxi.kfc.R;
import com.example.youxi.kfc.overview.javabean.StoreBean;
import com.example.youxi.kfc.overview.load_cache_prase_resource.LoadResource;
import com.example.youxi.kfc.overview.load_cache_prase_resource.cache_home_page_stores.MyDatabaseHelper;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private Subscriber subscriber;
    private StringBuffer stringBuffer=new StringBuffer("");
    private MyDatabaseHelper myDatabaseHelper;
    private  BriteDatabase db;
    @Bind(R.id.button_load)
    Button button;
    @Bind(R.id.textView_load)
    TextView textView;
    @Bind(R.id.button_query) Button button_query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        //初始化数据库
        myDatabaseHelper = new MyDatabaseHelper(this, "myDict.db3", 1);
        SqlBrite sqlBrite = SqlBrite.create();
        db = sqlBrite.wrapDatabaseHelper(myDatabaseHelper, Schedulers.io());
    }

    @OnClick(R.id.button_load)
    public void click() {
        getResource();
    }
    @OnClick(R.id.button_query)
    public void click_query(){
        //queryDataFromSQLite();
        Cursor cursor=myDatabaseHelper.getReadableDatabase().rawQuery("select * from dict",null);
        stringBuffer.append(cursor.getString(1));
        textView.setText(stringBuffer.toString());
    }

    private void getResource() {
        subscriber = new Subscriber<StoreBean>() {
            @Override
            public void onCompleted() {
                System.out.println("完成");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("错误：" + e.toString());
            }

            @Override
            public void onNext(StoreBean storeBean) {
                stringBuffer.append(storeBean.getBitmap_url()+"\n");
                textView.setText(stringBuffer.toString());
                saveDataBySQLite(storeBean);
            }
        };
        LoadResource.getInstance().getLoadResource(subscriber);
    }

    private void saveDataBySQLite(StoreBean storeBean){
        ContentValues values = new ContentValues();
        values.put("store_name", storeBean.getStore_name());
        values.put("store_picture", storeBean.getBitmap_url());
        values.put("store_url", storeBean.getStore_self_url());
        db.insert("dict", values);
    }
    /*
    *  "create table dict(store_name varchar(255) primary key ,"
            +" store_picture varchar(255) ,"
            +" store_url varchar(255))";
    * */
    private StoreBean queryDataFromSQLite(){
        //查询：query是结果
        Observable<SqlBrite.Query> users = db.createQuery("dict", "SELECT * FROM dict");
        users.subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SqlBrite.Query>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                      Log.d("e",e.toString());
                    }

                    @Override
                    public void onNext(SqlBrite.Query query) {
                        Cursor cursor = query.run();
                        StoreBean storeBean=new StoreBean();
                        storeBean.setStore_name(cursor.getString(0));
                        storeBean.setBitmap_url(cursor.getString(1));
                        storeBean.setStore_self_url(cursor.getString(2));
                        stringBuffer.append(cursor.getString(0));
                    }
                });
        /*users .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<SqlBrite.Query>() {
            @Override
            public void call(SqlBrite.Query query) {
                Cursor cursor = query.run();
                StoreBean storeBean=new StoreBean();
                storeBean.setStore_name(cursor.getString(0));
                storeBean.setBitmap_url(cursor.getString(1));
                storeBean.setStore_self_url(cursor.getString(2));
                stringBuffer.append(cursor.getString(0));
            }
        });*/
        return null;
    }
}
