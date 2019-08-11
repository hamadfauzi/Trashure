package com.example.trashure.Feature.Setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.trashure.Feature.Beranda.BerandaFragment;
import com.example.trashure.Feature.Notifikasi.NotifikasiFragment;
import com.example.trashure.R;

public class SettingFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private NotifikasiFragment notifikasiFragment;
    private RelativeLayout rlNotifikasi;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        eventFragmentAkun();
    }

    private void setStatusBar(){
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getActivity().getWindow().clearFlags(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getActivity().getWindow().setStatusBarColor(getActivity().getResources().getColor(R.color.white));
    }

    private void eventFragmentAkun() {
        setStatusBar();
        initialize();
    }

    private void setToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void initialize(){
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_setting);
        rlNotifikasi  = (RelativeLayout) getActivity().findViewById(R.id.layout_notif);
        setToolbar();
        notifikasiFragment = new NotifikasiFragment();
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottomNavBar);
        bottomNavigationView.setVisibility(View.GONE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        rlNotifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentToNotif();
            }
        });

    }

    private void sentToNotif(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameFragment,notifikasiFragment,null).addToBackStack(null);
        fragmentTransaction.commit();
    }

}