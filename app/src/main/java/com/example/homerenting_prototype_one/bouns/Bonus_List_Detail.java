package com.example.homerenting_prototype_one.bouns;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homerenting_prototype_one.adapter.base_adapter.BonusAdapter;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.adapter.re_adpater.SalaryAdapter;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.setting.Setting;
import com.example.homerenting_prototype_one.sync_scroll.ScrollManager;
import com.example.homerenting_prototype_one.sync_scroll.SyncedHorizontalScrollView;
import com.example.homerenting_prototype_one.system.System;
import com.example.homerenting_prototype_one.valuation.Valuation;

import java.util.ArrayList;

import static com.example.homerenting_prototype_one.show.global_function.dip2px;

public class Bonus_List_Detail extends AppCompatActivity {
    private ArrayList<ArrayList<String[]>> data;
    private ArrayList<Integer> date;

    ScrollManager scrollManager;

    String month;

    private Context context = this;
    private int DATA_WIDTH;
    private String TAG = "Bonus_List_Detail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_bonus__list__detail );

        ImageButton back_btn = findViewById( R.id.back_imgBtn);
        TextView title_text = findViewById( R.id.month_title_text );
        Button export_btn = findViewById(R.id.export_bonus_btn);

        Bundle bundle = getIntent().getExtras();
        month = bundle.getString( "month" );

        data = new ArrayList<>();
        date = new ArrayList<>();
        scrollManager = new ScrollManager();
        DATA_WIDTH = dip2px(context, 90);

        title_text.setText( month+"月獎金報表" );

        ArrayList<String[]> data1 = new ArrayList();
        for(int i = 0; i < 2; i++){
            String[] row_data = {"員工1", String.valueOf(i+1), String.valueOf((i+1)*1)};
            data1.add(row_data);
        }
        data.add(data1);

        ArrayList<String[]> data2 = new ArrayList();
        for(int i = 0; i < 2; i++){
            String[] row_data = {"員工2", String.valueOf(i+3), String.valueOf((i+1)*2)};
            data2.add(row_data);
        }
        data.add(data2);

        date.add(1);
        date.add(1+1);
        date.add(3);
        date.add(1+3);

        setHeaderRow();
        setFixedTable();
        setSalaryTable();


        back_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );
        export_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent export_intent = new Intent();
                export_intent.setAction( Intent.ACTION_SEND );
                export_intent.putExtra( Intent.EXTRA_TEXT,"分享此報表" );
                export_intent.setType( "text/plain" );

                Intent share_intent = Intent.createChooser( export_intent, null);
                startActivity( share_intent);
            }
        } );

        globalNav();
    }

    private void setFixedTable(){
        TableLayout fixedTable = findViewById(R.id.fixedColumn_BLD);
        for(int i = 0; i < data.size(); i++){
            int dp2 = dip2px(context, 2);
            TableRow row = new TableRow(context);
            row.addView(newTextView(data.get(i).get(0)[0]));

            View rowView = newRowDividerView();
            TableRow.LayoutParams rowParams = (TableRow.LayoutParams) rowView.getLayoutParams();
            rowParams.setMargins(dp2, 0, dp2, 0);
            rowView.setLayoutParams(rowParams);
            row.addView(rowView);

            row.addView(newTextView(String.valueOf(getSumSalary(data.get(i)))));
            row.addView(newRowDividerView());
            fixedTable.addView(row);

            fixedTable.addView(newDividerView());
        }
    }

    private void setHeaderRow(){
        LinearLayout headerRow = findViewById(R.id.headerRow_LL_BLD);
        SyncedHorizontalScrollView scrollView = new SyncedHorizontalScrollView(context);
        scrollView.setHorizontalScrollBarEnabled(false); //隱藏滑動條
        scrollView.setOverScrollMode(View.OVER_SCROLL_NEVER); //關閉反彈動畫
        LinearLayout l = new LinearLayout(context);
        l.setOrientation(LinearLayout.HORIZONTAL);

        for(int i = 0; i < date.size(); i++){
            TextView datetext = newTextView(month+"/"+date.get(i));
            datetext.setTextSize(20);
            datetext.setTextColor(Color.parseColor("#FFFFFF"));
            datetext.setTypeface(null, Typeface.BOLD);
            l.addView(datetext);

            View view = newRowDividerView();
            view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            l.addView(view);
        }

        scrollView.addView(l);
        headerRow.addView(scrollView);
        scrollManager.addScrollClient(scrollView);
    }

    private void setSalaryTable(){
        LinearLayout fillableArea = findViewById(R.id.fillableArea_BLD);
        SyncedHorizontalScrollView scrollView = new SyncedHorizontalScrollView(context);
        scrollView.setOverScrollMode(View.OVER_SCROLL_NEVER); //關閉反彈動畫
        TableLayout salaryTable = new TableLayout(context);
        for(int i = 0; i < data.size(); i++){ //第幾個人
            TableRow tr = new TableRow(context);
            ArrayList<String[]> staff = data.get(i);
//            Log.d(TAG, "staff: "+staff.get(0)[0]);
            for(int ii = 0; ii < date.size(); ii++){ //第幾天
//                Log.d(TAG, "date: "+month+"/"+date.get(ii));
                String salary = "";
                for(int iii = 0; iii < staff.size(); iii++){ //第幾筆資料
                    if(Integer.parseInt(staff.get(iii)[1]) == date.get(ii)){ //有沒有第ii天的資料
                        salary = staff.get(iii)[2];
//                        Log.d(TAG, "find salary at date.get("+iii+")");
                        break;
                    }
                    if(Integer.parseInt(staff.get(iii)[1]) > date.get(ii)){
//                        Log.d(TAG, "cannot find salary after "+staff.get(iii)[1]+" ("+iii+")");
                        break;
                    }
                }
//                Log.d(TAG, month+"/"+date.get(ii)+" salary: "+salary);
                TextView salaryText = newTextView(salary);
                tr.addView(salaryText);
                tr.addView(newRowDividerView());
//                Log.d(TAG, i+". tr.childCount: "+tr.getChildCount());
            }
            salaryTable.addView(tr);
            salaryTable.addView(newDividerView());
//            Log.d(TAG, "table.childCount: "+salaryTable.getChildCount());
        }
        scrollView.addView(salaryTable);
        fillableArea.addView(scrollView);
        scrollManager.addScrollClient(scrollView);
    }

    private TextView newTextView(String text){
        int dp4 = dip2px(context, 4);
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setWidth(DATA_WIDTH);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(0, dp4, 0, dp4);
        textView.setTextSize(14);
        return textView;
    }

    private View newRowDividerView(){
        int dp1 = dip2px(context, 1);
        View view = new View(context);
        TableRow.LayoutParams params = new TableRow.LayoutParams(dp1 , TableRow.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        view.setBackgroundColor(Color.parseColor("#000000"));
        return view;
    }

    private View newDividerView(){
        int dp1 = dip2px(context, 1);
        View view = new View(context);
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT , dp1);
        view.setLayoutParams(params);
        view.setBackgroundColor(Color.parseColor("#D3D3D3"));
        return view;
    }

    private int getSumSalary(ArrayList<String[]> staff){
        int sum = 0;
        for(int i = 0; i < staff.size(); i++){
            int salary = Integer.parseInt(staff.get(i)[2]);
            sum = sum + salary;
        }
        return sum;
    }

    private void globalNav(){
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        valuation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent valuation_intent = new Intent(Bonus_List_Detail.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Bonus_List_Detail.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Bonus_List_Detail.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Bonus_List_Detail.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Bonus_List_Detail.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
