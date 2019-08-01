package com.example.trashure.Feature.Beranda;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trashure.R;

import java.util.ArrayList;
import java.util.List;

public class SetoranAdapter extends RecyclerView.Adapter<SetoranAdapter.ViewHolder>{

    private List<SetoranModel> mList = new ArrayList<>();
    private Context mContext;
    private FragmentActivity mActivity;

    public SetoranAdapter(List<SetoranModel> mList, Context mContext, FragmentActivity mActivity) {
        this.mList = mList;
        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();

        View v = LayoutInflater.from(mContext).inflate(R.layout.list_setoran,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final int position = i;
        final SetoranModel model = mList.get(i);

        viewHolder.tvItemId.setText(model.getTrashbagId());
        viewHolder.tvItemDate.setText(model.getDate());

        if (model.getDesc().equalsIgnoreCase("Selesai")){
            viewHolder.tvItemDesc.setTextColor(mActivity.getResources().getColor(R.color.colorAccent));
        }else{
            viewHolder.tvItemDesc.setTextColor(mActivity.getResources().getColor(R.color.colorBtn));
        }
        viewHolder.tvItemDesc.setText(model.getDesc());

        int gambar = mActivity.getResources().getIdentifier("iv_setoran","drawable",mActivity.getPackageName());
        viewHolder.ivItemSetoran.setImageResource(gambar);

        viewHolder.cvItemListSetoran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView cvItemListSetoran;
        ImageView ivItemSetoran;
        TextView tvItemId, tvItemDate, tvItemDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cvItemListSetoran = (CardView) itemView.findViewById(R.id.cvItemListSetoran);
            ivItemSetoran = (ImageView) itemView.findViewById(R.id.iv_setoran);
            tvItemId = (TextView) itemView.findViewById(R.id.tv_tb_id);
            tvItemDate = (TextView) itemView.findViewById(R.id.tv_setoran_date);
            tvItemDesc = (TextView) itemView.findViewById(R.id.setoran_desc);
        }
    }

}
