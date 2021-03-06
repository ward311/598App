package com.example.homerenting_prototype_one.valuation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

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

    TextView nameText, nameTitleText, phoneText, contactTimeText, valuationTimeText;
    TextView fromAddressText, toAddressText, remainderText;
    TextView movingDateText, movingTimeText, valPriceText, newValPriceText;
    TextView sugCarsText;
    TextView valRangeText;
    TextView program_type;
    TextView current_discount;
    TextView onlineText;
    EditText carNumEdit, carWeightEdit, carTypeEdit;
    EditText worktimeEdit, priceEdit, memoEdit;
    String result = "";
    String suggestStr;
    String lowestStr;
    String originalStr;
    RecyclerView carAssignRList;

    Button check_btn, furniture_btn, phoneCall_btn;

    String order_id;
    String name, gender, phone, contactTime, valuationTime, fromAddress, toAddress, estimate_fee, remainder, memo, isAuto;
    String email;
    String program;
    String mvfopt, mvtopt;
    String sugCars;
    String estimateDis, estimateTime;
    String fixDiscount, fixEnable;
    String lowPrice, middlePrice, highPrice;
    String plan;
    Spinner memoSpinner;
    Boolean isDiscount;
    CarAdapter carAdapter;
    Date setDate;
    ArrayList<String[]> cars;
    int valPrice = -1, firstRowEmpty = 1;
    float discount ;
    String TAG = "Valuation_Booking_Detail";
    private final String PHP = "/user_data.php";
    Context context = ValuationBooking_Detail.this;
    Bundle bundle;
    Bundle fromBooking = new Bundle();
    Bundle getFromEdit = new Bundle();
    Handler handler = new Handler();
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valuation_booking__detail);
        final GregorianCalendar calendar = new GregorianCalendar();
        cars = new ArrayList<>();
        String[] newString = {"", "", ""};
        cars.add(newString);
        bundle = getIntent().getExtras();
        fromBooking = getIntent().getExtras();
        getFromEdit = getIntent().getExtras();
        order_id = bundle.getString("order_id");
        plan = bundle.getString("plan");
        Log.i(TAG, "order_id: "+order_id);

        linking(); //???xml?????????????????????java
        setValPrice();
        //getCompanyFixDis();
        //getOrder();
        new Async().execute();
        priceChangeAlert();
        if(getFromEdit.getString("suggestCars")==null){
            sugCarsText.setText("???????????????");
        }else{
            String returnCar = getFromEdit.getString("suggestCars");
            handler.postDelayed(() -> sugCarsText.setText(returnCar), 1000);
            //String[] split_car = returnCar.split("[*]");
            //String isZero = split_car[1];
            //Log.d(TAG, "zero: "+isZero);
            /*if(isZero.equals(" 0")){
                sugCarsText.setText("123");
            }else{

            }*/
        }
        if(getFromEdit.getString("suggestPrice")==null){
            //setValPrice();
            valPriceText.setText("3600~8000");
        }else{

            handler.postDelayed(()->{
                valPriceText.setPaintFlags(valPriceText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                onlineText.setText("????????????");
                onlineText.setTextColor(Color.RED);
                Log.d(TAG, "discount get: "+ discount+
                        " isDis: "+isDiscount);
                calculate();
                newValPriceText.setText(getFromEdit.getString("suggestPrice"));
            } , 1500);

        }

        //int total = (int) ((int)((Integer.parseInt(getFromEdit.getString("suggestPrice"))+600))*0.9);
        //newValPriceText.setText(String.valueOf(total));

        Log.d(TAG, "get Car from API: "+getFromEdit.getString("suggestCars")+
                ", price from API: "+getFromEdit.getString("suggestPrice"));
        furniture_btn.setOnClickListener(v -> {
            Intent intent = new Intent(context, Edit_Furniture.class);
            fromBooking.putString("clickFromBooking", isAuto);
            fromBooking.putString("estimate_distance", estimateDis);
            fromBooking.putString("estimate_time", estimateTime);
            fromBooking.putString("mvfopt", mvfopt);
            fromBooking.putString("mvtopt", mvtopt);
            bundle.putString("isWeb", isAuto);
            intent.putExtras(bundle);
            intent.putExtras(fromBooking);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        handler.postDelayed(() -> {
            try {
                int year = Year.now().getValue();
                String[] dateSplit = valuationTime.split(" ");
                String valDate = dateSplit[0];
                setDate = convertDate(year +"/"+valDate);
                Log.d(TAG,"getDATE "+ year +"/"+valDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        },1000);


        /*????????????????????????Date????????????????????????????????????????????????????????????*/
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Taipei"));
        Log.d(TAG, "now: "+now.getYear()+"-"+monthToInt(String.valueOf(now.getMonth()))+"-"+now.getDayOfMonth());
        movingDateText.setOnClickListener(v -> {
            movingDateText.setError(null);
            @SuppressLint("ShowToast") DatePickerDialog date_picker = new DatePickerDialog( context, (view, year, month, dayOfMonth) -> {
                if(year<=now.getYear() && (month+1)<=monthToInt(String.valueOf(now.getMonth())) && dayOfMonth<now.getDayOfMonth()) {
                    Toast.makeText(context, "???????????????????????????", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "????????????("+year+"/"+(month+1)+"/"+dayOfMonth+")"+
                            "???????????????("+now.getYear()+"/"+monthToInt(String.valueOf(now.getMonth()))+"/"+now.getDayOfMonth()+") ??????");
                    movingDateText.setError("???????????????????????????");
                    return;
                }
                movingDateText.setText(year+"-"+(month+1)+"-"+dayOfMonth);
            },calendar.get( GregorianCalendar.YEAR ),calendar.get( GregorianCalendar.MONTH ),calendar.get( GregorianCalendar.DAY_OF_MONTH));
            //date_picker.getDatePicker().setMinDate(setDate.getTime());
            date_picker.getDatePicker().setMinDate(new Date().getTime());
            date_picker.show();
        });

        movingTimeText.setOnClickListener(v -> {
            movingTimeText.setError(null);
            TimePickerDialog time_picker = new TimePickerDialog( context, (view, hourOfDay, minute) -> {
                String mm = String.valueOf(minute);
                if(minute < 10) mm = "0"+mm;
                String hh = String.valueOf(hourOfDay);
                if(hourOfDay < 10) hh = "0"+hh;
                movingTimeText.setText(hh+":"+mm);
            },calendar.get(GregorianCalendar.DAY_OF_MONTH ),calendar.get(GregorianCalendar.MINUTE ),true);
            time_picker.show();
        });


        setPhoneBtn();
        setCheckBtn();
        setSpr();
        globalNav(); //??????nav
    }
    private void setSpr(){
        final String[] reasons= {"???????????????","??????????????????", "??????????????????", "??????????????????"};

        ArrayAdapter<String> reasonsList = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item, reasons);
        memoSpinner.setAdapter(reasonsList);
        memoSpinner.setSelection(0);
        memoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = memoSpinner.getSelectedItemPosition();
                if(pos!=0){
                    if(pos!=3){
                       result += memoSpinner.getSelectedItem().toString()+"\n";
                       memoEdit.setText(result);
                       memoEdit.setSelection(memoEdit.getText().length());
                    }
                    else{
                        memoEdit.getText().clear();
                        result = "";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public class Async extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            getCompanyFixDis();
            getOrder();
            return null;
        }

    }

    private Date convertDate(String date) throws ParseException {
        Date date1 = new SimpleDateFormat("yyyy/MM/dd").parse(date);
        return date1;
    }
    private void calculate(){
        float most = 0;
        float least = 0;
        if(isDiscount){
            discount = (100 - discount) / 100;
            Log.d(TAG, "????????????: "+discount) ;
        }else{
            discount = 1;
        }
        float suggest = Integer.parseInt(getFromEdit.getString("suggestPrice")); /*API???????????????????????????*/

        switch (plan){
            case "????????????":
                if(suggest<=5000){
                    least = Math.round((suggest-600) * 8/10);
                    most = Math.round((suggest-600)*discount);
                    break;
                }else if(suggest>=5001&&suggest<=10000){
                    least = Math.round((suggest-1200) * 8/10);
                    most =  Math.round((suggest-1200) * discount);
                    break;
                }else if(suggest>=10001&&suggest<=15000){
                    least = Math.round((suggest-1800) * 8/10);
                    most =  Math.round((suggest-1800) * discount);
                    break;
                }else if(suggest>=15001&&suggest<=20000){
                    least = Math.round((suggest-2400) * 8/10);
                    most =  Math.round((suggest-2400) * discount);
                    break;
                }else if(suggest>=20001&&suggest<=30000) {
                    least = Math.round((suggest-3000) * 8/10);
                    most =  Math.round((suggest-3000) * discount);
                    break;
                }else{
                    least = Math.round((suggest-3600) * 8/10);
                    most =  Math.round((suggest-3600) * discount);
                    break;
                }

            case "????????????":
                if(suggest<=5000){
                    least = Math.round((suggest+1680) * 8/10);
                    most =  Math.round((suggest+1680)*discount);
                    break;
                }else if(suggest>=5001&&suggest<=10000){
                    least = Math.round((suggest+3360) * 8/10);
                    most =  Math.round((suggest+3360) * discount);
                    break;
                }else if(suggest>=10001&&suggest<=15000){
                    least = Math.round((suggest+5040) * 8/10);
                    most =  Math.round((suggest+5040) * discount);
                    break;
                }else if(suggest>=15001&&suggest<=20000){
                    least = Math.round((suggest+6720) * 8/10);
                    most =  Math.round((suggest+6720) * discount);
                    break;
                }else if(suggest>=20001&&suggest<=30000){
                    least = Math.round((suggest+8400) * 8/10);
                    most =  Math.round((suggest+8400) * discount);
                    break;
                }else{
                    least = Math.round((suggest+10080) * 8/10);
                    most =  Math.round((suggest+10080) * discount);
                    break;
                }
            case "????????????":
                if(suggest<=5000){
                    least = Math.round((suggest) * 8/10);
                    most =  Math.round((suggest)*discount);
                    break;
                }else if(suggest>=5001&&suggest<=10000){
                    least = Math.round((suggest) * 8/10);
                    most = Math.round((suggest) * discount);
                    break;
                }else if(suggest>=10001&&suggest<=15000){
                    least = Math.round((suggest) * 8/10);
                    most = Math.round((suggest) * discount);
                    break;
                }else if(suggest>=15001&&suggest<=20000){
                    least = Math.round((suggest) * 8/10);
                    most =  Math.round((suggest)*discount);
                    break;
                }else if(suggest>=20001&&suggest<=30000){
                    least = Math.round((suggest) * 8/10);
                    most =  Math.round((suggest)*discount);
                    break;
                }else{
                    least = Math.round((suggest) * 8/10);
                    most =  Math.round((suggest)*discount);
                    break;
                }
        }
        originalStr = String.valueOf((int)most);
        lowestStr = String.valueOf((int)least);
        Log.d(TAG, ""+ (int)least +" "+ (int)most);

        Handler handler = new Handler();
        //newValPriceText.setText("????????????");
        handler.postDelayed(() -> {
            valRangeText.setText(lowestStr +"~"+originalStr);
            valRangeText.setTextColor(Color.RED);
        }, 2000);

    }
    private void getCompanyFixDis(){
        String function_name = "company_fix_discount";
        String company_id = getCompany_id(this);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", company_id)
                .build();
        Log.d(TAG, "company_id: "+company_id);

        //????????????
        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+PHP)
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newBuilder().connectTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES);
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //??????????????????
                //???app???????????????????????????
                runOnUiThread(() -> Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG,"responseData: "+responseData); //????????????

                try {
                    JSONArray responseArr = new JSONArray(responseData);
                    JSONObject company02 = responseArr.getJSONObject(0);
//                    Log.i(TAG,"JSONObject of order:"+order);

                    //????????????
                    fixDiscount = company02.getString("fixDiscount");
                    fixEnable = company02.getString("isEnable");
                    discount = Integer.parseInt(fixDiscount);
                    if(fixEnable.equals("1")){
                        isDiscount = true;
                    }else{
                        isDiscount = false;
                    }
                    runOnUiThread(()->{
                        /*Toast.makeText(context, "fixDiscount: "+fixDiscount+" "+
                                " isEnable: "+ fixEnable
                                +" discount: "+discount
                                +" isDiscount: "+isDiscount, Toast.LENGTH_LONG).show();*/
                        if(isDiscount){
                            current_discount.setText("????????????: "+fixDiscount + "%off");
                        }else{
                            current_discount.setText("???????????????");
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    private void getOrder(){
        //????????????????????????function_name
        String function_name = "valuation_detail";
        String company_id = getCompany_id(this);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .add("company_id", company_id)
                .add("plan", plan)
                .build();
        Log.d(TAG, "order_id: "+order_id);

        //????????????
        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+PHP)
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newBuilder().connectTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES);
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //??????????????????
                //???app???????????????????????????
                runOnUiThread(() -> Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG,"responseData: "+responseData); //????????????

                try {
                    JSONArray responseArr = new JSONArray(responseData);
                    JSONObject order = responseArr.getJSONObject(0);
//                    Log.i(TAG,"JSONObject of order:"+order);

                    //????????????
                    name = order.getString("member_name");
                    gender = order.getString("gender");
                    phone = order.getString("phone");
                    email = order.getString("email");
                    contactTime = order.getString("contact_time");
                    valuationTime = getDate(order.getString("valuation_date"));
                    if(!order.getString("valuation_time").equals("null"))
                        valuationTime = valuationTime+" "+order.getString("valuation_time");
                    if(!order.has("from_address") || order.getString("from_address").equals("null")){
                        fromAddress = order.getString("outcity")+order.getString("outdistrict")+order.getString("address1");
                    }
                    else if(order.has("from_address"))  fromAddress = order.getString("from_address");
                    if(!order.has("to_address") || order.getString("to_address").equals("null")){
                        toAddress = order.getString("incity")+order.getString("indistrict")+order.getString("address2");
                    }
                    else if(order.has("to_address")) toAddress = order.getString("to_address");
                    lowPrice = order.getString("low_price");
                    estimate_fee = order.getString("estimate_fee");
                    middlePrice = order.getString("middle_price");
                    highPrice = order.getString("high_price");
                    mvfopt = order.getString("from_elevator");
                    mvtopt = order.getString("to_elevator");
                    estimateDis = order.getString("estimate_distance");//distance
                    estimateTime = order.getString("estimate_time");//duration
                    sugCars = order.getString("SugCars");
                    program = order.getString("program");
                    remainder = order.getString("additional");
                    isAuto = order.getString("is_web");
                    memo = order.getString("memo");
                    if(memo.equals("null")) memo = "";

                    //????????????
                    runOnUiThread(() -> {
                        nameText.setText(name);
                        if(gender.equals("???")) nameTitleText.setText("??????");
                        else if(gender.equals("???")) nameTitleText.setText("??????");
                        else nameTitleText.setText("");
                        phoneText.setText(phone);
                        contactTimeText.setText(contactTime);
                        if(contactTime.equals("null")){
                            contactTimeText.setText("???????????????");
                        }
                        if(valuationTime.contains("null")){
                            String[] newVal = valuationTime.split("null");
                            valuationTimeText.setText(newVal[0]);
                        }else{
                            valuationTimeText.setText(valuationTime);
                        }
                        fromAddressText.setText(fromAddress);
                        toAddressText.setText(toAddress);
                        remainderText.setText(remainder);
                        if(sugCars.equals("null") || sugCars.isEmpty()){
                            sugCarsText.setText("???????????????");
                        }else{
                            sugCarsText.setText(sugCars);
                        }
                        if(isAuto.equals("1")){
                            if(!estimate_fee.equals("null")){
                                valPriceText.setText(estimate_fee+" (???)");
                                switch(plan){
                                    case("????????????"):
                                        float original = Integer.parseInt(middlePrice);
                                        String lowString = String.valueOf(Math.round(original*8/10));
                                        originalStr = String.valueOf((int)original);
                                        valRangeText.setText(lowString+"~"+originalStr);
                                        break;
                                    case("????????????"):
                                        original = Integer.parseInt(lowPrice);
                                        lowString = String.valueOf(Math.round(original*8/10));
                                        originalStr = String.valueOf((int)original);
                                        valRangeText.setText(lowString+"~"+originalStr);
                                        break;
                                    case("????????????"):
                                        original = Integer.parseInt(highPrice);
                                        lowString = String.valueOf(Math.round(original*8/10));
                                        originalStr = String.valueOf((int)original);
                                        valRangeText.setText(lowString+"~"+originalStr);
                                        break;

                                }
                            }
                            else{
                                valPriceText.setText("????????????");
                            }
                        }
                        else{
                            valPriceText.setText("????????????");
                            valPriceText.setTextColor(Color.RED);
                            valRangeText.setText("???????????????");
                            valRangeText.setTextColor(Color.RED);
                        }
                        memoEdit.setText(memo);
                        program_type.setText(plan);
                        //Toast.makeText(context, "program: "+program,Toast.LENGTH_LONG).show();
                    });

                    if(isAuto.equals("1")){
                        handler.postDelayed(() -> convert_furniture(),500);
                    }
//                    if(auto==0) runOnUiThread(() -> furnitureLL.setVisibility(View.GONE));
                } catch (JSONException e) {
                    e.printStackTrace();
//                    runOnUiThread(() -> Toast.makeText(context, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show());
                }

                carAdapter = new CarAdapter(cars);
                runOnUiThread(() -> {
                    carAssignRList.setLayoutManager(new LinearLayoutManager(context));
                    carAssignRList.setAdapter(carAdapter);
                });
            }
        });
    }
    private void convert_furniture(){
        String function_name = "convert_furniture";
        String company_id = getCompany_id(this);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .add("company_id", company_id)
                .build();
        Log.d(TAG, "order_id: "+order_id);

        //????????????
        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/furniture.php")
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newBuilder().connectTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES);
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //??????????????????
                //???app???????????????????????????
                runOnUiThread(() -> Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG,"responseData convert: "+responseData); //????????????

            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setPhoneBtn(){
        phoneCall_btn.setOnClickListener(v -> {
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Taipei"));
            Log.i(TAG, "?????????"+now);
            if(isContactTime(timeToStr(
                    isWeekend(String.valueOf(now.getDayOfWeek())),
                    isNight(now.getHour())))){
                callIntent();
            }
            else{
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("????????????");
                dialog.setMessage("?????????????????????????????????????????????????????????????????????????????????");
                dialog.setPositiveButton("??????", (dialog1, which) -> callIntent());
                dialog.setNegativeButton("??????", null);
                dialog.create().show();
            }
        });
    }

    private boolean isWeekend(String dayOfWeek){
        switch (dayOfWeek){
            case "MONDAY":
            case "TUESDAY":
            case "WEDNESDAY":
            case "THURSDAY":
            case "FRIDAY":
                return false;
            case "SUNDAY":
            case "SATURDAY":
                return true;
            default:
                Log.i(TAG, "the today dayOfWeek error");
        }
        return false;
    }

    private boolean isNight(int hour) {
        return (hour < 6 || hour > 19);
    }

    private String timeToStr(boolean isWeekend, boolean isNight) {
        if(isWeekend && isNight) return "????????????";
        if(!isWeekend && isNight) return "????????????";
        if(isWeekend) return "????????????";
        return "????????????";
    }

    private boolean isContactTime(String currentTime) {
        String[] token = contactTime.split(",");
        for (String ct : token) {
            if (ct.equals(currentTime))
                return true;
        }
        return false;
    }

    private void callIntent(){
        Intent call_intent = new Intent(Intent.ACTION_DIAL);
        call_intent.setData(Uri.parse("tel:"+phone));
        startActivity(call_intent);
    }

    private int monthToInt(String month){
        switch (month){
            case "JANUARY":
                return 1;
            case "FEBRUARY":
                return 2;
            case "MARCH":
                return 3;
            case "APRIL":
                return 4;
            case "MAY":
                return 5;
            case "JUNE":
                return 6;
            case "JULY":
                return 7;
            case "AUGUST":
                return 8;
            case "SEPTEMBER":
                return 9;
            case "OCTOBER":
                return 10;
            case "NOVEMBER":
                return 11;
            case "DECEMBER":
                return 12;
            default:
                return 0;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        bundle = getIntent().getExtras();
        getFromEdit= getIntent().getExtras();


    }

    private void setCheckBtn(){
        check_btn.setOnClickListener(v -> {
            String movingDate = movingDateText.getText().toString().trim();
            String movingTime = movingTimeText.getText().toString().trim();
            String moving_date = movingDate + " " + movingTime;
            String estimate_worktime = worktimeEdit.getText().toString().trim();
            String fee = priceEdit.getText().toString().trim();
            memo = memoEdit.getText().toString();
//                Log.d(TAG,"check_price_btn, fee: "+fee+", memo: "+memo);

            boolean check = true;

            if(checkEmpty(estimate_worktime, fee)) check = false;
            if(checkCarsViewEmpty()) check = false;
            if(!check) return;

            //updateValuation(moving_date, estimate_worktime, fee);
            //updateCarDemand();
            updateSugCars();
            //finishValuation();
            bundle.putString("name", name);
            bundle.putString("gender",gender);
            bundle.putString("program", plan);
            bundle.putString("phone", phone);
            bundle.putString("contactTime", contactTime);
            bundle.putString("valuationTime", valuationTime);
            bundle.putString("fromAddress", fromAddress);
            bundle.putString("toAddress", toAddress);
            bundle.putString("moving_date", moving_date);
            bundle.putString("estimate_worktime", estimate_worktime);
            bundle.putString("fee", fee);
            bundle.putString("valRange", valRangeText.getText().toString());
            bundle.putString("valPrice", valPriceText.getText().toString());
            bundle.putString("remainder", remainder);
            bundle.putString("memo", memo);
            bundle.putString("isAuto", isAuto);
            bundle.putString("remainder", remainder);
            bundle.putString("isAuto", isAuto);
            bundle.putString("order_id", order_id);
            bundle.putString("discount", current_discount.getText().toString());
            bundle.putString("memo", memoEdit.getText().toString());
            bundle.putString("email", email);
            bundle.putString("plan", plan);
            bundle.putString("isAuto", isAuto);
            bundle.putString("carDemand", carsToString());
            if(newValPriceText.getText().length()!=0) bundle.putString("newPrice", newValPriceText.getText().toString());

            Intent intent = new Intent(context, ConfirmValuation_Detail.class);

            intent.putExtras(bundle);
            startActivity(intent);




        });
    }
    private void callDialog(){
        new AlertDialog.Builder(context)
                .setTitle("?????????")
                .setMessage("????????????????????????????????????\n????????????????????????????????????\n??????????????????")
                .setPositiveButton( "??????", (dialog, which) -> {
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        Intent intent = new Intent(context, Valuation.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }, 1000);
                })
                .show();
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
                               .setTitle("????????????")
                               .setMessage("\n        ??????????????????????????????????????????")
                               .setPositiveButton( "??????", (dialog, which) -> callDialog())
                               .show());
                   }else{
                       runOnUiThread(() -> runOnUiThread(() -> new AlertDialog.Builder(context)
                               .setTitle("????????????")
                               .setMessage("\n        ????????????????????????????????????")
                               .setPositiveButton( "??????", (dialog, which) -> callDialog())
                               .show()));
                   }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean checkEmpty(String estimate_worktime, String fee){
        boolean check = false;
        if(TextUtils.isEmpty(movingDateText.getText().toString())){
            movingDateText.setError("???????????????");
            Log.d(TAG, "????????????");
            check = true;
        }
        if(TextUtils.isEmpty(movingTimeText.getText().toString())){
            movingTimeText.setError("???????????????");
            Log.d(TAG, "????????????");
            check = true;
        }
        if(TextUtils.isEmpty(estimate_worktime) || estimate_worktime.length() == 0){
            worktimeEdit.setError("???????????????");
            Log.d(TAG, "????????????");
            check = true;
        }
        if(TextUtils.isEmpty(fee) || fee.length() == 0){
            priceEdit.setError("?????????????????????");
            Log.d(TAG, "??????????????????");
            check = true;
        }
        if(isAuto.equals("1")){
            if(priceEdit.getText().length()!=0 ){
                if(priceEdit.getText().toString().equals("0")){
                    priceEdit.setError("?????????????????????????????????0");
                    check = true;
                }
                Log.d(TAG, "valPrice:"+getValPrice(0)+"~"+getValPrice(1));
                if(Integer.parseInt(fee) < getValPrice(0)){
                    priceEdit.setError("????????????????????????????????????"+getValPrice(0));
                    Log.d(TAG, "??????????????????");
                    check = true;
                }


            }
            if(priceEdit.getText().length()!=0 && originalStr.length()!=0){

                if(Integer.valueOf(fee)>Integer.valueOf(originalStr) && memo.length() ==0){
                    memoEdit.setError("???????????????");
                    check = true;
                }
            }else{
                if(priceEdit.getText().length()!=0 && fee.length()!=0){
                    if(Integer.valueOf(fee)>Integer.valueOf(originalStr) && memo.length() ==0){
                        memoEdit.setError("???????????????");
                        check = true;
                    }
                }

            }
        }
            if(getFromEdit.getString("suggestPrice")==null){
                //setValPrice();
                //valPriceText.setText("3600~8000");
            /*if(Integer.valueOf(fee)>Integer.valueOf("8000") && memo.isEmpty()){
                memoEdit.setError("???????????????");
                check = true;
            }*/
                if(valPriceText.getText().length()==0){
                    priceEdit.setError("????????????????????????????????????");
                    check = true;
                }
            /*if(valPriceText.getText().length()!=0){
                Log.d(TAG, "valPrice:"+getValPrice(0)+"~"+getValPrice(1));
                if(Integer.parseInt(fee) < getValPrice(0)){
                    priceEdit.setError("????????????????????????????????????"+getValPrice(0));
                    Log.d(TAG, "??????????????????");
                    check = true;
                }

            }else{

            }*/
            }





        return check;
    }

    private void setValPrice() {
        Log.d(TAG, ""+bundle.getBoolean("isEdited"));
        if (bundle.getBoolean("isEdited")){
            //valPriceText.setPaintFlags(valPriceText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            //newValPriceText.setVisibility(View.INVISIBLE);
            valPrice = 1;
        }
        else {
            //valPriceText.setPaintFlags(valPriceText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            //newValPriceText.setVisibility(View.INVISIBLE);
            valPrice = -1;
        }
    }

    private int getValPrice(int i){
        String valPriceStr = valRangeText.getText().toString();
        String[] token = valPriceStr.split("~");
        Log.d(TAG, "valPriceStr: "+valPriceStr+", token:"+token[0]+" & "+token[1]);
        return Integer.parseInt(token[i]);
    }

    private boolean checkCarsViewEmpty(){
        boolean isEmpty = false;
        int CAR_LENGTH = 3;
        for(int position = carAdapter.getItemCount()-1; position >= 0; position--){
            View view = carAssignRList.getLayoutManager().findViewByPosition(position);
            if(view != null){
                int check_empty = 0;
                EditText weight_edit = view.findViewById(R.id.weight_CI);
                EditText type_edit = view.findViewById(R.id.type_CI);
                EditText num_edit = view.findViewById(R.id.num_CI);

                check_empty += checkCarViewStr(weight_edit, position, 0);
                check_empty += checkCarViewStr(type_edit, position, 1);
                check_empty += checkCarViewStr(num_edit, position, 2);

                Log.d(TAG, "car: "+Arrays.toString(cars.get(position)));
                if(check_empty == CAR_LENGTH) {
                    Log.d(TAG, "car row "+position+" is empty");
                    weight_edit.setError(null);
                    type_edit.setError(null);
                    num_edit.setError(null);
                    if(position != 0){
                        Log.d(TAG, "car row "+position+" delete");
                        cars.remove(position);
                        carAdapter.notifyDataSetChanged();
                    }
                }
                else if(check_empty > 0){
                    isEmpty = true;
                }

                if(position == 0){
                    if(check_empty == CAR_LENGTH){
                        Log.d(TAG, "cars first row is empty"+check_empty+")");
                        firstRowEmpty = 1;
                    }
                    else{
                        Log.d(TAG, "cars first row is not empty("+check_empty+")");
                        firstRowEmpty = 0;
                    }
                }
            }
            else {
                Log.d(TAG, "checkCarsViewEmpty. view "+position+" is null");
                int check_empty = 0;

                Log.d(TAG, "car: "+Arrays.toString(cars.get(position)));
                for(int i = 0; i < CAR_LENGTH; i++)
                    if(cars.get(position)[i].equals("")) check_empty += 1;

                if(check_empty == CAR_LENGTH) {
                    Log.d(TAG, "car row "+position+" empty(out of version)");
                    if(position != 0){
                        Log.d(TAG, "cars "+position+" delete(out of version)");
                        cars.remove(position);
                        carAdapter.notifyDataSetChanged();
                    }
                }
                else if(check_empty > 0){
                    isEmpty = true;
                }
                else Log.d(TAG, "car row ("+(3-check_empty)+"): "+Arrays.toString(cars.get(position)));
            }
        }

        Log.d(TAG, "cars("+cars.size()+"):"+carsToString());
        Log.d(TAG, "___________________________________");
        return isEmpty;
    }

    private int checkCarViewStr(EditText editText, int position, int i){
        String str = editText.getText().toString();
        if(!str.isEmpty()) cars.get(position)[i] = str;
        else {
            editText.setError("?????????");
            cars.get(position)[i] = "";
            return 1;
        }
        return 0;
    }

    private void updateCarDemand(){
        String function_name = "add_vehicleDemands";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .add("company_id", getCompany_id(context))
                .add("vehicleItems", carsToString())
                .build();
        Log.i(TAG, "carDamand. order_id: "+order_id+", 'vehicleItems: "+carsToString());

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
                String responseData = response.body().string();
                Log.d(TAG, "submit update_carDemand responseData: " + responseData);
            }
        });
    }

    private String carsToString(){
        String str = "[";
        for(int i = firstRowEmpty; i < cars.size(); i++){
            if(i != firstRowEmpty) str = str + ", ";
            String[] car = cars.get(i);
            str = str + "[";
            for(int ii = 0; ii < car.length; ii++){
                if(ii != 0) str = str+", ";
                if(ii == 1) str = str +"\" "+car[ii]+"\"";
                else str = str + "\""+ car[ii] +"\"";
            }
            str = str +"]";
//            Log.d(TAG, "car str:"+str);
        }
        str = str + "]";
        return str;
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
                runOnUiThread(() -> Toast.makeText(context, "??????????????????", Toast.LENGTH_LONG).show());
                Log.d(TAG, "submit update_valuation responseData: " + responseData);
            }
        });
    }
    private void updateSugCars(){
        String function_name = "update_sugCars";
        String company_id = getCompany_id(context);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id",company_id)
                .add("order_id", order_id)
                .add("sugCars",sugCarsText.getText().toString())

                .build();
        Log.d(TAG, "check_btn: order_id: "+order_id+", "+
                "suggest Cars : "+ sugCarsText.getText().toString());

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
                Log.d(TAG, "responseData: " + responseData);
            }
        });
    }
    private void priceChangeAlert(){
        priceEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                new androidx.appcompat.app.AlertDialog.Builder(context)
                        .setTitle("????????????")
                        .setMessage("????????????????????????????????????????????????????????????")
                        .setPositiveButton("?????????", (dialog, which) -> dialog.dismiss())
                        .create()
                        .show();
            }
        });
    }

    public void linking(){
        nameText = findViewById(R.id.name_VBD);
        nameTitleText = findViewById(R.id.nameTitle_VBD);
        phoneText = findViewById(R.id.phone_VBD);
        phoneCall_btn = findViewById(R.id.call_btn_VBD);
        contactTimeText = findViewById(R.id.contactTime_VBD);
        valuationTimeText = findViewById(R.id.valuationTime_VBD);
        fromAddressText = findViewById(R.id.FromAddress_VBD);
        toAddressText = findViewById(R.id.ToAddress_VBD);
        furniture_btn = findViewById(R.id.edit_furniture_btn_VBD);
        remainderText = findViewById(R.id.notice_VBD);
        movingDateText = findViewById(R.id.movingDate_VBD);
        movingTimeText = findViewById(R.id.movingTime_VBD);
        worktimeEdit = findViewById(R.id.worktime_VBD);
        priceEdit = findViewById(R.id.price_VBD);
        check_btn = findViewById(R.id.check_evaluation_btn);
        furnitureLL = findViewById(R.id.furniture_LL_VBD);
        valPriceText = findViewById(R.id.valPrice_VBD);
        newValPriceText = findViewById(R.id.newValPrice_VBD);
        memoEdit = findViewById(R.id.PS_VBD);
        carAssignRList = findViewById(R.id.car_assign_VBD);
        sugCarsText = findViewById(R.id.sugText);
        valRangeText = findViewById(R.id.range_text);
        program_type = findViewById(R.id.setProgram);
        current_discount = findViewById(R.id.cur_dis);
        memoSpinner = findViewById(R.id.memoSpr);
        onlineText = findViewById(R.id.valuate_textView);
    }

    private void globalNav(){
        ImageView back_btn = findViewById(R.id.back_imgBtn_VBD);
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

       back_btn.setOnClickListener(v -> {
           Intent valB = new Intent(this, Valuation.class);
           valB.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
           startActivity(valB);
       });

        valuation_btn.setOnClickListener(v -> {
            Intent valuation_intent = new Intent(context, Valuation.class);
            startActivity(valuation_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
        order_btn.setOnClickListener(v -> {
            Intent order_intent = new Intent(context, Order.class);
            startActivity(order_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
        calendar_btn.setOnClickListener(v -> {
            Intent calender_intent = new Intent(context, Calendar.class);
            startActivity(calender_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
        system_btn.setOnClickListener(v -> {
            Intent system_intent = new Intent(context, System.class);
            startActivity(system_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
        setting_btn.setOnClickListener(v -> {
            Intent setting_intent = new Intent(context, Setting.class);
            startActivity(setting_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
    }
    public void onBackPressed(){
        super.onBackPressed();
        this.finish();

    }
}
