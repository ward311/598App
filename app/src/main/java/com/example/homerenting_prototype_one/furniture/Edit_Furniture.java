package com.example.homerenting_prototype_one.furniture;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.adapter.base_adapter.FurnitureAdapter;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.add_order.Add_Order;
import com.example.homerenting_prototype_one.order.Order_Today;
import com.example.homerenting_prototype_one.order.Today_Detail;
import com.example.homerenting_prototype_one.setting.Setting;
import com.example.homerenting_prototype_one.system.System;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.valuation.Valuation;
import com.example.homerenting_prototype_one.valuation.ValuationBooking_Detail;

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


public class Edit_Furniture extends AppCompatActivity {
    private ListView list;
    Button add_btn, check_btn;
    Spinner furnitureSpaceSpr, spaceSpr, furnitureSpr;

    View view;
    ProgressDialog dialog2;

    private final String PHP = "/furniture.php";
    String TAG = "Edit_Furniture";
    String order_id, fspace;
    String duration, distance, mvfopt, mvtopt;
    String[] space, furniture;
    String[] new_furniture = new String[3];
    String suggestCar;
    String totalPrice;

    int[][] furniture_data;
    int nowSpace;

    ArrayList<String[]> data;
    ArrayList<ArrayList<String[]>> space_data;
    ArrayList<String> spaceAL, furnitureAL, furnitureIDs, zeroFurniture;

    FurnitureAdapter adapter;

    Bundle bundle;
    Bundle fromBooking;
    Context context = Edit_Furniture.this;
    public static final int FUNC_ADDORDER = 1;
    boolean newFurnitureLock = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__furniture);
        check_btn = findViewById(R.id.check_furniture_btn);
        add_btn = findViewById(R.id.add_furniture_btn);

        data = new ArrayList<>();
        space_data = new ArrayList<>();
        spaceAL = new ArrayList<>();
        furnitureAL = new ArrayList<>();
        furnitureIDs = new ArrayList<>();
        zeroFurniture = new ArrayList<>();
        list = findViewById(R.id.furniture_listView);

