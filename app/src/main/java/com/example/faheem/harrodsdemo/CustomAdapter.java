package com.example.faheem.harrodsdemo;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class CustomAdapter extends ArrayAdapter {
    public CustomAdapter(Context context, Object[] objects) {
        super(context,R.layout.custom_list_view, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View v = layoutInflater.inflate(R.layout.custom_list_view,parent,false);

        TextView fk_text = (TextView)v.findViewById(R.id.fkTextId);
        ImageView fk_image = (ImageView) v.findViewById(R.id.fkImageViewId);

        fk_text.setText(getItem(position).toString());
        fk_text.setBackgroundColor(Color.BLACK);
        fk_text.setTextColor(Color.WHITE);
        int r = (int)(Math.random()*50);
        int g = (int)(Math.random()*50);
        int b = (int)(Math.random()*25);
        fk_image.setBackgroundColor(Color.rgb(73+r,169+g,201+b));
        return v;
    }
}
