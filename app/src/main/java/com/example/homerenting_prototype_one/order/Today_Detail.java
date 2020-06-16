package com.example.homerenting_prototype_one.order;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.Calendar;
import com.example.homerenting_prototype_one.furniture.Furniture_Location;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.Setting;
import com.example.homerenting_prototype_one.System;
import com.example.homerenting_prototype_one.valuation.Valuation;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.homerenting_prototype_one.show.global_function.getDate;
import static com.example.homerenting_prototype_one.show.global_function.getTime;

public class Today_Detail extends AppCompatActivity {

    OkHttpClient okHttpClient = new OkHttpClient();
    
    ConstraintLayout cLayout;

    TextView nameText;
    TextView nameTitleText;
    TextView phoneText;
    TextView movingTimeText;
    TextView fromAddressText;
    TextView toAddressText;
    TextView remainderText;
    TextView carText;
    TextView staffText;
    TextView worktimeText;
    TextView feeText;
    TextView toPriceText;
    TextView finalPriceText;
    TextView priceUnitText;

    EditText changePriceText;
    EditText memoEdit;

    String name;
    String gender;
    String phone;
    String contact_address;
    String movingTime;
    String fromAddress;
    String toAddress;
    String remainder;
    String car;
    String staff;
    String worktime;
    String fee;
    String memo;

    Button changePriceBtn;
    Button check_btn;

    boolean change;

    int price_origin;
    int price;

    String TAG = "Today_Detail";
    String PHP = "/user_data.php";
    String PHP2 = "/functional.php";

    public ListView furniture_list;
    public String[] furnitures = {"1 單人沙發   2    ","2 兩人沙發   1    ","3 三人沙發   1    ","4 L型沙發   1    ",
            "5 沙發桌   3    ","6 傳統電視   1    ","7 液晶電視37吋以下   1    ","8 液晶電視40吋以上   1    ","9 電視櫃   1    ",
            "10 酒櫃   2    ","11 鞋櫃   2    ","12 按摩椅   1    ","13 佛桌   1    ","14 鋼琴   1    ",
            "15 健身器材   3    "};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today__detail);
        Button call_btn = findViewById(R.id.call_btn);
        Button detail_btn = findViewById(R.id.furniture_btn);

        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        change = false;

        Bundle bundle = getIntent().getExtras();
        final String order_id = bundle.getString("order_id");

        linking(); //將xml裡的元件連至此java
        changePriceMark(); //讓+-按鈕可以按
        changePrice();

        //傳值
        String function_name = "order_detail";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .build();
        Log.d(TAG, "order_id:"+order_id);

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+PHP)
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
                        Toast.makeText(Today_Detail.this, "Toast onFailure.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG,"responseData: "+responseData); //顯示資料

                try {
                    JSONArray responseArr = new JSONArray(responseData);
                    JSONObject order = responseArr.getJSONObject(0);
                    Log.i(TAG,"order:"+order);

                    //取得資料
                    name = order.getString("member_name");
                    gender = order.getString("gender");
                    phone = order.getString("phone");
                    contact_address = order.getString("contact_address");
                    movingTime = getDate(order.getString("moving_date"))+" "+getTime(order.getString("moving_date"));
                    remainder = order.getString("additional");
                    worktime = order.getString("estimate_worktime")+"小時";
                    fee = order.getString("fee");
                    price_origin = Integer.parseInt(fee);
                    memo = order.getString("memo");

                    int i;
                    for(i = 1; i < 3; i++){
                        JSONObject address = responseArr.getJSONObject(i);
                        if(address.getString("from_or_to").equals("from"))
                            fromAddress = address.getString("address");
                        else toAddress = address.getString("address");
                    }

                    car = "";
                    for (i = 3; i < responseArr.length(); i++) {
                        JSONObject vehicle_assign = responseArr.getJSONObject(i);
                        if(!vehicle_assign.has("vehicle_type")) break;
                        Log.i(TAG, "vehicle:" + vehicle_assign);
                        car = car+
                                vehicle_assign.getString("num")+"輛"
                                +vehicle_assign.getString("vehicle_weight")+"噸"
                                +vehicle_assign.getString("vehicle_type");
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
                            if(gender.equals("female")) nameTitleText.setText("小姐");
                            else if(gender.equals("male")) nameTitleText.setText("先生");
                            else nameTitleText.setText("");
                            phoneText.setText(phone);
                            movingTimeText.setText(movingTime);
                            fromAddressText.setText(fromAddress);
                            toAddressText.setText(toAddress);
                            remainderText.setText(remainder);
                            carText.setText(car);
                            staffText.setText(staff);
                            worktimeText.setText(worktime);
                            feeText.setText(fee);
                            finalPriceText.setText(fee);
                            memoEdit.setText(memo);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Today_Detail.this, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fee = finalPriceText.getText().toString();
                memo = memoEdit.getText().toString();
                Log.d(TAG,"check_price_btn, fee: "+fee+", memo: "+memo);

                String function_name = "update_todayOrder";
                RequestBody body = new FormBody.Builder()
                        .add("function_name", function_name)
                        .add("order_id", order_id)
                        .add("fee", fee)
                        .add("memo", memo)
                        .build();

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
                                Toast.makeText(Today_Detail.this, "Toast onFailure.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        final String responseData = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Today_Detail.this, "收款成功", Toast.LENGTH_LONG).show();
                            }
                        });
                        Log.d(TAG, "responseData: " + responseData);
                    }
                });


                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Today_Detail.this,Order_Today.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }, 1000);
            }
        });






        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call_intent = new Intent(Intent.ACTION_DIAL);
                call_intent.setData(Uri.parse("tel:"+phone));
                startActivity(call_intent);
            }
        });
       detail_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail_intent = new Intent();
                detail_intent.setClass(Today_Detail.this, Furniture_Location.class);
                Bundle today_bundle = new Bundle();
                today_bundle.putString( "key","today" );
                detail_intent.putExtras( today_bundle );
                startActivity(detail_intent);
            }
        } );

        //收款按鈕
