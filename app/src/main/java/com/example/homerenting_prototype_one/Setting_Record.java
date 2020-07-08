package com.example.homerenting_prototype_one;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.valuation.Valuation;

import java.util.ArrayList;
import java.util.List;

public class Setting_Record extends AppCompatActivity {
    private ListView list;
    private List<String> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting__record);
        ImageButton back_btn = findViewById(R.id.back_imgBtn);
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        data = new ArrayList<>();
        list = findViewById(R.id.year_list);
        data.add( "2020" );
        data.add( "2019" );
        data.add( "2018" );
        data.add( "2017" );
        data.add( "2016" );
        MonthAdapter adapter = new MonthAdapter( data );
        list.setAdapter(adapter);
        list.setOnItemClickListener( new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent month_intent = new Intent();
//                month_intent.setClass( Setting_Record.this,Record_Detail.class );
//                Bundle month_bundle = new Bundle();
                switch (view.getId()){
                    case R.id.january_btn:
//                        month_bundle.putString( "month","1月" );
//                        month_intent.putExtras( month_bundle );
//                        startActivity( month_intent );
                    case R.id.february_btn:
                    case R.id.march_btn:
                    case R.id.april_btn:
                    case R.id.may_btn:
                    case R.id.june_btn:
                    case R.id.july_btn:
                    case R.id.august_btn:
                    case R.id.september_btn:
                    case R.id.october_btn:
                    case R.id.november_btn:
                    case R.id.december_btn:
                }
            }
        } );
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back_setting_intent = new Intent(Setting_Record.this, Setting.class);
                startActivity(back_setting_intent);
            }
        });
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Setting_Record.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Setting_Record.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Setting_Record.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Setting_Record.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Setting_Record.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
