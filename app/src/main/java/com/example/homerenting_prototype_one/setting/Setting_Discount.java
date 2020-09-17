package com.example.homerenting_prototype_one.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.system.System;
import com.example.homerenting_prototype_one.valuation.Valuation;

public class Setting_Discount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting__discount);
        ImageButton back_btn = findViewById(R.id.back_imgBtn);
        Button add_btn = findViewById(R.id.add_discount_btn);
        Button delete_btn = findViewById(R.id.delete_discount_btn);
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);
        final LinearLayout discount_text_layout = findViewById(R.id.new_discount_text_layout);
        final LinearLayout discount_switch_layout = findViewById(R.id.new_discount_switch_layout);
        final LinearLayout discount_begin_layout = findViewById(R.id.new_discount_begin_layout);
        final LinearLayout middle_layout = findViewById(R.id.new_middle_layout);
        final LinearLayout discount_end_layout = findViewById(R.id.new_discount_end_layout);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final EditText discount_edit = new EditText(this);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (discount_edit!=null){
                    ViewGroup discount_group = (ViewGroup) discount_edit.getParent();
                    if (discount_group!=null){
                        discount_group.removeView(discount_edit);
                    }
                }
                new AlertDialog.Builder(Setting_Discount.this)
                        .setTitle("新增優惠選項")
                        .setView(discount_edit)
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TextView discount_text = new TextView(getApplicationContext());
                                Switch discount_switch = new Switch(getApplicationContext());
                                EditText discount_begin = new EditText(getApplicationContext());
                                TextView middle = new TextView(getApplicationContext());
                                EditText discount_end = new EditText(getApplicationContext());
                                discount_text.setText(discount_edit.getText().toString());
                                discount_text.setTextColor(Color.BLACK);
                                discount_text.setTextSize(20);
                                discount_begin.setHint( "10/1" );
                                middle.setText("—");
                                discount_end.setHint( "10/31" );
                                discount_text_layout.addView(discount_text);
                                discount_switch_layout.addView(discount_switch);
                                discount_begin_layout.addView(discount_begin);
                                middle_layout.addView(middle);
                                discount_end_layout.addView(discount_end);
                            }
                        })
                        .setNegativeButton("取消",null).create()
                        .show();
            }
        });
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder( Setting_Discount.this )
                        .setTitle("刪除選項")
                        .setMessage("確定是否刪除?")
                        .setPositiveButton( "確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                discount_text_layout.setVisibility( View.INVISIBLE );
//                                discount_switch_layout.setVisibility( View.INVISIBLE );
//                                discount_begin_layout.setVisibility( View.INVISIBLE );
//                                middle_layout.setVisibility( View.INVISIBLE );
//                                discount_end_layout.setVisibility( View.INVISIBLE );
                                discount_text_layout.removeView( discount_text_layout.getChildAt( 0 ) );
                                discount_switch_layout.removeView( discount_switch_layout.getChildAt( 0 ) );
                                discount_begin_layout.removeView( discount_begin_layout.getChildAt( 0 ) );
                                middle_layout.removeView( middle_layout.getChildAt( 0 ) );
                                discount_end_layout.removeView( discount_end_layout.getChildAt( 0 ) );
                            }
                        } )
                        .setNegativeButton("取消",null).create()
                        .show();
            }
        });
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Setting_Discount.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Setting_Discount.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Setting_Discount.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Setting_Discount.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Setting_Discount.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
