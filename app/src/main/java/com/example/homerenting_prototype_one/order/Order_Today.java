package com.example.homerenting_prototype_one.order;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.adapter.re_adpater.NoDataRecyclerAdapter;
import com.example.homerenting_prototype_one.adapter.re_adpater.SwipeDeleteAdapter;
import com.example.homerenting_prototype_one.helper.RecyclerViewAction;
import com.example.homerenting_prototype_one.setting.Setting;
import com.example.homerenting_prototype_one.system.System;
import com.example.homerenting_prototype_one.adapter.base_adapter.ListAdapter;
import com.example.homerenting_prototype_one.adapter.base_adapter.NoDataAdapter;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.valuation.Valuation;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.homerenting_prototype_one.show.global_function.changeStat;
import static com.example.homerenting_prototype_one.show.global_function.changeStatus;
import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;
import static com.example.homerenting_prototype_one.show.global_function.getDate;
import static com.example.homerenting_prototype_one.show.global_function.getDay;
import static com.example.homerenting_prototype_one.show.global_function.getMonth;
import static com.example.homerenting_prototype_one.show.global_function.getMonthStr;
import static com.example.homerenting_prototype_one.show.global_function.getTime;
import static com.example.homerenting_prototype_one.show.global_function.getToday;
import static com.example.homerenting_prototype_one.show.global_function.getYear;
import static com.example.homerenting_prototype_one.show.global_function.removeNew;


public class Order_Today extends AppCompatActivity {
    ArrayList<String[]> data;

    TextView week_text;
    TextView month_text;
    RecyclerView orderRList;

    String TAG = "Order_Today";
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__today);
        week_text = findViewById(R.id.week_OT);
        month_text = findViewById(R.id.month_OT);
        orderRList = findViewById(R.id.order_recyclerView_OT);

        Button order = findViewById(R.id.order_btn);
        Button booking_order = findViewById(R.id.bookingOrder_btn);
        Button today_order = findViewById(R.id.todayOrder_btn);
        Button cancel_order = findViewById(R.id.cancelOrder_btn);
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        data = new ArrayList<>();

        week_text.setText(getToday());
        month_text.setText(getMonthStr());

        //????????????????????????function_name
        String function_name = "order_member_today";
        String company_id = getCompany_id(this);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", company_id)
                .build();

        //????????????
        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/user_data.php")
                .post(body)
                .build();

        //??????
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            //????????????
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //??????????????????
                //???app???????????????????????????
                runOnUiThread(() -> Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show());
            }

            //????????????
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG, "responseData: "+responseData); //????????????

                try {
                    //?????????json?????????array???object
                    final JSONArray responseArr = new JSONArray(responseData);
//                    Log.d(TAG,"responseObj: "+ responseArr);

                    //??????????????????JSONArray??????json??????
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject member = responseArr.getJSONObject(i);
                        Log.d(TAG,"member:"+member);

                        //???????????????
                        String order_id = member.getString("order_id");
                        String datetime = member.getString("moving_date");
                        String name = member.getString("member_name");
                        String nameTitle;
                        if(member.getString("gender").equals("???")) nameTitle = "??????";
                        else nameTitle = "??????";
                        final String phone = member.getString("phone");
                        String contact_address = member.getString("contact_address");
                        if(contact_address.equals("null")) contact_address = "";
                        String auto = member.getString("auto");
                        String newicon = member.getString("new");
                        String status = member.getString("order_status");
                        String plan = member.getString("plan");
                        if(status.equals("done")) status = "done_today";

                        //???????????????
                        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Taipei"));
                        Log.d(TAG, "now: "+now.getYear()+"-"+monthToInt(String.valueOf(now.getMonth()))+"-"+now.getDayOfMonth());
                        Log.d(TAG, "order: "+Integer.parseInt(getYear(datetime))+"-"+Integer.parseInt(getMonth(datetime))+"-"+Integer.parseInt(getDay(datetime)));
                        if(Integer.parseInt(getYear(datetime))<now.getYear() ||
                                (Integer.parseInt(getYear(datetime))<=now.getYear() &&
                                        Integer.parseInt(getMonth(datetime))<monthToInt(String.valueOf(now.getMonth()))) ||
                                (Integer.parseInt(getYear(datetime))<=now.getYear() &&
                                        Integer.parseInt(getMonth(datetime))<=monthToInt(String.valueOf(now.getMonth())) &&
                                        Integer.parseInt(getDay(datetime))<now.getDayOfMonth())) {
                            Log.d(TAG, "moving_date "+datetime+" of order_id "+order_id+" is over time");
                            changeStat(order_id, "orders", "cancel", context);
                            continue;
                        }

                        //?????????????????????
                        String[] row_data = {order_id, getDate(datetime), getTime(datetime), name, nameTitle, phone, contact_address, auto, newicon, status, plan};
                        data.add(row_data);
                    }
                } catch (JSONException e) { //??????????????????????????????json??????????????????????????????json??????
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        if(responseData.equals("null")){
                            Log.d(TAG, "NO DATA");
                            NoDataRecyclerAdapter noDataAdapter = new NoDataRecyclerAdapter();
                            orderRList.setLayoutManager(new LinearLayoutManager(context));
                            orderRList.setAdapter(noDataAdapter);
                        }
                        //else Toast.makeText(Order_Booking.this, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show();
                    });
                }
                //????????????
                if(!responseData.equals("null")){
                    for(int i=0; i < data.size(); i++)
                        Log.i(TAG, "data: "+ Arrays.toString(data.get(i)));
                    setRList();
                }
            }
        });



        //??????nav
        order.setOnClickListener(v -> {
            Intent order_intent = new Intent(Order_Today.this, Order.class);
            startActivity(order_intent);
        });
        booking_order.setOnClickListener(v -> {
            Intent bookingOrder_intent = new Intent(Order_Today.this, Order_Booking.class);
            startActivity(bookingOrder_intent);
        });
//        today_order.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent todayOrder_intent = new Intent(Order_Today.this, Order_Today.class);
//                startActivity(todayOrder_intent);
//            }
//        });
        cancel_order.setOnClickListener(v -> {
            Intent cancelOrder_intent = new Intent(Order_Today.this, Order_Cancel.class);
            startActivity(cancelOrder_intent);
        });

        //??????nav
        valuation_btn.setOnClickListener(v -> {
            Intent valuation_intent = new Intent(Order_Today.this, Valuation.class);
            startActivity(valuation_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
//        order_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent order_intent = new Intent(Order.this, Order.class);
//                startActivity(order_intent);
//            }
//        });
        calendar_btn.setOnClickListener(v -> {
            Intent calender_intent = new Intent(Order_Today.this, Calendar.class);
            startActivity(calender_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
        system_btn.setOnClickListener(v -> {
            Intent system_intent = new Intent(Order_Today.this, System.class);
            startActivity(system_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
        setting_btn.setOnClickListener(v -> {
            Intent setting_intent = new Intent(Order_Today.this, Setting.class);
            startActivity(setting_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
    }
    private void setRList(){
        final SwipeDeleteAdapter adapter = new SwipeDeleteAdapter(this, data, Today_Detail.class);
        runOnUiThread(() -> {
            orderRList.setLayoutManager(new LinearLayoutManager(context));
            orderRList.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL)); //?????????
            orderRList.setAdapter(adapter);

            //????????????
//                ItemTouchHelper helper = new ItemTouchHelper(new RecyclerViewAction(context, adapter));
//                helper.attachToRecyclerView(orderRList);
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
    public void onBackPressed(){
        Intent toOrder = new Intent(Order_Today.this, Order.class);
        toOrder.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(toOrder);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}
