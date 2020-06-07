package com.example.homerenting_prototype_one.show;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.order.Order;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
    private static Date startDate;
    private static Date endDate;

    private static String TAG = "global_function";

    static OkHttpClient okHttpClient = new OkHttpClient();

    public static void removeNew(String order_id, final Context context){
        Log.d(TAG, "removeNew");
        String function_name = "update_new";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("order_id", order_id)
                .add("new", "FALSE")
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
                Log.d(TAG, "Failed: " + e.getMessage()); //顯示錯誤訊息
                Toast.makeText(context, "new onFailure.", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG, "new click success");
            }
        });
    }
    public static String getYear(String datetime){ //從datetime中取出年部分
        String[] token = datetime.split(" ");
        String[] date_token = token[0].split("-");
        return date_token[0];
    }

    public static String getMonth(String datetime){ //從datetime中取出月部分
        String[] token = datetime.split(" ");
        String[] date_token = token[0].split("-");
        return date_token[1];
    }

    public static String getDay(String datetime){ //從datetime中取出日部分
        String[] token = datetime.split(" ");
        String[] date_token = token[0].split("-");
        return date_token[2];
    }

    public static String getDate(String datetime){ //從datetime中取出日期部分
        String[] token = datetime.split(" ");
        String[] date_token = token[0].split("-");
        return date_token[1]+"/"+date_token[2];
    }

    public static String getTime(String datetime){ //從datetime中取出時間部分
        String[] token = datetime.split(" ");
        String[] time_token = token[1].split(":");
        return time_token[0]+":"+time_token[1];
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

        int week = c.get(Calendar.DAY_OF_WEEK);   //今天星期幾
        //Log.i(TAG, "week: "+week); //日:1、一:2、...、五:6、六:7
        c.add(Calendar.DATE, (1-week)+(wCount*7));   //取一週開頭日期
        startDate = c.getTime();
        c.add(Calendar.DATE, 6); //取一週結尾日期
        endDate = c.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("MM.dd");
        String startStr = sdf.format(startDate);
        String endStr = sdf.format(endDate);
        String weekDay = startStr+" ~ "+endStr;
        Log.i(TAG, "week date: "+weekDay);

        return weekDay;
    }

    public static String getStartOfWeek(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startStr = sdf.format(startDate);
        //Log.i(TAG, "startStr: "+startStr);
        return startStr;
    }

    public static String getEndOfWeek(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String endStr = sdf.format(endDate);
        //Log.i(TAG, "endStr: "+endStr);
        return endStr;
    }

    public static void setwCount(int newWCount){ wCount = newWCount;}
    public static int getwCount(){ return wCount;}
}
