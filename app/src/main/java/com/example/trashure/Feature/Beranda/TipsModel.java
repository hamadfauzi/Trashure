package com.example.trashure.Feature.Beranda;

public class TipsModel {

    String judul,deskripsi,gambar,tgl;

    public TipsModel() {

    }

    public TipsModel(String judul,String deskripsi,String gambar,String tgl)
    {
        this.deskripsi = deskripsi;
        this.gambar = gambar;
        this.judul = judul;
        this.tgl = tgl;
    }

    public String getJudul() {
        return judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getGambar() {
        return gambar;
    }

    public String getTgl() {
        return tgl;
    }
}

