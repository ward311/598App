package com.example.homerenting_prototype_one.calendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.Setting;
import com.example.homerenting_prototype_one.System;
import com.example.homerenting_prototype_one.adapter.New_CalendarAdapter;
import com.example.homerenting_prototype_one.adapter.NoDataAdapter;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.order.Order_Detail;
import com.example.homerenting_prototype_one.valuation.Valuation;
import com.example.homerenting_prototype_one.valuation.Valuation_Detail;

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

import static com.example.homerenting_prototype_one.show.global_function.clearDatalist;
import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;
import static com.example.homerenting_prototype_one.show.global_function.getStartTime;
import static com.example.homerenting_prototype_one.show.global_function.getTime;
import static com.example.homerenting_prototype_one.show.global_function.removeNew;

public class Calendar extends AppCompatActivity {
    CalendarView calendar;
    ImageButton valuation_btn, order_btn, calendar_btn, system_btn, setting_btn;
    TextView valuation_bar, order_bar, dateText;
    ListView orderList;
    Button cancel_btn;

    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View v;
    New_CalendarAdapter calendarAdapter, calendarAdapter_v, calendarAdapter_o;
    ArrayList<String[]> data, data_v, data_o;

    Boolean VB, OB;

    Context context;
    OkHttpClient okHttpClient = new OkHttpClient();
    String TAG = "Calendar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        context = Calendar.this;
        data = new ArrayList<>();
        data_v = new ArrayList<>();
        data_o = new ArrayList<>();
        initData();
        VB = true;
        OB = true;

        linking();

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String monthStr = String.valueOf(month + 1);
                if (month + 1 < 10) monthStr = "0" + monthStr;
                String dayOfMonthStr = String.valueOf(dayOfMonth);
                if (dayOfMonth < 10) dayOfMonthStr = "0" + dayOfMonthStr;
                String date = year + "-" + monthStr + "-" + dayOfMonthStr;
                Log.i(TAG, "Date Change: " + date);

                dialog = new AlertDialog.Builder(context);
                inflater = getLayoutInflater();
                v = inflater.inflate(R.layout.calendar_dialog, null);
                v.setMinimumHeight(2000);
                dialog.setView(v);
                linking2();
                dateText.setText(date);
                getOrder(date);

                valuation_bar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (VB) {
                            VB = false;
                            valuation_bar.setBackgroundColor(Color.rgb(195, 184, 173));
                            valuation_bar.setTextColor(Color.rgb(140, 132, 132));
                        } else {
                            VB = true;
                            valuation_bar.setBackgroundColor(Color.rgb(251, 133, 39));
                            valuation_bar.setTextColor(Color.rgb(255, 255, 255));
                        }
                        setList();
                    }
                });

                order_bar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (OB) {
                            OB = false;
                            order_bar.setBackgroundColor(Color.rgb(195, 184, 173));
                            order_bar.setTextColor(Color.rgb(140, 132, 132));
                        } else {
                            OB = true;
                            order_bar.setBackgroundColor(Color.rgb(25, 176, 237));
                            order_bar.setTextColor(Color.rgb(255, 255, 255));
                        }
                        setList();
                    }
                });

                final AlertDialog alertDialog = dialog.create();
                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                alertDialog.getWindow().setLayout(1400, 2000);
            }
        });

        //底下nav
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(context, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(context, Order.class);
                startActivity(order_intent);
            }
        });
