package com.example.trashure.Feature.Scan;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trashure.MainActivity;
import com.example.trashure.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class ScanFragment extends Fragment{

    private ScanFragment scanFragment;
    private BerhasilScanFragment berhasilScanFragment;
    private SurfaceView sv_scanner;
    private CameraSource cameraSource;
    private BarcodeDetector barcodeDetector;
    private String scanResult="",tBagConnect="";
    private DatabaseReference databaseReference,setoranRefs,trashbagRefs;
    private String[] id_trashbag;
    private BottomNavigationView bottomNavigationView;
    private EditText et_id;
    private Button btn_scan;
    private DatabaseReference userRefs;
    private boolean cek;
    private TextView setoranCount,isEmpty;
    private AlertDialog.Builder alertDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scan, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        setStatusBar();
        fetching();
        initialize();
    }

    private void setStatusBar(){
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getActivity().getWindow().clearFlags(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getActivity().getWindow().setStatusBarColor(getActivity().getResources().getColor(R.color.white));
    }

    private void fetching(){
        //Fetching Trashbag ID From Database
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Trashbag");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> trashId = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.getKey();
                    trashId.add(name);
                    id_trashbag = new String[trashId.size()];
                    for (int i = 0; i < trashId.size(); i++) {
                        id_trashbag[i] = trashId.get(i);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Fetching Trashbag whose connected with user
        userRefs = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("trashbag"))
                {
                    settBagConnect(dataSnapshot.child("trashbag").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        setoranCount = (TextView) getActivity().findViewById(R.id.setoranCount);
        setoranRefs = FirebaseDatabase.getInstance().getReference().child("Setoran").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        isEmpty = (TextView) getActivity().findViewById(R.id.isEmpty);
        trashbagRefs = FirebaseDatabase.getInstance().getReference().child("Trashbag");
    }

    private void initialize(){
        alertDialog = new AlertDialog.Builder(getActivity());
        scanFragment = new ScanFragment();
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottomNavBar);
        bottomNavigationView.setVisibility(View.VISIBLE);
        btn_scan = (Button) getActivity().findViewById(R.id.btn_scan);
        et_id = (EditText) getActivity().findViewById(R.id.edit_id) ;
        berhasilScanFragment = new BerhasilScanFragment();
        sv_scanner = (SurfaceView) getActivity().findViewById(R.id.QRScanner);
        barcodeDetector = new BarcodeDetector.Builder(getActivity()).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(getActivity().getApplicationContext(), barcodeDetector).setRequestedPreviewSize(1024,768).setAutoFocusEnabled(true).build();
        sv_scanner.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try{
                    if(ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        cameraSource.start(sv_scanner.getHolder());
                    }else{
                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},1024);
                    }
                }catch (IOException e){
                    Log.e("Camera Start Error-->>",e.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();  //Retrieving QR Code
                if (barcodes.size() > 0) {
                    barcodeDetector.release();  //Releasing barcodeDetector
                    ToneGenerator toneNotification = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);  //Setting beep sound
                    toneNotification.startTone(ToneGenerator.TONE_PROP_BEEP, 150);

                    scanResult = barcodes.valueAt(0).displayValue.toString();  //Retrieving text from QR Code
                    Log.d("SCANRESULT",scanResult);
                    Log.d("ISI ID TRASHBAG",id_trashbag[0]+","+id_trashbag[1]);
                    for (int i = 0; i < id_trashbag.length; i++) {
                        if (!scanResult.equalsIgnoreCase(id_trashbag[i])){
                            cek = false;
                        }else{
                            cek = true;
                            break;
                        }
                    }
                    if (!cek){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                alertDialog.setTitle("Wahh ..");
                                alertDialog.setMessage("QR Code yang kamu scan salah, QR Code-nya ada di trashbag lohh...");
                                alertDialog.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.frameFragment,scanFragment);
                                        fragmentTransaction.commit();
                                    }
                                });
                                alertDialog.show();
                            }
                        });
                    }else{
                        trashbagRefs.child(scanResult).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                isEmpty.setText(dataSnapshot.child("user_id").getValue().toString());
                                if (isEmpty.getText().toString().equalsIgnoreCase("Kosong")){
                                    berhasilScanFragment.setId(scanResult);
                                    FirebaseAuth.getInstance().updateCurrentUser(FirebaseAuth.getInstance().getCurrentUser()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                HashMap userMap = new HashMap();
                                                userMap.put("trashbag",scanResult);
                                                userRefs.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                                                    @Override
                                                    public void onComplete(@NonNull Task task) {
                                                        Log.d("TAG","SCAN SUCCES");
                                                    }
                                                });
                                            }
                                        }
                                    });
                                    if (getActivity()!=null){
                                        scanActivity(scanResult);
                                        sentToSuccess();
                                    }
                                }else{
                                    if (getActivity()!=null){
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                alertDialog.setTitle("Yahh ..");
                                                alertDialog.setMessage("Trashbag ini sudah terhubung dengan user lain. Cari yang lain yuk!!");
                                                alertDialog.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                                        fragmentTransaction.replace(R.id.frameFragment,scanFragment);
                                                        fragmentTransaction.commit();
                                                    }
                                                });
                                                alertDialog.show();
                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }
        });
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker();
            }
        });
    }

    private void checker(){
        if (TextUtils.isEmpty(et_id.getText())){
            Toast.makeText(getActivity(), "ID Trashbag harus diisi!", Toast.LENGTH_SHORT).show();
        }else{
            for (int i = 0; i < id_trashbag.length; i++) {
                if (id_trashbag[i].equalsIgnoreCase(et_id.getText().toString())){
                    cek = true;
                    break;
                }else cek = false;
            }
            if (cek){
                trashbagRefs.child(et_id.getText().toString().toUpperCase()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("user_id").getValue().toString().equalsIgnoreCase("Kosong")){
                            berhasilScanFragment.setId(et_id.getText().toString().toUpperCase());
                            HashMap userMap = new HashMap();
                            userMap.put("trashbag",et_id.getText().toString().toUpperCase());
                            userRefs.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    Log.d("TAG","SCAN SUCCESS");
                                }
                            });
                            if (getActivity()!=null){
                                sentToSuccess();
                                scanActivity(et_id.getText().toString().toUpperCase());
                            }
                        }else{
                            if (getActivity()!= null){
                                Toast.makeText(getActivity(), "Trashbag ini sudah terhubung dengan user lain. Silahkan cari trashbag lain!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }else{
                Toast.makeText(getActivity(), "ID Trashbag Salah!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void scanActivity(final String trash_id){
        //Fetching Trashbag whose connected with user
        setoranRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("COUNTINGG!!",String.valueOf(dataSnapshot.getChildrenCount()));
                if (dataSnapshot.exists()){
                    setoranCount.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                }else{
                    setoranCount.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        setoranRefs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("LOG!!",setoranCount.getText().toString());
                Date now = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
                String date = dateFormat1.format(now);

                HashMap setoranMap = new HashMap();
                setoranMap.put("id",setoranCount.getText().toString());
                setoranMap.put("id_trashbag",trash_id);
                setoranMap.put("id_user", FirebaseAuth.getInstance().getCurrentUser().getUid());
                setoranMap.put("date",date);
                setoranMap.put("status","Proses");
                setoranMap.put("berat",0);
                setoranRefs.child(setoranCount.getText().toString()).updateChildren(setoranMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        trashbagRefs.child(trash_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                trashbagRefs.child(trash_id).child("user_id").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sentToSuccess(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameFragment,berhasilScanFragment);
        fragmentTransaction.commit();
    }
    private void sentToScan(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameFragment,scanFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        initialize();
    }

    public void settBagConnect(String tBagConnect) {
        this.tBagConnect = tBagConnect;
    }
}