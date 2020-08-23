package com.example.homerenting_prototype_one.system;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.adapter.SalaryAdapter;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.setting.Setting;
import com.example.homerenting_prototype_one.valuation.Valuation;

import java.util.ArrayList;

public class Bonus_View extends AppCompatActivity {

    private String[] employees = {"王小明","陳聰明","劉光明","吳輝明","邱朝明","葉大明","劉案全",
            "王守興","彭玉唱","徐將義","劉曹可","秦因文","方子優","古蘭花","朱柯基"};
    private String[] total = {"30000","40000","25000","55000","35000","30000","30000",
            "25000","35000","20000","25000","30000","30000","25000","25000"};

    RecyclerView salaryView;
    ImageButton backBtn;
    Button chartBtn;

    private ArrayList<String[]> data;

    private Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus__view);
        Button total_btn = findViewById(R.id.total_salary_btn);

        salaryView = findViewById(R.id.salaryList_BV);
        backBtn = findViewById(R.id.back_imgBtn);
        chartBtn = findViewById(R.id.chart_btn_BV);

        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);


        total_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                salary_list.setVisibility(View.GONE);
//                date_salary_list.setVisibility(View.VISIBLE);
            }
        });

        setChart(); //圖表



        data = new ArrayList<>();
        int i;
        for(i = 0; i < employees.length; i++){
            String[] row_data = {employees[i], total[i]};
            data.add(row_data);
        }
        SalaryAdapter salaryAdapter = new SalaryAdapter(data);
        salaryView.setLayoutManager(new LinearLayoutManager(context));
        salaryView.setAdapter(salaryAdapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        //底下nav
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Bonus_View.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Bonus_View.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Bonus_View.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Bonus_View.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Bonus_View.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }

    private void setChart(){ //圖表
        final Dialog dialog = new Dialog( context);
        dialog.setContentView(R.layout.bonus_dialog);
        //dialog.setTitle("圖表");
        Button dialog_btn = dialog.findViewById( R.id.check_dialog_btn );
        dialog_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        } );
        dialog.getWindow().setLayout( 1400,2000 );
        dialog.show();

        //點其他地方也能取消
        LinearLayout BV = findViewById(R.id.bonusView_BV);
        BV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        chartBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        } );
    }
}
