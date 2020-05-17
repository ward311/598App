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
import android.widget.TextView;
import android.widget.Toast;

import com.example.homerenting_prototype_one.Calendar;
import com.example.homerenting_prototype_one.Order_Booking;
import com.example.homerenting_prototype_one.Order_Today;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.Setting;
import com.example.homerenting_prototype_one.System;
import com.example.homerenting_prototype_one.Valuation;
import com.example.homerenting_prototype_one.show.show_user_data;

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

public class Order extends AppCompatActivity {
    OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Button order = findViewById(R.id.order_btn);
        Button booking_order = findViewById(R.id.bookingOrder_btn);
        Button today_order = findViewById(R.id.todayOrder_btn);
        Button cancel_order = findViewById(R.id.cancelOrder_btn);
        ImageButton valuation_btn = findViewById(R.id.valuation_imgBtn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);
        LinearLayout first_order = findViewById(R.id.firstOrder_layout);

        //測試用文字區塊
        final LinearLayout orderL = findViewById(R.id.LinearOrderDetail);
        final TextView resText = new TextView(this);

        //傳至網頁的值，傳function_name
        String function_name = "order_member";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("status", "scheduled")
                .build();

        //連線要求
        Request request = new Request.Builder()
                .url("http://54.166.177.4/user_data.php")
                .post(body)
                .build();

        //連線
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            //連線失敗
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d("Fail", "Failed: " + e.getMessage()); //顯示錯誤訊息
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //在app畫面上呈現錯誤訊息
                        Toast.makeText(Order.this, "Toast onFailure.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            //連線成功
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d("responseData", responseData); //顯示資料

                try {
                    //轉換成json格式，array或object
                    final JSONArray responseArr = new JSONArray(responseData);
                    //final JSONObject responseObj = new JSONObject(responseData);
                    Log.d("JSONObject ","responseObj: "+ responseArr);

                    //一筆一筆的取JSONArray中的json資料
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject member = responseArr.getJSONObject(i);

                        //取欄位資料
                        final String member_id = member.getString("member_id");
                        final String name = member.getString("name");
                        final String gender = member.getString("gender");
                        final String phone = member.getString("phone");
                        final String contact_address = member.getString("contact_address");

                        //呈現在app上
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                show_user_data show = new show_user_data(Order.this, orderL.getContext());
                                orderL.addView(show.create_view()); //分隔線

                                //新增客戶資料
                                ConstraintLayout CustomerInfo;
                                if(gender.equals("female"))
                                    CustomerInfo = show.newCustomerInfoLayout("01/01", "01:23", name, "小姐", phone, contact_address, true);
                                else CustomerInfo = show.newCustomerInfoLayout("01/01", "01:23", name, "先生", phone, contact_address, true);

                                //切換頁面的功能
                                CustomerInfo.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        intent.setClass(Order.this, Order_Detail.class);

                                        //交給其他頁面的變數
                                        Bundle bundle = new Bundle();
                                        bundle.putString("member_id", member_id);
                                        intent.putExtras(bundle);

                                        startActivity(intent);
                                    }
                                });

                                orderL.addView(CustomerInfo); //加入原本的畫面中
                            }
                        });
                    }
                } catch (JSONException e) { //會到這裡通常表示用錯json格式或網頁的資料不是json格式
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Order.this, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });






        first_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail_intent = new Intent(Order.this, Order_Detail.class);
                startActivity(detail_intent);
            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Order.this, Order.class);
                startActivity(order_intent);
            }
        });
        booking_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookingOrder_intent = new Intent(Order.this, Order_Booking.class);
                startActivity(bookingOrder_intent);
            }
        });
        today_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent todayOrder_intent = new Intent(Order.this, Order_Today.class);
                startActivity(todayOrder_intent);
            }
        });
        cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancelOrder_intent = new Intent(Order.this, Order_Cancel.class);
                startActivity(cancelOrder_intent);
            }
        });
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Order.this, Valuation.class);
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
                Intent calender_intent = new Intent(Order.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Order.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Order.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
