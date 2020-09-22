package com.example.homerenting_prototype_one.setting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.system.System;
import com.example.homerenting_prototype_one.valuation.Valuation;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;

public class Setting_Service extends AppCompatActivity {
    ChipGroup serviceArea, serviceItem, specialItem, carType, boxType, wrapItem;
    Button delete_btn;
    ImageButton back_btn;

    ArrayList<String[]> originItems, enableItems, disableItems, deleteItems;

    boolean lock = true, deleteMode = false;
    Context context = this;
    String TAG = "Setting_Service";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting__service);

        originItems = new ArrayList<>();
        enableItems = new ArrayList<>();
        disableItems = new ArrayList<>();
        deleteItems = new ArrayList<>();

        back_btn = findViewById(R.id.back_imgBtn);
        delete_btn = findViewById(R.id.delete_btn_SS);
        serviceArea = findViewById(R.id.serviceArea_CG_SS);
        serviceItem = findViewById(R.id.serviceItem_CG_SS);
        specialItem = findViewById(R.id.specialItem_CG_SS);
        carType = findViewById(R.id.carType_CG_SS);
        boxType = findViewById(R.id.boxType_CG_SS);
        wrapItem = findViewById(R.id.wrapItem_CG_SS);

        setChipGroupTag();

        getCarData();

        initChip(serviceArea);
        initChip(serviceItem);
        initChip(specialItem);
        initChip(boxType);
        initChip(wrapItem);

        setAddChip(serviceItem);
        setAddChip(specialItem);
        setAddChip(boxType);
        setAddChip(wrapItem);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeOriginItem();
                for(int i = 0; i < enableItems.size(); i++){
                    Log.d(TAG, "enable: "+ Arrays.toString(enableItems.get(i)));
                }
                if(enableItems.size() == 0) Log.d(TAG, "enableItems no item");

                for(int i = 0; i < disableItems.size(); i++){
                    Log.d(TAG, "disable: "+ Arrays.toString(disableItems.get(i)));
                }
                if(disableItems.size() == 0) Log.d(TAG, "disableItems no item");

                for(int i = 0; i < deleteItems.size(); i++){
                    Log.d(TAG, "delete: "+ Arrays.toString(deleteItems.get(i)));
                }
                if(deleteItems.size() == 0) Log.d(TAG, "deleteItems no item");

                update_serviceItem();

