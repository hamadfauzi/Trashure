package com.example.trashure.Feature.Penukaran;

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

import com.example.trashure.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class BerhasilPenukaranFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private PenukaranFragment penukaranFragment;
    private TextView tv_sisa_saldo;
    private Button btnKembali;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_berhasil_tukar,container,false);
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
        penukaranFragment = new PenukaranFragment();
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottomNavBar);
        bottomNavigationView.setVisibility(View.GONE);
        btnKembali = (Button) getActivity().findViewById(R.id.btn_kembali);
        tv_sisa_saldo = (TextView) getActivity().findViewById(R.id.sisa_saldo);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String mySaldo = dataSnapshot.child("saldo").getValue().toString();
                    //DecimalFormat decimalFormat = new DecimalFormat("#.###.###");
                    //String saldo = decimalFormat.format(Integer.valueOf(mySaldo));
                    String saldo = NumberFormat.getInstance(Locale.ITALIAN).format(Integer.valueOf(mySaldo));
                    tv_sisa_saldo.setText("Sisa saldo anda Rp."+saldo);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentToTukar();
            }
        });
    }

    private void sentToTukar(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameFragment,penukaranFragment,null);
        fragmentTransaction.commit();
    }
}
