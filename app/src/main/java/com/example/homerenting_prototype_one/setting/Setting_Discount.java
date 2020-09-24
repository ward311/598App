package com.example.homerenting_prototype_one.setting;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
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

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
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
import static com.example.homerenting_prototype_one.show.global_function.getDay;
import static com.example.homerenting_prototype_one.show.global_function.getMonth;
import static com.example.homerenting_prototype_one.show.global_function.getToday;
import static com.example.homerenting_prototype_one.show.global_function.getYear;

public class Setting_Discount extends AppCompatActivity {
    TableLayout discountTable;
    Switch valuate, deposit, cancel;
    Button checkBtn, addBtn, deleteBtn;
    ImageButton backBtn;

    ArrayList<String[]> period_discounts;
    ArrayList<String> delete_discounts;

    boolean deleteMode = false, disable = false;

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
        delete_discounts = new ArrayList<>();

        getFreeRow();
        getPeriodRow(true);
//        getData();

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
                                String thisYear = getToday("yyyy");
                                discountTable.addView(addNewRow("-1", new_discount_name, -1, thisYear+"-01-01", thisYear+"-12-31", false));
                                String[] period_discount = {"-1", new_discount_name, "-1", thisYear+"-01-01", thisYear+"-12-31", "false"};
                                period_discounts.add(period_discount);
                            }
                        })
                        .setNegativeButton("取消",null)
                        .create()
                        .show();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!deleteMode){
                    for(int i = 0; i < discountTable.getChildCount(); i++){
                        TableRow tr = (TableRow) discountTable.getChildAt(i);
                        Button db = (Button) tr.getChildAt(0);
                        db.setVisibility(View.VISIBLE);
                    }
                    deleteMode = true;
                }
                else{
                    for(int i = 0; i < discountTable.getChildCount(); i++){
                        TableRow tr = (TableRow) discountTable.getChildAt(i);
                        Button db = (Button) tr.getChildAt(0);
                        db.setVisibility(View.GONE);
                    }
                    deleteMode = false;
                }
            }
        });

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "valuate is "+valuate.isChecked());
                Log.d(TAG, "deposit is "+deposit.isChecked());
                Log.d(TAG, "cancel is "+cancel.isChecked());

                getPeriodRow(false);

                Log.d(TAG, "size of period discount: "+period_discounts.size());
                Log.d(TAG, "period discount: "+itemsToString(period_discounts));

                Log.d(TAG, "size of delete discount: "+delete_discounts.size());
                Log.d(TAG, "delete discount: "+itemsToString2(delete_discounts));

                updateDiscount();
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

    private void getFreeRow(){
        for(int i = 0; i < 3; i++){
            TableRow freeItem = (TableRow) discountTable.getChildAt(i);
            int switchPosition = 2;
            switch (i){
                case 0:
                    valuate = (Switch) freeItem.getChildAt(switchPosition);
                    break;
                case 1:
                    deposit = (Switch) freeItem.getChildAt(switchPosition);
                    break;
                case 2:
                    cancel = (Switch) freeItem.getChildAt(switchPosition);
                    break;
            }
        }
        Log.d(TAG, "valuate is "+valuate.isChecked());
        Log.d(TAG, "deposit is "+deposit.isChecked());
        Log.d(TAG, "cancel is "+cancel.isChecked());
    }

    private void getPeriodRow(boolean init){
        for(int i = 3; i < discountTable.getChildCount(); i++){
            final String[] period_discount = getRowData(i);
            if(init){
                final TableRow discountItem = (TableRow) discountTable.getChildAt(i);
                final Switch switcher = (Switch) discountItem.getChildAt(2);
                final TextView startView = (TextView) discountItem.getChildAt(5);
                final TextView endView = (TextView) discountItem.getChildAt(7);

                setSwitch(switcher, startView, endView, period_discount);

                if(switcher.isChecked()){
                    startView.setOnClickListener(null);
                    endView.setOnClickListener(null);
                }
                else{
                    setDateBtn(startView);
                    setDateBtn(endView);
                }
                period_discounts.add(period_discount);

                final Button deleteBtn = (Button) discountItem.getChildAt(0);
                final int finalI = i-3;
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        discountTable.removeView(discountItem);
                        delete_discounts.add(period_discount[0]);
                        period_discounts.remove(finalI);
                    }
                });
            }
            else {
                period_discounts.set((i-3), period_discount);
            }
        }
    }

    private String[] getRowData(int i){
        TableRow discountItem = (TableRow) discountTable.getChildAt(i);
        //0 : X
        TextView nameText = (TextView) discountItem.getChildAt(1);
        Switch enableSw = (Switch) discountItem.getChildAt(2);
        EditText discountEdit = (EditText) discountItem.getChildAt(3);
        //4 : %
        TextView startView = (TextView) discountItem.getChildAt(5);
        //6 : ─
        TextView endView = (TextView) discountItem.getChildAt(7);
        TextView discountIdText = (TextView) discountItem.getChildAt(discountItem.getChildCount()-1);

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
        String discountId = discountIdText.getText().toString();

        String[] period_discount = {discountId, name, discount, startTime, endTime, enable};
        return period_discount;
    }

    private void setSwitch(final Switch switcher, final TextView startView, final TextView endView, final String[] period_discount){
        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(switcher.isChecked()){
                    startView.setOnClickListener(null);
                    endView.setOnClickListener(null);
                }
                else{
                    setDateBtn(startView);
                    setDateBtn(endView);
                    String startDate = startView.getText().toString();
                    String endDate = endView.getText().toString();
                    LocalDate now = LocalDate.now();
                    LocalDate start = LocalDate.of(Integer.parseInt(getYear(startDate)), Integer.parseInt(getMonth(startDate)), Integer.parseInt(getDay(startDate)));
                    LocalDate end = LocalDate.of(Integer.parseInt(getYear(endDate)), Integer.parseInt(getMonth(endDate)), Integer.parseInt(getDay(endDate)));

                    if(start.isBefore(now) && end.isAfter(now) && !disable){
                        switcher.setChecked(true);
                        Log.d(TAG, start+" < "+now+" < "+end);
                        new AlertDialog.Builder(context)
                                .setTitle("現在進行折扣中，要強制取消嗎？")
                                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        disableDiscount(period_discount);
                                        disable = true;
                                        switcher.setChecked(false);
                                        disable = false;
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .create()
                                .show();
                    }
                    else Log.d(TAG, "start: "+start+", end: "+end+", now: "+now);
                }
            }
        });
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
                        dateBtn.setText(year+"-"+monthStr+"-"+dayStr);
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
                        String discountId = discountItem.getString("discount_id");
                        String discountName = discountItem.getString("discount_name");
                        int percent = discountItem.getInt("discount");
                        String startTime = discountItem.getString("start_date");
                        String endTime = discountItem.getString("end_date");
                        String disableTime = discountItem.getString("disable_time");
                        boolean enable = false;
                        if(!disableTime.isEmpty()) enable = true;

                        discountTable.addView(addNewRow(discountId, discountName, percent, startTime, endTime, enable));
                        String[] period_discount = {discountId, discountName, String.valueOf(percent), startTime, endTime, String.valueOf(enable)};
                        period_discounts.add(period_discount);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private TableRow addNewRow(String discountId, final String discountName, int percent, String startTime, String endTime, boolean enable){
        String[] period_discount = {discountId, discountName, String.valueOf(percent), startTime, endTime, String.valueOf(enable)};
        final TableRow newDiscount = new TableRow(context);

        Button deleteBtn = new Button(context);
        TextView nameText = new TextView(context);
        Switch enableSw = new Switch(context);
        EditText discountEdit = new EditText(context);
        TextView percentIcon =  new TextView(context);
        TextView startView = addNewDate(startTime);
        TextView toIcon =  new TextView(context);
        TextView endView = addNewDate(endTime);
        TextView discountIdText = new TextView(context);

        int dp40 = dip2px(context, 40);
        int dp15 = dip2px(context, 15);
        int dp8 = dip2px(context, 8);
        int dp5 = dip2px(context, 5);

        newDiscount.setGravity(Gravity.CENTER_VERTICAL);
        deleteBtn.setBackgroundResource(R.drawable.dot);
        deleteBtn.setLayoutParams(new TableRow.LayoutParams(dp15, dp15));
        if(!deleteMode) deleteBtn.setVisibility(View.GONE);
        else deleteBtn.setVisibility(View.VISIBLE);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discountTable.removeView(newDiscount);
                delete_discounts.add(discountName);
                pdRemoveBy(discountName);
            }
        });
        nameText.setText(discountName);
        nameText.setTextSize(20);
        nameText.setTextColor(Color.parseColor("#000000"));
        nameText.setPadding(0, 0, 0, dp5);
        enableSw.setPadding(dp15, 0, 0, 0);
        enableSw.setChecked(enable);
        setSwitch(enableSw, startView, endView, period_discount);
        if(percent >= 0) discountEdit.setText(percent);
        else discountEdit.setHint("1");
        discountEdit.setWidth(dp40);
        discountEdit.setGravity(Gravity.RIGHT);
        percentIcon.setText("%");
        percentIcon.setTextSize(18);
        percentIcon.setPadding(0, 0, 0 ,dp8);
        toIcon.setText("─");
        toIcon.setPadding(dp5, 0, dp5, 0);
        discountIdText.setText(discountId);
        discountIdText.setVisibility(View.GONE);

        newDiscount.addView(deleteBtn);
        newDiscount.addView(nameText);
        newDiscount.addView(enableSw);
        newDiscount.addView(discountEdit);
        newDiscount.addView(percentIcon);
        newDiscount.addView(startView);
        newDiscount.addView(toIcon);
        newDiscount.addView(endView);
        newDiscount.addView(discountIdText);

        return newDiscount;
    }

    private TextView addNewDate(String date){
        int dp60 = dip2px(context, 60);
        int dp3 = dip2px(context, 3);
        TextView dateView =  new TextView(context);
        dateView.setWidth(dp60);
        dateView.setText(date);
        dateView.setTextSize(18);
        dateView.setGravity(Gravity.CENTER);
        dateView.setBackgroundResource(R.drawable.edittext_rectanngle);
        dateView.setPadding(dp3, dp3, dp3, dp3); //要在backgoundResource後設置才有用
        setDateBtn(dateView);
        return dateView;
    }

    private void pdRemoveBy(String discountName){
        for(int i = 0; i < period_discounts.size(); i++){
            if(period_discounts.get(i)[0].equals(discountName)){
                period_discounts.remove(i);
                return;
            }
        }
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
                .add("delete_items", itemsToString2(delete_discounts))
                .build();
        Log.i(TAG, "valuate: "+valuate.isChecked()+", deposit: "+deposit.isChecked()+", cancel: "+cancel.isChecked()
                +", period_item: "+itemsToString(period_discounts)+", "+itemsToString2(delete_discounts));

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
                Log.d(TAG, "responseData of update_discount: " + responseData);
            }
        });
    }

    private void disableDiscount(String[] period_discount){
        String function_name = "disableDiscount";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", getCompany_id(context))
                .add("discount_id", period_discount[0])
                .add("discount_name", period_discount[1])
                .add("discount", period_discount[2])
                .add("start_date", period_discount[3])
                .add("end_date", period_discount[4])
                .build();
        Log.d(TAG, "disableDiscount: discount_id: "+period_discount[0]+", discount_name: "+period_discount[1]
                +", discount: "+period_discount[2]+", start_date: "+period_discount[3]+", end_date: "+period_discount[4]);

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
                Log.d(TAG, "responseData of disable_discount: " + responseData);
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

    private String itemsToString2(ArrayList<String> items){
        if(items.size() == 0) return "";

        String itemStr;
        itemStr = "[";
        for(int i = 0; i < items.size(); i++){
            itemStr = itemStr+"\""+items.get(i)+"\"";
            if(i < items.size()-1) itemStr = itemStr+", ";
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
