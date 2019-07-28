package com.example.trashure.Feature.Scan;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trashure.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class ScanFragment extends Fragment{

    private ScanFragment scanFragment;
    private BerhasilScanFragment berhasilScanFragment;
    private SurfaceView sv_scanner;
    private CameraSource cameraSource;
    private BarcodeDetector barcodeDetector;
    private String scanResult="";
    private DatabaseReference databaseReference;
    private String[] id_trashbag;
    private BottomNavigationView bottomNavigationView;
    private EditText et_id;
    private Button btn_scan;
    private boolean cek;


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
        initialize();
    }

    private void initialize(){
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
        scanFragment = new ScanFragment();
        btn_scan = (Button) getActivity().findViewById(R.id.btn_scan);
        et_id = (EditText) getActivity().findViewById(R.id.edit_id) ;
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottomNavBar);
        bottomNavigationView.setVisibility(View.VISIBLE);
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
                            Log.d("QRCODE SALAH!","QRCODE TIDAK TERDETEKSI");
                            sentToScan();
                        }else{
                            berhasilScanFragment.setId(scanResult);
                            sentToSuccess();
                            break;
                        }
                    }
                }
            }
        });
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_id.getText())){
                    Toast.makeText(getActivity(), "ID Trashbag harus diisi!", Toast.LENGTH_SHORT).show();
                }else{
                    for (int i = 0; i < id_trashbag.length; i++) {
                        if (id_trashbag[i].equalsIgnoreCase(et_id.getText().toString())){
                            berhasilScanFragment.setId(et_id.getText().toString().toUpperCase());
                            sentToSuccess();
                            break;
                        }else{
                            Toast.makeText(getActivity(), "ID Trashbag salah!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
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
}