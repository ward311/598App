package com.example.homerenting_prototype_one.calendar;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;

import com.applandeo.materialcalendarview.EventDay;
import com.example.homerenting_prototype_one.add_order.Add_Order;
import com.example.homerenting_prototype_one.add_order.Add_Valuation;
import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.helper.DatabaseHelper;
import com.example.homerenting_prototype_one.model.TableContract;
import com.example.homerenting_prototype_one.setting.Setting;
import com.example.homerenting_prototype_one.system.System;
import com.example.homerenting_prototype_one.adapter.base_adapter.New_CalendarAdapter;
import com.example.homerenting_prototype_one.adapter.base_adapter.NoDataAdapter;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.order.Order_Detail;
import com.example.homerenting_prototype_one.valuation.MatchMaking_Detail;
import com.example.homerenting_prototype_one.valuation.Valuation;
import com.example.homerenting_prototype_one.valuation.ValuationBooking_Detail;
import com.example.homerenting_prototype_one.valuation.ValuationCancel_Detail;
import com.example.homerenting_prototype_one.valuation.Valuation_Detail;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.PendingIntent.getActivity;
import static com.example.homerenting_prototype_one.show.global_function.addDatalist;
import static com.example.homerenting_prototype_one.show.global_function.changeStatus;
import static com.example.homerenting_prototype_one.show.global_function.clearDatalist;
import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;
import static com.example.homerenting_prototype_one.show.global_function.getDay;
import static com.example.homerenting_prototype_one.show.global_function.getMonth;
import static com.example.homerenting_prototype_one.show.global_function.getStartTime;
import static com.example.homerenting_prototype_one.show.global_function.getTime;
import static com.example.homerenting_prototype_one.show.global_function.getYear;
import static com.example.homerenting_prototype_one.show.global_function.removeNew;

public class Calendar extends AppCompatActivity {
    CalendarView calendar;
    com.applandeo.materialcalendarview.CalendarView mCalendar;
    ImageButton valuation_btn, order_btn, calendar_btn, system_btn, setting_btn;
    TextView valuation_bar, order_bar, dateText;
    ListView orderList;
    Button addV_btn, addO_btn, cancel_btn;

    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View v;
    New_CalendarAdapter calendarAdapter, calendarAdapter_v, calendarAdapter_o;
    ArrayList<String[]> data, data_v, data_o, choose_data;
    ArrayList<Integer> checkedMonth = new ArrayList<>();


    private static DatabaseHelper dbHelper;
    private static SQLiteDatabase db;

    Boolean VB, OB;
    Boolean isOvertime = true;

    Context context = Calendar.this;
    String TAG = "Calendar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        init();

        onBackPressed();

        dbHelper = new DatabaseHelper(this);
        runOnUiThread(()->setmCalendar());
        Handler handler = new Handler();
        handler.postDelayed(() -> new AsyncRetrieve().execute(), 3000);
        handler.postDelayed(() -> {
            getAllMemberData();
            getChoose();
        }, 4500);

