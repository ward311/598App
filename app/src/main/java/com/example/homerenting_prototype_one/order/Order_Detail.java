package com.example.homerenting_prototype_one.order;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.Calendar;
import com.example.homerenting_prototype_one.Furniture_Location;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.Setting;
import com.example.homerenting_prototype_one.System;
import com.example.homerenting_prototype_one.System_Schedule;
import com.example.homerenting_prototype_one.Valuation;

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

public class Order_Detail extends AppCompatActivity {
    OkHttpClient okHttpClient = new OkHttpClient();
    TextView nameText;
    TextView nameTitleText;
    TextView phoneText;
    TextView movingTimeText;
    TextView fromAddressText;
    TextView toAddressText;
    TextView remainderText;
    TextView carText;
    TextView worktimeText;
    TextView feeText;

    String name;
    String gender;
    String phone;
    String contact_address;
    String movingTime;
    String fromAddress;
    String toAddress;
    String remainder;
    String car;
    String worktime;
    String fee;

    public ListView furniture_list;
    public String[] furnitures = {"1 單人沙發   2    ","2 兩人沙發   1    ","3 三人沙發   1    ","4 L型沙發   1    ",
            "5 沙發桌   3    ","6 傳統電視   1    ","7 液晶電視37吋以下   1    ","8 液晶電視40吋以上   1    ","9 電視櫃   1    ",
            "10 酒櫃   2    ","11 鞋櫃   2    ","12 按摩椅   1    ","13 佛桌   1    ","14 鋼琴   1    ",
            "15 健身器材   3    "};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__detail);
        Button call_btn = findViewById(R.id.call_btn);
        //furniture_list = findViewById(R.id.furniture_listView);
        Button detail_btn = findViewById(R.id.furniture_btn);
        Button check_btn = findViewById(R.id.check_order_btn);
        ImageButton valuation_btn = findViewById(R.id.valuation_imgBtn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);



        Bundle bundle = getIntent().getExtras();
        String order_id = bundle.getString("order_id");

        linking(); //將xml裡的元件連至此java

        //傳值
        String function_name = "order_detail";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .build();

        Request request = new Request.Builder()
                .url("http://54.166.177.4/user_data.php")
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d("Fail", "Failed: " + e.getMessage()); //顯示錯誤訊息
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //在app畫面上呈現錯誤訊息
                        Toast.makeText(Order_Detail.this, "Toast onFailure.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d("responseData", responseData); //顯示資料

                try {
                    JSONArray responseArr = new JSONArray(responseData);
                    JSONObject order = responseArr.getJSONObject(0);
                    Log.i("JSONObject", "order:"+order);

                    //取得資料
                    name = order.getString("name");
                    gender = order.getString("gender");
                    phone = order.getString("phone");
                    contact_address = order.getString("contact_address");
                    movingTime = order.getString("moving_date") + " " + order.getString("moving_time");
                    fromAddress = order.getString("moveout_address");
                    toAddress = order.getString("movein_address");
                    remainder = order.getString("additional");
                    car = order.getString("num")+"輛"+order.getString("vehicle_weight")+"噸"+order.getString("vehicle_type");
                    worktime = order.getString("estimate_worktime");
                    fee = order.getString("fee");

                    //顯示資料
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            nameText.setText(name);
                            if(gender.equals("female")) nameTitleText.setText("小姐");
                            else if(gender.equals("male")) nameTitleText.setText("先生");
                            else nameTitleText.setText("");
                            phoneText.setText(phone);
                            movingTimeText.setText(movingTime);
                            fromAddressText.setText(fromAddress);
                            toAddressText.setText(toAddress);
                            remainderText.setText(remainder);
                            carText.setText(car);
                            worktimeText.setText(worktime);
                            feeText.setText(fee);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Order_Detail.this, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });







        ArrayAdapter furniture_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,furnitures);
        //furniture_list.setAdapter(furniture_adapter);
        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call_intent = new Intent(Intent.ACTION_DIAL);
                call_intent.setData(Uri.parse("tel:0933669877"));
                startActivity(call_intent);
            }
        });
        detail_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail_intent = new Intent();
                detail_intent.setClass( Order_Detail.this, Furniture_Location.class);
                Bundle order_bundle = new Bundle();
                order_bundle.putString( "key","order" );
                detail_intent.putExtras( order_bundle );
                startActivity( detail_intent);
            }
        } );
        check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back_order_intent = new Intent(Order_Detail.this, System_Schedule.class);
                startActivity(back_order_intent);
            }
        });
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Order_Detail.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Order_Detail.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Order_Detail.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Order_Detail.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Order_Detail.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }

    public void linking(){
        nameText = findViewById(R.id.name_OD);
        nameTitleText = findViewById(R.id.nameTitle_OD);
        phoneText = findViewById(R.id.phone_OD);
        movingTimeText = findViewById(R.id.movingTime_OD);
        fromAddressText = findViewById(R.id.FromAddress_OD);
        toAddressText = findViewById(R.id.ToAddress_OD);
        remainderText = findViewById(R.id.notice_OD);
        carText = findViewById(R.id.car_OD);
        worktimeText = findViewById(R.id.worktime_OD);
        feeText = findViewById(R.id.price_OD);
    }
}
