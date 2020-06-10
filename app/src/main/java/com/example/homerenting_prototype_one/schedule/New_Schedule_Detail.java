package com.example.homerenting_prototype_one.schedule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
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
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

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
    Bundle bundle;
    String order_id;

    OkHttpClient okHttpClient = new OkHttpClient();

    TextView titleText;
    TextView nameText;
    TextView nameTitleText;
    TextView movingDateText;
    TextView fromAddressText;
    TextView toAddressText;
    TextView staffText;
    TextView carText;

    ImageButton backBtn;

    String title;
    String name;
    String nameTitle;
    String contact_address;
    String movingDate;
    String fromAddress;
    String toAddress;
    String staff;
    String car;

    ChipGroup staffGroup;
    ChipGroup carGroup;

    ArrayList<String> staffs_text;
    ArrayList<Integer> staffs;
    ArrayList<String> cars_text;
    ArrayList<Integer> cars;

    Context context;

    String TAG = "New_Schedule_Detail";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_schedule_detail);
        staffs_text = new ArrayList<>();
        staffs = new ArrayList<>();
        cars_text = new ArrayList<>();
        cars = new ArrayList<>();
        context = New_Schedule_Detail.this;

        bundle = getIntent().getExtras();
        order_id = bundle.getString("order_id");

        linking(); //將xml裡的元件連至此java

        getOrder();
        getChip();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String function_name = "modify_vehicleAssignment";
                RequestBody body = new FormBody.Builder()
                        .add("function_name", function_name)
                        .add("order_id", order_id)
                        .add("vehicle_assign", String.valueOf(cars))
                        .build();
                Log.i(TAG, "order_id: "+order_id+", vehicle_assign: "+cars);

                Request request = new Request.Builder()
                        .url(BuildConfig.SERVER_URL+"/functional.php")
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
                                Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        final String responseData = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, responseData, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
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
                Intent system_intent = new Intent(context, System_Schedule.class);
                startActivity(system_intent);
            }
        });
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
        titleText = findViewById(R.id.title_SD);
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
    }

    private void getOrder(){
        String function_name = "order_detail";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .build();

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/user_data.php")
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
                    name = order.getString("name");
                    String gender = order.getString("gender");
                    if(gender.equals("female")) nameTitle = "小姐";
                    else if(gender.equals("male")) nameTitle = "先生";
                    else nameTitle = "";
                    contact_address = order.getString("contact_address");
                    movingDate = getDate(order.getString("moving_date"))+getTime(order.getString("moving_date"));
                    fromAddress = order.getString("moveout_address");
                    toAddress = order.getString("movein_address");

                    int i;
                    car = "";
                    for (i = 1; i < responseArr.length(); i++) {
                        JSONObject vehicle_assign = responseArr.getJSONObject(i);
                        if(!vehicle_assign.has("vehicle_id")) break;
                        Log.i(TAG, "vehicle:" + vehicle_assign);
                        car = car+vehicle_assign.getString("plate_num")+" ";
                    }
                    if(i == 1) car = "尚未安排車輛";

                    if(responseArr.length()-i < 1) staff = "尚未安排人員";
                    else staff = "";
                    for (; i < responseArr.length(); i++) {
                        JSONObject staff_assign = responseArr.getJSONObject(i);
                        if(!staff_assign.has("staff_id")) break;
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
                            Toast.makeText(context, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private void getChip(){
        String function_name = "staff-vehicle_data";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .build();

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/user_data.php")
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
                        Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                //Log.d(TAG,"responseData"+responseData); //顯示資料

                try {
                    //轉換成json格式，array或object
                    final JSONArray responseArr = new JSONArray(responseData);
                    //Log.i(TAG,"responseObj: "+ responseArr);

                    //一筆一筆的取JSONArray中的json資料
                    int i;
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
                                final Chip chip = new Chip(staffGroup.getContext());
                                chip.setId(Integer.parseInt(staff_id));
                                chip.setTag(Integer.parseInt(staff_id));
                                chip.setText(staff_name);
                                chip.setTextSize(18);
                                ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(staffGroup.getContext(), null, 0 ,R.style.Widget_MaterialComponents_Chip_Choice);
                                chip.setChipDrawable(chipDrawable);
                                chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        String sname = chip.getText().toString();
                                        if(isChecked) staffs_text.add(sname);
                                        else staffs_text.remove(sname);
                                        Log.d(TAG, "click chip: " + sname);
                                        Log.d(TAG, "staffs: " + staffs_text);
                                        setStaffText();
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
                                chip.setText(plate_num);
                                chip.setTextSize(18);
                                ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(carGroup.getContext(), null, 0 ,R.style.Widget_MaterialComponents_Chip_Choice);
                                chip.setChipDrawable(chipDrawable);
                                chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        int tag = (int) buttonView.getTag();
                                        String cname = chip.getText().toString();
                                        if(isChecked){
                                            cars.add(tag);
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
