package com.example.homerenting_prototype_one.bouns;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homerenting_prototype_one.BonusAdapter;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.adapter.re_adpater.SalaryAdapter;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.setting.Setting;
import com.example.homerenting_prototype_one.system.System;
import com.example.homerenting_prototype_one.valuation.Valuation;

import java.util.ArrayList;

public class Bonus_List_Detail extends AppCompatActivity {
    private RecyclerView employeeDataView, test1, test2;

    private ArrayList<String> employees;
    private ArrayList<String[]> data;
    private Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_bonus__list__detail );

        employeeDataView = findViewById(R.id.employeeData_rv_BLD);
        test1 = findViewById(R.id.test1_rv_BLD);
        test2 = findViewById(R.id.test2_rv_BLD);

        ImageButton back_btn = findViewById( R.id.back_imgBtn);
        TextView title_text = findViewById( R.id.month_title_text );
        ListView bonus_list = findViewById( R.id.bonus_detail_list );
        Button export_btn = findViewById(R.id.export_bonus_btn);
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);
        TextView twenty_nine = findViewById( R.id.twenty_nine_text );
        TextView thirty = findViewById( R.id.thirty_text );
        TextView thirty_one = findViewById( R.id.thirty_one_text );

        Bundle bundle = getIntent().getExtras();
        String month = bundle.getString( "month" );

        title_text.setText( month+"獎金報表" );
        if (month.equals("2月")){
            thirty.setVisibility( View.GONE );
            thirty_one.setVisibility( View.GONE );
        }
        else if (month.equals( "4月" )||month.equals( "6月" )||month.equals( "9月" )||month.equals( "11月" )){
            thirty_one.setVisibility( View.GONE );
        }

        employees = new ArrayList<>();
        employees.add("王小明");
        employees.add("陳聰明");
        employees.add("劉光明");
        employees.add("吳輝明");
        employees.add("邱朝明");
        employees.add("葉大明");
        employees.add("劉案全");
        employees.add("王守興");
        employees.add("彭玉唱");
        employees.add("徐將義");
        employees.add("劉曹可");
        employees.add("秦因文");
        employees.add("方子優");
        employees.add("古蘭花");
        employees.add("朱柯基");
        BonusAdapter adapter = new BonusAdapter( employees );
        bonus_list.setAdapter(adapter);

        data = new ArrayList<>();
        int i;
        for(i = 0; i < employees.size(); i++){
            String[] row_data = {employees.get(i), "20000"};
            data.add(row_data);
        }
        SalaryAdapter salaryAdapter = new SalaryAdapter(data);
        employeeDataView.setLayoutManager(new LinearLayoutManager(context));
        employeeDataView.setAdapter(salaryAdapter);

        test1.setLayoutManager(new LinearLayoutManager(context));
        test1.setAdapter(salaryAdapter);


        back_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back_intent = new Intent(Bonus_List_Detail.this, Bonus_List.class);
                startActivity( back_intent );
            }
        } );
        export_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent export_intent = new Intent();
                export_intent.setAction( Intent.ACTION_SEND );
                export_intent.putExtra( Intent.EXTRA_TEXT,"分享此報表" );
                export_intent.setType( "text/plain" );

                Intent share_intent = Intent.createChooser( export_intent, null);
                startActivity( share_intent);
            }
        } );
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Bonus_List_Detail.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Bonus_List_Detail.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Bonus_List_Detail.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Bonus_List_Detail.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Bonus_List_Detail.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
