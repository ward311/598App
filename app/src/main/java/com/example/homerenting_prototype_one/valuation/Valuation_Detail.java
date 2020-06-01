package com.example.homerenting_prototype_one.valuation;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.Calendar;
import com.example.homerenting_prototype_one.furniture.Furniture_Detail;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.Setting;
import com.example.homerenting_prototype_one.System;
import com.example.homerenting_prototype_one.order.Order;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.GregorianCalendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Valuation_Detail extends AppCompatActivity {
    TextView nameText;
    TextView nameTitleText;
    TextView phoneText;
    TextView selfValTimeText;
    TextView fromAddressText;
    TextView toAddressText;
    TextView contactTimeText;
    TextView cusValTimeText;
    TextView noticeText;
    TextView sysValPriceText;
    TextView valPriceText;

    EditText pickDate_edit;
    EditText pickTime_edit;

    Button furniture_btn;
    Button check_date_btn;
    Button check_price_btn;

    String name;
    String gender;
    String phone;
    String selfValTime;
    String fromAddress;
    String toAddress;
    String contactTime;
    String cusValTime;
    String notice;
    String sysValPrice;
    String valPrice;

    String TAG = "Valuation_Detail";

    final OkHttpClient okHttpClient = new OkHttpClient();


    public ListView furniture_list;
    public String[] furnitures = {"1 單人沙發   2    ","2 兩人沙發   1    ","3 三人沙發   1    ","4 L型沙發   1    ",
            "5 沙發桌   3    ","6 傳統電視   1    ","7 液晶電視37吋以下   1    ","8 液晶電視40吋以上   1    ","9 電視櫃   1    ",
            "10 酒櫃   2    ","11 鞋櫃   2    ","12 按摩椅   1    ","13 佛桌   1    ","14 鋼琴   1    ",
            "15 健身器材   3    "};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valuation__detail);
        Button phoneCall_btn = findViewById(R.id.call_btn);
        //furniture_list = findViewById(R.id.furniture_listView);
        //final TextView pickDate_text = findViewById( R.id.pickDate_text );
        //final TextView pickTime_text = findViewById( R.id.pickTime_text );
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);
        final GregorianCalendar calendar = new GregorianCalendar();

        final Bundle bundle = getIntent().getExtras();
        final String order_id = bundle.getString("order_id");
        Log.d(TAG, "order_id: " + order_id);

        linking();

        //將傳至網頁的值
        String function_name = "valuation_detail";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .build();

        //連線要求
        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/user_data.php")
                .post(body)
                .build();

        //final ProgressDialog dialog = ProgressDialog.show(this,"讀取中","請稍候",true);

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //顯示錯誤訊息
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Valuation_Detail.this, "Toast onFailure.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                //Log.d(TAG,"responseData: "+responseData); //顯示資料

                try {
                    JSONArray responseObj = new JSONArray(responseData);
                    JSONObject order = responseObj.getJSONObject(0);
                    Log.d(TAG,"order:" + order);

                    name = order.getString("name");
                    gender = order.getString("gender");
                    phone = order.getString("phone");
                    selfValTime = order.getString("last_update");
                    fromAddress = order.getString("moveout_address");
                    toAddress = order.getString("movein_address");
                    contactTime = order.getString("contact_time");
                    cusValTime = order.getString("prefer_valuation_time");
                    notice = order.getString("additional");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //dialog.dismiss();
                            nameText.setText(name);
                            if(gender.equals("female")) nameTitleText.setText("小姐");
                            else if(gender.equals("male")) nameTitleText.setText("先生");
                            else nameTitleText.setText("");
                            phoneText.setText(phone);
                            selfValTimeText.setText(selfValTime);
                            fromAddressText.setText(fromAddress);
                            toAddressText.setText(toAddress);
                            contactTimeText.setText(contactTime);
                            cusValTimeText.setText(cusValTime);
                            noticeText.setText(notice);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Valuation_Detail.this, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        furniture_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail_intent = new Intent(  );
                detail_intent.setClass( Valuation_Detail.this, Furniture_Detail.class );
                bundle.putString("key","valuation");
                detail_intent.putExtras(bundle);
                startActivity( detail_intent );
            }
        } );

        pickDate_edit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog date_picker = new DatePickerDialog( Valuation_Detail.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        pickDate_edit.setText(String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(dayOfMonth));
                    }
                },calendar.get( GregorianCalendar.YEAR ),calendar.get( GregorianCalendar.MONTH ),calendar.get( GregorianCalendar.DAY_OF_MONTH));
                date_picker.show();
            }
        } );

        pickTime_edit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog time_picker = new TimePickerDialog( Valuation_Detail.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        pickTime_edit.setText(hourOfDay+":"+minute);
                    }
                },calendar.get(GregorianCalendar.DAY_OF_MONTH ),calendar.get(GregorianCalendar.MINUTE ),true);
                time_picker.show();
            }
        } );


        check_price_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Valuation_Detail.this, Valuation.class);
                startActivity(intent);
            }
        } );

        check_date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valTime = pickDate_edit.getText().toString() + " " + pickTime_edit.getText().toString();

                String function_name = "update_selfValuation";
                RequestBody body = new FormBody.Builder()
                        .add("function_name", function_name)
                        .add("order_id",order_id)
                        .add("valuation_time", valTime + ":00")
                        .build();
                Log.d(TAG,"check_price_btn: order_id: " + order_id + ", valuation_time: " + valTime + ":00");

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
                                Toast.makeText(Valuation_Detail.this, "Toast onFailure.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        final String responseData = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Valuation_Detail.this, "線上估價成功", Toast.LENGTH_LONG).show();
                            }
                        });
                        Log.d(TAG, "check_price_btn, responseData: " + responseData);
                    }
                });

                Intent intent = new Intent(Valuation_Detail.this, Valuation.class);
                startActivity(intent);
            }
        });







        ArrayAdapter furniture_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,furnitures);
        //furniture_list.setAdapter(furniture_adapter);
        phoneCall_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call_intent = new Intent(Intent.ACTION_DIAL);
                call_intent.setData(Uri.parse("tel:0933669877"));
//              call_intent.setData(Uri.parse("tel"+phone_number));
//                if (ActivityCompat.checkSelfPermission(Valuation_Detail.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
//                    return;
//                }
//                Intent call_intent = new Intent("android.intent.action.CALL",Uri.parse("tel:0933669877"));
                startActivity(call_intent);
            }
        });


        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Valuation_Detail.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Valuation_Detail.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Valuation_Detail.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Valuation_Detail.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Valuation_Detail.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }

    public void linking(){
        nameText = findViewById(R.id.name_VD);
        nameTitleText = findViewById(R.id.nameTitle_VD);
        phoneText = findViewById(R.id.phone_VD);
        selfValTimeText = findViewById(R.id.selfValTime_VD);
        fromAddressText = findViewById(R.id.FromAddress_VD);
        toAddressText = findViewById(R.id.ToAddress_VD);
        furniture_btn = findViewById(R.id.furniture_btn_VD);
        contactTimeText = findViewById(R.id.contactTime_VD);
        cusValTimeText = findViewById(R.id.cusValTime_VD);
        noticeText = findViewById(R.id.notice_VD);
        pickDate_edit = findViewById(R.id.pickDate_editText);
        pickTime_edit = findViewById(R.id.pickTime_editText);
        check_date_btn = findViewById(R.id.check_date_btn_VD);
        sysValPriceText = findViewById(R.id.sysValPrice_VD);
        valPriceText = findViewById(R.id.cusValTime_VD);
        check_price_btn = findViewById(R.id.check_price_btn_VD);
    }
}
