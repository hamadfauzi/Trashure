package com.example.trashure.Feature.Scan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.trashure.Feature.Beranda.BerandaFragment;
import com.example.trashure.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class BerhasilScanFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private BerandaFragment berandaFragment;
    private TextView tv_id;
    private Button btnKembali;
    private String id;
    private TextView setoranCount;
    private DatabaseReference setoranRefs;
    private String tBagConnect="";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_berhasil_scan,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        setStatusBar();
        initialize();
    }

    private void setStatusBar(){
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getActivity().getWindow().clearFlags(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getActivity().getWindow().setStatusBarColor(getActivity().getResources().getColor(R.color.colorBackground));
    }

    private void initialize(){
        berandaFragment = new BerandaFragment();
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottomNavBar);
        bottomNavigationView.setVisibility(View.GONE);
        btnKembali = (Button) getActivity().findViewById(R.id.btn_kembali);
        tv_id = (TextView) getActivity().findViewById(R.id.sisa_saldo);
        tv_id.setText("Trashbag ID "+id);
        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentToBeranda();
            }
        });
    }

    private void sentToBeranda(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameFragment,berandaFragment,null);
        fragmentTransaction.commit();
        bottomNavigationView.setSelectedItemId(R.id.menuBeranda);
    }

    public void setId(String id) {
        this.id = id;
    }
}