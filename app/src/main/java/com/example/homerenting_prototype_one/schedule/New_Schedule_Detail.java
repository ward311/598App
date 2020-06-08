package com.example.homerenting_prototype_one.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.Calendar;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.Setting;
import com.example.homerenting_prototype_one.System;
import com.example.homerenting_prototype_one.System_Schedule;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.valuation.Valuation;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.homerenting_prototype_one.show.global_function.getDate;
import static com.example.homerenting_prototype_one.show.global_function.getTime;

public class New_Schedule_Detail extends AppCompatActivity {
    OkHttpClient okHttpClient = new OkHttpClient();

    TextView nameText;
    TextView nameTitleText;
    TextView movingDateText;
    TextView fromAddressText;
    TextView toAddressText;
    TextView staffText;
    TextView carText;

    String name;
    String nameTitle;
    String contact_address;
    String movingDate;
    String fromAddress;
    String toAddress;
    String staff;
    String car;

    ArrayList<String> carArr;

    String TAG = "New_Schedule_Detail";
    private final String PHP = "/user_data.php";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_schedule_detail);

        Bundle bundle = getIntent().getExtras();
        final String order_id = bundle.getString("order_id");

        linking(); //將xml裡的元件連至此java

        Log.d(TAG,"New_Detail_Schedule"+order_id);

        String function_name = "order_detail";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .build();

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+PHP)
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //顯示錯誤訊息
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //在app畫面上呈現錯誤訊息
                        Toast.makeText(New_Schedule_Detail.this, "Toast onFailure.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                //Log.d(TAG,"responseData"+responseData); //顯示資料

                try {
                    JSONArray responseArr = new JSONArray(responseData);
                    JSONObject order = responseArr.getJSONObject(0);
                    Log.i(TAG,"JSONObject of order:"+order);

                    //取得資料
                    name = order.getString("name");
                    String gender = order.getString("gender");
                    if(gender.equals("female")) nameTitle = "小姐";
                    else if(gender.equals("male")) nameTitle = "先生";
                    else nameTitle = "";
                    contact_address = order.getString("contact_address");
                    movingDate = getDate(order.getString("moving_date"));
                    fromAddress = order.getString("moveout_address");
                    toAddress = order.getString("movein_address");

                    int i;
                    car = "";
                    for (i = 1; i < responseArr.length(); i++) {
                        JSONObject vehicle_assign = responseArr.getJSONObject(i);
                        if(!vehicle_assign.has("vehicle_id")) break;
                        Log.i(TAG, "vehicle:" + vehicle_assign);
                        car = car+
                                vehicle_assign.getString("num")+"輛"
                                +vehicle_assign.getString("vehicle_weight")+"噸"
                                +vehicle_assign.getString("vehicle_type")+"\n";
                    }
                    if(i == 1) car = "尚未安排車輛";

                    if(responseArr.length()-i < 1) staff = "尚未安排人員";
                    else staff = "";
                    for (; i < responseArr.length(); i++) {
                        JSONObject staff_assign = responseArr.getJSONObject(i);
                        Log.i(TAG, "staff:" + staff_assign);
                        staff = staff+staff_assign.getString("staff_name")+" ";
                    }

                    //顯示資料
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            nameText.setText(name);
                            nameTitleText.setText(nameTitle);
                            movingDateText.setText(movingDate);
                            fromAddressText.setText(fromAddress);
                            toAddressText.setText(toAddress);
                            staffText.setText(staff);
                            carText.setText(car);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(New_Schedule_Detail.this, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });


        ImageButton back_btn = findViewById(R.id.back_imgBtn);
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(New_Schedule_Detail.this, System_Schedule.class);
                startActivity(system_intent);
            }
        });
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(New_Schedule_Detail.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(New_Schedule_Detail.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(New_Schedule_Detail.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(New_Schedule_Detail.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(New_Schedule_Detail.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }

    private void linking() {
        movingDateText = findViewById(R.id.Date_SD);
        nameText = findViewById(R.id.name_SD);
        nameTitleText = findViewById(R.id.nameTitle_SD);
        fromAddressText = findViewById(R.id.FromAddress_SD);
        toAddressText = findViewById(R.id.ToAddress_SD);
        staffText = findViewById(R.id.staff_SD);
        carText = findViewById(R.id.car_SD);
    }
}
