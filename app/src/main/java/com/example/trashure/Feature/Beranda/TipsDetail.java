package com.example.trashure.Feature.Beranda;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trashure.R;
import com.squareup.picasso.Picasso;

public class TipsDetail extends AppCompatActivity {

    ImageView ivGambar;
    TextView tvJudul,tvDeskripsi,tvTanggal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_detail);
        initializr();

        String judul = getIntent().getStringExtra("judul");
        String gambar = getIntent().getStringExtra("gambar");
        String deskripsi = getIntent().getStringExtra("deskripsi");
        String tanggal = getIntent().getStringExtra("tgl");

        Picasso.with(this).load(gambar).into(ivGambar);
        tvDeskripsi.setText(deskripsi);
        tvTanggal.setText(tanggal);
        tvJudul.setText(judul);
    }


    private void initializr() {
        ivGambar = (ImageView) findViewById(R.id.iv_detail_tips);
        tvJudul = (TextView) findViewById(R.id.tv_judul_detail);
        tvTanggal = (TextView) findViewById(R.id.tv_tanggal);
        tvDeskripsi = (TextView) findViewById(R.id.tv_deskripsi);

    }
}
