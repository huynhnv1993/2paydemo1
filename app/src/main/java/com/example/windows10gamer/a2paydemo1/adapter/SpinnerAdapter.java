package com.example.windows10gamer.a2paydemo1.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.windows10gamer.a2paydemo1.R;
import com.example.windows10gamer.a2paydemo1.activity.Fragment1;
import com.example.windows10gamer.a2paydemo1.model.SpinnerModel;

import java.util.ArrayList;

/**
 * Created by Windows 10 Gamer on 26/06/2017.
 */

public class SpinnerAdapter extends ArrayAdapter<SpinnerModel> {
   int groupid;
    Context context;
    ArrayList<SpinnerModel> list;
    LayoutInflater inflater;

    public SpinnerAdapter(Context context, int groupid, int id, ArrayList<SpinnerModel> list){
        super(context,id,list);
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupid = groupid;

    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(groupid,parent,false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.image_companylogo);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(150, 150);
        imageView.setLayoutParams(params);
        imageView.setImageResource(list.get(position).getImage());
        TextView company = (TextView) itemView.findViewById(R.id.company);
        TextView sub = (TextView) itemView.findViewById(R.id.sub);
        company.setText(list.get(position).getCompanyname());
        sub.setText(list.get(position).getSub());
        return itemView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position,convertView,parent);
    }
}

