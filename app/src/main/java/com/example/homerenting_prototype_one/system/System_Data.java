package com.example.homerenting_prototype_one.system;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.helper.RecyclerViewAction;
import com.example.homerenting_prototype_one.adapter.TextAdapter;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.setting.Setting;
import com.example.homerenting_prototype_one.valuation.Valuation;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;

public class System_Data extends AppCompatActivity {

//    public String[] employees = {"王小明","陳聰明","劉光明","吳輝明","邱朝明","葉大明","劉案全",
//            "王守興","彭玉唱","徐將義","劉曹可","秦因文","方子優","古蘭花","朱柯基"};
//    public String[] cars = {"3.5噸平斗車NRT-134","3.5噸平斗車HWE-353","3.5噸平斗車ITE-774","3.5噸平斗車BTU-255","3.5噸箱型車YEU-712",
//            "3.5噸箱型車NKC-456","3.5噸箱型車ZXE-654","7.7噸箱型車RSF-673","7.7噸箱型車ESF-553","7.7噸箱型車KMS-352"};

    ListView employee_list, car_list;
    RecyclerView recyclerList;
    TextView employee_text, car_text;
    Button addEmployee_btn, addCar_btn;
    ImageButton back_btn, valuation_btn, order_btn, calendar_btn, system_btn, setting_btn;

    private List<String> employee_aList, car_aList;
    private ArrayList<String[]> employees, vehicles;
    private ArrayList<String> new_employee;

    TextAdapter e_adapter, c_adapter;


    Context context = this;
    String TAG = "System_Data";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system__data);

        linking();
        init();

        getData();
//        setList();


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
//                employee_list.setVisibility(View.VISIBLE);
//                car_list.setVisibility(View.GONE);
                setEmployeeRList();
//                recyclerList.setAdapter(null);
//                recyclerList.setAdapter(e_adapter);
            }
        });

        car_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                employee_list.setVisibility(View.GONE);
//                car_list.setVisibility(View.VISIBLE);
                setCarRList();
//                recyclerList.setAdapter(null);
//                recyclerList.setAdapter(c_adapter);
            }
        });

//       addButton();

        globalNav();
    }

    private void linking(){
        recyclerList = findViewById(R.id.staff_vehicle_rv_STD);

        back_btn = findViewById(R.id.back_imgBtn);

        employee_text = findViewById(R.id.employee_bar);
        car_text = findViewById(R.id.car_bar);

        addEmployee_btn = findViewById(R.id.add_employee_btn);
        addCar_btn = findViewById(R.id.add_car_btn);

        valuation_btn = findViewById(R.id.valuationBlue_Btn);
        order_btn = findViewById(R.id.order_imgBtn);
        calendar_btn = findViewById(R.id.calendar_imgBtn);
        system_btn = findViewById(R.id.system_imgBtn);
        setting_btn = findViewById(R.id.setting_imgBtn);
    }

    private void init(){
        employees = new ArrayList<>();
        vehicles = new ArrayList<>();
        new_employee = new ArrayList<>();


        for(int i = 1; i <= 20 ; i++){
            String[] row_data = {String.valueOf(i), "name "+i};
            vehicles.add(row_data);
        }

        recyclerList.setLayoutManager(new LinearLayoutManager(context));
        recyclerList.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
    }

    private void getData(){
        String function_name = "staff-vehicle_data";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
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
                //Log.d(TAG,"responseData"+responseData); //顯示資料

                try {
                    //轉換成json格式，array或object
                    final JSONArray responseArr = new JSONArray(responseData);
                    //Log.i(TAG,"responseObj: "+ responseArr);

                    //一筆一筆的取JSONArray中的json資料
                    int i;
                    for (i = 0; i < responseArr.length(); i++) {
                        JSONObject staff = responseArr.getJSONObject(i);
                        if (!staff.has("staff_id")) break;
                        Log.i(TAG, "staff: " + staff);

                        //取欄位資料
                        String staff_id = staff.getString("staff_id");
                        String staff_name = staff.getString("staff_name");
                        String[] row_data = {staff_id, staff_name};
                        employees.add(row_data);
                    }

//                    for (; i < responseArr.length(); i++) {
//                        JSONObject vehicle = responseArr.getJSONObject(i);
//                        if (!vehicle.has("vehicle_id")) break;
//                        Log.i(TAG, "vehicle: " + vehicle);
//
//                        //取欄位資料
//                        String vehicle_id = vehicle.getString("vehicle_id");
//                        String plate_num = vehicle.getString("plate_num");
//                        String[] row_data = {vehicle_id, plate_num};
//                        vehicles.add(row_data);
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Toast onResponse failed because JSON", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                //顯示資訊
                if(!responseData.equals("null")){
                    for(int i=0; i < employees.size(); i++)
                        Log.i(TAG, "employees: "+ Arrays.toString(employees.get(i)));
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setCarRList();
                        setEmployeeRList();
                    }
                });
            }
        });
    }


    private void setEmployeeRList(){
        recyclerList.setAdapter(null);
        e_adapter = new TextAdapter(employees);
        recyclerList.setAdapter(e_adapter);
        ItemTouchHelper ehelper = new ItemTouchHelper(new RecyclerViewAction(context, e_adapter));
        ehelper.attachToRecyclerView(recyclerList);
    }

    private void setCarRList(){
        recyclerList.setAdapter(null);
        c_adapter = new TextAdapter(vehicles);
        recyclerList.setAdapter(c_adapter);
        ItemTouchHelper chelper = new ItemTouchHelper(new RecyclerViewAction(context, c_adapter));
        chelper.attachToRecyclerView(recyclerList);
    }

    private void setList(){
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
    }

    private void addButton(){
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
                                String new_employee_name = employee_edit.getText().toString();
                                employee_aList.add( new_employee_name );
//                                employee_adapter.notifyDataSetChanged();
//                                new_employee.add(new_employee_name);
                                add_employee(new_employee_name);

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
//                                car_adapter.notifyDataSetChanged();
                            }
                        } )
                        .setNegativeButton( "取消",null ).create()
                        .show();
            }
        });
    }

    private void add_employee(String employee_name){
        String function_name = "order_detail";
        String company_id = getCompany_id(this);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("employee_name", employee_name)
                .add("company_id", company_id)
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
                final String responseData = response.body().string();
                Log.d(TAG,"responseData of add_employee: "+responseData); //顯示資料
            }
        });
    }

    private void globalNav(){
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
