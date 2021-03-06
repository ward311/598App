package com.example.homerenting_prototype_one.order;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.furniture.Edit_Furniture;
import com.example.homerenting_prototype_one.setting.Setting;
import com.example.homerenting_prototype_one.Signature_Pad;
import com.example.homerenting_prototype_one.system.System;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.furniture.Furniture_Location;
import com.example.homerenting_prototype_one.valuation.Valuation;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
import static com.example.homerenting_prototype_one.show.global_function.getTime;

public class Today_Detail extends AppCompatActivity {
    Context context = Today_Detail.this;

    ConstraintLayout cLayout;

    TextView nameText, nameTitleText, phoneText, movingTimeText, fromAddressText, toAddressText;
    TextView remainderText, carText, staffText, worktimeText, feeText, toPriceText, finalPriceText, priceUnitText, extraPriceText;
    TextView today_pay;
    EditText changePriceText, memoEdit;

    String name, gender, phone, contact_address, movingTime, fromAddress, toAddress;
    String remainder, car, staff, worktime, fee, memo, status, isWeb, additional_price;
    String depositFee;
    String order_id, member_id;
    String plan;
    Button furnitureBtn, changePriceBtn, check_btn, sign_btn, change_furniture, cash_btn;
    AlertDialog.Builder dialog;
    boolean change = false, check = false, result = false;

    int price_origin, price;

    Bundle bundle;
    Bundle fromBooking = new Bundle();


    String TAG = "Today_Detail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today__detail);
        Button call_btn = findViewById(R.id.call_btn);
        cash_btn = findViewById(R.id.cashier_btn);
//        bundle = new Bundle();
//        bundle.putString("order_id", "74");
        bundle = getIntent().getExtras();
        order_id = bundle.getString("order_id");
        plan = bundle.getString("plan");
        memo = bundle.getString("memo");
        linking(); //???xml?????????????????????java
        changePriceMark(); //???+-???????????????
        changePrice();

