package com.example.homerenting_prototype_one;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.setting.Setting;
import com.example.homerenting_prototype_one.system.System;
import com.example.homerenting_prototype_one.valuation.Valuation;

public class Information_Edit extends AppCompatActivity {
    public EditText address_edit, phone_edit, number_edit, url_edit, idea_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information__edit);
        address_edit = findViewById(R.id.company_address_edit_SIF);
        phone_edit = findViewById(R.id.company_phone_edit_SIF);
        number_edit = findViewById(R.id.employee_number_edit_SIF);
        url_edit = findViewById(R.id.company_url_edit_SIF);
        idea_edit = findViewById(R.id.company_idea_editText);
        ImageButton back_btn = findViewById(R.id.back_imgBtn);
        Button informationFinished_btn = findViewById(R.id.companyInfo_finished_btn_SIF);
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back_setting_intent = new Intent(Information_Edit.this, Setting.class);
                startActivity(back_setting_intent);
            }
        });
        informationFinished_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent informationEditted_intent = new Intent();
//                informationEditted_intent.setClass(Information_Edit.this, Setting_Information.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("address",address_edit.getText().toString());
//                bundle.putString("phone",phone_edit.getText().toString());
//                bundle.putString("number",number_edit.getText().toString());
//                bundle.putString("url",url_edit.getText().toString());
//                bundle.putString("idea",idea_edit.getText().toString());
//                informationEditted_intent.putExtras(bundle);
//                startActivity(informationEditted_intent);
                //Intent information_editted_intent = new Intent(Information_Edit.this, Setting_Information.class);
            }
        });
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Information_Edit.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Information_Edit.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Information_Edit.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Information_Edit.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Information_Edit.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
