package com.example.windows10gamer.a2paydemo1.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.windows10gamer.a2paydemo1.R;
import com.example.windows10gamer.a2paydemo1.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class PaymentActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView imageView;
    TextView txtbalance;
    TextView txtphone,txtnamecard,txtprice,txtqty,txtdiscount,txttotal;
    TableLayout table;
    TableRow row_phone,row_qty;
    JSONObject profile;
    double balance, total;
    Button btnconfirm;
    Dialog alertbox;
    String type,profilenew,sendresponse;
    JSONArray data = null;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Mapping();

        type = getIntent().getStringExtra("type");
        switch (type){
            case "PREPAIDCARD" :
                table.removeView(row_phone);
                JSONObject obj = new JSONObject();
                try {
                    obj.put("type", type);
                    obj.put("cardTypeId", getIntent().getIntExtra("id",0));
                    obj.put("qty", getIntent().getIntExtra("qty",0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                data = new JSONArray();
                data.put(obj);
                Log.d("data", data.toString());
                break;
            case "TOPUP" :
                break;
            default:
                return;
        }
        ActionToolbar();
        ActionButton();
    }



    private void ActionButton() {
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (total > balance){
                    TextView textView1 = (TextView) alertbox.findViewById(R.id.textview_message);
                    TextView textView2 = (TextView) alertbox.findViewById(R.id.title_dialog);
                    textView2.setText("THÔNG BÁO");
                    textView1.setText("Số dư tài khoản không đủ để thực hiện giao dịch!\nVui lòng nạp tiền vào tài khoản!\nCảm ơn!");
                    alertbox.show();
                }else {
                    if (data != null){
//                        Testbuy();
                        ActionBuyCard();
                    }
                }
            }
        });
    }

    private void Testbuy() {
        String response ="{\"errorcode\":0,\"orderid\":867,\"message\":\"Mua h\\u00e0ng th\\u00e0nh c\\u00f4ng.\",\"balance\":\"67,415 \\u20ab\",\"order\":{\"id\":867,\"type\":2,\"mp_transaction_id\":3335,\"total\":19040,\"description\":\"Mua s\\u1eafm: Website\",\"status\":200,\"created_at\":\"2017-06-29 18:20:37\",\"items\":[{\"total\":19040,\"type\":\"PREPAIDCARD\",\"qty\":1,\"cardTypeId\":41,\"isLast\":true,\"cards\":[{\"id\":251,\"serial\":\"58238909561\",\"pin\":\"9554-3573-3806-7\",\"price\":\"20000.00\",\"name\":\"Viettel 20K\",\"card_type_id\":41,\"import_transaction_id\":\"BLUECOM_1498735235388326106\",\"pin_ussd\":\"*100*9554357338067#\"}]}]}}";
        Intent intent = new Intent(getApplicationContext(),ConfirmationActivity.class);
        intent.putExtra("discount",getIntent().getDoubleExtra("discount",0.00));
        intent.putExtra("name",getIntent().getStringExtra("name"));
        intent.putExtra("price",getIntent().getIntExtra("price",0));
        intent.putExtra("qty",getIntent().getIntExtra("qty",0));
        intent.putExtra("type",type);
        intent.putExtra("response",response);
        intent.putExtra("profile",getIntent().getStringExtra("profile"));
        intent.putExtra("token",getIntent().getStringExtra("token"));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void ActionBuyCard() {
        progressDialog = ProgressDialog.show(PaymentActivity.this, "GIAO DỊCH",
                "Quá trình giao dịch đang thực hiện. Xin chờ trong giây lát.", true);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Server.Urlbuycard+data.toString()+"&token="+getIntent().getStringExtra("token");
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response ", response);
                sendresponse = response;
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Server.Urlprofile +"&token="+getIntent().getStringExtra("token"), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response1) {
                        Intent intent = new Intent(getApplicationContext(),ConfirmationActivity.class);
                        intent.putExtra("discount",getIntent().getDoubleExtra("discount",0.00));
                        intent.putExtra("name",getIntent().getStringExtra("name"));
                        intent.putExtra("price",getIntent().getIntExtra("price",0));
                        intent.putExtra("qty",getIntent().getIntExtra("qty",0));
                        intent.putExtra("type",type);
                        intent.putExtra("response",sendresponse);
                        intent.putExtra("profile",response1);
                        Log.d("profile1 ", response1);
                        Log.d("profile2 ",getIntent().getStringExtra("profile"));
                        intent.putExtra("token",getIntent().getStringExtra("token"));
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Fail ", error.toString());
                progressDialog.dismiss();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void Mapping() {
        alertbox = showDialogcustom();
        table = (TableLayout) findViewById(R.id.tableLayout);
        row_phone = (TableRow)findViewById(R.id.row_phone);
        row_qty = (TableRow) findViewById(R.id.row_qty);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        imageView = (ImageView) findViewById(R.id.image_type);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(150, 150);
//        params.setMargins(10,10,10,10);
//        imageView.setLayoutParams(params);
        txtdiscount = (TextView) findViewById(R.id.textView_discount);
        Double discount = getIntent().getDoubleExtra("discount",0.00) / 100;
        txtdiscount.setText(getIntent().getDoubleExtra("discount",0.00) + "%");
        txtnamecard = (TextView) findViewById(R.id.textView_namecard);
        txtnamecard.setText(getIntent().getStringExtra("name"));
        txtprice = (TextView) findViewById(R.id.textView_price);
        txtprice.setText(decimalFormat.format(getIntent().getIntExtra("price",0)) + " đ");
        txtqty = (TextView) findViewById(R.id.textView_qty);
        txtqty.setText(String.valueOf(getIntent().getIntExtra("qty",0)));
        txtphone = (TextView) findViewById(R.id.textView_phone);
        txtphone.setText(getIntent().getStringExtra("phone"));
        txttotal = (TextView) findViewById(R.id.textView_total);
        total = getIntent().getIntExtra("price",0) * getIntent().getIntExtra("qty",0) * (1- discount);
        txttotal.setText(decimalFormat.format(total) + " đ");
        txtbalance = (TextView) findViewById(R.id.text_balance);
        try {
            profile = new JSONObject(getIntent().getStringExtra("profile"));
            balance = profile.getDouble("balance");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        txtbalance.setText(decimalFormat.format(balance) + " đ");
        btnconfirm = (Button) findViewById(R.id.button_confirm);

    }

    private void ActionToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_payment);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private Dialog showDialogcustom(){
        final Dialog aDialog = new Dialog(this);
        aDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        aDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        aDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        aDialog.setContentView(R.layout.dialog_custom);
        aDialog.setCancelable(false);
        Button btn_close = (Button) aDialog.findViewById(R.id.close_button);
        Button btn_OK = (Button) aDialog.findViewById(R.id.buttonOK);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aDialog.dismiss();
            }
        });
        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aDialog.dismiss();
            }
        });

        return aDialog;
    }
}
