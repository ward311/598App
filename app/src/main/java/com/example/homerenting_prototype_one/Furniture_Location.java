package com.example.homerenting_prototype_one;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.order.Order_Detail;

import java.util.ArrayList;
import java.util.List;

public class Furniture_Location extends AppCompatActivity {
    private Context context;
    private List<String> location_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_furniture__location );
        ImageButton back_btn = findViewById( R.id.back_imgBtn );
        ListView location_list = findViewById( R.id.furniture_location_listView );
        Button check_btn = findViewById(R.id.check_furniture_location_btn);
        ImageButton valuation_btn = findViewById(R.id.valuation_imgBtn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);
        location_data = new ArrayList<>();
        location_data.add( "單人沙發" );
        location_data.add( "兩人沙發" );
        location_data.add( "三人沙發" );
        location_data.add( "按摩椅" );
        location_data.add( "電視" );
        location_data.add( "單人沙發" );
        location_data.add( "兩人沙發" );
        location_data.add( "三人沙發" );
        location_data.add( "按摩椅" );
        location_data.add( "電視" );
        LocationAdapter location_adapter = new LocationAdapter( location_data );
        location_list.setAdapter( location_adapter );
        Bundle location_bundle = getIntent().getExtras();
        final String key = location_bundle.getString( "key" );
        back_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (key.matches( "order" )){
                    Intent back_intent = new Intent(Furniture_Location.this, Order_Detail.class);
                    startActivity( back_intent );
                }
                else if (key.matches( "today" )){
                    Intent back_intent = new Intent(Furniture_Location.this,Today_Detail.class);
                    startActivity( back_intent );
                }
            }
        } );
        check_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (key.matches( "order" )){
                    Intent back_intent = new Intent(Furniture_Location.this,Order_Detail.class);
                    startActivity( back_intent );
                }
                else if (key.matches( "today" )){
                    Intent back_intent = new Intent(Furniture_Location.this,Today_Detail.class);
                    startActivity( back_intent );
                }
            }
        } );
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Furniture_Location.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Furniture_Location.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Furniture_Location.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Furniture_Location.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Furniture_Location.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
