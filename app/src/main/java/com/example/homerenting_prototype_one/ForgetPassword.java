package com.example.homerenting_prototype_one;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.homerenting_prototype_one.helper.SessionManager;
import com.example.homerenting_prototype_one.main.Login;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ForgetPassword extends AppCompatActivity {

    Context context = this;
    EditText emailText, phoneEdit,veriCode, pwd, conPwd;
    TextView verificationTime, verifyStatus;
    String account, password, confirmPassword;
    Button setPwd, back_btn, verifySMS, verifyGet;
    String TAG = "ForgetPassword";
    String email, phone;
    String return_email, return_phoneNum, return_vCode;
    OkHttpClient okHttpClient = new OkHttpClient();
    SessionManager session;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        emailText = findViewById(R.id.email_editext);
        //phoneEdit = findViewById(R.id.phoneInput);
        veriCode = findViewById(R.id.verifycode_editext);
        verifyStatus = findViewById(R.id.verifyStat);
        pwd = findViewById(R.id.newPwd);
        conPwd = findViewById(R.id.confirmPwd);
        setPwd = findViewById(R.id.setPwd_btn);
        back_btn = findViewById(R.id.goback_btn);
        verifyGet = findViewById(R.id.getverify_btn);
        Intent LoginIntent = new Intent(context, Login.class);
        sp = getSharedPreferences("login", Context.MODE_PRIVATE);


        setPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailText.getText().length()!=0&&veriCode.getText().length()!=0&&pwd.getText().length()!=0
                    &&conPwd.getText().length()!=0){
                    if(pwd.getText().toString().equals(conPwd.getText().toString())){
                        if(!veriCode.getText().toString().equals("1234")){
                            verifyStatus.setTextColor(Color.RED);
                            verifyStatus.setText("驗證碼錯誤");
                        }
                        else{
                            email = emailText.getText().toString();
                            password = pwd.getText().toString();
                            resetPassword();

                        }

                    }
                    else{
                        pwd.setError("新密碼與確認設置新密碼不符");
                        conPwd.setError("新密碼與確認設置新密碼不符");
                    }
                }
                else{
                 Toast.makeText(context,"輸入資料不得有空，請再檢查一次", Toast.LENGTH_LONG).show();
                }
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(LoginIntent);
            }
        });
        veriCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(veriCode.getText().length()==0){
                    verifyStatus.setText("請輸入驗證碼");
                    verifyStatus.setTextColor(Color.rgb(255,0,0));
                }else veriCode.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        emailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(emailText.getText().length()==0){
                    emailText.setError("請輸入電子郵件");
                }else emailText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(pwd.getText().length()==0){
                    pwd.setError("請輸入新密碼");
                }else pwd.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        conPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(conPwd.getText().length()==0){
                    conPwd.setError("請輸入確認新密碼");
                }else conPwd.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        verifyGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    email = emailText.getText().toString();
                    verifyFunction();
                    verifyGet.setEnabled(false);
                    new CountDownTimer(5000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            verifyStatus.setText(millisUntilFinished / 1000+" 秒後可再次發送認證簡訊");
                            verifyStatus.setTextColor(Color.rgb(0,0,255));
                        }

                        public void onFinish() {
                            verifyStatus.setText("");
                            verifyGet.setEnabled(true);
                        }
                    }.start();


            }
        });

    }


    public void verifyFunction(){
        RequestBody body = new FormBody.Builder()
                .add("email",email)
                .build();

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/verify.php")
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(context, "驗證產生失敗", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
                    Log.d(TAG, ""+responseData);
                    try{
                        JSONObject result = new JSONObject(responseData);
                        if(result.getString("status").equals("failed")){
                            runOnUiThread(() -> Toast.makeText(context,"此帳號不存在", Toast.LENGTH_LONG).show());
                            Log.d(TAG, ""+result.getString("message"));
                        }
                        else{
                            if(result.get("status").equals("success")){
                                return_email = result.getString("email");
                                return_phoneNum = result.getString("phone");
                                return_vCode = result.getString("verifyCode");
                                runOnUiThread(() -> Toast.makeText(context, "簡訊驗證碼已發送至帳號綁定之手機 ", Toast.LENGTH_LONG).show());

                            }else{
                                runOnUiThread(() -> Toast.makeText(context, "驗證發送失敗 ", Toast.LENGTH_LONG).show());
                            }

                        }


                    }catch (JSONException e){
                        e.printStackTrace();
                    }

            }

        });
    }

    public void resetPassword(){
        RequestBody body = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .build();
        Log.d(TAG, "email: "+email);
        Log.d(TAG,"password change to: "+password);

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/reset.php")
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(context, "密碼更新失敗", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG, "reset responseData: " + responseData);
                try{
                    JSONObject resetData = new JSONObject(responseData);
                    if(resetData.getString("status").equals("failed")){
                        runOnUiThread(() -> {
                            Toast.makeText(context, "此帳號尚未註冊", Toast.LENGTH_LONG).show();
                        });
                    }else{
                        Log.d(TAG, ""+resetData.getString("status")+""+resetData.getString("message"));
                        runOnUiThread(() -> {
                            Intent goLogin = new Intent(context, Login.class);
                            goLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(goLogin);
                            finish();
                            Toast.makeText(context, "新密碼設定成功，請重新登入 ", Toast.LENGTH_LONG).show();

                        });
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }


            }
        });
    }
}
