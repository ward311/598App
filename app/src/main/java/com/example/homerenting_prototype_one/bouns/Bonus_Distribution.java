package com.example.homerenting_prototype_one.bouns;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.adapter.base_adapter.ListAdapter;
import com.example.homerenting_prototype_one.adapter.base_adapter.NoDataAdapter;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.setting.Setting;
import com.example.homerenting_prototype_one.system.System;
import com.example.homerenting_prototype_one.valuation.Valuation;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.homerenting_prototype_one.show.global_function.clearDatalist;
import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;
import static com.example.homerenting_prototype_one.show.global_function.getDate;
import static com.example.homerenting_prototype_one.show.global_function.getTime;
import static com.example.homerenting_prototype_one.show.global_function.removeNew;

public class Bonus_Distribution extends AppCompatActivity {
    ListView orderList;
    ImageButton backBtn;

    ArrayList<String[]> data = new ArrayList<>();
    ListAdapter listAdapter;
    String plan;
    OkHttpClient okHttpClient = new OkHttpClient();
    private Context context = this;
    private String TAG = "Bonus_Distribution";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus__distribution);

        backBtn = findViewById(R.id.back_imgBtn);
        orderList = findViewById(R.id.orderList_BD);

        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);


        new Handler().postDelayed((Runnable) () -> getOrder(),500) ;

        backBtn.setOnClickListener(v -> {
            super.onBackPressed();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        //??????nav
        valuation_btn.setOnClickListener(v -> {
            Intent valuation_intent = new Intent(Bonus_Distribution.this, Valuation.class);
            startActivity(valuation_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
        order_btn.setOnClickListener(v -> {
            Intent order_intent = new Intent(Bonus_Distribution.this, Order.class);
            startActivity(order_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
        calendar_btn.setOnClickListener(v -> {
            Intent calender_intent = new Intent(Bonus_Distribution.this, Calendar.class);
            startActivity(calender_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
        system_btn.setOnClickListener(v -> {
            Intent system_intent = new Intent(Bonus_Distribution.this, System.class);
            startActivity(system_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
        setting_btn.setOnClickListener(v -> {
            Intent setting_intent = new Intent(Bonus_Distribution.this, Setting.class);
            startActivity(setting_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
    }

    private void getOrder(){
        clearDatalist();
        //??????????????????
        String function_name = "order_member";
        String company_id = getCompany_id(context);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", company_id)
                .add("status", "done")
                .build();
        Log.i(TAG, "getOrder.  "+"company_id: "+company_id+", status:"+"done");

        //????????????
        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL + "/user_data.php")
                .post(body)
                .build();
        //http://54.166.177.4/user_data.php

        //??????
        okHttpClient.newBuilder().connectTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3,TimeUnit.MINUTES)
                .readTimeout(3,TimeUnit.MINUTES);
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            //????????????
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //??????????????????
                //???app???????????????????????????
                runOnUiThread(() -> Toast.makeText(context, "????????????", Toast.LENGTH_LONG).show());
            }

            //????????????
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.i(TAG,"responseData: "+responseData); //????????????

                try {
                    //?????????json?????????array???object
                    final JSONArray responseArr = new JSONArray(responseData);
                    //Log.i(TAG,"responseObj: "+ responseArr);

                    //??????????????????JSONArray??????json??????
                    for (int i = 0; i < responseArr.length()/2; i++) {// dunno why the data duplicates by double...
                        JSONObject member = responseArr.getJSONObject(i);
                        Log.i(TAG,"member: "+member);

                        //???????????????
                        String order_id = member.getString("order_id");
                        String datetime = member.getString("moving_date");
                        String name = member.getString("member_name");
                        String nameTitle;
                        if(member.getString("gender").equals("???")) nameTitle = "??????";
                        else nameTitle = "??????";
                        String phone = member.getString("phone");
                        String contact_address = member.getString("contact_address");
                        if(contact_address.equals("null")) contact_address = "";
                        String auto = member.getString("auto");
                        String newicon = member.getString("new");
                        plan = member.getString("plan");
                        //????????????????????????
                        String[] row_data = {order_id, getDate(datetime), getTime(datetime), name, nameTitle, phone, contact_address, auto, plan, newicon};
                        data.add(row_data);
                    }
                } catch (JSONException e) { //??????????????????????????????json??????????????????????????????json??????
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        if(responseData.equals("null")){
                            Log.d(TAG, "NO DATA");
                            NoDataAdapter noData = new NoDataAdapter();
                            orderList.setAdapter(noData);
                        }
                        //else Toast.makeText(Order.this, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show();
                    });
                }

                //????????????
                if(!responseData.equals("null")){
                    for(int i=0; i < data.size(); i++)
                        Log.i(TAG, "data: "+ Arrays.toString(data.get(i)));
                    listAdapter = new ListAdapter(data);
                    runOnUiThread(() -> {
                        orderList.setAdapter(listAdapter);
                        orderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String[] row_data = (String[])parent.getItemAtPosition(position);
                                Log.d(TAG, "row_data: "+ Arrays.toString(row_data));
                                String order_id = row_data[0];
                                String plan = row_data[row_data.length-2];
                                Log.d(TAG, "plan: "+plan);
                                Bundle bundle = new Bundle();
                                bundle.putString("order_id", order_id);
                                bundle.putString("plan", plan);
                                String newicon = row_data[row_data.length-1];
                                if(newicon.equals("1")) removeNew(order_id, context, plan);

                                Intent intent = new Intent();
                                intent.setClass(context, Distribution_Detail.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    });
                }
            }
        });
    }
}
