package com.example.homerenting_prototype_one;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.order.Order;

import java.util.ArrayList;
import java.util.List;

public class Valuation extends AppCompatActivity {
    ArrayList<DataModel> dataModels;
    public ListView evaluation_list, self_evaluation, booking_evaluation, matchMaking_evaluation, cancel_evaluation;
    private List<String> data;
    private static CustomAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valuation);
        self_evaluation = findViewById(R.id.selfEvaluation_listView);
        booking_evaluation = findViewById(R.id.bookingEvaluation_listView);
        matchMaking_evaluation = findViewById(R.id.matchMaking_Evaluation_listView);
        cancel_evaluation = findViewById(R.id.cancelEvaluation_listView);
        Button self_btn = findViewById(R.id.selfEvaluation_btn);
        Button booking_btn = findViewById(R.id.bookingEvaluation_btn);
        Button matchMaking_btn = findViewById(R.id.matchMaking_Evaluation_btn);
        Button cancel_btn = findViewById(R.id.cancelEvaluation_btn);
        ImageButton valuation_btn = findViewById(R.id.valuation_imgBtn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        data = new ArrayList<>();
        evaluation_list = findViewById(R.id.evaluation_listView);
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

        ListAdapter adapter = new ListAdapter(data);
        evaluation_list.setAdapter(adapter);
        evaluation_list.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detail_intent = new Intent(Valuation.this, Valuation_Detail.class);
                startActivity(detail_intent);
            }
        });
        LinearLayout first = findViewById(R.id.first_layout);
        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail_intent = new Intent(Valuation.this, Valuation_Detail.class);
                startActivity(detail_intent);
            }
        });
//        dataModels = new ArrayList<>();
//        dataModels.add(new DataModel("10/03","9:30","汪小飛","先生","0987777777","高雄市鹽埕區五福四路164號"));
//        adapter = new CustomAdapter(dataModels,getApplicationContext());
//        self_evaluation.setAdapter(adapter);
//        self_evaluation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                DataModel dataModel = dataModels.get(position);
////                Snackbar.make(view, dataModel.getName()+"\n"+dataModel.getType()+" API: "+dataModel.getVersion_number(), Snackbar.LENGTH_LONG)
////                        .setAction("No action", null).show();
//            }
//        });
        self_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent selfValuation_intent = new Intent(Valuation.this, Valuation.class);
//                startActivity(selfValuation_intent);
//                self_evaluation.setVisibility(View.VISIBLE);
//                booking_evaluation.setVisibility(View.GONE);
//                matchMaking_evaluation.setVisibility(View.GONE);
//                cancel_evaluation.setVisibility(View.GONE);
            }
        });
        booking_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookingValuation_intent = new Intent(Valuation.this, Valuation_Booking.class);
                startActivity(bookingValuation_intent);
//                self_evaluation.setVisibility(View.GONE);
//                booking_evaluation.setVisibility(View.VISIBLE);
//                matchMaking_evaluation.setVisibility(View.GONE);
//                cancel_evaluation.setVisibility(View.GONE);
            }
        });
        matchMaking_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent matchMakingValuation_intent = new Intent(Valuation.this, Valuation_MatchMaking.class);
                startActivity(matchMakingValuation_intent);
//                self_evaluation.setVisibility(View.GONE);
//                booking_evaluation.setVisibility(View.GONE);
//                matchMaking_evaluation.setVisibility(View.VISIBLE);
//                cancel_evaluation.setVisibility(View.GONE);
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancelValuation_intent = new Intent(Valuation.this, Valuation_Cancel.class);
                startActivity(cancelValuation_intent);
//                self_evaluation.setVisibility(View.GONE);
//                booking_evaluation.setVisibility(View.GONE);
//                matchMaking_evaluation.setVisibility(View.GONE);
//                cancel_evaluation.setVisibility(View.VISIBLE);
            }
        });
//        valuation_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent valuation_intent = new Intent(Valuation.this, Valuation.class);
//                startActivity(valuation_intent);
//            }
//        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Valuation.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Valuation.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Valuation.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Valuation.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
