package com.example.homerenting_prototype_one.furniture;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.adapter.FurnitureAdapter;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.setting.Setting;
import com.example.homerenting_prototype_one.System;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.valuation.Valuation;

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

import static com.example.homerenting_prototype_one.show.global_function.dip2px;
import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;


public class Edit_Furniture extends AppCompatActivity {
    String TAG = "Edit_Furniture";
    private ListView list;
    private ArrayList<String[]> data;

    FurnitureAdapter adapter;

    Context context = Edit_Furniture.this;
    OkHttpClient okHttpClient = new OkHttpClient();
    private final String PHP = "/furniture.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__furniture);
        final Button check_btn = findViewById(R.id.check_furniture_btn);
        final Button add_btn = findViewById(R.id.add_furniture_btn);

        final ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        final ImageButton order_btn = findViewById(R.id.order_imgBtn);
        final ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        final ImageButton system_btn = findViewById(R.id.system_imgBtn);
        final ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        data = new ArrayList<>();
        list = findViewById(R.id.furniture_listView);


        final Bundle bundle = getIntent().getExtras();
        final String order_id = bundle.getString("order_id");

        String function_name = "furniture_detail";
        String company_id = getCompany_id(this);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .add("company_id", company_id)
                .build();
        Log.d(TAG, "order_id: "+order_id);

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
                Log.d(TAG, "Failed: " + e.getMessage()); //顯示錯誤訊息
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //在app畫面上呈現錯誤訊息
                        Toast.makeText(Edit_Furniture.this, "Toast onFailure.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG,"responseData: "+responseData);

                try {
                    JSONArray responseArr = new JSONArray(responseData);

                    //取得資料
                    for(int i = 0 ; i < responseArr.length() ; i++) {
                        JSONObject furniture = responseArr.getJSONObject(i);
                        String furniture_id = furniture.getString("furniture_id");
                        String name = furniture.getString("furniture_name");
                        String num = furniture.getString("num");

                        String[] row_data = {furniture_id, name, num};
                        Log.d(TAG, "row_data: "+Arrays.toString(row_data));
                        data.add(row_data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(Edit_Furniture.this, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                Log.d(TAG, "data.size(): "+data.size());
                for(int i=0; i < data.size(); i++)
                    Log.i(TAG, "data: "+ Arrays.toString(data.get(i)));
                adapter = new FurnitureAdapter(data);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        list.setAdapter(adapter);
                        check_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //把資料放進ArrayList
                                ArrayList<int[]> fd = new ArrayList<>();
                                for(int i = 0 ; i < adapter.getCount() ; i++){
                                    String[] row_data = (String[])adapter.getItem(i);
                                    int[] row_data2 = {Integer.parseInt(row_data[0]), Integer.parseInt(row_data[2])};
                                    fd.add(row_data2);
                                }

                                //把ArrayList的資料放進二微陣列
                                int[][] furniture_data = new int[fd.size()][2];
                                for(int i = 0; i < fd.size(); i++)
                                    for(int ii = 0; ii < 2; ii++)
                                        furniture_data[i][ii] = fd.get(i)[ii];

                                String function_name = "modify_furniture";
                                String company_id = getCompany_id(context);
                                RequestBody body = new FormBody.Builder()
                                        .add("function_name", function_name)
                                        .add("order_id", order_id)
                                        .add("company_id",company_id)
                                        .add("furniture_data", Arrays.deepToString(furniture_data))
                                        .build();
                                Log.d(TAG,"order_id: "+order_id+", furniture_data:"+ Arrays.deepToString(furniture_data));

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
                                                Toast.makeText(Edit_Furniture.this, "Toast onFailure.", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                        final String responseData = response.body().string();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(Edit_Furniture.this, "修改家具完成", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        Log.d(TAG, "responseData: " + responseData);
                                    }
                                });

                                finish();
                            }
                        });
                    }
                });
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context thisActivity = Edit_Furniture.this;
                FrameLayout container = new FrameLayout(thisActivity);
                FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = dip2px(thisActivity, 20);
                params.rightMargin = dip2px(thisActivity, 20);
                final EditText f_name_edit = new EditText(Edit_Furniture.this);
                f_name_edit.setLayoutParams(params);
                container.addView(f_name_edit);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("家具名稱");
                builder.setMessage("請輸入家具名稱");
                builder.setView(container);
                builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String f_name = f_name_edit.getText().toString();
                        Log.d(TAG, "f_name: "+f_name);
                        String[] row_data = {"-1", f_name, "0"};
                        Log.d(TAG, "row_data: "+Arrays.toString(row_data));
                        data.add(row_data);
                        final FurnitureAdapter adapter = new FurnitureAdapter(data);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                list.setAdapter(adapter);
                            }
                        });
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Edit_Furniture.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Edit_Furniture.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Edit_Furniture.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Edit_Furniture.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Edit_Furniture.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}