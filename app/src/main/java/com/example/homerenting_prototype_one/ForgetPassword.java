package com.example.homerenting_prototype_one;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.main.Login;
import com.example.homerenting_prototype_one.setting.Setting_Evaluation;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;

public class ForgetPassword extends AppCompatActivity {

    OkHttpClient okHttpClient = new OkHttpClient();
    Context context = this;
    EditText pwd, conPwd;
    String password, confirmPassword;
    Button changePwd, back_btn;
    String TAG = "ForgetPassword";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        pwd = findViewById(R.id.newPwd);
        conPwd = findViewById(R.id.confirmPwd);
        changePwd = findViewById(R.id.changePwd_btn);
        back_btn = findViewById(R.id.goback_btn);
        Intent LoginIntent = new Intent(context, Login.class);



        changePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = pwd.getText().toString();
                confirmPassword = conPwd.getText().toString();
                if(!password.equals(confirmPassword)){
                    conPwd.setError("確認新密碼與密碼不符");
                }else{
                    startActivity(LoginIntent);
                    //changePassword();
                }
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(LoginIntent);
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
