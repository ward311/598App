package com.example.homerenting_prototype_one.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import android.provider.BaseColumns;
import android.sax.EndElementListener;
import android.util.Log;
import android.widget.Toast;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.helper.DatabaseHelper;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;



public class TableContract {
    private static String TAG = TableContract.class.getSimpleName();
    public static DatabaseHelper dbHelper;
    public TableContract() {}

    public static class AreaTable implements BaseColumns{
        public static final String TABLE_NAME = "area";
        public static final String COLUMN_NAME_CITY_NAME = "city_name";
        public static final String COLUMN_NAME_CITY_DISTRICT = "city_district";
        public static final String COLUMN_NAME_POSTAL_CODE = "postal_code";

        public static final String SQL_CREATE_AREA = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_CITY_NAME+" VARCHAR(11) NOT NULL, "+
                COLUMN_NAME_CITY_DISTRICT+" VARCHAR(11) NOT NULL, "+
                COLUMN_NAME_POSTAL_CODE+" VARCHAR(10) NOT NULL, "+
                "PRIMARY KEY (`postal_code`) "+");";
        public static final String SQL_DELETE_AREA =
                "DROP TABLE IF EXISTS "+TABLE_NAME;
    }

    public static class MemberTable implements BaseColumns{
        public static final String TABLE_NAME = "member";
        public static final String COLUMN_NAME_MEMBER_ID = "member_id";
        public static final String COLUMN_NAME_MEMBER_NAME = "member_name";
        public static final String COLUMN_NAME_GENDER = "gender";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_CONTACT_ADDRESS = "contact_address";
        public static final String COLUMN_NAME_CONTACT_WAY = "contact_way";
        public static final String COLUMN_NAME_CONTACT_TIME = "contact_time";

        public static final String SQL_CREATE_MEMBER = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_MEMBER_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_MEMBER_NAME+" NVARCHAR(50) NOT NULL, "+
                COLUMN_NAME_GENDER+" NVARCHAR(10), "+
                COLUMN_NAME_PHONE+" VARCHAR(13), "+
                COLUMN_NAME_CONTACT_ADDRESS+" NVARCHAR(100), "+
                COLUMN_NAME_CONTACT_WAY+" NVARCHAR(100), "+
                COLUMN_NAME_CONTACT_TIME+" NVARCHAR(100), "+
                "PRIMARY KEY (`member_id`) "+
                ");";
        public static final String SQL_DELETE_MEMBER =
                "DROP TABLE IF EXISTS "+TABLE_NAME;
    }

    public static class CompanyTable implements BaseColumns {
        public static final String TABLE_NAME = "company";
        public static final String COLUMN_NAME_COMPANY_ID = "company_id";
        public static final String COLUMN_NAME_COMPANY_NAME = "company_name";
        public static final String COLUMN_NAME_IMG = "img";
        public static final String COLUMN_NAME_ADDRESS = "address";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_STAFF_NUM = "staff_num";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_LINE_ID = "line_id";
        public static final String COLUMN_NAME_PHILOSOPHY = "pilosophy";
        public static final String COLUMN_NAME_LAST_DISTRIBUTION = "last_distribution";

        public static final String SQL_CREATE_COMPANY =
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                        COLUMN_NAME_COMPANY_ID+" INTEGER(10) NOT NULL, "+
                        COLUMN_NAME_COMPANY_NAME+" NVARCHAR(100), "+
                        COLUMN_NAME_IMG+" VARCHAR(99), "+
                        COLUMN_NAME_ADDRESS+" NVARCHAR(50), "+
                        COLUMN_NAME_PHONE+" VARCHAR(13), "+
                        COLUMN_NAME_STAFF_NUM+" INTEGER(4) NOT NULL, "+
                        COLUMN_NAME_URL+" VARCHAR(100), "+
                        COLUMN_NAME_EMAIL+" VARCHAR(100), "+
                        COLUMN_NAME_LINE_ID+" VARCHAR(100), "+
                        COLUMN_NAME_PHILOSOPHY+" NVARCHAR(500), "+
                        COLUMN_NAME_LAST_DISTRIBUTION+" FLOAT DEFAULT 0, "+
                        "PRIMARY KEY(`company_id`)"+
                ");";

        public static final String SQL_DELETE_COMPANY =
                "DROP TABLE IF EXISTS "+ TABLE_NAME;
    }

    public static class OrdersTable implements BaseColumns{
        public static final String TABLE_NAME = "orders";
        public static final String COLUMN_NAME_ORDER_ID = "order_id";
        public static final String COLUMN_NAME_MEMBER_ID = "member_id";
        public static final String COLUMN_NAME_ADDITIONAL = "additional";
        public static final String COLUMN_NAME_MEMO = "memo";
        public static final String COLUMN_NAME_OUT_CITY = "outcity";
        public static final String COLUMN_NAME_OUT_DISTRICT = "outdistrict";
        public static final String COLUMN_NAME_OUT_ADDRESS = "address1";
        public static final String COLUMN_NAME_IN_CITY = "incity";
        public static final String COLUMN_NAME_IN_DISTRICT = "indistrict";
        public static final String COLUMN_NAME_IN_ADDRESS = "address2";
        public static final String COLUMN_NAME_FROM_ELEVATOR = "from_elevator";
        public static final String COLUMN_NAME_TO_ELEVATOR = "to_elevator";
        public static final String COLUMN_NAME_STORAGE_SPACE = "storage_space";
        public static final String COLUMN_NAME_CARTON_NUM = "carton_num";
        public static final String COLUMN_NAME_PROGRAM = "program";
        public static final String COLUMN_NAME_ORDER_STATUS = "order_status";
//        public static final String COLUMN_NAME_NEW = "new";
        public static final String COLUMN_NAME_AUTO = "auto";
        public static final String COLUMN_NAME_LAST_UPDATE = "last_update";
        public static final String COLUMN_NAME_SIGNATURE = "signature";

        public static final String SQL_CREATE_ORDERS = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+ " ( "+
                COLUMN_NAME_ORDER_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_MEMBER_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_ADDITIONAL+" NVARCHAR(300), "+
                COLUMN_NAME_MEMO+" NVARCHAR(300), "+
                COLUMN_NAME_OUT_CITY+" VARCHAR(100), "+
                COLUMN_NAME_OUT_DISTRICT+" VARCHAR(100), "+
                COLUMN_NAME_OUT_ADDRESS+" VARCHAR(100), "+
                COLUMN_NAME_IN_CITY+" VARCHAR(100), "+
                COLUMN_NAME_IN_DISTRICT+" VARCHAR(100), "+
                COLUMN_NAME_IN_ADDRESS+" VARCHAR(100), "+
                COLUMN_NAME_FROM_ELEVATOR+" BOOLEAN, "+
                COLUMN_NAME_TO_ELEVATOR+" BOOLEAN, "+
                COLUMN_NAME_STORAGE_SPACE+" VARCHAR(10), "+
                COLUMN_NAME_CARTON_NUM+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_PROGRAM+" VARCHAR(4), "+
                COLUMN_NAME_ORDER_STATUS+" VARCHAR(11) NOT NULL, "+
//                COLUMN_NAME_NEW+" BOOLEAN DEFAULT TRUE, "+
                COLUMN_NAME_AUTO+" BOOLEAN DEFAULT TRUE, "+
                COLUMN_NAME_LAST_UPDATE+" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "+
                COLUMN_NAME_SIGNATURE+" CLOB, "+
                "PRIMARY KEY (`order_id`), "+
                "FOREIGN KEY (`member_id`) "+
                "REFERENCES member (`member_id`) ON DELETE SET NULL "+
                ");"+"\n"+
                "CREATE TRIGGER last_update_time_trigger "+
                "AFTER UPDATE ON "+TABLE_NAME+" FOR EACH ROW WHEN "+
                "NEW.last_update <= OLD.last_update"+
                "BEGIN \n"+
                "UPDATE "+TABLE_NAME+" SET "+COLUMN_NAME_LAST_UPDATE+
                " = CURRENT_TIMESTAMP WHERE "+COLUMN_NAME_ORDER_ID+" = "+
                "OLD."+COLUMN_NAME_ORDER_ID+";"+
                "\n END";