        //getAllOrdersData();
        //getAllMemberData();
        globalNav();
    }

    private void getChoose(){
        RequestBody body = new FormBody.Builder()
                .add("company_id", getCompany_id(this))
                .build();
        Log.i(TAG, "company_id: "+getCompany_id(this));

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL + "/get_data/choose_data.php")
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            //連線失敗
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //顯示錯誤訊息
                runOnUiThread(() -> Toast.makeText(context, "連線失敗", Toast.LENGTH_LONG).show());
            }

            //連線成功
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG, "responseData of getChoose: "+responseData); //顯示資料
                if (responseData.equals("null") || responseData.equals("no result")) {
                   Log.d(TAG, "NO data of get_Choose");
                    return;
                }
                try {
                    //轉換成json格式，array或object
                    final JSONArray responseArr = new JSONArray(responseData);
                    //Log.d(TAG,"responseObj: "+ responseArr);

                    //一筆一筆的取JSONArray中的json資料
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject choose = responseArr.getJSONObject(i);
                        String order_id = choose.getString("order_id");
                        String company_id = choose.getString("company_id");
                        String prefer_valuation_date = choose.getString("prefer_valuation_date");
                        String prefer_valuation_time = choose.getString("prefer_valuation_time");
                        String valuation_date = choose.getString("valuation_date");
                        String valuation_time = choose.getString("valuation_time");
                        String valuation_status = choose.getString("valuation_status");
                        String moving_date = choose.getString("moving_date");
                        String estimate_fee = choose.getString("estimate_fee");
                        String accurate_fee = choose.getString("accurate_fee");
                        String estimate_worktime = choose.getString("estimate_worktime");
                        String confirm = choose.getString("confirm");
                        String isNew = choose.getString("new");
                        String status = choose.getString("valuation_status");

                        String[] row_data = {order_id, company_id, prefer_valuation_date,
                                prefer_valuation_time, valuation_date, valuation_time,
                                valuation_status, moving_date, estimate_fee, accurate_fee,
                                estimate_worktime, confirm, isNew, status};
                        choose_data.add(row_data);

                        int success_counter = 0, fail_counter = 0;

                            db = dbHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                        values.put(TableContract.ChooseTable.COLUMN_NAME_ORDER_ID, order_id);
                        values.put(TableContract.ChooseTable.COLUMN_NAME_COMPANY_ID, company_id);
                        values.put(TableContract.ChooseTable.COLUMN_PREFER_VALUATION_DATE, prefer_valuation_date);
                        values.put(TableContract.ChooseTable.COLUMN_PREFER_VALUATION_TIME, prefer_valuation_time);
                        values.put(TableContract.ChooseTable.COLUMN_NAME_VALUATION_DATE, valuation_date);
                        values.put(TableContract.ChooseTable.COLUMN_NAME_VALUATION_TIME, valuation_time);
                        values.put(TableContract.ChooseTable.COLUMN_NAME_MOVING_DATE, moving_date);
                        values.put(TableContract.ChooseTable.COLUMN_NAME_ESTIMATE_FEE, estimate_fee);
                        values.put(TableContract.ChooseTable.COLUMN_NAME_ACCURATE_FEE, accurate_fee);
                        values.put(TableContract.ChooseTable.COLUMN_NAME_ESTIMATE_WORKTIME, estimate_worktime);
                        values.put(TableContract.ChooseTable.COLUMN_NAME_CONFIRM, confirm);
                        values.put(TableContract.ChooseTable.COLUMN_NAME_NEW, isNew);
                        values.put(TableContract.ChooseTable.COLUMN_NAME_VALUATION_STATUS,valuation_status);
//                      Log.d(TAG, (i+1)+". "+values.toString());

                        try {
                                long newRowId = db.insertOrThrow(TableContract.ChooseTable.TABLE_NAME, null, values);
                                if (newRowId != -1) {
                                    success_counter = success_counter + 1;
                                    Log.d(TAG, "create choose successfully");
                                } else {
                                    fail_counter = fail_counter + 1;
                                    Log.d(TAG, "create choose failed");
                                }
                            } catch (SQLException e) {
                                if (e.getMessage().contains("no such table")) break;
                                if (Objects.requireNonNull(e.getMessage()).contains("PRIMARYKEY"))
                                    success_counter = success_counter + 1;
                                else {
                                    e.printStackTrace();
                                    Log.d(TAG, "insert furniture data: " + e.getMessage());
                                }
                            }
                    }
                } catch (JSONException e) { //會到這裡通常表示用錯json格式或網頁的資料不是json格式
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(context, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show());
                }

            }
        });
    }
    private void getOrder(String date) {
        initData();
        clearDatalist();
        String function_name = "order_member_oneDay";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", getCompany_id(this))
                .add("date", date)
                .build();
        Log.i(TAG, "function_name: " + function_name + ", company_id: "+getCompany_id(this)+", date: " + date);

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL + "/user_data.php")
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            //連線失敗
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //顯示錯誤訊息
                runOnUiThread(() -> Toast.makeText(context, "連線失敗", Toast.LENGTH_LONG).show());
            }

            //連線成功
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG, "responseData of getOrder: "+responseData); //顯示資料
                if (responseData.equals("null") || responseData.equals("no result")) {
                    runOnUiThread(() -> {
                        NoDataAdapter noData = new NoDataAdapter();
                        orderList.setAdapter(noData);
                    });
                    return;
                }

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
                        if (time.equals("null")) time = getStartTime(order.getString("valuation_time"));
                        else time = getTime(time);
                        if(time.equals("null")) time = "";
                        String address = "";
                        if(!order.has("from_address") || order.getString("from_address").equals("null")){
                            address = order.getString("outcity")+order.getString("outdistrict")+order.getString("address1");
                        }
                        else if(order.has("from_address"))  address = order.getString("from_address");

                        String order_status = order.getString("order_status");
                        String valuation_status = order.getString("valuation_status");
                        String newicon = order.getString("new");

                        String[] row_data = {order_id, time, address, order_status, valuation_status, newicon};
                        data.add(row_data);
                        if (order_status.equals("evaluating")) data_v.add(row_data);
                        else{
                            data_o.add(row_data);
                            addDatalist(order_id);
                        }
                    }
                } catch (JSONException e) { //會到這裡通常表示用錯json格式或網頁的資料不是json格式
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(context, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show());
                }

                //顯示資訊
                if (data.size() > 1) {
                    for (int i = 0; i < data.size(); i++)
                        Log.i(TAG, "data: " + Arrays.toString(data.get(i)));
                    calendarAdapter = new New_CalendarAdapter(data);
                    calendarAdapter_v = new New_CalendarAdapter(data_v);
                    calendarAdapter_o = new New_CalendarAdapter(data_o);
                    runOnUiThread(() -> {
                        orderList.setAdapter(calendarAdapter);
                        orderList.setOnItemClickListener((parent, view, position, id) -> {
                            String[] row_data = (String[]) parent.getItemAtPosition(position);
                            Log.i(TAG, "row_data: " + Arrays.toString(row_data));
                            String order_id = row_data[0];

                            Bundle bundle = new Bundle();
                            bundle.putString("order_id", order_id);

                            String newicon = row_data[row_data.length - 1];
                            if (newicon.equals("1"))
                                removeNew(order_id, context);

                            Intent intent = new Intent();
                            String order_status = row_data[3];
                            String valuation_status = row_data[4];
                            Log.d(TAG, "status(" + order_id + "): " + order_status);
                            if (order_status.equals("evaluating")) {
                                switch (valuation_status) {
                                    case "self":
                                        intent.setClass(context, Valuation_Detail.class);
                                        break;
                                    case "booking":
                                        intent.setClass(context, ValuationBooking_Detail.class);
                                        break;
                                    case "match":
                                        intent.setClass(context, MatchMaking_Detail.class);
                                        break;
                                    default:
                                        intent.setClass(context, ValuationCancel_Detail.class);
                                        break;
                                }
                            }
                            else intent.setClass(context, Order_Detail.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        });
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

    private void setmCalendar(){

        java.util.Calendar calendarTime = java.util.Calendar.getInstance();
        int year = calendarTime.get(java.util.Calendar.YEAR);
        int month = (calendarTime.get(java.util.Calendar.MONTH)+1);
        Log.d(TAG, "check "+year+"/"+month);

        getOrders(String.valueOf(year), String.valueOf(month));

        mCalendar.setOnPreviousPageChangeListener(() -> {
            int previous_month = calendarTime.get(java.util.Calendar.MONTH);
            calendarTime.set(java.util.Calendar.MONTH, previous_month-1);
            if(previous_month == 0) previous_month = 12;
            int previous_year = calendarTime.get(java.util.Calendar.YEAR);
            Log.i(TAG, "calendarTime: "+calendarTime.getTime().toString());
            Log.d(TAG, "page change: "+previous_year+"/"+previous_month);
            getOrders(String.valueOf(previous_year), String.valueOf(previous_month));
        });

        mCalendar.setOnForwardPageChangeListener(() -> {
            int next_month = calendarTime.get(java.util.Calendar.MONTH)+2;
            calendarTime.set(java.util.Calendar.MONTH, next_month-1);
            if(next_month == 13) next_month = 1;
            int next_year = calendarTime.get(java.util.Calendar.YEAR);
            Log.i(TAG, "calendarTime: "+calendarTime.getTime().toString());
            Log.d(TAG, "page change: "+next_year+"/"+next_month);
            getOrders(String.valueOf(next_year), String.valueOf(next_month));
        });

        mCalendar.setOnDayClickListener(eventDay -> {
            Date datetime = eventDay.getCalendar().getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = simpleDateFormat.format(datetime);

            dialog = new AlertDialog.Builder(context);
            inflater = getLayoutInflater();
            v = inflater.inflate(R.layout.calendar_dialog, null);
            dialog.setView(v);
            linking2();
            dateText.setText(date);
            getOrder(date);

            runOnUiThread(() -> {
                final AlertDialog alertDialog = dialog.create();
                if(alertDialog != null){
                    alertDialog.show();
                }
                cancel_btn.setOnClickListener(v -> alertDialog.dismiss());
            });

            valuation_bar.setOnClickListener(v -> {
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
            });

            order_bar.setOnClickListener(v -> {
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
            });

            Bundle bundle = new Bundle();
            bundle.putString("date", date);
            addV_btn.setOnClickListener(v -> {
                Intent intent = new Intent(context, Add_Valuation.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            });

            addO_btn.setOnClickListener(v -> {
                Intent intent = new Intent(context, Add_Order.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            });


//            alertDialog.getWindow().setLayout(dip2px(context, 370), dip2px(context, 600));
        });
    }


    private void getOrders(String year, String month){
        List<EventDay> events = new ArrayList<>();

        String function_name = "order_member_oneMonth";
        String company_id = getCompany_id(this);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", company_id)
                .add("year", year)
                .add("month", month)
                .build();
        Log.i(TAG, "function_name: "+function_name+", company_id: "+company_id+", year: "+year+", month: "+month);

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
                runOnUiThread(() -> Toast.makeText(context, "連線失敗", Toast.LENGTH_LONG).show());
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, "responseData of getOrders: "+responseData); //顯示資料
                if(responseData.equals("no result") || responseData.equals("null")) return;

                try {
                    JSONArray responseArr = new JSONArray(responseData);
                    String last_date = "";
                    int last_isOrder = -1;
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject order = responseArr.getJSONObject(i);
//                        Log.d(TAG, "order:" + order);

                        int isOrder = 1;
                        String order_id = order.getString("order_id");
                        String valuation_status = order.getString("valuation_status");
                        String status = order.getString("order_status");
                        Log.d(TAG, "order_id: "+order_id+", valuation_status: "+valuation_status+", order_status: "+status);
                        if(status.equals("cancel") || status.equals("done") || status.equals("paid")) continue;
                        if(status.equals("evaluating")) {
                            isOrder = 0;
                            if(valuation_status.equals("cancel")) continue;
                        }
                        else if(!valuation_status.equals("chosen")) continue;


                        String date;
                        if(order.getString("moving_date").equals("null")) {
                            if(order.getString("valuation_date").equals("null") || order.getString("valuation_status").equals("self")){
                                date = order.getString("last_update");
                                String[] token = date.split(" ");
                                date = token[0];
                            }
                            else date = order.getString("valuation_date");
                        }
                        else {
                            date = order.getString("moving_date");
                            String[] token = date.split(" ");
                            date = token[0];

                            if(isOvertime){
                                //取消過期單
                                LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Taipei"));
                                Log.d(TAG, "now: "+now.getYear()+"-"+monthToInt(String.valueOf(now.getMonth()))+"-"+now.getDayOfMonth());
                                Log.d(TAG, "order("+order_id+"): "+Integer.parseInt(getYear(date))+"-"+Integer.parseInt(getMonth(date))+"-"+Integer.parseInt(getDay(date)));
                                if(Integer.parseInt(getYear(date))<now.getYear() ||
                                        (Integer.parseInt(getYear(date))<=now.getYear() &&
                                                Integer.parseInt(getMonth(date))<monthToInt(String.valueOf(now.getMonth()))) ||
                                        (Integer.parseInt(getYear(date))<=now.getYear() &&
                                                Integer.parseInt(getMonth(date))<=monthToInt(String.valueOf(now.getMonth())) &&
                                                Integer.parseInt(getDay(date))<now.getDayOfMonth())) {
                                    Log.d(TAG, "moving_date "+date+" of order_id "+order_id+" is over time");
                                    changeStatus(order_id, "orders", "cancel", context);
                                    continue;
                                }
                                else isOvertime = false;
                            }
                        }
                        Log.d(TAG, "order_id: "+order_id+", date: "+date+", isOrder:"+isOrder+", status: "+status);




                        java.util.Calendar calendar = java.util.Calendar.getInstance();
                        calendar.set(java.util.Calendar.YEAR, Integer.parseInt(getYear(date)));
                        calendar.set(java.util.Calendar.MONTH, Integer.parseInt(getMonth(date))-1);
                        calendar.set(java.util.Calendar.DATE, Integer.parseInt(getDay(date)));

                        EventDay eventDay;
                        if(date.equals(last_date)){
//                            Log.d(TAG, "isOrder: "+isOrder+", last_isOrder: "+last_isOrder);
                            if(isOrder == 1 && last_isOrder == 0) {
//                                Log.d(TAG, "DOUBLE ORDER");
                                eventDay = new EventDay(calendar, R.drawable.calendar_double_dot);
                                events.set(events.size()-1, eventDay);
                                last_date = date;
                                last_isOrder = 4;
                            }
                            continue;
                        }

                        if(isOrder == 1) {
//                            Log.d(TAG, "BLUE");
                            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.calendar_dot2);
                            eventDay = new EventDay(calendar, new InsetDrawable(drawable, 55));
                        }
                        else {
//                            Log.d(TAG, "ORANGE");
                            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.calendar_dot);
                            eventDay = new EventDay(calendar, new InsetDrawable(drawable, 55));
                        }

                        events.add(eventDay);
                        last_date = date;
                        last_isOrder = isOrder;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "getOrders: "+e.getMessage());
                }

                runOnUiThread(() -> mCalendar.setEvents(events));
            }
        });
    }

    private void getAllOrdersData(){
        Log.d(TAG, "start getAllOrdersData and write in sqlite db");
        RequestBody body = new FormBody.Builder()
                .add("company_id", getCompany_id(context))
                .build();

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL + "/get_data/all_orders_data.php")
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(context, "連線失敗", Toast.LENGTH_LONG).show());

                Handler handler = new Handler();
                handler.postDelayed(() -> new AsyncRetrieve().execute(), 3000);

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, "responseData of getAllOrdersData: "+responseData); //顯示資料

                JSONArray responseArr;
                try {
                    responseArr= new JSONArray(responseData);
                }catch (JSONException e){
                    e.printStackTrace();
                    Log.d(TAG, "getAllOrdersData: "+e.getMessage());
                    return;
                }

                int success_counter = 0, fail_counter = 0;
                for (int i = 0; i < responseArr.length(); i++) {
                    JSONObject order;
                    try {
                        order = responseArr.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
//                    Log.d(TAG, (i+1)+". order: "+order);

                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    try {
                        values.put(TableContract.OrdersTable.COLUMN_NAME_ORDER_ID, order.getString(TableContract.OrdersTable.COLUMN_NAME_ORDER_ID));
                        values.put(TableContract.OrdersTable.COLUMN_NAME_MEMBER_ID, order.getString(TableContract.OrdersTable.COLUMN_NAME_MEMBER_ID));
                        values.put(TableContract.OrdersTable.COLUMN_NAME_ADDITIONAL, order.getString(TableContract.OrdersTable.COLUMN_NAME_ADDITIONAL));
                        values.put(TableContract.OrdersTable.COLUMN_NAME_MEMO, order.getString(TableContract.OrdersTable.COLUMN_NAME_MEMO));
                        values.put(TableContract.OrdersTable.COLUMN_NAME_OUT_CITY, order.getString(TableContract.OrdersTable.COLUMN_NAME_OUT_CITY));
                        values.put(TableContract.OrdersTable.COLUMN_NAME_OUT_DISTRICT, order.getString(TableContract.OrdersTable.COLUMN_NAME_OUT_DISTRICT));
                        values.put(TableContract.OrdersTable.COLUMN_NAME_OUT_ADDRESS, order.getString(TableContract.OrdersTable.COLUMN_NAME_OUT_ADDRESS));
                        values.put(TableContract.OrdersTable.COLUMN_NAME_IN_CITY, order.getString(TableContract.OrdersTable.COLUMN_NAME_IN_CITY));
                        values.put(TableContract.OrdersTable.COLUMN_NAME_IN_DISTRICT, order.getString(TableContract.OrdersTable.COLUMN_NAME_IN_DISTRICT));
                        values.put(TableContract.OrdersTable.COLUMN_NAME_IN_ADDRESS, order.getString(TableContract.OrdersTable.COLUMN_NAME_IN_ADDRESS));
                        values.put(TableContract.OrdersTable.COLUMN_NAME_FROM_ELEVATOR, order.getString(TableContract.OrdersTable.COLUMN_NAME_FROM_ELEVATOR));
                        values.put(TableContract.OrdersTable.COLUMN_NAME_TO_ELEVATOR, order.getString(TableContract.OrdersTable.COLUMN_NAME_TO_ELEVATOR));
                        //values.put(TableContract.OrdersTable.COLUMN_NAME_STORAGE_SPACE, order.getString(TableContract.OrdersTable.COLUMN_NAME_STORAGE_SPACE));
                        //values.put(TableContract.OrdersTable.COLUMN_NAME_CARTON_NUM, order.getString(TableContract.OrdersTable.COLUMN_NAME_CARTON_NUM));
                        values.put(TableContract.OrdersTable.COLUMN_NAME_PROGRAM, order.getString(TableContract.OrdersTable.COLUMN_NAME_PROGRAM));
                        values.put(TableContract.OrdersTable.COLUMN_NAME_ORDER_STATUS, order.getString(TableContract.OrdersTable.COLUMN_NAME_ORDER_STATUS));
                        values.put(TableContract.OrdersTable.COLUMN_NAME_AUTO, order.getString(TableContract.OrdersTable.COLUMN_NAME_AUTO));
                        values.put(TableContract.OrdersTable.COLUMN_NAME_LAST_UPDATE, order.getString(TableContract.OrdersTable.COLUMN_NAME_LAST_UPDATE));
                        values.put(TableContract.OrdersTable.COLUMN_NAME_IS_WEB, order.getString(TableContract.OrdersTable.COLUMN_NAME_IS_WEB));
//                        Log.d(TAG, (i+1)+". "+values.toString())
                    } catch (JSONException e) {
                        e.printStackTrace();
                        continue;
                    }

                    try{
                        long newRowId = db.insertOrThrow(TableContract.OrdersTable.TABLE_NAME, null, values);
                        if(newRowId != -1){
                            success_counter = success_counter + 1;
                            Log.d(TAG, "create orders successfully");
                        }
                        else{
                            fail_counter = fail_counter + 1;
                            Log.d(TAG, "create orders failed");
                        }
                    } catch (SQLException e){
                        if(e.getMessage().contains("no such table")) break;
                        if(Objects.requireNonNull(e.getMessage()).contains("PRIMARYKEY")){
                            String selection = TableContract.OrdersTable.COLUMN_NAME_ORDER_ID+" = ?";
                            String[] selectionArgs = {values.getAsString(TableContract.OrdersTable.COLUMN_NAME_ORDER_ID)};

                            int count = db.update(
                                    TableContract.OrdersTable.TABLE_NAME,
                                    values,
                                    selection,
                                    selectionArgs
                            );
                            if(count != -1) success_counter = success_counter + 1;
                            else fail_counter = fail_counter + 1;
                        }
                        else{
                            e.printStackTrace();
                            Log.d(TAG, "insert order data: "+e.getMessage());
                        }
                    }
                }
                Log.d(TAG, "orders data:\n success data: "+success_counter+", fail data: "+fail_counter);
            }
        });
    }

    private void getAllMemberData(){
        Log.d(TAG, "start getAllMemberData and write in sqlite db");
        RequestBody body = new FormBody.Builder()
                .add("company_id", getCompany_id(context))
                .build();

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL + "/get_data/all_company_member.php")
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(context, "連線失敗", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, "responseData of getAllMemberData: "+responseData); //顯示資料

                JSONArray responseArr;
                try {
                    responseArr= new JSONArray(responseData);
                }catch (JSONException e){
                    e.printStackTrace();
                    Log.d(TAG, "getAllMemberData: "+e.getMessage());
                    return;
                }

                int success_counter = 0, fail_counter = 0;
                for (int i = 0; i < responseArr.length(); i++) {
                    JSONObject member;
                    try {
                        member = responseArr.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
//                    Log.d(TAG, (i+1)+". member: "+member);

                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    try {
                        values.put(TableContract.MemberTable.COLUMN_NAME_MEMBER_ID, member.getString(TableContract.MemberTable.COLUMN_NAME_MEMBER_ID));
                        values.put(TableContract.MemberTable.COLUMN_NAME_MEMBER_NAME, member.getString(TableContract.MemberTable.COLUMN_NAME_MEMBER_NAME));
                        values.put(TableContract.MemberTable.COLUMN_NAME_GENDER, member.getString(TableContract.MemberTable.COLUMN_NAME_GENDER));
                        values.put(TableContract.MemberTable.COLUMN_NAME_PHONE, member.getString(TableContract.MemberTable.COLUMN_NAME_PHONE));
                        values.put(TableContract.MemberTable.COLUMN_NAME_CONTACT_ADDRESS, member.getString(TableContract.MemberTable.COLUMN_NAME_CONTACT_ADDRESS));
                        values.put(TableContract.MemberTable.COLUMN_NAME_CONTACT_WAY, member.getString(TableContract.MemberTable.COLUMN_NAME_CONTACT_WAY));
                        values.put(TableContract.MemberTable.COLUMN_NAME_CONTACT_TIME, member.getString(TableContract.MemberTable.COLUMN_NAME_CONTACT_TIME));
//                        Log.d(TAG, (i+1)+". "+values.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        continue;
                    }

                    try{
                        long newRowId = db.insertOrThrow(TableContract.MemberTable.TABLE_NAME, null, values);
                        if(newRowId != -1){
                            success_counter = success_counter + 1;
                            Log.d(TAG, "create member successfully");
                        }
                        else{
                            fail_counter = fail_counter + 1;
                            Log.d(TAG, "create member failed");
                        }
                    } catch (SQLException e){
                        if(e.getMessage().contains("no such table")) break;
                        if(Objects.requireNonNull(e.getMessage()).contains("PRIMARYKEY"))
                            success_counter = success_counter + 1;
                        else{
                            e.printStackTrace();
                            Log.d(TAG, "insert member data: "+e.getMessage());
                        }
                    }
                }
                Log.d(TAG, "member data:\n success data: "+success_counter+", fail data: "+fail_counter);
            }
        });
    }






    private void linking() {
        calendar = findViewById(R.id.main_calendar);
        mCalendar = findViewById(R.id.mCalendar_main);
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
        addV_btn = v.findViewById(R.id.add_valuation_btn_C);
        addO_btn = v.findViewById(R.id.add_order_btn_C);
        cancel_btn = v.findViewById(R.id.cancelBtn_C);
    }

    private void init(){
        data = new ArrayList<>();
        data_v = new ArrayList<>();
        data_o = new ArrayList<>();
        choose_data = new ArrayList<>();
        initData();
        VB = true;
        OB = true;
        linking();
    }

    private void initData(){
        data.clear();
        data_v.clear();
        data_o.clear();
        choose_data.clear();
        String[] row_data = {"編號", "時間", "地址", "0", "0"};
        data.add(row_data);
        data_v.add(row_data);
        data_o.add(row_data);
    }

    private void globalNav(){
        //底下nav
        valuation_btn.setOnClickListener(v -> {
            Intent valuation_intent = new Intent(context, Valuation.class);
            valuation_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(valuation_intent);

        });
        order_btn.setOnClickListener(v -> {
            Intent order_intent = new Intent(context, Order.class);
            order_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(order_intent);
        });
//        calendar_btn.setOnClickListener(v -> {
//            Intent calender_intent = new Intent(context, Calendar.class);
//            startActivity(calender_intent);
//        });
        system_btn.setOnClickListener(v -> {
            Intent system_intent = new Intent(context, System.class);
            system_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(system_intent);
        });
        setting_btn.setOnClickListener(v -> {
            Intent setting_intent = new Intent(context, Setting.class);
            setting_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(setting_intent);
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

    }
    public class AsyncRetrieve extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... Void) {
            getAllOrdersData();
            return null;
        }
    }
    protected void onDestroy(){
        super.onDestroy();
        if (dialog != null) {
            dialog.create().dismiss();
        }
    }
}

