package com.example.homerenting_prototype_one.order;

import static com.example.homerenting_prototype_one.show.global_function.changeStat;
import static com.example.homerenting_prototype_one.show.global_function.changeStatus;
import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;
import static com.example.homerenting_prototype_one.show.global_function.getDate;
import static com.example.homerenting_prototype_one.show.global_function.getDay;
import static com.example.homerenting_prototype_one.show.global_function.getMonth;
import static com.example.homerenting_prototype_one.show.global_function.getTime;
import static com.example.homerenting_prototype_one.show.global_function.getYear;

import android.app.Activity;
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
import android.widget.Toast;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
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


public class Fragment_Order_Today extends Fragment {
    View view;
    Activity mContext;
    RecyclerView orderRList;
    String TAG = "Order_TodayFragment";
    ArrayList<String[]> data;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        orderRList = view.findViewById(R.id.OrderTodayRecycler);
        data = new ArrayList<>();

        String function_name = "order_member_today";
        String company_id = getCompany_id(mContext);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", company_id)
                .build();

        //連線要求
        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/user_data.php")
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
                mContext.runOnUiThread(() -> Toast.makeText(mContext, "Toast onFailure.", Toast.LENGTH_LONG).show());
            }

            //連線成功
            @RequiresApi(api = Build.VERSION_CODES.O)
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
                        String plan = member.getString("plan");
                        if(status.equals("done")) status = "done_today";

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
                            changeStat(order_id, "orders", "cancel", mContext);
                            continue;
                        }

                        //將資料放入陣列
                        String[] row_data = {order_id, getDate(datetime), getTime(datetime), name, nameTitle, phone, contact_address, auto, newicon, status, plan};
                        data.add(row_data);
                    }
                } catch (JSONException e) { //會到這裡通常表示用錯json格式或網頁的資料不是json格式
                    e.printStackTrace();
                    mContext.runOnUiThread(() -> {
                        if(responseData.equals("null")){
                            Log.d(TAG, "NO DATA");
                            NoDataRecyclerAdapter noDataAdapter = new NoDataRecyclerAdapter();
                            orderRList.setLayoutManager(new LinearLayoutManager(mContext));
                            orderRList.setAdapter(noDataAdapter);
                        }
                        //else Toast.makeText(Order_Booking.this, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment__order__today, container, false);
        return view;
    }

    private void setRList(){
        final SwipeDeleteAdapter adapter = new SwipeDeleteAdapter(mContext, data, Today_Detail.class);
        mContext.runOnUiThread(() -> {
            orderRList.setLayoutManager(new LinearLayoutManager(mContext));
            orderRList.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)); //分隔線
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
}