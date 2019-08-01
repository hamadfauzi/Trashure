package com.example.trashure.Feature.Notifikasi;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trashure.Feature.Beranda.BerandaFragment;
import com.example.trashure.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;


public class TransaksiFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<TransaksiModel> mLists = new ArrayList<>();
    private static String TIMESTAMP_PATTERN1 = "dd/MM/yyyy";
    private DatabaseReference transaksiRefs;
    private int notif,counter,image_id;
    private int iv_pulsa;
    private String title_pulsa;
    private String[] desc_pulsa;
    private String title,desc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transaksi_notif, container, false);
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
        initRecyclerView();
        initialize();
    }

    public void initialize(){

        iv_pulsa = getActivity().getResources().getIdentifier("iv_pulsa","drawable",getActivity().getPackageName());

        title_pulsa = getActivity().getResources().getString(R.string.title_transaksi);

        desc_pulsa = getActivity().getResources().getStringArray(R.array.desc);

        transaksiRefs = FirebaseDatabase.getInstance().getReference().child("Transaksi").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        transaksiRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mLists.clear();
                for (DataSnapshot dsp : dataSnapshot.getChildren()){

                    if (dsp.child("jenisPenukaran").getValue().toString().equalsIgnoreCase("pulsa")){
                        image_id = iv_pulsa;
                        title = title_pulsa;
                    }

                    switch(dsp.child("nominal").getValue().toString()) {
                        case "1000":
                            desc = desc_pulsa[0];
                            Log.d("DESCRIPTION",desc);
                            break;
                        case "5000":
                            desc = desc_pulsa[1];
                            Log.d("DESCRIPTION",desc);
                            break;
                        case "10000":
                            desc = desc_pulsa[2];
                            Log.d("DESCRIPTION",desc);
                            break;
                        case "20000":
                            desc = desc_pulsa[3];
                            Log.d("DESCRIPTION",desc);
                            break;
                        case "50000":
                            desc = desc_pulsa[4];
                            Log.d("DESCRIPTION",desc);
                            break;
                        default:
                            desc = desc_pulsa[5];
                            Log.d("DESCRIPTION",desc);
                            break;
                    }

                    mLists.add(new TransaksiModel(dsp.getKey(),image_id,title,dsp.child("tanggal_penukaran").getValue().toString(),desc,
                            Boolean.parseBoolean(dsp.child("read").getValue().toString()),dsp.child("jam_penukaran").getValue().toString(),dsp.child("provider").getValue().toString(),
                            dsp.child("phoneNumber").getValue().toString(),dsp.child("sisa_saldo").getValue().toString()));
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void initRecyclerView() {
        mRecyclerView = getActivity().findViewById(R.id.rvListTransaksi);
        mAdapter = new TransaksiAdapter(mLists,getActivity().getApplicationContext(),getActivity());
        mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,true);
        ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}