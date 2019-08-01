package com.example.trashure.Feature.Notifikasi;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trashure.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransaksiDetail extends Fragment {

    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private String title,desc,date,operator,noHp,berhasil,saldo;
    private TextView tv_title,tv_subTitle,tv_desc,tv_date;
    private List<TransaksiModel> mList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transaksi_detail,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        eventFragmentAkun();
    }

    private void eventFragmentAkun() {
        initialize();
    }

    private void setToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mList.clear();
                getActivity().onBackPressed();
            }
        });
    }

    private void initialize(){
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_transaksi_detail);
        setToolbar();
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottomNavBar);
        bottomNavigationView.setVisibility(View.GONE);
        tv_title = (TextView) getActivity().findViewById(R.id.title_transaksi_detail);
        tv_subTitle = (TextView) getActivity().findViewById(R.id.sub_title_transaksi_detail);
        tv_date = (TextView) getActivity().findViewById(R.id.date_transaksi_detail);
        tv_desc = (TextView) getActivity().findViewById(R.id.desc_transaksi_detail);
        berhasil = getActivity().getResources().getString(R.string.berhasil_ditukar);

        String mySaldo = NumberFormat.getInstance(Locale.ITALIAN).format(Integer.valueOf(saldo));
        tv_title.setText(title);
        tv_subTitle.setText(title);
        tv_date.setText(date);
        tv_desc.setText(desc+" "+operator+" "+noHp+" "+berhasil+mySaldo);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public void setmList(List<TransaksiModel> mList) {
        this.mList = mList;
    }
}