//                finish();
            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMode = !deleteMode;
                setDeleteMode(serviceItem);
                setDeleteMode(specialItem);
                setDeleteMode(boxType);
                setDeleteMode(wrapItem);
            }
        });

        globalNav();
    }

    private void setChipGroupTag(){
        serviceArea.setTag("服務範圍");
        serviceItem.setTag("服務項目");
        specialItem.setTag("特殊項目");
        carType.setTag("車輛種類");
        boxType.setTag("紙箱種類");
        wrapItem.setTag("包裝材料");
    }

    private void getCarData(){
        String function_name = "vehicle_type_data";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", getCompany_id(context))
                .build();

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/user_data.php")
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
                Log.d(TAG,"responseData of getCarType: "+responseData); //顯示資料

                try {
                    JSONArray responseArr = new JSONArray(responseData);
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject vehicle = responseArr.getJSONObject(i);
                        Log.i(TAG, "vehicle: " + vehicle);

                        String vehicle_weight = vehicle.getString("vehicle_weight");
                        String vehicle_type = vehicle.getString("vehicle_type");
                        String carTypeStr = vehicle_weight+"噸"+vehicle_type;
                        addChip(carType, carTypeStr, false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                while(!lock){
                    Log.d(TAG, "wait for lock..."); //等到chip都新增完畢之後再執行下個程式
                }
                getCheckedData();
            }
        });
    }

    private void getCheckedData(){
        String function_name = "serviceItem_data";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", getCompany_id(context))
                .build();

        //連線要求
        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL + "/user_data.php")
                .post(body)
                .build();

        //連線
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
                Log.i(TAG,"responseData of getData: "+responseData); //顯示資料

                try {
                    JSONArray responseArr = new JSONArray(responseData);
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject serviceItem = responseArr.getJSONObject(i);
                        Log.i(TAG,"serviceItem: "+serviceItem);

                        String service_name = serviceItem.getString("service_name");
                        final String item_name = serviceItem.getString("item_name");
                        String end_time = serviceItem.getString("end_time");

                        final ChipGroup chipGroup = getChipGroup(service_name);
                        if(chipGroup != null){
                            int ii;
                            for(ii = 0; ii < chipGroup.getChildCount(); ii++){
                                final Chip item = (Chip) chipGroup.getChildAt(ii);
                                Log.d(TAG, "get item:"+item.getText().toString()+"("+chipGroup.getTag()+")");
                                if(item.getText().toString().equals(item_name)){
                                    if(!end_time.equals("null")){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                item.setChecked(false);
                                                Log.d(TAG, item_name+" is disable");
                                            }
                                        });
                                        break;
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            item.setChecked(true);
                                            Log.d(TAG, item.getText().toString()+" is checked");
                                        }
                                    });
                                    String[] originalItem = {chipGroup.getTag().toString(), item_name};
                                    originItems.add(originalItem);
                                    break;
                                }
                            }
                            if(ii == chipGroup.getChildCount() && chipGroup != carType){
                                if(end_time.equals("null")){
                                    addChip(chipGroup, item_name, true);
                                    String[] originalItem = {chipGroup.getTag().toString(), item_name};
                                    originItems.add(originalItem);
                                }
                                else addChip(chipGroup, item_name, false);
                            }

                        }
                        else{
                            Log.d(TAG, "chipGroup: "+service_name+" is null");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d(TAG, "OriginItem.size:"+originItems.size());
                for(int i = 0; i < originItems.size(); i++){
                    Log.d(TAG, "originItem: "+ Arrays.toString(originItems.get(i)));
                }
            }
        });
    }

    private ChipGroup getChipGroup(String service_name){
        if(service_name.equals(serviceArea.getTag())) return serviceArea;
        if(service_name.equals(serviceItem.getTag())) return serviceItem;
        if(service_name.equals(specialItem.getTag())) return specialItem;
        if(service_name.equals(carType.getTag())) return carType;
        if(service_name.equals(boxType.getTag())) return boxType;
        if(service_name.equals(wrapItem.getTag())) return wrapItem;
        return null;
    }

    private void initChip(final ChipGroup chipGroup){
        for(int i = 0; i < chipGroup.getChildCount()-1; i++){
            final Chip chip = (Chip) chipGroup.getChildAt(i);
            setChipClick(chipGroup, chip);
        }
    }

    private void setAddChip(final ChipGroup chipGroup){
        Chip addItem = (Chip) chipGroup.getChildAt(chipGroup.getChildCount()-1);
        addItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //輸入欄
                final EditText newItem_edit = new EditText(context);
                //設定margin
                FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = 80;
                params.rightMargin = params.leftMargin;
                newItem_edit.setLayoutParams(params);
                //要放到FrameLayout裡，margin才有用
                FrameLayout container = new FrameLayout(context);
                container.addView(newItem_edit);

                AlertDialog.Builder newItem_dialog = new AlertDialog.Builder(context);
                newItem_dialog.setTitle("新增項目");
                newItem_dialog.setView(container);
                newItem_dialog.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newItem = newItem_edit.getText().toString();
                        addChip(chipGroup, newItem, true);
                    }
                });
                newItem_dialog.setNegativeButton("取消", null);

                newItem_dialog.create().show();
            }
        });
    }

    private Chip addChip(final ChipGroup chipGroup, final String newItem, final boolean checked){
        Log.d(TAG, "add new chip:"+newItem);
        lock = false;
        //新chip
        final Chip chip = new Chip(chipGroup.getContext());
        chip.setId(R.id.added_chip_id);
        chip.setText(newItem);
        if(checked){
            String[] item = {chipGroup.getTag().toString(), newItem};
            enableItems.add(item);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chip.setChecked(checked);

                //先移除 新增+ 的Chip
                Chip addChip = (Chip) chipGroup.getChildAt(chipGroup.getChildCount()-1);
                chipGroup.removeViewAt(chipGroup.getChildCount()-1);

                chipGroup.addView(chip);
                chipGroup.addView(addChip); //把 新增+ 給加回來
                lock = true;
            }
        });

        setChipClick(chipGroup, chip);
        return chip;
    }

    private void setChipClick(final ChipGroup chipGroup, final Chip chip){
        String name = chip.getText().toString();
        final String[] item = {chipGroup.getTag().toString(), name};
        chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    enableItems.add(item);
                    removeItem(disableItems, item);
                }
                else{
                    removeItem(enableItems, item);
                    disableItems.add(item);
                }
            }
        });

        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder deleteItem_dialog = new AlertDialog.Builder(context);
                deleteItem_dialog.setTitle("確定要刪除 "+chip.getText().toString()+"?");
                deleteItem_dialog.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeItem(enableItems, item);
                        removeItem(disableItems, item);
                        deleteItems.add(item);
                        chip.setVisibility(View.GONE);
                    }
                });
                deleteItem_dialog.setNegativeButton("取消", null);
                deleteItem_dialog.create().show();
            }
        });
    }

    private void setDeleteMode(ChipGroup chipGroup){
        for(int i = 0; i < chipGroup.getChildCount()-1; i++){
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if(chip.getId() == (R.id.added_chip_id)) chip.setCloseIconVisible(deleteMode);
        }
    }

    private void update_serviceItem(){
        String function_name = "modify_serviceItems";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", getCompany_id(context))
                .add("enableItems", itemsToString(enableItems))
                .add("disableItems", itemsToString(disableItems))
                .add("deleteItems", itemsToString(deleteItems))
                .build();

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/functional.php")
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
                Log.d(TAG,"responseData of deleteItem: "+responseData); //顯示資料

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(context, "response!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void removeItem(ArrayList<String[]> items, String[] item){
        for(int i = 0; i < items.size(); i++){
            if(items.get(i)[1].equals(item[1])){
                items.remove(i);
                break;
            }
        }
    }

    private void removeOriginItem(){
        Log.d(TAG, "remove origin items");
        for(int i = 0; i < originItems.size(); i++){
            Log.d(TAG, "originItems: "+ Arrays.toString(originItems.get(i)));
            removeItem(enableItems, originItems.get(i));
        }
    }

    private String itemsToString(ArrayList<String[]> items){
        if(items.size() == 0) return "";

        String itemStr;
        itemStr = "[";
        itemStr = itemStr+"[\""+items.get(0)[0] +"\", \""+items.get(0)[1]+"\"]";
        for(int i = 1; i < items.size(); i++){
            itemStr = itemStr+", [\""+items.get(i)[0] +"\", \""+items.get(i)[1]+"\"]";
        }
        itemStr = itemStr+"]";

        return itemStr;
    }

    private void globalNav(){ //底下nav
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

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
    }

    @Override
    public void onBackPressed() {
        removeOriginItem();
        for(int i = 0; i < enableItems.size(); i++){
            Log.d(TAG, "enable: "+ Arrays.toString(enableItems.get(i)));
        }
        if(enableItems.size() == 0) Log.d(TAG, "enableItems no item");

        for(int i = 0; i < disableItems.size(); i++){
            Log.d(TAG, "disable: "+ Arrays.toString(disableItems.get(i)));
        }
        if(disableItems.size() == 0) Log.d(TAG, "disableItems no item");

        for(int i = 0; i < deleteItems.size(); i++){
            Log.d(TAG, "delete: "+ Arrays.toString(deleteItems.get(i)));
        }
        if(deleteItems.size() == 0) Log.d(TAG, "deleteItems no item");

        update_serviceItem();
        super.onBackPressed();
    }
}
