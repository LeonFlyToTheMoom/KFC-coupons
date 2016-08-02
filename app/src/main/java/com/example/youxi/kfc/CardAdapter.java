package com.example.youxi.kfc;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by youxi on 2016-6-6.
 */
public class CardAdapter extends RecyclerView.Adapter {
    private Context context;
    private float  ydpi;


    public  static  interface OnRecylerViewListener{
        void onItemClick(int position);
    }

    private OnRecylerViewListener onRecylerViewListener;

    public void setOnRecylerViewListener(OnRecylerViewListener onRecylerViewListener){
        this.onRecylerViewListener=onRecylerViewListener;

    }

    private ArrayList<Discount> mList;

    public CardAdapter(ArrayList<Discount> mList,Context context,float  ydpi){
        this.mList=mList;
        this.context=context;
        this.ydpi=ydpi;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fsfasf,null);
        int yy= (int) (150*ydpi/160);
        //510
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, yy);
        lp.setMargins(50,0,50,5);
        view.setLayoutParams(lp);
        return  new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
       CardViewHolder holder1= (CardViewHolder) holder;
        holder1.content_textView.setText(mList.get(position).getNo()+mList.get(position).getProduct_name());
        holder1.price_textView.setText(mList.get(position).getProduct_price());
        holder1.date_textView.setText(mList.get(position).getValidity());
        holder1.no_textView.setText(mList.get(position).getNo());
        Glide.with(context)
                .load(mList.get(position).getImg())
                .centerCrop()
                .into(holder1.imageView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        public View rootView;
        public ImageView imageView;
        public TextView no_textView;
        public TextView content_textView;
        public TextView price_textView;
        public TextView date_textView;
        public Button get_button;

        public CardViewHolder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.card_imageView);
            no_textView= (TextView) itemView.findViewById(R.id.no_textView);
            content_textView= (TextView) itemView.findViewById(R.id.content_textView);
            price_textView= (TextView) itemView.findViewById(R.id.price_textView);
            date_textView= (TextView) itemView.findViewById(R.id.date_textView);
            get_button= (Button) itemView.findViewById(R.id.buy_button);
            //整一个布局
            rootView=itemView.findViewById(R.id.mian_card_layout);
            rootView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

}
