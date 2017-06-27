package com.example.windows10gamer.a2paydemo1.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.windows10gamer.a2paydemo1.R;
import com.example.windows10gamer.a2paydemo1.ultil.Server;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {
    private String token;
    private TextView name,phonenumber,balance;
    ProgressDialog progressDialog;
    String cardphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((OnNavigationItemSelectedListener) this);

        ImageView imageView = (ImageView) findViewById(R.id.image_prepaid);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(MainActivity.this, "LOAD DATA",
                        "Quá trình LOAD sẽ kết thúc sau 1 tiếng =]]].", true);
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Server.Urlcardphone, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null){
                            cardphone = response;
                            Intent intent = new Intent(getApplicationContext(),PrepaidActivity.class);
                            intent.putExtra("cardphone", cardphone);
                            startActivity(intent);
                            progressDialog.dismiss();
                        }else {
                            progressDialog.dismiss();
                            Toast bread = Toast.makeText(getApplicationContext(), "Không có dữ liệu", Toast.LENGTH_LONG);
                            bread.show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast bread = Toast.makeText(getApplicationContext(), "Vui lòng kiểm tra lại kết nối mạng", Toast.LENGTH_LONG);
                        bread.show();
                        Log.d("Fail ", error.toString());
                    }
                });
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                requestQueue.add(stringRequest);

            }
        });

        name = (TextView) findViewById(R.id.text_user);
        phonenumber = (TextView) findViewById(R.id.text_phone);
        balance = (TextView) findViewById(R.id.text_balance);

        token = getIntent().getStringExtra("token");
        try {
            JSONObject profile = new JSONObject(getIntent().getStringExtra("profile"));
            name.setText(profile.getString("name"));
            phonenumber.setText(profile.getString("mobilePhone"));
            balance.setText(profile.getString("balanceFormatted"));
            Log.d("name",profile.getString("name"));
            Log.d("phone",profile.getString("mobilePhone"));
            Log.d("balance",profile.getString("balanceFormatted"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
