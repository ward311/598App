package com.example.homerenting_prototype_one.bouns;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.adapter.re_adpater.DistributionAdapter;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.setting.Setting;
import com.example.homerenting_prototype_one.system.System;
import com.example.homerenting_prototype_one.valuation.Valuation;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;
import static com.example.homerenting_prototype_one.show.global_function.getDate;
import static com.example.homerenting_prototype_one.show.global_function.getTime;

public class Distribution_Detail extends AppCompatActivity {
    TextView nameText, nameTitleText, fromAddressText, toAddressText, movingTimeText, feeText;
    RecyclerView salaryDistribution;
    ImageButton backBtn;
    Button checkBtn;

    Bundle bundle;

    private DistributionAdapter distributionAdapter;

    private String order_id, name, gender, movingTime, fromAddress, toAddress, fee;
    private String TAG = "Distribution_Detail";

    private ArrayList<String> staffs, staffIds;
    private ArrayList<Integer> salaries;
    private String salaryStr;
    int net;
    boolean lock = false;

    private Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distribution__detail);

        nameText = findViewById(R.id.member_name_DD);
        nameTitleText = findViewById(R.id.nameTitle_DD);
        fromAddressText = findViewById(R.id.fromAddress_DD);
        toAddressText = findViewById(R.id.toAddress_DD);
        movingTimeText = findViewById(R.id.movingTime_DD);
        feeText = findViewById(R.id.price_DD);
        salaryDistribution = findViewById(R.id.salaryDistribution_DD);
        backBtn = findViewById(R.id.back_imgBtn);
        checkBtn = findViewById(R.id.check_bonus_btn);


        bundle = getIntent().getExtras();
        order_id = bundle.getString("order_id");

        staffs = new ArrayList<>();
        staffIds = new ArrayList<>();
        salaries = new ArrayList<>();

        //傳值
        getData();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        globalNav();
    }

    private void getData(){
        String function_name = "order_detail";
        String company_id = getCompany_id(this);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .add("company_id", company_id)
                .build();
        Log.d(TAG, "order_id:"+order_id);

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/user_data.php")
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //顯示錯誤訊息
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //在app畫面上呈現錯誤訊息
                        Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG,"responseData: "+responseData); //顯示資料

                try {
                    JSONArray responseArr = new JSONArray(responseData);
                    JSONObject order = responseArr.getJSONObject(0);
                    Log.i(TAG,"order:"+order);

                    //取得資料
                    name = order.getString("member_name");
                    gender = order.getString("gender");
                    movingTime = getDate(order.getString("moving_date"))+" "+getTime(order.getString("moving_date"));
                    fromAddress = order.getString("from_address");
                    toAddress = order.getString("to_address");
                    fee = order.getString("accurate_fee");

                    int i;
                    //跳過車輛
                    for (i = 1; i < responseArr.length(); i++) {
                        JSONObject vehicle_assign = responseArr.getJSONObject(i);
                        if(!vehicle_assign.has("vehicle_type")) break;
                    }

                    //取得員工資訊
                    for (; i < responseArr.length(); i++) {
                        JSONObject staff_assign = responseArr.getJSONObject(i);
                        if(!staff_assign.has("staff_id")) break;
                        Log.i(TAG, "staff:" + staff_assign);
                        staffs.add(staff_assign.getString("staff_name"));
                        staffIds.add(staff_assign.getString("staff_id"));

                        String pay = staff_assign.getString("pay");
                        salaries.add(Integer.parseInt(pay));
                    }

                    //顯示基本資訊
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            nameText.setText(name);
                            if(gender.equals("female")) nameTitleText.setText("小姐");
                            else if(gender.equals("male")) nameTitleText.setText("先生");
                            else nameTitleText.setText("");
                            movingTimeText.setText(movingTime);
                            fromAddressText.setText(fromAddress);
                            toAddressText.setText(toAddress);
                            feeText.setText(fee);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //顯示安排人力
                distributionAdapter = new DistributionAdapter(staffs);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        salaryDistribution.setLayoutManager(new LinearLayoutManager(context));
                        salaryDistribution.setAdapter(distributionAdapter);
                        Log.d(TAG, "setAdapter");
                    }
                });
                while(distributionAdapter.getReady() != -1){ //lock
                    Log.d(TAG, "ready: "+distributionAdapter.getReady());
                }
                for(int i = 0; i < distributionAdapter.getItemCount(); i++){
                    getItem(i); //取得分配薪水的edittext
                }
                setCheckBtn(); //設置確認送出按鈕
            }
        });
    }

    private void getItem(final int position){
        View view = salaryDistribution.getLayoutManager().findViewByPosition(position);
        if(view != null){
            Log.d(TAG, "view "+position+" is not null");
            final EditText salaryPEdit = view.findViewById(R.id.salaryP_DI);
            final TextView salaryPText = view.findViewById(R.id.salaryP_text_DI);
            final EditText salaryEdit = view.findViewById(R.id.salary_DI);
            final TextView salaryText = view.findViewById(R.id.salary_text_DI);

            salaryPEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String salaryPStr = salaryPEdit.getText().toString();
                    if(salaryPStr.isEmpty()) salaryPStr = "0";
                    Log.d(TAG, "(sp) salary(p): "+salaryPStr);

                    int salary = -1;
                    String salaryStr = "";
                    if(!salaryPStr.equals("0")){
                        float salaryP = Float.parseFloat(salaryPStr);
                        salary = Math.round((Integer.parseInt(fee))*salaryP/100);
                        if(salary == 0) salary = -1;
                        else salaryStr = String.valueOf(salary);
                    }
                    salaryText.setText(String.valueOf(salary));
                    salaries.set(position, salary);
                    setFeeText();
                }
            });

            salaryPText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String salaryPStr = salaryPText.getText().toString();
                    salaryPEdit.setText(salaryPStr);
                    salaryStr = salaryEdit.getText().toString();
                    if(salaryStr.isEmpty()) salaryStr = "0";
                    salaryText.setText(salaryStr);

                    salaryPText.setVisibility(View.GONE);
                    salaryPEdit.setVisibility(View.VISIBLE);
                    salaryEdit.setVisibility(View.GONE);
                    salaryText.setVisibility(View.VISIBLE);
                    return false;
                }
            });

            salaryEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int salary = 0;
                    salaryStr = salaryEdit.getText().toString();
                    if(!salaryStr.isEmpty()) salary = Integer.parseInt(salaryStr);
                    Log.d(TAG, "(s) salary: "+salary);
                    salaries.set(position, salary);
                    setFeeText();

                    if(salary == 0) salaryPText.setText("0");
                    else{
                        float salaryP =  ((float) salary/(Integer.parseInt(fee)))*100;
                        NumberFormat nf = NumberFormat.getInstance();
                        nf.setMaximumFractionDigits(2); //只顯示到小數點後兩位
                        String salaryPStr = nf.format(salaryP);
                        Log.d(TAG, (position+1)+". salary percent("+salary+"/"+fee+"): "+salaryPStr);
                        salaryPText.setText(salaryPStr);
                     }
                }
            });

            salaryText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String salaryPStr = salaryPEdit.getText().toString();
                    salaryPText.setText(salaryPStr);
                    salaryStr = salaryText.getText().toString();
                    salaryEdit.setText(salaryStr);

                    salaryPText.setVisibility(View.VISIBLE);
                    salaryPEdit.setVisibility(View.GONE);
                    salaryEdit.setVisibility(View.VISIBLE);
                    salaryText.setVisibility(View.GONE);
                    return false;
                }
            });

            if(salaries.get(position) != -1){
                salaryEdit.setText(String.valueOf(salaries.get(position)));
            }
        }
        else{
            feeText.setText(fee+"("+position+" null)");
            Log.d(TAG, "view "+position+" is null");
        }
    }

    private void setFeeText(){
        int total = 0;
        for(int i = 0; i < salaries.size(); i++){
            if(salaries.get(i) == -1) continue;
            total = total + salaries.get(i);
        }
        if(total > 0) net = Integer.parseInt(fee) - total;
        else net = Integer.parseInt(fee) + total;

        if(total == 0) feeText.setText(fee);
        else{
            double percentage = 100;
            if(net != 0) percentage = (net/(double)Integer.parseInt(fee))*100;
            DecimalFormat df = new DecimalFormat("0.00");
            String p = df.format(percentage);
            if(total > 0) feeText.setText(fee+" - "+total+" = "+net+"("+p+"%)");
            else feeText.setText(fee+" "+total+" = "+net+"("+p+"%)");
        }
    }

    private void setCheckBtn(){
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(net < 0){
                    Toast.makeText(context, "工錢總額大於搬家費用", Toast.LENGTH_SHORT).show();
                }
                else{
                    boolean checkAll = true;
                    for(int i = 0; i < salaries.size(); i++){
                        Log.i(TAG, "salaries("+i+"): "+salaries.get(i));
                        if(salaries.get(i) == -1){
                            checkAll = false;
                            Log.d(TAG, "not complete thd distribution");
                            continue;
                        }
                        update_staff_salary(i);
                    }
                    if(checkAll){
                        Log.d(TAG, "complete, finish the distirbution");
                        changeStatus();
                    }

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(context, Bonus_Distribution.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            Toast.makeText(context, "done", Toast.LENGTH_LONG).show();
                        }
                    }, 1000);
                }
            }
        });
    }

    private void update_staff_salary(final int i){
        String function_name = "distribute_pay";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .add("staff_id", staffIds.get(i))
                .add("pay", String.valueOf(salaries.get(i)))
                .build();
        Log.d(TAG, "order_id: "+order_id+", staff_id: "+staffIds.get(i)+", pay: "+salaries.get(i));

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
                Log.d(TAG, "Failed: " + e.getMessage()); //顯示錯誤訊息
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //在app畫面上呈現錯誤訊息
                        Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG, "responseData of update_staff_salary("+i+"): " + responseData);
            }
        });
    }

    private void changeStatus(){
        String function_name = "change_status";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .add("table", "orders")
                .add("status", "paid")
                .build();
        Log.d(TAG, "order_id: "+order_id+", table: orders, status: paid");

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
                Log.d(TAG, "Failed: " + e.getMessage()); //顯示錯誤訊息
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //在app畫面上呈現錯誤訊息
                        Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG, "responseData of change_status: " + responseData);
            }
        });
    }

    private void globalNav(){
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);
        //底下nav
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Distribution_Detail.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Distribution_Detail.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Distribution_Detail.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Distribution_Detail.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Distribution_Detail.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
