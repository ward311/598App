package com.example.homerenting_prototype_one;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    public ImageView btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout hone_page = findViewById(R.id.homePage_linearLayout);
        hone_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login_intent = new Intent(MainActivity.this,Login.class);
                startActivity(login_intent);
            }
        });
//        btn = findViewById(R.id.logo_btn);
//        //btn.setImageResource(R.drawable.master_logo);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }
//    public void enter(View view){
////        Intent intent = new Intent(this,Activity_login.class);
////        startActivity(intent);
//    }
}
