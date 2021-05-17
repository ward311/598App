package com.example.homerenting_prototype_one;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.homerenting_prototype_one.main.Login;

public class ChangePassword extends AppCompatActivity {

    EditText old_pwd, new_pwd, con_pwd;
    Button cancel, change_pwd;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        old_pwd = findViewById(R.id.old_pwd_edit);
        new_pwd = findViewById(R.id.new_pwd_edit);
        con_pwd = findViewById(R.id.con_pwd_edit);
        cancel = findViewById(R.id.cancel_change_btn);
        change_pwd = findViewById(R.id.change_password_btn);
        Intent Login = new Intent(context, Login.class);

        old_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(old_pwd.getText().length()==0){
                    old_pwd.setError("請輸入舊密碼");
                }else old_pwd.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        new_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(new_pwd.getText().length()==0){
                    new_pwd.setError("請輸入新密碼");
                }else new_pwd.setError(null);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        con_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(con_pwd.getText().length()==0){
                    con_pwd.setError("請輸入確認新密碼");
                } else con_pwd.setError(null);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Login);
            }
        });

        change_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(old_pwd.getText().length()!=0 && new_pwd.getText().length()!=0 && con_pwd.getText().length()!=0){
                    if(!new_pwd.getText().toString().equals(old_pwd.getText().toString())){
                        if(new_pwd.getText().toString().equals(con_pwd.getText().toString())){
                            startActivity(Login);
                            Toast.makeText(context,"密碼修改成功", Toast.LENGTH_LONG).show();
                        }else{
                            new_pwd.setError("新密碼與確認新密碼不符");
                            con_pwd.setError("新密碼與確認新密碼不符");
                        }
                    }else{
                            new_pwd.setError("新密碼不可與舊密碼相同");
                    }
                }
                else{
                    Toast.makeText(context,"輸入資料不得有空，請再檢查一次", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}