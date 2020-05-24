package com.example.homerenting_prototype_one.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.Calendar;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.Setting;
import com.example.homerenting_prototype_one.System;
import com.example.homerenting_prototype_one.System_Schedule;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.order.Order_Booking;
import com.example.homerenting_prototype_one.valuation.Valuation;

public class Schedule_Detail extends AppCompatActivity {
    TextView date_text;
    Button check_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule__detail);

        date_text = findViewById(R.id.schedule_date_text);
        check_btn = findViewById(R.id.check_btn_SD);

        Bundle get_bundle = getIntent().getExtras();
        int year = get_bundle.getInt("year");
        int month = get_bundle.getInt("month");
        int date = get_bundle.getInt("date");
        date_text.setText(year+"/"+month+"/"+date);

        check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "";
                /*
                * 連線至資料庫
                * 輸入function和需要變數
                * 取得回傳的response
                * (確認資料已被改變)
                * */

                //Intent intent = new Intent(Schedule_Detail.this, Order_Booking.class);
                //startActivity(intent);
                check_btn.setText(result);
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