//        check_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new AlertDialog.Builder(Today_Detail.this)
//                        .setTitle("收款")
//                        .setMessage("確認家具完好無缺後，向您\n收搬家金額"+String.valueOf( price )+"元。\n通知已寄給您的信箱，完成後請您給我們意見回饋，感謝好評。")
//                        .setPositiveButton( "確認", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                check_btn.setText("完成當日工單");
//                                check_btn.setOnClickListener( new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Intent next_intent = new Intent(Today_Detail.this, Order_Today.class);
//                                        startActivity( next_intent );
//                                    }
//                                } );
//                                new AlertDialog.Builder(Today_Detail.this)
//                                        .setTitle("確認工單後顧客簽名")
//                                        .setView(check_edit)
//                                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                check_btn.setText("收款");
//                                                check_btn.setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {
//                                                        new AlertDialog.Builder( Today_Detail.this)
//                                                                .setTitle("收款")
//                                                                .setMessage("確認家具完好無缺後，向您\n收搬家金額6000元。\n通知已寄給您的信箱，完成後請您給我們意見回饋，感謝好評。")
//                                                                .setPositiveButton( "確認", new DialogInterface.OnClickListener() {
//                                                                    @Override
//                                                                    public void onClick(DialogInterface dialog, int which) {
//                                                                        new AlertDialog.Builder(Today_Detail.this)
//                                                                                .setTitle("待確認款項後顧客簽名")
//                                                                                .setView(custoner_check_edit)
//                                                                                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
//                                                                                    @Override
//                                                                                    public void onClick(DialogInterface dialog, int which) {
//                                                                                        check_btn.setText("完成當日工單");
//                                                                                        check_btn.setOnClickListener(new View.OnClickListener() {
//                                                                                            @Override
//                                                                                            public void onClick(View v) {
//                                                                                                Intent back_order_intent = new Intent(Today_Detail.this, Order_Today.class);
//                                                                                                startActivity(back_order_intent);
//                                                                                            }
//                                                                                        });
//                                                                                    }
//                                                                                }).setNegativeButton("取消",null).create()
//                                                                                .show();
//                                                                    }
//                                                                } ).create().show();
//                                                    }
//                                                });
//                                            }
//                                        }).setNegativeButton("取消",null).create()
//                                        .show();
//                            }
//                        } )
//                        .setNegativeButton("取消",null).create()
//                        .show();
//            }
//        });

        //底下nav
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Today_Detail.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Today_Detail.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Today_Detail.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Today_Detail.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Today_Detail.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }

    private void changePriceMark(){
        changePriceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(changePriceBtn.getText().toString().equals("+"))
                    changePriceBtn.setText("-");
                else changePriceBtn.setText("+");

                String changeprice = changePriceText.getText().toString();
                if(!changeprice.isEmpty()){
                    if(changePriceBtn.getText().toString().equals("+"))
                        price = price_origin+Integer.parseInt(changeprice);
                    else
                        price = price_origin-Integer.parseInt(changeprice);
                    finalPriceText.setText(String.valueOf(price));
                }
            }
        });
    }

    private void changePrice(){
        changePriceText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String changeprice = changePriceText.getText().toString();
                if(!changeprice.isEmpty()){
                    if(changePriceBtn.getText().toString().equals("+"))
                        price = price_origin+Integer.parseInt(changeprice);
                    else
                        price = price_origin-Integer.parseInt(changeprice);
                    finalPriceText.setText(String.valueOf(price));
                    if(!change){
                        change = true;
                        setPriceUnitPlace();
                        toPriceText.setVisibility(View.VISIBLE);
                        finalPriceText.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    change = false;
                    setPriceUnitPlace();
                    toPriceText.setVisibility(View.INVISIBLE);
                    finalPriceText.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void setPriceUnitPlace(){
        ConstraintSet s = new ConstraintSet();
        s.clone(cLayout);
        if(change)
            s.connect(priceUnitText.getId(), ConstraintSet.START, finalPriceText.getId(), ConstraintSet.END, 5);
        else
            s.connect(priceUnitText.getId(), ConstraintSet.START, feeText.getId(), ConstraintSet.END, 5);
        s.applyTo(cLayout);
    }

    public void linking(){
        cLayout = findViewById(R.id.CLayout_OTD);
        nameText = findViewById(R.id.name_OTD);
        nameTitleText = findViewById(R.id.nameTitle_OTD);
        phoneText = findViewById(R.id.phone_OTD);
        movingTimeText = findViewById(R.id.movingTime_OTD);
        fromAddressText = findViewById(R.id.FromAddress_OTD);
        toAddressText = findViewById(R.id.ToAddress_OTD);
        remainderText = findViewById(R.id.notice_OTD);
        carText = findViewById(R.id.car_OTD);
        staffText = findViewById(R.id.staff_OTD);
        worktimeText = findViewById(R.id.worktime_OTD);
        feeText = findViewById(R.id.price_OTD);
        toPriceText = findViewById(R.id.toPrice_OTD);
        finalPriceText = findViewById(R.id.finalPrice_OTD);
        priceUnitText = findViewById(R.id.priceUnitText_OTD);
        changePriceBtn = findViewById(R.id.changePrice_btn_OTD);
        changePriceText = findViewById(R.id.changePrice_OTD);
        memoEdit = findViewById(R.id.PS_OTD);
        check_btn = findViewById(R.id.check_btn_OTD);
    }
}
