package com.example.kpss;

public class ListViewMenu {
    private String menuID;
    private String menuName;
    private int menuSoruSayisi;
    private int uyeninDogruCevapSayisi;
    private int uyeninYanlisCevapSayisi;

    public ListViewMenu(String menuID, String menuName, int menuSoruSayisi, int uyeninDogruCevapSayisi, int uyeninYanlisCevapSayisi){
        this.menuID = menuID;
        this.menuName = menuName;
        this.menuSoruSayisi = menuSoruSayisi;
        this.uyeninDogruCevapSayisi = uyeninDogruCevapSayisi;
        this.uyeninYanlisCevapSayisi = uyeninYanlisCevapSayisi;
    }

    public String getMenuID() {
        return menuID;
    }

    public void setMenuID(String menuID) {
        this.menuID = menuID;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getMenuSoruSayisi() {
        return menuSoruSayisi;
    }

    public void setMenuSoruSayisi(int menuSoruSayisi) {
        this.menuSoruSayisi = menuSoruSayisi;
    }

    public int getUyeninDogruCevapSayisi() {
        return uyeninDogruCevapSayisi;
    }

    public void setUyeninDogruCevapSayisi(int uyeninDogruCevapSayisi) {
        this.uyeninDogruCevapSayisi = uyeninDogruCevapSayisi;
    }

    public int getUyeninYanlisCevapSayisi() {
        return uyeninYanlisCevapSayisi;
    }

    public void setUyeninYanlisCevapSayisi(int uyeninYanlisCevapSayisi) {
        this.uyeninYanlisCevapSayisi = uyeninYanlisCevapSayisi;
    }
}
