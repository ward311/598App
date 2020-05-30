package com.example.homerenting_prototype_one;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.valuation.Valuation;

public class Add_Valuation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__valuation);
        Button edit_btn = findViewById(R.id.edit_furniture_btn);
        final Button pick_date_btn = findViewById(R.id.pickDate_valuate_btn);
        //final EditText pick_date_edit = findViewById(R.id.pickDate_valuate_edit);
        final TextView pick_date_text = findViewById( R.id.pickDate_valuate_text );
        Button finish_btn = findViewById(R.id.finish_evaluation_btn);
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
//        pick_date_edit.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DatePickerDialog datePicker;
//                datePicker = new DatePickerDialog( Add_Valuation.this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        pick_date_text.setText(String.valueOf( year )+"/"+String.valueOf( month+1 )+"/"+String.valueOf( dayOfMonth ));
//                    }
//                },0,0,0 );
//                datePicker.show();
//                pick_date_edit.setVisibility( View.GONE );
//                pick_date_text.setVisibility( View.VISIBLE );
//            }
//        } );
        pick_date_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePicker;
                datePicker = new DatePickerDialog( Add_Valuation.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        pick_date_text.setText(String.valueOf( year )+"/"+String.valueOf( month+1 )+"/"+String.valueOf( dayOfMonth ));
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
                Intent finish_valuation_intent = new Intent();
                finish_valuation_intent.setClass(Add_Valuation.this, Valuation.class);
                Bundle valuation_bundle = new Bundle();
                finish_valuation_intent.putExtras(valuation_bundle);
                startActivity(finish_valuation_intent);
            }
        });
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Add_Valuation.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Add_Valuation.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Add_Valuation.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Add_Valuation.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Add_Valuation.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
