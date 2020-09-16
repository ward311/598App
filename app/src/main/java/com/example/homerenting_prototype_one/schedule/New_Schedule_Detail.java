package com.example.homerenting_prototype_one.schedule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.setting.Setting;
import com.example.homerenting_prototype_one.system.System;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.order.Order_Booking;
import com.example.homerenting_prototype_one.valuation.Valuation;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.shape.ShapeAppearanceModel;

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

import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;
import static com.example.homerenting_prototype_one.show.global_function.getDate;
import static com.example.homerenting_prototype_one.show.global_function.getTime;
import static com.example.homerenting_prototype_one.show.global_function.getlastDatalist;
import static com.example.homerenting_prototype_one.show.global_function.getnextDatalist;

public class New_Schedule_Detail extends AppCompatActivity {
    Bundle bundle;
    String order_id;

    TextView titleText, nameText, nameTitleText, movingDateText, fromAddressText, toAddressText;
    TextView staffText, carText;

    ImageButton backBtn, lastBtn, nextBtn;
    Button submitBtn;

    String name, nameTitle, movingDate, fromAddress, toAddress;
    String staff, car;

    ChipGroup staffGroup;
    ChipGroup carGroup;

    ArrayList<String> staffs_text, cars_text;
    ArrayList<Integer> staffs, cars;

    Context context = this;

    String TAG = "New_Schedule_Detail";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_schedule_detail);

        staffs_text = new ArrayList<>();
        staffs = new ArrayList<>();
        cars_text = new ArrayList<>();
        cars = new ArrayList<>();

