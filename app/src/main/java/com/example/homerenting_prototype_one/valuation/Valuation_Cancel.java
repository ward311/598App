package com.example.homerenting_prototype_one.valuation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.Calendar;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.Setting;
import com.example.homerenting_prototype_one.System;
import com.example.homerenting_prototype_one.adapter.ListAdapter;
import com.example.homerenting_prototype_one.adapter.NoDataAdapter;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.show.show_valuation_cancel_data;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.homerenting_prototype_one.show.global_function.getDate;
import static com.example.homerenting_prototype_one.show.global_function.getTime;
import static com.example.homerenting_prototype_one.show.global_function.removeNew;

public class Valuation_Cancel extends AppCompatActivity {
    ArrayList<String[]> data;
    ListView orderList;
    ListAdapter listAdapter;

    OkHttpClient okHttpClient = new OkHttpClient();
    String TAG = "Valuation_Cancel";
    private final String PHP = "/user_data.php";

    public ListView self_evaluation, booking_evaluation, matchMaking_evaluation, cancel_evaluation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valuation__cancel);
        orderList = findViewById(R.id.order_listView_VC);

        self_evaluation = findViewById(R.id.selfEvaluation_listView);
        booking_evaluation = findViewById(R.id.bookingEvaluation_listView);
        matchMaking_evaluation = findViewById(R.id.matchMaking_Evaluation_listView);
        cancel_evaluation = findViewById(R.id.cancelEvaluation_listView);
        Button self_btn = findViewById(R.id.selfEvaluation_btn);
        Button booking_btn = findViewById(R.id.bookingEvaluation_btn);
        Button matchMaking_btn = findViewById(R.id.matchMaking_Evaluation_btn);
        Button cancel_btn = findViewById(R.id.cancelEvaluation_btn);
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);
        LinearLayout first_layout = findViewById(R.id.first_cancel_layout);

        data = new ArrayList<>();

        //將傳至網頁的值
        String function_name = "valuation_member";
        String status = "cancel";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("status", status)
                .build();

        //連線要求
        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+PHP)
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
                        Toast.makeText(Valuation_Cancel.this, "Toast onFailure.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                //Log.d(TAG, "responseData" + responseData);

                try {
                    final JSONArray responseArr = new JSONArray(responseData);
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject member = responseArr.getJSONObject(i);
                        Log.d(TAG, "member: " + member);

                        final String order_id = member.getString("order_id");
                        final String name = member.getString("name");
                        final String nameTitle;
                        if(member.getString("gender").equals("female")) nameTitle = "小姐";
                        else nameTitle = "先生";
                        final String phone = member.getString("phone");
                        final String contact_address = member.getString("contact_address");
                        final String newicon = member.getString("new");

                        String[] row_data = {order_id, name, nameTitle, phone, contact_address, newicon};
                        data.add(row_data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(responseData.equals("null")){
                                NoDataAdapter noData = new NoDataAdapter();
                                orderList.setAdapter(noData);
                            }
                            else Toast.makeText(Valuation_Cancel.this, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                //顯示資訊
                if(!responseData.equals("null")) {
                    for (int i = 0; i < data.size(); i++)
                        Log.i(TAG, "data: " + Arrays.toString(data.get(i)));
                    listAdapter = new ListAdapter(data);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            orderList.setAdapter(listAdapter);
                            orderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String[] row_data = (String[]) parent.getItemAtPosition(position);
                                    Log.d(TAG, "row_data: " + Arrays.toString(row_data));
                                    String order_id = row_data[0];

                                    Bundle bundle = new Bundle();
                                    bundle.putString("order_id", order_id);

                                    removeNew(order_id, Valuation_Cancel.this);

                                    Intent intent = new Intent();
                                    intent.setClass(Valuation_Cancel.this, ValuationCancel_Detail.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                }
            }
        });



        first_layout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail_layout = new Intent(Valuation_Cancel.this, ValuationCancel_Detail.class);
                Bundle bundle = new Bundle();
                bundle.putString("order_id", "9");
                detail_layout.putExtras(bundle);
                startActivity( detail_layout );
            }
        } );
        self_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selfValuation_intent = new Intent(Valuation_Cancel.this, Valuation.class);
                startActivity(selfValuation_intent);
            }
        });
        booking_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookingValuation_intent = new Intent(Valuation_Cancel.this, Valuation_Booking.class);
                startActivity(bookingValuation_intent);
            }
        });
        matchMaking_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent matchMakingValuation_intent = new Intent(Valuation_Cancel.this, Valuation_MatchMaking.class);
                startActivity(matchMakingValuation_intent);
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent cancelValuation_intent = new Intent(Valuation_Cancel.this, Valuation_Cancel.class);
//                startActivity(cancelValuation_intent);

            }
        });
//        valuation_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent valuation_intent = new Intent(Valuation.this, Valuation.class);
//                startActivity(valuation_intent);
//            }
//        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Valuation_Cancel.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Valuation_Cancel.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Valuation_Cancel.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Valuation_Cancel.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
