package com.example.homerenting_prototype_one;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.valuation.Valuation;

import java.util.ArrayList;
import java.util.List;

public class Calendar extends AppCompatActivity {
    public ListView valuation_list, order_list;
    private List<String> valuation_arrayList, order_arrayList;
    final Context context = this;
    int num_self, num_home, num_order, num_today_valuation, num_today_order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        num_self = 2;
        num_home = 1;
        num_order = 2;
        num_today_valuation = 13;
        num_today_order = 11;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle( "消息提醒" );
//        builder.setMessage( "1.新自助估價單   2筆\n2.新到府估價單    1筆\n3.新訂單      2筆\n4.今日到府估價      13筆" );
        builder.setMessage( "1.新自助估價單   "+String.valueOf( num_self )+"筆\n2.新到府估價單   "+String.valueOf( num_home )+"筆\n3.新訂單               "+String.valueOf( num_order )+"筆\n4.今日到府估價   "+String.valueOf( num_today_valuation )+"筆\n5.今日到府搬家   "+String.valueOf( num_today_order )+"筆");
        builder.setPositiveButton( "確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        } );
        builder.create();
        builder.show();
//        valuation_list = findViewById(R.id.homeValuation_listView);
//        order_list = findViewById(R.id.movingOrder_listView);
//        final Button valuationList_btn = findViewById(R.id.valuation_list_btn);
//        final Button orderList_btn = findViewById(R.id.order_list_btn);
//        final Button valuationList_clickBtn = findViewById(R.id.valuation_list_click_btn);
//        final Button orderList_clickBtn = findViewById(R.id.order_list_click_btn);
        final ImageButton valuation_btn = findViewById(R.id.valuation_imgBtn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        final String[] valuations = {"1 09:00 高雄市三民區建國四路15號","2 09:30 高雄市三民區建國四路15號","3 10:00 高雄市三民區建國四路15號",
                "4 10:20 高雄市三民區建國四路15號","5 10:40 高雄市三民區建國四路15號","6 11:00 高雄市三民區建國四路15號",
                "7 11:20 高雄市三民區建國四路15號","8 11:50 高雄市三民區建國四路15號","9 13:00 高雄市三民區建國四路15號",
                "10 14:00 高雄市三民區建國四路15號","11 14:30 高雄市三民區建國四路15號","12 15:00 高雄市三民區建國四路15號",
                "13 16:00 高雄市三民區建國四路15號"};
        String[] orders = {"1 09:00 高雄市三民區建國四路15號","2 09:30 高雄市三民區建國四路15號","3 10:00 高雄市三民區建國四路15號",
                "4 10:20 高雄市三民區建國四路15號","5 10:40 高雄市三民區建國四路15號","6 11:00 高雄市三民區建國四路15號",
                "7 11:20 高雄市三民區建國四路15號","8 11:50 高雄市三民區建國四路15號","9 13:00 高雄市三民區建國四路15號",
                "10 14:00 高雄市三民區建國四路15號","11 14:30 高雄市三民區建國四路15號"};
        //Context context;
        valuation_arrayList = new ArrayList<>();
        order_arrayList = new ArrayList<>();
        valuation_arrayList.add( "1/ 09:00/ 高雄市三民區建國四路15號" );
        valuation_arrayList.add( "2/ 09:30/ 高雄市三民區建國四路15號" );
        valuation_arrayList.add( "3/ 10:00/ 高雄市三民區建國四路15號" );
        valuation_arrayList.add( "4/ 10:20/ 高雄市三民區建國四路15號" );
        valuation_arrayList.add( "5/ 10:40/ 高雄市三民區建國四路15號" );
        valuation_arrayList.add( "6/ 11:00/ 高雄市三民區建國四路15號" );
        valuation_arrayList.add( "7/ 11:20/ 高雄市三民區建國四路15號" );
        valuation_arrayList.add( "8/ 11:50/ 高雄市三民區建國四路15號" );
        valuation_arrayList.add( "9/ 13:00/ 高雄市三民區建國四路15號" );
        valuation_arrayList.add( "10/ 14:00/ 高雄市三民區建國四路15號" );
        valuation_arrayList.add( "11/ 14:30/ 高雄市三民區建國四路15號" );
        valuation_arrayList.add( "12/ 15:00/ 高雄市三民區建國四路15號" );
        valuation_arrayList.add( "13/ 16:00/ 高雄市三民區建國四路15號" );

        order_arrayList.add( "1/ 09:00/ 高雄市三民區建國四路15號" );
        order_arrayList.add( "2/ 09:00/ 高雄市三民區建國四路15號" );
        order_arrayList.add( "3/ 09:00/ 高雄市三民區建國四路15號" );
        order_arrayList.add( "4/ 09:00/ 高雄市三民區建國四路15號" );
        order_arrayList.add( "5/ 09:00/ 高雄市三民區建國四路15號" );
        order_arrayList.add( "6/ 09:00/ 高雄市三民區建國四路15號" );
        order_arrayList.add( "7/ 09:00/ 高雄市三民區建國四路15號" );
        order_arrayList.add( "8/ 09:00/ 高雄市三民區建國四路15號" );
        order_arrayList.add( "9/ 09:00/ 高雄市三民區建國四路15號" );
        order_arrayList.add( "10/ 09:00/ 高雄市三民區建國四路15號" );
        order_arrayList.add( "11/ 09:00/ 高雄市三民區建國四路15號" );
        order_arrayList.add( "12/ 09:00/ 高雄市三民區建國四路15號" );
        order_arrayList.add( "13/ 09:00/ 高雄市三民區建國四路15號" );
        final ArrayAdapter valuation_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,valuation_arrayList);
        final ArrayAdapter order_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,orders);
//        valuation_list.setAdapter(valuation_adapter);
//        order_list.setAdapter(order_adapter);
        final ListView valuations_list = new ListView(this);
        valuations_list.setAdapter(valuation_adapter);
        valuations_list.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                final ListView listView = (ListView) parent;
                new AlertDialog.Builder(Calendar.this)
                        .setTitle("是否確認刪除?")
                        .setMessage(listView.getItemAtPosition(position).toString())
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //valuation_adapter.remove(listView.getItemAtPosition(position));
                                //valuations.remove(position);
                                valuation_arrayList.remove( position );
                                valuation_adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("取消",null).create()
                        .show();
            }
        });
        CalendarView calendar = findViewById(R.id.main_calendar);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//                new AlertDialog.Builder(Calendar.this)
