package com.example.homerenting_prototype_one.system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.adapter.base_adapter.NoDataAdapter;
import com.example.homerenting_prototype_one.bouns.Bonus_Distribution;
import com.example.homerenting_prototype_one.bouns.Bonus_List;
import com.example.homerenting_prototype_one.bouns.Bonus_View;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.helper.DatabaseHelper;
import com.example.homerenting_prototype_one.model.TableContract;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.setting.Setting;
import com.example.homerenting_prototype_one.valuation.Valuation;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
import static com.example.homerenting_prototype_one.show.global_function.getDay;
import static com.example.homerenting_prototype_one.show.global_function.getMonth;
import static com.example.homerenting_prototype_one.show.global_function.getTime;
import static com.example.homerenting_prototype_one.show.global_function.getToday;
import static com.example.homerenting_prototype_one.show.global_function.getYear;

public class System_Bonus extends AppCompatActivity {
    TextView month, doneOrderText, paidOrderText;
    Button viewBtn, distributedBtn, listBtn;
    ArrayList<String[]> staff_assignments = new ArrayList<>();
    private Context context = this;
    private String TAG = "System_Bonus";
    SQLiteDatabase db;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system__bonus);
        ImageButton back_btn = findViewById(R.id.back_imgBtn);
        month = findViewById(R.id.monthdate_SB);
        doneOrderText = findViewById(R.id.doneOrder_text_SB);
        paidOrderText = findViewById(R.id.paidOrder_text_SB);

        viewBtn = findViewById(R.id.salaryView_btn_SB);
        distributedBtn = findViewById(R.id.bonusDistributed_btn_SB);
        listBtn = findViewById(R.id.bonusList_btn_SB);

        String today = getToday("yyyy-MM-dd");
        month.setText("至"+getYear(today)+"年"+getMonth(today)+"月"+getDay(today)+"日");

        dbHelper = new DatabaseHelper(this);
        setDoneOrder();
        getStaffAssignData();

        back_btn.setOnClickListener(v -> {
            super.onBackPressed();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });
        viewBtn.setOnClickListener(v -> {
            Intent view_intent = new Intent(context, Bonus_View.class);
            startActivity(view_intent);
            overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
        });
        distributedBtn.setOnClickListener(v -> {
            Intent distributed_intent = new Intent(context, Bonus_Distribution.class);
            startActivity(distributed_intent);
            overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
        });
        listBtn.setOnClickListener(v -> {
            Intent list_intent = new Intent(context, Bonus_List.class);
            startActivity(list_intent);
            overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
        });

        globalNav();
    }
    private void getStaffAssignData(){
        RequestBody body = new FormBody.Builder()
                .build();

        //連線要求
        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL + "/get_data/staffassign_data.php")
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //在app畫面上呈現錯誤訊息
                        Toast.makeText(context, "連線失敗", Toast.LENGTH_LONG).show();
                        Handler handler = new Handler();
                        handler.postDelayed(() -> getStaffAssignData(), 3000);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
                Log.i(TAG,"responseData: "+responseData); //顯示資料

                try {
                    JSONArray responseArr = new JSONArray(responseData);
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject staff_assign = responseArr.getJSONObject(i);
                        String order_id = staff_assign.getString("order_id");
                        String staff_id = staff_assign.getString("staff_id");
                        String pay = staff_assign.getString("pay");
                        String[] row_data = {order_id, staff_id, pay};
                        staff_assignments.add(row_data);

                        db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put((TableContract.StaffAssignmentTable.COLUMN_NAME_ORDER_ID), order_id);
                        values.put((TableContract.StaffAssignmentTable.COLUMN_NAME_STAFF_ID), staff_id);
                        values.put((TableContract.StaffAssignmentTable.COLUMN_NAME_PAY), pay);

                        try{
                            long newRowId = db.replace(TableContract.StaffAssignmentTable.TABLE_NAME, null, values);
                            if(newRowId != -1) {
                                Log.d(TAG,"create successfully");}
                            else Log.d(TAG, "create failed");
                        }catch (SQLException e){
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        if(responseData.equals("null")){
                            Log.d(TAG, "NO DATA");
                        }
                        //else Toast.makeText(Order.this, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show();
                    });
                }
            }
        });
    }

    private void setDoneOrder(){
        String function_name = "done_paid_data";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", getCompany_id(context))
                .build();

        //連線要求
        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL + "/user_data.php")
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //在app畫面上呈現錯誤訊息
                        Toast.makeText(context, "連線失敗", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
                Log.i(TAG,"responseData: "+responseData); //顯示資料

                try {
                    JSONArray responseArr = new JSONArray(responseData);

                    JSONObject doneOrder = responseArr.getJSONObject(0);
                    final String finish_amount = doneOrder.getString("finish_amount");

                    JSONObject paidOrder = responseArr.getJSONObject(1);
                    final String paid_amount = paidOrder.getString("paid_amount");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            doneOrderText.setText("完成"+finish_amount+"筆訂單");
                            paidOrderText.setText("已分潤"+paid_amount+"筆工單");
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        valuation_btn.setOnClickListener(v -> {
            Intent valuation_intent = new Intent(System_Bonus.this, Valuation.class);
            startActivity(valuation_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
        order_btn.setOnClickListener(v -> {
            Intent order_intent = new Intent(System_Bonus.this, Order.class);
            startActivity(order_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
        calendar_btn.setOnClickListener(v -> {
            Intent calender_intent = new Intent(System_Bonus.this, Calendar.class);
            startActivity(calender_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
        system_btn.setOnClickListener(v -> {
            Intent system_intent = new Intent(System_Bonus.this, System.class);
            startActivity(system_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
        setting_btn.setOnClickListener(v -> {
            Intent setting_intent = new Intent(System_Bonus.this, Setting.class);
            startActivity(setting_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
    }
}
