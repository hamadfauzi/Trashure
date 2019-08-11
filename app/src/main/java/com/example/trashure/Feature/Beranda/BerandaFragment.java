package com.example.trashure.Feature.Beranda;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trashure.Feature.Notifikasi.LainnyaFragment;
import com.example.trashure.Feature.Notifikasi.NotifikasiFragment;
import com.example.trashure.Feature.Notifikasi.TransaksiAdapter;
import com.example.trashure.Feature.Notifikasi.TransaksiFragment;
import com.example.trashure.Feature.Notifikasi.TransaksiModel;
import com.example.trashure.Feature.Setting.SettingFragment;
import com.example.trashure.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;


public class BerandaFragment extends Fragment{

    private List<SetoranModel> mLists = new ArrayList<>();
    private List<TipsModel> tipsList = new ArrayList<>();
    private RecyclerView mRecyclerView,rvTips;
    private RecyclerView.Adapter mAdapter,mTipsAdapter;
    private RecyclerView.LayoutManager mLayoutManager,mLayoutManager2;

    private NotificationBadge mBadge;
    private ImageButton btnNotif, btnSetting;
    private NotifikasiFragment notifikasiFragment;
    private SettingFragment settingFragment;
    private BottomNavigationView bottomNavigationView;
    private TextView tv_status,tv_saldo,tv_level,getStatus,setoranDetail,setoranEmpty;
    private DatabaseReference userRefs,notifRefs,setoranRefs,tipsRefs;
    private FirebaseAuth mAuth;
    private SetoranDetail FragmentSetoranDetail;

    private void setupViewPager(ViewPager viewPager) {
        BerandaFragment.tabAdapter adapter = new BerandaFragment.tabAdapter(getChildFragmentManager());
        adapter.addFragment(new GraphFragment(), "   Minggu   ");
        adapter.addFragment(new GraphFragment(), "   Bulan   ");
        adapter.addFragment(new GraphFragment(), "   Tahun   ");
        viewPager.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_beranda, container, false);
        View view = inflater.inflate(R.layout.fragment_beranda,container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpagerGraph);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        final TabLayout tabs = (TabLayout) view.findViewById(R.id.tabs_graph);
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabs.setupWithViewPager(viewPager);
        return view;
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

    private void setStatusBar(){
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getActivity().getWindow().clearFlags(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getActivity().getWindow().setStatusBarColor(getActivity().getResources().getColor(R.color.colorBackground));
    }


    private void eventFragmentAkun() {
        setStatusBar();
        initRecyclerView();
        initialize();

    }

    public void initialize(){
        FragmentSetoranDetail = new SetoranDetail();
        setoranEmpty = (TextView) getActivity().findViewById(R.id.setoran_kosong);
        setoranDetail = (TextView) getActivity().findViewById(R.id.expand);
        getStatus = (TextView) getActivity().findViewById(R.id.getStatus);
        setoranRefs = FirebaseDatabase.getInstance().getReference().child("Setoran").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mAuth = FirebaseAuth.getInstance();
        userRefs = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getCurrentUser().getUid());
        tipsRefs = FirebaseDatabase.getInstance().getReference().child("Tips");
        notifRefs = FirebaseDatabase.getInstance().getReference().child("Transaksi").child(mAuth.getCurrentUser().getUid());
        mBadge = (NotificationBadge) getActivity().findViewById(R.id.notif_badge);
        btnSetting = (ImageButton) getActivity().findViewById(R.id.setting);
        btnNotif = (ImageButton) getActivity().findViewById(R.id.notifikasi);
        tv_saldo = (TextView) getActivity().findViewById(R.id.tv_saldo_beranda);
        tv_level = (TextView) getActivity().findViewById(R.id.tv_level_beranda);
        tv_status = (TextView) getActivity().findViewById(R.id.tv_status_trashbag);
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
                    tv_level.setText(" "+level);
                    tv_saldo.setText("Rp."+mySaldo);
                    if(dataSnapshot.hasChild("trashbag"))
                    {
                        getStatus.setText(dataSnapshot.child("trashbag").getValue().toString());
                    }

                    if (getStatus.getText().toString().equalsIgnoreCase("Kosong")){
                        tv_status.setText(" Tidak Ada");
                    }else{
                        tv_status.setText(" "+getStatus.getText().toString());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        notifRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int counter=0;
                for (DataSnapshot dsp : dataSnapshot.getChildren()){
                    if (dsp.child("read").getValue().toString().equalsIgnoreCase("false")){
                        counter++;
                    }
                }
                mBadge.setNumber(counter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        setoranRefs.limitToLast(4).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()!=0){
                    mRecyclerView.setVisibility(View.VISIBLE);
                    setoranEmpty.setVisibility(View.GONE);
                    mLists.clear();
                    for (DataSnapshot dsp:dataSnapshot.getChildren()){
                        mLists.add(new SetoranModel(dsp.child("id_trashbag").getValue().toString(),dsp.child("date").getValue().toString(),dsp.child("status").getValue().toString()));
                        mAdapter.notifyDataSetChanged();
                    }
                }else{
                    mRecyclerView.setVisibility(View.GONE);
                    setoranEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        tipsRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tipsList.clear();
                for (DataSnapshot dsp:dataSnapshot.getChildren()){
                    tipsList.add(new TipsModel(dsp.child("judul").getValue().toString(),dsp.child("deskripsi").getValue().toString(),dsp.child("gambar").getValue().toString(),dsp.child("tgl").getValue().toString()));
                    mTipsAdapter.notifyDataSetChanged();
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

        setoranDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentToSetoran();
            }
        });

    }

    public void initRecyclerView() {
        mRecyclerView = getActivity().findViewById(R.id.rvListSetoran);
        mAdapter = new SetoranAdapter(mLists,getActivity().getApplicationContext(),getActivity());
        mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,true);
        ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        rvTips = getActivity().findViewById(R.id.rv_tips);
        mTipsAdapter = new TipsAdapter(tipsList,getActivity().getApplicationContext(),getActivity());
        mLayoutManager2 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,true);
        ((LinearLayoutManager) mLayoutManager2).setStackFromEnd(true);
        rvTips.setLayoutManager(mLayoutManager2);
        rvTips.setAdapter(mTipsAdapter);

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

    private void sentToSetoran(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameFragment,FragmentSetoranDetail,null).addToBackStack(null);
        fragmentTransaction.commit();
    }

    static class tabAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public tabAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
