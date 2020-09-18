package com.example.homerenting_prototype_one.setting;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.system.System;
import com.example.homerenting_prototype_one.valuation.Valuation;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.homerenting_prototype_one.show.global_function.dip2px;
import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;

public class Setting_Discount extends AppCompatActivity {
    TableLayout discountTable;
    Switch valuate, deposit, cancel;
    Button checkBtn, addBtn, deleteBtn;
    ImageButton backBtn;

    ArrayList<String[]> period_discounts;

    String TAG = "Setting_Discount";
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting__discount);

        discountTable = findViewById(R.id.discount_table_SDC);
        checkBtn = findViewById(R.id.check_btn_SDC);
        backBtn = findViewById(R.id.back_imgBtn);
        addBtn = findViewById(R.id.add_btn_SDC);
        deleteBtn = findViewById(R.id.delete_discount_btn);

        period_discounts = new ArrayList<>();

        getFreeData();
        getPeriodData(true);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //輸入欄
                final EditText discount_edit = new EditText(context);
                //設定margin
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = 80;
                params.rightMargin = params.leftMargin;
                discount_edit.setLayoutParams(params);
                //要放到FrameLayout裡，margin才有用
                FrameLayout container = new FrameLayout(context);
                container.addView(discount_edit);

                new AlertDialog.Builder(context)
                        .setTitle("新增優惠")
                        .setView(container)
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String new_discount_name = discount_edit.getText().toString();
                                if(new_discount_name.isEmpty()){
                                    Toast.makeText(context, "未輸入優惠名稱", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                discountTable.addView(addNewRow(new_discount_name, -1, "01-01", "12-31", false));
                                String[] period_discount = {new_discount_name, "-1", "01-01", "12-31", "false"};
                                period_discounts.add(period_discount);
                            }
                        })
                        .setNegativeButton("取消",null)
                        .create()
                        .show();
            }
        });

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "valuate is "+valuate.isChecked());
                Log.d(TAG, "deposit is "+deposit.isChecked());
                Log.d(TAG, "cancel is "+cancel.isChecked());

                getPeriodData(false);

                Log.d(TAG, "size of period discount: "+period_discounts.size());
                for(int i = 0; i < period_discounts.size(); i++){
                    Log.d(TAG, Arrays.toString(period_discounts.get(i)));
                }
