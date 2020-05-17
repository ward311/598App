package com.example.homerenting_prototype_one;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.order.Order;

import java.util.ArrayList;
import java.util.List;

public class Edit_Furniture extends AppCompatActivity {
    private ListView list;
    private List<String> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__furniture);
        Button check_btn = findViewById(R.id.check_furniture_btn);
        ImageButton valuation_btn = findViewById(R.id.valuation_imgBtn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        data = new ArrayList<>();
        list = findViewById(R.id.furniture_listView);
        data.add("1. 單人沙發");
        data.add("2. 兩人沙發");
        data.add("3. 三人沙發");
        data.add("4. L型沙發");
        data.add("5. 沙發桌");
        data.add("6. 傳統電視");
        data.add("7. 液晶電視37吋以下");
        data.add("8. 液晶電視40吋以上");
        data.add("9. 電視櫃");
        data.add("10. 酒櫃");
        data.add("11. 鞋櫃");
        data.add("12. 按摩椅");
        data.add("13. 佛桌");
        data.add("14. 鋼琴");
        data.add("15. 健身器材");

        FurnitureAdapter adapter = new FurnitureAdapter(data);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent finish_intent = new Intent(Edit_Furniture.this, ValuationBooking_Detail.class);
                startActivity(finish_intent);
            }
        });
        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Edit_Furniture.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Edit_Furniture.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Edit_Furniture.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Edit_Furniture.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Edit_Furniture.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
