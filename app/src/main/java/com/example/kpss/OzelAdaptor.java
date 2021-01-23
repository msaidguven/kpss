package com.example.kpss;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class OzelAdaptor extends BaseAdapter {
    LayoutInflater layoutInflater;
    List <ListViewMenu> list;
    Activity activity;

    public OzelAdaptor(Activity activity, List<ListViewMenu> mlist){
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        list = mlist;
        this.activity=activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View menuView;
        menuView = layoutInflater.inflate(R.layout.list_view_menu, null);
        TextView textView_menuName = menuView.findViewById(R.id.textView_menuName);
        TextView textView_menuSoruSayisi = menuView.findViewById(R.id.textView_menuSoruSayisi);
        TextView textView_uyeDogruCevapSayisi = menuView.findViewById(R.id.textView_uyeDogruCevapSayisi);
        TextView textView_uyeYanlisCevapSayisi = menuView.findViewById(R.id.textView_uyeYanlisCevapSayisi);

        final ListViewMenu menuList = list.get(position);

        //Toast.makeText(activity, "123", Toast.LENGTH_SHORT).show();

        textView_menuName.setText(menuList.getMenuName());
        textView_menuSoruSayisi.setText(String.valueOf(menuList.getMenuSoruSayisi()) + " sorudan");
        textView_uyeDogruCevapSayisi.setText(String.valueOf(menuList.getUyeninDogruCevapSayisi()) + " doğru cevap");
        textView_uyeYanlisCevapSayisi.setText(String.valueOf(menuList.getUyeninYanlisCevapSayisi()) + " yanlış cevap");
        return menuView;
    }
}