        public static final String SQL_DELETE_ORDERS =
                "DROP TABLE IF EXISTS "+TABLE_NAME;

    }


    public static class SpecialTable implements BaseColumns{
        public static final String TABLE_NAME = "special";
        public static final String COLUMN_NAME_SPECIAL_ID = "special_id";
        public static final String COLUMN_NAME_ORDER_ID = "order_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_NUM = "num";
        public static final String SQL_CREATE_SPECIAL = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_SPECIAL_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_ORDER_ID+" INTEGER(10), "+
                COLUMN_NAME_NAME+" NVARCHAR(100), "+
                COLUMN_NAME_NUM+" INTEGER(10), "+
                "PRIMARY KEY (`special_id`), "+
                "FOREIGN KEY (`order_id`) "+
                "REFERENCES orders(`order_id`) ON DELETE CASCADE "+");";
        public static final String SQL_DELETE_SPECIAL =
                "DROP TABLE IF EXISTS "+TABLE_NAME;
    }

    public static class ChooseTable implements BaseColumns {
        public static final String TABLE_NAME = "choose";
        public static final String COLUMN_NAME_ORDER_ID = "order_id";
        public static final String COLUMN_NAME_COMPANY_ID = "company_id";
        public static final String COLUMN_NAME_VALUATION_DATE = "valuation_date";
        public static final String COLUMN_NAME_VALUATION_TIME = "valuation_time";
        public static final String COLUMN_NAME_MOVING_DATE = "moving_date";
        public static final String COLUMN_NAME_ESTIMATE_FEE = "estimate_fee";
        public static final String COLUMN_NAME_ACCURATE_FEE = "accurate_fee";
        public static final String COLUMN_NAME_CONFIRM = "confirm";
        public static final String COLUMN_NAME_VALUATION_STATUS = "valuation_status";

        public static final String SQL_CREATE_CHOOSE = "" +
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_ORDER_ID+" int(10) NOT NULL, "+
                COLUMN_NAME_COMPANY_ID+" int(10) NOT NULL, "+
                COLUMN_NAME_VALUATION_DATE+" date, "+
                COLUMN_NAME_VALUATION_TIME+" varchar(99), "+
                COLUMN_NAME_MOVING_DATE+" datetime, "+
                COLUMN_NAME_ESTIMATE_FEE+" int(10), "+
                COLUMN_NAME_ACCURATE_FEE+" int(10), "+
                COLUMN_NAME_CONFIRM+" boolean DEFAULT FALSE, "+
                COLUMN_NAME_VALUATION_STATUS+" enum('self', 'booking', 'match', 'cancel', 'chosen') DEFAULT 'self', "+
                "PRIMARY KEY(order_id, company_id), "+
                "FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE, "+
                "FOREIGN KEY (company_id) REFERENCES company(company_id) ON DELETE CASCADE "+
                ");";

        public static final String SQL_DELETE_CHOOSE =
                "DROP TABLE IF EXISTS "+ TABLE_NAME;
    }

    public static class StaffTable implements BaseColumns{
        public static final String TABLE_NAME = "staff";
        public static final String COLUMN_NAME_STAFF_ID = "staff_id";
        public static final String COLUMN_NAME_STAFF_NAME = "staff_name";
        public static final String COLUMN_NAME_COMPANY_ID = "company_id";
        public static final String COLUMN_NAME_START_TIME= "start_time";
        public static final String COLUMN_NAME_END_TIME = "end_time";
        public static final String SQL_CREATE_STAFF = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_STAFF_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_STAFF_NAME+" VARCHAR(50), "+
                COLUMN_NAME_COMPANY_ID+" INTEGER(10), "+
                COLUMN_NAME_START_TIME+" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "+
                COLUMN_NAME_END_TIME+" DATETIME DEFAULT NULL, "+
                "PRIMARY KEY (`staff_id`), "+
                "FOREIGN KEY (`company_id`) REFERENCES company(`company_id`) ON DELETE CASCADE "+
                ");\n"+
                "CREATE TRIGGER start_time_trigger "+
                "AFTER UPDATE ON "+TABLE_NAME+" FOR EACH ROW WHEN "+
                "NEW.start_time <= OLD.start_time"+
                "BEGIN \n"+
                "UPDATE "+TABLE_NAME+" SET "+COLUMN_NAME_START_TIME+
                " = CURRENT_TIMESTAMP WHERE "+COLUMN_NAME_STAFF_ID+" = "+
                "OLD."+COLUMN_NAME_STAFF_ID+";"+
                "\n END";
        public static final String SQL_DELETE_STAFF =
                "DROP TABLE IF NOT EXISTS "+TABLE_NAME;

