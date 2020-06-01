package com.example.homerenting_prototype_one.furniture;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.Calendar;
import com.example.homerenting_prototype_one.adapter.DetailAdapter;
import com.example.homerenting_prototype_one.Setting;
import com.example.homerenting_prototype_one.System;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.valuation.MatchMaking_Detail;
import com.example.homerenting_prototype_one.valuation.Valuation;
import com.example.homerenting_prototype_one.valuation.ValuationCancel_Detail;
import com.example.homerenting_prototype_one.valuation.Valuation_Detail;

import java.util.ArrayList;
import java.util.List;

import static com.example.homerenting_prototype_one.R.*;

public class Furniture_Detail extends AppCompatActivity {
    ImageButton valuation_btn;
    ImageButton order_btn;
    ImageButton calendar_btn;
    ImageButton system_btn;
    ImageButton setting_btn;

    String TAG = "Furniture_Detail";

    private ListView detail_list;
    ArrayList<String[]> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( layout.activity_furniture__detail );
        ImageButton back_btn = findViewById( id.back_imgBtn );
        Button check_btn = findViewById(id.check_furniture_detail_btn);

        linking();

        Bundle detail_bundle = getIntent().getExtras();
        //final String key = detail_bundle.getString("key");
        //String order_id = detail_bundle.getString("order_id");
        final String key = "valuation";
        String order_id = "6";
        Log.d(TAG, "key: "+key+", order_id: "+order_id);

        detail_list = findViewById( id.furniture_detail_listView );

        data = new ArrayList<>();

//        String[][] datas = {{"test1", "100"}, {"test2", "200"}};
//        data.add(datas[0]);
//        data.add(datas[1]);

        ArrayList<String[]> datas = new ArrayList<>();
        for(int i = 0; i < 2; i++){
            String[] row_data = {"test"+(i+1), String.valueOf(i+1)};
            datas.add(row_data);
        }
        data.add(datas.get(0));
        data.add(datas.get(1));

        DetailAdapter detail_adapter = new DetailAdapter(data);
        detail_list.setAdapter(detail_adapter);



        back_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent back_intent = new Intent(Furniture_Detail.this,Valuation_Detail.class);
//                startActivity( back_intent );
                if (key.matches( "match" )){
                    Intent back_intent = new Intent(Furniture_Detail.this, MatchMaking_Detail.class);
                    startActivity( back_intent );
                }
                else if (key.matches( "cancel" )){
                    Intent back_intent = new Intent(Furniture_Detail.this, ValuationCancel_Detail.class);
                    startActivity( back_intent );
                }
                else {
                    Intent back_intent = new Intent(Furniture_Detail.this, Valuation_Detail.class);
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

        //底下nav
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

    public void linking(){
        valuation_btn = findViewById(id.valuation_imgBtn);
        order_btn = findViewById(id.order_imgBtn);
        calendar_btn = findViewById(id.calendar_imgBtn);
        system_btn = findViewById(id.system_imgBtn);
        setting_btn = findViewById(id.setting_imgBtn);
    }
}
