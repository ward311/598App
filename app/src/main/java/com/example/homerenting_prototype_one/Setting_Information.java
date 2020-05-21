package com.example.homerenting_prototype_one;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.valuation.Valuation;

public class Setting_Information extends AppCompatActivity {
    public TextView address_txt, phone_txt, number_txt, url_txt, email_text,line_text,idea_txt;
    public EditText address_edit, phone_edit, number_edit, url_edit, email_edit, line_edit,idea_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting__information);
        address_txt = findViewById(R.id.company_address_text);
        phone_txt = findViewById(R.id.company_phone_text);
        number_txt = findViewById(R.id.employee_number_text);
        url_txt = findViewById(R.id.company_url_text);
        email_text = findViewById(R.id.company_email_text);
        line_text = findViewById(R.id.company_line_text);
        idea_txt = findViewById(R.id.company_idea_text);
        address_edit = findViewById(R.id.company_address_editText);
        phone_edit = findViewById(R.id.company_phone_editText);
        number_edit = findViewById(R.id.employee_number_editText);
        url_edit = findViewById(R.id.company_url_editText);
        email_edit = findViewById(R.id.company_email_editText);
        line_edit = findViewById(R.id.company_line_editText);
        idea_edit = findViewById(R.id.company_idea_editText);
        ImageButton back_btn = findViewById(R.id.back_imgBtn);
        ImageButton valuation_btn = findViewById(R.id.valuation_imgBtn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);
        final Button edit_btn = findViewById(R.id.informationEdit_btn);
        final Button finish_btn = findViewById(R.id.companyInformation_finished_btn);
        final LinearLayout finish_lsyout = findViewById(R.id.finished_linearLayout);

//        Bundle bundle = getIntent().getExtras();
//        String address = bundle.getString("address");
//        String phone = bundle.getString("phone");
//        String number = bundle.getString("number");
//        String url = bundle.getString("url");
//        String idea = bundle.getString("idea");
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back_setting_intent = new Intent(Setting_Information.this, Setting.class);
                startActivity(back_setting_intent);
            }
        });
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Setting_Information.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Setting_Information.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Setting_Information.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Setting_Information.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Setting_Information.this, Setting.class);
                startActivity(setting_intent);
            }
        });
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent edit_intent = new Intent(Setting_Information.this, Information_Edit.class);
//                startActivity(edit_intent);
                edit_btn.setVisibility(View.GONE);
                finish_lsyout.setVisibility(View.VISIBLE);
                address_txt.setVisibility(View.GONE);
                phone_txt.setVisibility(View.GONE);
                number_txt.setVisibility(View.GONE);
                url_txt.setVisibility(View.GONE);
                email_text.setVisibility(View.GONE);
                line_text.setVisibility(View.GONE);
                idea_txt.setVisibility(View.GONE);
                address_edit.setVisibility(View.VISIBLE);
                phone_edit.setVisibility(View.VISIBLE);
                number_edit.setVisibility(View.VISIBLE);
                url_edit.setVisibility(View.VISIBLE);
                email_edit.setVisibility(View.VISIBLE);
                line_edit.setVisibility(View.VISIBLE);
                idea_edit.setVisibility(View.VISIBLE);
            }
        });
        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address_txt.setText(address_edit.getText().toString());
                phone_txt.setText(phone_edit.getText().toString());
                number_txt.setText(number_edit.getText().toString());
                url_txt.setText(url_edit.getText().toString());
                email_text.setText(email_edit.getText().toString());
                line_text.setText(line_edit.getText().toString());
                idea_txt.setText(idea_edit.getText().toString());

                finish_lsyout.setVisibility(View.GONE);
                edit_btn.setVisibility(View.VISIBLE);
                address_txt.setVisibility(View.VISIBLE);
                phone_txt.setVisibility(View.VISIBLE);
                number_txt.setVisibility(View.VISIBLE);
                url_txt.setVisibility(View.VISIBLE);
                email_text.setVisibility(View.VISIBLE);
                line_text.setVisibility(View.VISIBLE);
                idea_txt.setVisibility(View.VISIBLE);
                address_edit.setVisibility(View.GONE);
                phone_edit.setVisibility(View.GONE);
                number_edit.setVisibility(View.GONE);
                url_edit.setVisibility(View.GONE);
                email_edit.setVisibility(View.GONE);
                line_edit.setVisibility(View.GONE);
                idea_edit.setVisibility(View.GONE);
            }
        });
    }
}