        //??????
        String function_name = "order_detail";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .add("company_id", getCompany_id(context))
                .add("plan", plan)
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
                Log.d(TAG, "Failed: " + e.getMessage()); //??????????????????
                //???app???????????????????????????
                runOnUiThread(() -> Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
//                Log.d(TAG,"responseData of get_order: "+responseData); //????????????

                try {
                    JSONArray responseArr = new JSONArray(responseData);
                    JSONObject order = responseArr.getJSONObject(0);
                    Log.i(TAG,"order:"+order);

                    //????????????
                    name = order.getString("member_name");
                    member_id = order.getString("member_id");
                    gender = order.getString("gender");
                    phone = order.getString("phone");
                    contact_address = order.getString("contact_address");
                    movingTime = getDate(order.getString("moving_date"))+" "+getTime(order.getString("moving_date"));
                    if(!order.has("from_address") || order.getString("from_address").equals("null")){
                        fromAddress = order.getString("outcity")+order.getString("outdistrict")+order.getString("address1");
                    }
                    else if(order.has("from_address"))  fromAddress = order.getString("from_address");
                    if(!order.has("to_address") || order.getString("to_address").equals("null")){
                        toAddress = order.getString("incity")+order.getString("indistrict")+order.getString("address2");
                    }
                    else if(order.has("to_address")) toAddress = order.getString("to_address");
                    remainder = order.getString("additional");
                    worktime = order.getString("estimate_worktime")+"??????";
                    fee = order.getString("estimate_fee");
                    if(fee.equals("null")) fee = "0";
                    price_origin = Integer.parseInt(fee);
                    //additional_price = order.getString("additional_price");
                    //if(additional_price.equals("null")) additional_price = "0";
                    depositFee = order.getString("deposit_fee");
                    if(depositFee.equals("null")){
                        depositFee = "0";
                    }
                    memo = order.getString("memo");
                    if(memo.equals("null")) memo = "";
                    status = order.getString("order_status");
                    isWeb = order.getString("is_web");
                    if(isWeb.equals("0")){
                        runOnUiThread(()-> {
                            check_btn.setVisibility(View.GONE);
                            sign_btn.setX(450);
                            sign_btn.setText("????????????");
                        });

                    }else{
                        runOnUiThread(()-> check_btn.setVisibility(View.GONE));
                    }
                    //????????????
                    runOnUiThread(() -> {
                        nameText.setText(name);
                        if(gender.equals("???")) nameTitleText.setText("??????");
                        else if(gender.equals("???")) nameTitleText.setText("??????");
                        else nameTitleText.setText("");
                        phoneText.setText(phone);
                        movingTimeText.setText(movingTime);
                        fromAddressText.setText(fromAddress);
                        toAddressText.setText(toAddress);
                        remainderText.setText(remainder);
                        worktimeText.setText(worktime);
                        feeText.setText(fee);
                        extraPriceText.setText(depositFee);
                        finalPriceText.setText(fee);
                        memoEdit.setText(memo);
                        int paymentToday = Integer.parseInt(fee) - Integer.parseInt(depositFee);
                        today_pay.setText(String.valueOf(paymentToday));

                    });

                    setFurniture_btn();
                } catch (JSONException e) {
                    e.printStackTrace();
//                    runOnUiThread(() -> Toast.makeText(Today_Detail.this, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show());
                }
            }
        });

        getVehicleData();
        getStaffData();
        cash_btn.setOnClickListener(v -> {
                Intent sign_intent = new Intent(context, Signature_Pad.class);
                new AlertDialog.Builder(context)
                        .setTitle("??????????????????")
                        .setMessage("??????????????????????????????????????????????????????")
                        .setPositiveButton("???", (dialog, which) -> {
                            updateAddress();
                            int finalPrice = Integer.parseInt(finalPriceText.getText().toString());
                            int additional_fee = Integer.parseInt(extraPriceText.getText().toString());
                            String moving_fee = String.valueOf(finalPrice);
                            memo = memoEdit.getText().toString();
                            Log.d(TAG,"check_price_btn, fee: "+fee+", memo: "+memo);
                            bundle.putString("order_id", order_id);
                            bundle.putString("fee", moving_fee);
                            bundle.putString("memo", memoEdit.getText().toString());
                            bundle.putString("deposit", depositFee);
                            sign_intent.putExtras(bundle);
                            startActivity(sign_intent);
                        })
                        .setNegativeButton("???", (dialog, which) -> {
                            int finalPrice = Integer.parseInt(finalPriceText.getText().toString());
                            int additional_fee = Integer.parseInt(extraPriceText.getText().toString());
                            String moving_fee = String.valueOf(finalPrice);
                            memo = memoEdit.getText().toString();
                            Log.d(TAG,"check_price_btn, fee: "+fee+", memo: "+memo);
                            bundle.putString("order_id", order_id);
                            bundle.putString("fee", moving_fee);
                            bundle.putString("memo", memo);
                            bundle.putString("deposit", depositFee);
                            sign_intent.putExtras(bundle);
                            startActivity(sign_intent);
                        })
                        .create()
                        .show();

        });
        /*????????????*/
        check_btn.setOnClickListener(v -> {
            int finalPrice = Integer.parseInt(finalPriceText.getText().toString());
            int additional_fee = Integer.parseInt(extraPriceText.getText().toString());
            fee = String.valueOf(finalPrice);
            memo = memoEdit.getText().toString();
            Log.d(TAG,"check_price_btn, moving_fee: "+fee+", extra_fee: "+ additional_fee +", memo: "+memo);
            update_today_order(); /*??????????????????*/
            checkTotalPrice(); /* URL GET_METHOD ?????????????????? */

        });

        call_btn.setOnClickListener(v -> {
            Intent call_intent = new Intent(Intent.ACTION_DIAL);
            call_intent.setData(Uri.parse("tel:"+phone));
            startActivity(call_intent);
        });
        change_furniture.setOnClickListener( v->{
            Intent change_intent = new Intent(this, Edit_Furniture.class);
            bundle.putBoolean("fromOrder", true);
            bundle.putString("order_id", order_id);
            bundle.putString("isWeb", isWeb);
            fromBooking.putString("clickFromBooking","0");
            change_intent.putExtras(bundle);
            change_intent.putExtras(fromBooking);
            startActivity(change_intent);
        });
        sign_btn.setOnClickListener(v -> {
            /*int finalPrice = Integer.parseInt(finalPriceText.getText().toString());
            int additional_fee = Integer.parseInt(extraPriceText.getText().toString());
            String moving_fee = String.valueOf(finalPrice);
            memo = memoEdit.getText().toString();
            Log.d(TAG,"check_price_btn, fee: "+fee+", memo: "+memo);

            bundle.putString("order_id", order_id);
            bundle.putString("fee", moving_fee);
            bundle.putString("memo", memo);
            sign_intent.putExtras(bundle);*/
            //startActivity(sign_intent);
            int price = Integer.parseInt(feeText.getText().toString());
            int finalPrice = Integer.parseInt(finalPriceText.getText().toString());
            if(finalPrice == 0){
                changePriceText.setError("?????????????????????0");
            }else if(price != finalPrice && memoEdit.getText().length() == 0){
                memoEdit.setError("???????????????????????????");
            }else{
                Intent confirm_intent = new Intent(context, Confirm_Detail.class);
                Bundle confirm = new Bundle();
                confirm.putString("name", name);
                confirm.putString("member_id", member_id);
                confirm.putString("gender", gender);
                confirm.putString("phone", phone);
                confirm.putString("mvTime", movingTime);
                confirm.putString("mvOut", fromAddress);
                confirm.putString("mvIn", toAddress);
                confirm.putString("additional", remainder);
                confirm.putString("mvFee", finalPriceText.getText().toString());
                confirm.putString("deposit", depositFee);
                confirm.putString("memo", memoEdit.getText().toString());//??????
                confirm.putString("order_id", order_id);
                confirm.putString("plan", plan);
                confirm_intent.putExtras(confirm);
                startActivity(confirm_intent);
            }

        });

        globalNav();
    }

    private void checkTotalPrice(){
        Request request = new Request.Builder()
                .url("http://598new.ddns.net/598_new_20211026/appecpay.php?order_id="+order_id+"&company_id="+getCompany_id(context))
                .build();
        Log.d(TAG, "order_id: "+ order_id+ " company_id: "+getCompany_id(context));
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //??????????????????
                //???app???????????????????????????
                runOnUiThread(() -> Toast.makeText(context, "????????????", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                        runOnUiThread(() ->{
                            LayoutInflater inflater = getLayoutInflater();
                            View view = inflater.inflate(R.layout.qrcode_image, null);
                            ImageView qrcodeView = view.findViewById(R.id.qrcode_img_QI);

                            String url = "http://598new.ddns.net/598_new_20211026/appecpay.php?order_id="+order_id+"&company_id="+getCompany_id(context);
                            Log.d(TAG, "website: "+ url);
                            try {
                                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                                Bitmap bitmap = barcodeEncoder.encodeBitmap(url, BarcodeFormat.QR_CODE, 600, 600);
                                qrcodeView.setImageBitmap(bitmap);

                            } catch (WriterException e){
                                e.printStackTrace();
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("??????QR CODE");
//               builder.setMessage("?????????QR CODE");
                            builder.setView(view);
                            builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(check){
                                        //change_order_status();
                                        //receiveResult();
                                        Toast.makeText(context, "???????????? - ?????????", Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Toast.makeText(context, "??????????????????", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            builder.setNegativeButton("??????", (dialog, which) -> { });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        });




            }
        });
    }
    private void receiveResult(){
        RequestBody body = new FormBody.Builder()
                .add("order_id", order_id)
                .build();

        Request request = new Request.Builder()
                .url("http://598new.ddns.net/598_new_20211026/receive.php")
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //??????????????????
                //???app???????????????????????????
                runOnUiThread(() -> Toast.makeText(context, "????????????", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG, "responseData of receive_result: " + responseData); //????????????

                try {
                    JSONArray responseArr = new JSONArray(responseData);
                    JSONObject company = responseArr.getJSONObject(0);
                    runOnUiThread(() -> Toast.makeText(context, "????????????", Toast.LENGTH_LONG).show());
                    Log.d(TAG, ""+company);
                } catch (JSONException e) {
                    if(!responseData.equals("null")) e.printStackTrace();
                }
            }
        });
    }
    private void getVehicleData(){
        RequestBody body = new FormBody.Builder()
                .add("order_id", order_id)
                .build();

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/get_data/vehicle_each_detail.php")
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //??????????????????
                //???app???????????????????????????
                runOnUiThread(() -> Toast.makeText(context, "????????????", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG, "responseData of vehicle_each_detail: " + responseData); //????????????

                try {
                    JSONArray responseArr = new JSONArray(responseData);

                    int i;
                    car = "";
                    for (i = 0; i < responseArr.length(); i++) {
                        JSONObject vehicle_assign = responseArr.getJSONObject(i);
                        if(!vehicle_assign.has("vehicle_id")) break;
                        Log.i(TAG, "vehicle_assign:" + vehicle_assign);
                        car = car+vehicle_assign.getString("plate_num")+" ";
                    }
                    Log.d(TAG, "car: "+car);

                    runOnUiThread(() -> carText.setText(car));
                } catch (JSONException e) {
                    if(!responseData.equals("null")) e.printStackTrace();
                }
            }
        });
    }


    private void getStaffData(){
        RequestBody body = new FormBody.Builder()
                .add("order_id", order_id)
                .build();

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/get_data/staff_detail.php")
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
                runOnUiThread(() -> Toast.makeText(context, "????????????", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG, "responseData of staff_detail: " + responseData); //????????????
                staff = "??????????????????";

                try {
                    JSONArray responseArr = new JSONArray(responseData);

                    int i;
                    staff = "";
                    for (i = 0; i < responseArr.length(); i++) {
                        JSONObject staff_assign = responseArr.getJSONObject(i);
                        if(!staff_assign.has("staff_id")) break;
                        Log.i(TAG, "staff:" + staff_assign);
                        staff = staff+staff_assign.getString("staff_name");

                        String pay = staff_assign.getString("pay");
                        if(!pay.equals("-1")) staff = staff + "("+pay+")";

                        staff = staff + " ";
                    }

                } catch (JSONException e) {
                    if(!responseData.equals("null")) e.printStackTrace();
                }

                runOnUiThread(() -> staffText.setText(staff));
            }
        });
    }

    private void changePriceMark(){
        changePriceBtn.setOnClickListener(v -> {
            if(changePriceBtn.getText().toString().equals("+"))
                changePriceBtn.setText("-");
            else changePriceBtn.setText("+");

            String changeprice = changePriceText.getText().toString();
            if(!changeprice.isEmpty()){
                if(changePriceBtn.getText().toString().equals("+"))
                    price = price_origin+Integer.parseInt(changeprice);
                else
                    price = price_origin - Integer.parseInt(changeprice);
                finalPriceText.setText(String.valueOf(price));
            }
        });
    }

    private void changePrice(){
        changePriceText.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                new AlertDialog.Builder(context)
                        .setTitle("????????????")
                        .setMessage("???????????????????????????????????????")
                        .setPositiveButton("?????????", (dialog, which) -> dialog.dismiss())
                        .create()
                        .show();
            }
        });
        changePriceText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String changeprice = changePriceText.getText().toString();
                int payment;
                if(!changeprice.isEmpty()){
                    if(changePriceBtn.getText().toString().equals("+")) {
                        price = price_origin + Integer.parseInt(changeprice);
                        payment = price - Integer.parseInt(depositFee);
                    }
                    else{
                        price = price_origin - Integer.parseInt(changeprice);
                        if(price < 0) price = 0;
                        payment = price - Integer.parseInt(depositFee);
                        if(payment < 0) payment = 0;

                    }
                    finalPriceText.setText(String.valueOf(price));
                    today_pay.setText(String.valueOf(payment));
                    if(!change){
                        change = true;
                        setPriceUnitPlace();
                        toPriceText.setVisibility(View.VISIBLE);
                        finalPriceText.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    change = false;
                    finalPriceText.setText(String.valueOf(price_origin));
                    setPriceUnitPlace();
                    toPriceText.setVisibility(View.INVISIBLE);
                    finalPriceText.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    private void updateAddress(){
        String function_name = "modify_contact_address";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("member_id", member_id)

                .build();
        Log.d(TAG, "update member_id: "+member_id+", address: "+toAddress);
        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/functional.php")
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
                runOnUiThread(() -> Toast.makeText(context, "????????????", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG, "responseData of update_address: " + responseData); //????????????
                runOnUiThread(() -> {
                    if(responseData.contains("success"))
                        Toast.makeText(context, "??????????????????", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(context, "??????????????????", Toast.LENGTH_LONG).show();
                });

            }
        });
    }
    private void setPriceUnitPlace(){
        ConstraintSet s = new ConstraintSet();
        s.clone(cLayout);
        if(change) s.connect(priceUnitText.getId(), ConstraintSet.START, finalPriceText.getId(), ConstraintSet.END, 5);
        else s.connect(priceUnitText.getId(), ConstraintSet.START, feeText.getId(), ConstraintSet.END, 5);
        s.applyTo(cLayout);
    }

    private void linking(){
        cLayout = findViewById(R.id.CLayout_OTD);
        nameText = findViewById(R.id.name_OTD);
        nameTitleText = findViewById(R.id.nameTitle_OTD);
        phoneText = findViewById(R.id.phone_OTD);
        movingTimeText = findViewById(R.id.movingTime_OTD);
        fromAddressText = findViewById(R.id.FromAddress_OTD);
        toAddressText = findViewById(R.id.ToAddress_OTD);
        furnitureBtn = findViewById(R.id.furniture_btn_OTD);
        remainderText = findViewById(R.id.notice_OTD);
        carText = findViewById(R.id.car_OTD);
        staffText = findViewById(R.id.staff_OTD);
        worktimeText = findViewById(R.id.worktime_OTD);
        feeText = findViewById(R.id.price_OTD);
        toPriceText = findViewById(R.id.toPrice_OTD);
        extraPriceText = findViewById(R.id.extra_Price);
        finalPriceText = findViewById(R.id.finalPrice_OTD);
        priceUnitText = findViewById(R.id.priceUnitText_OTD);
        changePriceBtn = findViewById(R.id.changePrice_btn_OTD);
        changePriceText = findViewById(R.id.changePrice_OTD);
        memoEdit = findViewById(R.id.PS_OTD);
        check_btn = findViewById(R.id.check_btn_OTD);
        sign_btn = findViewById(R.id.signature_btn);
        change_furniture = findViewById(R.id.furniture_btn);
        today_pay = findViewById(R.id.today_needPay);
    }

    private void setFurniture_btn(){
        furnitureBtn.setOnClickListener(v -> {
            Intent detail_intent = new Intent();
            detail_intent.setClass( context, Furniture_Location.class);
            bundle.putString( "key","order" );
            detail_intent.putExtras(bundle);
            startActivity( detail_intent);
        });
    }

    private void update_today_order(){
        String function_name = "update_todayOrder";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .add("company_id", getCompany_id(context))
                .add("accurate_fee", fee) /*??????????????????*/
                .add("memo", memo)
                .build();

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/functional.php")
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
                runOnUiThread(() -> Toast.makeText(context, "????????????", Toast.LENGTH_LONG).show());
                check = false;
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG, "responseData of update_today_order: " + responseData);
                check = responseData.contains("success");
            }
        });
    }

    private void change_order_status(){
        String function_name = "change_status";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", getCompany_id(context))
                .add("table", "orders")
                .add("order_id", order_id)
                .add("status", "done")
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
                runOnUiThread(() -> Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();

                if(responseData.contains("success")) {
                    runOnUiThread(() -> {
                        Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            //Intent intent = new Intent(context, Order_Today.class);
                            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            /*??????????????????????????????????????????????????????????????? ( ref. Signature_Pad : change_order_status() )*/
                            Log.d(TAG, "responseData of change_status: " + responseData);
                            Toast.makeText(context, "???????????? - ?????????", Toast.LENGTH_LONG).show();
                            //startActivity(intent);
                        }, 500);

                    });

                }
            }
        });
    }

    private void globalNav(){
        ImageView back_btn = findViewById(R.id.back_imgBtn);
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);
        back_btn.setOnClickListener(v -> {
            Intent calendar = new Intent(this, Calendar.class);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            calendar.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(calendar);
            this.finish();
        });
        //??????nav
        valuation_btn.setOnClickListener(v -> {
            Intent valuation_intent = new Intent(Today_Detail.this, Valuation.class);
            startActivity(valuation_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
        order_btn.setOnClickListener(v -> {
            Intent order_intent = new Intent(Today_Detail.this, Order.class);
            startActivity(order_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
        calendar_btn.setOnClickListener(v -> {
            Intent calender_intent = new Intent(Today_Detail.this, Calendar.class);
            startActivity(calender_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
        system_btn.setOnClickListener(v -> {
            Intent system_intent = new Intent(Today_Detail.this, System.class);
            startActivity(system_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
        setting_btn.setOnClickListener(v -> {
            Intent setting_intent = new Intent(Today_Detail.this, Setting.class);
            startActivity(setting_intent);
            overridePendingTransition(R.anim.up_from_bottom, R.anim.fade_in);
        });
    }
}
