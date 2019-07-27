package com.example.trashure.Feature.Beranda;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.trashure.Feature.Notifikasi.NotifikasiFragment;
import com.example.trashure.Feature.Setting.SettingFragment;
import com.example.trashure.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class BerandaFragment extends Fragment{

    private ImageButton btnNotif, btnSetting;
    private NotifikasiFragment notifikasiFragment;
    private SettingFragment settingFragment;
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;
    private String currentUserID;
    private DatabaseReference userRefs;
    private TextView tvSaldo,tvLevel,tvStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_beranda, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        eventFragmentAkun();
    }

    private void eventFragmentAkun() {
        initialize();

        userRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String saldo = dataSnapshot.child("saldo").getValue().toString();
                    String level = dataSnapshot.child("level").getValue().toString();

                    tvSaldo.setText("Rp. " + saldo);
                    tvLevel.setText(" " + level);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void initialize(){
        btnSetting = (ImageButton) getActivity().findViewById(R.id.setting);
        btnNotif = (ImageButton) getActivity().findViewById(R.id.notifikasi);
        settingFragment = new SettingFragment();
        notifikasiFragment = new NotifikasiFragment();
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottomNavBar);
        bottomNavigationView.setVisibility(View.VISIBLE);
        tvSaldo = (TextView) getActivity().findViewById(R.id.tv_saldo_beranda);
        tvLevel = (TextView) getActivity().findViewById(R.id.tv_level_beranda);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentToSetting();
            }
        });

        btnNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentToNotif();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userRefs = FirebaseDatabase.getInstance().getReference().child("User").child(currentUserID);

    }

    private void sentToSetting(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameFragment,settingFragment,null).addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void sentToNotif(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameFragment,notifikasiFragment,null).addToBackStack(null);
        fragmentTransaction.commit();
    }

}
