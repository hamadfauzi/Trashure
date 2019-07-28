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
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;


public class BerandaFragment extends Fragment{

    private ImageButton btnNotif, btnSetting;
    private NotifikasiFragment notifikasiFragment;
    private SettingFragment settingFragment;
    private BottomNavigationView bottomNavigationView;
    private TextView tv_saldo,tv_level;
    private DatabaseReference userRefs;
    private FirebaseAuth mAuth;

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
    }

    public void initialize(){
        mAuth = FirebaseAuth.getInstance();
        userRefs = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getCurrentUser().getUid());
        btnSetting = (ImageButton) getActivity().findViewById(R.id.setting);
        btnNotif = (ImageButton) getActivity().findViewById(R.id.notifikasi);
        tv_saldo = (TextView) getActivity().findViewById(R.id.uang);
        tv_level = (TextView) getActivity().findViewById(R.id.level);
        settingFragment = new SettingFragment();
        notifikasiFragment = new NotifikasiFragment();
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottomNavBar);
        bottomNavigationView.setVisibility(View.VISIBLE);
        userRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String nama = dataSnapshot.child("nama").getValue().toString();
                    String level = dataSnapshot.child("level").getValue().toString();
                    String saldo = dataSnapshot.child("saldo").getValue().toString();
                    String totalsampah = dataSnapshot.child("jumlahsampah").getValue().toString();
                    String phonenumber = dataSnapshot.child("phonenumber").getValue().toString();
                    String tgllahir = dataSnapshot.child("bod").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();

                    String mySaldo = NumberFormat.getInstance(Locale.ITALIAN).format(Integer.valueOf(saldo));
                    tv_level.setText(level);
                    tv_saldo.setText("Rp."+mySaldo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
