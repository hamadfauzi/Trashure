package com.example.trashure.Feature.Beranda;

public class SetoranModel {

    private String trashbagId,date,desc;

    public SetoranModel(String trashbagId, String date, String desc) {
        this.trashbagId = trashbagId;
        this.date = date;
        this.desc = desc;
    }

    public String getTrashbagId() {
        return trashbagId;
    }

    public void setTrashbagId(String trashbagId) {
        this.trashbagId = trashbagId;
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
}
