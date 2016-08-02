package com.example.youxi.kfc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.constants.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.waps.AppConnect;
import cn.waps.AppListener;
import cn.waps.UpdatePointsListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements UpdatePointsListener , View.OnClickListener ,SplashADListener{

    private final static String KFC_FAVORABLE_WED = "http://m.5ikfc.com/kfcm ";
    private String web_back;
    private OkHttpClient client;
    private Handler mHandler;
    private ArrayList<Discount> mList = new ArrayList<>();
    private ArrayList<Discount> sipnnerList = new ArrayList<>();
    private ArrayList<Discount>  setList=new ArrayList<>();
    private int num;
    private int nowProgress = 0;
    private int nowAlpha = 8;
    private int nowAlphaNum;
    private CardAdapter cardAdapter;
    private PopupWindow popupWindow;
    private ArrayAdapter<String> adapter;
    private int i = 0;
    private String[] singleString = {"蛋挞", "咖啡", "薯条", "鸡米花", "鸡腿堡", "虾"
                                        , "鸡块", "红豆派", "淇淋", "米棒", "原味鸡"
                                        , "BBQ手撕猪肉堡", "猪肉卷","可乐", "圣代",  "骨肉相连"
                                        , "香辣鸡翅","鸡肉卷","小鲜"};
    private int[] state_image=new int[4];
    private String[] m_string={"no1","no2","no3","no4"};
    private int times=0;
    private ImageView xx_image_view;
    private Button jiesuo_button;
    private int now_price=0;
    @Bind(R.id.main_activity) LinearLayout mLayout;
    @Bind(R.id.start_layout) RelativeLayout startLayout;
    @Bind(R.id.main_layout) LinearLayout mianLayout;
    @Bind(R.id.progressBar_main) ProgressBar progressBar;
    @Bind(R.id.imageView) ImageView mImageView;
    @Bind(R.id.recycler_view) RecyclerView recyclerView;
    @Bind(R.id.spinner_choose) Spinner spinner;
    @Bind(R.id.layout_spinner) LinearLayout linearLayout_spinner;
    @Bind(R.id.button2) Button button_car;
    @Bind(R.id.four_image)LinearLayout four_layout;
    @Bind(R.id.quanbu)ImageView quanbu_image;
    @Bind(R.id.zaocan)ImageView zaocan_image;
    @Bind(R.id.danpin)ImageView danpin_image;
    @Bind(R.id.taocan)ImageView taocan_image;
    @Bind(R.id.my_toolbar)Toolbar toolbar;
    @Bind(R.id.layout_jiesuo) RelativeLayout jiesuo_layout;
    //开屏广告
    @SuppressWarnings("unused")
    private SplashAD splashAD;
    private ViewGroup container;
    public boolean canJump = false;
    @Bind(R.id.guangao_layout)RelativeLayout guanggao_layout;
    //屏幕的宽高, 单位像素
    private int screenWidth;
    private int screenHeight;
    //屏幕的密度
    private float density;  //只有四种情况 : 0.75/ 1.0/ 1.5/ 2.0
    private int densityDpi; //只有四种情况 : 120/ 160/ 240/ 320
    //水平垂直精确密度
    private float xdpi; //水平方向上的准确密度, 即每英寸的像素点
    private float ydpi; //垂直方向上的准确密度, 即没音村的像素点
    //是否开启免解锁模式
    private String if_no_lock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppConnect.getInstance("bf167632b1823b2cf712845e58a45210", "QQ", this);
        ButterKnife.bind(this);
        startHttpClient();
        startAlphaImage(mImageView);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mHandler = new Handler();
        setBitmap();
        initListener();
        jiesuo_layout.setVisibility(View.GONE);
        xx_image_view= (ImageView) jiesuo_layout.findViewById(R.id.xx_image_view);
        jiesuo_button= (Button) jiesuo_layout.findViewById(R.id.jiesuo_button);
        //开屏广告
        container = (ViewGroup) this.findViewById(R.id.splash_container);
        splashAD = new SplashAD(this, container, com.example.youxi.kfc.Constants.APPID,com.example.youxi.kfc.Constants.SplashPosID, this);
        //获取设备屏幕密度进行适配
        getPixelDisplayMetricsII();
        if_no_lock=AppConnect.getInstance(this).getConfig("if_have_key","no");
        Log.d("if_no_lock",if_no_lock);
    }

    private void setBitmap() {
        SharedPreferences n=getSharedPreferences("test",MODE_PRIVATE);
        times=n.getInt("times",0);
        Log.d("times",times+"");
        AppConnect.getInstance(MainActivity.this).getPoints( MainActivity.this);
    }
    private void setBitmapSecond(){
        SharedPreferences mySharedPreferences=getSharedPreferences("test", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=mySharedPreferences.edit();
        if(times==0){
            editor.putInt("no1",1);
            editor.putInt("no2",0);
            editor.putInt("no3",0);
            editor.putInt("no4",0);
            editor.putInt("times",1);
            editor.putInt("price",now_price-350);
            editor.commit();
        }else {
            now_price=now_price-350;
            int i=now_price/70;
            Log.d("ixx",i+"XX"+now_price);
            for(int j=1;j<=i;j++){
                if(j==4) break;
                editor.putInt(m_string[j],1);
            }
            editor.commit();
        }
        SharedPreferences s=getSharedPreferences("test",MODE_PRIVATE);
        state_image[0]=s.getInt("no1",0);
        state_image[1]=s.getInt("no2",0);
        state_image[2]=s.getInt("no3",0);
        state_image[3]=s.getInt("no4",0);
        Log.d("state_image",state_image[0]+"/"+state_image[1]+"/"+state_image[2]+"/"+state_image[3]+"");
        Message m=new Message();
        m.what=4;
        xxMessageHandler.sendMessage(m);
    }

    private void initListener() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if (i == 1) {
                            initRecycler(mList);
                        }
                        break;
                    case 1:
                        Log.d("quan", "早餐");
                        my_sipnner_mlist("早餐");
                        break;
                    case 2:
                        getSingle(mList);
                        initRecycler(sipnnerList);
                        break;
                    case 3:
                        Log.d("quan", "套餐");
                        getSingle(mList);
                        initRecycler(setList);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,TestActivity.class));
            }
        });

        quanbu_image.setOnClickListener(this);
        zaocan_image.setOnClickListener(this);
        danpin_image.setOnClickListener(this);
        taocan_image.setOnClickListener(this);

        jiesuo_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jiesuo_layout.setVisibility(View.GONE);
            }
        });

        AppConnect.getInstance(this).setOffersCloseListener(new AppListener(){
            @Override
            public void onOffersClose() {
               startActivity(new Intent(MainActivity.this,MainActivity.class));
            }
        });

    }

    private void my_sipnner_mlist(String keyWords) {
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getProduct_name().indexOf(keyWords) >= 0) {
                sipnnerList.add(mList.get(i));
            }
        }
        initRecycler(sipnnerList);
    }

    private void getSingle(ArrayList<Discount> list) {
        int c = 0;
        for (int j = 0; j < list.size(); j++) {
            Discount dis = mList.get(j);
            String b = dis.getProduct_name();
            for (int i = 0; i < singleString.length; i++) {
                String a = singleString[i];
                if (b.indexOf(a) >= 0) {
                    Log.d("pro_name", a + "cc" + b);
                    c++;
                }
            }
            if (c == 1 && b.indexOf("早餐") < 0) {
                sipnnerList.add(dis);
                c = 0;
            } else if(b.indexOf("早餐")<0) {
                setList.add(dis);
                c = 0;
            }
        }

    }

    private void initRecycler(ArrayList<Discount> list) {
        //保持固定宽高，这会提高性能
        recyclerView.setHasFixedSize(true);
        //LiearLayoutManager设置滚动方向,默认是竖直方向
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        cardAdapter = new CardAdapter(list, this,ydpi);
        recyclerView.setAdapter(cardAdapter);
        //recyclerView.setVisibility(View.GONE);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        sipnnerList = null;
        sipnnerList = new ArrayList<>();
        setList=null;
        setList=new ArrayList<>();
    }

    private void initViews() {
        initRecycler(mList);
        i++;
        //spinner
        String[] str = {"全部", "早餐", "单品", "套餐"};
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_edittext, R.id.spinner_edit);
        adapter.addAll(str);
        spinner.setAdapter(adapter);
    }

    private void startHttpClient() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client = new OkHttpClient();
                    final Request request = new Request.Builder()
                            .url(KFC_FAVORABLE_WED)
                            .build();
                    Response response = client.newCall(request).execute();
                    web_back = response.body().string();
                    clearTheWebBack(web_back);
                    //开启一个线程去处理解析数据
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int i = 1;
                            while (web_back.indexOf("img") >= 0 && web_back.indexOf("<b>") >= 0
                                    && web_back.indexOf("<i>") >= 0 && web_back.indexOf("<em>") >= 0) {
                                getDiscount(web_back);
                                i++;
                            }
                            insertSort(mList);

                        }
                    }).start();

                } catch (IOException e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(mLayout, "请检查网络是否开启", Snackbar.LENGTH_LONG).setAction
                                    ("确定", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    })
                                    .show();
                        }
                    });
                }
            }
        }).start();
    }

    private void insertSort(ArrayList<Discount> mList) {

        ArrayList<Discount> k_mList = new ArrayList<>();
        ArrayList<Discount> c_mList = new ArrayList<>();

        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getNo().indexOf("C") >= 0) {
                c_mList.add(mList.get(i));
            } else if (mList.get(i).getNo().indexOf("K") >= 0) {
                k_mList.add(mList.get(i));
            }
        }
        Discount temp;
        for (int i = 1; i < c_mList.size(); i++) {
            int j = i - 1;
            temp = c_mList.get(i);
            for (; j >= 0 &&
                    Integer.valueOf(temp.getNo().substring(1))
                            < Integer.valueOf(c_mList.get(j).getNo().substring(1))
                    ; j--) {
                c_mList.set(j + 1, c_mList.get(j));
            }
            c_mList.set(j + 1, temp);
        }
        k_mList = sort(k_mList);
        c_mList = sort(c_mList);
        k_mList.addAll(c_mList);
        this.mList = k_mList;

        if (nowProgress == (num - 1)) {
            Message message2 = new Message();
            message2.what = 2;
            xxMessageHandler.sendMessage(message2);
        }
    }

    private ArrayList<Discount> sort(ArrayList<Discount> mList) {
        Discount temp;
        for (int i = 1; i < mList.size(); i++) {
            int j = i - 1;
            temp = mList.get(i);
            for (; j >= 0 &&
                    Integer.valueOf(temp.getNo().substring(1))
                            < Integer.valueOf(mList.get(j).getNo().substring(1))
                    ; j--) {
                mList.set(j + 1, mList.get(j));
            }
            mList.set(j + 1, temp);
        }
        return mList;
    }

    private void getDiscount(String to_get) {
        int begin = to_get.indexOf("div");
        StringBuilder stringBuffer = new StringBuilder(to_get);
        stringBuffer.replace(begin, begin + 2, "xxx");
        to_get = new String(stringBuffer);
        int last = to_get.indexOf("div");
        //获取了第一段的div首部和尾部的位置
        String cutOutString = web_back.substring(begin, last);
        Log.d("cut", cutOutString);

        while (cutOutString.indexOf("img") >= 0 && cutOutString.indexOf("<b>") >= 0
                && cutOutString.indexOf("<i>") >= 0 && cutOutString.indexOf("<em>") >= 0) {
            //构建对象
            Discount discount = new Discount();
            //获取图片地址
            int src = cutOutString.indexOf("src=");
            int src2 = cutOutString.indexOf("alt");
            discount.setImg(cutOutString.substring(src + 5, src2 - 2));
            Log.d("img2", cutOutString.substring(src + 5, src2 - 2));
            cutOutString = cutOutString.substring(src2);
            //获取商品描述
            int b = cutOutString.indexOf("<b>");
            int b2 = cutOutString.indexOf("</b>");
            StringBuilder describe2 = new StringBuilder(cutOutString.substring(b + 3, b2));
            discount.setNo(describe2.toString());
            int i = cutOutString.indexOf("<i>");
            StringBuilder describe = new StringBuilder(cutOutString.substring(b2 + 5, i));
            try {
                int end = describe.toString().indexOf("201");
                String s = describe.toString().substring(0, end - 1);
                if (s.indexOf("凭此") >= 0) {
                    s = s.substring(0, s.indexOf("凭此") - 1);
                }
                discount.setProduct_name(s);
                Log.d("name", s);
            } catch (Exception e) {
                String s = describe.toString();
                if (s.indexOf("凭此") >= 0) {
                    s = s.substring(0, s.indexOf("凭此") - 1);
                }
                discount.setProduct_name(s);
                Log.d("name", s);
            }
            //获取商品价格
            int i2 = cutOutString.indexOf("</i>");
            discount.setProduct_price(cutOutString.substring(i + 3, i2));
            Log.d("price", cutOutString.substring(i + 3, i2));
            //获取有效日期
            int em = cutOutString.indexOf("<em>");
            int em2 = cutOutString.indexOf("</em>");
            Calendar calender = Calendar.getInstance();
            discount.setValidity("有效期至" + calender.get(Calendar.YEAR) + cutOutString.substring(em + 4, em2));
            Log.d("in", "有效期至" + calender.get(Calendar.YEAR) + cutOutString.substring(em + 4, em2));
            mList.add(discount);
            Log.d("if", "成功了");
            //把第一段代码切除
            web_back = web_back.substring(last + 3);
            Log.d("change", web_back);
            /*try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            nowProgress++;
            Message message = new Message();
            message.what = 1;
            xxMessageHandler.sendMessage(message);
            Log.d("ppp", nowProgress + "");
        }
    }

    //去除开头无效的div三个
    private void clearTheWebBack(String web_back) {
        int begin;
        for (int i = 0; i < 3; i++) {
            begin = web_back.indexOf("div");
            web_back = web_back.substring(begin + 2);
        }
        int last = web_back.indexOf("<div class=\"page\"><a href=\"#\" class=\"fl back\">返&nbsp;回</a>");
        web_back = web_back.substring(0, last);
        this.web_back = web_back;
        num = getTheNum(web_back);
        intProgress();
    }

    private void intProgress() {
        progressBar.setMax((num - 1) * 10);
        progressBar.setProgress(0);
    }

    private int getTheNum(String to_get) {
        int i = 1;
        while (to_get.indexOf("img") >= 0 && to_get.indexOf("<b>") >= 0
                && to_get.indexOf("<i>") >= 0 && to_get.indexOf("<em>") >= 0) {
            int begin = to_get.indexOf("div");
            StringBuilder stringBuffer = new StringBuilder(to_get);
            stringBuffer.replace(begin, begin + 2, "xxx");
            to_get = new String(stringBuffer);
            int last = to_get.indexOf("div");
            to_get = to_get.substring(last + 3);
            i++;
        }
        return i;
    }

    private void startAlphaImage(ImageView image) {
        image.setAlpha(0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 30; i++) {
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = new Message();
                    message.what = 3;
                    nowAlpha = 8 * i;
                    Log.d("alpha", nowAlpha + "");
                    xxMessageHandler.sendMessage(message);
                    nowAlphaNum = i;
                }
            }
        }).start();
    }

    private Handler xxMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    progressBar.setProgress(10 * nowProgress);
                    break;
                case 2:
                    four_layout.setVisibility(View.VISIBLE);
                    startLayout.setVisibility(View.GONE);
                    //mianLayout.setVisibility(View.VISIBLE);
                    //initViews();
                    break;
                case 3:
                    if (nowAlphaNum == 30) {
                        mImageView.setAlpha(255);
                    } else {
                        mImageView.setAlpha(nowAlpha);
                    }
                    break;
                case 4:
                    if(state_image[1]==1&&if_no_lock.equals("no")){
                        zaocan_image.setImageResource(R.drawable.zaocan);
                    }
                    if ((state_image[2]==1)&&if_no_lock.equals("no")){
                        danpin_image.setImageResource(R.drawable.danpin);
                    }
                    if (state_image[3]==1&&if_no_lock.equals("no")){
                        taocan_image.setImageResource(R.drawable.taocan);
                    }
                    Log.d("id_have2","xxx"+if_no_lock);
                    if (if_no_lock.equals("yes")){
                        zaocan_image.setImageResource(R.drawable.zaocan);
                        danpin_image.setImageResource(R.drawable.danpin);
                        taocan_image.setImageResource(R.drawable.taocan);
                    }
                    break;

            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if(item.getItemId()==android.R.id.home){
           mianLayout.setVisibility(View.GONE);
           four_layout.setVisibility(View.VISIBLE);
           return true;
       }
        return super.onOptionsItemSelected(item);
    }
    private void jiesuoset(){
        xx_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jiesuo_layout.setVisibility(View.GONE);
                //Toast.makeText(MainActivity.this,"退出",Toast.LENGTH_LONG).show();
            }
        });
        jiesuo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConnect.getInstance(MainActivity.this).showOffers(MainActivity.this);
                jiesuo_layout.setVisibility(View.GONE);
            }
        });
    }
    private int getScore(){
        AppConnect.getInstance(MainActivity.this).getPoints( MainActivity.this);
        return 1;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.quanbu:
                four_layout.setVisibility(View.GONE);
                mianLayout.setVisibility(View.VISIBLE);
                toolbar.setTitle("肯德基全部优惠券");
                initViews();
                break;
            case R.id.zaocan:
                if(state_image[1]==1||if_no_lock.equals("yes")){
                    four_layout.setVisibility(View.GONE);
                    mianLayout.setVisibility(View.VISIBLE);
                    toolbar.setTitle("早餐");
                    my_sipnner_mlist("早餐");
                }else {
                   jiesuo_layout.setVisibility(View.VISIBLE);
                    jiesuoset();
                    /*Snackbar.make(mLayout, "此款App是完全免费的，开发者付出业余时间给大家制作的。" +
                            "所以帮个忙嘛>3<  ，击解锁按钮随便下载软件后，删除即可~谢谢你╭(╯3╰)╮", Snackbar.LENGTH_LONG).show();*/
                }
                break;
            case R.id.danpin:
                if(state_image[2]==1||if_no_lock.equals("yes")){
                    four_layout.setVisibility(View.GONE);
                    mianLayout.setVisibility(View.VISIBLE);
                    toolbar.setTitle("单品");
                    getSingle(mList);
                    initRecycler(sipnnerList);
                }else {
                    jiesuo_layout.setVisibility(View.VISIBLE);
                    jiesuoset();
                }

                break;
            case R.id.taocan:
                if(state_image[3]==1||if_no_lock.equals("yes")){
                    four_layout.setVisibility(View.GONE);
                    mianLayout.setVisibility(View.VISIBLE);
                    toolbar.setTitle("套餐");
                    getSingle(mList);
                    initRecycler(setList);
                }else {
                    jiesuo_layout.setVisibility(View.VISIBLE);
                    jiesuoset();
                }
                break;
        }
    }

    @Override
    public void getUpdatePoints(String s, int i) {
        now_price=i;
        Log.d("price1",now_price+"");
        setBitmapSecond();
    }

    @Override
    public void getUpdatePointsFailed(String s) {
        SharedPreferences x=getSharedPreferences("test",MODE_PRIVATE);
        now_price=x.getInt("price",0);
        Log.d("price2",now_price+"");
        setBitmapSecond();
    }

    @Override
    public void onADPresent() {
        Log.i("AD_DEMO", "SplashADPresent");
    }
    @Override
    public void onADClicked() {
        Log.i("AD_DEMO", "SplashADClicked");
    }
    @Override
    public void onADDismissed() {
        Log.i("AD_DEMO", "SplashADDismissed");
        next();
    }
    @Override
    public void onNoAD(int i) {
        next();
    }
    private void next() {
        if (canJump) {
            guanggao_layout.setVisibility(View.GONE);
            four_layout.setVisibility(View.VISIBLE);
        } else {
            canJump = true;
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        canJump = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (canJump) {
            next();
        }
        canJump = true;
    }

    /** 开屏页最好禁止用户对返回按钮的控制 */
    int first_second;
    int second_second;
    int ii=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK&&ii==0) {
            Toast.makeText(MainActivity.this,"再按一次返回键退出",Toast.LENGTH_SHORT).show();
            Calendar mCalendar=Calendar.getInstance();
            first_second=mCalendar.get(Calendar.SECOND);
            Log.d("second",first_second+"");
            ii++;
            return true;
        }else if(keyCode == KeyEvent.KEYCODE_BACK&&ii==1) {
            Calendar mCalendar2=Calendar.getInstance();
            second_second=mCalendar2.get(Calendar.SECOND);
            Log.d("second",second_second+"---"+Math.abs(second_second-first_second));
            if(Math.abs(second_second-first_second)<=1.5){
                finish();
            }else {
                ii=0;
            }
        }
        //return super.onKeyDown(keyCode, event);
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppConnect.getInstance(this).close();
    }

    private void getPixelDisplayMetricsII() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        density = dm.density;
        densityDpi = dm.densityDpi;

        xdpi = dm.xdpi;
        ydpi = dm.ydpi;
    }
}
