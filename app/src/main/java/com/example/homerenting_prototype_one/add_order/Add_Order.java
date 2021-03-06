package com.example.homerenting_prototype_one.add_order;

import android.app.AlertDialog;
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
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
    Spinner contact_citySpin, contact_districtSpin;
    Spinner in_citySpin, in_districtSpin;
    String gender = "???", furniture_data = "";
    String time;
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
        defaultInput(false);
        setSpinner();
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
                    Toast.makeText(context, "???????????????????????????", Toast.LENGTH_SHORT);
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
        time = now.getHour()+":00";
        movingTime_text.setOnClickListener(v -> {
            TimePickerDialog time_picker = new TimePickerDialog( context, (view, hourOfDay, minute) -> {
                String mm = String.valueOf(minute);
                if(minute < 10) mm = "0"+mm;
                String hh = String.valueOf(hourOfDay);
                if(hourOfDay < 10) hh = "0"+hh;
                movingTime_text.setText(hh+":"+mm);
                time = hourOfDay+":"+minute;
            },calendar.get(GregorianCalendar.DAY_OF_MONTH ),calendar.get(GregorianCalendar.MINUTE ),true);
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
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("end", "true");
                startActivity(intent);

            }, 1500);
        });

        globalNav();

    }

    public void defaultInput(boolean status){
        contact_citySpin.setEnabled(status);
        contact_districtSpin.setEnabled(status);
        cAddress.setEnabled(status);
        phone_edit.setEnabled(status);
        outCity.setEnabled(status);
        outDistrict.setEnabled(status);
        outAddress.setEnabled(status);
        in_citySpin.setEnabled(status);
        in_districtSpin.setEnabled(status);
        editFurnitureBtn.setEnabled(status);
        editFurnitureBtn.setAlpha(.5f);
        movingDate_text.setEnabled(status);
        movingTime_text.setEnabled(status);
        price_edit.setEnabled(status);
        worktime_edit.setEnabled(status);
        notice_edit.setEnabled(status);
        addOrderBtn.setEnabled(status);
        addOrderBtn.setAlpha(.5f);
        name_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(name_edit.getText().length()!=0)
                    contact_citySpin.setEnabled(!status);
                    contact_districtSpin.setEnabled(!status);
                    cAddress.setEnabled(!status);

            }
        });
       cAddress.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {

           }

           @Override
           public void afterTextChanged(Editable s) {
                if(cAddress.getText().length()!=0){
                    phone_edit.setEnabled(!status);
                }
           }
       });
        phone_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(phone_edit.getText().length()!=0){
                    outCity.setEnabled(!status);
                    outDistrict.setEnabled(!status);
                    outAddress.setEnabled(!status);
                    in_citySpin.setEnabled(!status);
                    in_districtSpin.setEnabled(!status);
                    inAddress.setEnabled(!status);
                }
            }
        });
        inAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(inAddress.getText().length()!=0){
                    movingDate_text.setEnabled(!status);
                    movingTime_text.setEnabled(!status);
                    price_edit.setEnabled(!status);
                    worktime_edit.setEnabled(!status);
                    notice_edit.setEnabled(!status);
                    editFurnitureBtn.setEnabled(!status);
                    editFurnitureBtn.setAlpha(1f);
                }
            }
        });
        worktime_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(worktime_edit.getText().length()!=0){

                        addOrderBtn.setEnabled(!status);
                        addOrderBtn.setAlpha(1f);
                }
            }
        });
    }

    public void getTextData(){
        int cCityChoice, cDisChoice, inCityChoice, inDisChoice;
        cCityChoice = contact_citySpin.getSelectedItemPosition();
        cDisChoice = contact_districtSpin.getSelectedItemPosition();
        inCityChoice = in_citySpin.getSelectedItemPosition();
        inDisChoice = in_districtSpin.getSelectedItemPosition();
        bundle.putInt("cCityChoice", cCityChoice);
        bundle.putInt("cDisChoice", cDisChoice);
        bundle.putInt("inCityChoice", inCityChoice);
        bundle.putInt("inDisChoice", inDisChoice);
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
         if(bundle.containsKey("cCityChoice")) {
             contact_citySpin.setSelection(bundle.getInt("cCityChoice"));
             contact_districtSpin.setSelection(bundle.getInt("cDisChoice"));
         }
         if(bundle.containsKey("inCityChoice")){
             in_citySpin.setSelection(bundle.getInt("inCityChoice"));
             in_districtSpin.setSelection(bundle.getInt("inDisChoice"));
         }
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
                gender = "???";
                break;
            case R.id.female_rbtn_AO:
                gender = "???";
                break;
            case R.id.other_rbtn_AO:
                gender = "??????";
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
                runOnUiThread(() -> Toast.makeText(context, "????????????", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                runOnUiThread(() -> {
                    if(responseData.contains("success"))
                        Toast.makeText(context, "??????????????????", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(context, "??????????????????", Toast.LENGTH_LONG).show();
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
            name_edit.setError("???????????????");
            check = true;
        }
        if(TextUtils.isEmpty(cCity.getText().toString())){
            cCity.setError("?????????????????????");
            check = true;
        }
        if(TextUtils.isEmpty(cDistrict.getText().toString())){
            cDistrict.setError("?????????????????????");
            check = true;
        }
        if(TextUtils.isEmpty(cAddress.getText().toString())){
            cAddress.setError("?????????????????????");
            check = true;
        }
        if(TextUtils.isEmpty(phone_edit.getText().toString())){
            phone_edit.setError("?????????????????????");
            check = true;
        }
        if(phone_edit.getText().length()<10){
            phone_edit.setError("??????????????????????????????");
            check = true;
        }
        if(TextUtils.isEmpty(outCity.getText().toString())){
            outCity.setError("?????????????????????");
            check = true;
        }
        if(TextUtils.isEmpty(outDistrict.getText().toString())){
            outDistrict.setError("?????????????????????");
            check = true;
        }
        if(TextUtils.isEmpty(outAddress.getText().toString())){
            outAddress.setError("?????????????????????");
            check = true;
        }

        if(TextUtils.isEmpty(inCity.getText().toString())){
            inCity.setError("?????????????????????");
            check = true;
        }
        if(TextUtils.isEmpty(inDistrict.getText().toString())){
            inDistrict.setError("?????????????????????");
            check = true;
        }
        if(TextUtils.isEmpty(inAddress.getText().toString())){
            inAddress.setError("?????????????????????");
            check = true;
        }

        if(move_out.equals(move_in) && !move_out.isEmpty()){
            Toast.makeText(context, "????????????????????????", Toast.LENGTH_SHORT).show();
            check = true;
        }
        if(TextUtils.isEmpty(price_edit.getText().toString())){
            price_edit.setError("?????????????????????");
            check = true;
        }
        if(TextUtils.isEmpty(worktime_edit.getText().toString())){
            worktime_edit.setError("?????????????????????");
            check = true;
        }
        if(TextUtils.isEmpty(movingDate_text.getText().toString())){
            movingDate_text.setError("???????????????");
            check = true;
        }
        if(contact_citySpin.getSelectedItem().equals("???????????????")){
            Toast.makeText(context, "?????????????????????",Toast.LENGTH_SHORT).show();
            check = true;
        }
        if(contact_districtSpin.getSelectedItem().equals("???????????????")){
            Toast.makeText(context, "?????????????????????",Toast.LENGTH_SHORT).show();
            check = true;
        }
        if(in_citySpin.getSelectedItem().equals("?????????????????????")){
            Toast.makeText(context, "?????????????????????",Toast.LENGTH_SHORT).show();
            check = true;
        }
        if(in_districtSpin.getSelectedItem().equals("?????????????????????")){
            Toast.makeText(context, "?????????????????????",Toast.LENGTH_SHORT).show();
            check = true;
        }

        return check;
    }
    private void setSpinner(){
        final String[] cities=
                {"???????????????", "?????????", "?????????", "?????????",
                "?????????", "?????????", "?????????", "?????????",
                "?????????", "?????????", "?????????", "?????????",
                "?????????", "?????????", "?????????", "?????????",
                "?????????",  "?????????", "?????????", "?????????",
                "?????????", "?????????", "?????????"};
        final String[][] districts =
                {{"?????????????????????"},{"??????????????????","?????????", "?????????", "?????????", "?????????", "?????????",
                "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????"},
                {"??????????????????","?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????"},
                {"??????????????????","?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????"},
                {"??????????????????","?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "?????????", "?????????", "?????????", "?????????"},
                {"??????????????????","??????", "??????", "?????????"},{"?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
                "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????"},
                {"??????????????????","?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "?????????", "?????????", "?????????", "?????????"},
                {"??????????????????","?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "?????????", "?????????", "?????????", "?????????", "?????????"},
                {"??????????????????","??????", "??????", "??????", "??????", "??????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "?????????", "?????????", "?????????"},
                {"??????????????????","?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "?????????"},
                {"??????????????????","?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "?????????", "?????????", "?????????", "?????????"},
                {"??????????????????","??????", "??????"},
                {"??????????????????","?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "????????????"},
                {"??????????????????","?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "?????????", "?????????", "?????????"},
                {"??????????????????","?????????", "??????", "??????", "??????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "?????????", "?????????", "?????????", "?????????"},
                {"??????????????????","?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "?????????", "?????????", "?????????", "?????????", "????????????"},
                {"??????????????????","?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "????????????"},
                {"??????????????????","?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "????????????"},
                {"??????????????????","?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
                        "?????????", "?????????", "?????????", "?????????"},
                {"??????????????????","?????????", "?????????", "?????????", "?????????", "?????????", "?????????"},
                {"??????????????????","?????????", "?????????", "?????????", "?????????", "?????????", "?????????"},
                {"??????????????????","?????????", "?????????", "?????????", "?????????"}
        };
        ArrayAdapter<String> citiesList = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item, cities);
        contact_citySpin.setAdapter(citiesList);
        contact_citySpin.setSelection(0);
        in_citySpin.setAdapter(citiesList);
        in_citySpin.setSelection(0);
        contact_citySpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = contact_citySpin.getSelectedItemPosition();

                if(pos!=0){
                    cCity.setText(contact_citySpin.getSelectedItem().toString());
                    ArrayAdapter<String> districtList = new ArrayAdapter<>(context,
                            android.R.layout.simple_spinner_dropdown_item, districts[pos]);
                    contact_districtSpin.setAdapter(districtList);
                }else{
                    ArrayAdapter<String> districtList = new ArrayAdapter<>(context,
                            android.R.layout.simple_spinner_dropdown_item, districts[0]);
                    contact_districtSpin.setAdapter(districtList);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        contact_districtSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = contact_districtSpin.getSelectedItemPosition();
                if(pos!=0){
                    cDistrict.setText(contact_districtSpin.getSelectedItem().toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        in_citySpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = in_citySpin.getSelectedItemPosition();
                if(pos!=0){
                    inCity.setText(in_citySpin.getSelectedItem().toString());
                    ArrayAdapter<String> districtList = new ArrayAdapter<>(context,
                            android.R.layout.simple_spinner_dropdown_item, districts[pos]);
                    in_districtSpin.setAdapter(districtList);
                }else{
                    ArrayAdapter<String> districtList = new ArrayAdapter<>(context,
                            android.R.layout.simple_spinner_dropdown_item, districts[0]);
                    in_districtSpin.setAdapter(districtList);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        in_districtSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = in_districtSpin.getSelectedItemPosition();
                if(pos!=0){
                    inDistrict.setText(in_districtSpin.getSelectedItem().toString());
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
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
        name_edit = findViewById(R.id.name_AO);
        cCity = findViewById(R.id.cCity);
        cDistrict = findViewById(R.id.cDistrict);
        cAddress = findViewById(R.id.cAddress);

        phone_edit = findViewById(R.id.phone_AO);

        outCity = findViewById(R.id.out_city);
        outDistrict = findViewById(R.id.out_district);
        outAddress = findViewById(R.id.out_address);

        inCity = findViewById(R.id.in_city);
        inDistrict = findViewById(R.id.in_district);
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
        contact_citySpin = findViewById(R.id.cCity_spin);
        contact_districtSpin = findViewById(R.id.cDistrcit_spin);
        in_citySpin = findViewById(R.id.inCity_spin);
        in_districtSpin = findViewById(R.id.inDistrict_spin);
    }

    private void globalNav(){
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        //??????nav
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}
