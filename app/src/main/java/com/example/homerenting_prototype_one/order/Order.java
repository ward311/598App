package com.example.homerenting_prototype_one.order;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.adapter.re_adpater.NoDataRecyclerAdapter;
import com.example.homerenting_prototype_one.add_order.Add_Order;
import com.example.homerenting_prototype_one.add_order.Add_Valuation;
import com.example.homerenting_prototype_one.helper.DatabaseHelper;
import com.example.homerenting_prototype_one.helper.RecyclerViewAction;
import com.example.homerenting_prototype_one.adapter.re_adpater.SwipeDeleteAdapter;
import com.example.homerenting_prototype_one.model.TableContract;
import com.example.homerenting_prototype_one.setting.Setting;
import com.example.homerenting_prototype_one.system.System;
import com.example.homerenting_prototype_one.adapter.base_adapter.ListAdapter;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.valuation.Fragment_Valuation;
import com.example.homerenting_prototype_one.valuation.Fragment_Valuation_Booking;
import com.example.homerenting_prototype_one.valuation.Fragment_Valuation_Cancel;
import com.example.homerenting_prototype_one.valuation.Fragment_Valuation_Match_Making;
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
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.homerenting_prototype_one.show.global_function.addDatalist;
import static com.example.homerenting_prototype_one.show.global_function.changeStat;
import static com.example.homerenting_prototype_one.show.global_function.changeStatus;
import static com.example.homerenting_prototype_one.show.global_function.clearDatalist;
import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;
import static com.example.homerenting_prototype_one.show.global_function.getDate;
import static com.example.homerenting_prototype_one.show.global_function.getDay;
import static com.example.homerenting_prototype_one.show.global_function.getEndOfWeek;
import static com.example.homerenting_prototype_one.show.global_function.getMonth;
import static com.example.homerenting_prototype_one.show.global_function.getMonthStr;
import static com.example.homerenting_prototype_one.show.global_function.getStartOfWeek;
import static com.example.homerenting_prototype_one.show.global_function.getTime;
import static com.example.homerenting_prototype_one.show.global_function.getToday;
import static com.example.homerenting_prototype_one.show.global_function.getWeek;
import static com.example.homerenting_prototype_one.show.global_function.getYear;
import static com.example.homerenting_prototype_one.show.global_function.getwCount;
import static com.example.homerenting_prototype_one.show.global_function.setwCount;

public class Order extends AppCompatActivity {

    TextView month_text;
    TextView week_text;
    ImageButton lastWeek_btn, nextWeek_btn;
    ListView orderList;
    RecyclerView orderRList;


    ArrayList<String[]> data = new ArrayList<>();
    ArrayList<String[]> furniture_data = new ArrayList<>();
    ListAdapter listAdapter;
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;
    Context context = this;
    String TAG = "Order";

    //private String company_id;

    boolean first = true;
    public static final int FRAG_ORDER = 5 ;
    public static final int FRAG_ORDER_BOOK = 6 ;
    public static final int FRAG_ORDER_TODAY = 7 ;
    public static final int FRAG_ORDER_CAN = 8 ;
    public int current_FRAG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        month_text = findViewById(R.id.month_O);
        week_text = findViewById(R.id.week_O);
        lastWeek_btn = findViewById(R.id.lastWeek_btn_O);
        nextWeek_btn = findViewById(R.id.nextWeek_btn_O);
        orderRList = findViewById(R.id.order_recyclerView_O);

