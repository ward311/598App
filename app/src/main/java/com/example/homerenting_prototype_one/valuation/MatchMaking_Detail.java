package com.example.homerenting_prototype_one.valuation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.furniture.Furniture_Detail;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.setting.Setting;
import com.example.homerenting_prototype_one.System;
import com.example.homerenting_prototype_one.order.Order;

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

import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;
import static com.example.homerenting_prototype_one.show.global_function.getDate;
import static com.example.homerenting_prototype_one.show.global_function.getTime;

public class MatchMaking_Detail extends AppCompatActivity {
    TextView nameText;
    TextView nameTitleText;
    TextView phoneText;
    TextView valuationTimeText;
    TextView fromAddressText;
    TextView toAddressText;
    TextView noticeText;
    TextView movingTimeText;
    TextView carText;
    TextView worktimeText;
    TextView priceText;

    String order_id;
    String name;
    String gender;
    String phone;
    String valuationTime;
    String fromAddress;
    String toAddress;
    String notice;
    String movingTime;
    String car;
    String worktime;
    String price;

    Button confirmBtn;

    OkHttpClient okHttpClient = new OkHttpClient();
    Context context = MatchMaking_Detail.this;
    String TAG = "Valuation_MatchMaking_Detail";
    private final String PHP = "/user_data.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_match_making__detail );

        final Bundle bundle = getIntent().getExtras();
        order_id = bundle.getString("order_id");
        Log.d(TAG, "order_id: " + order_id);

        linking();

        String function_name = "valuation_detail";
        String company_id = getCompany_id(this);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .add("company_id",company_id)
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
                Log.d("Fail", "Failed: " + e.getMessage()); //顯示錯誤訊息
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //在app畫面上呈現錯誤訊息
                        Toast.makeText(MatchMaking_Detail.this, "Toast onFailure.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG, "responseData" + responseData); //顯示資料

                try {
                    JSONArray responseArr = new JSONArray(responseData);
                    JSONObject order = responseArr.getJSONObject(0);
                    Log.d(TAG, "member:" + order);
                    name = order.getString("member_name");
                    gender = order.getString("gender");
                    phone = order.getString("phone");
                    valuationTime = getDate(order.getString("valuation_date"));
                    if(!order.getString("valuation_time").equals("null"))
                        valuationTime = valuationTime+" "+order.getString("valuation_time");
                    fromAddress = order.getString("from_address");
                    toAddress = order.getString("to_address");
                    notice = order.getString("additional");
                    String moving_date = order.getString("moving_date");
                    if(moving_date.equals("null")) movingTime = "未安排搬家時間";
                    else movingTime = getDate(moving_date)+" "+getTime(moving_date);
                    if(order.getString("vehicle_type").equals("null")) car = "尚未安排車輛";
                    else car = order.getString("num")+"輛"+order.getString("vehicle_weight")+"噸"+order.getString("vehicle_type");
                    worktime = order.getString("estimate_worktime");
                    if(worktime.equals("null")) worktime = "未預計搬家時長";
                    if(order.getString("accurate_fee").equals("null")) price = "0元";
                    else price = order.getString("accurate_fee")+"元";


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            nameText.setText(name);
                            if(gender.equals("女")) nameTitleText.setText("小姐");
                            else if(gender.equals("男")) nameTitleText.setText("先生");
                            else nameTitleText.setText("");
                            phoneText.setText(phone);
                            valuationTimeText.setText(valuationTime);
                            fromAddressText.setText(fromAddress);
                            toAddressText.setText(toAddress);
                            noticeText.setText(notice);
                            movingTimeText.setText(movingTime);
                            carText.setText(car);
                            worktimeText.setText(worktime);
                            priceText.setText(price);
                        }
                    });

                    int auto = order.getInt("auto");
                    if(auto==0) setConfirmBtn();
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(MatchMaking_Detail.this, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });





//        ImageButton back_btn = findViewById( R.id.back_imgBtn );
//        back_btn.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent back_intent = new Intent(MatchMaking_Detail.this, Valuation_MatchMaking.class);
//                startActivity( back_intent );
//            }
//        } );

        Button call_btn = findViewById(R.id.call_btn);
        call_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call_intent = new Intent(Intent.ACTION_DIAL);
                call_intent.setData( Uri.parse("tel:0933669877"));
                startActivity(call_intent);
            }
        } );

        Button detail_btn = findViewById(R.id.furniture_btn_MMD);
        detail_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail_intent = new Intent();
                detail_intent.setClass( MatchMaking_Detail.this, Furniture_Detail.class );
                bundle.putString("key","match");
                detail_intent.putExtras( bundle );
                startActivity( detail_intent );
            }
        } );

//        Button edit_btn = findViewById(R.id.edit_furniture_btn);
//        edit_btn.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent edit_intent = new Intent(MatchMaking_Detail.this,Edit_Furniture.class);
//                startActivity( edit_intent );
//            }
//        } );

        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);
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

    public void linking(){
        nameText = findViewById(R.id.name_MMD);
        nameTitleText = findViewById(R.id.nameTitle_MMD);
        phoneText = findViewById(R.id.phone_MMD);
        valuationTimeText =findViewById(R.id.valuationTime_MMD);
        fromAddressText = findViewById(R.id.FromAddress_MMD);
        toAddressText = findViewById(R.id.ToAddress_MMD);
        noticeText = findViewById(R.id.notice_MMD);
        movingTimeText = findViewById(R.id.movingTime_MMD);
        carText = findViewById(R.id.car_MMD);
        worktimeText = findViewById(R.id.worktime_MMD);
        priceText = findViewById(R.id.price_MMD);
        confirmBtn = findViewById(R.id.confirm_MMD);
    }

    private void setConfirmBtn(){
        confirmBtn.setVisibility(View.VISIBLE);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String function_name = "become_order";
                RequestBody body = new FormBody.Builder()
                        .add("function_name", function_name)
                        .add("order_id", order_id)
                        .build();

                Request request = new Request.Builder()
                        .url(BuildConfig.SERVER_URL+"/functional.php")
                        .post(body)
                        .build();

                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String responseData = response.body().string();
                        Log.d(TAG, "confirm, responseData: " + responseData);
                    }
                });

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(context, Order.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }, 1000);
            }
        });
    }
}
