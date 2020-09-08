package com.example.homerenting_prototype_one.bouns;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.BonusListAdapter;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.setting.Setting;
import com.example.homerenting_prototype_one.system.System;
import com.example.homerenting_prototype_one.valuation.Valuation;

import java.util.ArrayList;
import java.util.List;

import static com.example.homerenting_prototype_one.show.global_function.getDay;
import static com.example.homerenting_prototype_one.show.global_function.getMonth;
import static com.example.homerenting_prototype_one.show.global_function.getToday;
import static com.example.homerenting_prototype_one.show.global_function.getYear;

public class Bonus_List extends AppCompatActivity {
    ListView bonus_list;
    private List<String> bonus_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus__list);

        ImageButton back_btn = findViewById(R.id.back_imgBtn);
        bonus_list = findViewById( R.id.month_bonus_list );
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);


        TextView month = findViewById(R.id.monthdate_BL);
        String today = getToday("yyyy-MM-dd");
        month.setText("至"+getYear(today)+"年"+getMonth(today)+"月"+getDay(today)+"日");

        bonus_data = new ArrayList<>();
        bonus_data.add( "2020" );
        bonus_data.add( "2019" );
        bonus_data.add( "2018" );
        bonus_data.add( "2017" );
        bonus_data.add( "2016" );
//        MonthAdapter adapter = new MonthAdapter( bonus_data );
        BonusListAdapter adapter = new BonusListAdapter( bonus_data );
        bonus_list.setAdapter( adapter );
        bonus_list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detail_intent = new Intent();
                detail_intent.setClass( Bonus_List.this, Bonus_List_Detail.class );
                Bundle detail_bundle = new Bundle();
                detail_bundle.putString( "month","1月" );
                detail_intent.putExtras( detail_bundle );
                startActivity( detail_intent );
            }
        } );
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back_setting_intent = new Intent(Bonus_List.this, System_Bonus.class);
                startActivity(back_setting_intent);
            }
        });
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Bonus_List.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Bonus_List.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Bonus_List.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Bonus_List.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Bonus_List.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
