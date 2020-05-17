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

import java.util.ArrayList;
import java.util.List;

public class Furniture_Detail extends AppCompatActivity {
    private ListView detail_list;
    private List<String> data,amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_furniture__detail );
        ImageButton back_btn = findViewById( R.id.back_imgBtn );
        Button check_btn = findViewById(R.id.check_furniture_detail_btn);
        ImageButton valuation_btn = findViewById(R.id.valuation_imgBtn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);
        data = new ArrayList<>();
//        amount = new ArrayList<>();
        Context context;
        detail_list = findViewById( R.id.furniture_detail_listView );
        data.add("單人沙發");
        data.add("兩人沙發");
        data.add("三人沙發");
        data.add("L型沙發");
        data.add("沙發桌");
        data.add("傳統電視");
        data.add("液晶電視37吋以下");
        data.add("液晶電視40吋以上");
        data.add("電視櫃");
        data.add("酒櫃");
        data.add("鞋櫃");
        data.add("按摩椅");
        data.add("佛桌");
        data.add("鋼琴");
        data.add("健身器材");
        data.add( "日用品裝箱" );
        data.add( "壁畫" );
        data.add( "電腦" );
//        amount.add( "2" );
//        amount.add( "1" );
//        amount.add( "1" );
//        amount.add( "1" );
//        amount.add( "3" );
//        amount.add( "1" );
//        amount.add( "1" );
//        amount.add( "1" );
//        amount.add( "1" );
//        amount.add( "2" );
//        amount.add( "2" );
//        amount.add( "1" );
//        amount.add( "1" );
//        amount.add( "1" );
//        amount.add( "3" );
//        amount.add( "1" );
//        amount.add( "1" );
//        amount.add( "5" );
        DetailAdapter detail_adapter = new DetailAdapter( data);
        detail_list.setAdapter( detail_adapter );
        Bundle detail_bundle = getIntent().getExtras();
        final String key = detail_bundle.getString( "key" );
        back_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent back_intent = new Intent(Furniture_Detail.this,Valuation_Detail.class);
//                startActivity( back_intent );
                if (key.matches( "match" )){
                    Intent back_intent = new Intent(Furniture_Detail.this,MatchMaking_Detail.class);
                    startActivity( back_intent );
                }
                else if (key.matches( "cancel" )){
                    Intent back_intent = new Intent(Furniture_Detail.this,ValuationCancel_Detail.class);
                    startActivity( back_intent );
                }
                else {
                    Intent back_intent = new Intent(Furniture_Detail.this,Valuation_Detail.class);
                    startActivity( back_intent );
                }
            }
        } );
        check_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent back_intent = new Intent(Furniture_Detail.this,Valuation_Detail.class);
//                startActivity( back_intent );
                if (key.matches( "match" )){
                    Intent back_intent = new Intent(Furniture_Detail.this,MatchMaking_Detail.class);
                    startActivity( back_intent );
                }
                else if (key.matches( "cancel" )){
                    Intent back_intent = new Intent(Furniture_Detail.this,ValuationCancel_Detail.class);
                    startActivity( back_intent );
                }
                else {
                    Intent back_intent = new Intent(Furniture_Detail.this,Valuation_Detail.class);
                    startActivity( back_intent );
                }
            }
        } );
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Furniture_Detail.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Furniture_Detail.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Furniture_Detail.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Furniture_Detail.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Furniture_Detail.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
