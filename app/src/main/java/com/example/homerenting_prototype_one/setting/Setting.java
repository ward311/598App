package com.example.homerenting_prototype_one.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.helper.DatabaseHelper;
import com.example.homerenting_prototype_one.helper.SessionManager;
import com.example.homerenting_prototype_one.main.Login;
import com.example.homerenting_prototype_one.model.TableContract;
import com.example.homerenting_prototype_one.system.System;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.valuation.Valuation;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
    TextView company_email, user_auth;

    Context context = this;
    Button sign_out;
    String TAG = "Setting";
    String imageUrl;
    private SharedPreferences sp ;
    SQLiteDatabase db;
    DatabaseHelper dbHelper;
    ImageView companyImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_setting);
        LinearLayout company_information = findViewById(R.id.companyInfo_LL_S);
        LinearLayout service_item = findViewById(R.id.serviceItem_LL_S);
        LinearLayout discount = findViewById(R.id.discount_LL_S);
        LinearLayout customer_evaluation = findViewById(R.id.customerEvaluation_LL_S);
        LinearLayout system_announcement = findViewById(R.id.announce_LL_S);
        LinearLayout history_record = findViewById(R.id.btn_logout);
        LinearLayout add_account = findViewById(R.id.btn_add_acct);
        companyImage = findViewById(R.id.companyImage);
        dbHelper = new DatabaseHelper(this);
        TableContract.ServiceClassTable.getServiceClass(dbHelper);
        TableContract.ServiceItemTable.getServiceItem(dbHelper, context);

        if(sp.getString("title", null).contains("staff")){
            company_information.setEnabled(false);
            company_information.setBackgroundColor(Color.parseColor("#f1f1f1"));
            company_information.setAlpha(.3F);

            service_item.setEnabled(false);
            service_item.setBackgroundColor(Color.parseColor("#f2f2f2"));
            service_item.setAlpha(.3F);
            discount.setEnabled(false);
            discount.setBackgroundColor(Color.parseColor("#f2f2f2"));
            discount.setAlpha(.3F);
            customer_evaluation.setEnabled(false);
            customer_evaluation.setBackgroundColor(Color.parseColor("#f2f2f2"));
            customer_evaluation.setAlpha(.3F);
            system_announcement.setEnabled(false);
            system_announcement.setBackgroundColor(Color.parseColor("#f2f2f2"));
            system_announcement.setAlpha(.3F);
            history_record.setEnabled(false);
            history_record.setBackgroundColor(Color.parseColor("#f2f2f2"));
            history_record.setAlpha(.3F);
            add_account.setEnabled(false);
            add_account.setBackgroundColor(Color.parseColor("#f2f2f2"));
            add_account.setAlpha(.3F);

        }else{
            company_information.setEnabled(true);
            service_item.setEnabled(true);
            discount.setEnabled(true);
            customer_evaluation.setEnabled(true);
            system_announcement.setEnabled(true);
            history_record.setEnabled(true);
            add_account.setEnabled(true);
        }
        company_email = findViewById(R.id.company_email_S);
        user_auth = findViewById(R.id.authorization);

        if(sp.getString("title",null).equals("admin")){
            user_auth.setText("當前權限：管理者");
        }else{
            user_auth.setText("當前權限：員工");
        }

        sign_out = findViewById(R.id.signout_btn);

        getCompanyDetail();
        new Handler().postDelayed(() -> new ImageLoadTask(imageUrl, companyImage).execute(), 500);

        company_information.setOnClickListener(v -> {
            Intent information_intent = new Intent(Setting.this, Setting_Information.class);
            startActivity(information_intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        service_item.setOnClickListener(v -> {
            Intent service_intent = new Intent(Setting.this, Setting_Service.class);
            startActivity(service_intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        discount.setOnClickListener(v -> {
            Intent discount_intent = new Intent(Setting.this, Setting_Discount.class);
            startActivity(discount_intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        customer_evaluation.setOnClickListener(v -> {
            Intent evaluation_intent = new Intent(Setting.this, Setting_Evaluation.class);
            startActivity(evaluation_intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        system_announcement.setOnClickListener(v -> {
            Intent announcement_intent = new Intent(Setting.this, Setting_Announcement.class);
            startActivity(announcement_intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        history_record.setOnClickListener(v -> {
            Intent record_intent = new Intent(Setting.this, Setting_Record.class);
            startActivity(record_intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        add_account.setOnClickListener(v -> {
            Intent account_intent = new Intent(Setting.this, Setting_Account.class);
            startActivity(account_intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        sign_out.setOnClickListener(view -> {
            SessionManager session = SessionManager.getInstance(context);
            session.logout();
            Intent loginPage_intent = new Intent(Setting.this, Login.class);

            loginPage_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(loginPage_intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            sp = getSharedPreferences("login", Context.MODE_PRIVATE);
            sp.edit().putBoolean("logged", false).apply();
            sp.edit().clear();
            sp.edit().commit();
            Toast.makeText(context, "已登出", Toast.LENGTH_LONG).show();
            finish();

        });

        globalNav();
    }
    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }

    }

    private void getCompanyDetail() {
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
                runOnUiThread(() -> {
                    //在app畫面上呈現錯誤訊息
                    Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
                Log.i(TAG, "responseData: " + responseData); //顯示資料

                try {
                    JSONArray responseArr = new JSONArray(responseData);
                    JSONObject company = responseArr.getJSONObject(0);
                    String email = company.getString("email");
                    imageUrl = "https://598new.ddns.net/598_new_20211026/"+company.getString("img");
                    Log.d(TAG, imageUrl);
                    runOnUiThread(() -> company_email.setText(email));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void getCommentData() {
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
                Log.i(TAG, "responseData: " + responseData); //顯示資料

                try {
                    JSONArray responseArr = new JSONArray(responseData);
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject comment = responseArr.getJSONObject(i);
                        Log.i(TAG, "comment: " + comment);
                        String comment_id = comment.getString("comment_id");
                        String name = comment.getString("member_name");
                        String nameTitle;
                        if (comment.getString("gender").equals("女")) nameTitle = "小姐";
                        else nameTitle = "先生";
                        String date = comment.getString("comment_date");
                        String commentStr = comment.getString("comment");
                        String commentSummary;
                        if (commentStr.length() > 35)
                            commentSummary = commentStr.substring(0, 30) + "...";
                        else commentSummary = commentStr;

                        double service_star = comment.getDouble("service_quality");
                        double work_star = comment.getDouble("work_attitude");
                        double price_star = comment.getDouble("price_grade");
                        double starf = (service_star + work_star + price_star) / 3;
                        starf = (double) Math.round(starf * 10) / 10;
                        Log.d(TAG, "service(" + service_star + ")+work(" + work_star + ")+price(" + price_star + ")/3 = " + starf);

                        String[] row_data = {comment_id, String.valueOf(starf), name, nameTitle, date, commentSummary};
                        Log.d(TAG, "row_data: " + Arrays.toString(row_data));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void globalNav() {
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        //底下nav
        valuation_btn.setOnClickListener(v -> {
            Intent valuation_intent = new Intent(Setting.this, Valuation.class);
            valuation_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(valuation_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
        order_btn.setOnClickListener(v -> {
            Intent order_intent = new Intent(Setting.this, Order.class);
            order_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(order_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
        calendar_btn.setOnClickListener(v -> {
            Intent calender_intent = new Intent(Setting.this, Calendar.class);
            calender_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(calender_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
        system_btn.setOnClickListener(v -> {
            Intent system_intent = new Intent(Setting.this, System.class);
            system_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(system_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
        /*setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Setting.this, Setting.class);
                setting_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(setting_intent);
            }
        });*/
    }
    public void onBackPressed(){
        Intent toCalendar = new Intent(Setting.this, Calendar.class);
        toCalendar.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toCalendar);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
