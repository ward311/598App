package com.example.homerenting_prototype_one.add_order;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
    EditText name_edit, cAddress_edit, phone_edit, moveOut_edit, moveIn_edit, price_edit, worktime_edit, notice_edit;
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
        else{
            bundle = new Bundle();
        }

        movingDate_text.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePicker = new DatePickerDialog( Add_Order.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String monthStr = String.valueOf(month+1);
                        if(month+1 < 10) monthStr = "0"+monthStr;
                        String dayStr = String.valueOf(dayOfMonth);
                        if(dayOfMonth < 10) dayStr = "0"+dayStr;
                        movingDate_text.setText(year+"-"+monthStr+"-"+dayStr);
                    }
                },calendar.get( GregorianCalendar.YEAR ),calendar.get( GregorianCalendar.MONTH ),calendar.get( GregorianCalendar.DAY_OF_MONTH));
                datePicker.show();
            }
        } );

        movingTime_text.setOnClickListener(v -> {
            TimePickerDialog time_picker = new TimePickerDialog( context, (view, hourOfDay, minute) -> movingTime_text.setText(hourOfDay+":"+minute),calendar.get(GregorianCalendar.DAY_OF_MONTH ),calendar.get(GregorianCalendar.MINUTE ),true);
            time_picker.show();
        });

        cAddress_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String address = cAddress_edit.getText().toString();
                String moveOut_address = moveOut_edit.getText().toString();
                if(!address.equals("") && !moveOut_address.equals("")){
                    if(address.substring(0, 1).equals(moveOut_address.substring(0, 1)))
                        moveOut_edit.setText(address);
                }
                else if(!address.equals("") || moveOut_address.length()<=1){
                    moveOut_edit.setText(address);
                }
            }
        });

        editFurnitureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                bundle.putString("order_id", "-1");
                getTextData();
                intent.putExtras(bundle);
                intent.setClass(context, Edit_Furniture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        addOrderBtn.setOnClickListener(v -> {
            if(bundle.getString("furniture_data") != null)
                furniture_data = bundle.getString("furniture_data");

            addOrder();

            Handler handler = new Handler();
            handler.postDelayed(() -> finish(), 1000);
        });

        globalNav();
    }

    private void getTextData(){
        bundle.putString("name", name_edit.getText().toString());
        bundle.putString("cAddress", cAddress_edit.getText().toString());
        bundle.putString("phone", phone_edit.getText().toString());
        bundle.putString("fromAddress", moveOut_edit.getText().toString());
        bundle.putString("toAddress", moveIn_edit.getText().toString());
        bundle.putString("price", price_edit.getText().toString());
        bundle.putString("worktime", worktime_edit.getText().toString());
        bundle.putString("notice", notice_edit.getText().toString());
        bundle.putString("date", movingDate_text.getText().toString());
    }

    private void setTextData(){
        name_edit.setText(bundle.getString("name"));
        cAddress_edit.setText(bundle.getString("cAddress"));
        phone_edit.setText(bundle.getString("phone"));
        moveOut_edit.setText(bundle.getString("fromAddress"));
        moveIn_edit.setText(bundle.getString("toAddress"));
        price_edit.setText(bundle.getString("price"));
        worktime_edit.setText(bundle.getString("worktime"));
        notice_edit.setText(bundle.getString("notice"));
        movingDate_text.setText(bundle.getString("date"));
    }

    private void addOrder(){
        String name = name_edit.getText().toString();
        String cAddress = cAddress_edit.getText().toString();
        String phone = phone_edit.getText().toString();
        String fromAddress = moveOut_edit.getText().toString();
        String toAddress = moveIn_edit.getText().toString();
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
                .add("contact_address", cAddress)
                .add("phone", phone)
                .add("from_address", fromAddress)
                .add("to_address", toAddress)
                .add("estimate_fee", price)
                .add("worktime", worktime)
                .add("additional", notice)
                .add("moving_date", date)
                .add("furniture_data", furniture_data)
                .build();
        Log.i(TAG,"function_name: " + function_name +
                ", member_name: " + name +
                ", gender: " + gender +
                ", contact_address: " + cAddress +
                ", phone: " + phone +
                ", from_address: " + fromAddress+
                ", to_address: " + toAddress +
                ", estimate_fee: " + price +
                ", worktime: " + worktime +
                ", additional: " + notice +
                ", moving_date: " + date +
                ", furniture_data" + furniture_data);

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
                final String responseData = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(responseData.equals("success"))
                            Toast.makeText(context, "新增訂單成功", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(context, "新增訂單失敗", Toast.LENGTH_LONG).show();
                    }
                });
                Log.d(TAG, "add_btn, responseData: " + responseData);
            }
        });
    }

    private void linking(){
        name_edit = findViewById(R.id.name_AO);
        cAddress_edit = findViewById(R.id.contactAddress_AO);
        phone_edit = findViewById(R.id.phone_AO);
        moveOut_edit = findViewById(R.id.fromAddress_AO);
        moveIn_edit = findViewById(R.id.toAddress_AO);
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
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Add_Order.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Add_Order.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Add_Order.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Add_Order.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Add_Order.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
