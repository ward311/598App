package com.example.homerenting_prototype_one;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.valuation.Valuation;

public class Setting_Service extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting__service);
        ImageButton back_btn = findViewById(R.id.back_imgBtn);
        //final Button edit_btn = findViewById(R.id.informationEdit_btn);
//        final TextView range_text = findViewById(R.id.range_text);
//        final EditText range_edit = findViewById(R.id.range_editText);
        final Button add_item = findViewById(R.id.add_item_btn);
        final Button delete_item = findViewById(R.id.delete_item_btn);
        Button add_special = findViewById(R.id.add_special_btn);
        Button delete_special = findViewById(R.id.delete_special_btn);
        final Button add_type = findViewById(R.id.add_type_btn);
        final Button delete_type = findViewById(R.id.delete_type_btn);
        final Button add_box_type = findViewById(R.id.add_boxType_btn);
        final Button delete_box_type = findViewById(R.id.delete_boxType_btn);
        final Button add_material = findViewById(R.id.add_material_btn);
        final Button delete_material = findViewById(R.id.delete_material_btn);
        //final Button finish_btn = findViewById(R.id.service_finished_btn);
        ImageButton valuation_btn = findViewById(R.id.valuation_imgBtn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);
        final LinearLayout item_layout = findViewById(R.id.new_item_layout);
        final LinearLayout special_layout = findViewById(R.id.new_special_layout);
        final LinearLayout type_layout = findViewById(R.id.new_type_layout);
        final LinearLayout boxType_layout = findViewById(R.id.new_boxType_layout);
        final LinearLayout material_layout = findViewById(R.id.new_material_layout);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back_setting_intent = new Intent(Setting_Service.this, Setting.class);
                startActivity(back_setting_intent);
            }
        });
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Setting_Service.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Setting_Service.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Setting_Service.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Setting_Service.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Setting_Service.this, Setting.class);
                startActivity(setting_intent);
            }
        });
        final EditText add_item_edit = new EditText(this);
        final EditText add_special_edit = new EditText(this);
        final EditText add_type_edit = new EditText(this);
        final EditText add_boxType_edit = new EditText(this);
        final EditText add_material_edit = new EditText(this);
        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (add_item_edit!=null){
                    ViewGroup item_viewGroup = (ViewGroup) add_item_edit.getParent();
                    if (item_viewGroup!=null){
                        item_viewGroup.removeView( add_item_edit );
                    }
                }
                new AlertDialog.Builder(Setting_Service.this)
                        .setTitle("新增服務項目")
                        .setView(add_item_edit)
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CheckBox item_cb = new CheckBox(getApplicationContext());
                                item_cb.setText(add_item_edit.getText().toString());
                                item_layout.addView(item_cb);
                            }
                        }).setNegativeButton("取消",null).create()
                        .show();
            }
        });
        delete_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder( Setting_Service.this )
                        .setTitle( "確認是否刪除服務項目？" )
                        .setPositiveButton( "確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                item_layout.removeView( item_layout.getChildAt( 0 ) );
                            }
                        } ).setNegativeButton( "取消",null ).create()
                        .show();
            }
        });
        add_special.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (add_special_edit!=null){
                    ViewGroup special_viewGroup = (ViewGroup)add_special_edit.getParent();
                    if (special_viewGroup!=null){
                        special_viewGroup.removeView( add_special_edit );
                    }
                }
                new AlertDialog.Builder( Setting_Service.this )
                        .setTitle( "新增特殊項目" )
                        .setView( add_special_edit )
                        .setPositiveButton( "確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CheckBox special_cb = new CheckBox( getApplicationContext() );
                                special_cb.setText( add_special_edit.getText().toString() );
                                special_layout.addView( special_cb );
                            }
                        } ).setNegativeButton( "取消",null ).create()
                        .show();
            }
        } );
        delete_special.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder( Setting_Service.this )
                        .setTitle( "確認是否刪除特殊項目？" )
                        .setPositiveButton( "確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                special_layout.removeView( special_layout.getChildAt( 0 ) );
                            }
                        } ).setNegativeButton( "取消",null ).create()
                        .show();
            }
        } );
        add_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (add_type_edit!=null){
                    ViewGroup type_viewGroup = (ViewGroup)add_type_edit.getParent();
                    if (type_viewGroup!=null){
                        type_viewGroup.removeView( add_type_edit );
                    }
                }
                new AlertDialog.Builder(Setting_Service.this)
                        .setTitle("新增車輛種類")
                        .setView(add_type_edit)
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CheckBox type_cb = new CheckBox(getApplicationContext());
                                type_cb.setText(add_type_edit.getText().toString());
                                type_layout.addView(type_cb);
                            }
                        }).setNegativeButton("取消",null).create()
                        .show();
            }
        });
        delete_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder( Setting_Service.this )
                        .setTitle( "確認是否刪除車輛種類？" )
                        .setPositiveButton( "確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                type_layout.removeView( type_layout.getChildAt( 0 ) );
                            }
                        } ).setNegativeButton( "取消",null ).create()
                        .show();
            }
        });
        add_box_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (add_boxType_edit!=null){
                    ViewGroup boxType_viewGroup = (ViewGroup)add_boxType_edit.getParent();
                    if (boxType_viewGroup!=null){
                        boxType_viewGroup.removeView( add_boxType_edit );
                    }
                }
                new AlertDialog.Builder(Setting_Service.this)
                        .setTitle("新增紙箱種類")
                        .setView(add_boxType_edit)
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CheckBox boxType_cb = new CheckBox(getApplicationContext());
                                boxType_cb.setText(add_boxType_edit.getText().toString());
                                boxType_layout.addView(boxType_cb);
                            }
                        }).setNegativeButton("取消",null).create()
                        .show();
            }
        });
        delete_box_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder( Setting_Service.this )
                        .setTitle( "確認是否刪除紙箱種類？" )
                        .setPositiveButton( "確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                boxType_layout.removeView( boxType_layout.getChildAt( 0 ) );
                            }
                        } ).setNegativeButton( "取消",null ).create()
                        .show();
            }
        });
        add_material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (add_material_edit!=null){
                    ViewGroup material_viewGroup = (ViewGroup)add_material_edit.getParent();
                    if (material_viewGroup!=null){
                        material_viewGroup.removeView( add_material_edit );
                    }
                }
                new AlertDialog.Builder(Setting_Service.this)
                        .setTitle("新增包裝材料")
                        .setView(add_material_edit)
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CheckBox material_cb = new CheckBox(getApplicationContext());
                                material_cb.setText(add_material_edit.getText().toString());
                                material_layout.addView(material_cb);
                            }
                        }).setNegativeButton("取消",null).create()
                        .show();
            }
        });
        delete_material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder( Setting_Service.this )
                        .setTitle( "確認是否刪除包裝材料？" )
                        .setPositiveButton( "確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                material_layout.removeView( material_layout.getChildAt( 0 ) );
                            }
                        } ).setNegativeButton( "取消",null ).create()
                        .show();
            }
        });

//        finish_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                edit_btn.setVisibility(View.VISIBLE);
//                range_text.setVisibility(View.VISIBLE);
//                range_edit.setVisibility(View.GONE);
//                add_item.setVisibility(View.GONE);
//                delete_item.setVisibility(View.GONE);
//                add_type.setVisibility(View.GONE);
//                delete_type.setVisibility(View.GONE);
//                add_box_type.setVisibility(View.GONE);
//                delete_box_type.setVisibility(View.GONE);
//                add_material.setVisibility(View.GONE);
//                delete_material.setVisibility(View.GONE);
//                finish_btn.setVisibility(View.GONE);
//
//                range_text.setText(range_edit.getText().toString());
//            }
//        });
    }
}