//        calendar_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent calender_intent = new Intent(context, Calendar.class);
//                startActivity(calender_intent);
//            }
//        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(context, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(context, Setting.class);
                startActivity(setting_intent);
            }
        });
    }

    private void linking() {
        calendar = findViewById(R.id.main_calendar);
        valuation_btn = findViewById(R.id.valuationBlue_Btn);
        order_btn = findViewById(R.id.order_imgBtn);
        calendar_btn = findViewById(R.id.calendar_imgBtn);
        system_btn = findViewById(R.id.system_imgBtn);
        setting_btn = findViewById(R.id.setting_imgBtn);
    }

    private void linking2() {
        valuation_bar = v.findViewById(R.id.valuation_bar_C);
        order_bar = v.findViewById(R.id.order_bar_C);
        dateText = v.findViewById(R.id.date_C);
        orderList = v.findViewById(R.id.orderList_C);
        cancel_btn = v.findViewById(R.id.cancelBtn_C);
    }

    private void initData(){
        data.clear();
        data_v.clear();
        data_o.clear();
        String[] row_data = {"編號", "時間", "地址", "0", "0"};
        data.add(row_data);
        data_v.add(row_data);
        data_o.add(row_data);
    }

    private void getOrder(String date) {
        initData();
        String function_name = "order_member_oneDay";
        String company_id = getCompany_id(this);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", company_id)
                .add("date", date)
                .build();
        Log.i(TAG, "function_name: " + function_name + ", date: " + date);

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL + "/user_data.php")
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            //連線失敗
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //顯示錯誤訊息
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            //連線成功
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d("responseData", responseData); //顯示資料

                try {
                    //轉換成json格式，array或object
                    final JSONArray responseArr = new JSONArray(responseData);
                    //Log.d(TAG,"responseObj: "+ responseArr);

                    //一筆一筆的取JSONArray中的json資料
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject order = responseArr.getJSONObject(i);
                        Log.d(TAG, "order:" + order);
                        String order_id = order.getString("order_id");
                        String time = order.getString("moving_date");
                        if (time.equals("null"))
                            time = getStartTime(order.getString("valuation_time"));
                        else time = getTime(time);
                        String to_address = order.getString("to_address");
                        String status = order.getString("order_status");
                        String newicon = order.getString("new");

                        String[] row_data = {order_id, time, to_address, status, newicon};
                        data.add(row_data);
                        if (status.equals("evaluating")) data_v.add(row_data);
                        else data_o.add(row_data);
                    }
                } catch (JSONException e) { //會到這裡通常表示用錯json格式或網頁的資料不是json格式
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (responseData.equals("null")) {
                                NoDataAdapter noData = new NoDataAdapter();
                                orderList.setAdapter(noData);
                                //Toast.makeText(context, "no data", Toast.LENGTH_LONG).show();
                            } else
                                Toast.makeText(context, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                //顯示資訊
                if (data.size() > 1) {
                    for (int i = 0; i < data.size(); i++)
                        Log.i(TAG, "data: " + Arrays.toString(data.get(i)));
                    calendarAdapter = new New_CalendarAdapter(data);
                    calendarAdapter_v = new New_CalendarAdapter(data_v);
                    calendarAdapter_o = new New_CalendarAdapter(data_o);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            orderList.setAdapter(calendarAdapter);
                            orderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String[] row_data = (String[]) parent.getItemAtPosition(position);
                                    Log.i(TAG, "row_data: " + Arrays.toString(row_data));
                                    String order_id = row_data[0];

                                    Bundle bundle = new Bundle();
                                    bundle.putString("order_id", order_id);

                                    String newicon = row_data[row_data.length - 1];
                                    if (newicon.equals("1"))
                                        removeNew(order_id, context);

                                    Intent intent = new Intent();
                                    String status = row_data[3];
                                    Log.d(TAG, "status(" + order_id + "): " + status);
                                    if (status.equals("evaluating"))
                                        intent.setClass(context, Valuation_Detail.class);
                                    else intent.setClass(context, Order_Detail.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                } else Log.i(TAG, "data: no data");
            }
        });
    }

    private void setList() {
        orderList.setAdapter(null);
        if (VB && OB) orderList.setAdapter(calendarAdapter);
        else if (VB) orderList.setAdapter(calendarAdapter_v);
        else if (OB) orderList.setAdapter(calendarAdapter_o);
    }
}