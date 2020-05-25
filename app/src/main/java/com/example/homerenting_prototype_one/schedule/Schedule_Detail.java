package com.example.homerenting_prototype_one.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.Calendar;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.Setting;
import com.example.homerenting_prototype_one.System;
import com.example.homerenting_prototype_one.System_Schedule;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.order.Order_Booking;
import com.example.homerenting_prototype_one.valuation.Valuation;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Schedule_Detail extends AppCompatActivity {

    TextView date_text;
    Button check_btn;

    OkHttpClient okHttpClient = new OkHttpClient();
    String TAG = "Schedule_Detail";
    private final String PHP = "/functional.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule__detail);

        date_text = findViewById(R.id.schedule_date_text);
        check_btn = findViewById(R.id.check_btn_SD);

        final Bundle bundle = getIntent().getExtras();
        int year = bundle.getInt("year");
        int month = bundle.getInt("month");
        int date = bundle.getInt("date");
        date_text.setText(year+"/"+month+"/"+date);

        check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String order_id = bundle.getString("order_id");
                String function_name = "change_status";
                RequestBody body = new FormBody.Builder()
                        .add("function_name", function_name)
                        .add("table","orders" )
                        .add("order_id", order_id)
                        .add("status","assigned")
                        .build();
                Log.d(TAG,"order_id: " + order_id);

                Request request = new Request.Builder()
                        .url(BuildConfig.SERVER_URL+PHP)
                        .post(body)
                        .build();

                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Schedule_Detail.this, "Toast onFailure.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        final String responseData = response.body().string();
                        Log.d(TAG, "responseData: " + responseData);
                    }
                });

                Intent intent = new Intent(Schedule_Detail.this, Order_Booking.class);
                startActivity(intent);
            }
        });






        //以下為切換頁面之按鈕，包括底下nav
        ImageButton back_btn = findViewById(R.id.back_imgBtn);
        ImageButton valuation_btn = findViewById(R.id.valuation_imgBtn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Schedule_Detail.this, System_Schedule.class);
                startActivity(system_intent);
            }
        });
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Schedule_Detail.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Schedule_Detail.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Schedule_Detail.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Schedule_Detail.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Schedule_Detail.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
