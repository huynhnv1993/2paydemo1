package com.example.windows10gamer.a2paydemo1.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.windows10gamer.a2paydemo1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class ConfirmationActivity extends AppCompatActivity {
    Button btnbackhome;
    TextView txtphone,txtcardname,txtprice,txtqty,txtdiscount,txttotal,txtbalance,txtcard,txtmessage,txtorderid;
    TableLayout table;
    TableRow row_phone,row_card,row_qty;
    String string,message,type,namecard ="", cardcontent = "";
    JSONObject data,order,item,card;
    JSONArray itemorder,resultcard;
    DecimalFormat decimalFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        Mapping();
        ProcessBuy();
        ActionButton();
        Log.d("profile", getIntent().getStringExtra("profile"));
    }

    private void ActionButton() {
        btnbackhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("token",getIntent().getStringExtra("token"));
                intent.putExtra("profile",getIntent().getStringExtra("profile"));
                startActivity(intent);
                ConfirmationActivity.this.finish();
            }
        });
    }

    private void ProcessBuy() {
        try {
            data = new JSONObject(string);
            if (data.getInt("errorcode")==0){
                switch (type){
                    case "PREPAIDCARD":
                        table.removeView(row_phone);
                        txtorderid.setText(String.valueOf(data.getInt("orderid")));
                        txtmessage.setText(data.getString("message"));
                        txtbalance.setText(data.getString("balance"));
                        order = data.getJSONObject("order");
                        txttotal.setText(decimalFormat.format(order.getInt("total")) + " đ" );
                        itemorder = order.getJSONArray("items");
                        item = itemorder.getJSONObject(0);
                        txtqty.setText(String.valueOf(getIntent().getIntExtra("qty",0)));
                        txtcardname.setText(getIntent().getStringExtra("name"));
                        txtprice.setText(decimalFormat.format(getIntent().getIntExtra("price",0)) + " đ");
                        txtdiscount.setText(String.valueOf(getIntent().getDoubleExtra("discount",0.00)) + "%");
                        resultcard = item.getJSONArray("cards");
                        for (int i = 0; i<resultcard.length() ; i++){
                            card= resultcard.getJSONObject(i);
                            cardcontent += card.getString("serial") + " / " + card.getString("pin") + "\n";
                        }
                        txtcard.setText(cardcontent);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Mapping() {
        decimalFormat = new DecimalFormat("###,###,###");
        txtorderid = (TextView) findViewById(R.id.textView_orderid);
        txtmessage = (TextView) findViewById(R.id.textview_message);
        txtphone = (TextView) findViewById(R.id.textView_phone);
        txtcardname = (TextView) findViewById(R.id.textView_namecard);
        txtprice = (TextView) findViewById(R.id.textView_price);
        txtqty = (TextView) findViewById(R.id.textView_qty);
        txtdiscount = (TextView) findViewById(R.id.textView_discount);
        txttotal = (TextView) findViewById(R.id.textView_total);
        txtbalance = (TextView) findViewById(R.id.textView_balance);
        txtcard = (TextView) findViewById(R.id.textView_card);
        table = (TableLayout) findViewById(R.id.tableLayout);
        row_card = (TableRow) findViewById(R.id.row_card);
        row_qty = (TableRow) findViewById(R.id.row_qty);
        row_phone = (TableRow) findViewById(R.id.row_phone);
        string = getIntent().getStringExtra("response");
        type = getIntent().getStringExtra("type");
        btnbackhome = (Button) findViewById(R.id.button_backhome);


    }
}
