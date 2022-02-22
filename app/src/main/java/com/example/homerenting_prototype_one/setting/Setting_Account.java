package com.example.homerenting_prototype_one.setting;

import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.add_order.Add_Order;
import com.example.homerenting_prototype_one.model.TableContract;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Setting_Account extends AppCompatActivity {
    Spinner company_list;
    Context context = Setting_Account.this;
    EditText email_edit, password_edit, confirm_password_edit, phone_edit;
    TextView origin;
    String name, account, password, title, phone;
    Button cancel, add_account;
    final String TAG = "Setting_Account";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_account);

        company_list = findViewById(R.id.company_spinner);
        origin = findViewById(R.id.origin_text);
        cancel = findViewById(R.id.cancel_btn);
        add_account = findViewById(R.id.setAcc_btn);

        email_edit = findViewById(R.id.email_text);
        password_edit = findViewById(R.id.new_pwd_text);
        confirm_password_edit = findViewById(R.id.new_pwd_con_text);
        phone_edit = findViewById(R.id.phone_addText);
        String[] companies= {"請選擇帳號類型", "管理者帳號", "員工帳號"};
        getData();
        ArrayAdapter<String> companiesList = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item, companies);
        company_list.setAdapter(companiesList);
        company_list.setSelection(0);

        company_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = company_list.getSelectedItemPosition();
                if(pos == 1){
                    title = "admin";
                }else if(pos == 2){
                    title = "staff";
                }else title = "";
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        email_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(email_edit.getText().length()==0){
                    email_edit.setError("請輸入電子郵件");
                }else email_edit.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        phone_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(phone_edit.getText().length()==0){
                    phone_edit.setError("請輸入手機號碼");
                }else phone_edit.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(password_edit.getText().length()==0){
                    password_edit.setError("請輸入密碼");
                }else password_edit.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirm_password_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(confirm_password_edit.getText().length()==0){
                    confirm_password_edit.setError("請輸入確認密碼");
                }else if(!password_edit.getText().toString().equals(confirm_password_edit.getText().toString())){
                    confirm_password_edit.setError("確認密碼不符");
                }else confirm_password_edit.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        add_account.setOnClickListener(v -> {
            if(checkEmpty()){
                setAccount();
            }else{
                Toast.makeText(context, "資料有誤，請再次檢查", Toast.LENGTH_SHORT).show();
            }

        });

        cancel.setOnClickListener(v -> {
            Intent intent = new Intent(this, Setting.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });
    }
    private boolean checkEmpty(){
        if(email_edit.getText().length()!=0 && phone_edit.getText().length()!=0 && password_edit.getText().length()!=0){
            if(password_edit.getText().toString().equals(confirm_password_edit.getText().toString())){
                if(title.length()!=0) {
                    account = email_edit.getText().toString();
                    password = confirm_password_edit.getText().toString();
                    phone = phone_edit.getText().toString();
                    return true;
                }
                else return false;
            }else{
                return false;
            }
        }else return false;
    }
    private void getData(){
        String function_name = "company_detail";
        String company_id = getCompany_id(context);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", company_id)
                .build();

        //連線要求
        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL + "/user_data.php")
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //顯示錯誤訊息
                //在app畫面上呈現錯誤訊息
                runOnUiThread(() -> Toast.makeText(context, "連線失敗", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
                Log.i(TAG,"responseData: "+responseData); //顯示資料

                try {
                    JSONArray responseArr = new JSONArray(responseData);
                    JSONObject company = responseArr.getJSONObject(0);

                    name = company.getString("company_name");


                    runOnUiThread(() -> origin.setText(name));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setAccount(){
        String function_name = "add_account";
        String company_id = getCompany_id(context);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", company_id)
                .add("account", account)
                .add("password", password)
                .add("title", title)
                .add("phone", phone)
                .build();

        //連線要求
        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL + "/functional.php")
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //顯示錯誤訊息
                //在app畫面上呈現錯誤訊息
                runOnUiThread(() -> Toast.makeText(context, "連線失敗", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
                Log.i(TAG,"responseData: "+responseData); //顯示資料

                try {
                    JSONObject adding = new JSONObject(responseData);
                    if(adding.getString("status").equals("success")){
                        runOnUiThread(()-> Toast.makeText(context, "新增帳號成功", Toast.LENGTH_SHORT).show());
                        Intent setting = new Intent(context, Setting.class);
                        startActivity(setting);
                        finish();
                    }else{
                        runOnUiThread(()-> Toast.makeText(context, "新增帳號失敗", Toast.LENGTH_SHORT).show());

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}