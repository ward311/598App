package com.example.homerenting_prototype_one;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.valuation.Valuation;
import com.example.homerenting_prototype_one.valuation.Valuation_MatchMaking;

public class MatchMaking_Detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_match_making__detail );
        ImageButton back_btn = findViewById( R.id.back_imgBtn );
        Button call_btn = findViewById(R.id.call_btn);
        Button detail_btn = findViewById(R.id.furniture_detail_btn);
//        Button edit_btn = findViewById(R.id.edit_furniture_btn);
        ImageButton valuation_btn = findViewById(R.id.valuation_imgBtn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);
        back_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back_intent = new Intent(MatchMaking_Detail.this, Valuation_MatchMaking.class);
                startActivity( back_intent );
            }
        } );
        call_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call_intent = new Intent(Intent.ACTION_DIAL);
                call_intent.setData( Uri.parse("tel:0933669877"));
                startActivity(call_intent);
            }
        } );
        detail_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail_intent = new Intent();
                detail_intent.setClass( MatchMaking_Detail.this,Furniture_Detail.class );
                Bundle detail_bundle = new Bundle();
                detail_bundle.putString("key","match");
                detail_intent.putExtras( detail_bundle );
                startActivity( detail_intent );
            }
        } );
//        edit_btn.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent edit_intent = new Intent(MatchMaking_Detail.this,Edit_Furniture.class);
//                startActivity( edit_intent );
//            }
//        } );
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(MatchMaking_Detail.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(MatchMaking_Detail.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(MatchMaking_Detail.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(MatchMaking_Detail.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(MatchMaking_Detail.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
