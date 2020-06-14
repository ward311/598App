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

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.Calendar;
import com.example.homerenting_prototype_one.furniture.Furniture_Location;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.schedule.New_Schedule_Detail;
import com.example.homerenting_prototype_one.schedule.Schedule_Detail;
import com.example.homerenting_prototype_one.Setting;
import com.example.homerenting_prototype_one.System;
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
import static com.example.homerenting_prototype_one.show.global_function.getDay;
import static com.example.homerenting_prototype_one.show.global_function.getMonth;
import static com.example.homerenting_prototype_one.show.global_function.getTime;
import static com.example.homerenting_prototype_one.show.global_function.getYear;

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
    String movingDatetime;
    String movingTime;
    String fromAddress;
    String toAddress;
    String remainder;
    ArrayList<String> carArr;
    String car;
    String worktime;
    String fee;

    Button check_btn;

    String TAG = "Order_Detail";
    private final String PHP = "/user_data.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__detail);
        Button call_btn = findViewById(R.id.call_btn);
        //furniture_list = findViewById(R.id.furniture_listView);
        Button detail_btn = findViewById(R.id.furniture_btn);

        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);



        final Bundle bundle = getIntent().getExtras();
        final String order_id = bundle.getString("order_id");
        Boolean btn = bundle.getBoolean("btn");

        linking(); //將xml裡的元件連至此java
        if(btn) check_btn.setVisibility(View.VISIBLE);
        else check_btn.setVisibility(View.GONE);

        //傳值
        String function_name = "order_detail";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .build();
        Log.d(TAG, "order_id:"+order_id);

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
                        Toast.makeText(Order_Detail.this, "Toast onFailure.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG,"responseData"+responseData); //顯示資料

                try {
                    JSONArray responseArr = new JSONArray(responseData);
                    JSONObject order = responseArr.getJSONObject(0);
                    Log.i(TAG,"JSONObject of order:"+order);

                    //取得資料
                    name = order.getString("name");
                    gender = order.getString("gender");
                    phone = order.getString("phone");
                    contact_address = order.getString("contact_address");
                    movingDatetime = order.getString("moving_date");
                    movingTime = getDate(movingDatetime)+" "+getTime(movingDatetime);
                    remainder = order.getString("additional");
                    worktime = order.getString("estimate_worktime")+"小時";
                    fee = order.getString("fee")+"元";

                    int i;
                    for(i = 1; i < 3; i++){
                        JSONObject address = responseArr.getJSONObject(i);
                        if(address.getString("from_or_to").equals("from"))
                            fromAddress = address.getString("address");
                        else toAddress = address.getString("address");
                    }

                    car = "";
                    for (i = 3; i < responseArr.length(); i++) {
                        JSONObject vehicle_assign = responseArr.getJSONObject(i);
                        if(!vehicle_assign.has("vehicle_id")) break;
                        Log.i(TAG, "vehicle:" + vehicle_assign);
                        car = car+vehicle_assign.getString("num")+"輛"
                                +vehicle_assign.getString("vehicle_weight")+"噸"
                                +vehicle_assign.getString("vehicle_type")+"\n";
                    }
                    if(i == 1) car = "尚未安排車輛";

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

        check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Order_Detail.this, New_Schedule_Detail.class);
                Bundle bundle = new Bundle();
                bundle.putInt("year", Integer.parseInt(getYear(movingDatetime)));
                bundle.putInt("month", Integer.parseInt(getMonth(movingDatetime)));
                bundle.putInt("date", Integer.parseInt(getDay(movingDatetime)));
                bundle.putString("order_id", order_id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });






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
                bundle.putString( "key","order" );
                detail_intent.putExtras( bundle );
                startActivity( detail_intent);
            }
        } );
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
        check_btn = findViewById(R.id.check_order_btn);
    }
}
