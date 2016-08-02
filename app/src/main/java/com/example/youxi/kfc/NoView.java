package com.example.youxi.kfc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by youxi on 2016-6-10.
 */
public class NoView extends View {
    public NoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        //#00 ff ff ff
        //设置画笔
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        //paint.setColor(0x2E6DBF);
        paint.setARGB(255,73,60,60);
        //0xFFFF0000 2e6dbf
        paint.setStrokeWidth(4);
        int viewWidth=this.getWidth();
        int viewHeight=this.getHeight();
        //设置位置
        Path path=new Path();
        path.moveTo(0,0);
        path.lineTo(0, (float) (viewHeight*0.618));
        path.lineTo((float) (viewWidth*0.5),viewHeight);
        path.lineTo(viewWidth, (float) (viewHeight*0.618));
        path.lineTo(viewWidth,0);
        path.lineTo(0,0);
        canvas.drawPath(path,paint);

    }
}
