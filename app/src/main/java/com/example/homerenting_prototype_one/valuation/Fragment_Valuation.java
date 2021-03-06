package com.example.homerenting_prototype_one.valuation;

import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;
import static com.example.homerenting_prototype_one.show.global_function.getEndOfWeek;
import static com.example.homerenting_prototype_one.show.global_function.getMonthStr;
import static com.example.homerenting_prototype_one.show.global_function.getStartOfWeek;
import static com.example.homerenting_prototype_one.show.global_function.getWeek;
import static com.example.homerenting_prototype_one.show.global_function.getwCount;
import static com.example.homerenting_prototype_one.show.global_function.removeNew;
import static com.example.homerenting_prototype_one.show.global_function.setwCount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.adapter.base_adapter.ListAdapter;
import com.example.homerenting_prototype_one.adapter.base_adapter.NoDataAdapter;
import com.example.homerenting_prototype_one.adapter.re_adpater.NoDataRecyclerAdapter;
import com.example.homerenting_prototype_one.adapter.re_adpater.SwipeDeleteAdapter;

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

public class Fragment_Valuation extends Fragment {
    View view;
    ListView valuationList;
    ArrayList<String[]> data = new ArrayList<>();
    ListAdapter listAdapter = new ListAdapter(data);
    ImageButton lastWeek_btn, nextWeek_btn;
    TextView month_text;
    TextView week_text;
    OkHttpClient okHttpClient = new OkHttpClient();
    String TAG = "ValuationFragment";
    Activity mContext ;
    private final String PHP = "/user_data.php";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_valuation, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        month_text = mContext.findViewById(R.id.month_V);
        week_text = mContext.findViewById(R.id.week_V);
        valuationList = view.findViewById(R.id.listViewValuation);

        getValuation();


    }
    private void getValuation(){
        //????????????????????????function_name
        String function_name = "self_valuation_member";
        String startDate =  getStartOfWeek();
        String endDate = getEndOfWeek();
        String company_id = getCompany_id(mContext);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id",company_id)
                .add("startDate", startDate)
                .add("endDate", endDate)
                .build();
        Log.i(TAG, "getValuation: company_id: "+company_id+", startDate:"+startDate+", endDate:"+endDate+", status:"+"self");

        //????????????
        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL + PHP)
                .post(body)
                .build();
        //http://54.166.177.4/user_data.php

        //??????
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            //????????????
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //??????????????????
                mContext.runOnUiThread(() -> {
                    //???app???????????????????????????
                    Toast.makeText(mContext, "????????????", Toast.LENGTH_LONG).show();
                });
                Looper.prepare();
                Looper.loop();
                Handler handler = new Handler();
                handler.postDelayed(() -> getValuation(), 3000);
            }

            //????????????
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG,"responseData: "+responseData); //????????????

                try {
                    //?????????json?????????array???object
                    final JSONArray responseArr = new JSONArray(responseData);

                    //??????????????????JSONArray??????json??????
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject member = responseArr.getJSONObject(i);
                        Log.d(TAG,"member:" + member);

                        //???????????????
                        final String order_id = member.getString("order_id");
                        final String name = member.getString("member_name");
                        final String nameTitle;
                        if(member.getString("gender").equals("???")) nameTitle = "??????";
                        else nameTitle = "??????";
                        final String phone = member.getString("phone");
                        final String contact_address = member.getString("contact_address");
                        String auto = member.getString("auto");
                        final String newicon = member.getString("new");
                        String plan = member.getString("plan") ;
                        String[] row_data = {order_id, name, nameTitle, phone, contact_address, auto, newicon, plan};
                        data.add(row_data);
                    }
                } catch (JSONException e) { //??????????????????????????????json??????????????????????????????json??????
                    e.printStackTrace();
                    mContext.runOnUiThread(() -> {
                        if(responseData.equals("null")){
                            NoDataAdapter noData = new NoDataAdapter();
                            valuationList.setAdapter(noData);
                        }
                        //else Toast.makeText(Valuation.this, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show();
                    });
                }

                //????????????
                if(!responseData.equals("null")){
                    for(int i = 0; i < data.size(); i++)
                        Log.i(TAG, "data: "+ Arrays.toString(data.get(i)));
                    mContext.runOnUiThread(() -> {
                        valuationList.setAdapter(listAdapter);
                        valuationList.setOnItemClickListener((parent, view, position, id) -> {
                            String[] row_data = (String[])parent.getItemAtPosition(position);
                            Log.d(TAG, "row_data: "+ Arrays.toString(row_data));
                            String order_id = row_data[0];
                            String plan = row_data[7];
                            Bundle bundle = new Bundle();
                            bundle.putString("order_id", order_id);
                            bundle.putString("plan", plan);
                            removeNew(order_id, mContext,plan);

                            Intent intent = new Intent();
                            intent.setClass(mContext, Valuation_Detail.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        });
                    });
                }

            }
        });
    }

}