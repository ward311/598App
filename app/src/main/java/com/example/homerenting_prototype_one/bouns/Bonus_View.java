package com.example.homerenting_prototype_one.bouns;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.adapter.re_adpater.SalaryAdapter;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.setting.Setting;
import com.example.homerenting_prototype_one.system.System;
import com.example.homerenting_prototype_one.valuation.Valuation;

import java.util.ArrayList;

import static com.example.homerenting_prototype_one.show.global_function.dip2px;

public class Bonus_View extends AppCompatActivity {

    private String[] employees = {"王小明","陳聰明","劉光明","吳輝明","邱朝明","葉大明","劉案全",
            "王守興","彭玉唱","徐將義","劉曹可","秦因文","方子優","古蘭花","朱柯基"};
    private String[] total = {"30000","40000","25000","55000","35000","30000","30000",
            "25000","35000","20000","25000","30000","30000","25000","25000"};

    RecyclerView salaryView;
    ImageButton backBtn;
    Button chartBtn;

    ArrayList<String[]> data;
    ArrayList<String[]> emp = new ArrayList<>();

    private Context context = this;
    String TAG = "Bonus_View";
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
        String[] row_data = {"一號", "55000"}; emp.add(row_data);
        String[] row_data2 = {"二號", "25000"}; emp.add(row_data2);

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.bonus_dialog);

        setList(dialog);

        Button dialog_btn = dialog.findViewById(R.id.check_dialog_btn);
        dialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setLayout( 1400,2000 );
        dialog.setCanceledOnTouchOutside(true); //點其他地方也能取消
        dialog.show();


        chartBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        } );
    }

    private void setList(Dialog dialog){
        double m = getMultiplier();

        LinearLayout salaryChart = dialog.findViewById(R.id.salaryChart_LL_BD);
        for(int i = 0; i < emp.size(); i++) {
            LinearLayout rowSalaryChart = new LinearLayout(context); //新的員工薪資條
            rowSalaryChart.setPadding(dip2px(context, 20), 0, 0, dip2px(context, 10));
            {
                TextView name = new TextView(context); //員工姓名
                {
                    name.setText(emp.get(i)[0]);
                    name.setTextSize(20);
                    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 3);
                    name.setLayoutParams(p);
                }
                rowSalaryChart.addView(name); //員工姓名加入薪資條

                LinearLayout chart = new LinearLayout(context); //薪資區塊
                {
                    LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                    chart.setLayoutParams(p2);
                    TextView salary = new TextView(context); //薪資文字
                    {
                        salary.setText(emp.get(i)[1]);
                        int sa = Integer.parseInt(emp.get(i)[1]);
                        int width = (int) Math.round(sa * m);
                        Log.d(TAG, emp.get(i)[0] + "'s width: " + width + "(salary:" + sa + "/m:" + m + ")");
                        salary.setWidth(dip2px(context, width));
                        salary.setBackgroundColor(Color.parseColor("#FB8527"));
                        salary.setTextColor(Color.parseColor("#FFFFFF"));
                        salary.setTextSize(20);
                    }
                    chart.addView(salary); //薪資文字加入薪資區塊
                }
                rowSalaryChart.addView(chart); //薪資區塊加入薪資條
            }
            salaryChart.addView(rowSalaryChart); //新的薪資條加入圖表
        }
    }

    private double getMultiplier(){
        int maxSalary = 0;
        for(int i = 0; i < emp.size(); i++){
            if(Integer.parseInt(emp.get(i)[1]) > maxSalary){
                maxSalary = Integer.parseInt(emp.get(i)[1]);
            }
        }

        double multiplier = (double) 275/maxSalary;
        Log.d(TAG, "muliplier: "+multiplier);
        return multiplier;
    }
}
