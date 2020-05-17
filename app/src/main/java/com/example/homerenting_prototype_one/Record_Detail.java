package com.example.homerenting_prototype_one;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.order.Order;

import java.util.ArrayList;
import java.util.List;

public class Record_Detail extends AppCompatActivity {
    private ListView list;
    private List<String> day, employee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_record__detail );
        ImageButton back_btn = findViewById(R.id.back_imgBtn);
        TextView title_text = findViewById( R.id.title_text );
        Button export_btn = findViewById(R.id.export_btn);
        ImageButton valuation_btn = findViewById(R.id.valuation_imgBtn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);
        Bundle bundle = getIntent().getExtras();
        String month = bundle.getString( "month" );
        list = findViewById( R.id.record_list );
        day = new ArrayList<>();
        employee = new ArrayList<>();
        day.add( "1" );
        day.add( "2" );
        day.add( "3" );
        day.add( "4" );
        day.add( "5" );
        day.add( "6" );
        day.add( "7" );
        day.add( "8" );
        day.add( "9" );
        day.add( "10" );
        day.add( "11" );
        day.add( "12" );
        day.add( "13" );
        day.add( "14" );
        day.add( "15" );
        day.add( "16" );
        day.add( "17" );
        day.add( "18" );
        day.add( "19" );
        day.add( "20" );
        day.add( "21" );
        day.add( "22" );
        day.add( "23" );
        day.add( "24" );
        day.add( "25" );
        day.add( "26" );
        day.add( "27" );
        day.add( "28" );
        day.add( "29" );
        day.add( "30" );
        if (month.equals("1月")||month.equals("3月")||month.equals("5月")||month.equals("7月")||month.equals("8月")||month.equals("10月")||month.equals("12月")){
            day.add("31");
        }
        else if (month.equals("2月")){
            day.remove("30");
        }
        final RecordAdapter adapter = new RecordAdapter(day);
        list.setAdapter(adapter);

        title_text.setText( month+"工單紀錄" );
        export_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent export_intent = new Intent();
                export_intent.setAction( Intent.ACTION_SEND );
                //export_intent.putExtra( Intent.EXTRA_TEXT,getResources().getString(  ) );
                export_intent.putExtra( Intent.EXTRA_TEXT,"分享此報表" );
                export_intent.setType( "text/plain" );

                Intent share_intent = Intent.createChooser( export_intent, null);
                startActivity( share_intent);
            }
        } );
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back_setting_intent = new Intent(Record_Detail.this, Setting_Record.class);
                startActivity(back_setting_intent);
            }
        });
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Record_Detail.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Record_Detail.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Record_Detail.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Record_Detail.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Record_Detail.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
