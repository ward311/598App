package com.example.homerenting_prototype_one;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.valuation.Valuation;

public class Add_Order extends AppCompatActivity {
    public EditText name_edit, phone_edit, moveOut_edit, moveIn_edit,
            time_edit, price_edit, notice_edit;
    public RadioButton sir_btn, lady_btn, other_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__order);
        Button edit_btn = findViewById(R.id.edit_furniture_btn);
        final Button pick_date_btn = findViewById(R.id.pickDate_order_btn);
        final TextView pick_date_text = findViewById( R.id.pickDate_order_text );
        Button finish_btn = findViewById(R.id.finish_order_btn);
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        pick_date_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePicker = new DatePickerDialog( Add_Order.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        pick_date_text.setText( String.valueOf( year )+"/"+String.valueOf( month+1 )+"/"+String.valueOf( dayOfMonth ) );
                    }
                },0,0,0 );
                datePicker.show();
                pick_date_btn.setVisibility( View.GONE );
                pick_date_text.setVisibility( View.VISIBLE );
            }
        } );
        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent finish_order_intent = new Intent();
                finish_order_intent.setClass(Add_Order.this, Order.class);
                Bundle order_bundle = new Bundle();
                //order_bundle
                finish_order_intent.putExtras(order_bundle);
                startActivity(finish_order_intent);
            }
        });
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Add_Order.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Add_Order.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Add_Order.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Add_Order.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Add_Order.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
