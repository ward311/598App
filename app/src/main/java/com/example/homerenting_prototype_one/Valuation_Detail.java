package com.example.homerenting_prototype_one;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.order.Order;

import java.util.GregorianCalendar;

public class Valuation_Detail extends AppCompatActivity {
    public ListView furniture_list;
    public String[] furnitures = {"1 單人沙發   2    ","2 兩人沙發   1    ","3 三人沙發   1    ","4 L型沙發   1    ",
            "5 沙發桌   3    ","6 傳統電視   1    ","7 液晶電視37吋以下   1    ","8 液晶電視40吋以上   1    ","9 電視櫃   1    ",
            "10 酒櫃   2    ","11 鞋櫃   2    ","12 按摩椅   1    ","13 佛桌   1    ","14 鋼琴   1    ",
            "15 健身器材   3    "};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valuation__detail);
        Button phoneCall_btn = findViewById(R.id.call_btn);
        //furniture_list = findViewById(R.id.furniture_listView);
        final Button detail_btn = findViewById(R.id.detail_btn);
        Button check_btn = findViewById(R.id.check_evaluation_btn);
        Button check_price_btn = findViewById(R.id.check_price_btn);
        final EditText pickDate_edit = findViewById(R.id.pickDate_editText);
        final EditText pickTime_edit = findViewById(R.id.pickTime_editText);
        final TextView pickDate_text = findViewById( R.id.pickDate_text );
        final TextView pickTime_text = findViewById( R.id.pickTime_text );
        ImageButton valuation_btn = findViewById(R.id.valuation_imgBtn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);
        final GregorianCalendar calendar = new GregorianCalendar();
        Context context;
        ArrayAdapter furniture_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,furnitures);
        //furniture_list.setAdapter(furniture_adapter);
        phoneCall_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call_intent = new Intent(Intent.ACTION_DIAL);
                call_intent.setData(Uri.parse("tel:0933669877"));
//              call_intent.setData(Uri.parse("tel"+phone_number));
//                if (ActivityCompat.checkSelfPermission(Valuation_Detail.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
//                    return;
//                }
//                Intent call_intent = new Intent("android.intent.action.CALL",Uri.parse("tel:0933669877"));
                startActivity(call_intent);
            }
        });
        detail_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail_intent = new Intent(  );
                detail_intent.setClass( Valuation_Detail.this,Furniture_Detail.class );
                Bundle detail_bundle = new Bundle();
                detail_bundle.putString( "key","valuation" );
                detail_intent.putExtras( detail_bundle );
                startActivity( detail_intent );
            }
        } );
        pickDate_edit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog picker = new DatePickerDialog( Valuation_Detail.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        pickDate_text.setText( String.valueOf( year )+"/"+String.valueOf( month+1 )+"/"+String.valueOf( dayOfMonth ) );
                        pickDate_edit.setHint(String.valueOf( year )+"/"+String.valueOf( month+1 )+"/"+String.valueOf( dayOfMonth )  );
                    }
                },calendar.get(GregorianCalendar.YEAR ),calendar.get( GregorianCalendar.MONTH ),calendar.get( GregorianCalendar.DAY_OF_MONTH ) );
                picker.show();
            }
        } );
        pickTime_edit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("WrongConstant") TimePickerDialog timePicker = new TimePickerDialog( Valuation_Detail.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        pickTime_text.setText( (hourOfDay>12? hourOfDay-12:hourOfDay)+":"+minute+""+(hourOfDay>12? "PM":"AM") );
                        pickTime_edit.setHint( (hourOfDay>12? hourOfDay-12:hourOfDay)+":"+minute+""+(hourOfDay>12? "PM":"AM") );
                    }
                },calendar.get( GregorianCalendar.DAY_OF_MONTH),calendar.get( android.icu.util.GregorianCalendar.MINUTE ),false);
                timePicker.show();
            }
        } );
        check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate_edit.setVisibility( View.GONE );
                pickDate_text.setVisibility( View.VISIBLE );
                pickTime_edit.setVisibility( View.GONE );
                pickTime_text.setVisibility( View.VISIBLE );
//                Intent checked_intent = new Intent(Valuation_Detail.this, Valuation.class);
//                startActivity(checked_intent);
            }
        });
        check_price_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent checked_intent = new Intent(Valuation_Detail.this, Valuation.class);
                startActivity(checked_intent);
            }
        } );
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Valuation_Detail.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Valuation_Detail.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Valuation_Detail.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Valuation_Detail.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Valuation_Detail.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