//                updateDiscount();
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        globalNav();
    }

    private void getFreeData(){
        for(int i = 0; i < 3; i++){
            TableRow freeItem = (TableRow) discountTable.getChildAt(i);
            switch (i){
                case 0:
                    valuate = (Switch) freeItem.getChildAt(1);
                    break;
                case 1:
                    deposit = (Switch) freeItem.getChildAt(1);
                    break;
                case 2:
                    cancel = (Switch) freeItem.getChildAt(1);
                    break;
            }
        }
        Log.d(TAG, "valuate is "+valuate.isChecked());
        Log.d(TAG, "deposit is "+deposit.isChecked());
        Log.d(TAG, "cancel is "+cancel.isChecked());
    }

    private void getPeriodData(boolean init){
        for(int i = 3; i < discountTable.getChildCount(); i++){
            String[] period_discount = getRowData(i);
            if(init){
                TableRow discountItem = (TableRow) discountTable.getChildAt(i);
                TextView startView = (TextView) discountItem.getChildAt(5);
                TextView endView = (TextView) discountItem.getChildAt(8);
                setDateBtn(startView);
                setDateBtn(endView);
                period_discounts.add(period_discount);
            }
            else {
                period_discounts.set((i-3), period_discount);
            }
        }
    }

    private String[] getRowData(int i){
        TableRow discountItem = (TableRow) discountTable.getChildAt(i);
        TextView nameText = (TextView) discountItem.getChildAt(0);
        Switch enableSw = (Switch) discountItem.getChildAt(1);
        EditText discountEdit = (EditText) discountItem.getChildAt(2);
        //3 : %
//        EditText startView = (EditText) discountItem.getChildAt(4);
        TextView startView = (TextView) discountItem.getChildAt(5);
        //6 : ─
//        EditText endView = (EditText) discountItem.getChildAt(7);
        TextView endView = (TextView) discountItem.getChildAt(8);

        String name = nameText.getText().toString();
        if(name.isEmpty()) name = "";
        String enable = String.valueOf(enableSw.isChecked());
        if(enable.isEmpty()) enable = "";
        String discount = discountEdit.getText().toString();
        if(discount.isEmpty()) discount = "0";
        String startTime = startView.getText().toString();
        if(startTime.isEmpty()) startTime = "";
        String endTime = endView.getText().toString();
        if(endTime.isEmpty()) endTime = "";

        String[] period_discount = {name, discount, startTime, endTime, enable};
        return period_discount;
    }

    private void setDateBtn(final TextView dateBtn){
        dateBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GregorianCalendar calendar = new GregorianCalendar();
                DatePickerDialog date_picker = new DatePickerDialog( context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String monthStr = String.valueOf(month+1);
                        if((month+1) < 10) monthStr = "0"+monthStr;
                        String dayStr = String.valueOf(dayOfMonth);
                        if(dayOfMonth < 10) dayStr = "0"+dayStr;
                        dateBtn.setText(monthStr+"-"+dayStr);
                    }
                },calendar.get(GregorianCalendar.YEAR),calendar.get(GregorianCalendar.MONTH),calendar.get(GregorianCalendar.DAY_OF_MONTH));
                date_picker.show();
            }
        } );
    }

    private void getData(){
        String function_name = "discount_data";
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
                        Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
                Log.i(TAG,"responseData of getData: "+responseData); //顯示資料

                try {
                    JSONArray responseArr = new JSONArray(responseData);

                    JSONObject freeItems = responseArr.getJSONObject(0);
                    boolean valuateBl = freeItems.getBoolean("valuate");
                    boolean depositBl = freeItems.getBoolean("deposit");
                    boolean cancelBl = freeItems.getBoolean("cancel");

                    valuate.setChecked(valuateBl);
                    deposit.setChecked(depositBl);
                    cancel.setChecked(cancelBl);

                    for(int i = 1; i < responseArr.length(); i++){
                        JSONObject discountItem = responseArr.getJSONObject(i);
                        String discountName = discountItem.getString("discount_name");
                        int percent = discountItem.getInt("discount");
                        String startTime = discountItem.getString("start_date");
                        String endTime = discountItem.getString("end_date");
                        boolean enable = discountItem.getBoolean("turn_on");

                        discountTable.addView(addNewRow(discountName, percent, startTime, endTime, enable));
                        String[] period_discount = {discountName, String.valueOf(percent), startTime, endTime, String.valueOf(enable)};
                        period_discounts.add(period_discount);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private TableRow addNewRow(String discountName, int percent, String startTime, String endTime, boolean enable){
        TableRow newDiscount = new TableRow(context);

        TextView nameText = new TextView(context);
        Switch enableSw = new Switch(context);
        EditText discountEdit = new EditText(context);
        TextView percentIcon =  new TextView(context);
        EditText startViewe = new EditText(context);
        TextView startView = addNewDate(startTime);
        TextView toIcon =  new TextView(context);
        EditText endViewe = new EditText(context);
        TextView endView = addNewDate(endTime);

        int dp40 = dip2px(context, 40);
        int dp15 = dip2px(context, 15);
        int dp8 = dip2px(context, 8);

        newDiscount.setPadding(dp15, 0, 0, 0);
        nameText.setText(discountName);
        nameText.setTextSize(20);
        nameText.setTextColor(Color.parseColor("#000000"));
        enableSw.setPadding(dp15, 0, 0, 0);
        enableSw.setChecked(enable);
        if(percent >= 0) discountEdit.setText(percent);
        else discountEdit.setHint("1");
        discountEdit.setWidth(dp40);
        discountEdit.setGravity(Gravity.RIGHT);
        percentIcon.setText("%");
        percentIcon.setTextSize(18);
        percentIcon.setPadding(0, 0, 0 ,dp8);
        startViewe.setVisibility(View.GONE);
        toIcon.setText("─");
        endViewe.setVisibility(View.GONE);

        newDiscount.addView(nameText);
        newDiscount.addView(enableSw);
        newDiscount.addView(discountEdit);
        newDiscount.addView(percentIcon);
        newDiscount.addView(startViewe);
        newDiscount.addView(startView);
        newDiscount.addView(toIcon);
        newDiscount.addView(endViewe);
        newDiscount.addView(endView);

        return newDiscount;
    }

    private TextView addNewDate(String date){
        int dp3 = dip2px(context, 3);
        TextView dateView =  new TextView(context);
        dateView.setText(date);
        dateView.setTextSize(18);
        dateView.setBackgroundResource(R.drawable.edittext_rectanngle);
        dateView.setPadding(dp3, dp3, dp3, dp3); //要在backgoundResource後設置才有用
        return dateView;
    }

    private void updateDiscount(){
        String function_name = "updateDiscount";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", getCompany_id(context))
                .add("valuate", String.valueOf(valuate.isChecked()))
                .add("deposit", String.valueOf(deposit.isChecked()))
                .add("cancel", String.valueOf(cancel.isChecked()))
                .add("period_items", itemsToString(period_discounts))
                .build();
        Log.i(TAG, "valuate: "+valuate.isChecked()+", deposit: "+deposit.isChecked()+", cancel: "+cancel.isChecked()
                +", period_item: "+itemsToString(period_discounts));

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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, "responseData of update_today_order: " + responseData);
            }
        });
    }

    private String itemsToString(ArrayList<String[]> items){
        if(items.size() == 0) return "";

        String itemStr;
        itemStr = "[";
        itemStr = itemStr+"[\""+items.get(0)[0] +"\", "+items.get(0)[1]+", \""+items.get(0)[2]+"\", \""+items.get(0)[3]+"\", "+items.get(0)[4]+"]";
        for(int i = 1; i < items.size(); i++){
            itemStr = itemStr+", [\""+items.get(i)[0] +"\", "+items.get(i)[1]+", \""+items.get(i)[2]+"\", \""+items.get(i)[3]+"\", "+items.get(i)[4]+"]";
        }
        itemStr = itemStr+"]";

        return itemStr;
    }

    private void globalNav(){
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Setting_Discount.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Setting_Discount.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Setting_Discount.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Setting_Discount.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Setting_Discount.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
