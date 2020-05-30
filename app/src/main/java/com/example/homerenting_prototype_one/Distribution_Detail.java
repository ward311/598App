package com.example.homerenting_prototype_one;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.valuation.Valuation;

public class Distribution_Detail extends AppCompatActivity {
    public ListView furniture_list;
    public String[] furnitures = {"1 單人沙發   2    ","2 兩人沙發   1    ","3 三人沙發   1    ","4 L型沙發   1    ",
            "5 沙發桌   3    ","6 傳統電視   1    ","7 液晶電視37吋以下   1    ","8 液晶電視40吋以上   1    ","9 電視櫃   1    ",
            "10 酒櫃   2    ","11 鞋櫃   2    ","12 按摩椅   1    ","13 佛桌   1    ","14 鋼琴   1    ",
            "15 健身器材   3    "};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distribution__detail);

        ImageButton back_btn = findViewById(R.id.back_imgBtn);
        furniture_list = findViewById(R.id.furniture_listView);
        Button input_one = findViewById(R.id.input_btn_one);
        Button input_two = findViewById(R.id.input_btn_two);
        Button check_btn = findViewById(R.id.check_bonus_btn);
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        ArrayAdapter furniture_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,furnitures);
        furniture_list.setAdapter(furniture_adapter);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back_intent = new Intent(Distribution_Detail.this, Bonus_Distribution.class);
                startActivity(back_intent);
            }
        });
        input_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        input_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent distribution_intent = new Intent(Distribution_Detail.this, Bonus_Distribution.class);
                startActivity(distribution_intent);
            }
        });
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Distribution_Detail.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Distribution_Detail.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Distribution_Detail.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Distribution_Detail.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Distribution_Detail.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
