package com.example.homerenting_prototype_one.system;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.EventDay;
import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.adapter.re_adpater.NoDataRecyclerAdapter;
import com.example.homerenting_prototype_one.adapter.re_adpater.SwipeDeleteAdapter;
import com.example.homerenting_prototype_one.add_order.Add_Order;
import com.example.homerenting_prototype_one.add_order.Add_Valuation;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.helper.RecyclerViewAction;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.order.Order_Detail;
import com.example.homerenting_prototype_one.schedule.New_Schedule_Detail;
import com.example.homerenting_prototype_one.setting.Setting;
import com.example.homerenting_prototype_one.valuation.Valuation;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

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
import static com.example.homerenting_prototype_one.show.global_function.getMonth;
import static com.example.homerenting_prototype_one.show.global_function.getTime;
import static com.example.homerenting_prototype_one.show.global_function.getToday;
import static com.example.homerenting_prototype_one.show.global_function.getYear;

public class System_Schedule extends AppCompatActivity {
    ScrollView calendar_sv;
    CalendarView calendar;
    com.applandeo.materialcalendarview.CalendarView mCalendar;
    RecyclerView orderList;
    ChipGroup staffGroup,  carGroup;

    ArrayList<String[]> data = new ArrayList<>();
    ArrayList<String> staffs_text, cars_text;
    ArrayList<Integer> staffs, cars;

    boolean lock = false;

    Context context = this;
    String TAG = "System_Schedule";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system__schedule);
