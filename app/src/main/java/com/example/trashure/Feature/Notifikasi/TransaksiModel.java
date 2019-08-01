package com.example.trashure.Feature.Notifikasi;

public class TransaksiModel {
    private String id;
    private int image_url;
    private String title;
    private String date;
    private String desc;
    private boolean read;
    private String hour;
    private String provider,noHp,saldo;

    public TransaksiModel(String id, int image_url, String title, String date, String desc, boolean read, String hour, String provider, String noHp, String saldo) {
        this.id = id;
        this.image_url = image_url;
        this.title = title;
        this.date = date;
        this.desc = desc;
        this.read = read;
        this.hour = hour;
        this.provider = provider;
        this.noHp = noHp;
        this.saldo = saldo;
    }

    /*public TransaksiModel(String id, int image_url, String title, String date, String desc, boolean read) {
        this.id = id;
        this.image_url = image_url;
        this.title = title;
        this.date = date;
        this.desc = desc;
        this.read = read;
    }*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImage_url() {
        return image_url;
    }

    public void setImage_url(int image_url) {
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }
}
