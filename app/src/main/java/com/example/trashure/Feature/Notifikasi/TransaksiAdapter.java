package com.example.trashure.Feature.Notifikasi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trashure.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TransaksiAdapter extends RecyclerView.Adapter<TransaksiAdapter.ViewHolder>{
    private List<TransaksiModel> mLists = new ArrayList<>();
    private DatabaseReference transaksiRefs;
    private Context mContext;
    private FragmentActivity mActivity;
    private TransaksiDetail transaksiDetail;
    private String PATTERN_1 = "dd/MM/yyyy";
    private String PATTERN_2 = "HH.mm";
    private String PATTERN_3 = "dd MMMM";
    private String PATTERN_4 = "yyyy";

    public TransaksiAdapter(List<TransaksiModel> mLists, Context mContext, FragmentActivity mActivity) {
        this.mLists = mLists;
        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_transaksi,viewGroup,false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final int position = i;
        final TransaksiModel list = mLists.get(i);
        viewHolder.tvItemListTransaksiTitle.setText(list.getTitle());
        viewHolder.tvItemListTransaksiDesc.setText(list.getDesc()+" ...");
        viewHolder.ivItemListTransaksi.setImageResource(list.getImage_url());


        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat1 = new SimpleDateFormat(PATTERN_1);
        SimpleDateFormat dateFormat2 = new SimpleDateFormat(PATTERN_2);
        SimpleDateFormat dateFormat3 = new SimpleDateFormat(PATTERN_3);
        SimpleDateFormat dateFormat4 = new SimpleDateFormat(PATTERN_4);

        String date =  dateFormat1.format(now);
        try{
            if (list.getDate().equalsIgnoreCase(date)){
                viewHolder.tvItemListTransaksiDate.setText(list.getHour());
            }else if (dateFormat4.format(dateFormat1.parse(list.getDate())).equals(dateFormat4.format(now))){
                viewHolder.tvItemListTransaksiDate.setText(dateFormat3.format(dateFormat1.parse(list.getDate())));
            }else{
                viewHolder.tvItemListTransaksiDate.setText(dateFormat1.format(dateFormat1.parse(list.getDate())));
            }
        }catch (ParseException e){
            Log.d("PARSE ERROR",e.getMessage());
        }

        if (list.isRead()){
            viewHolder.ivItemDotListTransaksi.setVisibility(View.INVISIBLE);
            viewHolder.tvItemListTransaksiDate.setVisibility(View.VISIBLE);
        }else{
            viewHolder.ivItemDotListTransaksi.setVisibility(View.VISIBLE);
            viewHolder.tvItemListTransaksiDate.setVisibility(View.INVISIBLE);
        }

        viewHolder.cvItemListTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(mContext,MainActivity.class);
                mContext.startActivity(intent);*/
                transaksiRefs = FirebaseDatabase.getInstance().getReference().child("Transaksi").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(list.getId());

                HashMap transaksiMap = new HashMap();
                transaksiMap.put("read",true);
                transaksiRefs.updateChildren(transaksiMap);
                transaksiDetail = new TransaksiDetail();
                transaksiDetail.setTitle(list.getTitle());
                transaksiDetail.setDate(list.getDate());
                transaksiDetail.setDesc(list.getDesc());
                transaksiDetail.setNoHp(list.getNoHp());
                transaksiDetail.setSaldo(list.getSaldo());
                transaksiDetail.setOperator(list.getProvider());
                //transaksiDetail.setmList(mLists);
                FragmentTransaction fragmentTransaction = mActivity.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameFragment,transaksiDetail).addToBackStack(null).commit();
            }
        });
    }
    @Override
    public int getItemCount() {
        return mLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cvItemListTransaksi;
        ImageView ivItemListTransaksi;
        ImageView ivItemDotListTransaksi;
        TextView tvItemListTransaksiDate;
        TextView tvItemListTransaksiTitle;
        TextView tvItemListTransaksiDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cvItemListTransaksi = itemView.findViewById(R.id.cvItemListTransaksi);
            ivItemListTransaksi = itemView.findViewById(R.id.iv_transaksi);
            ivItemDotListTransaksi = itemView.findViewById(R.id.transaksi_badge);
            tvItemListTransaksiDate = itemView.findViewById(R.id.date_transaksi);
            tvItemListTransaksiTitle = itemView.findViewById(R.id.title_transaksi);
            tvItemListTransaksiDesc = itemView.findViewById(R.id.ket_transaksi);
        }
    }
}