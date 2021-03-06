package com.example.homerenting_prototype_one.order;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.setting.Setting;
import com.example.homerenting_prototype_one.system.System;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.furniture.Furniture_Location;
import com.example.homerenting_prototype_one.schedule.New_Schedule_Detail;
import com.example.homerenting_prototype_one.valuation.MatchMaking_Detail;
import com.example.homerenting_prototype_one.valuation.Valuation;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;
import static com.example.homerenting_prototype_one.show.global_function.getDate;
import static com.example.homerenting_prototype_one.show.global_function.getDay;
import static com.example.homerenting_prototype_one.show.global_function.getMonth;
import static com.example.homerenting_prototype_one.show.global_function.getTime;
import static com.example.homerenting_prototype_one.show.global_function.getToday;
import static com.example.homerenting_prototype_one.show.global_function.getYear;

public class Order_Detail extends AppCompatActivity {
    OkHttpClient okHttpClient = new OkHttpClient();

    TextView nameText, nameTitleText, phoneText, movingTimeText, fromAddressText, toAddressText;
    TextView remainderText, carText, staffText, worktimeText, feeText, memoText, extraFeeText;
    TextView carDemand;
    String name, gender, phone, contact_address, movingDatetime, movingTime;
    String fromAddress, toAddress, remainder, car, staff, worktime, fee, memo, status, additional_fee;
    String order_id, plan;
    String depositFee;
    Button call_btn, furniture_btn, check_btn, goOrderDetail;

    Bundle bundle;
    Boolean fromRecord = false;

    Context context = Order_Detail.this;
    String TAG = "Order_Detail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__detail);

        linking(); //???xml?????????????????????java

        bundle = getIntent().getExtras();
        order_id = bundle.getString("order_id");
        plan = bundle.getString("plan");
        fromRecord = bundle.getBoolean("fromRecord");
        if(bundle.getBoolean("btn")){
            call_btn.setVisibility(View.GONE);
            check_btn.setVisibility(View.GONE);
            goOrderDetail.setVisibility(View.GONE);
        }

        getOrder();
        Handler handler = new Handler();
        handler.postDelayed(() -> getVehicleData(),1500);

        getStaffData();

        check_btn.setOnClickListener(v -> {
            Intent intent = new Intent(Order_Detail.this, New_Schedule_Detail.class);
            Bundle bundle = new Bundle();
            bundle.putString("order_id", order_id);
            bundle.putString("plan", plan);
            bundle.putString("order_detail", "true");
            intent.putExtras(bundle);
            startActivity(intent);
        });


        call_btn.setOnClickListener(v -> {
            Intent call_intent = new Intent(Intent.ACTION_DIAL);
            call_intent.setData(Uri.parse("tel:"+phone));
            startActivity(call_intent);
        });

        goOrderDetail.setOnClickListener(v -> {
            if(carText.getText().toString().equals("??????????????????")||staffText.getText().toString().equals("??????????????????")){
                new AlertDialog.Builder(context)
                        .setTitle("??????????????????????????????????????????")
                        .setPositiveButton("????????????", (dialog, which) -> {
                            Intent intent = new Intent(Order_Detail.this, New_Schedule_Detail.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("order_id", order_id);
                            bundle.putString("plan", plan);
                            bundle.putString("order_detail", "true");
                            intent.putExtras(bundle);
                            startActivity(intent);
                        })
                        .setNegativeButton("??????",null)
                        .create()
                        .show();
            }else{
                bundle.putString("order_id", order_id);
                bundle.putString("plan", plan);
                bundle.putString("memo", memoText.getText().toString());
                Intent order_detail = new Intent(this, Today_Detail.class);
                order_detail.putExtras(bundle);
                startActivity(order_detail);
                this.finish();
            }

        });

        globalNav();
    }

