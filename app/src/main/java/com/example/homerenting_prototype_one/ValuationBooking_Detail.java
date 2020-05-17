package com.example.homerenting_prototype_one;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.order.Order;

import java.util.GregorianCalendar;

public class ValuationBooking_Detail extends AppCompatActivity {
//    public ListView furniture_list;
    public String[] furnitures = {"1 單人沙發   2    ","2 兩人沙發   1    ","3 三人沙發   1    ","4 L型沙發   1    ",
            "5 沙發桌   3    ","6 傳統電視   1    ","7 液晶電視37吋以下   1    ","8 液晶電視40吋以上   1    ","9 電視櫃   1    ",
            "10 酒櫃   2    ","11 鞋櫃   2    ","12 按摩椅   1    ","13 佛桌   1    ","14 鋼琴   1    ",
            "15 健身器材   3    "};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valuation_booking__detail);
        Button phoneCall_btn = findViewById(R.id.call_btn);
        Button change_btn = findViewById(R.id.edit_furniture_btn);
//        furniture_list = findViewById(R.id.furniture_listView);
        EditText notice_edit = findViewById(R.id.booking_notice_edit);
        final EditText pickDate_edit = findViewById(R.id.pickDate_editText);
        final EditText pickTime_edit = findViewById(R.id.pickTime_editText);
        EditText pickCar_edit = findViewById(R.id.pickCar_editText);
        EditText hour_edit = findViewById(R.id.hour_editText);
        EditText price_edit = findViewById( R.id.price_editText );
        Button check_btn = findViewById(R.id.check_evaluation_btn);
        ImageButton valuation_btn = findViewById(R.id.valuation_imgBtn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);
        final GregorianCalendar calendar = new GregorianCalendar();
        ArrayAdapter furniture_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,furnitures);
//        furniture_list.setAdapter(furniture_adapter);
        phoneCall_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call_intent = new Intent(Intent.ACTION_DIAL);
                call_intent.setData(Uri.parse("tel:0933669877"));
                startActivity(call_intent);
            }
        });
        change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent change_intent = new Intent(ValuationBooking_Detail.this, Edit_Furniture.class);
                startActivity(change_intent);
            }
        });
        check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ValuationBooking_Detail.this)
                        .setTitle("媒合中")
                        .setMessage("到府估價單媒合中，成功媒\n合會成為訂單，請公司注意\n新訂單通知。")
                        .setPositiveButton( "確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent checked_intent = new Intent(ValuationBooking_Detail.this, Valuation_Booking.class);
                                startActivity(checked_intent);
                            }
                        } )
                        .show();
            }
        });
        pickDate_edit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog date_picker = new DatePickerDialog( ValuationBooking_Detail.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        pickDate_edit.setText( String.valueOf( year )+"/"+String.valueOf( month+1 )+"/"+String.valueOf( dayOfMonth )  );
                    }
                },calendar.get( GregorianCalendar.YEAR ),calendar.get( GregorianCalendar.MONTH ),calendar.get( GregorianCalendar.DAY_OF_MONTH ) );
                date_picker.show();
            }
        } );
        pickTime_edit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog time_picker = new TimePickerDialog( ValuationBooking_Detail.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        pickTime_edit.setText( (hourOfDay>12? hourOfDay-12:hourOfDay)+":"+minute+""+(hourOfDay>12? "PM":"AM"));
                    }
                },calendar.get(GregorianCalendar.DAY_OF_MONTH ),calendar.get(GregorianCalendar.MINUTE ),false );
                time_picker.show();
            }
        } );
        final Context context = this;
        pickCar_edit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setTitle("請輸入安排車輛");
                dialog.setContentView(R.layout.car_dialog);
                Button done_btn = dialog.findViewById( R.id.done_btn );
                done_btn.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                } );
                Button cancel_btn = dialog.findViewById( R.id.cancel_btn );
                cancel_btn.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                } );
                final TextView num_first = dialog.findViewById( R.id.num_1 );
                final TextView num_second = dialog.findViewById( R.id.num_2 );
                final TextView num_third = dialog.findViewById( R.id.num_3 );
                final String num_one = num_first.getText().toString();
                final String num_two = num_second.getText().toString();
                final String num_three = num_third.getText().toString();
                Button minus_first = dialog.findViewById( R.id.minus_btn_1 );
                Button minus_second = dialog.findViewById( R.id.minus_btn_2 );
                Button minus_third = dialog.findViewById( R.id.minus_btn_3 );
                Button plus_first = dialog.findViewById( R.id.plus_btn_1 );
                Button plus_second = dialog.findViewById( R.id.plus_btn_2 );
                Button plus_third = dialog.findViewById( R.id.plus_btn_3 );
                minus_first.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        num_first.setText( String.valueOf((int) Integer.parseInt(num_one)-1) );
                    }
                } );
                minus_second.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        num_second.setText( String.valueOf((int) Integer.parseInt(num_two)-1) );
                    }
                } );
                minus_third.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        num_third.setText( String.valueOf((int) Integer.parseInt(num_three)-1) );
                    }
                } );
                plus_first.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        num_first.setText( String.valueOf((int) Integer.parseInt(num_one)+1) );
                    }
                } );
                plus_second.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        num_second.setText( String.valueOf((int) Integer.parseInt(num_two)+1) );
                    }
                } );
                plus_third.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        num_third.setText( String.valueOf((int) Integer.parseInt(num_three)+1) );
                    }
                } );
                dialog.show();
                dialog.getWindow().setLayout( 1400,2000 );
//                new AlertDialog.Builder( ValuationBooking_Detail.this )
//                        .setTitle( "請輸入安排車輛" )
//                        .setPositiveButton( "確定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        } )
//                        .setNegativeButton( "取消",null ).create()
//                        .show();
            }
        } );
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(ValuationBooking_Detail.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(ValuationBooking_Detail.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(ValuationBooking_Detail.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(ValuationBooking_Detail.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(ValuationBooking_Detail.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
