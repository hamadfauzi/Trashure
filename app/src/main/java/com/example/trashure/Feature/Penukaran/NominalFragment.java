package com.example.trashure.Feature.Penukaran;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.trashure.R;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

public class NominalFragment extends Fragment {

    private String phoneNumber;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private RelativeLayout nom1,nom2,nom3,nom4,nom5;
    private PenukaranFragment penukaranFragment;
    ExpandableRelativeLayout expandableRelativeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nominal, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

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

    public void initialize(){
        penukaranFragment = new PenukaranFragment();
        Log.d("PHONENUMBER---",phoneNumber);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_nominal);
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottomNavBar);
        bottomNavigationView.setVisibility(View.GONE);
        setToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        nom1 = (RelativeLayout) getActivity().findViewById(R.id.layout_nom_1);
        nom2 = (RelativeLayout) getActivity().findViewById(R.id.layout_nom_2);
        nom3 = (RelativeLayout) getActivity().findViewById(R.id.layout_nom_3);
        nom4 = (RelativeLayout) getActivity().findViewById(R.id.layout_nom_4);
        nom5 = (RelativeLayout) getActivity().findViewById(R.id.layout_nom_5);
        isClicked();
    }

    private void setToolbar(){
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_black_24dp);
    }

    private void isClicked(){
        nom1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                penukaranFragment.setHarga("Rp.1500");
                penukaranFragment.setNominal("Rp.1000");
                penukaranFragment.setPhoneNumber(phoneNumber);
                sentToPenukaran();
            }
        });
        nom2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                penukaranFragment.setHarga("Rp.6000");
                penukaranFragment.setNominal("Rp.5000");
                penukaranFragment.setPhoneNumber(phoneNumber);
                sentToPenukaran();
            }
        });
        nom3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                penukaranFragment.setHarga("Rp.11.000");
                penukaranFragment.setNominal("Rp.10.000");
                penukaranFragment.setPhoneNumber(phoneNumber);
                sentToPenukaran();
            }
        });
        nom4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                penukaranFragment.setHarga("Rp.21.000");
                penukaranFragment.setNominal("Rp.20.000");
                penukaranFragment.setPhoneNumber(phoneNumber);
                sentToPenukaran();
            }
        });
        nom5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                penukaranFragment.setHarga("Rp.51.000");
                penukaranFragment.setNominal("Rp.50.000");
                penukaranFragment.setPhoneNumber(phoneNumber);
                sentToPenukaran();
            }
        });
    }

    private void sentToPenukaran(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameFragment,penukaranFragment,null);
        fragmentTransaction.commit();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
