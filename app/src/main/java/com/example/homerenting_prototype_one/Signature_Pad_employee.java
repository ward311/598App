package com.example.homerenting_prototype_one;

import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.order.Order_Today;
import com.example.homerenting_prototype_one.order.Today_Detail;
import com.github.gcacace.signaturepad.views.SignaturePad;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Signature_Pad_employee extends AppCompatActivity {
    SignaturePad signPad ;
    Button clearBtn, checkBtn;
    TextView detailBtn;
    ImageView backBtn;
    Context context = this;
    String TAG = "Signature_Pad_Employee";
    String base64Image;
    Bundle bundle_employee;
    String order_id, fee, memo, deposit, plan;
    Boolean check = false;
    TextView resultView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_pad_employee);
        bundle_employee = new Bundle();
        signPad = findViewById(R.id.signaturePad_employee);
        checkBtn = findViewById(R.id.confirm_sign_btn_employee);
        clearBtn = findViewById(R.id.clear_btn_employee);
        backBtn = findViewById(R.id.back_ImgBtn_employee);
        resultView = findViewById(R.id.finalView_employee);
        detailBtn = findViewById(R.id.detailBtn_employee);
        checkBtn.setEnabled(false);
        checkBtn.setAlpha(.5f);
        clearBtn.setEnabled(false);
        clearBtn.setAlpha(.5f);

        bundle_employee = getIntent().getExtras();
        order_id = bundle_employee.getString("order_id");
        fee = bundle_employee.getString("fee");
        memo = bundle_employee.getString("memo");
        deposit = bundle_employee.getString("deposit");
        plan = bundle_employee.getString("plan");
        Log.d(TAG, "order_id : "+order_id);
        Log.d(TAG,"check_price_btn, fee: "+fee+", memo: "+memo+", deposit: "+deposit+", plan: "+plan);
        resultView.setText("??????????????????????????????????????????");
        signPad.setPenColor(Color.BLACK);

        signPad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                checkBtn.setEnabled(true);
                checkBtn.setAlpha(1f);
                clearBtn.setEnabled(true);
                clearBtn.setAlpha(1f);
            }

            @Override
            public void onClear() {
                checkBtn.setEnabled(false);
                checkBtn.setAlpha(.5f);
                clearBtn.setEnabled(false);
                clearBtn.setAlpha(.5f);
            }
        });

        checkBtn.setOnClickListener(v -> {
            Bitmap signPic = signPad.getSignatureBitmap();
            String result_encode = convertImage(signPic);
            Log.d(TAG, "Base64 : "+ result_encode);

            saveImage();
        });

        clearBtn.setOnClickListener(v -> signPad.clear());

        backBtn.setOnClickListener(v -> {
            Intent back_intent = new Intent(this, Today_Detail.class);
            back_intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(back_intent);
            this.finish();
        });
        detailBtn.setOnClickListener(v -> {
            int toPay = Integer.parseInt(fee) - Integer.parseInt(deposit);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            TextView mTitle = new TextView(this);
            mTitle.setText("????????????");
            mTitle.setTextColor(Color.BLACK);
            mTitle.setPadding(0,10,0,0);
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            mTitle.setGravity(Gravity.CENTER);
            builder.setCustomTitle(mTitle);
            builder.setMessage("????????????: "+fee+"\n????????????:"+deposit+
                    "\n????????????:"+toPay);
            builder.setPositiveButton("??????", null);
            AlertDialog dialog = builder.create();
            dialog.show();

        });

    }

    private String convertImage(Bitmap pic){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        pic.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return base64Image;
    }
    public void  saveImage(){
        RequestBody body = new FormBody.Builder()
                .add("order_id", order_id)
                .add("encode", base64Image)
                .build();
        Log.d(TAG, "order_id : "+order_id);
        Log.d(TAG, "company_id : "+getCompany_id(context));
        Log.d(TAG, "encode : "+base64Image);

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/signature_image_employee.php")
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
                Log.d(TAG,"responseData of signature_update: "+responseData); //????????????

                try {
                    JSONObject result = new JSONObject(responseData);
                    //????????????
                    if(result.getString("status").equals("failed")){
                        runOnUiThread(() -> Toast.makeText(context, "??????????????????", Toast.LENGTH_LONG).show());
                    }else{
                        Log.d(TAG, ""+result.getString("status")+" "+result.getString("message"));
                        //update_today_order(); /*??????????????????*/
                        runOnUiThread(() -> {
                            Handler handler = new Handler();
                            handler.postDelayed(() -> {
                                Intent intent = new Intent(context, Calendar.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                Log.d(TAG, "responseData of change_status: " + responseData);
                                Toast.makeText(context, "????????????", Toast.LENGTH_LONG).show();
                                startActivity(intent);
                            }, 500);
                            change_order_status(); /*?????????????????? -> done */
                        });

                    }
                    //????????????
                } catch (JSONException e) {
                    e.printStackTrace();
//
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
                .add("accurate_fee", fee)
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
        String function_name = "change_stat";
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
                            Intent intent = new Intent(context, Order_Today.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            Log.d(TAG, "responseData of change_status: " + responseData);
                            Toast.makeText(context, "????????????", Toast.LENGTH_LONG).show();
                            startActivity(intent);
                        }, 500);

                    });
                }
            }
        });
    }

    public void onBackPressed(){
        super.onBackPressed();
        this.finish();
    }

}