//                        .setTitle(String.valueOf(year)+"."+String.valueOf(month+1)+"."+String.valueOf(dayOfMonth))
//                        //.setMessage("")
//                        .setView(valuations_list)
//                        .setPositiveButton("新增到府估價單", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Intent add_valuation = new Intent(Calendar.this, Add_Valuation.class);
//                                startActivity(add_valuation);
//                            }
//                        })
//                        .setNegativeButton("取消",null)
//                        .setNeutralButton("新增搬家訂單", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Intent add_order = new Intent(Calendar.this, Add_Order.class);
//                                startActivity(add_order);
//                            }
//                        }).create()
//                        .show();
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.calendar_dialog);
                final TextView valuation_bar = dialog.findViewById( R.id.valuation_bar );
                final TextView order_bar = dialog.findViewById( R.id.order_bar );
                TextView day_text = dialog.findViewById( R.id.today_text );
                day_text.setText( String.valueOf(year)+"."+String.valueOf(month+1)+"."+String.valueOf(dayOfMonth) );
                final ListView valuation_list = dialog.findViewById( R.id.valuation_today_list );
                final ListView order_list = dialog.findViewById( R.id.order_today_list );
                final Button valuation_list_btn = dialog.findViewById( R.id.add_valuation_btn );
                final Button order_list_btn = dialog.findViewById( R.id.add_order_btn );
                Button cancel_btn = dialog.findViewById( R.id.cancel_btn );
                valuation_bar.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        valuation_bar.setTextColor( Color.YELLOW);
                        order_bar.setTextColor( Color.WHITE );
                        valuation_list.setVisibility( View.VISIBLE );
                        order_list.setVisibility( View.GONE );
                        valuation_list_btn.setVisibility( View.VISIBLE );
                        order_list_btn.setVisibility( View.GONE );
                    }
                } );
                order_bar.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        valuation_bar.setTextColor( Color.WHITE );
                        order_bar.setTextColor( Color.YELLOW );
                        valuation_list.setVisibility( View.GONE );
                        order_list.setVisibility( View.VISIBLE );
                        valuation_list_btn.setVisibility( View.GONE );
                        order_list_btn.setVisibility( View.VISIBLE );

                    }
                } );
                valuation_list.setAdapter( valuation_adapter );
                valuation_list.setOnItemClickListener( new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int pos, long id) {
                        final ListView listView = (ListView) parent;
                        new AlertDialog.Builder(Calendar.this)
                                .setTitle("是否確認刪除?")
                                .setMessage(listView.getItemAtPosition(pos).toString())
                                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //valuation_adapter.remove(listView.getItemAtPosition(position));
                                        //valuations.remove(position);
                                        valuation_arrayList.remove(pos);
                                        valuation_adapter.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("取消",null).create()
                                .show();
                    }
                } );
                order_list.setAdapter( order_adapter );
                order_list.setOnItemClickListener( new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int pos2, long id) {
                        final ListView listView = (ListView) parent;
                        new AlertDialog.Builder(Calendar.this)
                                .setTitle("是否確認刪除?")
                                .setMessage(listView.getItemAtPosition(pos2).toString())
                                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //valuation_adapter.remove(listView.getItemAtPosition(position));
                                        //valuations.remove(position);
                                        order_arrayList.remove(pos2);
                                        order_adapter.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("取消",null).create()
                                .show();
                    }
                } );
                valuation_list_btn.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent add_valuation_intent = new Intent();
                        add_valuation_intent.setClass(Calendar.this,Add_Valuation.class);
                        startActivity(add_valuation_intent);
                    }
                } );
                order_list_btn.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent add_order_intent = new Intent();
                        add_order_intent.setClass(Calendar.this,Add_Order.class);
                        startActivity(add_order_intent);
                    }
                } );
                cancel_btn.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                } );
                dialog.show();
                dialog.getWindow().setLayout( 1400,2000 );
            }
        });
//        valuationList_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                valuation_list.setVisibility(View.VISIBLE);
//                order_list.setVisibility(View.GONE);
//                valuationList_btn.setVisibility(View.GONE);
//                valuationList_clickBtn.setVisibility(View.VISIBLE);
//                orderList_btn.setVisibility(View.VISIBLE);
//                orderList_clickBtn.setVisibility(View.GONE);
//
//            }
//        });
//        orderList_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                valuation_list.setVisibility(View.GONE);
//                order_list.setVisibility(View.VISIBLE);
//                valuationList_btn.setVisibility(View.VISIBLE);
//                valuationList_clickBtn.setVisibility(View.GONE);
//                orderList_btn.setVisibility(View.GONE);
//                orderList_clickBtn.setVisibility(View.VISIBLE);
//            }
//        });
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Calendar.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Calendar.this, Order.class);
                startActivity(order_intent);
            }
        });
//        calendar_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent calender_intent = new Intent(Calendar.this, Calendar.class);
//                startActivity(calender_intent);
//            }
//        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Calendar.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Calendar.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
