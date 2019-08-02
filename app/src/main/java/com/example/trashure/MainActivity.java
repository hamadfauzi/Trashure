package com.example.trashure;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.trashure.Feature.Akun.AkunFragment;
import com.example.trashure.Feature.Beranda.BerandaFragment;
import com.example.trashure.Feature.Harga.HargaFragment;
import com.example.trashure.Feature.Penukaran.PenukaranFragment;
import com.example.trashure.Feature.Scan.ScanFragment;
import com.example.trashure.Feature.Scan.TrashbagTersambungFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    public static Context contextOfApplication;
    private DatabaseReference databaseReference;
    private String tBag="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("trashbag"))
                {
                    tBag = dataSnapshot.child("trashbag").getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        contextOfApplication = getApplicationContext();
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));

        setContentView(R.layout.activity_main);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavBar);
        final AkunFragment akunFragment = new AkunFragment();
        final PenukaranFragment penukaranFragment = new PenukaranFragment();
        final ScanFragment scanFragment = new ScanFragment();
        final BerandaFragment berandaFragment = new BerandaFragment();
        final HargaFragment hargaFragment = new HargaFragment();
        final TrashbagTersambungFragment tersambungFragment = new TrashbagTersambungFragment();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {

                int id = menuItem.getItemId();
                if (id == R.id.menuAkun) {
                    setFragment(akunFragment);
                    return true;
                } else if (id == R.id.menuBeranda) {
                    setFragment(berandaFragment);
                    return true;
                } else if (id == R.id.menuHarga) {
                    setFragment(hargaFragment);
                    return true;
                } else if (id == R.id.menuPenukaran) {
                    setFragment(penukaranFragment);
                    return true;
                } else if (id == R.id.menuScan) {
                    if (tBag.equalsIgnoreCase("kosong") || tBag.isEmpty()){
                        setFragment(scanFragment);
                    }else{
                        setFragment(tersambungFragment);
                    }
                    return true;
                }
                return  false;
            }

        });
        bottomNavigationView.setSelectedItemId(R.id.menuBeranda);

    }
    public void setFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameFragment,fragment);
        fragmentTransaction.commit();
    }

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }
}
