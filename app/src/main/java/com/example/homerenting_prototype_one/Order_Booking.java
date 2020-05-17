package com.example.homerenting_prototype_one;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.order.Order_Cancel;

public class Order_Booking extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__booking);
        Button order = findViewById(R.id.order_btn);
        Button booking_order = findViewById(R.id.bookingOrder_btn);
        Button today_order = findViewById(R.id.todayOrder_btn);
        Button cancel_order = findViewById(R.id.cancelOrder_btn);
        ImageButton valuation_btn = findViewById(R.id.valuation_imgBtn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Order_Booking.this, Order.class);
                startActivity(order_intent);
            }
        });
        booking_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent bookingOrder_intent = new Intent(Order_Booking.this, Order_Booking.class);
//                startActivity(bookingOrder_intent);
            }
        });
        today_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent todayOrder_intent = new Intent(Order_Booking.this, Order_Today.class);
                startActivity(todayOrder_intent);
            }
        });
        cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancelOrder_intent = new Intent(Order_Booking.this, Order_Cancel.class);
                startActivity(cancelOrder_intent);
            }
        });
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Order_Booking.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent order_intent = new Intent(Order.this, Order.class);
//                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Order_Booking.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Order_Booking.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Order_Booking.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
