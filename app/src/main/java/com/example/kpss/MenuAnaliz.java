package com.example.kpss;

public class MenuAnaliz extends Ders {

    private String userID;
    private String dogru_sayisi;
    private String yanlis_sayisi;
    private String time;

    public MenuAnaliz(String dersID, String dersName, int soruSayisi) {
        super(dersID, dersName, soruSayisi);
    }

    public MenuAnaliz(String dersID, String dersName, int soruSayisi, String userID, String dogru_sayisi, String yanlis_sayisi, String time) {
        super(dersID, dersName, soruSayisi);
        this.userID = userID;
        this.dogru_sayisi = dogru_sayisi;
        this.yanlis_sayisi = yanlis_sayisi;
        this.time = time;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDogru_sayisi() {
        return dogru_sayisi;
    }

    public void setDogru_sayisi(String dogru_sayisi) {
        this.dogru_sayisi = dogru_sayisi;
    }

    public String getYanlis_sayisi() {
        return yanlis_sayisi;
    }

    public void setYanlis_sayisi(String yanlis_sayisi) {
        this.yanlis_sayisi = yanlis_sayisi;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
