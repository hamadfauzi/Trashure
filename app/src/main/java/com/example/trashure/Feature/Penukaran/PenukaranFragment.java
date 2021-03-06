package com.example.trashure.Feature.Penukaran;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.trashure.R;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;


public class PenukaranFragment extends Fragment {

    private Button btnTukar;
    private EditText et_nominal,et_phoneNumber;
    private KonfirmasiFragment konfirmasiFragment;
    private NominalFragment nominalFragment;
    private BottomNavigationView bottomNavigationView;
    private TextView tv_harga,tv_noHp,tv_alert;
    private String nominal="Rp.5000",harga="Rp.6000",phoneNumber="",jenisLayanan="",provider="",mySaldo="";
    private ExpandableRelativeLayout expandableRelativeLayout;
    private InputFilter filter;
    private ColorFilter colorFilter;
    private boolean approved = true;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_penukaran, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
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

    @Override
    public void onStart() {
        super.onStart();
        eventPenukaran();
        eventFragmentAkun();
    }

    public void eventPenukaran() {
        expandableRelativeLayout = (ExpandableRelativeLayout) getActivity().findViewById(R.id.expandableLayout);
        final RelativeLayout llPulsa = (RelativeLayout) getActivity().findViewById(R.id.ll_pulsa);
        final RelativeLayout l2Dana = (RelativeLayout) getActivity().findViewById(R.id.l2_dana);
        final RelativeLayout l3Link = (RelativeLayout) getActivity().findViewById(R.id.l3_link);
        final ImageView arrow = (ImageView) getActivity().findViewById(R.id.iv_penukaran_pulsa);
        //llPulsa.setBackgroundResource(R.drawable.bg_white);
        llPulsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expandableRelativeLayout.isExpanded())
                {
                    expandableRelativeLayout.toggle();
                    arrow.setImageResource(R.drawable.ic_arrow_down);
                    llPulsa.setBackgroundResource(R.drawable.bg_white);
                }
                else
                {
                    expandableRelativeLayout.toggle();
                    arrow.setImageResource(R.drawable.ic_arrow_up);
                    llPulsa.setBackgroundResource(R.drawable.bg_white_top);
                }
            }
        });

        l2Dana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Comming Soon!", Toast.LENGTH_SHORT).show();
            }
        });

        l3Link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Comming Soon!", Toast.LENGTH_SHORT).show();
            }
        });
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
        colorFilter = new ColorFilter();
        filter= new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; ++i)
                {
                    if (!Pattern.compile("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890]*").matcher(String.valueOf(source.charAt(i))).matches())
                    {
                        return "";
                    }
                }
                return null;
            }
        };
        Log.d("PHONENUMBER___",phoneNumber);
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottomNavBar);
        bottomNavigationView.setVisibility(View.VISIBLE);
        btnTukar = (Button) getActivity().findViewById(R.id.btn_tukar);
        et_phoneNumber = (EditText) getActivity().findViewById(R.id.et_nb);
        Telkomsel(phoneNumber);
        et_nominal = (EditText) getActivity().findViewById(R.id.et_nominal);
        tv_harga = (TextView) getActivity().findViewById(R.id.tv_harga);
        tv_noHp = (TextView) getActivity().findViewById(R.id.nomor_hp);
        tv_alert = (TextView) getActivity().findViewById(R.id.tv_alert);
        nominalFragment = new NominalFragment();
        konfirmasiFragment = new KonfirmasiFragment();
        colorFilter = et_phoneNumber.getBackground().getColorFilter();
        et_nominal.setText(nominal);
        tv_harga.setText(harga);
        et_phoneNumber.setText(phoneNumber);
        et_phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("PHONENUMBER",String.valueOf(s));
                if(s.length()==0){
                    et_phoneNumber.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_IN);
                    tv_noHp.setTextColor(getResources().getColor(R.color.red));
                    et_phoneNumber.setTextColor(getResources().getColor(R.color.red));
                    tv_alert.setVisibility(View.VISIBLE);
                }else{
                    et_phoneNumber.setTextColor(Color.BLACK);
                    et_phoneNumber.getBackground().setColorFilter(colorFilter);
                    tv_noHp.setTextColor(Color.argb(120,0,0,0));
                    tv_alert.setVisibility(View.GONE);
                    Telkomsel(s);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()<10){
                    approved=false;
                }else{
                    phoneNumber = et_phoneNumber.getText().toString();
                }
            }
        });

        //harga = pemisah(harga);

        btnTukar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    /*tukarActivity();
                    toSuccess();*/
                    Log.d("PROVIDERCHECKER",String.valueOf(provider));
                    Log.d("BOOLEANCHECKER",String.valueOf(approved));
                    jenisLayanan = "Pulsa";
                    if(TextUtils.isEmpty(et_phoneNumber.getText().toString()) || approved==false){
                        Toast.makeText(getActivity(), "Data harus diisi", Toast.LENGTH_SHORT).show();
                        et_phoneNumber.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_IN);
                        tv_noHp.setTextColor(getResources().getColor(R.color.red));
                        et_phoneNumber.setTextColor(getResources().getColor(R.color.red));
                        tv_alert.setVisibility(View.VISIBLE);
                    }else{
                        /*if(Integer.valueOf(mySaldo)<Integer.valueOf(harga)){
                            Toast.makeText(getActivity(), "Saldo anda tidak cukup!", Toast.LENGTH_SHORT).show();
                        }else{*/
                            konfTukar();
                    //}
                }
            }
        });
        et_nominal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((et_phoneNumber.getText().toString().isEmpty() || et_phoneNumber.length()<10) || approved==false){
                    et_phoneNumber.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_IN);
                    tv_noHp.setTextColor(getResources().getColor(R.color.red));
                    et_phoneNumber.setTextColor(getResources().getColor(R.color.red));
                    tv_alert.setVisibility(View.VISIBLE);
                }else{
                    phoneNumber = et_phoneNumber.getText().toString();
                    nominalFragment.setPhoneNumber(et_phoneNumber.getText().toString());
                    sentToNominal();
                }
            }
        });
    }

    private void konfTukar(){
        if (jenisLayanan.equalsIgnoreCase("pulsa")){
            konfirmasiFragment.setJenisLayanan(jenisLayanan);
            konfirmasiFragment.setHarga(harga);
            konfirmasiFragment.setNomor(phoneNumber);
            konfirmasiFragment.setProvider(provider);
            konfirmasiFragment.setPulsa(nominal);
            sentToKonfirmasi();
        }
    }

    private void Telkomsel(CharSequence s){
        if(s.length()>=4){
            if(s.charAt(0)=='0' && s.charAt(1)=='8') {
                approved=true;
                provider = "Telkomsel";
                if (s.charAt(2) == '1' && s.charAt(3) == '1') {
                    Log.d("IDENTITAS KARTU", "KARTU HALO 10-11 Digit");
                    et_phoneNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.halo_logo_24x24, 0);
                } else if (s.charAt(2) == '1' && s.charAt(3) == '2') {
                    Log.d("IDENTITAS KARTU", "KARTU SIMPATI 10-11 Digit");
                    et_phoneNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.simpati_logo_24x24, 0);
                } else if (s.charAt(2) == '1' && s.charAt(3) == '3') {
                    Log.d("IDENTITAS KARTU", "KARTU SIMPATI 11-12 Digit");
                    et_phoneNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.simpati_logo_24x24, 0);
                } else if ((s.charAt(2) == '2' && s.charAt(3) == '1') || (s.charAt(2) == '2' && s.charAt(3) == '2')) {
                    Log.d("IDENTITAS KARTU", "KARTU SIMPATI 12 Digit");
                    et_phoneNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.simpati_logo_24x24, 0);
                } else if (s.charAt(2) == '2' && s.charAt(3) == '3') {
                    Log.d("IDENTITAS KARTU", "KARTU AS 12 Digit");
                    et_phoneNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.as_logo_24x24, 0);
                } else if ((s.charAt(2) == '5' && s.charAt(3) == '2') || (s.charAt(2) == '5' && s.charAt(3) == '3')) {
                    Log.d("IDENTITAS KARTU", "KARTU AS 12 Digit");
                    et_phoneNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.as_logo_24x24, 0);
                } else if (s.charAt(2) == '5' && s.charAt(3) == '1') {
                    Log.d("IDENTITAS KARTU", "KARTU AS 10-12 Digit");
                    et_phoneNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.as_logo_24x24, 0);
                } else {
                    Ooredo(s);
                }
            }else{
                approved = false;
                et_phoneNumber.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                et_phoneNumber.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_IN);
                tv_noHp.setTextColor(getResources().getColor(R.color.red));
                et_phoneNumber.setTextColor(getResources().getColor(R.color.red));
                tv_alert.setVisibility(View.VISIBLE);
            }
        }else if(s.length()==0){

        }else{
            et_phoneNumber.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        }
    }

    public void Ooredo(CharSequence s){
        provider = "Indosat Ooredoo";
            if(s.charAt(2) == '5' && s.charAt(3) == '5'){
                Log.d("IDENTITAS KARTU", "KARTU MATRIX 10 Digit");
                et_phoneNumber.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.mentari_logo_24x24,0);
            }else if(s.charAt(2) == '5' && s.charAt(3) == '6') {
                Log.d("IDENTITAS KARTU", "KARTU IM3 10-12 Digit");
                et_phoneNumber.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.im3_logo_24x24,0);
            }else if (s.charAt(2) == '5' && s.charAt(3) == '7'){
                Log.d("IDENTITAS KARTU", "KARTU IM3 12 Digit");
                et_phoneNumber.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.im3_logo_24x24,0);
            }else if((s.charAt(2) == '5' && s.charAt(3) == '8') || (s.charAt(2) == '2' && s.charAt(3) == '2')){
                Log.d("IDENTITAS KARTU", "KARTU MENTARI 12 Digit");
                et_phoneNumber.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.mentari_logo_24x24,0);
            }else if(s.charAt(2) == '1' && s.charAt(3) == '5'){
                Log.d("IDENTITAS KARTU", "KARTU MATRIX 11-12 Digit");
                et_phoneNumber.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.mentari_logo_24x24,0);
            }else if(s.charAt(2) == '1' && s.charAt(3) == '6'){
                Log.d("IDENTITAS KARTU", "KARTU MENTARI 10-12 Digit");
                et_phoneNumber.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.mentari_logo_24x24,0);
            }else{
                XL(s);
            }
    }

    public void XL(CharSequence s){
        provider="XL Axiata";
        if((s.charAt(2) == '1' && s.charAt(3) == '7') || (s.charAt(2) == '1' && s.charAt(3) == '8') || (s.charAt(2) == '1' && s.charAt(3) == '9')){
            Log.d("IDENTITAS KARTU", "KARTU XL 10-12 Digit");
            et_phoneNumber.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.xl_logo_24x24,0);
        }else if((s.charAt(2) == '5' && s.charAt(3) == '9') || (s.charAt(2) == '7' && s.charAt(3) == '7') || (s.charAt(2) == '7' && s.charAt(3) == '8')) {
            Log.d("IDENTITAS KARTU", "KARTU XL 12 Digit");
            et_phoneNumber.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.xl_logo_24x24,0);
        }else{
            Tri(s);
        }
    }

    public void Tri(CharSequence s){
        provider = "Tri Indonesia";
        if((s.charAt(2) == '9' && s.charAt(3) == '5') || (s.charAt(2) == '9' && s.charAt(3) == '6') || (s.charAt(2) == '9' && s.charAt(3) == '7') || (s.charAt(2) == '9' && s.charAt(3) == '8') || (s.charAt(2) == '9' && s.charAt(3) == '9')){
            Log.d("IDENTITAS KARTU", "KARTU Tri 11-12 Digit");
            et_phoneNumber.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.tri_logo_24x24,0);
        }else{
            Axis(s);
        }
    }

    public void Axis(CharSequence s){
        provider = "AXIS";
        if((s.charAt(2) == '3' && s.charAt(3) == '1') || (s.charAt(2) == '3' && s.charAt(3) == '2') || (s.charAt(2) == '3' && s.charAt(3) == '3') || (s.charAt(2) == '3' && s.charAt(3) == '8')){
            Log.d("IDENTITAS KARTU", "KARTU Axis 11-12 Digit");
            et_phoneNumber.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.axis_logo_24x24,0);
        }else{
            Smartfren(s);
        }
    }

    public void Smartfren(CharSequence s){
        provider = "Smartfren";
        if((s.charAt(2) == '8' && s.charAt(3) == '1') || (s.charAt(2) == '8' && s.charAt(3) == '2') || (s.charAt(2) == '8' && s.charAt(3) == '3') || (s.charAt(2) == '8' && s.charAt(3) == '4') || (s.charAt(2) == '8' && s.charAt(3) == '5')
                || (s.charAt(2) == '8' && s.charAt(3) == '6') || (s.charAt(2) == '8' && s.charAt(3) == '7') || (s.charAt(2) == '8' && s.charAt(3) == '8') || (s.charAt(2) == '8' && s.charAt(3) == '9')){
            Log.d("IDENTITAS KARTU", "KARTU Axis 11-12 Digit");
            et_phoneNumber.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.smartfren_logo_24x24,0);
        }else{
            approved = false;
            et_phoneNumber.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            et_phoneNumber.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_IN);
            tv_noHp.setTextColor(getResources().getColor(R.color.red));
            et_phoneNumber.setTextColor(getResources().getColor(R.color.red));
            tv_alert.setVisibility(View.VISIBLE);
        }
    }

    private void sentToNominal(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameFragment,nominalFragment,null).addToBackStack(null);
        fragmentTransaction.commit();

    }

    private void sentToKonfirmasi(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameFragment,konfirmasiFragment,null).addToBackStack(null);
        fragmentTransaction.commit();
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

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setHarga(String tv_harga) {
        this.harga = tv_harga;
    }

    public void setNominal(String et_nominal) {
        this.nominal = et_nominal;
    }

    public void setMySaldo(String mySaldo) {
        this.mySaldo = mySaldo;
    }
}