package com.example.homerenting_prototype_one.furniture;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.Calendar;
import com.example.homerenting_prototype_one.LocationAdapter;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.Setting;
import com.example.homerenting_prototype_one.System;
import com.example.homerenting_prototype_one.adapter.ListAdapter;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.order.Order_Booking;
import com.example.homerenting_prototype_one.order.Order_Detail;
import com.example.homerenting_prototype_one.order.Today_Detail;
import com.example.homerenting_prototype_one.valuation.Valuation;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.homerenting_prototype_one.show.show_data.getDate;
import static com.example.homerenting_prototype_one.show.show_data.getTime;

public class Furniture_Location extends AppCompatActivity {

    String TAG = "Furniture_Location";
    String PHP3 = "/furniture.php";


//    private ListView location_list;
    ArrayList<String[]> data;

//    private Context context;
//    private List<String[]> location_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_furniture__location );
        ImageButton back_btn = findViewById( R.id.back_imgBtn );
        final ListView location_list = findViewById( R.id.furniture_location_listView);
        Button check_btn = findViewById(R.id.check_furniture_location_btn);
        ImageButton valuation_btn = findViewById(R.id.valuation_imgBtn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);
//        location_data = new ArrayList<>();
//        location_data.add( "單人沙發" );
//        location_data.add( "兩人沙發" );
//        location_data.add( "三人沙發" );
//        location_data.add( "按摩椅" );
//        location_data.add( "電視" );
//        location_data.add( "單人沙發" );
//        location_data.add( "兩人沙發" );
//        location_data.add( "三人沙發" );
//        location_data.add( "按摩椅" );
//        location_data.add( "電視" );

        Bundle location_bundle = getIntent().getExtras();
        final String key = location_bundle.getString("key");
        String order_id = location_bundle.getString("order_id");
        Log.d(TAG, "key: "+key+", order_id: "+order_id);


        data = new ArrayList<>();

//        ArrayList<String[]> datas = new ArrayList<>();
//        for(int i = 0; i < 2; i++){
//            String[] row_data = {"test"+(i+1), String.valueOf(i+1)};
//            datas.add(row_data);
//        }
//        data.add(datas.get(0));
//        data.add(datas.get(1));

//        LocationAdapter location_adapter = new LocationAdapter( location_data );
//        location_list.setAdapter( location_adapter );

        String function_name = "furniture_room_detail";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .build();
        Log.d(TAG, "order_id:"+order_id);

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+PHP3)
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Furniture_Location.this, "Toast onFailure.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG,"responseData: "+responseData);

                try {
                    final JSONArray responseArr = new JSONArray(responseData);

                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject member = responseArr.getJSONObject(i);
                        final String floor = member.getString("floor");
                        final String room_name = member.getString("room_name");
                        final String furniture_name = member.getString("furniture_name");
                        final String num = member.getString("num");
                        String[] row_data = {floor,room_name,furniture_name,num};
                        data.add(row_data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            show_noData show = new show_noData(Order_Booking.this, orderL.getContext());
//                            if(responseData.equals("null")) orderL.addView(show.noDataMessage());
//                            else Toast.makeText(Order_Booking.this, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show();
//                        }
//                    });
                }

                //顯示資訊
                for(int i=0; i < data.size(); i++)
                    Log.i(TAG, "data: "+ Arrays.toString(data.get(i)));
//                final ListView locationList = findViewById(R.id.furniture_location_listView);
                final LocationAdapter LocationAdapter = new LocationAdapter(data);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        location_list.setAdapter(LocationAdapter);
                    }
                });

            }
        });


        back_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (key.matches( "order" )){
                    Intent back_intent = new Intent(Furniture_Location.this, Order_Detail.class);
                    startActivity( back_intent );
                }
                else if (key.matches( "today" )){
                    Intent back_intent = new Intent(Furniture_Location.this, Today_Detail.class);
                    startActivity( back_intent );
                }
            }
        } );
        check_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (key.matches( "order" )){
                    Intent back_intent = new Intent(Furniture_Location.this,Order_Detail.class);
                    startActivity( back_intent );
                }
                else if (key.matches( "today" )){
                    Intent back_intent = new Intent(Furniture_Location.this,Today_Detail.class);
                    startActivity( back_intent );
                }
            }
        } );
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Furniture_Location.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Furniture_Location.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Furniture_Location.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Furniture_Location.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Furniture_Location.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
