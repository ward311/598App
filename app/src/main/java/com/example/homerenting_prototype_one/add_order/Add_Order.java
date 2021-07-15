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
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.system.System;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.furniture.Edit_Furniture;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.setting.Setting;
import com.example.homerenting_prototype_one.valuation.Valuation;

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

public class Add_Order extends AppCompatActivity {
    EditText name_edit, phone_edit, price_edit, worktime_edit, notice_edit;
    EditText outCity, outDistrict, outAddress;
    EditText cCity, cDistrict, cAddress;
    EditText inCity, inDistrict, inAddress;
    TextView movingDate_text, movingTime_text;
    ListView furniture_list;
    RadioGroup genderRG;
    Button editFurnitureBtn, addOrderBtn;

    String gender = "男", furniture_data = "";

    Bundle bundle;

    GregorianCalendar calendar = new GregorianCalendar();
    OkHttpClient okHttpClient = new OkHttpClient();

    Context context = Add_Order.this;
    String TAG = "Add_Order";


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__order);

        linking();

        if(getIntent().getExtras() != null){
            bundle = getIntent().getExtras();
            Log.d(TAG, "furniture_data: "+bundle.getString("furniture_data"));
            setTextData();
        }
        else bundle = new Bundle();

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Taipei"));
//        String now_year = String.valueOf(now.getYear());
//        String now_month = String.valueOf(monthToInt(String.valueOf(now.getMonth())));
//        String now_day = String.valueOf(now.getDayOfMonth());
//        if(monthToInt(String.valueOf(now.getMonth())) < 10) now_month = "0"+now_month;
//        if(now.getDayOfMonth() < 10) now_day = "0"+now_day;
//        Log.d(TAG, "now: "+now_year+"-"+now_month+"-"+now_day);
//        movingDate_text.setText(now_year+"-"+now_month+"-"+now_day);

        movingDate_text.setOnClickListener(v -> {
            DatePickerDialog datePicker = new DatePickerDialog( Add_Order.this, (view, year, month, dayOfMonth) -> {
                if(year<now.getYear() || (month+1)<monthToInt(String.valueOf(now.getMonth())) || dayOfMonth<now.getDayOfMonth()) {
                    Toast.makeText(context, "請勿選擇過去的日期", Toast.LENGTH_SHORT);
                    return;
                }
                String monthStr = String.valueOf(month+1);
                if(month+1 < 10) monthStr = "0"+monthStr;
                String dayStr = String.valueOf(dayOfMonth);
                if(dayOfMonth < 10) dayStr = "0"+dayStr;
                movingDate_text.setText(year+"-"+monthStr+"-"+dayStr);
            },calendar.get( GregorianCalendar.YEAR ),calendar.get( GregorianCalendar.MONTH ),calendar.get( GregorianCalendar.DAY_OF_MONTH));
            datePicker.getDatePicker().setMinDate(new Date().getTime());
            datePicker.show();
        });

        movingTime_text.setText(now.getHour()+":00");
        movingTime_text.setOnClickListener(v -> {
            TimePickerDialog time_picker = new TimePickerDialog( context, (view, hourOfDay, minute) -> movingTime_text.setText(hourOfDay+":"+minute),calendar.get(GregorianCalendar.DAY_OF_MONTH ),calendar.get(GregorianCalendar.MINUTE ),true);
            time_picker.show();
        });

        cCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                outCity.setError(null);

                String c_City = cCity.getText().toString();
                String moveOut_City = outCity.getText().toString();

                outCity.setText(c_City);

            }
        });

        cDistrict.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                outDistrict.setError(null);

                String c_District = cDistrict.getText().toString();
                String moveOut_District = outDistrict.getText().toString();

                outDistrict.setText(c_District);

            }
        });

        cAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {

                outAddress.setError(null);

                String c_Address = cAddress.getText().toString();
                String moveOut_Address = outAddress.getText().toString();

                outAddress.setText(c_Address);

            }
        });


        editFurnitureBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            bundle.putString("order_id", "-1");
            getTextData();
            intent.putExtras(bundle);
            intent.setClass(context, Edit_Furniture.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            setResult(RESULT_OK, getIntent());
            startActivity(intent);
        });


        addOrderBtn.setOnClickListener(v -> {
            if(checkEmpty()) return;
            if(bundle.getString("furniture_data") != null)
                furniture_data = bundle.getString("furniture_data");

            addOrder();

            Handler handler = new Handler();
            handler.postDelayed(() -> {
                Intent intent = new Intent();
                intent.setClass(context, Calendar.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("end", "true");
                startActivity(intent);

            }, 1500);
        });

        globalNav();

    }

    public void getTextData(){
        bundle.putString("name", name_edit.getText().toString());
        bundle.putInt("gender", genderRG.getCheckedRadioButtonId());
        bundle.putString("cCity", cCity.getText().toString());
        bundle.putString("cDistrict", cDistrict.getText().toString());
        bundle.putString("cAddress", cAddress.getText().toString());
        bundle.putString("phone", phone_edit.getText().toString());
        bundle.putString("outCity", outCity.getText().toString());
        bundle.putString("outDistrict", outDistrict.getText().toString());
        bundle.putString("outAddress", outAddress.getText().toString());
        bundle.putString("inCity", inCity.getText().toString());
        bundle.putString("inDistrict", inDistrict.getText().toString());
        bundle.putString("inAddress", inAddress.getText().toString());
        bundle.putString("pickDate", movingDate_text.getText().toString());
        bundle.putString("pickTime", movingTime_text.getText().toString());
        bundle.putString("price", price_edit.getText().toString());
        bundle.putString("worktime", worktime_edit.getText().toString());
        bundle.putString("notice", notice_edit.getText().toString());
    }

     public void setTextData(){
        if(bundle.containsKey("name")) name_edit.setText(bundle.getString("name"));
        if(bundle.containsKey("gender")) genderRG.check(bundle.getInt("gender"));
        if(bundle.containsKey("cCity")) cCity.setText(bundle.getString("cCity"));
        if(bundle.containsKey("cDistrict")) cDistrict.setText(bundle.getString("cDistrict"));
        if(bundle.containsKey("cAddress")) cAddress.setText(bundle.getString("cAddress"));
        if(bundle.containsKey("phone")) phone_edit.setText(bundle.getString("phone"));
        if(bundle.containsKey("outCity")) outCity.setText(bundle.getString("outCity"));
        if(bundle.containsKey("outDistrict")) outDistrict.setText(bundle.getString("outDistrict"));
        if(bundle.containsKey("outAddress")) outAddress.setText(bundle.getString("outAddress"));
        if(bundle.containsKey("inCity")) inCity.setText(bundle.getString("inCity"));
        if(bundle.containsKey("inDistrict")) inDistrict.setText(bundle.getString("inDistrict"));
        if(bundle.containsKey("inAddress")) inAddress.setText(bundle.getString("inAddress"));
        if(bundle.containsKey("pickDate")) movingDate_text.setText(bundle.getString("pickDate"));
        if(bundle.containsKey("pickTime")) movingTime_text.setText(bundle.getString("pickTime"));
        if(bundle.containsKey("price")) price_edit.setText(bundle.getString("price"));
        if(bundle.containsKey("worktime")) worktime_edit.setText(bundle.getString("worktime"));
        if(bundle.containsKey("notice")) notice_edit.setText(bundle.getString("notice"));

    }

    private void addOrder(){
        String name = name_edit.getText().toString();

        String c_City = cCity.getText().toString();
        String c_District = cDistrict.getText().toString();
        String c_Address = cAddress.getText().toString();
        String con_cAddress = c_City + c_District + c_Address;

        String phone = phone_edit.getText().toString();

        String out_city = outCity.getText().toString();
        String out_district = outDistrict.getText().toString();
        String out_address = outAddress.getText().toString();
        String move_out = out_city + out_district + out_address;

        String in_city = inCity.getText().toString();
        String in_district = inDistrict.getText().toString();
        String in_address = inAddress.getText().toString();
        String move_in = in_city + in_district + in_address;

        String price = price_edit.getText().toString();
        String worktime = worktime_edit.getText().toString();
        String notice = notice_edit.getText().toString();
        String date = movingDate_text.getText().toString();
        date = date+" "+movingTime_text.getText().toString()+":00";

        switch(genderRG.getCheckedRadioButtonId()){
            case R.id.male_rbtn_AO:
                gender = "男";
                break;
            case R.id.female_rbtn_AO:
                gender = "女";
                break;
            case R.id.other_rbtn_AO:
                gender = "其他";
                break;
        }
        Log.i(TAG, "gender = "+gender);

        String function_name = "add_order";
        String company_id = getCompany_id(context);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", company_id)
                .add("member_name", name)
                .add("gender", gender)
                .add("contact_address", con_cAddress)
                .add("phone", phone)
                .add("additional", notice)
                .add("outcity", out_city)
                .add("outdistrict", out_district)
                .add("address1", out_address)
                .add("incity", in_city)
                .add("indistrict", in_district)
                .add("address2", in_address)
                .add("moving_date", date)
                .add("estimate_fee", price)
                .add("worktime", worktime)
                .add("furniture_data", furniture_data)
                .build();
        Log.i(TAG,"function_name: " + function_name +
                ", member_name: " + name +
                ", gender: " + gender +
                ", contact_address: " + con_cAddress +
                ", phone: " + phone +
                ", outcity: " + out_city+
                ", outdistrict: " + out_district+
                ", address1: " + out_address+
                ", incity: " + in_city +
                ", indistrict: " + in_district +
                ", address2: " + in_address +
                ", estimate_fee: " + price +
                ", worktime: " + worktime +
                ", additional: " + notice +
                ", moving_date: " + date +
                ", furniture_data: " + furniture_data);

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/functional.php")
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(context, "連線失敗", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                runOnUiThread(() -> {
                    if(responseData.contains("success"))
                        Toast.makeText(context, "新增訂單成功", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(context, "新增訂單失敗", Toast.LENGTH_LONG).show();
                });
                Log.d(TAG, "add_btn, responseData: " + responseData);
            }
        });
    }

    private boolean checkEmpty(){
        boolean check = false;

        String out_city = outCity.getText().toString();
        String out_district = outDistrict.getText().toString();
        String out_address = outAddress.getText().toString();
        String move_out = out_city + out_district + out_address;

        String in_city = inCity.getText().toString();
        String in_district = inDistrict.getText().toString();
        String in_address = inAddress.getText().toString();
        String move_in = in_city + in_district + in_address;

        if(TextUtils.isEmpty(name_edit.getText().toString())){
            name_edit.setError("請輸入姓名");
            check = true;
        }
        if(TextUtils.isEmpty(cCity.getText().toString())){
            cCity.setError("請輸入聯絡地址");
            check = true;
        }
        if(TextUtils.isEmpty(cDistrict.getText().toString())){
            cDistrict.setError("請輸入聯絡地址");
            check = true;
        }
        if(TextUtils.isEmpty(cAddress.getText().toString())){
            cAddress.setError("請輸入聯絡地址");
            check = true;
        }
        if(TextUtils.isEmpty(phone_edit.getText().toString())){
            phone_edit.setError("請輸入連絡電話");
            check = true;
        }
        if(phone_edit.getText().length()<10){
            phone_edit.setError("請輸入正確的電話號碼");
            check = true;
        }
        if(TextUtils.isEmpty(outCity.getText().toString())){
            outCity.setError("請輸入搬出地址");
            check = true;
        }
        if(TextUtils.isEmpty(outDistrict.getText().toString())){
            outDistrict.setError("請輸入搬出地址");
            check = true;
        }
        if(TextUtils.isEmpty(outAddress.getText().toString())){
            outAddress.setError("請輸入搬出地址");
            check = true;
        }

        if(TextUtils.isEmpty(inCity.getText().toString())){
            inCity.setError("請輸入搬入地址");
            check = true;
        }
        if(TextUtils.isEmpty(inDistrict.getText().toString())){
            inDistrict.setError("請輸入搬入地址");
            check = true;
        }
        if(TextUtils.isEmpty(inAddress.getText().toString())){
            inAddress.setError("請輸入搬入地址");
            check = true;
        }


        if(move_out.equals(move_in) && !move_out.isEmpty()){
            Toast.makeText(context, "搬入搬出地址相同", Toast.LENGTH_SHORT);
            check = true;
        }
        if(TextUtils.isEmpty(price_edit.getText().toString())){
            price_edit.setError("請輸入搬家費用");
            check = true;
        }
        if(TextUtils.isEmpty(worktime_edit.getText().toString())){
            worktime_edit.setError("請輸入預計工時");
            check = true;
        }
        if(TextUtils.isEmpty(movingDate_text.getText().toString())){
            movingDate_text.setError("請輸入日期");
            check = true;
        }

        return check;
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
        name_edit = findViewById(R.id.name_AO);
        cCity = findViewById(R.id.cCity);
        cDistrict = findViewById(R.id.cDistrict);
        cAddress = findViewById(R.id.cAddress);

        phone_edit = findViewById(R.id.phone_AO);

        outCity = findViewById(R.id.out_city);
        outDistrict = findViewById(R.id.out_district);
        outAddress = findViewById(R.id.out_address);

        inCity = findViewById(R.id.in_city);
        inDistrict = findViewById(R.id.in_district);;
        inAddress = findViewById(R.id.in_address);

        editFurnitureBtn = findViewById(R.id.edit_furniture_btn_AO);
        furniture_list = findViewById(R.id.furniture_list_AO);
        price_edit = findViewById(R.id.price_AO);
        worktime_edit = findViewById(R.id.worktime_AO);
        notice_edit = findViewById(R.id.notice_AO);
        movingDate_text = findViewById(R.id.pickDate_AO);
        movingTime_text = findViewById(R.id.pictTime_AO);
        genderRG = findViewById(R.id.genderRG_AO);
        addOrderBtn = findViewById(R.id.addOrder_btn_AO);
    }

    private void globalNav(){
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        //底下nav
        valuation_btn.setOnClickListener(v -> {
            Intent valuation_intent = new Intent(Add_Order.this, Valuation.class);
            startActivity(valuation_intent);
        });
        order_btn.setOnClickListener(v -> {
            Intent order_intent = new Intent(Add_Order.this, Order.class);
            startActivity(order_intent);
        });
        calendar_btn.setOnClickListener(v -> {
            Intent calender_intent = new Intent(Add_Order.this, Calendar.class);
            startActivity(calender_intent);
        });
        system_btn.setOnClickListener(v -> {
            Intent system_intent = new Intent(Add_Order.this, System.class);
            startActivity(system_intent);
        });
        setting_btn.setOnClickListener(v -> {
            Intent setting_intent = new Intent(Add_Order.this, Setting.class);
            startActivity(setting_intent);
        });
    }
}
