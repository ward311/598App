package com.example.homerenting_prototype_one.setting;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.helper.DatabaseHelper;
import com.example.homerenting_prototype_one.model.TableContract;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.system.System;
import com.example.homerenting_prototype_one.valuation.Valuation;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;

public class Setting_Information extends AppCompatActivity {
    TextView address_text, phone_text, number_text, url_text, email_text, line_text, idea_text, companyIdea_text;
    EditText address_edit, phone_edit, number_edit, url_edit, email_edit, line_edit,idea_edit;
    Button edit_btn, finish_btn;
    String address, phone, staff_num, url, email, line, idea;

    private static DatabaseHelper dbHelper;
    private static SQLiteDatabase db;

    String TAG = "Setting_Information";
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting__information);
        address_text = findViewById(R.id.company_address_SIF);
        phone_text = findViewById(R.id.company_phone_SIF);
        number_text = findViewById(R.id.employee_number_SIF);
        url_text = findViewById(R.id.company_url_SIF);
        email_text = findViewById(R.id.company_email_SIF);
        line_text = findViewById(R.id.company_line_SIF);
        idea_text = findViewById(R.id.company_idea_SIF);
        companyIdea_text = findViewById(R.id.companyIdea_text_SIF);

        address_edit = findViewById(R.id.company_address_edit_SIF);
        phone_edit = findViewById(R.id.company_phone_edit_SIF);
        number_edit = findViewById(R.id.employee_number_edit_SIF);
        url_edit = findViewById(R.id.company_url_edit_SIF);
        email_edit = findViewById(R.id.company_email_edit_SIF);
        line_edit = findViewById(R.id.company_line_edit_SIF);
        idea_edit = findViewById(R.id.company_idea_editText);

        edit_btn = findViewById(R.id.infoEdit_btn_SIF);
        finish_btn = findViewById(R.id.companyInfo_finished_btn_SIF);

        ImageButton back_btn = findViewById(R.id.back_imgBtn);

        back_btn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(this, Setting.class);
            finish();
        });



        dbHelper = new DatabaseHelper(this);
        readData();

        edit_btn.setOnClickListener(v -> {
            companyIdea_text.setGravity(Gravity.CENTER_VERTICAL);

            address_edit.setHint(address);
            phone_edit.setHint(phone);
            number_edit.setHint(staff_num);
            url_edit.setHint(url);
            email_edit.setHint(email);
            line_edit.setHint(line);
            idea_edit.setHint(idea);

            editMode();
        });
        finish_btn.setOnClickListener(v -> {
            if(!address_edit.getText().toString().equals("")) address = address_edit.getText().toString();
            if(!phone_edit.getText().toString().equals("")) phone = phone_edit.getText().toString();
            if(!number_edit.getText().toString().equals("")) staff_num = number_edit.getText().toString();
            if(!url_edit.getText().toString().equals("")) url = url_edit.getText().toString();
            if(!email_edit.getText().toString().equals("")) email = email_edit.getText().toString();
            if(!line_edit.getText().toString().equals("")) line = line_edit.getText().toString();
            if(!idea_edit.getText().toString().equals("")) idea = idea_edit.getText().toString();

            updateCompanyDB();
            updateCompany();
            companyIdea_text.setGravity(Gravity.TOP);

            address_text.setText(address);
            phone_text.setText(phone);
            number_text.setText(staff_num+"人");
            url_text.setText(url);
            email_text.setText(email);
            line_text.setText(line);
            idea_text.setText(idea);

            textMode();
        });

        globalNav();
    }

    private void readData(){ //從SQLite讀資料
        db = dbHelper.getReadableDatabase();

        String[] projection = { //資料表格式
                TableContract.CompanyTable.COLUMN_NAME_COMPANY_ID,
                TableContract.CompanyTable.COLUMN_NAME_COMPANY_NAME,
                TableContract.CompanyTable.COLUMN_NAME_IMG,
                TableContract.CompanyTable.COLUMN_NAME_ADDRESS,
                TableContract.CompanyTable.COLUMN_NAME_PHONE,
                TableContract.CompanyTable.COLUMN_NAME_STAFF_NUM,
                TableContract.CompanyTable.COLUMN_NAME_URL,
                TableContract.CompanyTable.COLUMN_NAME_EMAIL,
                TableContract.CompanyTable.COLUMN_NAME_LINE_ID,
                TableContract.CompanyTable.COLUMN_NAME_PHILOSOPHY,
                TableContract.CompanyTable.COLUMN_NAME_LAST_DISTRIBUTION
        };

        String selection = TableContract.CompanyTable.COLUMN_NAME_COMPANY_ID+" = ?";
        String[] selectionArgs = {"1"};

        Cursor cursor = db.query(
                TableContract.CompanyTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Log.d(TAG, "cursor count:"+cursor.getCount());
        if(!cursor.moveToNext()){
            cursor.close();
            getData();
            return;
        }
        String[] item = {
                cursor.getString(cursor.getColumnIndexOrThrow(TableContract.CompanyTable.COLUMN_NAME_COMPANY_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(TableContract.CompanyTable.COLUMN_NAME_COMPANY_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(TableContract.CompanyTable.COLUMN_NAME_IMG)),
                cursor.getString(cursor.getColumnIndexOrThrow(TableContract.CompanyTable.COLUMN_NAME_ADDRESS)),
                cursor.getString(cursor.getColumnIndexOrThrow(TableContract.CompanyTable.COLUMN_NAME_PHONE)),
                cursor.getString(cursor.getColumnIndexOrThrow(TableContract.CompanyTable.COLUMN_NAME_STAFF_NUM)),
                cursor.getString(cursor.getColumnIndexOrThrow(TableContract.CompanyTable.COLUMN_NAME_URL)),
                cursor.getString(cursor.getColumnIndexOrThrow(TableContract.CompanyTable.COLUMN_NAME_LINE_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(TableContract.CompanyTable.COLUMN_NAME_EMAIL)),
                cursor.getString(cursor.getColumnIndexOrThrow(TableContract.CompanyTable.COLUMN_NAME_PHILOSOPHY)),
                cursor.getString(cursor.getColumnIndexOrThrow(TableContract.CompanyTable.COLUMN_NAME_LAST_DISTRIBUTION))
        };
        Log.d(TAG, "sqlite company: "+Arrays.toString(item));

        address = cursor.getString(cursor.getColumnIndexOrThrow(TableContract.CompanyTable.COLUMN_NAME_ADDRESS));
        phone = cursor.getString(cursor.getColumnIndexOrThrow(TableContract.CompanyTable.COLUMN_NAME_PHONE));
        staff_num = cursor.getString(cursor.getColumnIndexOrThrow(TableContract.CompanyTable.COLUMN_NAME_STAFF_NUM));
        url = cursor.getString(cursor.getColumnIndexOrThrow(TableContract.CompanyTable.COLUMN_NAME_URL));
        email = cursor.getString(cursor.getColumnIndexOrThrow(TableContract.CompanyTable.COLUMN_NAME_EMAIL));
        line = cursor.getString(cursor.getColumnIndexOrThrow(TableContract.CompanyTable.COLUMN_NAME_LINE_ID));
        idea = cursor.getString(cursor.getColumnIndexOrThrow(TableContract.CompanyTable.COLUMN_NAME_PHILOSOPHY));

        cursor.close();
        setTexts();
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

                    address = company.getString("address");
                    phone = company.getString("phone");
                    staff_num = company.getString("staff_num");
                    url = company.getString("url");
                    email = company.getString("email");
                    line = company.getString("line_id");
                    idea = company.getString("philosophy");

                    runOnUiThread(() -> setTexts());

                    Log.d(TAG, "start create database");
                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(TableContract.CompanyTable.COLUMN_NAME_COMPANY_ID, getCompany_id(context));
                    values.put(TableContract.CompanyTable.COLUMN_NAME_COMPANY_NAME, company.getString(TableContract.CompanyTable.COLUMN_NAME_COMPANY_NAME));
                    values.put(TableContract.CompanyTable.COLUMN_NAME_IMG, company.getString(TableContract.CompanyTable.COLUMN_NAME_IMG));
                    values.put(TableContract.CompanyTable.COLUMN_NAME_ADDRESS, address);
                    values.put(TableContract.CompanyTable.COLUMN_NAME_PHONE, phone);
                    values.put(TableContract.CompanyTable.COLUMN_NAME_STAFF_NUM, staff_num);
                    values.put(TableContract.CompanyTable.COLUMN_NAME_URL, url);
                    values.put(TableContract.CompanyTable.COLUMN_NAME_EMAIL, email);
                    values.put(TableContract.CompanyTable.COLUMN_NAME_LINE_ID, line);
                    values.put(TableContract.CompanyTable.COLUMN_NAME_PHILOSOPHY, idea);
                    try {
                        long newRowId = db.insertOrThrow(TableContract.CompanyTable.TABLE_NAME, null, values);
                        if(newRowId != -1) Log.d(TAG, "create successfully");
                        else Log.d(TAG, "create failed");
                    } catch (SQLException e){
                        if(Objects.requireNonNull(e.getMessage()).contains("PRIMARYKEY")){
                            Log.d(TAG, "start update database");
                            String selection = TableContract.CompanyTable.COLUMN_NAME_COMPANY_ID+" = ?";
                            String[] seletctionArgs = {getCompany_id(context)};

                            int count = db.update(
                                    TableContract.CompanyTable.TABLE_NAME,
                                    values,
                                    selection,
                                    seletctionArgs
                            );
                            if(count != -1) Log.d(TAG, "update successfully");
                            else Log.d(TAG, "update failed");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateCompanyDB(){
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableContract.CompanyTable.COLUMN_NAME_ADDRESS, address);
        values.put(TableContract.CompanyTable.COLUMN_NAME_PHONE, phone);
        values.put(TableContract.CompanyTable.COLUMN_NAME_STAFF_NUM, staff_num);
        values.put(TableContract.CompanyTable.COLUMN_NAME_URL, url);
        values.put(TableContract.CompanyTable.COLUMN_NAME_EMAIL, email);
        values.put(TableContract.CompanyTable.COLUMN_NAME_LINE_ID, line);
        values.put(TableContract.CompanyTable.COLUMN_NAME_PHILOSOPHY, idea);

        String selection = TableContract.CompanyTable.COLUMN_NAME_COMPANY_ID+" = ?";
        String[] seletctionArgs = {getCompany_id(context)};

        int count = db.update(
                TableContract.CompanyTable.TABLE_NAME,
                values,
                selection,
                seletctionArgs
        );
        Log.d(TAG,""+count);
        if(count != -1) Log.d(TAG, "update successfully");
        else Log.d(TAG, "update failed");
    }

    private void setTexts(){
        address_text.setText(address);
        phone_text.setText(phone);
        number_text.setText(staff_num+"人");
        url_text.setText(url);
        email_text.setText(email);
        line_text.setText(line);
        idea_text.setText(idea);
    }

    private void editMode(){
        address_text.setVisibility(View.GONE);
        phone_text.setVisibility(View.GONE);
        number_text.setVisibility(View.GONE);
        url_text.setVisibility(View.GONE);
        email_text.setVisibility(View.GONE);
        line_text.setVisibility(View.GONE);
        idea_text.setVisibility(View.GONE);
        edit_btn.setVisibility(View.GONE);

        address_edit.setVisibility(View.VISIBLE);
        phone_edit.setVisibility(View.VISIBLE);
        number_edit.setVisibility(View.VISIBLE);
        url_edit.setVisibility(View.VISIBLE);
        email_edit.setVisibility(View.VISIBLE);
        line_edit.setVisibility(View.VISIBLE);
        idea_edit.setVisibility(View.VISIBLE);
        finish_btn.setVisibility(View.VISIBLE);
    }

    private void textMode(){
        address_text.setVisibility(View.VISIBLE);
        phone_text.setVisibility(View.VISIBLE);
        number_text.setVisibility(View.VISIBLE);
        url_text.setVisibility(View.VISIBLE);
        email_text.setVisibility(View.VISIBLE);
        line_text.setVisibility(View.VISIBLE);
        idea_text.setVisibility(View.VISIBLE);
        edit_btn.setVisibility(View.VISIBLE);

        address_edit.setVisibility(View.GONE);
        phone_edit.setVisibility(View.GONE);
        number_edit.setVisibility(View.GONE);
        url_edit.setVisibility(View.GONE);
        email_edit.setVisibility(View.GONE);
        line_edit.setVisibility(View.GONE);
        idea_edit.setVisibility(View.GONE);
        finish_btn.setVisibility(View.GONE);
    }

    private void updateCompany(){
        String function_name = "update_company";
        String company_id = getCompany_id(context);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", company_id)
                .add("address", address)
                .add("phone", phone)
                .add("staff_num", staff_num)
                .add("url", url)
                .add("email", email)
                .add("line_id", line)
                .add("philosophy", idea)
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //在app畫面上呈現錯誤訊息
                        Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
                Log.i(TAG,"responseData of update_company: "+responseData); //顯示資料
            }
        });
    }

    private void globalNav(){ //底下nav
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        valuation_btn.setOnClickListener(v -> {
            Intent valuation_intent = new Intent(Setting_Information.this, Valuation.class);
            startActivity(valuation_intent);
        });
        order_btn.setOnClickListener(v -> {
            Intent order_intent = new Intent(Setting_Information.this, Order.class);
            startActivity(order_intent);
        });
        calendar_btn.setOnClickListener(v -> {
            Intent calender_intent = new Intent(Setting_Information.this, Calendar.class);
            startActivity(calender_intent);
        });
        system_btn.setOnClickListener(v -> {
            Intent system_intent = new Intent(Setting_Information.this, System.class);
            startActivity(system_intent);
        });
        setting_btn.setOnClickListener(v -> {
            Intent setting_intent = new Intent(Setting_Information.this, Setting.class);
            startActivity(setting_intent);
        });
    }
}
