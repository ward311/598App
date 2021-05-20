package com.example.homerenting_prototype_one;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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

import com.example.homerenting_prototype_one.main.Login;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForgetPassword extends AppCompatActivity {

    Context context = this;
    EditText phoneEdit,veriCode, pwd, conPwd;
    TextView verificationTime, verifyStatus;
    String password, confirmPassword;
    Button setPwd, back_btn, verifySMS, verifyCheck;
    String TAG = "ForgetPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        phoneEdit = findViewById(R.id.phoneInput);
        veriCode = findViewById(R.id.verifyCode);
        verifyStatus = findViewById(R.id.verifyStat);
        pwd = findViewById(R.id.newPwd);
        conPwd = findViewById(R.id.confirmPwd);
        setPwd = findViewById(R.id.setPwd_btn);
        back_btn = findViewById(R.id.goback_btn);
        verifySMS = findViewById(R.id.verify_btn);
        verifyCheck = findViewById(R.id.verifying_btn);
        verificationTime = findViewById(R.id.verifyTimer);
        Intent LoginIntent = new Intent(context, Login.class);
        setPwd.setEnabled(false);
        String random = Integer.toString((int)(Math.random()*8998)+1000+1);


        setPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phoneEdit.getText().length()!=0&&veriCode.getText().length()!=0&&pwd.getText().length()!=0
                    &&conPwd.getText().length()!=0){
                    if(pwd.getText().toString().equals(conPwd.getText().toString())){
                        startActivity(LoginIntent);
                        Toast.makeText(context,"新密碼已設定完成", Toast.LENGTH_LONG).show();
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
        verifySMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phoneEdit.getText().length()==0){
                    Toast.makeText(context, "請輸入手機號碼", Toast.LENGTH_LONG).show();
                }else{
                    String phone = phoneEdit.getText().toString();
                    Log.d(TAG,"phone : "+phone+" random verify : "+random);
                    Toast.makeText(context, "簡訊驗證碼已發送至 : "+phone, Toast.LENGTH_LONG).show();
                    verifySMS.setEnabled(false);
                    new CountDownTimer(5000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            verificationTime.setText(millisUntilFinished / 1000+" 秒後可再次發送認證簡訊");
                            verificationTime.setTextColor(Color.rgb(0,0,255));
                        }

                        public void onFinish() {
                            verifySMS.setEnabled(true);
                            verificationTime.setText("");
                            veriCode.setEnabled(true);
                        }
                    }.start();

                }
            }
        });
        verifyCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(veriCode.getText().toString().equals(random)){
                    verifyStatus.setText("驗證成功，可設置新密碼");
                    verifyStatus.setTextColor(Color.parseColor("#0f422f"));
                    setPwd.setEnabled(true);
                    veriCode.setEnabled(false);
                }else{
                    verifyStatus.setText("驗證失敗，請重新驗證");
                    verifyStatus.setTextColor(Color.rgb(255,0,0));
                    veriCode.setEnabled(true);
                    setPwd.setEnabled(false);
                }
            }
        });

    }

    /*public void changePassword(){
        String company_id = getCompany_id(context);
        RequestBody body = new FormBody.Builder()
                .add("function_name", "check_companyID")
                .add("company_id", company_id)
                .add("password", password)
                .build();
        Log.d(TAG, "company_id: "+company_id);
        Log.d(TAG,"password change to: "+password);

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/update_data/login.php")
                .post(body)
                .build();

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
                        if(responseData.equals("success")){
                            Toast.makeText(context, "修改成功，請使用新密碼", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(context, Login.class));
                        }
                        else Toast.makeText(context, "修改失敗", Toast.LENGTH_LONG).show();
                    }
                });
                Log.d(TAG, "submit responseData: " + responseData);
            }
        });
    }*/
}
