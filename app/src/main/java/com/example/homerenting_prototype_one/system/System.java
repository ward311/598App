package com.example.homerenting_prototype_one.system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.helper.DatabaseHelper;
import com.example.homerenting_prototype_one.model.TableContract;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.setting.Setting;
import com.example.homerenting_prototype_one.valuation.Valuation;

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

import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;


public class System extends AppCompatActivity {

    String TAG = "System";
    Context context;
    private static DatabaseHelper dbHelper;
    private static SQLiteDatabase db;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_system);
        ImageButton data_btn = findViewById(R.id.data_imgBtn);
        ImageButton schedule_btn = findViewById(R.id.schedule_imgBtn);
        ImageButton vacation_btn = findViewById(R.id.vacation_imgBtn);
        ImageButton bonus_btn = findViewById(R.id.bonus_imgBtn);
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);


        dbHelper = new DatabaseHelper(this);
        getAllStaffData();
        getAllVehicleData();
        if(sp.getString("title", null).contains("staff")){
            data_btn.setEnabled(false);
            data_btn.setBackgroundColor(Color.parseColor("#f1f1f1"));
            data_btn.setAlpha(.3F);
            schedule_btn.setEnabled(false);
            schedule_btn.setBackgroundColor(Color.parseColor("#f2f2f2"));
            schedule_btn.setAlpha(.3F);
            vacation_btn.setEnabled(false);
            vacation_btn.setBackgroundColor(Color.parseColor("#f2f2f2"));
            vacation_btn.setAlpha(.3F);

        }else{
            data_btn.setEnabled(true);
            schedule_btn.setEnabled(true);
            vacation_btn.setEnabled(true);
        }

        data_btn.setOnClickListener(v -> {
            Intent data_intent = new Intent(System.this, System_Data.class);
            startActivity(data_intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        schedule_btn.setOnClickListener(v -> {
            Intent schedule_intent = new Intent(System.this, System_Schedule.class);
            startActivity(schedule_intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        vacation_btn.setOnClickListener(v -> {
            Intent vacation_intent = new Intent(System.this, System_Vacation.class);
            startActivity(vacation_intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        bonus_btn.setOnClickListener(v -> {
            Intent bonus_intent = new Intent(System.this, System_Bonus.class);
            startActivity(bonus_intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        //底下nav
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(System.this, Valuation.class);
                valuation_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(valuation_intent);
                overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(System.this, Order.class);
                order_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(order_intent);
                overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(System.this, Calendar.class);
                calender_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(calender_intent);
                overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
            }
        });
//        system_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent system_intent = new Intent(System.this, System.class);
//                startActivity(system_intent);
//            }
//        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(System.this, Setting.class);
                setting_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(setting_intent);
                overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
            }
        });

    }
    public void onBackPressed(){
        Intent toCalendar = new Intent(System.this, Calendar.class);
        toCalendar.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toCalendar);
    }

    private void getAllStaffData(){
        String function_name = "all_staff_data";
        Log.d(TAG, "start getAllStaffData and write in sqlite db");
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
                runOnUiThread(() -> Toast.makeText(context, "連線失敗", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, "responseData of getAllStaffData: "+responseData); //顯示資料

                JSONArray responseArr;
                try {
                    responseArr= new JSONArray(responseData);
                }catch (JSONException e){
                    e.printStackTrace();
                    Log.d(TAG, "getAllStaffData: "+e.getMessage());
                    return;
                }

                int success_counter = 0, fail_counter = 0;
                for (int i = 0; i < responseArr.length(); i++) {
                    JSONObject staff;
                    try {
                        staff = responseArr.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
//                    Log.d(TAG, (i+1)+". member: "+member);

                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    try {
                        values.put(TableContract.StaffTable.COLUMN_NAME_STAFF_ID, staff.getString(TableContract.StaffTable.COLUMN_NAME_STAFF_ID));
                        values.put(TableContract.StaffTable.COLUMN_NAME_STAFF_NAME, staff.getString(TableContract.StaffTable.COLUMN_NAME_STAFF_NAME));
                        values.put(TableContract.StaffTable.COLUMN_NAME_COMPANY_ID, staff.getString(TableContract.StaffTable.COLUMN_NAME_COMPANY_ID));
                        values.put(TableContract.StaffTable.COLUMN_NAME_START_TIME, staff.getString(TableContract.StaffTable.COLUMN_NAME_START_TIME));
                        values.put(TableContract.StaffTable.COLUMN_NAME_END_TIME, staff.getString(TableContract.StaffTable.COLUMN_NAME_END_TIME));
//                        Log.d(TAG, (i+1)+". "+values.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        continue;
                    }

                    try{
                        long newRowId = db.replace(TableContract.StaffTable.TABLE_NAME, null, values);
                        if(newRowId != -1){
                            success_counter = success_counter + 1;
                            Log.d(TAG, "create staff successfully");
                        }
                        else{
                            fail_counter = fail_counter + 1;
                            Log.d(TAG, "create staff failed");
                        }
                    } catch (SQLException e){
                        if(e.getMessage().contains("no such table")) break;
                        if(Objects.requireNonNull(e.getMessage()).contains("PRIMARYKEY"))
                            success_counter = success_counter + 1;
                        else{
                            e.printStackTrace();
                            Log.d(TAG, "insert staff data: "+e.getMessage());
                        }
                    }
                }
                Log.d(TAG, "staff data:\n success data: "+success_counter+", fail data: "+fail_counter);
            }
        });
    }

    private void getAllVehicleData(){
        String function_name = "all_vehicle_data";
        Log.d(TAG, "start getAllVehicleData and write in sqlite db");
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
                runOnUiThread(() -> Toast.makeText(context, "連線失敗", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, "responseData of getAllVehicleData: "+responseData); //顯示資料

                JSONArray responseArr;
                try {
                    responseArr= new JSONArray(responseData);
                }catch (JSONException e){
                    e.printStackTrace();
                    Log.d(TAG, "getAllVehicleData: "+e.getMessage());
                    return;
                }

                /*int success_counter = 0, fail_counter = 0;
                for (int i = 0; i < responseArr.length(); i++) {
                    JSONObject vehicle;
                    try {
                        vehicle = responseArr.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
//                    Log.d(TAG, (i+1)+". vehicle: "+vehicle);

                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    try {
                        values.put(TableContract.VehicleTable.COLUMN_NAME_VEHICLE_ID, vehicle.getString(TableContract.VehicleTable.COLUMN_NAME_VEHICLE_ID));
                        values.put(TableContract.VehicleTable.COLUMN_NAME_PLATE_NUM, vehicle.getString(TableContract.VehicleTable.COLUMN_NAME_PLATE_NUM));
                        values.put(TableContract.VehicleTable.COLUMN_NAME_VEHICLE_WEIGHT, vehicle.getString(TableContract.VehicleTable.COLUMN_NAME_VEHICLE_WEIGHT));
                        values.put(TableContract.VehicleTable.COLUMN_NAME_VEHICLE_TYPE, vehicle.getString(TableContract.VehicleTable.COLUMN_NAME_VEHICLE_TYPE));
                        values.put(TableContract.VehicleTable.COLUMN_NAME_COMPANY_ID, vehicle.getString(TableContract.VehicleTable.COLUMN_NAME_COMPANY_ID));
                        values.put(TableContract.VehicleTable.COLUMN_NAME_START_TIME, vehicle.getString(TableContract.VehicleTable.COLUMN_NAME_START_TIME));
                        values.put(TableContract.VehicleTable.COLUMN_NAME_END_TIME, vehicle.getString(TableContract.VehicleTable.COLUMN_NAME_END_TIME));

//                        Log.d(TAG, (i+1)+". "+values.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        continue;
                    }

                    try{
                        long newRowId = db.insertOrThrow(TableContract.VehicleTable.TABLE_NAME, null, values);
                        if(newRowId != -1){
                            success_counter = success_counter + 1;
                            Log.d(TAG, "create vehicle successfully");
                        }
                        else{
                            fail_counter = fail_counter + 1;
                            Log.d(TAG, "create vehicle failed");
                        }
                    } catch (SQLException e){
                        if(e.getMessage().contains("no such table")) break;
                        if(Objects.requireNonNull(e.getMessage()).contains("PRIMARYKEY"))
                            success_counter = success_counter + 1;
                        else{
                            e.printStackTrace();
                            Log.d(TAG, "insert vehicle data: "+e.getMessage());
                        }
                    }
                }
                Log.d(TAG, "vehicle data:\n success data: "+success_counter+", fail data: "+fail_counter);*/
            }
        });
    }
    public class AsyncTask extends android.os.AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            return null;
        }
    }
}
