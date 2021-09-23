package com.example.homerenting_prototype_one.order;

import static com.example.homerenting_prototype_one.show.global_function.addDatalist;
import static com.example.homerenting_prototype_one.show.global_function.clearDatalist;
import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;
import static com.example.homerenting_prototype_one.show.global_function.getDate;
import static com.example.homerenting_prototype_one.show.global_function.getEndOfWeek;
import static com.example.homerenting_prototype_one.show.global_function.getMonthStr;
import static com.example.homerenting_prototype_one.show.global_function.getStartOfWeek;
import static com.example.homerenting_prototype_one.show.global_function.getTime;
import static com.example.homerenting_prototype_one.show.global_function.getWeek;
import static com.example.homerenting_prototype_one.show.global_function.removeNew;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.adapter.base_adapter.ListAdapter;
import com.example.homerenting_prototype_one.adapter.base_adapter.NoDataAdapter;

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

public class Fragment_Order_Cancel extends Fragment {
    View view;
    Activity mContext;
    ArrayList<String[]> data;
    ListView orderList;
    OkHttpClient okHttpClient = new OkHttpClient();
    String TAG = "Order_CancelFragment";
    private final String PHP = "/user_data.php";
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        orderList = view.findViewById(R.id.listView_OrderCancel);
        data = new ArrayList<>();

        new AsyncRetrieve().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment__order__cancel, container, false);
        return view;
    }

    private void getOrder(){
        clearDatalist();
        //傳至網頁的值，傳function_name
        String function_name = "order_member";
        String startDate =  getStartOfWeek();
        String endDate = getEndOfWeek();
        String company_id = getCompany_id(mContext);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", company_id)
                .add("startDate", startDate)
                .add("endDate", endDate)
                .add("status", "cancel")
                .build();
        Log.i(TAG, "getOrder:\n"+"startDate:"+startDate+", endDate:"+endDate+", status:"+"scheduled");

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
                mContext.runOnUiThread(() -> {
                    //在app畫面上呈現錯誤訊息
                    Toast.makeText(mContext, "Toast onFailure.", Toast.LENGTH_LONG).show();
                });
            }

            //連線成功
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG,"responseData: "+responseData); //顯示資料

                try {
                    //轉換成json格式，array或object
                    final JSONArray responseArr = new JSONArray(responseData);
                    Log.d(TAG,"responseObj: "+ responseArr);

                    //一筆一筆的取JSONArray中的json資料
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject member = responseArr.getJSONObject(i);
                        Log.d(TAG,"member: "+member);

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

                        //將資料放入陣列
                        String[] row_data = {order_id, getDate(datetime), getTime(datetime), name, nameTitle, phone, contact_address, auto, newicon, "cancel"};
                        data.add(row_data);
                        addDatalist(order_id);
                    }
                } catch (JSONException e) { //會到這裡通常表示用錯json格式或網頁的資料不是json格式
                    e.printStackTrace();
                    mContext.runOnUiThread(() -> {
                        if(responseData.equals("null")){
                            NoDataAdapter noData = new NoDataAdapter();
                            orderList.setAdapter(noData);
                        }
                        //else Toast.makeText(Order_Cancel.this, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show();
                    });
                }
                //顯示資訊
                  if(!responseData.equals("null")) {
                    for (int i = 0; i < data.size(); i++)
                        Log.i(TAG, "data: " + Arrays.toString(data.get(i)));
                    final ListAdapter listAdapter = new ListAdapter(data);
                    mContext.runOnUiThread(() -> {
                        orderList.setAdapter(listAdapter);
                        orderList.setOnItemClickListener((parent, view, position, id) -> {
                            String[] row_data = (String[]) parent.getItemAtPosition(position);
                            Log.d(TAG, "row_data: " + Arrays.toString(row_data));
                            String order_id = row_data[0];

                            Bundle bundle = new Bundle();
                            bundle.putString("order_id", order_id);
                            bundle.putBoolean("btn", true);

                            removeNew(order_id, mContext);

                            Intent intent = new Intent();
                            intent.setClass(mContext, Order_Detail.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        });
                    });
                }
            }
        });
    }
    public class AsyncRetrieve extends AsyncTask<String, String, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            getOrder();
            return null;
        }
    }
}