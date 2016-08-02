package com.example.youxi.kfc;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import cn.waps.AppConnect;

/**
 * Created by youxi on 2016-6-12.
 */
public class StartActiviy extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        // 初始化统计器，并通过代码设置APP_ID, APP_PID
        AppConnect.getInstance("09f277ca386ee99cb4c910e09f562112", "waps", this);
       // AppConnect.getInstance("APP_ID","APP_PID",this);
        AppConnect.getInstance(this).showOffers(this);
    }
}
