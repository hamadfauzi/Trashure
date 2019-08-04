package com.example.trashure.Feature.Beranda;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.trashure.R;
import com.example.trashure.RoundedCornersTransformation;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.ViewHolder>{

    private List<TipsModel> mList = new ArrayList<>();
    private Context mContext;
    private FragmentActivity mActivity;

    public TipsAdapter(List<TipsModel> mList, Context mContext, FragmentActivity mActivity) {
        this.mList = mList;
        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();

        View v = LayoutInflater.from(mContext).inflate(R.layout.item_tips,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final int position = i;
        final TipsModel model = mList.get(i);

        viewHolder.tvJudul.setText(model.getJudul());

        final int radius = 20;
        final int margin = 20;
        final Transformation transformation = new RoundedCornersTransformation(radius, margin);

        Picasso.with(mContext).load(model.getGambar()).transform(transformation).into(viewHolder.ivTips);
        //Picasso.with(mContext).load(model.getGambar()).into(viewHolder.ivTips);

        viewHolder.rlTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(mContext,TipsDetail.class);
                detailIntent.putExtra("deskripsi",model.getDeskripsi());
                detailIntent.putExtra("tgl",model.getTgl());
                detailIntent.putExtra("gambar",model.getGambar());
                detailIntent.putExtra("judul",model.getJudul());
                mContext.startActivity(detailIntent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        ImageView ivTips;
        TextView tvJudul;
        RelativeLayout rlTips;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivTips = (ImageView) itemView.findViewById(R.id.iv_tips);
            tvJudul = (TextView) itemView.findViewById(R.id.tv_judul_tips);
            rlTips = (RelativeLayout) itemView.findViewById(R.id.rl_tips);

        }
    }

}