//        bundle = new Bundle();
//        bundle.putString("order_id", "16");
        if(getIntent().hasExtra("end")){
            Intent intent = new Intent();
            intent.setClass(context, Calendar.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        bundle = getIntent().getExtras();

        getBundleFromBooking();

        showBundleData();
        order_id = bundle.getString("order_id");
//        order_id = "242";
        Log.i(TAG, "order_id: "+order_id);
        Log.d(TAG, "fromOrder: "+bundle.getBoolean("fromOrder"));
        Log.d(TAG, "isWeb: "+bundle.getString("isWeb"));
        fspace = "all";
        nowSpace = 0;

        setSpaceSpr();

        if(order_id.equals("-1")) {
            String furniture_data_str = bundle.getString("furniture_data");
            if(furniture_data_str == null) furniture_data_str = "null";
            getListData(furniture_data_str);
            setFurnitureList();
            setCheck_btn();
        }
        else if(bundle.getString("isWeb").equals("1")){
            get_WebFurnitureData();
        }
        else{
            getFurnitureData();
        }
        add_btn.setOnClickListener(v -> {
            setSpinner();
            newFurnitureLock = true;

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("????????????");
            builder.setMessage("????????????????????????");
            builder.setView(view);
            builder.setPositiveButton("??????", (dialog, which) -> {
                int i = 0;
                /*while (newFurnitureLock){
                    if(i++%10000000 == 0) Log.d(TAG, "waiting for new furniture lock...");
                }*/
                if(isNew(new_furniture[0])){
                    String[] row_data = {new_furniture[0], new_furniture[1], "0", "1", spaceAL.get(Integer.parseInt(new_furniture[2])+1), getCompany_id(context)};
                    Log.d(TAG, "add new furniture row_data: "+Arrays.toString(row_data));
                    data.add(row_data);
                    space_data.get(Integer.parseInt(new_furniture[2])).add(row_data);
                    if(nowSpace != 0){
                        nowSpace = Integer.parseInt(new_furniture[2])+1;
                        furnitureSpaceSpr.setSelection(nowSpace);
                    }
                        setList();
                }else{
                    runOnUiThread(() -> Toast.makeText(context,"???????????????",Toast.LENGTH_LONG).show());

                }
            });
            builder.setNegativeButton("??????", (dialog, which) -> { newFurnitureLock = false; });
            AlertDialog dialog = builder.create();
            dialog.show();
        });


        globalNav();
    }
    private void getBundleFromBooking(){
        fromBooking = getIntent().getExtras();
        duration = fromBooking.getString("estimate_time");
        distance = fromBooking.getString("estimate_distance");
        mvfopt = fromBooking.getString("mvfopt");
        mvtopt = fromBooking.getString("mvtopt");
        Log.d(TAG, "fromBooking: "+fromBooking.getString("clickFromBooking")+
                " estimate_dis: "+fromBooking.getString("estimate_distance")+
                " estimate_time: "+fromBooking.getString("estimate_time")+
                " mvfopt: "+fromBooking.getString("mvfopt")+
                " mvtopt: "+fromBooking.getString("mvtopt"));
    }
    private void get_WebFurnitureData(){
        String function_name = "furniture_fine_detail";
        String company_id = getCompany_id(this);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .add("company_id", company_id)
                .build();
        Log.d(TAG, "order_id: "+order_id);

        //????????????
        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/furniture.php")
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //??????????????????
                //???app???????????????????????????
                runOnUiThread(() -> Toast.makeText(Edit_Furniture.this, "Toast onFailure.", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG,"responseData of furniture_list: "+responseData);


                getListData(responseData);
                setFurnitureList();
                setCheck_btn();
            }
        });
    }
    private void getFurnitureData(){
        String function_name = "furniture_detail";
        String company_id = getCompany_id(this);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .add("company_id", company_id)
                .build();
        Log.d(TAG, "order_id: "+order_id);

        //????????????
        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/furniture.php")
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //??????????????????
                //???app???????????????????????????
                runOnUiThread(() -> Toast.makeText(Edit_Furniture.this, "Toast onFailure.", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG,"responseData of furniture_list: "+responseData);


                getListData(responseData);
                setFurnitureList();
                setCheck_btn();
            }
        });
    }

    private void getListData(String responseData){
        try {
            JSONArray responseArr = new JSONArray(responseData);

            ArrayList<String[]> livingRoom_data = new ArrayList<>();
            ArrayList<String[]> outside_data = new ArrayList<>();
            ArrayList<String[]> studyRoom_data = new ArrayList<>();
            ArrayList<String[]> bedRoom_data = new ArrayList<>();
            ArrayList<String[]> diningRoom_data = new ArrayList<>();

            //????????????
            for(int i = 0 ; i < responseArr.length() ; i++) {
                JSONObject furniture = responseArr.getJSONObject(i);

                String furniture_id = furniture.getString("furniture_id");
                String name = furniture.getString("furniture_name");
                String space_type = furniture.getString("space_type");
                String furniture_company = furniture.getString("company_id");
                String num = furniture.getString("num");
                if(num.equals("0")) continue;
                ArrayList<String[]> list = null;
                switch(space_type){
                    case "??????":
                        list = livingRoom_data;
                        break;
                    case "????????????":
                        list = outside_data;
                        break;
                    case "??????":
                        list = studyRoom_data;
                        break;
                    case "??????":
                        list = bedRoom_data;
                        break;
                    case "??????":
                        list = diningRoom_data;
                        break;
                    default:
                        Log.d(TAG, name+" no space type");
                }

                //??????id, ????????????, ????????????, ????????????, ????????????, ??????id
                String[] row_data = {furniture_id, name, num, "-1", space_type, furniture_company};
                if(data.size() > 0 && data.get(data.size()-1)[0].equals(furniture_id)) {
                    data.get(data.size()-1)[2] = num;
                    if(list != null) list.get(list.size()-1)[2] = num;
                    Log.i(TAG, "data.get: "+Arrays.toString(data.get(data.size()-1)));
                }
                else {
                    if(!furniture_company.equals("999")) { //?????????????????????id(?????????)
                        row_data[2] = num;
                        row_data[3] = num;
                    }
                    data.add(row_data);
                    Log.i(TAG, "data.add: "+Arrays.toString(row_data));
                    if(list != null) list.add(row_data);
                }
//                Log.d(TAG, "furniture_list_item: "+Arrays.toString(data.get(data.size()-1)));
            }
            space_data.add(0, livingRoom_data);
            space_data.add(1, outside_data);
            space_data.add(2, studyRoom_data);
            space_data.add(3, bedRoom_data);
            space_data.add(4, diningRoom_data);
        } catch (JSONException e) {
            if(!responseData.equals("null")) e.printStackTrace();
            for(int i = 0; i < 5; i++){
                space_data.add(new ArrayList<>());
            }
        }
    }

    private void setFurnitureList(){
        Log.d(TAG, "data.size(): "+data.size());
        for(int i=0; i < data.size(); i++)
            Log.i(TAG, "data: "+ Arrays.toString(data.get(i)));
        adapter = new FurnitureAdapter(data, fspace);
        adapter.setOrder_id(order_id);

        runOnUiThread(() -> list.setAdapter(adapter));
    }

    private void setCheck_btn(){
        check_btn.setOnClickListener(v -> {
            if(datalist()){
                if(order_id.equals("-1")) orderFurniture();
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("????????????");
                    String message = "?????????????????????0???";
                    int i;
                    for(i = 0; i < zeroFurniture.size(); i++){
                        message = message+"\n"+zeroFurniture.get(i);
                        if(i!=(zeroFurniture.size()-1)) message = message+", ";
                        else message = message+"??????????????????????????????";
                    }
                    builder.setMessage(message);
                    builder.setPositiveButton("??????", (dialog, which) -> {
                            if(fromBooking.getString("clickFromBooking").equals("1")){
                                calculateFurnitureAPI();
                                modify_webFurniture();
                            }else if(bundle.getBoolean("fromOrder")){
                                Intent today = new Intent(this, Today_Detail.class);
                                Handler handler = new Handler();
                                if(bundle.getString("isWeb").equals("1")){
                                    today.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    today.putExtras(bundle);
                                    modify_webFurniture();
                                }else{
                                    today.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    today.putExtras(bundle);
                                    modifyFurniture();
                                }
                                handler.postDelayed(() -> startActivity(today),2000);
                            }else{
                                modifyFurniture();
                                toValuationBookingDetail();

                            }

                            //toValuationBookingDetail();
                    });
                    builder.setNegativeButton("??????", (dialog, which) -> { });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
            else{
                if(order_id.equals("-1")) orderFurniture();
                else {
                    if(fromBooking.getString("clickFromBooking").equals("1")){
                            calculateFurnitureAPI();
                            modify_webFurniture();
                            //toValuationBookingDetail();
                    }else if(bundle.containsKey("fromOrder")){
                        Handler handler = new Handler();
                        Intent today = new Intent(this, Today_Detail.class);
                        if(bundle.getString("isWeb").equals("1")){
                            today.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            today.putExtras(bundle);
                            modify_webFurniture();
                        }else{
                            today.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            today.putExtras(bundle);
                            modifyFurniture();
                        }
                        handler.postDelayed(() -> startActivity(today),1500);
                    }else{
                        modifyFurniture();
                        toValuationBookingDetail();
                    }
                }
            }
        });
    }

    private void toValuationBookingDetail() {
        bundle.putBoolean("isEdited", isEdited());
        Log.d(TAG, "isEdited: "+isEdited());
            Intent intent = new Intent();
            intent.putExtras(bundle);
            intent.putExtras(fromBooking);
            intent.setClass(context, ValuationBooking_Detail.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
    }

    private void setSpaceSpr(){
        furnitureSpaceSpr = findViewById(R.id.furnitureSpace_sp_EF);
        if(spaceAL.isEmpty()) getSpace(1);
        else setSpace(1);

        furnitureSpaceSpr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) fspace = "all";
                else fspace = space[position];
                nowSpace = position;
//                Toast.makeText(context, "??????"+fspace, Toast.LENGTH_SHORT).show();
                setList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void setList(){
        final FurnitureAdapter spaceAdapter;
        list.setAdapter(null);
        if(nowSpace == 0) spaceAdapter = new FurnitureAdapter(data, fspace);
        else spaceAdapter = new FurnitureAdapter(space_data.get(nowSpace-1), fspace);
        runOnUiThread(() -> list.setAdapter(spaceAdapter));
    }

    private void setSpinner(){
        LayoutInflater inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.add_furniture, null);
        spaceSpr = view.findViewById(R.id.spaceType_sp_AF);
        furnitureSpr = view.findViewById(R.id.furniture_sp_AF);

        if(spaceAL.isEmpty()) getSpace(2);
        else setSpace(2);
        initFSpinner();

        spaceSpr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    dialog2 = ProgressDialog.show(context, "", "Loading. Please wait...", true);
                    new_furniture[2] = String.valueOf(position-1);
                    Log.d(TAG, "space: "+space.length);
                    getFurniture(space[position]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        furnitureSpr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                if(furnitureIDs.size() > 0){
                    Log.d(TAG, "position: "+position);
                    showToast(furniture, position);
                    //Toast.makeText(context, "??????"+furniture[position], Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "furnitureIDs.size: "+furnitureIDs.size());
                    Log.d(TAG, "furnitureIDs: "+furnitureIDs.get(0));
                    new_furniture[0]=furnitureIDs.get(position);
                    new_furniture[1]=furniture[position];
                    newFurnitureLock = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }
    private Toast mToastToShow;
    public void showToast(String[]array, final int position) {
        // Set the toast and duration
        int toastDurationInMilliSeconds = 1000;
        mToastToShow = Toast.makeText(this,"??????"+array[position] , Toast.LENGTH_SHORT);

        // Set the countdown to display the toast
        CountDownTimer toastCountDown;
        toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 1000 /*Tick duration*/) {
            public void onTick(long millisUntilFinished) {
                mToastToShow.show();
            }
            public void onFinish() {
                mToastToShow.cancel();
            }
        };

        // Show the toast and starts the countdown
        mToastToShow.show();
        toastCountDown.start();
    }

    private void getSpace(final int choose){
        if(choose == 1) spaceAL.add("??????");
        else spaceAL.add("????????????????????????");

        String function_name = "all_space";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .build();
        Log.d(TAG, "all space");

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL + "/furniture.php")
                .post(body)
                .build();


        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //??????????????????
                runOnUiThread(() -> Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, "responseData of space: "+responseData); //????????????

                try {
                    JSONArray responseArr = new JSONArray(responseData);
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject spaceJO = responseArr.getJSONObject(i);
//                        Log.d(TAG, "spaceJO:" + spaceJO);

                        String space_type = spaceJO.getString("space_type");
                        spaceAL.add(space_type);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                space = new String[spaceAL.size()];
                space = spaceAL.toArray(space);
                setSpace(choose);
            }
        });
    }



    private void setSpace(int choose){
        final ArrayAdapter<String> spaceList = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, space);
        if(choose==1){
            runOnUiThread(() -> furnitureSpaceSpr.setAdapter(spaceList));
            spaceAL.clear();
        }
        if(choose==2){
            runOnUiThread(() -> spaceSpr.setAdapter(spaceList));
        }
    }

    private void getFurniture(String space_type){
        furnitureAL.clear();
        furnitureIDs.clear();
        String function_name = "furniture_space";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("space_type", space_type)
                .build();
        Log.d(TAG, "space_type: "+space_type);

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL + "/furniture.php")
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //??????????????????
                runOnUiThread(() -> Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
//                Log.d(TAG, "responseData of furniture: "+responseData); //????????????

                try {
                    JSONArray responseArr = new JSONArray(responseData);
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject furnitureJO = responseArr.getJSONObject(i);
//                        Log.d(TAG, "furnitureJO:" + furnitureJO);

                        String furniture_name = furnitureJO.getString("furniture_name");
                        String furniture_id = furnitureJO.getString("furniture_id");
                        furnitureAL.add(furniture_name);
                        furnitureIDs.add(furniture_id);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                furniture = new String[furnitureAL.size()];
                furniture = furnitureAL.toArray(furniture);
                Log.i(TAG, "furniture: "+Arrays.toString(furniture));
                runOnUiThread(() -> {
                    ArrayAdapter<String> furnitureList = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, furniture);
                    furnitureSpr.setAdapter(null);
                    furnitureSpr.setAdapter(furnitureList);
                });
            }
        });

        dialog2.dismiss();
    }

    private void initFSpinner(){
        final String[] init = {"-------"};
        runOnUiThread(() -> {
            ArrayAdapter<String> initAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, init);
            furnitureSpr.setAdapter(initAdapter);
        });
    }

    private boolean isNew(String new_furniture){
        int i;
        for(i=0; i<data.size(); i++){
            if(data.get(i)[0].equals(new_furniture))
                return false;
        }
        return true;
    }

    private boolean datalist(){
        boolean checkZero = false;
        zeroFurniture.clear();
        //???????????????ArrayList
        ArrayList<int[]> fd = new ArrayList<>();
        for(int i = 0 ; i < adapter.getCount() ; i++){
            String[] row_data = (String[])adapter.getItem(i);
            int[] row_data2 = {Integer.parseInt(row_data[0]), Integer.parseInt(row_data[3])};
            if(row_data[3].equals("-1")) row_data2[1] =  Integer.parseInt(row_data[2]);
            if(row_data2[1]==0){
                zeroFurniture.add(row_data[1]);
                checkZero = true;
            }
            fd.add(row_data2);
        }

        //???ArrayList???????????????????????????
        furniture_data = new int[fd.size()][2];
        for(int i = 0; i < fd.size(); i++)
            for(int ii = 0; ii < 2; ii++)
                furniture_data[i][ii] = fd.get(i)[ii];

        return checkZero;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FUNC_ADDORDER){
            if(resultCode == RESULT_OK){
                bundle = data.getExtras();
            }
        }
    }
    private void orderFurniture(){

        showBundleData();
        bundle.putString("furniture_data", Arrays.deepToString(furniture_data));
        Log.d(TAG, "furniture_data of add_order: "+ Arrays.deepToString(furniture_data));
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(context, Add_Order.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        //intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivityForResult(intent, FUNC_ADDORDER);
    }

    private void showBundleData(){

        if(bundle.containsKey("name")) Log.d(TAG, "name: "+bundle.getString("name"));
        if(bundle.containsKey("gender")) Log.d(TAG, "gender: "+bundle.getInt("gender"));
        if(bundle.containsKey("cCity")) Log.d(TAG, "cCity: "+bundle.getString("cCity"));
        if(bundle.containsKey("cDistrict")) Log.d(TAG, "cDistrict: "+bundle.getString("cDistrict"));
        if(bundle.containsKey("cAddress")) Log.d(TAG, "cAddress: "+bundle.getString("cAddress"));
        if(bundle.containsKey("phone")) Log.d(TAG, "phone: "+bundle.getString("phone"));
        if(bundle.containsKey("outCity")) Log.d(TAG, "outCity: "+bundle.getString("outCity"));
        if(bundle.containsKey("outDistrict")) Log.d(TAG, "outDistrict: "+bundle.getString("outDistrict"));
        if(bundle.containsKey("outAddress")) Log.d(TAG, "outAddress: "+bundle.getString("outAddress"));
        if(bundle.containsKey("inCity")) Log.d(TAG, "inCity: "+bundle.getString("inCity"));
        if(bundle.containsKey("inDistrict")) Log.d(TAG, "inDistrict: "+bundle.getString("inDistrict"));
        if(bundle.containsKey("inAddress")) Log.d(TAG, "inAddress: "+bundle.getString("inAddress"));
        if(bundle.containsKey("pickDate")) Log.d(TAG, "pickDate: "+bundle.getString("pickDate"));
        if(bundle.containsKey("pickTime")) Log.d(TAG, "pickTime: "+bundle.getString("pickTime"));
        if(bundle.containsKey("price")) Log.d(TAG, "price: "+bundle.getString("price"));
        if(bundle.containsKey("worktime")) Log.d(TAG, "worktime: "+bundle.getString("worktime"));
        if(bundle.containsKey("notice")) Log.d(TAG, "notice: "+bundle.getString("notice"));



    }
    private void calculateFurnitureAPI(){ /*????????????????????????API*/
        String function_name = "calculate_furniture";
        String company_id = getCompany_id(context);
        RequestBody body = new FormBody.Builder()//?????????
                .add("function_name", function_name)
                .add("duration", duration)
                .add("distance", distance)
                .add("mvfopt", mvfopt)
                .add("mvtopt", mvtopt)
                .add("furniture_data", Arrays.deepToString(furniture_data))
                .build();
        Log.d(TAG,"duration:"+ duration+" distance: "+
                distance +" movefrom: "+mvfopt+" moveto: "+mvtopt+
                " furniture_dataToCalculate:"+ Arrays.deepToString(furniture_data));

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/furniture.php")//?????????
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG, "responseData of calculate_furniture: " + responseData);
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONObject data = jsonObject.getJSONObject("data");
                    String data_status = jsonObject.getString("status");
                    if(!data_status.equals("200")){
                        runOnUiThread(()->{
                            Intent intent = new Intent();
                            intent.putExtras(bundle);
                            intent.putExtras(fromBooking);
                            intent.setClass(context, ValuationBooking_Detail.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                        });
                    }else{
                        suggestCar = data.getString("sugCars");
                        totalPrice = data.getString("totalPrice");
                        Log.d(TAG, "api sugcars: "+suggestCar+", Price: "+totalPrice);
                        runOnUiThread(()->{
                            fromBooking.putString("suggestCars", suggestCar);
                            fromBooking.putString("suggestPrice", totalPrice);
                            if(!suggestCar.equals(null)&&!totalPrice.equals(null))
                                toValuationBookingDetail();
                        });
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    private void modify_webFurniture(){
        String function_name = "modify_web_furniture";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .add("company_id", getCompany_id(context))
                .add("furniture_data", Arrays.deepToString(furniture_data))
                .build();
        Log.d(TAG,"order_id: "+order_id+
                ", company_id: "+getCompany_id(context)+
                ", furniture_data:"+ Arrays.deepToString(furniture_data));

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+PHP)
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG, "responseData of modify_web_furniture: " + responseData);

            }
        });
    }
    private void modifyFurniture(){
        String function_name = "modify_furniture";
        String company_id = getCompany_id(context);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .add("company_id",company_id)
                .add("furniture_data", Arrays.deepToString(furniture_data))
                .build();
        Log.d(TAG,"order_id: "+order_id+", furniture_data:"+ Arrays.deepToString(furniture_data));

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+PHP)
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG, "responseData of modify_furniture: " + responseData);

            }
        });
    }

    private boolean isEdited() {
        for(int i = 0; i < data.size(); i++)
            if(!data.get(i)[5].equals("999")) return true;
        return false;
    }

    private void globalNav(){
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        valuation_btn.setOnClickListener(v -> {
            Intent valuation_intent = new Intent(Edit_Furniture.this, Valuation.class);
            startActivity(valuation_intent);
        });
        order_btn.setOnClickListener(v -> {
            Intent order_intent = new Intent(Edit_Furniture.this, Order.class);
            startActivity(order_intent);
        });
        calendar_btn.setOnClickListener(v -> {
            Intent calender_intent = new Intent(Edit_Furniture.this, Calendar.class);
            startActivity(calender_intent);
        });
        system_btn.setOnClickListener(v -> {
            Intent system_intent = new Intent(Edit_Furniture.this, System.class);
            startActivity(system_intent);
        });
        setting_btn.setOnClickListener(v -> {
            Intent setting_intent = new Intent(Edit_Furniture.this, Setting.class);

        });
    }
    public void onBackPressed(){

    }
}