//        calendar_sv = findViewById(R.id.calender_sv_SSD);
//        calendar = findViewById(R.id.calendar_SSD);
        mCalendar = findViewById(R.id.mCalendar_SSD);
        orderList = findViewById(R.id.orderList_rv_SSD);
        staffGroup = findViewById(R.id.staffCG_SSD);
        carGroup = findViewById(R.id.carCG_SSD);

        staffs_text = new ArrayList<>();
        staffs = new ArrayList<>();
        cars_text = new ArrayList<>();
        cars = new ArrayList<>();

        orderList.setLayoutManager(new LinearLayoutManager(context));
        orderList.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL)); //分隔線

        getStaffChip();
        Handler handler = new Handler();
        handler.postDelayed(() -> getVehicleChip(), 500);

        new AsyncRetrieve().execute();
        //getStaffVacation(getToday("yyyy-MM-dd"));
        //getVehicleVacation(getToday("yyyy-MM-dd"));
        setmCalendar();

        globalNav();
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
            if(calendarTime.get(java.util.Calendar.MONTH)+1 > previous_month)
                calendarTime.set(java.util.Calendar.DATE, calendarTime.get(java.util.Calendar.DATE)-1);
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
            Log.i(TAG, "Date Change: " + date);

            getOrder(date);
            orderList.setVisibility(View.VISIBLE);
            mCalendar.setVisibility(View.GONE);

            initArray();
            getStaffVacation(date);
            getVehicleVacation(date);
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
                runOnUiThread(() -> Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show());
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
//                Log.d(TAG, "responseData of getOrders: "+responseData); //顯示資料

                try {
                    JSONArray responseArr = new JSONArray(responseData);
                    String last_date = "";
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject order = responseArr.getJSONObject(i);
//                        Log.d(TAG, "order:" + order);

                        int isOrder = 1;
                        String order_id = order.getString("order_id");
                        String valuation_status = order.getString("valuation_status");
                        String status = order.getString("order_status");
                        if(!valuation_status.equals("chosen") || status.equals("evaluating") || status.equals("cancel") || status.equals("done") || status.equals("paid")){
                            continue;
                        }

                        String date;
                        date = order.getString("moving_date");
                        if(date == null || date.isEmpty()){
                            Log.d(TAG, "order_id: "+order_id+", 【date: "+date+"】, status: "+status);
                            continue;
                        }

                        //取消過期單
                        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Taipei"));
                        Log.d(TAG, "now: "+now.getYear()+"-"+monthToInt(String.valueOf(now.getMonth()))+"-"+now.getDayOfMonth());
                        Log.d(TAG, "order: "+Integer.parseInt(getYear(date))+"-"+Integer.parseInt(getMonth(date))+"-"+Integer.parseInt(getDay(date)));
                        if(Integer.parseInt(getYear(date))<now.getYear() ||
                                (Integer.parseInt(getYear(date))<=now.getYear() &&
                                        Integer.parseInt(getMonth(date))<monthToInt(String.valueOf(now.getMonth()))) ||
                                (Integer.parseInt(getYear(date))<=now.getYear() &&
                                        Integer.parseInt(getMonth(date))<=monthToInt(String.valueOf(now.getMonth())) &&
                                        Integer.parseInt(getDay(date))<now.getDayOfMonth())) {
                            Log.d(TAG, "moving_date "+date+" of order_id "+order_id+" is over time");
                            changeStat(order_id, "orders", "cancel", context);
                            continue;
                        }



                        String[] token = date.split(" ");
                        date = token[0];
                        Log.d(TAG, "order_id: "+order_id+", date: "+date+", status: "+status);

                        java.util.Calendar calendar = java.util.Calendar.getInstance();
                        calendar.set(java.util.Calendar.YEAR, Integer.parseInt(getYear(date)));
                        calendar.set(java.util.Calendar.MONTH, Integer.parseInt(getMonth(date))-1);
                        calendar.set(java.util.Calendar.DATE, Integer.parseInt(getDay(date)));

                        EventDay eventDay;
                        if(date.equals(last_date)) continue;

//                        Log.d(TAG, "BLUE");
                        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.calendar_dot2);
                        eventDay = new EventDay(calendar, new InsetDrawable(drawable, 55));

                        events.add(eventDay);
                        last_date = date;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "getOrders: "+e.getMessage());
                }

                runOnUiThread(() -> mCalendar.setEvents(events));
            }
        });
    }

    private void getStaffChip(){
        Log.d(TAG, "start getStaffChip()");
        lock = true;

        RequestBody body = new FormBody.Builder()
                .add("company_id", getCompany_id(context))
                .build();

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/get_data/all_staff_data.php")
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                lock = false;
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //顯示錯誤訊息
                //在app畫面上呈現錯誤訊息
                Looper.prepare();
                runOnUiThread(() -> Toast.makeText(context, "連線失敗", Toast.LENGTH_LONG).show());
                Looper.loop();
                Handler handler = new Handler();
                handler.postDelayed(() -> getStaffChip(), 3000);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG,"responseData of get_staff_chip: "+responseData); //顯示資料

                try {
                    //轉換成json格式，array或object
                    final JSONArray responseArr = new JSONArray(responseData);
                    //Log.i(TAG,"responseObj: "+ responseArr);

                    //一筆一筆的取JSONArray中的json資料
                    int i;
                    for (i = 0; i < responseArr.length(); i++) {
                        JSONObject staff = responseArr.getJSONObject(i);
                        if(!staff.has("staff_id")) break;
                        Log.i(TAG, "staff: " + staff);

                        //取欄位資料
                        final String staff_id = staff.getString("staff_id");
                        final String staff_name = staff.getString("staff_name");

                        //在staffGroup底下新增chip，加入ID和Tag
                        runOnUiThread(() -> staffGroup.addView(setChipDetail(staffGroup, staff_id, staff_name)));
                    }

                    int ii = 0;
                    /*while((staffGroup.getChildCount()-1) != responseArr.length()){
                        if((++ii)%5000000 == 0)
                            Log.d(TAG, (ii/5000000)+". waiting in getChip(): staffGroup:"+staffGroup.getChildCount()+", carGroup:"+carGroup.getChildCount());
                    }
                    Log.d(TAG, "waiting in getChip() final: staffGroup:"+staffGroup.getChildCount()+", carGroup:"+carGroup.getChildCount());*/
                    //getVehicleChip();
                } catch (JSONException e) {
                    lock = false;
                    e.printStackTrace();
                }
            }
        });
    }

    private void getVehicleChip(){
        Log.d(TAG, "start getVehicleChip()");
        lock = true;

        RequestBody body = new FormBody.Builder()
                .add("company_id", getCompany_id(context))
                .build();

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/get_data/all_vehicle_data.php")
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                lock = false;
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //顯示錯誤訊息
                //在app畫面上呈現錯誤訊息

                Looper.prepare();
                runOnUiThread(() -> Toast.makeText(context, "連線失敗", Toast.LENGTH_LONG).show());
                Looper.loop();
                Handler handler = new Handler();
                handler.postDelayed(() -> getVehicleChip(), 3000);

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG,"responseData of get_vehicle_chip: "+responseData); //顯示資料

                try {
                    //轉換成json格式，array或object
                    final JSONArray responseArr = new JSONArray(responseData);
                    //Log.i(TAG,"responseObj: "+ responseArr);

                    //一筆一筆的取JSONArray中的json資料
                    int i;
                    for (i = 0; i < responseArr.length(); i++) {
                        JSONObject vehicle = responseArr.getJSONObject(i);
                        if(!vehicle.has("vehicle_id")) break;
                        Log.i(TAG, "vehicle: " + vehicle);

                        //取欄位資料
                        final String vehicle_id = vehicle.getString("vehicle_id");
                        final String plate_num = vehicle.getString("plate_num");
                        final String vehicle_weight = vehicle.getString("vehicle_weight");
                        final String vehicle_type = vehicle.getString("vehicle_type");
                        final String verify = vehicle.getString("verified");
                        if(verify.equals("1")){
                             runOnUiThread(() -> {
                                carGroup.addView(setChipDetail(carGroup, vehicle_id, plate_num));
                                Chip carChip = (Chip) carGroup.getChildAt(carGroup.getChildCount()-1);
                            });
                        }

                    }

                    int ii = 0;
                    /*while((carGroup.getChildCount()-1) != responseArr.length()){
                        if((++ii)%5000000 == 0)
                            Log.d(TAG, (ii/5000000)+". waiting in getChip(): staffGroup:"+staffGroup.getChildCount()+", carGroup:"+carGroup.getChildCount());
                    }
                    Log.d(TAG, "waiting in getChip() final: staffGroup:"+staffGroup.getChildCount()+", carGroup:"+carGroup.getChildCount());*/
                    lock = false;
                } catch (JSONException e) {
                    lock = false;
                    e.printStackTrace();
                    if(!responseData.equals("null") && !responseData.equals("function_name not found."))
                        runOnUiThread(() -> Toast.makeText(context, "Toast onResponse failed because JSON in getChip", Toast.LENGTH_LONG).show());
                }
            }
        });
    }

    private Chip setChipDetail(ChipGroup chipGroup, String id, String name){
        final Chip chip = new Chip(chipGroup.getContext());
        chip.setId(Integer.parseInt(id));
        chip.setTag(Integer.parseInt(id));

        chip.setText(name); //顯示員工姓名
        chip.setTextSize(16); //文字的大小

        //選擇chip的模式:choice
        ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(chipGroup.getContext(), null, 0 ,R.style.Widget_MaterialComponents_Chip_Choice);
        chip.setChipDrawable(chipDrawable);

        chip.setCheckable(false); //可點擊

        chip.setChipBackgroundColorResource(R.color.bg_chip_state_list); //變換chip點擊背景顏色
        chip.setTextAppearance(R.style.chipTextColor); //變換chip點擊文字顏色

        if(chipGroup == staffGroup) setChipCheckedListener(chip, name, staffs, staffs_text);
        else setChipCheckedListener(chip, name, cars, cars_text);

        return chip;
    }

    private void setChipCheckedListener(final Chip chip, final String name, final ArrayList<Integer> items, final ArrayList<String> items_text){
        chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int tag = (int) buttonView.getTag();
            String sname = chip.getText().toString();
            if(isChecked){ //取消選擇
                items.add(tag);
                if(!items_text.contains(name))
                    items_text.add(sname);
            }
            else{ //加入選擇
                items.remove(Integer.valueOf(tag));
                items_text.remove(sname);
            }
            Log.i(TAG, "click chip: " + sname);

            String item_name;
            if(items == staffs) item_name = "staffs";
            else item_name = "cars";
            Log.i(TAG, item_name+"_text: " + items_text);
            Log.i(TAG, item_name+": " + items);

        });
    }

    private void getOrder(String date){
        clearDatalist();
        data.clear();
        //傳至網頁的值，傳function_name
        String function_name = "order_member_oneDay";
        String company_id = getCompany_id(this);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", company_id)
                .add("date", date)
                .add("isOrder", "TRUE")
                .build();
        Log.d(TAG, "getOrder: date: "+date);

        //連線要求
        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/user_data.php")
                .post(body)
                .build();

        //連線
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //顯示錯誤訊息
                //在app畫面上呈現錯誤訊息
                runOnUiThread(() -> Toast.makeText(context, "連線失敗", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG, "responseData: "+responseData); //顯示資料

                try {
                    //轉換成json格式，array或object
                    final JSONArray responseArr = new JSONArray(responseData);
//                    Log.d(TAG,"responseObj: "+ responseArr);

                    //一筆一筆的取JSONArray中的json資料
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject member = responseArr.getJSONObject(i);
                        Log.d(TAG,"member:"+member);

                        //取欄位資料
                        String order_id = member.getString("order_id");
                        String datetime = member.getString("moving_date");
                        String name = member.getString("member_name");
                        String nameTitle;
                        if(member.getString("gender").equals("女")) nameTitle = "小姐";
                        else nameTitle = "先生";
                        final String phone = member.getString("phone");
                        String contact_address = member.getString("contact_address");
                        if(contact_address.equals("null")) contact_address = "";
                        String auto = member.getString("auto");
                        String newicon = member.getString("new");
                        String status = member.getString("order_status");
                        if(status.equals("done")) status = "done_today";


                        //將資料放入陣列
                        String[] row_data = {order_id, getDate(datetime), getTime(datetime), name, nameTitle, phone, contact_address, auto, newicon, status};
                        data.add(row_data);
                        addDatalist(order_id);
                    }
                } catch (JSONException e) { //會到這裡通常表示用錯json格式或網頁的資料不是json格式
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        if(responseData.equals("null")){
                            Log.d(TAG, "NO DATA");
                            NoDataRecyclerAdapter noDataAdapter = new NoDataRecyclerAdapter();
                            orderList.setLayoutManager(new LinearLayoutManager(context));
                            orderList.setAdapter(noDataAdapter);
                        }
                    });
                }
                //顯示資訊
                if(!responseData.equals("null")){
                    for(int i=0; i < data.size(); i++)
                        Log.i(TAG, "data: "+ Arrays.toString(data.get(i)));
                    setList();
                }
            }
        });
    }

    private void setList(){
        final SwipeDeleteAdapter adapter = new SwipeDeleteAdapter(this, data, New_Schedule_Detail.class);
        runOnUiThread(() -> {
            orderList.setAdapter(null);
            orderList.setAdapter(adapter);
        });
    }

    private void initArray(){
        staffs.clear();
        staffs_text.clear();
        cars.clear();
        cars_text.clear();
        setChipCheck(staffGroup, staffs_text);
        setChipCheck(carGroup, cars_text);
    }

    private void getStaffVacation(String date){
        if(date == null){
            Log.d(TAG, "date is null in getStaffVacation");
            return;
        }

        Log.d(TAG, "start getStaffVacation()");

        RequestBody body = new FormBody.Builder()
                .add("company_id", getCompany_id(context))
                .add("date", date)
                .build();

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/get_data/staff_leave.php")
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //顯示錯誤訊息
                //在app畫面上呈現錯誤訊息

                Looper.prepare();
                runOnUiThread(() -> Toast.makeText(context, "連線失敗", Toast.LENGTH_LONG).show());
                Looper.loop();
                Handler handler = new Handler();
                handler.postDelayed(() -> getStaffVacation(date), 3000);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG,"responseData of getStaffVocation: "+responseData); //顯示資料
                if(responseData.equals("null")) return;

                try {
                    JSONArray responseArr = new JSONArray(responseData);

                    int i;
                    for (i = 0; i < responseArr.length(); i++) {
                        JSONObject staff_vacation = responseArr.getJSONObject(i);
                        if(!staff_vacation.has("staff_id")) break;
                        Log.i(TAG, "staff_vacation:" + staff_vacation);
                        staffs_text.add(staff_vacation.getString("staff_name"));
                        staffs.add(Integer.parseInt(staff_vacation.getString("staff_id")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    if(!responseData.equals("null") && !responseData.equals("function_name not found.")) {
                        runOnUiThread(() -> Toast.makeText(context, "Toast onResponse failed because JSON in getStaffVacation", Toast.LENGTH_LONG).show());
                    }
                }

                int ii = 0;
                while (lock){
                    if((++ii)%1000000 == 0) Log.d(TAG, "waiting for lock in getStaffVacation...");
                }
                Log.d(TAG, "getStaffVacation: staffGroup:"+staffGroup.getChildCount()+", carGroup:"+carGroup.getChildCount());
                setChipCheck(staffGroup, staffs_text);
            }
        });
    }

    private void getVehicleVacation(String date){
        if(date == null){
            Log.d(TAG, "date is null in getVehicleVacation");
            return;
        }

        Log.d(TAG, "start getVehicleVacation()");

        RequestBody body = new FormBody.Builder()
                .add("company_id", getCompany_id(context))
                .add("date", date)
                .build();

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/get_data/vehicle_maintain.php")
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //顯示錯誤訊息
                //在app畫面上呈現錯誤訊息
                Looper.prepare();
                runOnUiThread(() -> Toast.makeText(context, "連線失敗", Toast.LENGTH_LONG).show());
                Looper.loop();
                Handler handler = new Handler();
                handler.postDelayed(() -> getVehicleVacation(date), 3000);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG,"responseData of getVehicleVocation: "+responseData); //顯示資料
                if(responseData.equals("null")) return;

                try {
                    JSONArray responseArr = new JSONArray(responseData);

                    int i;
                    for (i = 0; i < responseArr.length(); i++) {
                        JSONObject vehicle_vacation = responseArr.getJSONObject(i);
                        if(!vehicle_vacation.has("vehicle_id")) break;
                        Log.i(TAG, "vehicle_vacation:" + vehicle_vacation);
                        cars_text.add(vehicle_vacation.getString("plate_num"));
                        cars.add(Integer.parseInt(vehicle_vacation.getString("vehicle_id")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    if(!responseData.equals("null") && !responseData.equals("function_name not found.")) {
                        runOnUiThread(() -> Toast.makeText(context, "Toast onResponse failed because JSON in getVehicleVacation", Toast.LENGTH_LONG).show());
                    }
                }

                int ii = 0;
                while (lock){
                    if((++ii)%1000000 == 0) Log.d(TAG, "waiting for lock in getVehicleVacation...");
                }
                Log.d(TAG, "getVehicleVacation: staffGroup:"+staffGroup.getChildCount()+", carGroup:"+carGroup.getChildCount());
                setChipCheck(carGroup, cars_text);
            }
        });
    }


    private void setChipCheck(ChipGroup chipGroup, ArrayList<String> items_text){
        for(int i = 1; i < chipGroup.getChildCount(); i++){
            Chip chip = (Chip) chipGroup.getChildAt(i);
            chip.setCheckable(true);
            if(items_text.contains(chip.getText().toString())) chip.setChecked(true); //把本單有的員工列為已點擊
            else chip.setChecked(false);
            chip.setCheckable(false);
        }
    }

    private void globalNav(){
        ImageButton back_btn = findViewById(R.id.back_imgBtn);
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        back_btn.setOnClickListener(v -> {
            if(mCalendar.getVisibility() == View.GONE){
                mCalendar.setVisibility(View.VISIBLE);
                orderList.setVisibility(View.GONE);
            }
            else {
                Intent intent = new Intent(this, System.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
        valuation_btn.setOnClickListener(v -> {
            Intent valuation_intent = new Intent(System_Schedule.this, Valuation.class);
            startActivity(valuation_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
        order_btn.setOnClickListener(v -> {
            Intent order_intent = new Intent(System_Schedule.this, Order.class);
            startActivity(order_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
        calendar_btn.setOnClickListener(v -> {
            Intent calender_intent = new Intent(System_Schedule.this, Calendar.class);
            startActivity(calender_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
        system_btn.setOnClickListener(v -> {
            Intent system_intent = new Intent(System_Schedule.this, System.class);
            startActivity(system_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
        setting_btn.setOnClickListener(v -> {
            Intent setting_intent = new Intent(System_Schedule.this, Setting.class);
            startActivity(setting_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
    }

    @Override
    public void onBackPressed() {
        if(mCalendar.getVisibility() == View.GONE){
            mCalendar.setVisibility(View.VISIBLE);
            orderList.setVisibility(View.GONE);
            initArray();
            getStaffVacation(getToday("yyyy-MM-dd"));
            getVehicleVacation(getToday("yyyy-MM-dd"));
        }
        else{
            super.onBackPressed();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

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
    public class AsyncRetrieve extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void...Void) {
            getStaffVacation(getToday("yyyy-MM-dd"));
            getVehicleVacation(getToday("yyyy-MM-dd"));
            return null;
        }
    }

}
