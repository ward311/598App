package com.example.homerenting_prototype_one.show;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.helper.SessionManager;
import com.example.homerenting_prototype_one.model.User;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class global_function {
    private static int wCount = 0;
    private static int month;
    private static Date startDate;
    private static Date endDate;
    private static String company_id, comment_id;
    private static SessionManager session;
    private static User user;
    private static ArrayList<String> datalist = new ArrayList<>();

    private static String TAG = "global_function";

    static OkHttpClient okHttpClient = new OkHttpClient();

    public static void removeNew(String order_id, final Context context, String plan){
        Log.d(TAG, "removeNew");
        String function_name = "update_new";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .add("company_id", getCompany_id(context))
                .add("new", "FALSE")
                .add("plan", plan)
                .build();

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/functional.php")
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //??????????????????
                Looper.prepare();
                Toast.makeText(context, "????????????????????????", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, "respneseData of remove_new: "+responseData);
            }
        });
    }

    public static void removeAnnounceNew(String announcement_id, final Context context){
        Log.d(TAG, "removeNew");
        String function_name = "update_announcement_new";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("announcement_id", announcement_id)
                .add("company_id", getCompany_id(context))
                .build();

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/functional.php")
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //??????????????????
                Toast.makeText(context, "new onFailure.", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, "respneseData of remove_new: "+responseData);
            }
        });
    }

    public static void changeStatus(String order_id, String table, String status, Context context, String plan) {
        Log.d(TAG, "change status: order_id "+order_id+" "+status);
        String function_name = "change_status";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .add("company_id", getCompany_id(context))
                .add("table", table)
                .add("status", status)
                .add("plan", plan)
                .build();

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/functional.php")
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //??????????????????
                Toast.makeText(context, "????????????", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, "respneseData of change_status: "+responseData);
            }
        });
    }

    public static void changeStat(String order_id, String table, String status, Context context) {
        Log.d(TAG, "change status: order_id "+order_id+" "+status);
        String function_name = "change_stat";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .add("company_id", getCompany_id(context))
                .add("table", table)
                .add("status", status)
                .build();

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/functional.php")
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //??????????????????
                Toast.makeText(context, "????????????", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, "respneseData of change_status: "+responseData);
            }
        });
    }

    public static String getYear(String datetime){ //???datetime??????????????????
        String[] token = datetime.split(" ");
        String[] date_token = token[0].split("-");
        return date_token[0];
    }

    public static String getMonth(String datetime){ //???datetime??????????????????
        String[] token = datetime.split(" ");
        String[] date_token = token[0].split("-");
        return date_token[1];
    }

    public static String getDay(String datetime){ //???datetime??????????????????
        String[] token = datetime.split(" ");
        String[] date_token = token[0].split("-");
        return date_token[2];
    }

    public static String getDate(String datetime){ //???datetime?????????????????????
        String[] token = datetime.split(" ");
        String[] date_token = token[0].split("-");
        return date_token[1]+"/"+date_token[2];
    }

    public static String getTime(String datetime){ //???datetime?????????????????????
        String[] token = datetime.split(" ");
        String[] time_token = token[1].split(":");
        return time_token[0]+":"+time_token[1];
    }

    public static String getStartTime(String time){ //???valuation_time(01:00~02:00)?????????????????????
        String[] token = time.split("~");
        return token[0];
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale);
    }

    public static String getWeek(){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        Date today = c.getTime();
        Log.i(TAG, "today: "+today);

        int week = c.get(Calendar.DAY_OF_WEEK);   //???????????????
        //Log.i(TAG, "week: "+week); //???:1??????:2???...??????:6??????:7
        c.add(Calendar.DATE, (1-week)+(wCount*7));   //?????????????????????
        startDate = c.getTime();
        c.add(Calendar.DATE, 6); //?????????????????????
        endDate = c.getTime();

        month = c.get(Calendar.MONTH);
        Log.d(TAG, "month: "+c.get(Calendar.MONTH));

        SimpleDateFormat sdf = new SimpleDateFormat("MM.dd");
        String startStr = sdf.format(startDate);
        String endStr = sdf.format(endDate);
        String weekDay = startStr+" ~ "+endStr;
        Log.i(TAG, "week date: "+weekDay);

        return weekDay;
    }

    public static String getStartOfWeek(){ //????????????????????????
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startStr = sdf.format(startDate);
        //Log.i(TAG, "startStr: "+startStr);
        return startStr;
    }

    public static String getEndOfWeek(){ //????????????????????????
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String endStr = sdf.format(endDate);
        //Log.i(TAG, "endStr: "+endStr);
        return endStr;
    }

    //???????????????
    public static void setwCount(int newWCount){ wCount = newWCount;}
    public static int getwCount(){ return wCount;}

    public static String getToday(){ //???????????????MM.dd
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        Date today = c.getTime();
        month = c.get(Calendar.MONTH);
        Log.d(TAG, "month: "+c.get(Calendar.MONTH));

        SimpleDateFormat sdf = new SimpleDateFormat("MM.dd");
        String todayStr = sdf.format(today);
        Log.i(TAG, "todayStr: "+todayStr);

        return todayStr;
    }

    public static String getToday(String dateFormate){ //???????????????yyyy MM dd
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        Date today = c.getTime();
        month = c.get(Calendar.MONTH);
        Log.d(TAG, "month: "+c.get(Calendar.MONTH));

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormate);
        String todayStr = sdf.format(today);
        Log.i(TAG, "todayStr: "+todayStr);

        return todayStr;
    }

    public static String getMonthStr(){ //?????????????????????
        String m;
        switch (month){
            case 0:
                m = "??????";
                break;
            case 1:
                m = "??????";
                break;
            case 2:
                m = "??????";
                break;
            case 3:
                m = "??????";
                break;
            case 4:
                m = "??????";
                break;
            case 5:
                m = "??????";
                break;
            case 6:
                m = "??????";
                break;
            case 7:
                m = "??????";
                break;
            case 8:
                m = "??????";
                break;
            case 9:
                m = "??????";
                break;
            case 10:
                m = "?????????";
                break;
            case 11:
                m = "?????????";
                break;
            default:
                m = "??????";
        }
        return m;
    }

    public static String getCompany_id(Context context){ //????????????ID
        session = SessionManager.getInstance(context);
        user = session.getUserDetail();
        company_id = user.getCompany_id();
        return company_id;
    }

    //??????????????????????????????
    public static void addDatalist(String id){
        datalist.add(id);
    }
    public static void clearDatalist(){
        datalist.clear();
    }

    public static ArrayList<String> getDatalist(){
        return datalist;
    }

    public static String getlastDatalist(String value){
        int current = datalist.indexOf(value);
        if(current == 0) return null;
        return datalist.get(current-1);
    }

    public static String getnextDatalist(String value){
        int current = datalist.indexOf(value);
        if(current == datalist.size()-1) return null;
        return datalist.get(current+1);
    }
}
