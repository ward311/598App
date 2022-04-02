package com.example.homerenting_prototype_one.valuation;

import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.adapter.base_adapter.LocationAdapter;
import com.example.homerenting_prototype_one.adapter.base_adapter.NoDataAdapter;
import com.example.homerenting_prototype_one.order.Confirm_Detail;

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

public class ConfirmValuation_Detail extends AppCompatActivity {

    String TAG = "ConfirmValuation_Booking_Detail";
    String PHP3 = "/furniture.php";
    Context context = ConfirmValuation_Detail.this;
    Bundle bundle;
    TextView nameText, nameTitleText, phoneText, contactTimeText, valuationTimeText;
    TextView fromAddressText, toAddressText, remainderText;
    TextView movingDateText, estimateTimeText, valPriceText, feeText;
    TextView valRangeText, newValPriceText;
    TextView program_type;
    TextView current_discount;
    EditText carsText;
    Button check;
    EditText memoEdit;
    ListView list;
    String order_id;
    String floor;
    String room_name;
    String email, plan, isAuto;
    String demandCar;
    ArrayList<String[]> data = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_valuation_detail);
        bundle = getIntent().getExtras();
        nameText = findViewById(R.id.conVal_name_VBD);
        nameTitleText = findViewById(R.id.conVal_nameTitle_VBD);
        phoneText = findViewById(R.id.conVal_phone_VBD);
        contactTimeText = findViewById(R.id.conVal_contactTime_VBD);
        valuationTimeText = findViewById(R.id.conVal_valuationTime_VBD);
        fromAddressText = findViewById(R.id.conVal_FromAddress_VBD) ;
        toAddressText = findViewById(R.id.conVal_ToAddress_VBD);
        remainderText = findViewById(R.id.conVal_notice_VBD);
        movingDateText = findViewById(R.id.conVal_movingDate);
        estimateTimeText = findViewById(R.id.conVal_estTime);
        valPriceText = findViewById(R.id.conVal_valPrice_VBD);
        valRangeText = findViewById(R.id.conVal_range_text);
        feeText = findViewById(R.id.conVal_price);
        program_type = findViewById(R.id.conVal_setProgram);
        current_discount = findViewById(R.id.conVal_cur_dis);
        memoEdit = findViewById(R.id.conVal_PS_VBD);
        newValPriceText = findViewById(R.id.newValPrice_VBD);
        check = findViewById(R.id.conVal_check_evaluation_btn);
        list = findViewById(R.id.conVal_location);
        carsText = findViewById(R.id.conVal_carDemand);
        carsText.setSingleLine(false);


        ImageView back_btn = findViewById(R.id.conVal_back_imgBtn_VBD);
        setBundle();

        back_btn.setOnClickListener(v -> this.finish());

        getFurniture();
        new Handler().postDelayed(() -> getVehicleDemandData(),500);

        check.setOnClickListener(v -> {
            updateValuation(bundle.getString("moving_date"), bundle.getString("estimate_worktime"), bundle.getString("fee"));
            finishValuation();
        });

    }

    private void setBundle(){
        order_id = bundle.getString("order_id");
        plan = bundle.getString("plan");
        email = bundle.getString("email");
        isAuto = bundle.getString("isAuto");
        nameText.setText(bundle.getString("name"));
        nameTitleText.setText(bundle.getString("gender"));
        program_type.setText(bundle.getString("program"));
        phoneText.setText(bundle.getString("phone"));
        contactTimeText.setText(bundle.getString("contactTime"));
        valuationTimeText.setText(bundle.getString("valuationTime"));
        fromAddressText.setText(bundle.getString("fromAddress"));
        toAddressText.setText(bundle.getString("toAddress"));
        remainderText.setText(bundle.getString("remainder"));
        movingDateText.setText(bundle.getString("moving_date"));
        estimateTimeText.setText(bundle.getString("estimate_worktime"));
        valPriceText.setText(bundle.getString("valPrice"));
        if(valPriceText.getText().equals("現場估價")) valPriceText.setTextColor(Color.RED);
        valRangeText.setText(bundle.getString("valRange"));
        if(valRangeText.getText().equals("無價格區間")) valRangeText.setTextColor(Color.RED);
        newValPriceText.setText(bundle.getString("newPrice"));
        if(newValPriceText.getText().length()!=0){
           valPriceText.setPaintFlags(valPriceText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        feeText.setText(bundle.getString("fee"));
        current_discount.setText(bundle.getString("discount"));
        memoEdit.setText(bundle.getString("memo"));

    }
    private void updateValuation(String moving_date, String estimate_worktime, String fee){
        String function_name = "update_bookingValuation";
        String company_id = getCompany_id(context);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .add("company_id",company_id)
                .add("moving_date",moving_date+":00")
                .add("estimate_worktime", estimate_worktime)
                .add("fee", fee)
                .add("plan", plan)
                .build();
        Log.d(TAG, "check_btn: order_id: "+order_id+", moving_date:  "+moving_date+":00"+
                ", estimate_worktime: "+estimate_worktime+", fee: "+fee+", plan: "+plan);

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/functional.php")
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

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                runOnUiThread(() -> Toast.makeText(context, "估價單已完成", Toast.LENGTH_LONG).show());
                Log.d(TAG, "submit update_valuation responseData: " + responseData);
            }
        });
    }

    private void finishValuation(){
        String function_name = "update_Valuation_Done";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .add("company_id", getCompany_id(context))
                .add("plan", plan)
                .build();
        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/functional.php")
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(context, "email send failure", Toast.LENGTH_LONG).show());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, "responseData: " + responseData);
                try{
                    JSONObject finish = new JSONObject(responseData);
                    if(finish.getString("status").equals("success")){
                        if(isAuto.equals("1")){
                            sendEmail();
                        }else{
                            Looper.prepare();
                            callDialog();
                            Looper.loop();
                        }

                    }else{
                        runOnUiThread(() -> Toast.makeText(context,"Valuation done status failed",Toast.LENGTH_LONG).show());
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendEmail(){
        RequestBody body = new FormBody.Builder()
                .add("email", email)
                .add("order_id", order_id)
                .add("plan", plan)
                .build();
        Log.i(TAG, "email has been sent to: " + email);

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/sendmail.php")
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(context, "email send failure", Toast.LENGTH_LONG).show());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, "responseData: " + responseData);
                try{
                    JSONObject sending = new JSONObject(responseData);
                    if(sending.getString("status").equals("Email Sent")){
                        runOnUiThread(() -> new AlertDialog.Builder(context)
                                .setTitle("通知信件")
                                .setMessage("\n        到府估價完成通知已寄出給客戶")
                                .setPositiveButton( "確認", (dialog, which) -> callDialog())
                                .show());
                    }else{
                        runOnUiThread(() -> runOnUiThread(() -> new AlertDialog.Builder(context)
                                .setTitle("通知信件")
                                .setMessage("\n        到府估價完成通知寄出失敗")
                                .setPositiveButton( "確認", (dialog, which) -> callDialog())
                                .show()));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void callDialog(){
        new AlertDialog.Builder(context)
                .setTitle("媒合中")
                .setMessage("到府估價單媒合中，成功媒\n合會成為訂單，請公司注意\n新訂單通知。")
                .setPositiveButton( "確認", (dialog, which) -> {
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        Intent intent = new Intent(context, Valuation.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }, 1000);
                })
                .show();
    }

    private void getVehicleDemandData(){
        String company_id = getCompany_id(this);
        RequestBody body = new FormBody.Builder()
                .add("order_id", order_id)
                .add("company_id",company_id)
                .build();

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/get_data/vehicle_demand_data.php")
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d("Fail", "Failed: " + e.getMessage()); //顯示錯誤訊息
                //在app畫面上呈現錯誤訊息
                runOnUiThread(() -> Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show());
                Handler handler = new Handler();
                Looper.prepare();
                handler.postDelayed(() -> getVehicleDemandData(), 3000);
                Looper.loop();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG, "responseData of vehicle_demand_data: " + responseData); //顯示資料


                demandCar = "無填寫需求車輛";
                try {
                    JSONArray responseArr = new JSONArray(responseData);

                    int i;
                    demandCar = "";
                    for (i = 0; i < responseArr.length(); i++) {
                        JSONObject vehicle_demand = responseArr.getJSONObject(i);
                        if(!vehicle_demand.has("num")) break;
                        Log.i(TAG, "vehicle_demand:" + vehicle_demand);
                        if(i != 0) demandCar = demandCar + "\n";
                        demandCar = demandCar+vehicle_demand.getString("vehicle_weight")
                                +vehicle_demand.getString("vehicle_type")
                                +vehicle_demand.getString("num")+"輛";
                    }
                    Log.d(TAG, "demandCar: "+demandCar);

                } catch (JSONException e) {
                    if(!responseData.equals("null")) e.printStackTrace();
                }

                runOnUiThread(() -> carsText.setText(demandCar));
            }
        });
    }

    private void getFurniture(){
        String function_name = "furniture_web_room_detail";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .add("company_id", getCompany_id(context))
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
                runOnUiThread(() -> Toast.makeText(ConfirmValuation_Detail.this, "連線失敗", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG,"responseData: "+responseData);

                try {
                    final JSONArray responseArr = new JSONArray(responseData);

                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject furniture = responseArr.getJSONObject(i);
                        if(!furniture.getString("room_id").equals("null")) {
                            floor = furniture.getString("floor");
                            room_name = furniture.getString("room_type") + furniture.getString("room_name");
                        }
                        else{
                            floor = "";
                            room_name = furniture.getString("space_type");
                        }
                        final String furniture_name = furniture.getString("furniture_name");
                        final String num = furniture.getString("num");
                        if(num.equals("0")) break;
                        String[] row_data = {floor,room_name,furniture_name,num};
                        data.add(row_data);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    if(responseData.equals("null")){
                        runOnUiThread(() -> {
                            Log.d(TAG, "NO DATA");
                            NoDataAdapter noData = new NoDataAdapter();
                            list.setAdapter(noData);
                        } );
                    }
                    //else Toast.makeText(context, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show();
                }
                //顯示資訊
                for(int i=0; i < data.size(); i++)
                    Log.i(TAG, "data: "+ Arrays.toString(data.get(i)));
                final LocationAdapter LocationAdapter = new LocationAdapter(data);
                runOnUiThread(() -> list.setAdapter(LocationAdapter));

            }
        });
    }
}