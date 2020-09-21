package com.example.homerenting_prototype_one;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.setting.Setting;
import com.example.homerenting_prototype_one.setting.Setting_Evaluation;
import com.example.homerenting_prototype_one.system.System;
import com.example.homerenting_prototype_one.valuation.Valuation;

public class Evaluation_Detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation__detail);
        final LinearLayout reply_layout = findViewById(R.id.reply_layout);
        final EditText reply_edit = findViewById(R.id.reply_edit);
        final TextView reply_text = findViewById( R.id.reply_text );
        final Button reply_btn = findViewById(R.id.reply_btn);
        final Button edit_btn = findViewById(R.id.edit_btn);
        final Button edit_again_btn = findViewById(R.id.edit_again_btn);
        ImageButton back_btn = findViewById(R.id.back_imgBtn);
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        reply_edit.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0){
                    reply_text.setText(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );
        reply_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reply_layout.setVisibility( View.VISIBLE );
                edit_btn.setVisibility( View.VISIBLE );
                reply_btn.setVisibility( View.GONE );
            }
        } );
        edit_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reply_edit.setVisibility( View.GONE );
                reply_text.setVisibility( View.VISIBLE );
                reply_text.setText( reply_edit.getText().toString() );
                edit_btn.setVisibility( View.GONE );
                edit_again_btn.setVisibility( View.VISIBLE );
            }
        } );
        edit_again_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_btn.setVisibility( View.VISIBLE );
                edit_again_btn.setVisibility( View.GONE );
                reply_edit.setVisibility( View.VISIBLE );
                reply_text.setVisibility( View.GONE );
            }
        } );
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back_setting_intent = new Intent(Evaluation_Detail.this, Setting_Evaluation.class);
                startActivity(back_setting_intent);
            }
        });
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Evaluation_Detail.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Evaluation_Detail.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Evaluation_Detail.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Evaluation_Detail.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Evaluation_Detail.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
