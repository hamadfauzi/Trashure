package com.example.trashure.Feature.Penukaran;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trashure.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class KonfirmasiFragment extends Fragment {

    private String jenisLayanan,pulsa,harga,nomor,provider,mySaldo="";
    private TextView tv_jelay,tv_nomor,tv_pulsa,tv_harga,tv_provider;
    private Toolbar toolbar;
    private DatabaseReference databaseReference,transaksiReference,counterReference;
    private FirebaseAuth mAuth;
    private Button btnTukar;
    private int counter;
    private BottomNavigationView bottomNavigationView;
    private BerhasilPenukaranFragment berhasilPenukaranFragment;

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
        initialize();
    }

    public void setToolbar(){
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initialize(){
        berhasilPenukaranFragment = new BerhasilPenukaranFragment();
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottomNavBar);
        bottomNavigationView.setVisibility(View.GONE);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getCurrentUser().getUid());
        transaksiReference = FirebaseDatabase.getInstance().getReference().child("Transaksi").child(mAuth.getCurrentUser().getUid());
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_konfirmasi);
        setToolbar();
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

        transaksiReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if(!dataSnapshot.child("counter").getValue().toString().isEmpty()){
                        setCounter(Integer.valueOf(dataSnapshot.child("counter").getValue().toString()));
                    }else{
                        setCounter(1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
        mAuth.updateCurrentUser(mAuth.getCurrentUser()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    HashMap userHash = new HashMap();
                    HashMap transaksiHash = new HashMap();
                    HashMap counterSS = new HashMap();

                    if(counter==0){
                        counterSS.put("counter",1);
                        transaksiReference.updateChildren(counterSS).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                Toast.makeText(getActivity(), "Penukaran Berhasil!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        counterSS.put("counter",counter+1);
                        transaksiReference.updateChildren(counterSS).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                Toast.makeText(getActivity(), "Penukaran Berhasil!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    userHash.put("saldo",(Integer.valueOf(mySaldo)-Integer.valueOf(harga)));
                    databaseReference.updateChildren(userHash).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            Log.d("PEMBAYARAN","SUCCESS");
                        }
                    });
                    transaksiHash.put("id_user",mAuth.getCurrentUser().getUid());
                    transaksiHash.put("jenisPenukaran",jenisLayanan);
                    transaksiHash.put("provider",provider);
                    transaksiHash.put("phoneNumber",nomor);
                    transaksiHash.put("nominal",pulsa);
                    transaksiHash.put("biaya",harga);
                    transaksiReference.child(String.valueOf(counter)).updateChildren(transaksiHash).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            Toast.makeText(getActivity(), "Penukaran Berhasil!", Toast.LENGTH_SHORT).show();
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
