package com.example.homerenting_prototype_one.valuation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.Calendar;
import com.example.homerenting_prototype_one.MatchMaking_Detail;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.Setting;
import com.example.homerenting_prototype_one.System;
import com.example.homerenting_prototype_one.Valuation_Cancel;
import com.example.homerenting_prototype_one.order.Order;

public class Valuation_MatchMaking extends AppCompatActivity {
    public ListView self_evaluation, booking_evaluation, matchMaking_evaluation, cancel_evaluation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valuation__match_making);
        self_evaluation = findViewById(R.id.selfEvaluation_listView);
        booking_evaluation = findViewById(R.id.bookingEvaluation_listView);
        matchMaking_evaluation = findViewById(R.id.matchMaking_Evaluation_listView);
        cancel_evaluation = findViewById(R.id.cancelEvaluation_listView);
        Button self_btn = findViewById(R.id.selfEvaluation_btn);
        Button booking_btn = findViewById(R.id.bookingEvaluation_btn);
        Button matchMaking_btn = findViewById(R.id.matchMaking_Evaluation_btn);
        Button cancel_btn = findViewById(R.id.cancelEvaluation_btn);
        ImageButton valuation_btn = findViewById(R.id.valuation_imgBtn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);
        LinearLayout first_layout = findViewById(R.id.first_matchMaking_layout);




        first_layout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail_intent = new Intent(Valuation_MatchMaking.this, MatchMaking_Detail.class);
                startActivity(detail_intent);
            }
        } );
        self_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selfValuation_intent = new Intent(Valuation_MatchMaking.this, Valuation.class);
                startActivity(selfValuation_intent);
            }
        });
        booking_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookingValuation_intent = new Intent(Valuation_MatchMaking.this, Valuation_Booking.class);
                startActivity(bookingValuation_intent);
            }
        });
        matchMaking_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent matchMakingValuation_intent = new Intent(Valuation_MatchMaking.this, Valuation_MatchMaking.class);
//                startActivity(matchMakingValuation_intent);
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancelValuation_intent = new Intent(Valuation_MatchMaking.this, Valuation_Cancel.class);
                startActivity(cancelValuation_intent);

            }
        });
//        valuation_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent valuation_intent = new Intent(Valuation.this, Valuation.class);
//                startActivity(valuation_intent);
//            }
//        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Valuation_MatchMaking.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Valuation_MatchMaking.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Valuation_MatchMaking.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Valuation_MatchMaking.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
