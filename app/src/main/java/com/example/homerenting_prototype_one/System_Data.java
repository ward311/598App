package com.example.homerenting_prototype_one;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.valuation.Valuation;

import java.util.ArrayList;
import java.util.List;

public class System_Data extends AppCompatActivity {

//    public String[] employees = {"王小明","陳聰明","劉光明","吳輝明","邱朝明","葉大明","劉案全",
//            "王守興","彭玉唱","徐將義","劉曹可","秦因文","方子優","古蘭花","朱柯基"};
//    public String[] cars = {"3.5噸平斗車NRT-134","3.5噸平斗車HWE-353","3.5噸平斗車ITE-774","3.5噸平斗車BTU-255","3.5噸箱型車YEU-712",
//            "3.5噸箱型車NKC-456","3.5噸箱型車ZXE-654","7.7噸箱型車RSF-673","7.7噸箱型車ESF-553","7.7噸箱型車KMS-352"};
    public ListView employee_list, car_list;
    private List<String> employee_aList, car_aList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system__data);
        employee_list = findViewById(R.id.employee_list);
        car_list = findViewById(R.id.car_list);
        ImageButton back_btn = findViewById(R.id.back_imgBtn);
        TextView employee_text = findViewById(R.id.employee_bar);
        TextView car_text = findViewById(R.id.car_bar);
        Button addEmployee_btn = findViewById(R.id.add_employee_btn);
        Button addCar_btn = findViewById(R.id.add_car_btn);
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);
        employee_aList = new ArrayList<>();
        car_aList = new ArrayList<>();
        employee_aList.add( "王小明" );
        employee_aList.add( "陳聰明" );
        employee_aList.add( "劉光明" );
        employee_aList.add( "吳輝明" );
        employee_aList.add( "邱朝明" );
        employee_aList.add( "葉大明" );
        employee_aList.add( "劉案全" );
        employee_aList.add( "王守興" );
        employee_aList.add( "彭玉唱" );
        employee_aList.add( "徐將義" );
        employee_aList.add( "劉曹可" );
        employee_aList.add( "秦因文" );
        employee_aList.add( "方子優" );
        employee_aList.add( "古蘭花" );
        employee_aList.add( "朱柯基" );
        car_aList.add( "3.5噸平斗車NRT-134" );
        car_aList.add( "3.5噸平斗車HWE-353" );
        car_aList.add( "3.5噸平斗車ITE-774" );
        car_aList.add( "3.5噸平斗車BTU-255" );
        car_aList.add( "3.5噸箱型車YEU-712" );
        car_aList.add( "3.5噸箱型車NKC-456" );
        car_aList.add( "3.5噸箱型車ZXE-654" );
        car_aList.add( "7.7噸箱型車RSF-673" );
        car_aList.add( "7.7噸箱型車ESF-553" );
        car_aList.add( "7.7噸箱型車KMS-352" );
        final ArrayAdapter employee_adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,employee_aList);
        Context context;
        final ArrayAdapter car_adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,car_aList);
        employee_list.setAdapter(employee_adapter);
        employee_list.setOnItemClickListener( new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final ListView listView = (ListView)parent;
                new AlertDialog.Builder( System_Data.this )
                        .setTitle( "確認是否刪除？" )
                        .setMessage(listView.getItemAtPosition( position ).toString())
                        .setPositiveButton( "確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                employee_aList.remove( position );
                                employee_adapter.notifyDataSetChanged();
                            }
                        } )
                        .setNegativeButton( "取消",null ).create()
                        .show();
            }
        } );
        car_list.setAdapter(car_adapter);
        car_list.setOnItemClickListener( new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final ListView listView = (ListView)parent;
                new AlertDialog.Builder( System_Data.this )
                        .setTitle( "確認是否刪除？" )
                        .setMessage( listView.getItemAtPosition( position ).toString() )
                        .setPositiveButton( "確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                car_aList.remove( position );
                                car_adapter.notifyDataSetChanged();
                            }
                        } )
                        .setNegativeButton( "取消",null ).create()
                        .show();
            }
        } );
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(System_Data.this, System.class);
                startActivity(system_intent);
            }
        });
        employee_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                employee_list.setVisibility(View.VISIBLE);
                car_list.setVisibility(View.GONE);
            }
        });
        car_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                employee_list.setVisibility(View.GONE);
                car_list.setVisibility(View.VISIBLE);
            }
        });
        final EditText employee_edit = new EditText(this);
        final EditText car_edit = new EditText(this);
        addEmployee_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (employee_edit!=null){
                    ViewGroup employee_vg = (ViewGroup)employee_edit.getParent();
                    if (employee_vg!=null){
                        employee_vg.removeView(employee_edit);
                    }
                }
                new AlertDialog.Builder( System_Data.this )
                        .setTitle( "新增員工" )
                        .setView( employee_edit )
                        .setPositiveButton( "確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String new_employee = employee_edit.getText().toString();
                                employee_aList.add( new_employee );
                                employee_adapter.notifyDataSetChanged();
                            }
                        } )
                        .setNegativeButton( "取消",null ).create()
                        .show();
            }
        });
        addCar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder( System_Data.this )
                        .setTitle( "新增車輛" )
                        .setView( car_edit )
                        .setPositiveButton( "確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String new_car = car_edit.getText().toString();
                                car_aList.add( new_car );
                                car_adapter.notifyDataSetChanged();
                            }
                        } )
                        .setNegativeButton( "取消",null ).create()
                        .show();
            }
        });
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(System_Data.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(System_Data.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(System_Data.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(System_Data.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(System_Data.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
