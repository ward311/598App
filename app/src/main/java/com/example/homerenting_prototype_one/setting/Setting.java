package com.example.homerenting_prototype_one.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.helper.SessionManager;
import com.example.homerenting_prototype_one.main.Login;
import com.example.homerenting_prototype_one.system.System;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.valuation.Valuation;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;

public class Setting extends AppCompatActivity {
    TextView company_email;
    Context context = this;
    Button sign_out;
    String TAG = "Setting";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        LinearLayout company_information = findViewById(R.id.companyInfo_LL_S);
        LinearLayout service_item = findViewById(R.id.serviceItem_LL_S);
        LinearLayout discount = findViewById(R.id.discount_LL_S);
        LinearLayout customer_evaluation = findViewById(R.id.customerEvaluation_LL_S);
        LinearLayout system_announcement = findViewById(R.id.announce_LL_S);
        LinearLayout history_record = findViewById(R.id.btn_logout);

        company_email = findViewById(R.id.company_email_S);
        sign_out = findViewById(R.id.signout_btn);

        getCompanyDetail();


        company_information.setOnClickListener(v -> {
            Intent information_intent = new Intent(Setting.this, Setting_Information.class);
            startActivity(information_intent);
        });
        service_item.setOnClickListener(v -> {
            Intent service_intent = new Intent(Setting.this, Setting_Service.class);
            startActivity(service_intent);
        });
        discount.setOnClickListener(v -> {
            Intent discount_intent = new Intent(Setting.this, Setting_Discount.class);
            startActivity(discount_intent);
        });
        customer_evaluation.setOnClickListener(v -> {
            Intent evaluation_intent = new Intent(Setting.this, Setting_Evaluation.class);
            startActivity(evaluation_intent);
        });
        system_announcement.setOnClickListener(v -> {
            Intent announcement_intent = new Intent(Setting.this, Setting_Announcement.class);
            startActivity(announcement_intent);
        });
        history_record.setOnClickListener(v -> {
            Intent record_intent = new Intent(Setting.this, Setting_Record.class);
            startActivity(record_intent);
        });
        sign_out.setOnClickListener(view -> {
            SessionManager session = SessionManager.getInstance(context);
            session.logout();
            Intent loginPage_intent = new Intent(Setting.this, Login.class);
            startActivity(loginPage_intent);
            Toast.makeText(context, "已登出", Toast.LENGTH_LONG).show();
        });

        globalNav();
    }

    private void getCompanyDetail(){
        String function_name = "company_detail";
        String company_id = getCompany_id(context);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", company_id)
                .build();

        //連線要求
        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL + "/user_data.php")
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
                String responseData = response.body().string();
                Log.i(TAG,"responseData: "+responseData); //顯示資料

                try {
                    JSONArray responseArr = new JSONArray(responseData);
                    JSONObject company = responseArr.getJSONObject(0);
                    final String email = company.getString("email");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            company_email.setText(email);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getCommentData(){
        String function_name = "comment_data";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", getCompany_id(context))
                .build();

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL + "/user_data.php")
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
                String responseData = response.body().string();
                Log.i(TAG,"responseData: "+responseData); //顯示資料

                try {
                    JSONArray responseArr = new JSONArray(responseData);
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject comment = responseArr.getJSONObject(i);
                        Log.i(TAG, "comment: "+comment);
                        String comment_id = comment.getString("comment_id");
                        String name = comment.getString("member_name");
                        String nameTitle;
                        if(comment.getString("gender").equals("女")) nameTitle = "小姐";
                        else nameTitle = "先生";
                        String date = comment.getString("comment_date");
                        String commentStr = comment.getString("comment");
                        String commentSummary;
                        if(commentStr.length() > 35) commentSummary = commentStr.substring(0, 30)+"...";
                        else commentSummary = commentStr;

                        double service_star = comment.getDouble("service_quality");
                        double work_star = comment.getDouble("work_attitude");
                        double price_star = comment.getDouble("price_grade");
                        double starf = (service_star+work_star+price_star)/3;
                        starf = (double) Math.round(starf*10)/10;
                        Log.d(TAG, "service("+service_star+")+work("+work_star+")+price("+price_star+")/3 = "+starf);

                        String[] row_data = {comment_id, String.valueOf(starf), name, nameTitle, date, commentSummary};
                        Log.d(TAG, "row_data: "+Arrays.toString(row_data));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
                Intent valuation_intent = new Intent(Setting.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Setting.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Setting.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Setting.this, System.class);
                startActivity(system_intent);
            }
        });
//        setting_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent setting_intent = new Intent(Setting.this, Setting.class);
//                startActivity(setting_intent);
//            }
//        });
    }
}
