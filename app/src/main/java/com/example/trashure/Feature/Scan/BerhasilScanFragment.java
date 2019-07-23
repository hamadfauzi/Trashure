package com.example.trashure.Feature.Scan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.trashure.R;

public class BerhasilScanFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private ScanFragment scanFragment;
    private TextView tv_id;
    private Button btnKembali;
    private String id;

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
        initialize();
    }

    private void initialize(){
        scanFragment = new ScanFragment();
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottomNavBar);
        bottomNavigationView.setVisibility(View.GONE);
        btnKembali = (Button) getActivity().findViewById(R.id.btn_kembali);
        tv_id = (TextView) getActivity().findViewById(R.id.sisa_saldo);
        tv_id.setText("Trashbag ID "+id);
        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentToTukar();
            }
        });
    }

    private void sentToTukar(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameFragment,scanFragment,null);
        fragmentTransaction.commit();
    }

    public void setId(String id) {
        this.id = id;
    }
}