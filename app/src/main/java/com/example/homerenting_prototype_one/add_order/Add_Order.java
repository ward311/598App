package com.example.homerenting_prototype_one.add_order;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.System;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.setting.Setting;
import com.example.homerenting_prototype_one.valuation.Valuation;
import com.example.homerenting_prototype_one.valuation.Valuation_Booking;

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
    RadioGroup genderRG;
    Button addOrderBtn;

    String gender = "男";

    GregorianCalendar calendar = new GregorianCalendar();
    OkHttpClient okHttpClient = new OkHttpClient();

    Context context = Add_Order.this;
    String TAG = "Add_Order";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__order);
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        linking();

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

        movingTime_text.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog time_picker = new TimePickerDialog( context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        movingTime_text.setText(hourOfDay+":"+minute);
                    }
                },calendar.get(GregorianCalendar.DAY_OF_MONTH ),calendar.get(GregorianCalendar.MINUTE ),true);
                time_picker.show();
            }
        } );

        addOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        .add("accurate_fee", price)
                        .add("worktime", worktime)
                        .add("additional", notice)
                        .add("moving_date", date)
                        .build();
                Log.i(TAG,"function_name: " + function_name +
                        ", member_name: " + name +
                        ", gender: " + gender +
                        ", contact_address: " + cAddress +
                        ", phone: " + phone +
                        ", from_address: " + fromAddress+
                        ", to_address: " + toAddress +
                        ", accurate_fee: " + price +
                        ", worktime: " + worktime +
                        ", additional: " + notice +
                        ", moving_date: " + date);

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


                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.setClass(context, Order.class);
                        startActivity(intent);
                    }
                }, 1000);
            }
        });

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

    private void linking(){
        name_edit = findViewById(R.id.name_AO);
        cAddress_edit = findViewById(R.id.contactAddress_AO);
        phone_edit = findViewById(R.id.phone_AO);
        moveOut_edit = findViewById(R.id.fromAddress_AO);
        moveIn_edit = findViewById(R.id.toAddress_AO);
        price_edit = findViewById(R.id.price_AO);
        worktime_edit = findViewById(R.id.worktime_AO);
        notice_edit = findViewById(R.id.notice_AO);
        movingDate_text = findViewById(R.id.pickDate_AO);
        movingTime_text = findViewById(R.id.pictTime_AO);
        genderRG = findViewById(R.id.genderRG_AO);
        addOrderBtn = findViewById(R.id.addOrder_btn_AO);
    }
}