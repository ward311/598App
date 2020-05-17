package com.example.homerenting_prototype_one;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.order.Order;

public class Today_Detail extends AppCompatActivity {
    public ListView furniture_list;
    public String[] furnitures = {"1 單人沙發   2    ","2 兩人沙發   1    ","3 三人沙發   1    ","4 L型沙發   1    ",
            "5 沙發桌   3    ","6 傳統電視   1    ","7 液晶電視37吋以下   1    ","8 液晶電視40吋以上   1    ","9 電視櫃   1    ",
            "10 酒櫃   2    ","11 鞋櫃   2    ","12 按摩椅   1    ","13 佛桌   1    ","14 鋼琴   1    ",
            "15 健身器材   3    "};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today__detail);
//        Button edit_btn = findViewById(R.id.edit_order_btn);
        Button call_btn = findViewById(R.id.call_btn);
        //furniture_list = findViewById(R.id.furniture_listView);
        final TextView total_price = findViewById(R.id.total_text);
        Button detail_btn = findViewById(R.id.today_detail_btn);
        EditText notice_edit = findViewById(R.id.today_notive_editText);
        final EditText change_edit = findViewById(R.id.change_price_edit);
        EditText ps_edit = findViewById(R.id.today_ps_edit);
        final Button check_btn = findViewById(R.id.check_todayOrder_btn);
        ImageButton valuation_btn = findViewById(R.id.valuation_imgBtn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);
        final int price = Integer.parseInt(total_price.getText().toString());

        ArrayAdapter furniture_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,furnitures);
        //furniture_list.setAdapter(furniture_adapter);
        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call_intent = new Intent(Intent.ACTION_DIAL);
                call_intent.setData(Uri.parse("tel:0933669877"));
                startActivity(call_intent);
            }
        });
//        final EditText check_edit = new EditText(this);
//        final EditText custoner_check_edit = new EditText(this);
        detail_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail_intent = new Intent();
                detail_intent.setClass(Today_Detail.this,Furniture_Location.class);
                Bundle today_bundle = new Bundle();
                today_bundle.putString( "key","today" );
                detail_intent.putExtras( today_bundle );
                startActivity(detail_intent);
            }
        } );
        change_edit.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String num = change_edit.getText().toString().trim();
                if (num.matches( "[+]" )){

                }
                else if (num.matches( "[-]" )){

                }
                else {

                }
            }
        } );
        check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Today_Detail.this)
                        .setTitle("收款")
                        .setMessage("確認家具完好無缺後，向您\n收搬家金額"+String.valueOf( price )+"元。\n通知已寄給您的信箱，完成後請您給我們意見回饋，感謝好評。")
                        .setPositiveButton( "確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                check_btn.setText("完成當日工單");
                                check_btn.setOnClickListener( new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent next_intent = new Intent(Today_Detail.this,Order_Today.class);
                                        startActivity( next_intent );
                                    }
                                } );
//                                new AlertDialog.Builder(Today_Detail.this)
//                                        .setTitle("確認工單後顧客簽名")
//                                        .setView(check_edit)
//                                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                check_btn.setText("收款");
//                                                check_btn.setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {
//                                                        new AlertDialog.Builder( Today_Detail.this)
//                                                                .setTitle("收款")
//                                                                .setMessage("確認家具完好無缺後，向您\n收搬家金額6000元。\n通知已寄給您的信箱，完成後請您給我們意見回饋，感謝好評。")
//                                                                .setPositiveButton( "確認", new DialogInterface.OnClickListener() {
//                                                                    @Override
//                                                                    public void onClick(DialogInterface dialog, int which) {
//                                                                        new AlertDialog.Builder(Today_Detail.this)
//                                                                                .setTitle("待確認款項後顧客簽名")
//                                                                                .setView(custoner_check_edit)
//                                                                                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
//                                                                                    @Override
//                                                                                    public void onClick(DialogInterface dialog, int which) {
//                                                                                        check_btn.setText("完成當日工單");
//                                                                                        check_btn.setOnClickListener(new View.OnClickListener() {
//                                                                                            @Override
//                                                                                            public void onClick(View v) {
//                                                                                                Intent back_order_intent = new Intent(Today_Detail.this, Order_Today.class);
//                                                                                                startActivity(back_order_intent);
//                                                                                            }
//                                                                                        });
//                                                                                    }
//                                                                                }).setNegativeButton("取消",null).create()
//                                                                                .show();
//                                                                    }
//                                                                } ).create().show();
//                                                    }
//                                                });
//                                            }
//                                        }).setNegativeButton("取消",null).create()
//                                        .show();
                            }
                        } )
                        .setNegativeButton("取消",null).create()
                        .show();
            }
        });
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Today_Detail.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Today_Detail.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Today_Detail.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Today_Detail.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Today_Detail.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
