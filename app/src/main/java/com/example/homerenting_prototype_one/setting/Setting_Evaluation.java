package com.example.homerenting_prototype_one.setting;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.adapter.re_adpater.CommentAdapter;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.helper.DatabaseHelper;
import com.example.homerenting_prototype_one.model.TableContract;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.system.System;
import com.example.homerenting_prototype_one.valuation.Valuation;
import com.google.android.material.tabs.TabLayout;


import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.homerenting_prototype_one.show.global_function.addDatalist;
import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;

public class Setting_Evaluation extends AppCompatActivity {
    TextView commentCount, allStars;
    ImageButton back_btn;
    RecyclerView commentList;

    ArrayList<String[]> data;
    ArrayList<Double> stars;
    ArrayList<String[]> comments;

    int commentcount = 0;
    boolean lock = false;

    CommentAdapter commentAdapter;

    Context context = this;
    String TAG = "Setting_Evaluation";
    private static DatabaseHelper dbHelper;
    private static SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting__evaluation);
        back_btn = findViewById(R.id.back_btn_SE);
        commentCount = findViewById(R.id.commentCount_ED);
        allStars = findViewById(R.id.allStar_ED);
        commentList = findViewById(R.id.comment_list_SE);
        data = new ArrayList<>();
        stars = new ArrayList<>();
        comments = new ArrayList<>();

        //getData();

        LinearLayout first_evaluation = findViewById(R.id.first_evaluation_layout);
        first_evaluation.setOnClickListener(v -> {
            Intent evaluation_detail = new Intent(Setting_Evaluation.this, Evaluation_Detail.class);
            startActivity(evaluation_detail);
        });

        back_btn.setOnClickListener(v -> finish());
        dbHelper = new DatabaseHelper(this);

        readData();
        globalNav();
    }

    private void readData(){
        db = dbHelper.getReadableDatabase();
        String[] projection = {
                TableContract.CommentsTable.COLUMN_NAME_COMMENT_ID,
                TableContract.CommentsTable.COLUMN_NAME_ORDER_ID,
                TableContract.CommentsTable.COLUMN_NAME_MEMBER_ID,
                TableContract.CommentsTable.COLUMN_NAME_COMPANY_ID,
                TableContract.CommentsTable.COLUMN_NAME_COMMENT_DATE,
                TableContract.CommentsTable.COLUMN_NAME_SERVICE_QUALITY,
                TableContract.CommentsTable.COLUMN_NAME_WORK_ATTITUDE,
                TableContract.CommentsTable.COLUMN_NAME_PRICE_GRADE,
                TableContract.CommentsTable.COLUMN_NAME_COMMENT,
                TableContract.CommentsTable.COLUMN_NAME_REPLY
        };
        String selection = TableContract.CommentsTable.COLUMN_NAME_COMPANY_ID+" = ?";
        String[] selectionArgs = {"1"};

        Cursor cursor = db.query(
                TableContract.CommentsTable.TABLE_NAME,// FROM TABLE_NAME
                projection,//SELECT *
                selection,//WHERE COMPANY_ID
                selectionArgs,//=1
                null,
                null,
                null
        );

        Log.d(TAG,"cursor count:"+cursor.getCount());//GET result from database
        if(cursor.moveToFirst()){
            while(cursor.moveToNext()){
                String[] item = {
                        cursor.getString(cursor.getColumnIndexOrThrow(TableContract.CommentsTable.COLUMN_NAME_COMMENT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TableContract.CommentsTable.COLUMN_NAME_ORDER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TableContract.CommentsTable.COLUMN_NAME_MEMBER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TableContract.CommentsTable.COLUMN_NAME_COMPANY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TableContract.CommentsTable.COLUMN_NAME_COMMENT_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TableContract.CommentsTable.COLUMN_NAME_SERVICE_QUALITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TableContract.CommentsTable.COLUMN_NAME_WORK_ATTITUDE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TableContract.CommentsTable.COLUMN_NAME_PRICE_GRADE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TableContract.CommentsTable.COLUMN_NAME_COMMENT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TableContract.CommentsTable.COLUMN_NAME_REPLY)),
                };
                comments.add(item);
                Log.d(TAG,"sqlite comment: "+Arrays.toString(item));
            }
        }
            cursor.close();
            getData();
            return;
    }


    private void getData(){
        lock = true;
        String function_name = "comment_data";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", getCompany_id(context))
                .build();

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL + "/user_data.php")
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //顯示錯誤訊息
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //在app畫面上呈現錯誤訊息
                        Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
                Log.i(TAG,"responseData: "+responseData); //顯示資料
                try {
                    JSONArray responseArr = new JSONArray(responseData);
                    commentcount = responseArr.length();
                    db = dbHelper.getWritableDatabase();
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject comment = responseArr.getJSONObject(i);
                        Log.i(TAG, "comment: "+comment);
                        String comment_id = comment.getString("comment_id");
                        String name = comment.getString("member_name");
                        String nameTitle;
                        if(comment.getString("gender").equals("女")) nameTitle = "小姐";
                        else nameTitle = "先生";
                        String date = comment.getString("comment_date");
                        String commentStr = comment.getString("comment");
                        String commentSummary;
                        String reply = comment.getString("reply");
                        if(commentStr.length() > 35) commentSummary = commentStr.substring(0, 30)+"...";
                        else commentSummary = commentStr;

                        double service_star = comment.getDouble("service_quality");
                        double work_star = comment.getDouble("work_attitude");
                        double price_star = comment.getDouble("price_grade");
                        double starf = (service_star+work_star+price_star)/3;
                        starf = (double) Math.round(starf*10)/10;
                        Log.d(TAG, "service("+service_star+")+work("+work_star+")+price("+price_star+")/3 = "+starf);
                        stars.add(service_star);
                        stars.add(work_star);
                        stars.add(price_star);

                        String[] row_data = {comment_id, String.valueOf(starf), name, nameTitle, date, commentSummary};
                        String[] comment_data = {comment_id, String.valueOf(starf), name, nameTitle, date, commentSummary, reply};
                        Log.d(TAG, "row_data: "+Arrays.toString(row_data));
                        Log.d(TAG, "comment_data"+Arrays.toString(comment_data));
                        comments.add(comment_data);
                        data.add(row_data);
                        db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put((TableContract.CommentsTable.COLUMN_NAME_COMMENT_ID), comment_id);
                        values.put((TableContract.CommentsTable.COLUMN_NAME_ORDER_ID), comment.getString(TableContract.CommentsTable.COLUMN_NAME_ORDER_ID));
                        values.put((TableContract.CommentsTable.COLUMN_NAME_MEMBER_ID), comment.getString(TableContract.CommentsTable.COLUMN_NAME_MEMBER_ID));
                        values.put((TableContract.CommentsTable.COLUMN_NAME_COMPANY_ID), getCompany_id(context));
                        values.put((TableContract.CommentsTable.COLUMN_NAME_COMMENT_DATE), date);
                        values.put((TableContract.CommentsTable.COLUMN_NAME_SERVICE_QUALITY), service_star);
                        values.put((TableContract.CommentsTable.COLUMN_NAME_WORK_ATTITUDE), work_star);
                        values.put((TableContract.CommentsTable.COLUMN_NAME_PRICE_GRADE), price_star);
                        values.put((TableContract.CommentsTable.COLUMN_NAME_COMMENT),commentStr);
                        values.put((TableContract.CommentsTable.COLUMN_NAME_REPLY), reply);
                        try{
                            long newRowId = db.insertOrThrow(TableContract.CommentsTable.TABLE_NAME, null, values);
                            if(newRowId != -1) {
                                Log.d(TAG,"create successfully");}
                            else Log.d(TAG, "create failed");
                        }catch (SQLException e){

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                lock = false;
                if(!responseData.equals("null")){
                    for(int i=0; i < data.size(); i++)
                        Log.i(TAG, "data: "+ Arrays.toString(data.get(i)));
                    setRList();
                }
            }
        });
    }

    private void setRList(){
        commentAdapter = new CommentAdapter(context, data);
        runOnUiThread(() -> {
            commentList.setLayoutManager(new LinearLayoutManager(context));
            commentList.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL)); //分隔線
            commentList.setAdapter(commentAdapter);
            setStars();
        });

    }


    private void setStars(){
        int i = 0;
        while (lock){ //等待stars收集好資料
            if((++i)%5000000 == 0) Log.d(TAG, (i/5000000)+". wait for lock...");
        }
        double allstar = 0;
        for(i = 0; i < stars.size(); i++) allstar = allstar+stars.get(i);
        allstar = allstar/stars.size();
        allstar = (double) Math.round(allstar*10)/10;

        final double finalAllStar = allstar;
        allStars.setText("評價 "+finalAllStar);
        commentCount.setText("共"+commentcount+"則評論");
        commentAdapter.setAllStars(allstar);
        commentAdapter.setCommentCount(commentcount);
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
                Intent valuation_intent = new Intent(Setting_Evaluation.this, Valuation.class);
                startActivity(valuation_intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_intent = new Intent(Setting_Evaluation.this, Order.class);
                startActivity(order_intent);
            }
        });
        calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calender_intent = new Intent(Setting_Evaluation.this, Calendar.class);
                startActivity(calender_intent);
            }
        });
        system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent system_intent = new Intent(Setting_Evaluation.this, System.class);
                startActivity(system_intent);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(Setting_Evaluation.this, Setting.class);
                startActivity(setting_intent);
            }
        });
    }
}
