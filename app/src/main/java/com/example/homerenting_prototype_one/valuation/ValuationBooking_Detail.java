package com.example.homerenting_prototype_one.valuation;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.adapter.re_adpater.CarAdapter;
import com.example.homerenting_prototype_one.setting.Setting;
import com.example.homerenting_prototype_one.system.System;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.furniture.Edit_Furniture;
import com.example.homerenting_prototype_one.order.Order;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;
import static com.example.homerenting_prototype_one.show.global_function.getDate;


public class ValuationBooking_Detail extends AppCompatActivity {
    LinearLayout furnitureLL;

    TextView nameText, nameTitleText, phoneText, valuationTimeText;
    TextView fromAddressText, toAddressText, remainderText;
    TextView movingDateText, movingTimeText, valPriceText;

    EditText carNumEdit, carWeightEdit, carTypeEdit;
    EditText worktimeEdit, priceEdit, memoEdit;

    RecyclerView carAssignRList;

    Button check_btn, furniture_btn;

    String order_id;
    String name, gender, phone, valuationTime, fromAddress, toAddress, remainder, memo;
    String valPrice = "4000";

    CarAdapter carAdapter;

    ArrayList<String[]> cars;

    String TAG = "Valuation_Booking_Detail";
    private final String PHP = "/user_data.php";

    Context context = this;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valuation_booking__detail);
        Button phoneCall_btn = findViewById(R.id.call_btn);
        final GregorianCalendar calendar = new GregorianCalendar();

        cars = new ArrayList<>();
        cars.add(new String[3]);

