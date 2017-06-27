package com.example.windows10gamer.a2paydemo1.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {
    private String username,password;
    private Button ok;
    private EditText editTextUsername,editTextPassword;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        ok = (Button)findViewById(R.id.buttonSignin);
        ok.setOnClickListener(this);
        editTextUsername = (EditText)findViewById(R.id.text_input_user);
        editTextPassword = (EditText)findViewById(R.id.text_input_password);
        saveLoginCheckBox = (CheckBox)findViewById(R.id.checkBoxSave);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            editTextUsername.setText(loginPreferences.getString("username", ""));
            editTextPassword.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }
    }


    public void onClick(View view) {
        if (view == ok) {
            progressDialog = ProgressDialog.show(SigninActivity.this, "ĐĂNG NHẬP",
                    "Quá trình đăng nhập sẽ kết thúc sau 1 tiếng =]]].", true);
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editTextUsername.getWindowToken(), 0);

            username = editTextUsername.getText().toString();
            password = editTextPassword.getText().toString();

            if (saveLoginCheckBox.isChecked()) {
                loginPrefsEditor.putBoolean("saveLogin", true);
                loginPrefsEditor.putString("username", username);
                loginPrefsEditor.putString("password", password);
                loginPrefsEditor.commit();
            } else {
                loginPrefsEditor.clear();
                loginPrefsEditor.commit();
            }
            doSomethingElse();
        }
    }

    public void doSomethingElse() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Server.Urlsignin +"username="+username+"&password="+password;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getInt("errorcode") == 0){
                            Intent intent = new Intent(SigninActivity.this,MainActivity.class);
                            intent.putExtra("profile", String.valueOf(jsonObject.getJSONObject("profile")));
                            intent.putExtra("token", jsonObject.getString("token"));
                            startActivity(intent);
                            SigninActivity.this.finish();
                            progressDialog.dismiss();
                        }else {
                            progressDialog.dismiss();
                            Toast bread = Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG);
                            bread.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Toast bread = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG);
                        bread.show();
                    }

                }
                Log.d("success ", response);
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
}
