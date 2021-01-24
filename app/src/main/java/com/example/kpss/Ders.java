package com.example.kpss;

public class Ders {

    private String dersID;
    private String dersName;
    private int soruSayisi;

    public Ders(String dersID, String dersName, int soruSayisi) {
        this.dersID = dersID;
        this.dersName = dersName;
        this.soruSayisi = soruSayisi;
    }

    public String getDersID() {
        return dersID;
    }

    public void setDersID(String dersID) {
        this.dersID = dersID;
    }

    public String getDersName() {
        return dersName;
    }

    public void setDersName(String dersName) {
        this.dersName = dersName;
    }

    public int getSoruSayisi() {
        return soruSayisi;
    }

    public void setSoruSayisi(int soruSayisi) {
        this.soruSayisi = soruSayisi;
    }
}