//        bundle = getIntent().getExtras();
        bundle = new Bundle();
        bundle.putString("order_id", "47");
        order_id = bundle.getString("order_id");
        Log.i(TAG, "order_id: "+order_id);

        linking(); //將xml裡的元件連至此java

        getOrder();

        getItems();

        furniture_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Edit_Furniture.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        movingDateText.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog date_picker = new DatePickerDialog( context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        movingDateText.setText(String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(dayOfMonth));
                    }
                },calendar.get( GregorianCalendar.YEAR ),calendar.get( GregorianCalendar.MONTH ),calendar.get( GregorianCalendar.DAY_OF_MONTH));
                date_picker.show();
            }
        } );

        movingTimeText.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog time_picker = new TimePickerDialog( context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        movingTimeText.setText(hourOfDay+":"+minute);
                    }
                },calendar.get(GregorianCalendar.DAY_OF_MONTH ),calendar.get(GregorianCalendar.MINUTE ),true);
                time_picker.show();
            }
        } );

        setCheckBtn();

        phoneCall_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call_intent = new Intent(Intent.ACTION_DIAL);
                call_intent.setData(Uri.parse("tel:"+phone));
                startActivity(call_intent);
            }
        });

        globalNav(); //底下nav
    }

    public void linking(){
        nameText = findViewById(R.id.name_VBD);
        nameTitleText = findViewById(R.id.nameTitle_VBD);
        phoneText = findViewById(R.id.phone_VBD);
        valuationTimeText = findViewById(R.id.valuationTime_VBD);
        fromAddressText = findViewById(R.id.FromAddress_VBD);
        toAddressText = findViewById(R.id.ToAddress_VBD);
        furniture_btn = findViewById(R.id.edit_furniture_btn_VBD);
        remainderText = findViewById(R.id.notice_VBD);
        movingDateText = findViewById(R.id.movingDate_VBD);
        movingTimeText = findViewById(R.id.movingTime_VBD);
        carNumEdit = findViewById(R.id.num_VBD);
        carWeightEdit = findViewById(R.id.weight_VBD);
        carTypeEdit = findViewById(R.id.type_VBD);
        worktimeEdit = findViewById(R.id.worktime_VBD);
        priceEdit = findViewById(R.id.price_VBD);
        check_btn = findViewById(R.id.check_evaluation_btn);
        furnitureLL = findViewById(R.id.furniture_LL_VBD);
        valPriceText = findViewById(R.id.valPrice_VBD);
        memoEdit = findViewById(R.id.PS_VBD);

        carAssignRList = findViewById(R.id.car_assign_VBD);
    }

    private void getOrder(){
        //傳至網頁的值，傳function_name
        String function_name = "valuation_detail";
        String company_id = getCompany_id(this);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .add("company_id", company_id)
                .build();
        Log.d(TAG, "order_id: "+order_id);

        //連線要求
        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+PHP)
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
                //Log.d(TAG,"responseData: "+responseData); //顯示資料

                try {
                    JSONArray responseArr = new JSONArray(responseData);
                    JSONObject order = responseArr.getJSONObject(0);
//                    Log.i(TAG,"JSONObject of order:"+order);

                    //取得資料
                    name = order.getString("member_name");
                    gender = order.getString("gender");
                    phone = order.getString("phone");
                    valuationTime = getDate(order.getString("valuation_date"));
                    if(!order.getString("valuation_time").equals("null"))
                        valuationTime = valuationTime+" "+order.getString("valuation_time");
                    fromAddress = order.getString("from_address");
                    toAddress = order.getString("to_address");
                    remainder = order.getString("additional");
                    memo = order.getString("memo");
                    if(memo.equals("null")) memo = "";

                    //顯示資料
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            nameText.setText(name);
                            if(gender.equals("女")) nameTitleText.setText("小姐");
                            else if(gender.equals("男")) nameTitleText.setText("先生");
                            else nameTitleText.setText("");
                            phoneText.setText(phone);
                            valuationTimeText.setText(valuationTime);
                            fromAddressText.setText(fromAddress);
                            toAddressText.setText(toAddress);
                            remainderText.setText(remainder);
                            memoEdit.setText(memo);
                        }
                    });

                    int auto = order.getInt("auto");
                    if(auto==0){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                furnitureLL.setVisibility(View.GONE);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(context, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                carAdapter = new CarAdapter(cars);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        carAssignRList.setLayoutManager(new LinearLayoutManager(context));
                        carAssignRList.setAdapter(carAdapter);
                    }
                });
            }
        });
    }

    private void getItems(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < carAdapter.getItemCount(); i++){
                    getItem(i);
                }
            }
        }, 3000);
    }

    private void getItem(final int position){
        Log.d(TAG, "getItem. position/getItemCount:"+position+"/"+carAdapter.getItemCount());
        View view = carAssignRList.getLayoutManager().findViewByPosition(position);

        if(view != null){
            if(!isCarsExist(position)) cars.add(position, new String[3]);

            final EditText weight_edit = view.findViewById(R.id.weight_CI);
            weight_edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(!weight_edit.getText().toString().isEmpty()){
                        String weightStr = weight_edit.getText().toString();
                        cars.get(position)[0] = weightStr;
                        nameText.setText(weightStr);

                        Log.d(TAG, position+". weight: "+weightStr);

                        if(carAdapter.getItemCount() == position+1){
                            if(!isCarsExist(position+1)) cars.add(position+1, new String[3]);
                            carAdapter.notifyDataSetChanged();
                            getItems();
                        }
                    }
                    else{
                        cars.get(position)[0] = "";
                        nameText.setText((position+1)+". no weight");
                    }
                }
            });


            final EditText type_edit = view.findViewById(R.id.type_CI);
            type_edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(!type_edit.getText().toString().isEmpty()){
                        String typeStr = type_edit.getText().toString();
                        cars.get(position)[1] = typeStr;
                        nameText.setText(typeStr);
                    }
                    else{
                        cars.get(position)[0] = "";
                        nameText.setText((position+1)+". no type");
                    }
                }
            });

            final EditText num_edit = view.findViewById(R.id.num_CI);
            num_edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(!num_edit.getText().toString().isEmpty()){
                        String numStr = num_edit.getText().toString();
                        cars.get(position)[2] = numStr;
                        nameText.setText(numStr);
                    }
                    else{
                        cars.get(position)[2] = "";
                        nameText.setText((position+1)+". no num");
                    }
                }
            });
        }
        else{
            Log.d(TAG, position+". view is null");
        }
    }

    private boolean isCarsExist(int position){
        try{
            cars.get(position);
            return true;
        } catch (IndexOutOfBoundsException e) {
            Log.d(TAG, "isCarsExist: add position "+position);
            return false;
        }
    }

    private void setCheckBtn(){
        check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String movingDate = movingDateText.getText().toString().trim();
                String movingTime = movingTimeText.getText().toString().trim();
                String moving_date = movingDate + " " + movingTime;
                String num = carNumEdit.getText().toString().trim();
                String weight = carWeightEdit.getText().toString().trim();
                String type = carTypeEdit.getText().toString().trim();
                String estimate_worktime = worktimeEdit.getText().toString().trim();
                String fee = priceEdit.getText().toString().trim();
                memo = memoEdit.getText().toString();
                Log.d(TAG,"check_price_btn, fee: "+fee+", memo: "+memo);

                if(checkEmpty(movingDate, movingTime, num, weight, type, estimate_worktime, fee))
                    return;

                updateValuation(moving_date, num, weight, type, estimate_worktime, fee);

                new AlertDialog.Builder(context)
                        .setTitle("媒合中")
                        .setMessage("到府估價單媒合中，成功媒\n合會成為訂單，請公司注意\n新訂單通知。")
                        .setPositiveButton( "確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(context, Valuation_Booking.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }, 1000);
                            }
                        } )
                        .show();
            }
        });
    }

    private boolean checkEmpty(String movingDate, String movingTime, String num, String weight, String type, String estimate_worktime, String fee){
        boolean check = false;
        if(TextUtils.isEmpty(movingDateText.getText().toString())){
            movingDateText.setError("請輸入日期");
            check = true;
        }
        if(TextUtils.isEmpty(movingTimeText.getText().toString())){
            movingTimeText.setError("請輸入時間");
            check = true;
        }
        if(TextUtils.isEmpty(num)){
            carNumEdit.setError("請輸入數量");
            check = true;
        }
        if(TextUtils.isEmpty(weight)){
            carWeightEdit.setError("請輸入重量");
            check = true;
        }
        if(TextUtils.isEmpty(type)){
            carTypeEdit.setError("請輸入車子種類");
            check = true;
        }
        if(TextUtils.isEmpty(estimate_worktime)){
            worktimeEdit.setError("請輸入工時");
            check = true;
        }
        if(TextUtils.isEmpty(fee)){
            priceEdit.setError("請輸入搬家價格");
            check = true;
        }
//        if(Integer.parseInt(fee) > Integer.parseInt(valPrice)){
//            priceEdit.setError("所輸入之搬家價格不得高於系統估價計價格");
//            check = true;
//        }

        return check;
    }

    private void updateValuation(String moving_date, String num, String weight, String type, String estimate_worktime, String fee){
        String function_name = "update_bookingValuation";
        String company_id = getCompany_id(context);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .add("company_id",company_id)
                .add("moving_date",moving_date+":00")
                .add("num", num)
                .add("weight", weight)
                .add("type", type)
                .add("estimate_worktime", estimate_worktime)
                .add("fee", fee)
                .add("memo", memo)
                .build();
        Log.d(TAG, "check_btn: order_id: "+order_id+", moving_date:  "+moving_date+":00"+
                ", num: "+num+", weight: "+weight+", type: "+type+
                ", estimate_worktime: "+estimate_worktime+", fee: "+fee);

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
                final String responseData = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "估價單已完成", Toast.LENGTH_LONG).show();
                    }
                });
                Log.d(TAG, "submit responseData: " + responseData);
            }
        });
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
                Intent valuation_intent = new Intent(context, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(context, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(context, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(context, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(context, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
