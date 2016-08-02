package com.example.youxi.kfc.overview.load_cache_prase_resource.cache_home_page_stores;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by youxi on 2016-7-20.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    private final static String CREATE_TABLE_SQL=
            "create table dict(store_name varchar(255) primary key ,"
            +" store_picture varchar(255),"
            +" store_url varchar(255))";

    private final static String TAG="sql";

    public MyDatabaseHelper(Context context, String name, int version) {
        super(context, name,null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //第一次使用数据库时自动创建表
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG,oldVersion+"-->"+newVersion);
    }
}
