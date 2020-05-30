package com.example.homerenting_prototype_one.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.Calendar;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.Setting;
import com.example.homerenting_prototype_one.System;
import com.example.homerenting_prototype_one.show.show_noData;
import com.example.homerenting_prototype_one.show.show_user_data;
import com.example.homerenting_prototype_one.valuation.Valuation;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Order_Booking extends AppCompatActivity {
    private final String PHP = "/user_data.php";
    String TAG = "Order_Booking";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__booking);
        Button order = findViewById(R.id.order_btn);
        Button booking_order = findViewById(R.id.bookingOrder_btn);
        Button today_order = findViewById(R.id.todayOrder_btn);
        Button cancel_order = findViewById(R.id.cancelOrder_btn);
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        final LinearLayout orderL = findViewById(R.id.LinearOrderDetail);


        String function_name = "order_member";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("status", "assigned")
                .build();

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+PHP)
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Order_Booking.this, "Toast onFailure.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG,"responseData"+responseData);

                try {
                    final JSONArray responseArr = new JSONArray(responseData);

                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject member = responseArr.getJSONObject(i);
                        final String order_id = member.getString("order_id");
                        final String name = member.getString("name");
                        final String datetime = member.getString("moving_date");
                        //final String datetime = member.getString("move_date")+" "+member.getString("move_time");
                        String gender = member.getString("gender");
                        if (gender.equals("female")) gender ="小姐";
                        //if (gender.equals("女")) gender ="小姐";
                        else gender = "先生";
                        final String phone = member.getString("phone");
                        final String contact_address = member.getString("contact_address");
                        Log.d(TAG,"order_id"+order_id);

                        //0517
                        final String finalGender = gender;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                show_user_data show = new show_user_data(Order_Booking.this, orderL.getContext());
                                orderL.addView(show.create_view()); //分隔線

                                //新增客戶資料
                                ConstraintLayout CustomerInfo;
                                CustomerInfo = show.newCustomerInfoLayout(datetime, name, finalGender, phone, contact_address, false);

                                //切換頁面的功能
                                CustomerInfo.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        intent.setClass(Order_Booking.this, Order_Detail.class);

                                        //交給其他頁面的變數
                                        Bundle bundle = new Bundle();
                                        bundle.putString("order_id", order_id);
                                        bundle.putBoolean("btn", false);
                                        intent.putExtras(bundle);

                                        startActivity(intent);
                                    }
                                });

                                orderL.addView(CustomerInfo); //加入原本的畫面中
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            show_noData show = new show_noData(Order_Booking.this, orderL.getContext());
                            if(responseData.equals("null")) orderL.addView(show.noDataMessage());
                            else Toast.makeText(Order_Booking.this, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });



        //上方nav
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Order_Booking.this, Order.class);
                startActivity(order_intent);
            }
        });
//        booking_order.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent bookingOrder_intent = new Intent(Order_Booking.this, Order_Booking.class);
//                startActivity(bookingOrder_intent);
//            }
//        });
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

        //下方nav
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Order_Booking.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
//        order_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent order_intent = new Intent(Order.this, Order.class);
//                startActivity(order_intent);
//            }
//        });
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
