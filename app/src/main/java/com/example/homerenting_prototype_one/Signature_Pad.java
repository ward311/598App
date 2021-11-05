package com.example.homerenting_prototype_one;

import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

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

public class Signature_Pad extends AppCompatActivity {
    SignaturePad signPad ;
    Button clearBtn, checkBtn;
    ImageView backBtn;
    Context context = this;
    String TAG = "Signature_Pad";
    String base64Image;
    Bundle bundle;
    String order_id, fee, memo;
    Boolean check = false;
    TextView resultView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_pad);
        signPad = findViewById(R.id.signaturePad);
        checkBtn = findViewById(R.id.confirm_sign_btn);
        clearBtn = findViewById(R.id.clear_btn);
        backBtn = findViewById(R.id.back_ImgBtn);
        resultView = findViewById(R.id.finalView);
        checkBtn.setEnabled(false);
        checkBtn.setAlpha(.5f);
        clearBtn.setEnabled(false);
        clearBtn.setAlpha(.5f);

        bundle = getIntent().getExtras();
        order_id = bundle.getString("order_id");
        fee = bundle.getString("fee");
        memo = bundle.getString("memo");
        Log.d(TAG, "order_id : "+order_id);
        Log.d(TAG,"check_price_btn, fee: "+fee+", memo: "+memo);
        resultView.setText("今日訂單總計費用 : "+fee+" \n"+"備註事項 : "+memo+"\n\n顧客簽名後視同確認工單無誤");
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
                .url(BuildConfig.SERVER_URL+"/signature_image.php")
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
                runOnUiThread(() -> Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG,"responseData of signature_update: "+responseData); //顯示資料

                try {
                    JSONObject result = new JSONObject(responseData);
                    //取得資料
                    if(result.getString("status").equals("failed")){
                        runOnUiThread(() -> Toast.makeText(context, "資料上傳失敗", Toast.LENGTH_LONG).show());
                    }else{
                        Log.d(TAG, ""+result.getString("status")+" "+result.getString("message"));
                        update_today_order(); /*金額費用上傳*/
                        runOnUiThread(() -> {
                            Handler handler = new Handler();
                            handler.postDelayed(() -> {
                                Intent intent = new Intent(context, Calendar.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                Log.d(TAG, "responseData of change_status: " + responseData);
                                Toast.makeText(context, "訂單完成", Toast.LENGTH_LONG).show();
                                startActivity(intent);
                            }, 500);
                        });
                        change_order_status(); /*更改訂單狀態 -> done */
                    }
                    //顯示資料
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
                            Intent intent = new Intent(context, Order_Today.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            Log.d(TAG, "responseData of change_status: " + responseData);
                            Toast.makeText(context, "完成訂單", Toast.LENGTH_LONG).show();
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