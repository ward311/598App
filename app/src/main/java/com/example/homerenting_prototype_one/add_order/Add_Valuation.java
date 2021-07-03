package com.example.homerenting_prototype_one.add_order;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.system.System;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.setting.Setting;
import com.example.homerenting_prototype_one.valuation.Valuation;
import com.example.homerenting_prototype_one.valuation.Valuation_Booking;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;

public class Add_Valuation extends AppCompatActivity {

    EditText nameText,  phoneText, noticeText;
    EditText cCityText, cDistrictText, cAddressText;
    EditText outCityText, outDistrictText, outAddressText;
    EditText inCityText, inDistrictText, inAddressText;
    TextView dateText, timeText;
    RadioGroup genderRG;
    Button addBtn;

    String gender = "男";
    String time, time2;

    GregorianCalendar calendar = new GregorianCalendar();

    OkHttpClient okHttpClient = new OkHttpClient();

    Context context = Add_Valuation.this;
    String TAG = "Add_Valuation";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__valuation);

        linking();

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Taipei"));
        Log.d(TAG, "now: "+now.getYear()+"-"+monthToInt(String.valueOf(now.getMonth()))+"-"+now.getDayOfMonth());

        String now_month = String.valueOf(monthToInt(String.valueOf(now.getMonth())));
        if(monthToInt(String.valueOf(now.getMonth())) < 10) now_month = "0"+now_month;
        dateText.setText(now.getYear()+"-"+now_month+"-"+now.getDayOfMonth());
        dateText.setOnClickListener(v -> {
            DatePickerDialog datePicker;
            datePicker = new DatePickerDialog( Add_Valuation.this, (view, year, month, dayOfMonth) -> {
                if(year<now.getYear() ||
                        (year >= now.getYear() && (month+1)<monthToInt(String.valueOf(now.getMonth()))) ||
                        (year >= now.getYear() && (month+1)>=monthToInt(String.valueOf(now.getMonth())) && dayOfMonth<now.getDayOfMonth())
                    ) {
                    Toast.makeText(context, "請勿選擇過去的日期", Toast.LENGTH_SHORT).show();
                    return;
                }
                String monthStr = String.valueOf(month+1);
                if(month+1 < 10) monthStr = "0"+monthStr;
                String dayStr = String.valueOf(dayOfMonth);
                if(dayOfMonth < 10) dayStr = "0"+dayStr;
                dateText.setText(year + "-" + monthStr + "-" + dayStr);
            },calendar.get( GregorianCalendar.YEAR ),calendar.get( GregorianCalendar.MONTH ),calendar.get( GregorianCalendar.DAY_OF_MONTH));
            datePicker.getDatePicker().setMinDate(new Date().getTime());
            datePicker.show();
        });

        timeText.setText(now.getHour()+":00");
        time = now.getHour()+":00";
        timeText.setOnClickListener(v -> {
            TimePickerDialog time_picker = new TimePickerDialog( context, (view, hourOfDay, minute) -> {
                String mm = String.valueOf(minute);
                if(minute < 10) mm = "0"+mm;
                String hh = String.valueOf(hourOfDay);
                if(hourOfDay < 10) hh = "0"+hh;
                timeText.setText(hh+":"+mm);
                time = hourOfDay+":"+minute;
                time2 = (hourOfDay+1)+":"+minute;
            },calendar.get(GregorianCalendar.DAY_OF_MONTH ),calendar.get(GregorianCalendar.MINUTE ),true);
            time_picker.show();
        });
        cCityText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String cCity = cCityText.getText().toString();
                outCityText.setText(cCity);
            }
        });
        cDistrictText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String cDistrict = cDistrictText.getText().toString();
                outDistrictText.setText(cDistrict);
            }
        });
        cAddressText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {

                String cAddress = cAddressText.getText().toString();
                outAddressText.setText(cAddress);

            }
        });

        addBtn.setOnClickListener(v -> {
            if(checkEmpty()) return;

            add_valuation();

            Handler handler = new Handler();
            handler.postDelayed(() -> {
                Intent finish_valuation_intent = new Intent();
                finish_valuation_intent.setClass(context, Valuation_Booking.class);
                startActivity(finish_valuation_intent);
            }, 1000);
        });

        globleBtn();
    }

    private boolean checkEmpty(){
        boolean check = false;
        if(TextUtils.isEmpty(nameText.getText().toString())){
            nameText.setError("請輸入姓名");
            check = true;
        }
        if(TextUtils.isEmpty(phoneText.getText().toString())){
            phoneText.setError("請輸入電話");
            check = true;
        }
        if(TextUtils.isEmpty(cCityText.getText().toString())){
            cCityText.setError("請輸入搬出地址");
            check = true;
        }
        if(TextUtils.isEmpty(cDistrictText.getText().toString())){
            cDistrictText.setError("請輸入搬入地址");
            check = true;
        }
        if(TextUtils.isEmpty(cAddressText.getText().toString())){
            cAddressText.setError("請輸入搬入地址");
            check = true;
        }
        if(TextUtils.isEmpty(outCityText.getText().toString())){
            outCityText.setError("請輸入搬入地址");
            check = true;
        }
        if(TextUtils.isEmpty(outDistrictText.getText().toString())){
            outDistrictText.setError("請輸入搬入地址");
            check = true;
        }
        if(TextUtils.isEmpty(outAddressText.getText().toString())){
            outAddressText.setError("請輸入搬入地址");
            check = true;
        }
        if(TextUtils.isEmpty(inCityText.getText().toString())){
            inCityText.setError("請輸入搬入地址");
            check = true;
        }
        if(TextUtils.isEmpty(inDistrictText.getText().toString())){
            inDistrictText.setError("請輸入搬入地址");
            check = true;
        }
        if(TextUtils.isEmpty(inAddressText.getText().toString())){
            inAddressText.setError("請輸入搬入地址");
            check = true;
        }

        if(TextUtils.isEmpty(dateText.getText().toString())){
            dateText.setError("請選擇日期");
            check = true;
        }
        if(time == null || time.equals("")){
            timeText.setError("請選擇時間");
            check = true;
        }

        return check;
    }

    private void add_valuation(){
        String name = nameText.getText().toString();
        String cCity = cCityText.getText().toString();
        String cDistrict = cDistrictText.getText().toString();
        String cAddress = cAddressText.getText().toString();
        String commSite = cCity + cDistrict + cAddress;
        String phone = phoneText.getText().toString();
        String outCity = outCityText.getText().toString();
        String outDistrict = outDistrictText.getText().toString();
        String outAddress = outAddressText.getText().toString();
        String outSite = outCity + outDistrict + outAddress;
        String inCity = inCityText.getText().toString();
        String inDistrict = inDistrictText.getText().toString();
        String inAddress = inAddressText.getText().toString();
        String inCitySite = inCity + inDistrict + inAddress;

        String notice = noticeText.getText().toString();
        String date = dateText.getText().toString();
        String valuation_time = time+"~"+time2;

        switch(genderRG.getCheckedRadioButtonId()){
            case R.id.male_rbtn_AV:
                gender = "男";
                break;
            case R.id.female_rbtn_AV:
                gender = "女";
                break;
            case R.id.other_rbtn_AV:
                gender = "其他";
                break;
        }
        Log.i(TAG, "gender = "+gender);

        String function_name = "add_valuation";
        String company_id = getCompany_id(context);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", company_id)
                .add("member_name", name)
                .add("gender", gender)
                .add("contact_address", commSite)
                .add("phone", phone)
                .add("outcity", outCity)
                .add("outdistrict", outDistrict)
                .add("address1", outAddress)
                .add("incity", inCity)
                .add("indistrict", inDistrict)
                .add("address2", inCity)
                .add("additional", notice)
                .add("valuation_date", date)
                .add("valuation_time", valuation_time)
                .build();
        Log.i(TAG,"function_name: " + function_name +
                ", member_name: " + name +
                ", gender: " + gender +
                ", contact_address: " + commSite +
                ", phone: " + phone +
                ", cCity: " + cCity+
                ", cDistrict: " + cDistrict +
                ", cAddress: " + cAddress +
                ", inCity: " + inCity +
                ", inDistrict: " + inDistrict +
                ", inAddress: " + inAddress +
                ", additional: " + notice +
                ", valuation_date: " + date +
                ", valuation_time: " + valuation_time);

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/functional.php")
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                runOnUiThread(() -> {
                    if(responseData.equals("success"))
                        Toast.makeText(context, "新增估價單成功", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(context, "新增估價單失敗", Toast.LENGTH_LONG).show();
                });
                Log.d(TAG, "add_btn, responseData: " + responseData);
            }
        });
    }

    private void globleBtn(){
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        //底下nav
        valuation_btn.setOnClickListener(v -> {
            Intent valuation_intent = new Intent(Add_Valuation.this, Valuation.class);
            startActivity(valuation_intent);
        });
        order_btn.setOnClickListener(v -> {
            Intent order_intent = new Intent(Add_Valuation.this, Order.class);
            startActivity(order_intent);
        });
        calendar_btn.setOnClickListener(v -> {
            Intent calender_intent = new Intent(Add_Valuation.this, Calendar.class);
            startActivity(calender_intent);
        });
        system_btn.setOnClickListener(v -> {
            Intent system_intent = new Intent(Add_Valuation.this, System.class);
            startActivity(system_intent);
        });
        setting_btn.setOnClickListener(v -> {
            Intent setting_intent = new Intent(Add_Valuation.this, Setting.class);
            startActivity(setting_intent);
        });
    }

    private int monthToInt(String month){
        switch (month){
            case "JANUARY":
                return 1;
            case "FEBRUARY":
                return 2;
            case "MARCH":
                return 3;
            case "APRIL":
                return 4;
            case "MAY":
                return 5;
            case "JUNE":
                return 6;
            case "JULY":
                return 7;
            case "AUGUST":
                return 8;
            case "SEPTEMBER":
                return 9;
            case "OCTOBER":
                return 10;
            case "NOVEMBER":
                return 11;
            case "DECEMBER":
                return 12;
            default:
                return 0;
        }
    }

    private void linking(){
        nameText = findViewById(R.id.name_AV);
        cAddressText = findViewById(R.id.c_city_edit);
        phoneText = findViewById(R.id.phone_AV);
        cCityText = findViewById(R.id.c_city_edit);
        cDistrictText = findViewById(R.id.c_district_edit);
        cAddressText = findViewById(R.id.c_address_edit);
        outCityText = findViewById(R.id.out_city_edit);
        outDistrictText = findViewById(R.id.out_district_edit);
        outAddressText = findViewById(R.id.out_address_edit);
        inCityText = findViewById(R.id.in_city_edit);
        inDistrictText = findViewById(R.id.in_district_edit);
        inAddressText = findViewById(R.id.in_address_edit);
        noticeText = findViewById(R.id.notice_editText_AV);
        dateText = findViewById(R.id.pickDate_AV);
        timeText = findViewById(R.id.pictTime_AV);
        genderRG = findViewById(R.id.genderRG_AV);
        addBtn = findViewById(R.id.add_valuation_btn_AV);
    }
}
