package com.example.homerenting_prototype_one;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText account_edit = findViewById(R.id.actEdit);
        final EditText password_edit = findViewById(R.id.pwdEdit);
        final EditText check_Edit = findViewById(R.id.checkEdit);
        //TextView forget_password = findViewById(R.id.forgetText);
        Button login_btn = findViewById(R.id.login_btn);
        Button edit_btn = findViewById(R.id.edit_pwd_btn);
        if (TextUtils.isEmpty( account_edit.getText().toString() )){
            account_edit.setError( "請輸入帳號" );
        }
        if (TextUtils.isEmpty( password_edit.getText().toString() )){
            password_edit.setError( "請輸入密碼" );
        }
        if (TextUtils.isEmpty( check_Edit.getText().toString() )){
            check_Edit.setError( "請確認密碼" );
        }

        login_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String passowrd_text = password_edit.getText().toString().trim();
                final String check_text = check_Edit.getText().toString().trim();
                if (check_text.equals( passowrd_text )&&!(TextUtils.isEmpty( passowrd_text ))&&!(TextUtils.isEmpty( check_text ))){
                    startActivity(new Intent(Login.this, Calendar.class));
                }
                else if (!(check_text.equals( passowrd_text ))&&!(TextUtils.isEmpty( passowrd_text ))&&TextUtils.isEmpty( check_text )){
                    startActivity(new Intent(Login.this, Calendar.class));
                }
                else {
                    new AlertDialog.Builder( Login.this )
                            .setTitle( "密碼不一致" )
                            .setMessage( "請輸入一致的密碼" )
                            .setPositiveButton( "確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            } )
                            .setNegativeButton( "取消",null ).create()
                            .show();
                }
            }
        } );
        edit_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_Edit.setVisibility( View.VISIBLE );
            }
        } );
    }
    public void login(View view){
        startActivity(new Intent(Login.this, Calendar.class));
    }
//    public void register(View view){
//        startActivity(new Intent(Login.this, Register.class));
//    }
}
