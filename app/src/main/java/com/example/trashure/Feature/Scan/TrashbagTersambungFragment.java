package com.example.trashure.Feature.Scan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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


public class TrashbagTersambungFragment extends Fragment {

    private DatabaseReference databaseReference,setoranRefs,trashbagRefs;
    private TextView tv_tbag;
    private Button btn_putus;
    private ScanFragment scanFragment;
    private TextView getID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tersambung_scan,container,false);
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
        getActivity().getWindow().setStatusBarColor(getActivity().getResources().getColor(R.color.white));
    }

    private void initialize(){
        getID = (TextView) getActivity().findViewById(R.id.getID);
        scanFragment = new ScanFragment();
        tv_tbag = (TextView) getActivity().findViewById(R.id.tv_trashbag_id_tersambung);
        btn_putus = (Button) getActivity().findViewById(R.id.btn_putus);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String tBag = "Trashbag dengan ID "+dataSnapshot.child("trashbag").getValue().toString();
                tv_tbag.setText(tBag);
                getID.setText(dataSnapshot.child("trashbag").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btn_putus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().updateCurrentUser(FirebaseAuth.getInstance().getCurrentUser()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        HashMap userMap = new HashMap();
                        userMap.put("trashbag","Kosong");
                        databaseReference.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                Log.d("PUTUS TRASHBAG","PUTUS BERHASIL");
                            }
                        });
                        setoranRefs = FirebaseDatabase.getInstance().getReference().child("Setoran").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        setoranRefs.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                setoranRefs.child(String.valueOf(dataSnapshot.getChildrenCount()-1)).child("status").setValue("Selesai");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        trashbagRefs = FirebaseDatabase.getInstance().getReference().child("Trashbag").child(getID.getText().toString());
                        trashbagRefs.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                trashbagRefs.child("user_id").setValue("Kosong");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
                scanFragment.settBagConnect("Kosong");
                sentToScan();
            }
        });
    }

    private void sentToScan(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameFragment,scanFragment,null);
        fragmentTransaction.commit();
    }
}