//        bundle = getIntent().getExtras();
        order_id = "12";//bundle.getString("order_id");

        linking(); //將xml裡的元件連至此java

        getOrder();
        getChip();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String function_name = "modify_staff_vehicle";
                RequestBody body = new FormBody.Builder()
                        .add("function_name", function_name)
                        .add("order_id", order_id)
                        .add("vehicle_assign", String.valueOf(cars))
                        .add("staff_assign", String.valueOf(staffs))
                        .build();
                Log.i(TAG, "order_id: "+order_id+", vehicle_assign: "+cars+", staff_assign: "+staffs);

                Request request = new Request.Builder()
                        .url(BuildConfig.SERVER_URL+"/functional.php")
                        .post(body)
                        .build();

                OkHttpClient okHttpClient = new OkHttpClient();
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
                                Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        final String responseData = response.body().string();
                        Log.d(TAG, "submit: "+responseData);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(context, responseData, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(context, Order_Booking.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }, 1000);
            }
        });

        lastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_order_id = getlastDatalist(order_id);
                bundle.putString("order_id", new_order_id);
                if(new_order_id == null)
                    Toast.makeText(context, "This is the last order.", Toast.LENGTH_LONG).show();
                else{
                    Intent intent = new Intent(context, New_Schedule_Detail.class);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_order_id = getnextDatalist(order_id);
                bundle.putString("order_id", new_order_id);
                if(new_order_id == null)
                    Toast.makeText(context, "This is the final order.", Toast.LENGTH_LONG).show();
                else{
                    Intent intent = new Intent(context, New_Schedule_Detail.class);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });


        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(context, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(context, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(context, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(context, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(context, Setting.class);
                startActivity(setting_intent);
            }
        });

    }

    private void linking() {
        titleText = findViewById(R.id.title_nSD);
        nameText = findViewById(R.id.name_SD);
        nameTitleText = findViewById(R.id.nameTitle_SD);
        movingDateText = findViewById(R.id.date_SD);
        fromAddressText = findViewById(R.id.FromAddress_SD);
        toAddressText = findViewById(R.id.ToAddress_SD);
        staffText = findViewById(R.id.staff_SD);
        carText = findViewById(R.id.car_SD);
        staffGroup = findViewById(R.id.staffCG_SD);
        carGroup = findViewById(R.id.carCG_SD);
        backBtn = findViewById(R.id.back_btn_SD);
        lastBtn = findViewById(R.id.last_imgBtn_SD);
        nextBtn = findViewById(R.id.next_imgBtn_SD);
        submitBtn = findViewById(R.id.submit_SD);
    }

    private void getOrder(){
        String function_name = "order_detail";
        String company_id = getCompany_id(this);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .add("company_id", company_id)
                .add("assign", "true")
                .build();

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/user_data.php")
                .post(body)
                .build();


        OkHttpClient okHttpClient = new OkHttpClient();
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
                        Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show();
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
                    name = order.getString("member_name");
                    String gender = order.getString("gender");
                    if(gender.equals("female")) nameTitle = "小姐";
                    else if(gender.equals("male")) nameTitle = "先生";
                    else nameTitle = "客戶";
                    movingDate = getDate(order.getString("moving_date"))+" "+getTime(order.getString("moving_date"));
                    fromAddress = order.getString("from_address");
                    toAddress = order.getString("to_address");

                    int i;
                    car = "";
                    for (i = 1; i < responseArr.length(); i++) {
                        JSONObject vehicle_assign = responseArr.getJSONObject(i);
                        if(!vehicle_assign.has("vehicle_id")) break;
                        Log.i(TAG, "vehicle_assign:" + vehicle_assign);
                        car = car+vehicle_assign.getString("plate_num")+" ";
                        cars_text.add(vehicle_assign.getString("plate_num"));
                        cars.add(Integer.parseInt(vehicle_assign.getString("vehicle_id")));
                    }
                    if(i == 1) car = "尚未安排車輛";
                    Log.d(TAG, "car: "+car);

                    if(responseArr.length()-i < 1) staff = "尚未安排人員";
                    else staff = "";
                    for (; i < responseArr.length(); i++) {
                        JSONObject staff_assign = responseArr.getJSONObject(i);
                        if(!staff_assign.has("staff_id")) break;
                        Log.i(TAG, "staff_assign:" + staff_assign);
                        staff = staff+staff_assign.getString("staff_name")+" ";
                        staffs_text.add(staff_assign.getString("staff_name"));
                        staffs.add(Integer.parseInt(staff_assign.getString("staff_id")));
                    }
                    Log.d(TAG, "staff:"+staff);

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
                            Toast.makeText(context, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private void getChip(){
        final Chip chip1 = findViewById(R.id.chip1_SD); //控制形狀用的chip

        String function_name = "staff-vehicle_data";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", getCompany_id(context))
                .build();

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/user_data.php")
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
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
                        Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                //Log.d(TAG,"responseData: "+responseData); //顯示資料

                try {
                    //轉換成json格式，array或object
                    final JSONArray responseArr = new JSONArray(responseData);
                    //Log.i(TAG,"responseObj: "+ responseArr);

                    //一筆一筆的取JSONArray中的json資料
                    int i;
                    final int chipSize = 16;
                    for (i = 0; i < responseArr.length(); i++) {
                        JSONObject staff = responseArr.getJSONObject(i);
                        if(!staff.has("staff_id")) break;
                        Log.i(TAG, "staff: " + staff);

                        //取欄位資料
                        final String staff_id = staff.getString("staff_id");
                        final String staff_name = staff.getString("staff_name");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //在staffGroup底下新增chip，加入ID和Tag
                                final Chip chip = new Chip(staffGroup.getContext());
                                chip.setId(Integer.parseInt(staff_id));
                                chip.setTag(Integer.parseInt(staff_id));

                                chip.setText(staff_name); //顯示員工姓名
                                chip.setCheckable(true); //可點擊

                                if(staffs_text.contains(staff_name)) chip.setChecked(true); //把本單有的員工列為已點擊
                                chip.setTextSize(chipSize); //文字的大小

                                //選擇chip的模式:choice
                                ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(staffGroup.getContext(), null, 0 ,R.style.Widget_MaterialComponents_Chip_Choice);
                                chip.setChipDrawable(chipDrawable);

                                //將邊框設為方形圓角
                                chip.setShapeAppearanceModel(chip1.getShapeAppearanceModel());

                                //點擊chip
                                chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        int tag = (int) buttonView.getTag();
                                        String sname = chip.getText().toString();
                                        if(isChecked){ //取消選擇
                                            staffs.add(tag);
                                            if(!staffs_text.contains(staff_name))
                                                staffs_text.add(sname);
                                        }
                                        else{ //加入選擇
                                            staffs.remove(new Integer(tag));
                                            staffs_text.remove(sname);
                                        }
                                        Log.i(TAG, "click chip: " + sname);
                                        Log.i(TAG, "staffs_text: " + staffs_text);
                                        Log.i(TAG, "staffs: " + staffs);
                                        setStaffText(); //顯示在上方已選擇員工列
                                    }
                                });
                                staffGroup.addView(chip);
                            }
                        });
                    }

                    for (; i < responseArr.length(); i++) {
                        JSONObject vehicle = responseArr.getJSONObject(i);
                        if(!vehicle.has("vehicle_id")) break;
                        Log.i(TAG, "vehicle: " + vehicle);

                        //取欄位資料
                        final String vehicle_id = vehicle.getString("vehicle_id");
                        final String plate_num = vehicle.getString("plate_num");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final Chip chip = new Chip(carGroup.getContext());
                                chip.setId(Integer.parseInt(vehicle_id));
                                chip.setTag(Integer.parseInt(vehicle_id));

                                chip.setText(plate_num); //顯示車牌號碼
                                chip.setCheckable(true); //可點擊

                                if(cars_text.contains(plate_num)) chip.setChecked(true); //把本單有的車輛列為已點擊
                                chip.setTextSize(chipSize); //文字的大小

                                //選擇chip的模式:choice
                                ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(carGroup.getContext(), null, 0 ,R.style.Widget_MaterialComponents_Chip_Choice);
                                chip.setChipDrawable(chipDrawable);

                                //將邊框設為方形圓角
                                chip.setShapeAppearanceModel(chip1.getShapeAppearanceModel());

                                chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        int tag = (int) buttonView.getTag();
                                        String cname = chip.getText().toString();
                                        if(isChecked){
                                            cars.add(tag);
                                            if(!cars_text.contains(plate_num))
                                                cars_text.add(cname);
                                        }
                                        else{
                                            cars.remove(new Integer(tag));
                                            cars_text.remove(cname);
                                        }
                                        Log.i(TAG, "click chip: " + cname);
                                        Log.i(TAG, "cars_text: " + cars_text);
                                        Log.i(TAG, "cars: " + cars);
                                        setCarText();
                                    }
                                });
                                carGroup.addView(chip);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private void setStaffText(){
        staff = "";
        for(int i = 0; i < staffs_text.size(); i++)
            staff = staff + staffs_text.get(i) + " ";
        staffText.setText(staff);
    }

    private void setCarText(){
        car = "";
        for(int i = 0; i < cars_text.size(); i++)
            car = car + cars_text.get(i) + " ";
        carText.setText(car);
    }
}
