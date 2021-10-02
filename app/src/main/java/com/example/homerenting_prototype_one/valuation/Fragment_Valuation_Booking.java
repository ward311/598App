package com.example.homerenting_prototype_one.valuation;

import static com.example.homerenting_prototype_one.show.global_function.changeStatus;
import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;
import static com.example.homerenting_prototype_one.show.global_function.getDate;
import static com.example.homerenting_prototype_one.show.global_function.getDay;
import static com.example.homerenting_prototype_one.show.global_function.getEndOfWeek;
import static com.example.homerenting_prototype_one.show.global_function.getMonth;
import static com.example.homerenting_prototype_one.show.global_function.getMonthStr;
import static com.example.homerenting_prototype_one.show.global_function.getStartOfWeek;
import static com.example.homerenting_prototype_one.show.global_function.getStartTime;
import static com.example.homerenting_prototype_one.show.global_function.getWeek;
import static com.example.homerenting_prototype_one.show.global_function.getYear;
import static com.example.homerenting_prototype_one.show.global_function.getwCount;
import static com.example.homerenting_prototype_one.show.global_function.setwCount;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.adapter.base_adapter.ListAdapter;
import com.example.homerenting_prototype_one.adapter.re_adpater.NoDataRecyclerAdapter;
import com.example.homerenting_prototype_one.adapter.re_adpater.SwipeDeleteAdapter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Fragment_Valuation_Booking extends Fragment {
    View view;
    RecyclerView valuationBookingList;
    TextView month_text;
    TextView week_text;
    ImageButton lastWeek_btn, nextWeek_btn;
    ArrayList<String[]> data = new ArrayList<>();
    ListAdapter listAdapter = new ListAdapter(data);

    OkHttpClient okHttpClient = new OkHttpClient();

    String TAG = "Valuation_Booking";
    Activity mContext;
    private final String PHP = "/user_data.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_valuation_booking, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        valuationBookingList = view.findViewById(R.id.valuationBookingRecycler);

        getValuationBooking();

    }

    private void getValuationBooking(){
        //傳至網頁的值，傳function_name
        String function_name = "valuation_member";
        String startDate =  getStartOfWeek();
        String endDate = getEndOfWeek();
        String company_id = getCompany_id(mContext);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id",company_id)
                .add("startDate", startDate)
                .add("endDate", endDate)
                .add("status", "booking")
                .build();
        //http://54.166.177.4/user_data.php

        //連線要求
        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+PHP)
                .post(body)
                .build();

        //連線
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            //連線失敗
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //顯示錯誤訊息
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //在app畫面上呈現錯誤訊息
                        Toast.makeText(mContext, "Toast onFailure.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            //連線成功
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG,"responseData: "+responseData); //顯示資料

                try {
                    //轉換成json格式，array或object
                    final JSONArray responseArr = new JSONArray(responseData);
                    //final JSONObject responseObj = new JSONObject(responseData);
                    //Log.d(TAG,"responseObj: "+ responseArr);

                    //一筆一筆的取JSONArray中的json資料
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject member = responseArr.getJSONObject(i);
//                        Log.d(TAG,"member:"+member);

                        //取欄位資料
                        String order_id = member.getString("order_id");
                        String date = member.getString("valuation_date");
                        String time = member.getString("valuation_time");
                        if(time.equals("null")) time = "";
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
                        Log.d(TAG, "order: "+Integer.parseInt(getYear(date))+"-"+Integer.parseInt(getMonth(date))+"-"+Integer.parseInt(getDay(date)));
                        if(Integer.parseInt(getYear(date))<now.getYear() ||
                                (Integer.parseInt(getYear(date))<=now.getYear() &&
                                        Integer.parseInt(getMonth(date))<monthToInt(String.valueOf(now.getMonth()))) ||
                                (Integer.parseInt(getYear(date))<=now.getYear() &&
                                        Integer.parseInt(getMonth(date))<=monthToInt(String.valueOf(now.getMonth())) &&
                                        Integer.parseInt(getDay(date))<now.getDayOfMonth())) {
                            Log.d(TAG, "valuation_date "+date+" of order_id "+order_id+" is over time");
                            changeStatus(order_id, "choose", "cancel", mContext);
                            continue;
                        }

                        //呈現在app上
                        String[] row_data = {order_id, getDate(date), getStartTime(time), name, nameTitle, phone, contact_address, auto, newicon};
                        data.add(row_data);
                    }
                } catch (JSONException e) { //會到這裡通常表示用錯json格式或網頁的資料不是json格式
                    e.printStackTrace();
                    mContext.runOnUiThread(() -> {
                        if(responseData.equals("null")){
                            Log.d(TAG, "NO DATA");
                            NoDataRecyclerAdapter noDataAdapter = new NoDataRecyclerAdapter();
                            valuationBookingList.setLayoutManager(new LinearLayoutManager(mContext));
                            valuationBookingList.setAdapter(noDataAdapter);
                        }
                        //else Toast.makeText(Valuation_Booking.this, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show();
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
        final SwipeDeleteAdapter adapter = new SwipeDeleteAdapter(mContext, data, ValuationBooking_Detail.class);
        adapter.type = "Valuation";
        mContext.runOnUiThread(() -> {
            valuationBookingList.setLayoutManager(new LinearLayoutManager(mContext));
            valuationBookingList.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)); //分隔線
            valuationBookingList.setAdapter(adapter);

            //側滑刪除
//                ItemTouchHelper helper = new ItemTouchHelper(new RecyclerViewAction(context, adapter));
//                helper.attachToRecyclerView(valuationBookingList);
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
}