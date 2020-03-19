package com.example.trashure.Feature.Penukaran;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.trashure.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

public class KonfirmasiFragment extends Fragment {

    private String jenisLayanan,pulsa,harga,nomor,provider,mySaldo="",itemCount;
    private TextView tv_jelay,tv_nomor,tv_pulsa,tv_harga,tv_provider;
    private Toolbar toolbar;
    private DatabaseReference databaseReference,transaksiReference;
    private FirebaseAuth mAuth;
    private Button btnTukar;
    private int counter,notif;
    private BottomNavigationView bottomNavigationView;
    private BerhasilPenukaranFragment berhasilPenukaranFragment;
    private TextView getChildren;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String saldo = dataSnapshot.child("saldo").getValue().toString();
                    setMySaldo(saldo);
                    Log.d("CHECKSALDO",saldo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_konfirmasi_penukaran,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        setStatusBar();
        initialize();
    }

    public void setToolbar(){
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setStatusBar(){
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getActivity().getWindow().clearFlags(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getActivity().getWindow().setStatusBarColor(getActivity().getResources().getColor(R.color.white));
    }

    private void initialize(){
        berhasilPenukaranFragment = new BerhasilPenukaranFragment();
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottomNavBar);
        bottomNavigationView.setVisibility(View.GONE);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getCurrentUser().getUid());
        transaksiReference = FirebaseDatabase.getInstance().getReference().child("Transaksi").child(mAuth.getCurrentUser().getUid());
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_konfirmasi);
        setToolbar();
        getChildren = (TextView) getActivity().findViewById(R.id.getChildren);
        btnTukar = (Button) getActivity().findViewById(R.id.btn_tukar_konfirmasi);
        tv_harga = (TextView) getActivity().findViewById(R.id.tv_get_harga);
        tv_jelay = (TextView) getActivity().findViewById(R.id.tv_get_jelay);
        tv_nomor = (TextView) getActivity().findViewById(R.id.tv_get_nomor);
        tv_provider = (TextView) getActivity().findViewById(R.id.tv_get_provider);
        tv_pulsa = (TextView) getActivity().findViewById(R.id.tv_get_pulsa);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        tv_pulsa.setText(pulsa);
        tv_provider.setText(provider);
        tv_nomor.setText(nomor);
        tv_jelay.setText(jenisLayanan);
        tv_harga.setText(harga);

        pulsa = pemisah(pulsa);
        harga = pemisah(harga);

        Log.d("HARGACHECKER",harga);
        btnTukar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.valueOf(mySaldo)<Integer.valueOf(harga)){
                    Toast.makeText(getActivity(), "Your Balance Not Enough!", Toast.LENGTH_SHORT).show();
                }else{
                    tukarActivity();
                    toSuccess();
                }
            }
        });

    }

    private String pemisah(String s){
        String[] separated = s.split("\\.");
        String nilai;
        if(separated.length>2){
            nilai = separated[1]+separated[2];
        }else{
            nilai = separated[1];
        }
        return nilai;
    }

    private void tukarActivity(){
        transaksiReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    getChildren.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                }else{
                    getChildren.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Log.d("LOGGG!!!",getChildren.getText().toString());
        mAuth.updateCurrentUser(mAuth.getCurrentUser()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    HashMap userHash = new HashMap();

                    userHash.put("saldo",(Integer.valueOf(mySaldo)-Integer.valueOf(harga)));
                    databaseReference.updateChildren(userHash).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            Log.d("PEMBAYARAN","SUCCESS");
                        }
                    });

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            HashMap transaksiHash = new HashMap();
                            transaksiHash.put("id",getChildren.getText().toString());
                            transaksiHash.put("id_user",mAuth.getCurrentUser().getUid());
                            transaksiHash.put("jenisPenukaran",jenisLayanan);
                            transaksiHash.put("provider",provider);
                            transaksiHash.put("phoneNumber",nomor);
                            transaksiHash.put("nominal",Integer.valueOf(pulsa));
                            transaksiHash.put("biaya",Integer.valueOf(harga));
                            Date tgl = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat tf = new SimpleDateFormat("HH.mm");
                            String date = df.format(tgl);
                            String time = tf.format(tgl);
                            transaksiHash.put("tanggal_penukaran",date);
                            transaksiHash.put("jam_penukaran",time);
                            transaksiHash.put("read",false);
                            transaksiHash.put("sisa_saldo",dataSnapshot.child("saldo").getValue());
                            transaksiReference.child(getChildren.getText().toString()).updateChildren(transaksiHash).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    Toast.makeText(getActivity(), "Penukaran Berhasil!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    private void toSuccess(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameFragment,berhasilPenukaranFragment,null);
        fragmentTransaction.commit();
    }

    public void setJenisLayanan(String jenisLayanan) {
        this.jenisLayanan = jenisLayanan;
    }

    public void setPulsa(String pulsa) {
        this.pulsa = pulsa;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setMySaldo(String mySaldo) {
        this.mySaldo = mySaldo;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