    private void getOrder(){
        //??????
        String function_name = "order_detail";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .add("company_id", getCompany_id(context))
                .add("plan", plan)
                .build();
        Log.d(TAG, "order_id:"+order_id+", company_id:"+getCompany_id(context)+"plan:"+plan);

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/user_data.php")
                .post(body)
                .build();
        okHttpClient.newBuilder().connectTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES);
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //??????????????????
                //???app???????????????????????????
                runOnUiThread(() -> Toast.makeText(context, "????????????", Toast.LENGTH_LONG).show());
                Looper.prepare();
                    getOrder();
                Looper.loop();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG,"responseData: "+responseData); //????????????

                try {
                    JSONArray responseArr = new JSONArray(responseData);
                    JSONObject order = responseArr.getJSONObject(0);
                    Log.i(TAG,"JSONObject of order:"+order);

                    //????????????
                    name = order.getString("member_name");
                    gender = order.getString("gender");
                    phone = order.getString("phone");
                    contact_address = order.getString("contact_address");
                    movingDatetime = order.getString("moving_date");
                    Log.d(TAG, "movingDatetime: "+movingDatetime);
                    if(!movingDatetime.isEmpty() && !movingDatetime.equals("null"))
                        movingTime = getDate(movingDatetime)+" "+getTime(movingDatetime);
                    else movingTime = "";

                    Date date = java.util.Calendar.getInstance().getTime();
                    DateFormat dateFormat = new SimpleDateFormat("MM/dd");
                    String strDate = dateFormat.format(date);
                    Log.d(TAG, "currentDate: "+strDate);
                    Log.d(TAG, "movingDateTimeGetDate: "+ getDate(movingDatetime));
                    if(!getDate(movingDatetime).equals(strDate)){
                        runOnUiThread(() ->{
                            goOrderDetail.setVisibility(View.GONE);
                            check_btn.setX(400);
                        });
                    }else {
                        runOnUiThread(() ->{
                            goOrderDetail.setVisibility(View.VISIBLE);
                            check_btn.setVisibility(View.VISIBLE);
                        });
                    }
                    if(!order.has("from_address") || order.getString("from_address").equals("null")){
                        fromAddress = order.getString("outcity")+order.getString("outdistrict")+order.getString("address1");
                    }
                    else if(order.has("from_address"))  fromAddress = order.getString("from_address");
                    if(!order.has("to_address") || order.getString("to_address").equals("null")){
                        toAddress = order.getString("incity")+order.getString("indistrict")+order.getString("address2");
                    }
                    else if(order.has("to_address")) toAddress = order.getString("to_address");
                    remainder = order.getString("additional");
                    worktime = order.getString("estimate_worktime")+"??????";
                    if(worktime.equals("null")) worktime = "???????????????";
                    if(fromRecord) fee = order.getString("accurate_fee");
                    else{
                        fee = order.getString("estimate_fee");
                        if(fee.isEmpty() || fee.equals("null")) fee = order.getString("estimate_fee");
                    }
                    String order_status = order.getString("order_status");
                    if(order_status.equals("done")) fee = order.getString("accurate_fee");
                    fee = fee+" ???";
                    //new commit on 05/23
                    //additional_fee = order.getString("additional_price");
                    //if(additional_fee.equals("null")) additional_fee = "0 ???";
                    //additional_fee +=" ???";
                    depositFee = order.getString("deposit_fee");
                    if(depositFee.equals("null")) depositFee = "0 ???";
                    else depositFee +=" ???";
                    memo = order.getString("memo");
                    if(memo.equals("null")) memo = "";

                    status = order.getString("order_status");
                    if(status.equals("paid")||status.equals("done")||status.equals("cancel")){
                        runOnUiThread(() ->{
                            goOrderDetail.setVisibility(View.GONE);
                            check_btn.setVisibility(View.GONE);
                        });
                    }
                    runOnUiThread(() ->{
                        if(status.equals("chosen")){
                            check_btn.setText("????????????");
                        }else if(status.equals("assigned")){
                            check_btn.setText("????????????");
                        }else{
                            check_btn.setText("????????????");
                        }
                    });

                    //????????????
                    runOnUiThread(() -> {
                        nameText.setText(name);
                        if(gender.equals("???")) nameTitleText.setText("??????");
                        else if(gender.equals("???")) nameTitleText.setText("??????");
                        else nameTitleText.setText("");
                        phoneText.setText(phone);
                        movingTimeText.setText(movingTime);
                        fromAddressText.setText(fromAddress);
                        toAddressText.setText(toAddress);
                        remainderText.setText(remainder);
                        worktimeText.setText(worktime);
                        feeText.setText(fee);
                        extraFeeText.setText(depositFee);
                        memoText.setText(memo);
                    });

                    setFurniture_btn(order.getInt("auto"));

                } catch (JSONException e) {
                    e.printStackTrace();
//                    runOnUiThread(() -> Toast.makeText(Order_Detail.this, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show());
                }
            }
        });
    }

    private void getVehicleData(){
        RequestBody body = new FormBody.Builder()
                .add("order_id", order_id)
                .build();

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/get_data/vehicle_each_detail.php")
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //??????????????????
                //???app???????????????????????????
                runOnUiThread(() -> Toast.makeText(context, "????????????", Toast.LENGTH_LONG).show());
                Looper.prepare();
                    getVehicleData();
                Looper.loop();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG, "responseData of vehicle_each_detail: " + responseData); //????????????

                try {
                    JSONArray responseArr = new JSONArray(responseData);

                    int i;
                    car = "";
                    for (i = 0; i < responseArr.length(); i++) {
                        JSONObject vehicle_assign = responseArr.getJSONObject(i);
                        if(!vehicle_assign.has("vehicle_id")) break;
                        Log.i(TAG, "vehicle_assign:" + vehicle_assign);
                        car = car+vehicle_assign.getString("plate_num")+" ";
                    }
                    Log.d(TAG, "car: "+car);
                    runOnUiThread(() -> carText.setText(car));

                } catch (JSONException e) {
                    car = "??????????????????";
                    runOnUiThread(() -> {
                        carText.setText(car);
                        carText.setTextColor(Color.RED);
                    });
                    e.printStackTrace();
                }
                getVehicleDemandData();
            }
        });
    }

    private void getVehicleDemandData(){
        String company_id = getCompany_id(this);
        RequestBody body = new FormBody.Builder()
                .add("order_id", order_id)
                .add("company_id",company_id)
                .build();

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/get_data/vehicle_demand_data.php")
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newBuilder().connectTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES);
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d("Fail", "Failed: " + e.getMessage()); //??????????????????
                //???app???????????????????????????
                runOnUiThread(() -> Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG, "responseData of vehicle_demand_data: " + responseData); //????????????

                car = "???????????????";

                try {
                    JSONArray responseArr = new JSONArray(responseData);

                    int i;
                    String demandCar = "";
                    for (i = 0; i < responseArr.length(); i++) {
                        JSONObject vehicle_demand = responseArr.getJSONObject(i);
                        if(!vehicle_demand.has("num")) break;
                        Log.i(TAG, "vehicle_demand:" + vehicle_demand);
                        if(i != 0) demandCar = demandCar+"" ;

                        demandCar = demandCar+vehicle_demand.getString("vehicle_weight")
                                +vehicle_demand.getString("vehicle_type")
                                +vehicle_demand.getString("num")+"??? ";
                    }
                    Log.d(TAG, "demandCar: "+demandCar);
                    car = demandCar;

                } catch (JSONException e) {
                    if(!responseData.equals("null")) e.printStackTrace();
                }
                runOnUiThread(() -> carDemand.setText(car));
            }
        });
    }

    private void getStaffData(){
        RequestBody body = new FormBody.Builder()
                .add("order_id", order_id)
                .build();

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/get_data/staff_detail.php")
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newBuilder().connectTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES);
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //??????????????????
                //???app???????????????????????????
                runOnUiThread(() -> Toast.makeText(context, "????????????", Toast.LENGTH_LONG).show());
                Looper.prepare();
                getStaffData();
                Looper.loop();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG, "responseData of staff_detail: " + responseData); //????????????
                staff = "??????????????????";

                try {
                    JSONArray responseArr = new JSONArray(responseData);

                    int i;
                    staff = "";
                    for (i = 0; i < responseArr.length(); i++) {
                        JSONObject staff_assign = responseArr.getJSONObject(i);
                        if(!staff_assign.has("staff_id")) break;
                        Log.i(TAG, "staff:" + staff_assign);
                        staff = staff+staff_assign.getString("staff_name");

                        String pay = staff_assign.getString("pay");
                        if(!pay.equals("-1")) staff = staff + "("+pay+")";

                        staff = staff + " ";
                    }

                } catch (JSONException e) {
                     e.printStackTrace();
                }

                runOnUiThread(() ->{
                    staffText.setText(staff);
                    if(staff.equals("??????????????????")) staffText.setTextColor(Color.RED);
                } );
            }
        });
    }

    private void linking(){
        nameText = findViewById(R.id.name_OD);
        nameTitleText = findViewById(R.id.nameTitle_OD);
        phoneText = findViewById(R.id.phone_OD);
        call_btn = findViewById(R.id.call_btn_OD);
        movingTimeText = findViewById(R.id.movingTime_OD);
        fromAddressText = findViewById(R.id.FromAddress_OD);
        toAddressText = findViewById(R.id.ToAddress_OD);
        remainderText = findViewById(R.id.notice_OD);
        carText = findViewById(R.id.car_OD);
        staffText = findViewById(R.id.staff_OD);
        worktimeText = findViewById(R.id.worktime_OD);
        feeText = findViewById(R.id.price_OD);
        extraFeeText = findViewById(R.id.extraFeeText);
        memoText = findViewById(R.id.PS_OD);
        check_btn = findViewById(R.id.check_order_btn);
        furniture_btn = findViewById(R.id.furniture_btn_OD);
        goOrderDetail = findViewById(R.id.goToDetail_btn);
        carDemand = findViewById(R.id.car_demanding);
    }

    private void setFurniture_btn(int auto){
//        if(auto==1){
            furniture_btn.setOnClickListener(v -> {
                Intent detail_intent = new Intent();
                detail_intent.setClass( context, Furniture_Location.class);
                bundle.putString("key", "order_detail");
                detail_intent.putExtras(bundle);
                startActivity( detail_intent);
            });
//        }
    }

    private void globalNav() {
        ImageView back_btn = findViewById(R.id.back_imgBtn);
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        back_btn.setOnClickListener(v -> this.finish());
        valuation_btn.setOnClickListener(v -> {
            Intent valuation_intent = new Intent(Order_Detail.this, Valuation.class);
            startActivity(valuation_intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        order_btn.setOnClickListener(v -> {
            Intent order_intent = new Intent(Order_Detail.this, Order.class);
            startActivity(order_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
        calendar_btn.setOnClickListener(v -> {
            Intent calender_intent = new Intent(Order_Detail.this, Calendar.class);
            startActivity(calender_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
        system_btn.setOnClickListener(v -> {
            Intent system_intent = new Intent(Order_Detail.this, System.class);
            startActivity(system_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
        setting_btn.setOnClickListener(v -> {
            Intent setting_intent = new Intent(Order_Detail.this, Setting.class);
            startActivity(setting_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
    }
}
