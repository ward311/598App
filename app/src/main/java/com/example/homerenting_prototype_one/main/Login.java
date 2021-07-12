package com.example.homerenting_prototype_one.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.ForgetPassword;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.helper.SessionManager;
import com.example.homerenting_prototype_one.model.User;

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

public class Login extends AppCompatActivity {
    String account, password;
    SharedPreferences sp;
    //public String rAccount = "admin";
    //public String rPassword = "123";
    EditText account_edit, password_edit;
    TextView forget_pwd;
    Button admin_login_btn, edit_btn, staff_login_btn;
    OkHttpClient okHttpClient = new OkHttpClient();
    String TAG = "Login";
    Context context = Login.this;;

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            account_edit = findViewById(R.id.actEdit_L);
            password_edit = findViewById(R.id.pwdEdit_L);
            admin_login_btn = findViewById(R.id.admin_login_btn);

            //edit_btn = findViewById(R.id.edit_pwd_btn);
            forget_pwd = findViewById(R.id.forgetText);
            sp = getSharedPreferences("login", Context.MODE_PRIVATE);
            session = SessionManager.getInstance(this);

            if(sp.getBoolean("logged", false)){
                loginTo();
            }
            if (session.isLogin()) { //關掉app後不會保存登入狀態的樣子
                User user = session.getUserDetail();
                Log.d(TAG, "user: " + user.getId() + ", " + user.getCompany_id() + ", " + user.getName() + ", " + user.getEmail() + ", " + user.getToken());
            } else Log.d(TAG, "no login");


            password_edit.setTransformationMethod(PasswordTransformationMethod.getInstance());

            account_edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (account_edit.getText().length() == 0)
                        account_edit.setError("請輸入帳號");
                    else
                        account_edit.setError(null);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            password_edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (password_edit.getText().length() == 0) {
                        password_edit.setError("請輸入密碼");
                    } else password_edit.setError(null);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });


            admin_login_btn.setOnClickListener(view -> {
                account = account_edit.getText().toString();
                password = password_edit.getText().toString();
                sp.edit().putBoolean("logged", true).apply();
                sp.edit().putString("account", account).apply();
                sp.edit().putString("password", password).apply();
                loginTo();

            });


            forget_pwd.setOnClickListener(view -> {
                Intent forgetPwd = new Intent(context, ForgetPassword.class);
                startActivity(forgetPwd);
            });


    }

    public void loginTo(){
        RequestBody body = new FormBody.Builder()
                .add("user_email",sp.getString("account", ""))
                .add("password", sp.getString("password", ""))
                .build();
        Log.d(TAG, "account: "+account);
        Log.d(TAG,"password: "+password);

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/update_data/login.php")
                .post(body)
                .build();

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
                Log.d(TAG, "login responseData: " + responseData);

                try {
                    JSONObject loginData = new JSONObject(responseData);
                    if(loginData.getString("status").equals("login success")){
                        JSONObject user = loginData.getJSONObject("user");
                        Log.d(TAG, "user: "+user.toString());
                        String company_id = user.getString("company_id");
                        String user_id = user.getString("user_id");
                        String user_email = user.getString("user_email");
                        String user_name = user.getString("user_name");
                        String token = user.getString("token");
                        String title = user.getString("title");
                        session.createLoginSession(user_id, String.valueOf(user));
                        runOnUiThread(() -> {

                                Toast.makeText(context, "登入成功", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(context, Calendar.class));
                                sp.edit().putString("title", title).apply();
                        });
                    }
                    else if(loginData.getString("status").equals("login failed")){
                        Log.d(TAG, "login failed: "+loginData.getString("message"));
                        runOnUiThread(() -> Toast.makeText(context, "登入失敗，請確認帳號密碼", Toast.LENGTH_LONG).show());
                    }
                    else Log.d(TAG, "create login session failed: "+loginData.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        if(responseData.equals("login success")){
                            Toast.makeText(context, "登入成功, 歡迎回來", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(context, Calendar.class));

                        }
                        else Toast.makeText(context, "登入失敗，請確認帳號密碼", Toast.LENGTH_LONG).show();
                    });
                }
            }
        });

    }

}
