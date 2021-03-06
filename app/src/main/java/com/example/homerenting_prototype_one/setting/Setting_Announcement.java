package com.example.homerenting_prototype_one.setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.adapter.re_adpater.AnnounceAdapter;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;

public class Setting_Announcement extends AppCompatActivity {
    RecyclerView announceView;

    ArrayList<String[]> data;
    ArrayList<String[]> company_data;

    private static DatabaseHelper dbHelper;
    private static SQLiteDatabase db;

    Context context = this;
    String TAG = "Setting_Announcement";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting__announcement);
        announceView = findViewById(R.id.announce_RV_SA);

        LinearLayout first_announcement = findViewById(R.id.first_announcement_layout);
        final ImageView first_icon = findViewById(R.id.first_announcement_icon);

        first_announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Setting_Announcement.this)
                        .setTitle("????????????")
                        .setMessage("??????????????????3.0.1???2.0.1???2019/10/20?????????????????????????????????????????????")
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                first_icon.setVisibility(View.INVISIBLE);
                            }
                        })
                        .show();
            }
        });

        data = new ArrayList<>();
        company_data = new ArrayList<>();
        dbHelper = new DatabaseHelper(this);
        readData();
        //getData();
        getCompany_Announce_Data();

        setBack_btn();
        globalNav();
    }

    private void readData() {
        db = dbHelper.getReadableDatabase();
        String sql_query = "SELECT * FROM `"+TableContract.AnnouncementTable.TABLE_NAME+"` "+
                "NATURAL JOIN `"+TableContract.AnnouncementCompanyTable.TABLE_NAME+"` "+
                "WHERE company_id = "+getCompany_id(context)+" "+
                "ORDER BY announcement_date DESC;";
        Cursor cursor = db.rawQuery(sql_query, null);

        Log.d(TAG,"cursor count:"+cursor.getCount());//GET result from database
        if(!cursor.moveToNext()){
            Log.d(TAG, "???online database???");
            cursor.close();
            getData();
            //getCompany_Announce_Data();
//            TableContract.AnnouncementTable.getAllAnnounceData();
//            TableContract.AnnouncementCompanyTable.getAllAnnounceCompanyData();
            return;
        }
        Log.d(TAG, "???sqlite database???");
        while(!cursor.isAfterLast()){
            String annouceId = cursor.getString(cursor.getColumnIndexOrThrow(TableContract.AnnouncementTable.COLUMN_NAME_ANNOUNCEMENT_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(TableContract.AnnouncementTable.COLUMN_NAME_TITLE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(TableContract.AnnouncementTable.COLUMN_NAME_DATE));
            String summary = cursor.getString(cursor.getColumnIndexOrThrow(TableContract.AnnouncementTable.COLUMN_NAME_OUTLINE));
            String announceContent = cursor.getString(cursor.getColumnIndexOrThrow(TableContract.AnnouncementTable.COLUMN_NAME_CONTENT));
            String isNew = cursor.getString(cursor.getColumnIndexOrThrow(TableContract.AnnouncementCompanyTable.COLUMN_NAME_NEW));

            String[] row_data = {annouceId, title, date, summary, announceContent, isNew};
            data.add(row_data);

            cursor.moveToNext();
        }

        for(int i=0; i < data.size(); i++)
            Log.i(TAG, "data: "+ Arrays.toString(data.get(i)));
        runOnUiThread(this::setRList);

        cursor.close();
    }

    private void getData(){
        String function_name = "announcement_data";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", getCompany_id(context))
                .build();

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
                Log.d(TAG, "Failed: " + e.getMessage()); //??????????????????
                //???app???????????????????????????
                runOnUiThread(() -> Toast.makeText(context, "????????????", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
                Log.i(TAG,"responseData: "+responseData); //????????????

                try {
                    JSONArray responseArr = new JSONArray(responseData);
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject announce = responseArr.getJSONObject(i);
                        Log.i(TAG, "announce: "+announce);

                        String annouceId = announce.getString("announcement_id");
                        String title = announce.getString("title");
                        String date = announce.getString("announcement_date");
                        String summary = announce.getString("outline");
                        String announceContent = announce.getString("content");


                        String[] row_data = {annouceId, title, date, summary, announceContent};
                        data.add(row_data);

                        db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put((TableContract.AnnouncementTable.COLUMN_NAME_ANNOUNCEMENT_ID), annouceId);
                        values.put((TableContract.AnnouncementTable.COLUMN_NAME_TITLE), title);
                        values.put((TableContract.AnnouncementTable.COLUMN_NAME_OUTLINE), summary);
                        values.put((TableContract.AnnouncementTable.COLUMN_NAME_CONTENT), announceContent);
                        values.put((TableContract.AnnouncementTable.COLUMN_NAME_DATE), date);

                        try{
                            long newRowId = db.insertOrThrow(TableContract.AnnouncementTable.TABLE_NAME, null, values);
                            if(newRowId != -1) {
                                Log.d(TAG,"create successfully");}
                            else Log.d(TAG, "create failed");
                        }catch (SQLException e){
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(!responseData.equals("null")){
                    for(int i=0; i < data.size(); i++)
                        Log.i(TAG, "data: "+ Arrays.toString(data.get(i)));
                    setRList();
                }
            }
        });
    }
    private void getCompany_Announce_Data(){
        String function_name = "announcement_company_data";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", getCompany_id(context))
                .build();

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
                Log.d(TAG, "Failed: " + e.getMessage()); //??????????????????
                //???app???????????????????????????
                runOnUiThread(() -> Toast.makeText(context, "????????????", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
                Log.i(TAG,"responseData: "+responseData); //????????????

                try {
                    JSONArray responseArr = new JSONArray(responseData);
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject announce_company = responseArr.getJSONObject(i);
                        Log.i(TAG, "announce_company: "+ announce_company);

                        String annouce_Id = announce_company.getString("announcement_id");
                        String company_id = announce_company.getString("company_id");
                        String isNew = announce_company.getString("new");

                        String[] row_data = {annouce_Id, company_id, isNew};
                        company_data.add(row_data);

                        db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put((TableContract.AnnouncementCompanyTable.COLUMN_NAME_ANNOUNCEMENT_ID), annouce_Id);
                        values.put((TableContract.AnnouncementCompanyTable.COLUMN_NAME_COMPANY_ID), getCompany_id(context));
                        values.put((TableContract.AnnouncementCompanyTable.COLUMN_NAME_NEW), isNew);


                        try{
                            long newRowId = db.replace(TableContract.AnnouncementCompanyTable.TABLE_NAME, null, values);
                            if(newRowId != -1) {
                                Log.d(TAG,"create successfully");}
                            else Log.d(TAG, "create failed");
                        }catch (SQLException e){
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(!responseData.equals("null")){
                    for(int i=0; i < company_data.size(); i++){
                        Log.i(TAG, "company_data: "+ Arrays.toString(company_data.get(i)));
                    }

                }
            }
        });
    }

    private void setRList(){
        final AnnounceAdapter adapter = new AnnounceAdapter(context, data);
        runOnUiThread(() -> {
            announceView.setLayoutManager(new LinearLayoutManager(context));
            announceView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL)); //?????????
            announceView.setAdapter(adapter);
        });
    }

    private void setBack_btn(){
        ImageButton back_btn = findViewById(R.id.back_imgBtn);
        back_btn.setOnClickListener(v -> {
            super.onBackPressed();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });
    }

    private void globalNav(){
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Setting_Announcement.this, Valuation.class);
                startActivity(valuation_intent);
                overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Setting_Announcement.this, Order.class);
                startActivity(order_intent);
                overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Setting_Announcement.this, Calendar.class);
                startActivity(calender_intent);
                overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Setting_Announcement.this, System.class);
                startActivity(system_intent);
                overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Setting_Announcement.this, Setting.class);
                startActivity(setting_intent);
                overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
            }
        });
    }
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
