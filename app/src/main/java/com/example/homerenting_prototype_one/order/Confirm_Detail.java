package com.example.homerenting_prototype_one.order;

import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.Signature_Pad;
import com.example.homerenting_prototype_one.adapter.base_adapter.LocationAdapter;
import com.example.homerenting_prototype_one.adapter.base_adapter.NoDataAdapter;
import com.example.homerenting_prototype_one.furniture.Furniture_Location;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

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

public class Confirm_Detail extends AppCompatActivity {
    String TAG = "Confirm_Detail";
    String PHP3 = "/furniture.php";
    String floor;
    String room_name;
    String name, gender, phone, movingTime, fromAddress, toAddress, additional, fee, deposit, memo;
    String order_id, member_id;
    String paid;
    String plan;
    Button pay;
    TextView nameText, nameTitleText, phoneText,movingTimeText, fromAddressText, toAddressText;
    TextView remainderText, feeText, depositText;
    TextView totalAmount;
    EditText memoEdit;
    ImageView back_btn;
    ListView furniture_list;
    ArrayList<String[]> data = new ArrayList<>();
    Bundle bundle;
    Context context = Confirm_Detail.this;

    boolean check = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_detail);
        linking();
        bundle = getIntent().getExtras();
        name = bundle.getString("name");
        gender = bundle.getString("gender");
        phone = bundle.getString("phone");
        movingTime = bundle.getString("mvTime");
        fromAddress = bundle.getString("mvOut");
        toAddress = bundle.getString("mvIn");
        additional = bundle.getString("additional");
        fee = bundle.getString("mvFee");
        deposit = bundle.getString("deposit");
        memo = bundle.getString("memo");
        order_id = bundle.getString("order_id");
        member_id = bundle.getString("member_id");
        plan = bundle.getString("plan");
        setText();
        int numFee = Integer.parseInt(fee);
        int numDeposit = Integer.parseInt(deposit);
        int finalAmount = numFee - numDeposit;

        totalAmount.setText(String.valueOf(finalAmount));
        back_btn.setOnClickListener(v -> this.finish());
        getFurniture();
        pay.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("選擇付款方式");

            builder.setPositiveButton("線上付款", (dialog, which) -> new AlertDialog.Builder(context)
                    .setTitle("更新會員資料")
                    .setMessage("是否同意會員聯絡地址更新為搬入地址？")
                    .setPositiveButton("是", (dialog1, which1) -> {
                        updateAddress();
                        int finalPrice = Integer.parseInt(fee);
                        String moving_fee = String.valueOf(finalPrice);
                        memo = memoEdit.getText().toString();
                        Log.d(TAG,"check_price_btn, fee: "+fee+", memo: "+memo);
                        bundle.putString("order_id", order_id);
                        bundle.putString("fee", moving_fee);
                        bundle.putString("memo", memo);
                        bundle.putString("deposit", deposit);
                        //update_today_order();
                        checkTotalPrice();
                    })
                    .setNegativeButton("否", (dialog1, which1) -> {
                        int finalPrice = Integer.parseInt(fee);
                        String moving_fee = String.valueOf(finalPrice);
                        memo = memoEdit.getText().toString();
                        Log.d(TAG,"check_price_btn, fee: "+fee+", memo: "+memo);
                        bundle.putString("order_id", order_id);
                        bundle.putString("fee", moving_fee);
                        bundle.putString("memo", memo);
                        bundle.putString("deposit", deposit);
                        //update_today_order();
                        checkTotalPrice();
                    })
                    .create()
                    .show());
            builder.setNeutralButton("取消", null);

            builder.setNegativeButton("現金付款", (dialog, which) ->{
                Intent sign_intent = new Intent(context, Signature_Pad.class);
                new AlertDialog.Builder(context)
                        .setTitle("更新會員資料")
                        .setMessage("是否同意會員聯絡地址更新為搬入地址？")
                        .setPositiveButton("是", (dialog1, which1) -> {
                            updateAddress();
                            int finalPrice = Integer.parseInt(fee);
                            String moving_fee = String.valueOf(finalPrice);
                            memo = memoEdit.getText().toString();
                            Log.d(TAG,"check_price_btn, fee: "+fee+", memo: "+memo);
                            bundle.putString("order_id", order_id);
                            bundle.putString("fee", moving_fee);
                            bundle.putString("memo", memo);
                            bundle.putString("deposit", deposit);
                            sign_intent.putExtras(bundle);
                            startActivity(sign_intent);
                        })
                        .setNegativeButton("否", (dialog1, which1) -> {
                            int finalPrice = Integer.parseInt(fee);
                            String moving_fee = String.valueOf(finalPrice);
                            memo = memoEdit.getText().toString();
                            Log.d(TAG,"check_price_btn, fee: "+fee+", memo: "+memo);
                            bundle.putString("order_id", order_id);
                            bundle.putString("fee", moving_fee);
                            bundle.putString("memo", memo);
                            bundle.putString("deposit", deposit);
                            sign_intent.putExtras(bundle);
                            startActivity(sign_intent);
                        })
                        .create()
                        .show();
            });
            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        });
    }


    private void linking(){
        nameText = findViewById(R.id.name_OTD);
        nameTitleText = findViewById(R.id.nameTitle_OTD);
        phoneText = findViewById(R.id.phone_OTD);
        movingTimeText = findViewById(R.id.movingTime_OTD);
        fromAddressText = findViewById(R.id.FromAddress_OTD);
        toAddressText = findViewById(R.id.ToAddress_OTD);
        remainderText = findViewById(R.id.notice_OTD);
        feeText = findViewById(R.id.price_OTD);
        memoEdit = findViewById(R.id.PS_OTD);
        depositText = findViewById(R.id.extra_Price);
        back_btn = findViewById(R.id.confirm_back_btn);
        furniture_list = findViewById(R.id.confirm_location);
        pay = findViewById(R.id.pay_btn);
        totalAmount = findViewById(R.id.needPay_text);
    }
    private void setText(){
        nameText.setText(name);
        if(gender.equals("男"))nameTitleText.setText("先生");
        else if(gender.equals("女"))nameTitleText.setText("小姐");
        else nameTitleText.setText("");
        phoneText.setText(phone);
        movingTimeText.setText(movingTime);
        fromAddressText.setText(fromAddress);
        toAddressText.setText(toAddress);
        remainderText.setText(additional);
        feeText.setText(fee);
        memoEdit.setText(memo);
        depositText.setText(deposit);
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
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //顯示錯誤訊息
                //在app畫面上呈現錯誤訊息
                runOnUiThread(() -> Toast.makeText(context, "連線失敗", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG, "responseData of update_address: " + responseData); //顯示資料
                runOnUiThread(() -> {
                    if(responseData.contains("success"))
                        Toast.makeText(context, "更新地址成功", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(context, "更新地址失敗", Toast.LENGTH_LONG).show();
                });

            }
        });
    }

    private void getFurniture(){
        String function_name = "furniture_web_room_detail";
        String company_id = getCompany_id(this);
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
                runOnUiThread(() -> Toast.makeText(Confirm_Detail.this, "連線失敗", Toast.LENGTH_LONG).show());
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
                            furniture_list.setAdapter(noData);
                        } );
                    }
                    //else Toast.makeText(context, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show();
                }
                //顯示資訊
                for(int i=0; i < data.size(); i++)
                    Log.i(TAG, "data: "+ Arrays.toString(data.get(i)));
                final LocationAdapter LocationAdapter = new LocationAdapter(data);
                runOnUiThread(() -> furniture_list.setAdapter(LocationAdapter));

            }
        });
    }
    private void checkTotalPrice(){
        Request request = new Request.Builder()
                .url("https://598new.ddns.net/598_new_20211026/appecpay.php?order_id="+order_id+"&company_id="+getCompany_id(context)+"&plan="+plan)
                .build();
        Log.d(TAG, "order_id: " + order_id+
                        " company_id: "+getCompany_id(context)+
                        " plan: "+plan);
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //顯示錯誤訊息
                //在app畫面上呈現錯誤訊息
                runOnUiThread(() -> Toast.makeText(context, "取得失敗", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                runOnUiThread(() ->{
                    LayoutInflater inflater = getLayoutInflater();
                    View view = inflater.inflate(R.layout.qrcode_image, null);
                    ImageView qrcodeView = view.findViewById(R.id.qrcode_img_QI);

                    String url = "https://598new.ddns.net/598_new_20211026/appecpay.php?order_id="+order_id+"&company_id="+getCompany_id(context)+"&plan="+plan;
                    Log.d(TAG, "website: "+ url);
                    try {
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.encodeBitmap(url, BarcodeFormat.QR_CODE, 600, 600);
                        qrcodeView.setImageBitmap(bitmap);

                    } catch (WriterException e){
                        e.printStackTrace();
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("匯款QR CODE");
//               builder.setMessage("請掃描QR CODE");
                    builder.setView(view);
                    builder.setPositiveButton("確定", (dialog, which) -> {
                        getPaidStatus();
                        /*if(check){
                            //change_order_status();
                            //receiveResult();
                            /*Intent sign_intent = new Intent(context, Signature_Pad.class);
                            int finalPrice = Integer.parseInt(fee);
                            String moving_fee = String.valueOf(finalPrice);
                            memo = memoEdit.getText().toString();
                            Log.d(TAG,"check_price_btn, fee: "+fee+", memo: "+memo);
                            bundle.putString("order_id", order_id);
                            bundle.putString("fee", moving_fee);
                            bundle.putString("memo", memo);
                            bundle.putString("deposit", deposit);
                            sign_intent.putExtras(bundle);
                            startActivity(sign_intent);
                            Toast.makeText(context, "完成訂單 - 已收款", Toast.LENGTH_LONG).show();
                        }*/
                        /*else {
                            Toast.makeText(context, "資料上傳失敗", Toast.LENGTH_LONG).show();
                        }*/
                    });
                    builder.setNegativeButton("取消", (dialog, which) -> { });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                });
            }
        });
    }
    private void getPaidStatus(){
        String function_name = "get_payment_result";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .build();
        //Log.d(TAG, order_id);
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
                runOnUiThread(() -> Toast.makeText(context, "連線失敗", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG, responseData);
                try {
                    JSONObject payment = new JSONObject(responseData);
                    if(payment.getString("status").equals("order_paid")){
                        Intent sign_intent = new Intent(context, Signature_Pad.class);
                        int finalPrice = Integer.parseInt(fee);
                        String moving_fee = String.valueOf(finalPrice);
                        memo = memoEdit.getText().toString();
                        Log.d(TAG,"check_price_btn, fee: "+fee+", memo: "+memo+", plan: "+plan);
                        bundle.putString("order_id", order_id);
                        bundle.putString("fee", moving_fee);
                        bundle.putString("memo", memo);
                        bundle.putString("deposit", deposit);
                        bundle.putString("plan", plan);
                        check = true;
                        sign_intent.putExtras(bundle);
                        startActivity(sign_intent);
                        runOnUiThread(() -> Toast.makeText(context, "已確認收款", Toast.LENGTH_LONG).show());
                    }else{
                        runOnUiThread(() ->{
                            LayoutInflater inflater = getLayoutInflater();
                            View view = inflater.inflate(R.layout.qrcode_image, null);
                            ImageView qrcodeView = view.findViewById(R.id.qrcode_img_QI);
                            String url = "https://140.117.68.146/598_new_20211026/appecpay.php?order_id="+order_id+"&company_id="+getCompany_id(context)+"&plan="+plan;
                            try {
                                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                                Bitmap bitmap = barcodeEncoder.encodeBitmap(url, BarcodeFormat.QR_CODE, 600, 600);
                                qrcodeView.setImageBitmap(bitmap);

                            } catch (WriterException e){
                                e.printStackTrace();
                            }
                            Toast.makeText(context, "尚未付款或付款失敗", Toast.LENGTH_LONG).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setView(view);
                            builder.setTitle("尚未收到款項，請再次檢查");
                            builder.setPositiveButton("是，重新檢查", (dialog, which) -> getPaidStatus());
                            builder.setNegativeButton("否，略過",(dialog, which)->{});
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        });
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }

            }
        });
    }
    private void update_today_order(){
        String function_name = "update_todayOrder";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .add("company_id", getCompany_id(context))
                .add("accurate_fee", fee) /*只有搬家費用*/
                .add("memo", memo)
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
                runOnUiThread(() -> Toast.makeText(context, "連線失敗", Toast.LENGTH_LONG).show());
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
}