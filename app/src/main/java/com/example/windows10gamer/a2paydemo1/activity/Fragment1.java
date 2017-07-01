package com.example.windows10gamer.a2paydemo1.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.windows10gamer.a2paydemo1.R;
import com.example.windows10gamer.a2paydemo1.adapter.GridCardAdapter;
import com.example.windows10gamer.a2paydemo1.adapter.SpinnerAdapter;
import com.example.windows10gamer.a2paydemo1.model.GridCardModel;
import com.example.windows10gamer.a2paydemo1.model.SpinnerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Windows 10 Gamer on 24/06/2017.
 */

public class Fragment1 extends Fragment {
    private int qty = 0,price = 0,Id = 0;
    private double discount = 0;
    private String name = "Viettel";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view1 = inflater.inflate(R.layout.frag1, container, false);
        // Now use the above view to populate the spinner.
        final GridView gridView;
        gridView = (GridView) view1.findViewById(R.id.gridView);
        final ArrayList<GridCardModel> arrList = new ArrayList<>();
        final GridCardAdapter gridAdapter = new GridCardAdapter(getContext(),arrList);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (gridView!= null && gridView.getChildAt(0) != null){
                    ((TextView) parent.getChildAt(position).findViewById(R.id.price)).setTextColor(Color.WHITE);
                    parent.getChildAt(position).findViewById(R.id.card_price).setBackgroundResource(R.drawable.border_box_blue);
                    for (int i = 0;i<parent.getCount();i++){
                        if (i!=position){
                            parent.getChildAt(i).findViewById(R.id.card_price).setBackgroundResource(R.drawable.border_box);
                            ((TextView) parent.getChildAt(i).findViewById(R.id.price)).setTextColor(Color.parseColor("#2d2c2c"));
                        }
                    }

                }
                price = arrList.get(position).getPrice();
                discount = arrList.get(position).getDiscount();
                Id = arrList.get(position).getId();
            }
        });

        final PrepaidActivity activity = (PrepaidActivity) getActivity();
        final ArrayList<SpinnerModel> list = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(activity.getIntent().getStringExtra("cardphone"));
            Iterator<String> temp = json.keys();
            while (temp.hasNext()) {
                String key = temp.next();
                JSONObject value = (JSONObject) json.get(key);
                JSONArray data = value.getJSONArray("data");
                int id = getResources().getIdentifier(key.toLowerCase(),"drawable", "com.example.windows10gamer.a2paydemo1");
                list.add(new SpinnerModel(value.getString("name"),id,"Nhà mạng "+value.getString("name"),data));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Spinner sp = (Spinner) view1.findViewById(R.id.spinner);
        SpinnerAdapter adapter = new SpinnerAdapter(getContext(),R.layout.spinner_rows,R.id.company,list);
        sp.setAdapter(adapter);
        for (int i =0;i<adapter.getCount();i++){
            if (adapter.getItem(i).getCompanyname().equals("Viettel")){
                sp.setSelection(i);
            }
        }

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp != null && sp.getChildAt(0) != null){
                    arrList.clear();
                    ((TextView) sp.getChildAt(0).findViewById(R.id.company))
                            .setTextColor(Color.BLUE);
                    JSONArray jsonArray = list.get(position).getCode();
                    for (int i = 0 ; i<jsonArray.length();i++){
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            arrList.add(new GridCardModel(jsonObject.getInt("id"),jsonObject.getString("code"),jsonObject.getString("name"),jsonObject.getDouble("import_discount"),jsonObject.getDouble("discount"),jsonObject.getInt("sale_price"),jsonObject.getInt("price")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Id = arrList.get(0).getId();
                    gridAdapter.notifyDataSetChanged();
                    price = arrList.get(0).getPrice();
                    discount = arrList.get(0).getDiscount();

                    name = (String) ((TextView) sp.getChildAt(0).findViewById(R.id.company)).getText();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0).findViewById(R.id.company)).setTextColor(Color.RED);
            }
        });

        Button btn_continue1 = (Button) view1.findViewById(R.id.btn_continue1);
        btn_continue1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qty = activity.minteger;
                Intent intent = new Intent(getContext(),PaymentActivity.class);
                intent.putExtra("qty",qty);
                intent.putExtra("id",Id);
                intent.putExtra("name",name);
                intent.putExtra("price",price);
                intent.putExtra("discount",discount);
                intent.putExtra("type","PREPAIDCARD");
                intent.putExtra("profile",activity.getIntent().getStringExtra("profile"));
                intent.putExtra("token",activity.getIntent().getStringExtra("token"));
                startActivity(intent);
            }
        });
        return view1;
    }


}