        Button order_btn = findViewById(R.id.order_btn);
        Button booking_order = findViewById(R.id.bookingOrder_btn);
        Button today_order = findViewById(R.id.todayOrder_btn);
        Button cancel_order = findViewById(R.id.cancelOrder_btn);
        Button addOrder = findViewById(R.id.addOrder_btn);
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);
        replaceFragment(new Fragment_Order());
        current_FRAG = FRAG_ORDER;
        week_text.setText(getWeek());
        month_text.setText(getMonthStr());
        dbHelper = new DatabaseHelper(this);
        //getFurniture();
        lastWeek_btn.setOnClickListener(v -> {
            int wCount = getwCount();
            setwCount(wCount-1);
            week_text.setText(getWeek());
            month_text.setText(getMonthStr());
            data.clear();
            switch (current_FRAG){
                case 5 :
                    replaceFragment(new Fragment_Order());
                    break;
                case 6 :
                    replaceFragment(new Fragment_Order_Booking());
                    break;
                case 7 :
                    replaceFragment(new Fragment_Order_Today());
                    lastWeek_btn.setVisibility(View.GONE);
                    nextWeek_btn.setVisibility(View.GONE);
                    break;
                case 8 :
                    replaceFragment(new Fragment_Order_Cancel());
                    break;
            }
            //getOrder();
            //new AsyncRetrieve().execute();

        });

        nextWeek_btn.setOnClickListener(v -> {
            int wCount = getwCount();
            setwCount(wCount+1);
            week_text.setText(getWeek());
            month_text.setText(getMonthStr());
            data.clear();
            switch (current_FRAG){
                case 5 :
                    replaceFragment(new Fragment_Order());
                    break;
                case 6 :
                    replaceFragment(new Fragment_Order_Booking());
                    break;
                case 7 :
                    replaceFragment(new Fragment_Order_Today());
                    lastWeek_btn.setVisibility(View.GONE);
                    nextWeek_btn.setVisibility(View.GONE);
                    week_text.setText(getToday());
                    month_text.setText(getMonthStr());
                    break;
                case 8 :
                    replaceFragment(new Fragment_Order_Cancel());
                    break;
            }
            //getOrder();
            //new AsyncRetrieve().execute();
        });
        addOrder.setOnClickListener(v -> {
            startActivity(new Intent(context, Add_Order.class));
        });
        //上方nav
        order_btn.setOnClickListener(v->{
            replaceFragment(new Fragment_Order());
            current_FRAG = FRAG_ORDER;
            week_text.setText(getWeek());
            month_text.setText(getMonthStr());
            lastWeek_btn.setVisibility(View.VISIBLE);
            nextWeek_btn.setVisibility(View.VISIBLE);
            order_btn.setTextColor(Color.parseColor("#FB8527"));
            booking_order.setTextColor(Color.BLACK);
            today_order.setTextColor(Color.BLACK);
            cancel_order.setTextColor(Color.BLACK);
        });
        booking_order.setOnClickListener(v -> {
            /*Intent bookingOrder_intent = new Intent(Order.this, Order_Booking.class);
            bookingOrder_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(bookingOrder_intent);*/
            replaceFragment(new Fragment_Order_Booking());
            current_FRAG = FRAG_ORDER_BOOK;
            week_text.setText(getWeek());
            month_text.setText(getMonthStr());
            lastWeek_btn.setVisibility(View.VISIBLE);
            nextWeek_btn.setVisibility(View.VISIBLE);
            booking_order.setTextColor(Color.parseColor("#FB8527"));
            order_btn.setTextColor(Color.BLACK);
            today_order.setTextColor(Color.BLACK);
            cancel_order.setTextColor(Color.BLACK);
        });
        today_order.setOnClickListener(v -> {
            /*Intent todayOrder_intent = new Intent(Order.this, Order_Today.class);
            todayOrder_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(todayOrder_intent);*/
            replaceFragment(new Fragment_Order_Today());
            lastWeek_btn.setVisibility(View.GONE);
            nextWeek_btn.setVisibility(View.GONE);
            week_text.setText(getToday());
            month_text.setText(getMonthStr());
            current_FRAG = FRAG_ORDER_TODAY;
            today_order.setTextColor(Color.parseColor("#FB8527"));
            order_btn.setTextColor(Color.BLACK);
            booking_order.setTextColor(Color.BLACK);
            cancel_order.setTextColor(Color.BLACK);
        });
        cancel_order.setOnClickListener(v -> {
            /*Intent cancelOrder_intent = new Intent(Order.this, Order_Cancel.class);
            cancelOrder_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(cancelOrder_intent);*/
            replaceFragment(new Fragment_Order_Cancel());
            current_FRAG = FRAG_ORDER_CAN;
            week_text.setText(getWeek());
            month_text.setText(getMonthStr());
            lastWeek_btn.setVisibility(View.VISIBLE);
            nextWeek_btn.setVisibility(View.VISIBLE);
            cancel_order.setTextColor(Color.parseColor("#FB8527"));
            order_btn.setTextColor(Color.BLACK);
            booking_order.setTextColor(Color.BLACK);
            today_order.setTextColor(Color.BLACK);
        });

        //底下nav
        valuation_btn.setOnClickListener(v -> {
            Intent valuation_intent = new Intent(Order.this, Valuation.class);
            valuation_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(valuation_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
            finish();
        });
        calendar_btn.setOnClickListener(v -> {
            Intent calender_intent = new Intent(Order.this, Calendar.class);
            calender_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(calender_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
            finish();
        });
        system_btn.setOnClickListener(v -> {
            Intent system_intent = new Intent(Order.this, System.class);
            system_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(system_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
            finish();
        });
        setting_btn.setOnClickListener(v -> {
            Intent setting_intent = new Intent(Order.this, Setting.class);
            setting_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(setting_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
            finish();
        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.commit();
    }
    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        init();
        //new AsyncRetrieve().execute();
    }

    private void init(){
        data.clear();
        orderRList.setAdapter(null);
    }

    private void getFurniture() {
        String function_name = "get_all_furniture";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .build();
        //連線要求
        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL + "/furniture.php")
                .post(body)
                .build();

        //連線
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            //連線失敗
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //顯示錯誤訊息
                //在app畫面上呈現錯誤訊息
                runOnUiThread(() -> Toast.makeText(context, "連線失敗", Toast.LENGTH_LONG).show());
            }

            //連線成功
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.i(TAG, "responseData of All Furniture: " + responseData); //顯示資料
                JSONArray responseArr;
                try {
                    responseArr = new JSONArray(responseData);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "getAllFurnitureData: " + e.getMessage());
                    return;
                }
                int success_counter = 0, fail_counter = 0;
                for (int i = 0; i < responseArr.length(); i++) {
                    JSONObject furniture;
                    try {
                        furniture = responseArr.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    try {
                        //values.put(TableContract.FurnitureTable.COLUMN_NAME_NO, furniture.getString(TableContract.FurnitureTable.COLUMN_NAME_NO));
                        values.put(TableContract.FurnitureTable.COLUMN_NAME_FURNITURE_ID, furniture.getString(TableContract.FurnitureTable.COLUMN_NAME_FURNITURE_ID));
                        values.put(TableContract.FurnitureTable.COLUMN_NAME_SPACE_TYPE, furniture.getString(TableContract.FurnitureTable.COLUMN_NAME_SPACE_TYPE));
                        values.put(TableContract.FurnitureTable.COLUMN_NAME_FURNITURE_NAME, furniture.getString(TableContract.FurnitureTable.COLUMN_NAME_FURNITURE_NAME));
                        values.put(TableContract.FurnitureTable.COLUMN_NAME_IMG, furniture.getString(TableContract.FurnitureTable.COLUMN_NAME_IMG));
//                      Log.d(TAG, (i+1)+". "+values.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        continue;
                    }

                    try {
                        long newRowId = db.insertOrThrow(TableContract.FurnitureTable.TABLE_NAME, null, values);
                        if (newRowId != -1) {
                            success_counter = success_counter + 1;
                            Log.d(TAG, "create furniture successfully");
                        } else {
                            fail_counter = fail_counter + 1;
                            Log.d(TAG, "create furniture failed");
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
                if (!responseData.equals("null")) {
                    for (int i = 0; i < furniture_data.size(); i++)
                        Log.i(TAG, "furniture data: " + Arrays.toString(furniture_data.get(i)));

                }
            }
        });
    }
    private void getOrder(){
        clearDatalist();
        //傳至網頁的值
        String function_name = "order_member";
        String startDate = getStartOfWeek();
        String endDate = getEndOfWeek();
        String company_id = getCompany_id(context);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", company_id)
                .add("startDate", startDate)
                .add("endDate", endDate)
                .add("status", "scheduled")
                .build();
        Log.i(TAG, "getOrder:\n"+"company_id: "+company_id+", startDate:"+startDate+", endDate:"+endDate+", status:"+"scheduled");

        //連線要求
        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL + "/user_data.php")
                .post(body)
                .build();
        //http://54.166.177.4/user_data.php

        //連線
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            //連線失敗
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //顯示錯誤訊息
                //在app畫面上呈現錯誤訊息
                Looper.prepare();
                runOnUiThread(() -> Toast.makeText(context, "連線失敗", Toast.LENGTH_LONG).show());
            }

            //連線成功
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.i(TAG,"responseData: "+responseData); //顯示資料

                try {
                    //轉換成json格式，array或object
                    final JSONArray responseArr = new JSONArray(responseData);
                    //Log.i(TAG,"responseObj: "+ responseArr);

                    //一筆一筆的取JSONArray中的json資料
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject member = responseArr.getJSONObject(i);
                        Log.i(TAG,"member: "+member);

                        //取欄位資料
                        String order_id = member.getString("order_id");
                        String datetime = member.getString("moving_date");
                        String name = member.getString("member_name");
                        String nameTitle;
                        if(member.getString("gender").equals("女")) nameTitle = "小姐";
                        else nameTitle = "先生";
                        String phone = member.getString("phone");
                        String contact_address = member.getString("contact_address");
                        if(contact_address.equals("null")) contact_address = "";
                        String auto = member.getString("auto");
                        String newicon = member.getString("new");

                        //取消過期單
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

                        //將資料存進陣列裡
                        String[] row_data = {order_id, getDate(datetime), getTime(datetime), name, nameTitle, phone, contact_address, auto, newicon};
                        data.add(row_data);
                        addDatalist(order_id);
                    }
                } catch (JSONException e) { //會到這裡通常表示用錯json格式或網頁的資料不是json格式
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        if(responseData.equals("null")){
                            Log.d(TAG, "NO DATA");
                            NoDataRecyclerAdapter noDataAdapter = new NoDataRecyclerAdapter();
                            orderRList.setLayoutManager(new LinearLayoutManager(context));
                            orderRList.setAdapter(noDataAdapter);
                        }
                        //else Toast.makeText(Order.this, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show();
                    });
                }

                //顯示資訊
                if(!responseData.equals("null")){
                    for(int i=0; i < data.size(); i++)
                        Log.i(TAG, "data: "+ Arrays.toString(data.get(i)));
                    setRList();
                }
            }
        });
    }

    private void setRList(){
        final SwipeDeleteAdapter adapter = new SwipeDeleteAdapter(context, data, Order_Detail.class);
        runOnUiThread(() -> {
            orderRList.setLayoutManager(new LinearLayoutManager(context));
            orderRList.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL)); //分隔線
            orderRList.setAdapter(adapter);

            //側滑刪除
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
        Intent toCalendar = new Intent(Order.this, Calendar.class);
        toCalendar.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toCalendar);
    }
    public class AsyncRetrieve extends AsyncTask<String, String, Void> {
        @Override
        protected Void doInBackground(String... strings) {
           getOrder();
           getFurniture();
            return null;
        }
    }
}
