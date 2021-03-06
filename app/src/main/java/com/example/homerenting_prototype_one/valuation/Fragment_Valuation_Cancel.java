package com.example.homerenting_prototype_one.valuation;

import static com.example.homerenting_prototype_one.show.global_function.clearDatalist;
import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;
import static com.example.homerenting_prototype_one.show.global_function.getEndOfWeek;
import static com.example.homerenting_prototype_one.show.global_function.getStartOfWeek;
import static com.example.homerenting_prototype_one.show.global_function.removeNew;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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


public class Fragment_Valuation_Cancel extends Fragment {
    View view;
    Activity mContext;
    ListView valuationCancelList;

    ArrayList<String[]> data = new ArrayList<>();

    OkHttpClient okHttpClient = new OkHttpClient();

    String TAG = "Valuation_CancelFragment";
    String plan;
    private final String PHP = "/user_data.php";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       mContext = getActivity();
       valuationCancelList = view.findViewById(R.id.listView_ValuationCancel);
        new AsyncRetrieve().execute();
       //getValuationCancel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_valuation__cancel, container, false);
        return view;
    }

    private void getValuationCancel(){
        clearDatalist();
        //?????????????????????
        String function_name = "cancel_valuation_member";
        String startDate =  getStartOfWeek();
        String endDate = getEndOfWeek();
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", getCompany_id(mContext))
                .add("startDate", startDate)
                .add("endDate", endDate)
                .add("status", "cancel")
                .build();

        //????????????
        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+PHP)
                .post(body)
                .build();
        //http://54.166.177.4/user_data.php

        //??????
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                mContext.runOnUiThread(() -> Toast.makeText(mContext, "????????????", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                //Log.d(TAG, "responseData" + responseData);

                try {
                    final JSONArray responseArr = new JSONArray(responseData);
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject member = responseArr.getJSONObject(i);
                        Log.d(TAG, "member: " + member);

                        String order_id = member.getString("order_id");
                        String name = member.getString("member_name");
                        String nameTitle;
                        if(member.getString("gender").equals("???")) nameTitle = "??????";
                        else nameTitle = "??????";
                        String phone = member.getString("phone");
                        String contact_address = member.getString("contact_address");
                        if(contact_address.equals("null")) contact_address = member.getString("outcity")
                                +member.getString("outdistrict")
                                +member.getString("address1");
                        String auto = member.getString("auto");
                        String newicon = member.getString("new");
                        plan = member.getString("plan");
                        String[] row_data = {order_id, name, nameTitle, phone, contact_address, auto, newicon, "cancel"};
                        data.add(row_data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mContext.runOnUiThread(() -> {
                        if(responseData.equals("null")){
                            NoDataAdapter noData = new NoDataAdapter();
                            valuationCancelList.setAdapter(noData);
                        }
                        //else Toast.makeText(Valuation_Cancel.this, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show();
                    });
                }

                //????????????
                if(!responseData.equals("null")) {
                    for (int i = 0; i < data.size(); i++)
                        Log.i(TAG, "data: " + Arrays.toString(data.get(i)));
                    final ListAdapter listAdapter = new ListAdapter(data);
                    mContext.runOnUiThread(() -> {
                        valuationCancelList.setAdapter(listAdapter);
                        valuationCancelList.setOnItemClickListener((parent, view, position, id) -> {
                            String[] row_data = (String[]) parent.getItemAtPosition(position);
                            Log.d(TAG, "row_data: " + Arrays.toString(row_data));
                            String order_id = row_data[0];
                            Bundle bundle = new Bundle();
                            bundle.putString("order_id", order_id);
                            bundle.putString("plan", plan);
                            removeNew(order_id, mContext,plan);

                            Intent intent = new Intent();
                            intent.setClass(mContext, ValuationCancel_Detail.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        });
                    });
                }
            }
        });
    }
    public class AsyncRetrieve extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... Void) {
            getValuationCancel();
            return null;
        }
    }
}