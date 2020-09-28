package com.example.homerenting_prototype_one.system;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.setting.Setting;
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

import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;

public class System_Vacation extends AppCompatActivity {

    ChipGroup staffGroup,  carGroup;

    ArrayList<String> staffs_text, cars_text;
    ArrayList<Integer> staffs, cars;

    Context context = this;
    String TAG = "System_Vacation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system__vacation);
        staffGroup = findViewById(R.id.staffCG_SV);
        carGroup = findViewById(R.id.carCG_SV);

        staffs_text = new ArrayList<>();
        staffs = new ArrayList<>();
        cars_text = new ArrayList<>();
        cars = new ArrayList<>();

        getVocation();
        getChip();

        globalNav();
    }

    private void getVocation(){
        String function_name = "all_vehicle_staff_leave";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", getCompany_id(context))
                .add("date", "2020-09-27")
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
                Log.d(TAG,"responseData of getVocation: "+responseData); //顯示資料

                try {
                    JSONArray responseArr = new JSONArray(responseData);

                    int i;
                    for (i = 0; i < responseArr.length(); i++) {
                        JSONObject vehicle_assign = responseArr.getJSONObject(i);
                        if(!vehicle_assign.has("vehicle_id")) break;
                        Log.i(TAG, "vehicle_assign:" + vehicle_assign);
                        cars_text.add(vehicle_assign.getString("plate_num"));
                        cars.add(Integer.parseInt(vehicle_assign.getString("vehicle_id")));
                    }

                    for (; i < responseArr.length(); i++) {
                        JSONObject staff_assign = responseArr.getJSONObject(i);
                        if(!staff_assign.has("staff_id")) break;
                        Log.i(TAG, "staff_assign:" + staff_assign);
                        staffs_text.add(staff_assign.getString("staff_name"));
                        staffs.add(Integer.parseInt(staff_assign.getString("staff_id")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    if(!responseData.equals("null")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }
        });
    }

    private void getChip(){
        final Chip chip1 = findViewById(R.id.chip1_SV); //控制形狀用的chip

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
                            @RequiresApi(api = Build.VERSION_CODES.M)
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

                                chip.setChipBackgroundColorResource(R.color.bg_chip_state_list); //變換chip點擊背景顏色
                                chip.setTextAppearance(R.style.chipTextColor); //變換chip點擊文字顏色

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
                                            staffs.remove(Integer.valueOf(tag));
                                            staffs_text.remove(sname);
                                        }
                                        Log.i(TAG, "click chip: " + sname);
                                        Log.i(TAG, "staffs_text: " + staffs_text);
                                        Log.i(TAG, "staffs: " + staffs);
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

                                chip.setChipBackgroundColorResource(R.color.bg_chip_state_list); //變換chip點擊背景顏色
                                chip.setTextAppearance(R.style.chipTextColor); //變換chip點擊文字顏色

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
                                            cars.remove(Integer.valueOf(tag));
                                            cars_text.remove(cname);
                                        }
                                        Log.i(TAG, "click chip: " + cname);
                                        Log.i(TAG, "cars_text: " + cars_text);
                                        Log.i(TAG, "cars: " + cars);
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

    private void globalNav(){
        ImageButton back_btn = findViewById(R.id.back_imgBtn);
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(System_Vacation.this, System.class);
                startActivity(system_intent);
            }
        });
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(System_Vacation.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(System_Vacation.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(System_Vacation.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(System_Vacation.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(System_Vacation.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}