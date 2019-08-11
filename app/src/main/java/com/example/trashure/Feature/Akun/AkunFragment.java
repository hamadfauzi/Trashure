package com.example.trashure.Feature.Akun;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.trashure.Feature.Login.LoginActivity;
import com.example.trashure.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AkunFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    //private EditAkunFragment editAkunFragment;
    private ImageButton btnEdit;
    private FirebaseAuth mAuth;
    private DatabaseReference userRefs;
    private String currentUserId;
    private CircleImageView civProfileImage;
    private Button btnKeluar;
    private TextView tvLevel,tvSaldo,tvTotalSampah,tvNomorHp,tvEmail,tvTglLahir,tvNama;
    private static final String TAG = "Upload";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_akun, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        setStatusBar();
        eventFragmentAkun();
    }

    private void setStatusBar(){
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getActivity().getWindow().clearFlags(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getActivity().getWindow().setStatusBarColor(getActivity().getResources().getColor(R.color.white));
    }

    private void eventFragmentAkun() {
        initialize();
    }

    private void initialize() {
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottomNavBar);
        bottomNavigationView.setVisibility(View.VISIBLE);
        //editAkunFragment = new EditAkunFragment();
        btnEdit = (ImageButton) getActivity().findViewById(R.id.edit_button);
        btnKeluar = (Button) getActivity().findViewById(R.id.btn_keluar);
        tvLevel = (TextView) getActivity().findViewById(R.id.tv_level_profile);
        tvSaldo = (TextView) getActivity().findViewById(R.id.tv_saldo_profile);
        tvTotalSampah = (TextView) getActivity().findViewById(R.id.tv_totalsampah_profile);
        tvTglLahir = (TextView) getActivity().findViewById(R.id.tv_tgl_lahir_profile);
        tvNama = (TextView) getActivity().findViewById(R.id.tv_profile_name);
        tvEmail = (TextView) getActivity().findViewById(R.id.tv_email_profile);
        tvNomorHp = (TextView) getActivity().findViewById(R.id.tv_nohandphone_profile);
        civProfileImage = (CircleImageView) getActivity().findViewById(R.id.civ_profile_image);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        userRefs = FirebaseDatabase.getInstance().getReference().child("User").child(currentUserId);

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

                    if (dataSnapshot.hasChild("displaypicture"))
                    {
                        Picasso.with(getApplicationContext()).load(dataSnapshot.child("displaypicture").getValue().toString()).into(civProfileImage);
                    } else {
                        Picasso.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/trashure-71595.appspot.com/o/DisplayPictures%2Fdummy%2FUserLogo.png?alt=media&token=0ca8fe79-4dac-46c7-8356-0df6ea65464b").into(civProfileImage);
                    }

                    String mySaldo = NumberFormat.getInstance(Locale.ITALIAN).format(Integer.valueOf(saldo));
                    tvNama.setText(nama);
                    tvLevel.setText(level);
                    tvSaldo.setText("Rp."+mySaldo);
                    tvTglLahir.setText(tgllahir);
                    tvEmail.setText(email);
                    tvNomorHp.setText(phonenumber);
                    tvTotalSampah.setText(totalsampah + "Kg");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        btnKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertsignout();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameFragment,editAkunFragment).addToBackStack(null);
                fragmentTransaction.commit();*/
                Intent mainIntent = new Intent(getActivity(), EditAkunActivity.class);
                startActivity(mainIntent);
                getActivity().finish();
            }
        });
    }

    public void alertsignout()
    {
        AlertDialog.Builder alertDialog2 = new
                AlertDialog.Builder(
                getActivity());

        // Setting Dialog Title
        alertDialog2.setTitle("Konfirmasi Keluar");

        // Setting Dialog Message
        alertDialog2.setMessage("Apakah anda yakin ingin keluar?");

        // Setting Positive "Yes" Btn
        alertDialog2.setPositiveButton("Iya",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        FirebaseAuth.getInstance().signOut();
                        Intent i = new Intent(getActivity(),
                                LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                });

        // Setting Negative "NO" Btn
        alertDialog2.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        /*Toast.makeText(getContext(),
                                "You clicked on NO", Toast.LENGTH_SHORT)
                                .show();*/
                        dialog.cancel();
                    }
                });

        // Showing Alert Dialog
        alertDialog2.show();

    }

}
