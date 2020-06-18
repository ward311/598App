package com.example.homerenting_prototype_one.main;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.helper.SessionManager;
import com.example.homerenting_prototype_one.order.Order;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

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

public class New_Login extends AppCompatActivity {
    ChipGroup companyList_Group;

    Bundle bundle;

    OkHttpClient okHttpClient = new OkHttpClient();
    String TAG = "New_Login";
    Context context;

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newlogin);
        companyList_Group = findViewById(R.id.companyList_NL);

        context = New_Login.this;
        bundle = new Bundle();
        session = SessionManager.getInstance(this);

        RequestBody body = new FormBody.Builder()
                .add("function_name", "all_company_data")
                .build();

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/user_data.php")
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
                        Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.i(TAG, "responeseData: "+responseData);

                try {
                    final JSONArray responseArr = new JSONArray(responseData);
                    int i;
                    for (i = 0; i < responseArr.length(); i++) {
                        JSONObject company = responseArr.getJSONObject(i);
                        Log.i(TAG, "company: " + company);

                        final String company_id = company.getString("company_id");
                        final String company_name = company.getString("company_name");
                        runOnUiThread(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void run() {
                                final Chip chip = new Chip(companyList_Group.getContext());
                                chip.setId(Integer.parseInt(company_id));
                                chip.setTag(Integer.parseInt(company_id));
                                chip.setText(company_name);
                                chip.setCheckable(true);
                                chip.setCheckedIconVisible(false);
                                chip.setTextSize(18);
                                ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(companyList_Group.getContext(), null, 0 ,R.style.Widget_MaterialComponents_Chip_Action);
                                chip.setChipDrawable(chipDrawable);
                                chip.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        JSONObject company = new JSONObject();
                                        try {
                                            company.put("company_id", company_id);
                                            company.put("company_name", company_name);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(context, "login failed no value", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                            return;
                                        }
                                        Log.i(TAG, "company: "+ company);
                                        session.createLoginSession(company_id, String.valueOf(company));
                                        startActivity(new Intent(context, Order.class));
                                    }
                                });
                                companyList_Group.addView(chip);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "login failed", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}
