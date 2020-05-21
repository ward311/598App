package com.example.homerenting_prototype_one;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.valuation.Valuation;

public class Setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        LinearLayout company_information = findViewById(R.id.companyInformation_linearLayout);
        LinearLayout service_item = findViewById(R.id.serviceItem_linearLayout);
        LinearLayout discount = findViewById(R.id.discount_linearLayout);
        LinearLayout customer_evaluation = findViewById(R.id.customerEvaluation_linearLayout);
        LinearLayout system_announcement = findViewById(R.id.systemAnnouncement_linearLayout);
        LinearLayout history_record = findViewById(R.id.historyRecord_linearLayout);
        ImageButton valuation_btn = findViewById(R.id.valuation_imgBtn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        company_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent information_intent = new Intent(Setting.this, Setting_Information.class);
                startActivity(information_intent);
            }
        });
        service_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent service_intent = new Intent(Setting.this, Setting_Service.class);
                startActivity(service_intent);
            }
        });
        discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent discount_intent = new Intent(Setting.this, Setting_Discount.class);
                startActivity(discount_intent);
            }
        });
        customer_evaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent evaluation_intent = new Intent(Setting.this, Setting_Evaluation.class);
                startActivity(evaluation_intent);
            }
        });
        system_announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent announcement_intent = new Intent(Setting.this, Setting_Announcement.class);
                startActivity(announcement_intent);
            }
        });
        history_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent record_intent = new Intent(Setting.this, Setting_Record.class);
                startActivity(record_intent);
            }
        });
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Setting.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Setting.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Setting.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Setting.this, System.class);
                startActivity(system_intent);
            }
        });
//        setting_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent setting_intent = new Intent(Setting.this, Setting.class);
//                startActivity(setting_intent);
//            }
//        });
    }
}
