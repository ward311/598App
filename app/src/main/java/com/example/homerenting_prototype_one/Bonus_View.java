package com.example.homerenting_prototype_one;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.valuation.Valuation;

public class Bonus_View extends AppCompatActivity {

    public String[] employees = {"王小明","陳聰明","劉光明","吳輝明","邱朝明","葉大明","劉案全",
            "王守興","彭玉唱","徐將義","劉曹可","秦因文","方子優","古蘭花","朱柯基"};
    public String[] total = {"30000","40000","25000","55000","35000","30000","30000",
            "25000","35000","20000","25000","30000","30000","25000","25000"};
    public ListView employee_list, salary_list, date_salary_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus__view);
        ImageButton back_btn = findViewById(R.id.back_imgBtn);
        Button total_btn = findViewById(R.id.total_salary_btn);
        Button chart_btn = findViewById( R.id.chart_btn );
        employee_list = findViewById(R.id.employee_list);
        salary_list = findViewById(R.id.employee_salary_list);
        date_salary_list = findViewById(R.id.date_salary_listview);
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        ArrayAdapter employee_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,employees);
        ArrayAdapter total_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,total);
        employee_list.setAdapter(employee_adapter);
        salary_list.setAdapter(total_adapter);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back_setting_intent = new Intent(Bonus_View.this, System_Bonus.class);
                startActivity(back_setting_intent);
            }
        });
        total_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                salary_list.setVisibility(View.GONE);
//                date_salary_list.setVisibility(View.VISIBLE);
            }
        });
        final Context context = this;
        chart_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog( context);
                dialog.setContentView(R.layout.bonus_dialog);
                //dialog.setTitle("圖表");
                Button dialog_btn = dialog.findViewById( R.id.check_dialog_btn );
                dialog_btn.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                } );
                dialog.show();
                dialog.getWindow().setLayout( 1400,2000 );
            }
        } );
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Bonus_View.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Bonus_View.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Bonus_View.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Bonus_View.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Bonus_View.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