        public static void getAllStaffData(DatabaseHelper dbHelper, Context context){
            String function_name = "getAllStaffData";
            String table_name = StaffTable.TABLE_NAME;
            Log.d(TAG, "start "+function_name+" and write in sqlite db");
            RequestBody body = new FormBody.Builder()
                    .add("company_id", getCompany_id(context))
                    .build();

            Request request = new Request.Builder()
                    .url(BuildConfig.SERVER_URL + "/get_data/all_staff_data.php")
                    .post(body)
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseData = response.body().string();
                    Log.d(TAG, "responseData of "+function_name+": "+responseData); //顯示資料

                    JSONArray responseArr;
                    try {
                        responseArr= new JSONArray(responseData);
                    }catch (JSONException e){
                        e.printStackTrace();
                        Log.d(TAG, function_name+": "+e.getMessage());
                        return;
                    }

                    int success_counter = 0, fail_counter = 0;
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject staff;
                        try {
                            staff = responseArr.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return;
                        }
//                    Log.d(TAG, (i+1)+". staff: "+staff);

                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        try {
                            values.put(StaffTable.COLUMN_NAME_STAFF_ID, staff.getString(StaffTable.COLUMN_NAME_STAFF_ID));
                            values.put(StaffTable.COLUMN_NAME_STAFF_NAME, staff.getString(StaffTable.COLUMN_NAME_STAFF_NAME));
                            values.put(StaffTable.COLUMN_NAME_COMPANY_ID, staff.getString(StaffTable.COLUMN_NAME_COMPANY_ID));
                            values.put(StaffTable.COLUMN_NAME_START_TIME, staff.getString(StaffTable.COLUMN_NAME_START_TIME));
                            values.put(StaffTable.COLUMN_NAME_END_TIME, staff.getString(StaffTable.COLUMN_NAME_END_TIME));
//                        Log.d(TAG, (i+1)+". "+values.toString())
                        } catch (JSONException e) {
                            e.printStackTrace();
                            continue;
                        }

                        try{
                            long newRowId = db.insertOrThrow(table_name, null, values);
                            if(newRowId != -1){
                                success_counter = success_counter + 1;
                                Log.d(TAG, "create "+table_name+" successfully");
                            }
                            else{
                                fail_counter = fail_counter + 1;
                                Log.d(TAG, "create "+table_name+" failed");
                            }
                        } catch (SQLException e){
                            if(e.getMessage().contains("no such table")) break;
                            if(Objects.requireNonNull(e.getMessage()).contains("PRIMARYKEY")){
                                String selection = StaffTable.COLUMN_NAME_STAFF_ID+" = ?";
                                String[] seletctionArgs = {values.getAsString(StaffTable.COLUMN_NAME_STAFF_ID)};

                                int count = db.update(
                                        table_name,
                                        values,
                                        selection,
                                        seletctionArgs
                                );
                                if(count != -1) success_counter = success_counter + 1;
                                else fail_counter = fail_counter + 1;
                            }
                            else{
                                e.printStackTrace();
                                Log.d(TAG, "insert "+table_name+" data: "+e.getMessage());
                            }
                        }
                    }
                    Log.d(TAG, table_name+" data:\n success data: "+success_counter+", fail data: "+fail_counter);
                }
            });
        }
    }

    public static class StaffAssignmentTable implements BaseColumns{
        public static final String TABLE_NAME = "staff_assignment";
        public static final String COLUMN_NAME_ORDER_ID = "order_id";
        public static final String COLUMN_NAME_STAFF_ID = "staff_id";
        public static final String COLUMN_NAME_PAY = "pay";
        public static final String SQL_CREATE_STAFF_ASSIGNMENT = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_ORDER_ID+" INTEGER(10), "+
                COLUMN_NAME_STAFF_ID+" INTEGER(10), "+
                COLUMN_NAME_PAY+" INTEGER(10) DEFAULT -1, "+
                "PRIMARY KEY (`order_id`, `staff_id`), "+
                "FOREIGN KEY (`order_id`) "+
                "REFERENCES orders(`order_id`) ON DELETE CASCADE, "+
                "FOREIGN KEY (`staff_id`) "+
                "REFERENCES staff(`staff_id`) ON DELETE CASCADE "+");";
        public static final String SQL_DELETE_STAFF_ASSIGNMENT =
                "DROP TABLE IF EXISTS "+TABLE_NAME;
    }

    public static class VehicleTable implements BaseColumns{
        public static final String TABLE_NAME = "vehicle";
        public static final String COLUMN_NAME_VEHICLE_ID = "vehicle_id";
        public static final String COLUMN_NAME_PLATE_NUM = "plate_num";
        public static final String COLUMN_NAME_VEHICLE_WEIGHT = "vehicle_weight";
        public static final String COLUMN_NAME_VEHICLE_TYPE = "vehicle_type";
        public static final String COLUMN_NAME_COMPANY_ID = "company_id";
        public static final String COLUMN_NAME_START_TIME = "start_time";
        public static final String COLUMN_NAME_END_TIME = "end_time";
        public static final String COLUMN_NAME_VERIFY = "verify";


        public static final String SQL_CREATE_VEHICLE = "" +
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_VEHICLE_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_PLATE_NUM+" VARCHAR(99) NOT NULL, "+
                COLUMN_NAME_VEHICLE_WEIGHT+" VARCHAR(10), "+
                COLUMN_NAME_VEHICLE_TYPE+" VARCHAR(99), "+
                COLUMN_NAME_COMPANY_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_START_TIME+" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "+
                COLUMN_NAME_END_TIME+" DATETIME DEFAULT NULL, "+
                COLUMN_NAME_VERIFY+" BOOLEAN DEFAULT false NOT NULL, "+
                "PRIMARY KEY (`vehicle_id`), "+
                "FOREIGN KEY (`company_id`) "+
                "REFERENCES company(company_id) ON DELETE CASCADE "+
                ");";

        public static final String SQL_DELETE_VEHICLE =
                "DROP TABLE IF EXISTS "+ TABLE_NAME;

        public static void getAllVehicleData(DatabaseHelper dbHelper, Context context){
            String function_name = "getAllVehicleData";
            String table_name = VehicleTable.TABLE_NAME;
            Log.d(TAG, "start "+function_name+" and write in sqlite db");
            RequestBody body = new FormBody.Builder()
                    .add("company_id", getCompany_id(context))
                    .build();

            Request request = new Request.Builder()
                    .url(BuildConfig.SERVER_URL + "/get_data/all_vehicle_data.php")
                    .post(body)
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseData = response.body().string();
                    Log.d(TAG, "responseData of "+function_name+": "+responseData); //顯示資料

                    JSONArray responseArr;
                    try {
                        responseArr= new JSONArray(responseData);
                    }catch (JSONException e){
                        e.printStackTrace();
                        Log.d(TAG, function_name+": "+e.getMessage());
                        return;
                    }

                    int success_counter = 0, fail_counter = 0;
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject vehicle;
                        try {
                            vehicle = responseArr.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return;
                        }
//                    Log.d(TAG, (i+1)+". staff: "+staff);

                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        try {
                            values.put(VehicleTable.COLUMN_NAME_VEHICLE_ID, vehicle.getString(VehicleTable.COLUMN_NAME_VEHICLE_ID));
                            values.put(VehicleTable.COLUMN_NAME_PLATE_NUM, vehicle.getString(VehicleTable.COLUMN_NAME_PLATE_NUM));
                            values.put(VehicleTable.COLUMN_NAME_VEHICLE_WEIGHT, vehicle.getString(VehicleTable.COLUMN_NAME_VEHICLE_WEIGHT));
                            values.put(VehicleTable.COLUMN_NAME_VEHICLE_TYPE, vehicle.getString(VehicleTable.COLUMN_NAME_VEHICLE_TYPE));
                            values.put(VehicleTable.COLUMN_NAME_COMPANY_ID, vehicle.getString(VehicleTable.COLUMN_NAME_COMPANY_ID));
                            values.put(VehicleTable.COLUMN_NAME_START_TIME, vehicle.getString(VehicleTable.COLUMN_NAME_START_TIME));
                            values.put(VehicleTable.COLUMN_NAME_END_TIME, vehicle.getString(VehicleTable.COLUMN_NAME_END_TIME));
//                        Log.d(TAG, (i+1)+". "+values.toString())
                        } catch (JSONException e) {
                            e.printStackTrace();
                            continue;
                        }

                        try{
                            long newRowId = db.insertOrThrow(table_name, null, values);
                            if(newRowId != -1){
                                success_counter = success_counter + 1;
                                Log.d(TAG, "create "+table_name+" successfully");
                            }
                            else{
                                fail_counter = fail_counter + 1;
                                Log.d(TAG, "create "+table_name+" failed");
                            }
                        } catch (SQLException e){
                            if(e.getMessage().contains("no such table")) break;
                            if(Objects.requireNonNull(e.getMessage()).contains("PRIMARYKEY")){
                                String selection = VehicleTable.COLUMN_NAME_VEHICLE_ID+" = ?";
                                String[] selectionArgs = {values.getAsString(VehicleTable.COLUMN_NAME_VEHICLE_ID)};

                                int count = db.update(
                                        table_name,
                                        values,
                                        selection,
                                        selectionArgs
                                );
                                if(count != -1) success_counter = success_counter + 1;
                                else fail_counter = fail_counter + 1;
                            }
                            else{
                                e.printStackTrace();
                                Log.d(TAG, "insert "+table_name+" data: "+e.getMessage());
                            }
                        }
                    }
                    Log.d(TAG, table_name+" data:\n success data: "+success_counter+", fail data: "+fail_counter);
                }
            });
        }
    }

    public static class VehicleAssignmentTable implements BaseColumns{
        public static final String TABLE_NAME = "vehicle_assignment";
        public static final String COLUMN_NAME_ORDER_ID = "order_id";
        public static final String COLUMN_NAME_VEHICLE_ID = "vehicle_id";
        public static final String SQL_CREATE_VEHICLE_ASSIGNMENT = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_ORDER_ID+" INTEGER(10), "+
                COLUMN_NAME_VEHICLE_ID+" INTEGER(10), "+
                "PRIMARY KEY (`order_id`, `vehicle_id`), "+
                "FOREIGN KEY (`order_id`) "+
                "REFERENCES orders(`order_id`) ON DELETE CASCADE, "+
                "FOREIGN KEY (`vehicle_id`) "+
                "REFERENCES vehicle(`vehicle_id`) "+");";
        public static final String SQL_DELETE_VEHICLE_ASSIGNMENT =
                "DROP TABLE IF EXISTS "+TABLE_NAME;
    }

    public static class VehicleDemandTable implements BaseColumns{
        public static final String TABLE_NAME = "vehicle_demand";
        public static final String COLUMN_NAME_ORDER_ID = "order_id";
        public static final String COLUMN_NAME_NUM = "num";
        public static final String COLUMN_NAME_VEHICLE_WEIGHT = "vehicle_weight";
        public static final String COLUMN_NAME_VEHICLE_TYPE = "vehicle_type";
        public static final String SQL_CREATE_VEHICLE_DEMAND = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+ " ( "+
                COLUMN_NAME_ORDER_ID+" INTEGER(10), "+
                COLUMN_NAME_NUM+"INTEGER(10), "+
                COLUMN_NAME_VEHICLE_WEIGHT+" VARCHAR(10), "+
                COLUMN_NAME_VEHICLE_TYPE+" VARCHAR(99), "+
                "PRIMARY KEY (`order_id`, `vehicle_weight`, vehicle_type`), "+
                "FOREIGN KEY (`order_id`) "+
                "REFERENCES orders(`order_id`) ON DELETE CASCADE "+");";
        public static final String SQL_DELETE_VEHICLE_DEMAND =
                "DROP TABLE IF EXISTS "+TABLE_NAME;

        public static void getVehicleDemandData(DatabaseHelper dbHelper, Context context, String order_id){
            String function_name = "vehicle_demand_data";
            String table_name = VehicleDemandTable.TABLE_NAME;
            Log.d(TAG, "start "+function_name+" and write in sqlite db");
            RequestBody body = new FormBody.Builder()
                    .add("function_name", function_name)
                    .add("order_id", order_id)
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
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseData = response.body().string();
                    Log.d(TAG, "responseData of "+function_name+": "+responseData); //顯示資料

                    JSONArray responseArr;
                    try {
                        responseArr= new JSONArray(responseData);
                    }catch (JSONException e){
                        e.printStackTrace();
                        Log.d(TAG, function_name+": "+e.getMessage());
                        return;
                    }

                    int success_counter = 0, fail_counter = 0;
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject vehicle_demand;
                        try {
                            vehicle_demand = responseArr.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return;
                        }
//                    Log.d(TAG, (i+1)+". staff: "+staff);

                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        try {
                            values.put(VehicleDemandTable.COLUMN_NAME_ORDER_ID, vehicle_demand.getString(VehicleDemandTable.COLUMN_NAME_ORDER_ID));
                            values.put(VehicleDemandTable.COLUMN_NAME_NUM, vehicle_demand.getString(VehicleDemandTable.COLUMN_NAME_NUM));
                            values.put(VehicleDemandTable.COLUMN_NAME_VEHICLE_WEIGHT, vehicle_demand.getString(VehicleDemandTable.COLUMN_NAME_VEHICLE_WEIGHT));
                            values.put(VehicleDemandTable.COLUMN_NAME_VEHICLE_TYPE, vehicle_demand.getString(VehicleDemandTable.COLUMN_NAME_VEHICLE_TYPE));
//                        Log.d(TAG, (i+1)+". "+values.toString())
                        } catch (JSONException e) {
                            e.printStackTrace();
                            continue;
                        }

                        try{
                            long newRowId = db.insertOrThrow(table_name, null, values);
                            if(newRowId != -1){
                                success_counter = success_counter + 1;
                                Log.d(TAG, "create "+table_name+" successfully");
                            }
                            else{
                                fail_counter = fail_counter + 1;
                                Log.d(TAG, "create "+table_name+" failed");
                            }
                        } catch (SQLException e){
                            if(e.getMessage().contains("no such table")) break;
                            if(Objects.requireNonNull(e.getMessage()).contains("PRIMARYKEY")){
                                String selection = VehicleDemandTable.COLUMN_NAME_ORDER_ID+" = ?";
                                String[] selectionArgs = {values.getAsString(VehicleDemandTable.COLUMN_NAME_ORDER_ID)};

                                int count = db.update(
                                        table_name,
                                        values,
                                        selection,
                                        selectionArgs
                                );
                                if(count != -1) success_counter = success_counter + 1;
                                else fail_counter = fail_counter + 1;
                            }
                            else{
                                e.printStackTrace();
                                Log.d(TAG, "insert "+table_name+" data: "+e.getMessage());
                            }
                        }
                    }
                    Log.d(TAG, table_name+" data:\n success data: "+success_counter+", fail data: "+fail_counter);
                }
            });
        }

    }
    public static class FurnitureTable implements BaseColumns{
        public static final String TABLE_NAME = "furniture";
        public static final String COLUMN_NAME_FURNITURE_ID = "furniture_id";
        public static final String COLUMN_NAME_SPACE_TYPE = "space_type";
        public static final String COLUMN_NAME_FURNITURE_NAME = "furniture_name";
        public static final String COLUMN_NAME_IMG = "img";
        public static final String SQL_CREATE_FURNITURE = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_FURNITURE_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_SPACE_TYPE+ "VARCHAR(99), "+
                COLUMN_NAME_FURNITURE_NAME+" VARCHAR(99), "+
                "PRIMARY KEY (`furniture_id`) "+");";
        public static final String SQL_DELETE_FURNITURE =
                "DROP TABLE IF EXISTS "+TABLE_NAME;
    }

    public static class RoomTable implements BaseColumns{
        public static final String TABLE_NAME = "room";
        public static final String COLUMN_NAME_ROOM_ID = "room_id";
        public static final String COLUMN_ORDER_ID = "order_id";
        public static final String COLUMN_FLOOR = "floor";
        public static final String COLUMN_ROOM_NAME = "room_name";
        public static final String COLUMN_ROOM_TYPE= "room_type";
        public static final String SQL_CREATE_ROOM = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_ROOM_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_ORDER_ID+" INTEGER(10), "+
                COLUMN_FLOOR+" INTEGER(10), "+
                COLUMN_ROOM_NAME+" INTEGER(10), "+
                COLUMN_ROOM_TYPE+" ENUM(`房間`, 廳`, `戶外`), "+
                "PRIMARY KEY (`room_id`), "+
                "FOREIGN KEY (`order_id`) "+
                "REFERENCES orders(`order_id`) ON DELETE CASCADE "+");";
        public static final String SQL_DELETE_ROOM =
                "DROP TABLE IF EXISTS "+TABLE_NAME;
    }

    public static class FurnitureListAppTable implements BaseColumns{
        public static final String TABLE_NAME = "furniture_list_app";
        public static final String COLUMN_NAME_ORDER_ID = "order_id";
        public static final String COLUMN_NAME_COMPANY_ID = "company_id";
        public static final String COLUMN_NAME_FURNITURE_ID = "furniture_id";
        public static final String COLUMN_NAME_NUM = "num";
        public static final String SQL_CREATE_FURNITURE_LIST = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_ORDER_ID+" INTEGER(10), "+
                COLUMN_NAME_COMPANY_ID+" INTEGER(20), "+
                COLUMN_NAME_FURNITURE_ID+" INTEGER(10), "+
                COLUMN_NAME_NUM+" INTEGER(10), "+
                "PRIMARY KEY (`order_id`, `company_id`, `furniture_id`), "+
                "FOREIGN KEY (`order_id`) "+
                "REFERENCES orders (`order_id`) ON DELETE CASCADE, "+
                "FOREIGN KEY (`company_id`) "+
                "REFERENCES company(`company`), "+
                "FOREIGN KEY (`furniture_id`) "+
                "REFERENCES furniture (`furniture_id`) ON DELETE CASCADE "+");";
        public static final String SQL_DELETE_FURNITURE_LIST =
                "DROP TABLE IF EXISTS "+TABLE_NAME;
    }

    public static class FurniturePositionTable implements BaseColumns {
        public static final String TABLE_NAME = "furniture_id";
        public static final String COLUMN_NAME_ROOM_ID = "room_id";
        public static final String COLUMN_NAME_FURNITURE_ID = "furniture_id";
        public static final String COLUMN_NAME_NUM = "num";
        public static final String SQL_CREATE_FURNITURE_POSITION = "" +
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                COLUMN_NAME_ROOM_ID + " INTEGER(10), " +
                COLUMN_NAME_FURNITURE_ID + " INTEGER(10), " +
                COLUMN_NAME_NUM + " NUM, " +
                "PRIMARY KEY (`room_id`, `furniture_id`), " +
                "FOREIGN KEY (`room_id`) " +
                "REFERENCES room(`room_id`), " +
                "FOREIGN KEY (`furniture_id`) " +
                "REFERENCES furniture(`furniture_id`) ON DELETE CASCADE " + ");";
        public static final String SQL_DELETE_FURNITURE_POSITION =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class DiscountTable implements BaseColumns{
        public static final String TABLE_NAME = "discount";
        public static final String COLUMN_NAME_COMPANY_ID = "company_id";
        public static final String COLUMN_NAME_VALUATE = "valuate";
        public static final String COLUMN_NAME_DEPOSIT = "deposit";
        public static final String COLUMN_NAME_CANCEL = "cancel";
        public static final String COLUMN_NAME_UPDATE_TIME = "update_time";
        public static final String SQL_CREATE_DISCOUNT = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_COMPANY_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_VALUATE+" BOOLEAN DEFAULT FALSE, "+
                COLUMN_NAME_DEPOSIT+" BOOLEAN DEFAULT FALSE, "+
                COLUMN_NAME_CANCEL+" BOOLEAN DEFAULT FALSE, "+
                COLUMN_NAME_UPDATE_TIME+" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "+
                "PRIMARY KEY (`company_id`, `update_time`), "+
                "FOREIGN KEY (`company_id`) "+
                "REFERENCES company(`company_id`) ON DELETE CASCADE"+");\n"+
                "CREATE TRIGGER update_time_trigger "+
                        "AFTER UPDATE ON "+TABLE_NAME+" FOR EACH ROW WHEN "+
                "NEW.update_time <= OLD.update_time"+
                "BEGIN \n"+
                "UPDATE "+TABLE_NAME+" SET "+COLUMN_NAME_COMPANY_ID+
                " = CURRENT_TIMESTAMP WHERE "+COLUMN_NAME_COMPANY_ID+" = "+
                "OLD."+COLUMN_NAME_COMPANY_ID+";"+
                "\n END";
        public static final String SQL_DELETE_DISCOUNT =
                "DROP TABLE IF EXISTS "+TABLE_NAME;
    }

    public static class PeriodDiscountTable implements BaseColumns{
        public static String TABLE_NAME = "period_discount";
        public static final String COLUMN_NAME_DISCOUNT_ID = "discount_id";
        public static final String COLUMN_NAME_COMPANY_ID = "company_id";
        public static final String COLUMN_NAME_DISCOUNT_NAME = "discount_name";
        public static final String COLUMN_NAME_DISCOUNT = "discount";
        public static final String COLUMN_NAME_START_DATE = "start_date";
        public static final String COLUMN_NAME_END_DATE = "end_date";
        public static final String COLUMN_NAME_ENABLE = "enable";
        public static final String COLUMN_NAME_IS_DELETE = "isDelete";
        public static final String COLUMN_NAME_ENABLE_TIME = "enable_time";
        public static final String COLUMN_NAME_DISABLE_TIME = "disable_time";
        public static final String SQL_CREATE_PERIOD_DISCOUNT = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_DISCOUNT_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_COMPANY_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_DISCOUNT_NAME+" VARCHAR(20), "+
                COLUMN_NAME_DISCOUNT+" FLOAT(10) NOT NULL CHECK (`discount` >=0.0 AND `discount` <= 100.0), "+
                COLUMN_NAME_START_DATE+" DATE NOT NULL, "+
                COLUMN_NAME_END_DATE+" DATE DEFAULT NULL, "+
                COLUMN_NAME_ENABLE+" BOOLEAN DEFAULT FALSE, "+
                COLUMN_NAME_IS_DELETE+" BOOLEAN DEFAULT FALSE, "+
                COLUMN_NAME_ENABLE_TIME+" DATETIME DEFAULT NULL, "+
                COLUMN_NAME_DISABLE_TIME+" DATETIME DEFAULT NULL, "+
                "PRIMARY KEY (`discount_id`), "+
                "FOREIGN KEY (`company_id`) "+
                "REFERENCES company(`company_id`) ON DELETE CASCADE "+");";
        public static final String SQL_DELETE_PERIOD_DISCOUNT =
                "DROP TABLE IF EXISTS "+TABLE_NAME;
    }

    public static class ServiceClassTable implements BaseColumns{
        public static final String TABLE_NAME = "service_class";
        public static final String COLUMN_NAME_SERVICE_ID = "service_id";
        public static final String COLUMN_NAME_SERVICE_NAME = "service_name";
        public static final String SQL_CREATE_SERVICE_CLASS = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_SERVICE_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_SERVICE_NAME+" VARCHAR(20), "+
                "PRIMARY KEY (`service_id`), "+
                "UNIQUE (`service_name`) "+");";
        public static final String SQL_DELETE_SERVICE_CLASS =
                "DROP TABLE IF EXISTS "+TABLE_NAME;

        public static void getServiceClass(DatabaseHelper dbHelper) {
            String function_name = "service_class_data";
            String table_name = ServiceClassTable.TABLE_NAME;
            Log.d(TAG, "start "+function_name+" and write in sqlite db");
            RequestBody body = new FormBody.Builder()
                    .build();

            Request request = new Request.Builder()
                    .url(BuildConfig.SERVER_URL + "/get_data/service_class_data.php")
                    .post(body)
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseData = response.body().string();
                    Log.d(TAG, "responseData of "+function_name+": "+responseData); //顯示資料

                    JSONArray responseArr;
                    try {
                        responseArr= new JSONArray(responseData);
                    }catch (JSONException e){
                        e.printStackTrace();
                        Log.d(TAG, function_name+": "+e.getMessage());
                        return;
                    }

                    int success_counter = 0, fail_counter = 0;
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject serviceClass;
                        try {
                            serviceClass = responseArr.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return;
                        }
//                    Log.d(TAG, (i+1)+". staff: "+staff);

                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        try {
                            values.put(ServiceClassTable.COLUMN_NAME_SERVICE_ID, serviceClass.getString(ServiceClassTable.COLUMN_NAME_SERVICE_ID));
                            values.put(ServiceClassTable.COLUMN_NAME_SERVICE_NAME, serviceClass.getString(ServiceClassTable.COLUMN_NAME_SERVICE_NAME));



//                        Log.d(TAG, (i+1)+". "+values.toString())
                        } catch (JSONException e) {
                            e.printStackTrace();
                            continue;
                        }

                        try{
                            long newRowId = db.insertOrThrow(table_name, null, values);
                            if(newRowId != -1){
                                success_counter = success_counter + 1;
                                Log.d(TAG, "create "+table_name+" successfully");
                            }
                            else{
                                fail_counter = fail_counter + 1;
                                Log.d(TAG, "create "+table_name+" failed");
                            }
                        } catch (SQLException e){
                            if(e.getMessage().contains("no such table")) break;
                            if(Objects.requireNonNull(e.getMessage()).contains("PRIMARYKEY")){
                                String selection = ServiceClassTable.COLUMN_NAME_SERVICE_ID+" = ?";
                                String[] selectionArgs = {values.getAsString(ServiceClassTable.COLUMN_NAME_SERVICE_ID)};

                                int count = db.update(
                                        table_name,
                                        values,
                                        selection,
                                        selectionArgs
                                );
                                if(count != -1) success_counter = success_counter + 1;
                                else fail_counter = fail_counter + 1;
                            }
                            else{
                                e.printStackTrace();
                                Log.d(TAG, "insert "+table_name+" data: "+e.getMessage());
                            }
                        }
                    }
                    Log.d(TAG, table_name+" data:\n success data: "+success_counter+", fail data: "+fail_counter);
                }
            });
        }

    }

    public static class ServiceItemTable implements BaseColumns{
        public static final String TABLE_NAME = "service_item";
        public static final String COLUMN_NAME_COMPANY_ID = "company_id";
        public static final String COLUMN_NAME_ITEM_NAME = "item_name";
        public static final String COLUMN_NAME_START_TIME = "start_time";
        public static final String COLUMN_NAME_END_TIME = "end_time";
        public static final String COLUMN_NAME_SERVICE_ID = "service_id";
        public static final String COLUMN_NAME_IS_DELETE = "isDelete";
        public static final String SQL_CREATE_SERVICE_ITEM = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_COMPANY_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_ITEM_NAME+" VARCHAR(20), "+
                COLUMN_NAME_START_TIME+" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "+
                COLUMN_NAME_END_TIME+" DATETIME DEFAULT NULL, "+
                COLUMN_NAME_SERVICE_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_IS_DELETE+" BOOLEAN DEFAULT FALSE, "+
                "PRIMARY KEY (`company_id`, `item_name`, `start_time`), "+
                "FOREIGN KEY (`company_id`) "+
                "REFERENCES company(`company_id`) ON DELETE CASCADE, "+
                "FOREIGN KEY (`service_id`) "+
                "REFERENCES service_class(`service_id`) ON DELETE CASCADE "+");";
        public static final String SQL_DELETE_SERVICE_ITEM =
                "DROP TABLE IF EXISTS "+TABLE_NAME;

        public static void getServiceItem(DatabaseHelper dbHelper, Context context) {
            String function_name = "serviceItem_data";
            String table_name = ServiceItemTable.TABLE_NAME;
            Log.d(TAG, "start "+function_name+" and write in sqlite db");
            RequestBody body = new FormBody.Builder()
                    .add("company_id", getCompany_id(context))
                    .build();

            Request request = new Request.Builder()
                    .url(BuildConfig.SERVER_URL + "/get_data/serviceItem_data.php")
                    .post(body)
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseData = response.body().string();
                    Log.d(TAG, "responseData of "+function_name+": "+responseData); //顯示資料

                    JSONArray responseArr;
                    try {
                        responseArr= new JSONArray(responseData);
                    }catch (JSONException e){
                        e.printStackTrace();
                        Log.d(TAG, function_name+": "+e.getMessage());
                        return;
                    }

                    int success_counter = 0, fail_counter = 0;
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject serviceItem;
                        try {
                            serviceItem = responseArr.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return;
                        }
//                    Log.d(TAG, (i+1)+". staff: "+staff);

                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        try {
                            values.put(ServiceItemTable.COLUMN_NAME_COMPANY_ID, serviceItem.getString(ServiceItemTable.COLUMN_NAME_END_TIME));
                            values.put(ServiceItemTable.COLUMN_NAME_ITEM_NAME, serviceItem.getString(ServiceItemTable.COLUMN_NAME_ITEM_NAME));
                            values.put(ServiceItemTable.COLUMN_NAME_START_TIME, serviceItem.getString(ServiceItemTable.COLUMN_NAME_START_TIME));
                            values.put(ServiceItemTable.COLUMN_NAME_END_TIME, serviceItem.getString(ServiceItemTable.COLUMN_NAME_END_TIME));
                            values.put(ServiceItemTable.COLUMN_NAME_SERVICE_ID, serviceItem.getString(ServiceItemTable.COLUMN_NAME_SERVICE_ID));
                            values.put(ServiceItemTable.COLUMN_NAME_IS_DELETE, serviceItem.getString(ServiceItemTable.COLUMN_NAME_IS_DELETE));


//                        Log.d(TAG, (i+1)+". "+values.toString())
                        } catch (JSONException e) {
                            e.printStackTrace();
                            continue;
                        }

                        try{
                            long newRowId = db.insertOrThrow(table_name, null, values);
                            if(newRowId != -1){
                                success_counter = success_counter + 1;
                                Log.d(TAG, "create "+table_name+" successfully");

                            }
                            else{
                                fail_counter = fail_counter + 1;
                                Log.d(TAG, "create "+table_name+" failed");
                            }
                        } catch (SQLException e){
                            if(e.getMessage().contains("no such table")) break;
                            if(Objects.requireNonNull(e.getMessage()).contains("PRIMARYKEY")){
                                String selection = ServiceItemTable.COLUMN_NAME_COMPANY_ID+" = ?";
                                String[] selectionArgs = {values.getAsString(ServiceItemTable.COLUMN_NAME_COMPANY_ID)};

                                int count = db.update(
                                        table_name,
                                        values,
                                        selection,
                                        selectionArgs
                                );
                                if(count != -1) success_counter = success_counter + 1;
                                else fail_counter = fail_counter + 1;
                            }
                            else{
                                e.printStackTrace();
                                Log.d(TAG, "insert "+table_name+" data: "+e.getMessage());
                            }
                        }
                    }
                    ArrayList<String[]> serviceItem = new ArrayList<>();;
                    SQLiteDatabase db;

                    String company_id = "";
                    String item_name = "";
                    String start_time = "";
                    String end_time = "";
                    String service_id = "";
                    String service_name = "";
                    String isDelete = "";
                    db = dbHelper.getReadableDatabase();
                    String sql_query =
                            "SELECT * FROM "+ ServiceItemTable.TABLE_NAME+" NATURAL JOIN "+TableContract.ServiceClassTable.TABLE_NAME+" " +
                                    "WHERE company_id = "+getCompany_id(context)+" " +
                                    "AND ((isDelete = FALSE AND end_time IS NOT NULL))"+" " +
                                    "OR (end_time IS NULL)" ;
                    Cursor cursor = db.rawQuery(sql_query, null);

                    Log.d(TAG,"cursor count:"+cursor.getCount());//GET result from database
                    if(!cursor.moveToNext()){
                        cursor.close();
                        return;
                    }
                    while(!cursor.isAfterLast()){
                        service_id = cursor.getString(cursor.getColumnIndexOrThrow(ServiceClassTable.COLUMN_NAME_SERVICE_ID));
                        company_id = cursor.getString(cursor.getColumnIndexOrThrow(ServiceItemTable.COLUMN_NAME_COMPANY_ID));
                        item_name = cursor.getString(cursor.getColumnIndexOrThrow(ServiceItemTable.COLUMN_NAME_ITEM_NAME));
                        start_time = cursor.getString(cursor.getColumnIndexOrThrow(ServiceItemTable.COLUMN_NAME_START_TIME));
                        end_time = cursor.getString(cursor.getColumnIndexOrThrow(ServiceItemTable.COLUMN_NAME_END_TIME));
                        isDelete = cursor.getString(cursor.getColumnIndexOrThrow(ServiceItemTable.COLUMN_NAME_END_TIME));
                        service_name = cursor.getString(cursor.getColumnIndexOrThrow(ServiceClassTable.COLUMN_NAME_SERVICE_NAME));
                        String[] serviceItem_data = {service_id, company_id, item_name, start_time, end_time, isDelete, service_name};
                        Log.d(TAG,"("+(cursor.getPosition()+1)+"/"+cursor.getCount()+"). sqlite comment: "+ Arrays.toString(serviceItem_data));
                        serviceItem.add(serviceItem_data);

                        cursor.moveToNext();
                    }
                    ContentValues values = new ContentValues();
                    for(int i=0; i < serviceItem.size(); i++){
                        db = dbHelper.getWritableDatabase();
                        values.put((ServiceItemTable.COLUMN_NAME_SERVICE_ID),service_id);
                        values.put((ServiceItemTable.COLUMN_NAME_COMPANY_ID), company_id);
                        values.put((ServiceItemTable.COLUMN_NAME_ITEM_NAME), item_name);
                        values.put((ServiceItemTable.COLUMN_NAME_START_TIME),start_time);
                        values.put((ServiceItemTable.COLUMN_NAME_END_TIME), end_time);
                        values.put((ServiceItemTable.COLUMN_NAME_IS_DELETE), isDelete);
                        //values.put((ServiceClassTable.COLUMN_NAME_SERVICE_NAME), service_name);


                        Log.i(TAG, "data: "+ Arrays.toString(serviceItem.get(i)));
                    }
                    cursor.close();

                    try{
                        long newRowId = db.insertOrThrow(TableContract.ServiceItemTable.TABLE_NAME, null, values);
                        if(newRowId != -1) {
                            Log.d(TAG,"create successfully");}
                        else Log.d(TAG, "create failed");
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                    Log.d(TAG, table_name+" data:\n success data: "+success_counter+", fail data: "+fail_counter);
                }
            });
        }

    }

    public static class AnnouncementTable implements BaseColumns{
        public static final String TABLE_NAME = "announcement";
        public static final String COLUMN_NAME_ANNOUNCEMENT_ID = "announcement_id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_OUTLINE = "outline";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String SQL_CREATE_ANNOUNCEMENT = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_ANNOUNCEMENT_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_TITLE+" VARCHAR(20), "+
                COLUMN_NAME_OUTLINE+" VARCHAR(30), "+
                COLUMN_NAME_CONTENT+" VARCHAR(300), "+
                COLUMN_NAME_DATE+" TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "+
                "PRIMARY KEY (`announcement_id`) "+");";
        public static final String SQL_DELETE_ANNOUNCEMENT =
                "DROP TABLE IF EXISTS "+TABLE_NAME;

        public static void getAnnouncementData(DatabaseHelper dbHelper, Context context){
            String function_name = "announcement_data";
            String table_name = AnnouncementCompanyTable.TABLE_NAME;
            Log.d(TAG, "start "+function_name+" and write in sqlite db");
            RequestBody body = new FormBody.Builder()
                    .add("company_id", getCompany_id(context))
                    .build();

            Request request = new Request.Builder()
                    .url(BuildConfig.SERVER_URL + "/get_data/announcement_data.php")
                    .post(body)
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseData = response.body().string();
                    Log.d(TAG, "responseData of "+function_name+": "+responseData); //顯示資料

                    JSONArray responseArr;
                    try {
                        responseArr= new JSONArray(responseData);
                    }catch (JSONException e){
                        e.printStackTrace();
                        Log.d(TAG, function_name+": "+e.getMessage());
                        return;
                    }

                    int success_counter = 0, fail_counter = 0;
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject announcement;
                        try {
                            announcement = responseArr.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return;
                        }
//                    Log.d(TAG, (i+1)+". staff: "+staff);

                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        try {
                            values.put(AnnouncementCompanyTable.COLUMN_NAME_ANNOUNCEMENT_ID, announcement.getString(AnnouncementCompanyTable.COLUMN_NAME_ANNOUNCEMENT_ID));
                            values.put(AnnouncementCompanyTable.COLUMN_NAME_COMPANY_ID, announcement.getString(AnnouncementCompanyTable.COLUMN_NAME_COMPANY_ID));
                            values.put(AnnouncementCompanyTable.COLUMN_NAME_NEW, announcement.getString(AnnouncementCompanyTable.COLUMN_NAME_NEW));

//                        Log.d(TAG, (i+1)+". "+values.toString())
                        } catch (JSONException e) {
                            e.printStackTrace();
                            continue;
                        }

                        try{
                            long newRowId = db.insertOrThrow(table_name, null, values);
                            if(newRowId != -1){
                                success_counter = success_counter + 1;
                                Log.d(TAG, "create "+table_name+" successfully");
                            }
                            else{
                                fail_counter = fail_counter + 1;
                                Log.d(TAG, "create "+table_name+" failed");
                            }
                        } catch (SQLException e){
                            if(e.getMessage().contains("no such table")) break;
                            if(Objects.requireNonNull(e.getMessage()).contains("PRIMARYKEY")){
                                String selection = AnnouncementCompanyTable.COLUMN_NAME_ANNOUNCEMENT_ID+" = ?";
                                String[] selectionArgs = {values.getAsString(AnnouncementCompanyTable.COLUMN_NAME_ANNOUNCEMENT_ID)};

                                int count = db.update(
                                        table_name,
                                        values,
                                        selection,
                                        selectionArgs
                                );
                                if(count != -1) success_counter = success_counter + 1;
                                else fail_counter = fail_counter + 1;
                            }
                            else{
                                e.printStackTrace();
                                Log.d(TAG, "insert "+table_name+" data: "+e.getMessage());
                            }
                        }
                    }
                    Log.d(TAG, table_name+" data:\n success data: "+success_counter+", fail data: "+fail_counter);
                }
            });
        }
    }



    public static class CommentsTable implements BaseColumns{
        public static final String TABLE_NAME = "comments";
        public static final String COLUMN_NAME_COMMENT_ID = "comment_id";
        public static final String COLUMN_NAME_ORDER_ID = "order_id";
        public static final String COLUMN_NAME_MEMBER_ID = "member_id";
        public static final String COLUMN_NAME_COMPANY_ID = "company_id";
        public static final String COLUMN_NAME_COMMENT_DATE = "comment_date";
        public static final String COLUMN_NAME_SERVICE_QUALITY = "service_quality";
        public static final String COLUMN_NAME_WORK_ATTITUDE = "work_attitude";
        public static final String COLUMN_NAME_PRICE_GRADE = "price_grade";
        public static final String COLUMN_NAME_COMMENT = "comment";
        public static final String COLUMN_NAME_REPLY = "reply";

        public static final String SQL_CREATE_COMMENTS = "" +
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_COMMENT_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_ORDER_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_MEMBER_ID+" INTEGER(10), "+
                COLUMN_NAME_COMPANY_ID+" INTEGER(10), "+
                COLUMN_NAME_COMMENT_DATE+" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "+
                COLUMN_NAME_SERVICE_QUALITY+" FLOAT(10) NOT NULL CHECK (`service_quality` >= 0 AND `service_quality` <= 5), "+
                COLUMN_NAME_WORK_ATTITUDE+" FLOAT(10) NOT NULL CHECK (`work_attitude` >= 0 AND `work_attitude` <=5), "+
                COLUMN_NAME_PRICE_GRADE+" FLOAT(10) NOT NULL CHECK (`price_grade` >= 0 AND `price_grade` <=5), "+
                COLUMN_NAME_COMMENT+" NVARCHAR(300) DEFAULT NULL, "+
                COLUMN_NAME_REPLY+" NVARCHAR(300) DEFAULT NULL, "+
                "PRIMARY KEY (`comment_id`), "+
                "FOREIGN KEY (`order_id`) "+
                "REFERENCES orders(`order_id`) ON DELETE CASCADE, "+
                "FOREIGN KEY (`member_id`) "+
                "REFERENCES member(`member_id`) ON DELETE CASCADE, "+
                "FOREIGN KEY (`company_id`) "+
                "REFERENCES company(`company_id`) ON DELETE CASCADE "+
                ");"+"\n"+
                "CREATE TRIGGER update_time_trigger "+
                "AFTER UPDATE ON "+TABLE_NAME+" FOR EACH ROW WHEN "+
                "NEW.comment_date <= OLD.comment_date"+
                "BEGIN \n"+
                    "UPDATE "+TABLE_NAME+" SET "+COLUMN_NAME_COMMENT_DATE+
                " = CURRENT_TIMESTAMP WHERE "+COLUMN_NAME_COMMENT_ID+" = "+
                "OLD."+COLUMN_NAME_COMMENT_ID+";"+
                "\n END";

        public static final String SQL_DELETE_COMMENTS =
                "DROP TABLE IF EXISTS "+ TABLE_NAME;
    }


    public static class StaffLeaveTable implements BaseColumns{
        public static final String TABLE_NAME = "staff_leave";
        public static final String COLUMN_NAME_STAFF_ID = "staff_id";
        public static final String COLUMN_NAME_LEAVE_DATE = "leave_date";
        public static final String SQL_CREATE_STAFF_LEAVE = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_STAFF_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_LEAVE_DATE+" DATE NOT NULL, "+
                "PRIMARY KEY (`staff_id`, `leave_date`), "+
                "FOREIGN KEY (`staff_id`) "+
                "REFERENCES staff(`staff_id`) ON DELETE CASCADE "+");";
        public static final String SQL_DELETE_SQL_STAFF_LEAVE =
                "DROP TABLE IF EXISTS "+TABLE_NAME;


        public static void getStaffLeave(DatabaseHelper dbHelper, Context context){
            ArrayList<String[]> leave = new ArrayList<>();;
            SQLiteDatabase db;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date datetime = simpleDateFormat.getCalendar().getTime();
            String date = simpleDateFormat.format(datetime);
            String staff_id="";
            String staff_name="";
            String company_id="";
            String start_time="";
            String end_time="";
            String leave_date="";
            db = dbHelper.getReadableDatabase();
            String sql_query =
                    "SELECT * FROM "+TableContract.StaffLeaveTable.TABLE_NAME+" NATURAL JOIN "+TableContract.StaffTable.TABLE_NAME+" " +
                            "WHERE company_id = "+getCompany_id(context)+" " +
                            "AND leave_date = "+date;
            Cursor cursor = db.rawQuery(sql_query, null);

            Log.d(TAG,"cursor count:"+cursor.getCount());//GET result from database
            if(!cursor.moveToNext()){
                cursor.close();
                return;
            }
            while(!cursor.isAfterLast()){
                 staff_id = cursor.getString(cursor.getColumnIndexOrThrow(StaffLeaveTable.COLUMN_NAME_STAFF_ID));
                 staff_name = cursor.getString(cursor.getColumnIndexOrThrow(StaffTable.COLUMN_NAME_STAFF_NAME));
                 company_id = cursor.getString(cursor.getColumnIndexOrThrow(StaffTable.COLUMN_NAME_COMPANY_ID));
                 start_time = cursor.getString(cursor.getColumnIndexOrThrow(StaffTable.COLUMN_NAME_START_TIME));
                 end_time = cursor.getString(cursor.getColumnIndexOrThrow(StaffTable.COLUMN_NAME_END_TIME));
                 leave_date = cursor.getString(cursor.getColumnIndexOrThrow(StaffLeaveTable.COLUMN_NAME_LEAVE_DATE));
                String[] leave_data = {staff_id, staff_name, company_id, start_time, end_time, staff_id, leave_date};
                Log.d(TAG,"("+(cursor.getPosition()+1)+"/"+cursor.getCount()+"). sqlite comment: "+ Arrays.toString(leave_data));
                leave.add(leave_data);

                cursor.moveToNext();
            }
            ContentValues values = new ContentValues();
            for(int i=0; i < leave.size(); i++){
                db = dbHelper.getWritableDatabase();

                values.put((TableContract.StaffLeaveTable.COLUMN_NAME_STAFF_ID),staff_id);
                //values.put((TableContract.StaffTable.COLUMN_NAME_STAFF_NAME), staff_name);
                //values.put((TableContract.StaffTable.COLUMN_NAME_COMPANY_ID), company_id);
                //values.put((TableContract.StaffTable.COLUMN_NAME_START_TIME),start_time);
                //values.put((TableContract.StaffTable.COLUMN_NAME_END_TIME), end_time);
                values.put((TableContract.StaffLeaveTable.COLUMN_NAME_LEAVE_DATE), leave_date);
                Log.i(TAG, "data: "+ Arrays.toString(leave.get(i)));
            }
            cursor.close();

            try{
                long newRowId = db.insertOrThrow(TableContract.StaffLeaveTable.TABLE_NAME, null, values);
                if(newRowId != -1) {
                    Log.d(TAG,"create successfully");}
                else Log.d(TAG, "create failed");
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }



    public static class VehicleMaintainTable implements BaseColumns{
        public static final String TABLE_NAME = "vehicle_maintain";
        public static final String COLUMN_NAME_VEHICLE_ID = "vehicle_id";
        public static final String COLUMN_NAME_MAINTAIN_DATE = "maintain_date";
        public static final String SQL_CREATE_VEHICLE_MAINTAIN = ""+
                "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
                COLUMN_NAME_VEHICLE_ID+" INTEGER(10) NOT NULL, "+
                COLUMN_NAME_MAINTAIN_DATE+" DATE NOT NULL, "+
                "PRIMARY KEY (`vehicle_id`, `maintain_date`), "+
                "FOREIGN KEY (`vehicle_id`) "+
                "REFERENCES vehicle(`vehicle_id`) ON DELETE CASCADE "+
                ");";
        public static final String SQL_DELETE_VEHICLE_MAINTAIN =
                "DELETE TABLE IF EXISTS "+TABLE_NAME;

        public static void getVehicleMaintain(DatabaseHelper dbHelper, Context context){
            ArrayList<String[]> maintain = new ArrayList<>();;
            SQLiteDatabase db;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date datetime = simpleDateFormat.getCalendar().getTime();
            String date = simpleDateFormat.format(datetime);
            String vehicle_id = "";
            String maintain_date = "";
            String plate_num = "";
            String vehicle_weight = "";
            String vehicle_type = "";
            String company_id = "";
            String start_time = "";
            String end_time = "";
            db = dbHelper.getReadableDatabase();
            String sql_query =
                    "SELECT * FROM "+ VehicleMaintainTable.TABLE_NAME+" NATURAL JOIN "+TableContract.VehicleTable.TABLE_NAME+" " +
                            "WHERE company_id = "+getCompany_id(context)+" " +
                            "AND maintain = "+date;
            Cursor cursor = db.rawQuery(sql_query, null);

            Log.d(TAG,"cursor count:"+cursor.getCount());//GET result from database
            if(!cursor.moveToNext()){
                cursor.close();
                return;
            }
            while(!cursor.isAfterLast()){
                vehicle_id = cursor.getString(cursor.getColumnIndexOrThrow(VehicleMaintainTable.COLUMN_NAME_VEHICLE_ID));
                maintain_date = cursor.getString(cursor.getColumnIndexOrThrow(VehicleMaintainTable.COLUMN_NAME_MAINTAIN_DATE));
                plate_num = cursor.getString(cursor.getColumnIndexOrThrow(VehicleTable.COLUMN_NAME_PLATE_NUM));
                vehicle_weight = cursor.getString(cursor.getColumnIndexOrThrow(VehicleTable.COLUMN_NAME_VEHICLE_WEIGHT));
                vehicle_type = cursor.getString(cursor.getColumnIndexOrThrow(VehicleTable.COLUMN_NAME_VEHICLE_TYPE));
                company_id = cursor.getString(cursor.getColumnIndexOrThrow(VehicleTable.COLUMN_NAME_COMPANY_ID));
                start_time = cursor.getString(cursor.getColumnIndexOrThrow(VehicleTable.COLUMN_NAME_START_TIME));
                end_time = cursor.getString(cursor.getColumnIndexOrThrow(VehicleTable.COLUMN_NAME_END_TIME));
                String[] leave_data = {vehicle_id, maintain_date, plate_num, vehicle_weight, vehicle_type, company_id, start_time, end_time};
                Log.d(TAG,"("+(cursor.getPosition()+1)+"/"+cursor.getCount()+"). sqlite comment: "+ Arrays.toString(leave_data));
                maintain.add(leave_data);

                cursor.moveToNext();
            }
            ContentValues values = new ContentValues();
            for(int i=0; i < maintain.size(); i++){
                db = dbHelper.getWritableDatabase();
                values.put((TableContract.VehicleMaintainTable.COLUMN_NAME_VEHICLE_ID),vehicle_id);
                values.put((TableContract.VehicleMaintainTable.COLUMN_NAME_MAINTAIN_DATE), maintain_date);
                //values.put((TableContract.VehicleTable.COLUMN_NAME_PLATE_NUM), plate_num);
                //values.put((TableContract.VehicleTable.COLUMN_NAME_VEHICLE_WEIGHT),vehicle_weight);
                //values.put((TableContract.VehicleTable.COLUMN_NAME_VEHICLE_TYPE), vehicle_type);
                //values.put((TableContract.VehicleTable.COLUMN_NAME_COMPANY_ID), company_id);
                //values.put((TableContract.VehicleTable.COLUMN_NAME_START_TIME), start_time);
                //values.put((TableContract.VehicleTable.COLUMN_NAME_END_TIME), end_time);

                Log.i(TAG, "data: "+ Arrays.toString(maintain.get(i)));
            }
            cursor.close();

            try{
                long newRowId = db.insertOrThrow(TableContract.VehicleMaintainTable.TABLE_NAME, null, values);
                if(newRowId != -1) {
                    Log.d(TAG,"create successfully");}
                else Log.d(TAG, "create failed");
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public static class AnnouncementCompanyTable implements BaseColumns{
        public static final String TABLE_NAME = "announcement_company";
        public static final String COLUMN_NAME_ANNOUNCEMENT_ID = "announcement_id";
        public static final String COLUMN_NAME_COMPANY_ID = "company_id";
        public static final String COLUMN_NAME_NEW = "new";
        public static final String SQL_CREATE_ANNOUNCEMENT_COMPANY = ""+
        "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ( "+
        COLUMN_NAME_ANNOUNCEMENT_ID+" INTEGER(10) NOT NULL, "+
        COLUMN_NAME_COMPANY_ID+" INTEGER(1) NOT NULL, "+
        COLUMN_NAME_NEW+" BOOLEAN DEFAULT TRUE, "+
        "PRIMARY KEY (`announcement_id`, `company_id`), "+
        "FOREIGN KEY (`announcement_id`) "+
        "REFERENCES announcement(`announcement_id`) ON DELETE CASCADE, "+
        "FOREIGN KEY (`company_id`) "+
        "REFERENCES company(`company_id`) ON DELETE CASCADE"+");";
        public static final String SQL_DELETE_ANNOUNCEMENT_COMPANY =
                "DELETE TABLE IF EXISTS "+TABLE_NAME;
    }

}



