package com.example.homerenting_prototype_one.add_order;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.system.System;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.furniture.Edit_Furniture;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.setting.Setting;
import com.example.homerenting_prototype_one.valuation.Valuation;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;

public class Add_Order extends AppCompatActivity {
    EditText name_edit, phone_edit, price_edit, worktime_edit, notice_edit;
    EditText outCity, outDistrict, outAddress;
    EditText cCity, cDistrict, cAddress;
    EditText inCity, inDistrict, inAddress;
    TextView movingDate_text, movingTime_text;
    ListView furniture_list;
    RadioGroup genderRG;
    Button editFurnitureBtn, addOrderBtn;
    Spinner contact_citySpin, contact_districtSpin;
    Spinner in_citySpin, in_districtSpin;
    String gender = "男", furniture_data = "";
    String time;
    Bundle bundle;

    GregorianCalendar calendar = new GregorianCalendar();
    OkHttpClient okHttpClient = new OkHttpClient();

    Context context = Add_Order.this;
    String TAG = "Add_Order";


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__order);

        linking();
        setSpinner();
        if(getIntent().getExtras() != null){
            bundle = getIntent().getExtras();
            Log.d(TAG, "furniture_data: "+bundle.getString("furniture_data"));
            setTextData();
        }
        else bundle = new Bundle();

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Taipei"));
//        String now_year = String.valueOf(now.getYear());
//        String now_month = String.valueOf(monthToInt(String.valueOf(now.getMonth())));
//        String now_day = String.valueOf(now.getDayOfMonth());
//        if(monthToInt(String.valueOf(now.getMonth())) < 10) now_month = "0"+now_month;
//        if(now.getDayOfMonth() < 10) now_day = "0"+now_day;
//        Log.d(TAG, "now: "+now_year+"-"+now_month+"-"+now_day);
//        movingDate_text.setText(now_year+"-"+now_month+"-"+now_day);

        movingDate_text.setOnClickListener(v -> {
            DatePickerDialog datePicker = new DatePickerDialog( Add_Order.this, (view, year, month, dayOfMonth) -> {
                if(year<now.getYear() || (month+1)<monthToInt(String.valueOf(now.getMonth())) || dayOfMonth<now.getDayOfMonth()) {
                    Toast.makeText(context, "請勿選擇過去的日期", Toast.LENGTH_SHORT);
                    return;
                }
                String monthStr = String.valueOf(month+1);
                if(month+1 < 10) monthStr = "0"+monthStr;
                String dayStr = String.valueOf(dayOfMonth);
                if(dayOfMonth < 10) dayStr = "0"+dayStr;
                movingDate_text.setText(year+"-"+monthStr+"-"+dayStr);
            },calendar.get( GregorianCalendar.YEAR ),calendar.get( GregorianCalendar.MONTH ),calendar.get( GregorianCalendar.DAY_OF_MONTH));
            datePicker.getDatePicker().setMinDate(new Date().getTime());
            datePicker.show();
        });

        movingTime_text.setText(now.getHour()+":00");
        time = now.getHour()+":00";
        movingTime_text.setOnClickListener(v -> {
            TimePickerDialog time_picker = new TimePickerDialog( context, (view, hourOfDay, minute) -> {
                String mm = String.valueOf(minute);
                if(minute < 10) mm = "0"+mm;
                String hh = String.valueOf(hourOfDay);
                if(hourOfDay < 10) hh = "0"+hh;
                movingTime_text.setText(hh+":"+mm);
                time = hourOfDay+":"+minute;
            },calendar.get(GregorianCalendar.DAY_OF_MONTH ),calendar.get(GregorianCalendar.MINUTE ),true);
            time_picker.show();
        });

        cCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                outCity.setError(null);

                String c_City = cCity.getText().toString();
                String moveOut_City = outCity.getText().toString();

                outCity.setText(c_City);

            }
        });

        cDistrict.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                outDistrict.setError(null);

                String c_District = cDistrict.getText().toString();
                String moveOut_District = outDistrict.getText().toString();

                outDistrict.setText(c_District);

            }
        });

        cAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {

                outAddress.setError(null);

                String c_Address = cAddress.getText().toString();
                String moveOut_Address = outAddress.getText().toString();

                outAddress.setText(c_Address);

            }
        });


        editFurnitureBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            bundle.putString("order_id", "-1");
            getTextData();
            intent.putExtras(bundle);
            intent.setClass(context, Edit_Furniture.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            setResult(RESULT_OK, getIntent());
            startActivity(intent);
        });


        addOrderBtn.setOnClickListener(v -> {
            if(checkEmpty()) return;
            if(bundle.getString("furniture_data") != null)
                furniture_data = bundle.getString("furniture_data");

            addOrder();

            Handler handler = new Handler();
            handler.postDelayed(() -> {
                Intent intent = new Intent();
                intent.setClass(context, Calendar.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("end", "true");
                startActivity(intent);

            }, 1500);
        });

        globalNav();

    }

    public void getTextData(){
        bundle.putString("name", name_edit.getText().toString());
        bundle.putInt("gender", genderRG.getCheckedRadioButtonId());
        bundle.putString("cCity", cCity.getText().toString());
        bundle.putString("cDistrict", cDistrict.getText().toString());
        bundle.putString("cAddress", cAddress.getText().toString());
        bundle.putString("phone", phone_edit.getText().toString());
        bundle.putString("outCity", outCity.getText().toString());
        bundle.putString("outDistrict", outDistrict.getText().toString());
        bundle.putString("outAddress", outAddress.getText().toString());
        bundle.putString("inCity", inCity.getText().toString());
        bundle.putString("inDistrict", inDistrict.getText().toString());
        bundle.putString("inAddress", inAddress.getText().toString());
        bundle.putString("pickDate", movingDate_text.getText().toString());
        bundle.putString("pickTime", movingTime_text.getText().toString());
        bundle.putString("price", price_edit.getText().toString());
        bundle.putString("worktime", worktime_edit.getText().toString());
        bundle.putString("notice", notice_edit.getText().toString());
    }

     public void setTextData(){
        if(bundle.containsKey("name")) name_edit.setText(bundle.getString("name"));
        if(bundle.containsKey("gender")) genderRG.check(bundle.getInt("gender"));
        if(bundle.containsKey("cCity")) cCity.setText(bundle.getString("cCity"));
        if(bundle.containsKey("cDistrict")) cDistrict.setText(bundle.getString("cDistrict"));
        if(bundle.containsKey("cAddress")) cAddress.setText(bundle.getString("cAddress"));
        if(bundle.containsKey("phone")) phone_edit.setText(bundle.getString("phone"));
        if(bundle.containsKey("outCity")) outCity.setText(bundle.getString("outCity"));
        if(bundle.containsKey("outDistrict")) outDistrict.setText(bundle.getString("outDistrict"));
        if(bundle.containsKey("outAddress")) outAddress.setText(bundle.getString("outAddress"));
        if(bundle.containsKey("inCity")) inCity.setText(bundle.getString("inCity"));
        if(bundle.containsKey("inDistrict")) inDistrict.setText(bundle.getString("inDistrict"));
        if(bundle.containsKey("inAddress")) inAddress.setText(bundle.getString("inAddress"));
        if(bundle.containsKey("pickDate")) movingDate_text.setText(bundle.getString("pickDate"));
        if(bundle.containsKey("pickTime")) movingTime_text.setText(bundle.getString("pickTime"));
        if(bundle.containsKey("price")) price_edit.setText(bundle.getString("price"));
        if(bundle.containsKey("worktime")) worktime_edit.setText(bundle.getString("worktime"));
        if(bundle.containsKey("notice")) notice_edit.setText(bundle.getString("notice"));

    }

    private void addOrder(){
        String name = name_edit.getText().toString();

        String c_City = cCity.getText().toString();
        String c_District = cDistrict.getText().toString();
        String c_Address = cAddress.getText().toString();
        String con_cAddress = c_City + c_District + c_Address;

        String phone = phone_edit.getText().toString();

        String out_city = outCity.getText().toString();
        String out_district = outDistrict.getText().toString();
        String out_address = outAddress.getText().toString();
        String move_out = out_city + out_district + out_address;

        String in_city = inCity.getText().toString();
        String in_district = inDistrict.getText().toString();
        String in_address = inAddress.getText().toString();
        String move_in = in_city + in_district + in_address;

        String price = price_edit.getText().toString();
        String worktime = worktime_edit.getText().toString();
        String notice = notice_edit.getText().toString();
        String date = movingDate_text.getText().toString();
        date = date+" "+movingTime_text.getText().toString()+":00";

        switch(genderRG.getCheckedRadioButtonId()){
            case R.id.male_rbtn_AO:
                gender = "男";
                break;
            case R.id.female_rbtn_AO:
                gender = "女";
                break;
            case R.id.other_rbtn_AO:
                gender = "其他";
                break;
        }
        Log.i(TAG, "gender = "+gender);

        String function_name = "add_order";
        String company_id = getCompany_id(context);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", company_id)
                .add("member_name", name)
                .add("gender", gender)
                .add("contact_address", con_cAddress)
                .add("phone", phone)
                .add("additional", notice)
                .add("outcity", out_city)
                .add("outdistrict", out_district)
                .add("address1", out_address)
                .add("incity", in_city)
                .add("indistrict", in_district)
                .add("address2", in_address)
                .add("moving_date", date)
                .add("estimate_fee", price)
                .add("worktime", worktime)
                .add("furniture_data", furniture_data)
                .build();
        Log.i(TAG,"function_name: " + function_name +
                ", member_name: " + name +
                ", gender: " + gender +
                ", contact_address: " + con_cAddress +
                ", phone: " + phone +
                ", outcity: " + out_city+
                ", outdistrict: " + out_district+
                ", address1: " + out_address+
                ", incity: " + in_city +
                ", indistrict: " + in_district +
                ", address2: " + in_address +
                ", estimate_fee: " + price +
                ", worktime: " + worktime +
                ", additional: " + notice +
                ", moving_date: " + date +
                ", furniture_data: " + furniture_data);

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/functional.php")
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(context, "連線失敗", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                runOnUiThread(() -> {
                    if(responseData.contains("success"))
                        Toast.makeText(context, "新增訂單成功", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(context, "新增訂單失敗", Toast.LENGTH_LONG).show();
                });
                Log.d(TAG, "add_btn, responseData: " + responseData);
            }
        });
    }

    private boolean checkEmpty(){
        boolean check = false;

        String out_city = outCity.getText().toString();
        String out_district = outDistrict.getText().toString();
        String out_address = outAddress.getText().toString();
        String move_out = out_city + out_district + out_address;

        String in_city = inCity.getText().toString();
        String in_district = inDistrict.getText().toString();
        String in_address = inAddress.getText().toString();
        String move_in = in_city + in_district + in_address;

        if(TextUtils.isEmpty(name_edit.getText().toString())){
            name_edit.setError("請輸入姓名");
            check = true;
        }
        if(TextUtils.isEmpty(cCity.getText().toString())){
            cCity.setError("請輸入聯絡地址");
            check = true;
        }
        if(TextUtils.isEmpty(cDistrict.getText().toString())){
            cDistrict.setError("請輸入聯絡地址");
            check = true;
        }
        if(TextUtils.isEmpty(cAddress.getText().toString())){
            cAddress.setError("請輸入聯絡地址");
            check = true;
        }
        if(TextUtils.isEmpty(phone_edit.getText().toString())){
            phone_edit.setError("請輸入連絡電話");
            check = true;
        }
        if(phone_edit.getText().length()<10){
            phone_edit.setError("請輸入正確的電話號碼");
            check = true;
        }
        if(TextUtils.isEmpty(outCity.getText().toString())){
            outCity.setError("請輸入搬出地址");
            check = true;
        }
        if(TextUtils.isEmpty(outDistrict.getText().toString())){
            outDistrict.setError("請輸入搬出地址");
            check = true;
        }
        if(TextUtils.isEmpty(outAddress.getText().toString())){
            outAddress.setError("請輸入搬出地址");
            check = true;
        }

        if(TextUtils.isEmpty(inCity.getText().toString())){
            inCity.setError("請輸入搬入地址");
            check = true;
        }
        if(TextUtils.isEmpty(inDistrict.getText().toString())){
            inDistrict.setError("請輸入搬入地址");
            check = true;
        }
        if(TextUtils.isEmpty(inAddress.getText().toString())){
            inAddress.setError("請輸入搬入地址");
            check = true;
        }

        if(move_out.equals(move_in) && !move_out.isEmpty()){
            Toast.makeText(context, "搬入搬出地址相同", Toast.LENGTH_SHORT).show();
            check = true;
        }
        if(TextUtils.isEmpty(price_edit.getText().toString())){
            price_edit.setError("請輸入搬家費用");
            check = true;
        }
        if(TextUtils.isEmpty(worktime_edit.getText().toString())){
            worktime_edit.setError("請輸入預計工時");
            check = true;
        }
        if(TextUtils.isEmpty(movingDate_text.getText().toString())){
            movingDate_text.setError("請輸入日期");
            check = true;
        }
        if(contact_citySpin.getSelectedItem().equals("請選擇縣市")){
            Toast.makeText(context, "請選擇聯絡地址",Toast.LENGTH_SHORT);
            check = true;
        }
        if(contact_districtSpin.getSelectedItem().equals("請選擇縣市")){
            Toast.makeText(context, "請選擇聯絡地址",Toast.LENGTH_SHORT).show();
            check = true;
        }
        if(in_citySpin.getSelectedItem().equals("請選擇鄉鎮市區")){
            Toast.makeText(context, "請選擇搬入地址",Toast.LENGTH_SHORT).show();
            check = true;
        }
        if(in_districtSpin.getSelectedItem().equals("請選擇鄉鎮市區")){
            Toast.makeText(context, "請選擇搬入地址",Toast.LENGTH_SHORT).show();
            check = true;
        }

        return check;
    }
    private void setSpinner(){
        final String[] cities=
                {"請選擇縣市", "臺北市", "基隆市", "新北市",
                "宜蘭縣", "新竹市", "新竹縣", "桃園市",
                "苗栗縣", "臺中市", "彰化縣", "南投縣",
                "嘉義市", "嘉義縣", "雲林縣", "臺南市",
                "高雄市",  "屏東縣", "臺東縣", "花蓮縣",
                "澎湖縣", "金門縣", "連江縣"};
        final String[][] districts =
                {{"請選擇鄉鎮市區"},{"請選擇鄉鎮區","中正區", "大同區", "中山區", "萬華區", "信義區",
                "松山區", "大安區", "南港區", "北投區", "內湖區", "士林區", "文山區"},
                {"請選擇鄉鎮區","仁愛區", "中正區", "信義區", "中山區", "安樂區", "暖暖區", "七堵區"},
                {"請選擇鄉鎮區","板橋區", "新莊區", "泰山區", "林口區", "淡水區", "金山區", "八里區",
                        "萬里區", "石門區", "三芝區", "瑞芳區", "汐止區", "平溪區", "貢寮區", "雙溪區",
                        "深坑區", "石碇區", "新店區", "坪林區", "烏來區", "中和區", "永和區",
                        "土城區", "三峽區", "樹林區", "鶯歌區", "三重區", "蘆洲區", "五股區"},
                {"請選擇鄉鎮區","宜蘭市", "羅東鎮", "蘇澳鎮", "頭城鎮", "礁溪鄉", "壯圍鄉", "員山鄉", "冬山鄉",
                        "五結鄉", "三星鄉", "大同鄉", "南澳鄉"},
                {"請選擇鄉鎮區","東區", "北區", "香山區"},{"竹北市", "竹東鎮", "新埔鎮", "關西鎮", "峨眉鄉", "寶山鄉",
                "北埔鄉", "橫山鄉", "芎林鄉", "湖口鄉", "新豐鄉", "尖石鄉", "五峰鄉"},
                {"請選擇鄉鎮區","桃園區", "中壢區", "平鎮區", "八德區", "楊梅區", "蘆竹區", "龜山區", "龍潭區", "大溪區",
                        "大園區", "觀音區", "新屋區", "復興區"},
                {"請選擇鄉鎮區","苗栗市", "通霄鎮", "苑裡鎮", "竹南鎮", "頭份鎮", "後龍鎮", "卓蘭鎮", "西湖鄉", "頭屋鄉", "公館鄉", "銅鑼鄉", "三義鄉", "造橋鄉",
                        "三灣鄉", "南庄鄉", "大湖鄉", "獅潭鄉", "泰安鄉"},
                {"請選擇鄉鎮區","中區", "東區", "南區", "西區", "北區", "北屯區", "西屯區", "南屯區", "太平區", "大里區",
                        "霧峰區", "烏日區", "豐原區", "后里區", "東勢區", "石岡區", "新社區", "和平區",
                        "神岡區", "潭子區", "大雅區", "大肚區", "龍井區", "沙鹿區", "梧棲區", "清水區",
                        "大甲區", "外埔區", "大安區"},
                {"請選擇鄉鎮區","彰化市", "員林鎮", "和美鎮", "鹿港鎮", "溪湖鎮", "二林鎮", "田中鎮", "北斗鎮", "花壇鄉",
                        "芬園鄉", "大村鄉", "永靖鄉", "伸港鄉", "線西鄉", "福興鄉", "秀水鄉", "埔心鄉",
                        "埔鹽鄉", "大城鄉", "芳苑鄉", "竹塘鄉", "社頭鄉", "二水鄉", "田尾鄉", "埤頭鄉",
                        "溪州鄉"},
                {"請選擇鄉鎮區","南投市", "埔里鎮", "草屯鎮", "竹山鎮", "集集鎮", "名間鄉", "鹿谷鄉", "中寮鄉", "魚池鄉",
                        "國姓鄉", "水里鄉", "信義鄉", "仁愛鄉"},
                {"請選擇鄉鎮區","東區", "西區"},
                {"請選擇鄉鎮區","太保市", "朴子市", "布袋鎮", "大林鎮", "民雄鄉", "溪口鄉", "新港鄉", "六腳鄉", "東石鄉",
                        "義竹鄉", "鹿草鄉", "水上鄉", "中埔鄉", "竹崎鄉", "梅山鄉", "番路鄉", "大埔鄉",
                        "阿里山鄉"},
                {"請選擇鄉鎮區","斗六市", "斗南鎮", "虎尾鎮", "西螺鎮", "土庫鎮", "北港鎮", "莿桐鄉", "林內鄉", "古坑鄉",
                        "大埤鄉", "崙背鄉", "二崙鄉", "麥寮鄉", "臺西鄉", "東勢鄉", "褒忠鄉", "四湖鄉",
                        "口湖鄉", "水林鄉", "元長鄉"},
                {"請選擇鄉鎮區","中西區", "東區", "南區", "北區", "安平區", "安南區", "永康區", "歸仁區", "新化區",
                        "左鎮區", "玉井區", "楠西區", "南化區", "仁德區", "關廟區", "龍崎區", "官田區",
                        "麻豆區", "佳里區", "西港區", "七股區", "將軍區", "學甲區", "北門區", "新營區",
                        "後壁區", "白河區", "東山區", "六甲區", "下營區", "柳營區", "鹽水區", "善化區",
                        "大內區", "山上區", "新市區", "安定區"},
                {"請選擇鄉鎮區","楠梓區", "左營區", "鼓山區", "三民區", "鹽埕區", "前金區", "新興區", "苓雅區", "前鎮區",
                        "小港區", "旗津區", "鳳山區", "大寮區", "鳥松區", "林園區", "仁武區", "大樹區",
                        "大社區", "岡山區", "路竹區", "橋頭區", "梓官區", "彌陀區", "永安區", "燕巢區",
                        "田寮區", "阿蓮區", "茄萣區", "湖內區", "旗山區", "美濃區", "內門區", "杉林區",
                        "甲仙區", "六龜區", "茂林區", "桃源區", "那瑪夏區"},
                {"請選擇鄉鎮區","屏東市", "潮州鎮", "東港鎮", "恆春鎮", "萬丹鄉", "長治鄉", "麟洛鄉", "九如鄉",
                        "里港鄉", "鹽埔鄉", "高樹鄉", "萬巒鄉", "內埔鄉", "竹田鄉", "新埤鄉", "枋寮鄉",
                        "新園鄉", "崁頂鄉", "林邊鄉", "南州鄉", "佳冬鄉", "琉球鄉", "車城鄉", "滿州鄉",
                        "枋山鄉", "霧台鄉", "瑪家鄉", "泰武鄉", "來義鄉", "春日鄉", "獅子鄉", "牡丹鄉",
                        "三地門鄉"},
                {"請選擇鄉鎮區","臺東市", "成功鎮", "關山鎮", "長濱鄉", "海端鄉", "池上鄉", "東河鄉", "鹿野鄉", "延平鄉",
                        "卑南鄉", "金峰鄉", "大武鄉", "達仁鄉", "綠島鄉", "蘭嶼鄉", "太麻里鄉"},
                {"請選擇鄉鎮區","花蓮市", "鳳林鎮", "玉里鎮", "新城鄉", "吉安鄉", "壽豐鄉", "秀林鄉", "光復鄉", "豐濱鄉",
                        "瑞穗鄉", "萬榮鄉", "富里鄉", "卓溪鄉"},
                {"請選擇鄉鎮區","馬公市", "湖西鄉", "白沙鄉", "西嶼鄉", "望安鄉", "七美鄉"},
                {"請選擇鄉鎮區","金城鎮", "金湖鎮", "金沙鎮", "金寧鄉", "烈嶼鄉", "烏坵鄉"},
                {"請選擇鄉鎮區","南竿鄉", "北竿鄉", "莒光鄉", "東引鄉"}
        };
        ArrayAdapter<String> citiesList = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item, cities);
        contact_citySpin.setAdapter(citiesList);
        contact_citySpin.setSelection(0);
        in_citySpin.setAdapter(citiesList);
        in_citySpin.setSelection(0);
        contact_citySpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = contact_citySpin.getSelectedItemPosition();

                if(pos!=0){
                    cCity.setText(contact_citySpin.getSelectedItem().toString());
                    ArrayAdapter<String> districtList = new ArrayAdapter<>(context,
                            android.R.layout.simple_spinner_dropdown_item, districts[pos]);
                    contact_districtSpin.setAdapter(districtList);
                }else{
                    ArrayAdapter<String> districtList = new ArrayAdapter<>(context,
                            android.R.layout.simple_spinner_dropdown_item, districts[0]);
                    contact_districtSpin.setAdapter(districtList);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        contact_districtSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = contact_districtSpin.getSelectedItemPosition();
                if(pos!=0){
                    cDistrict.setText(contact_districtSpin.getSelectedItem().toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        in_citySpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = in_citySpin.getSelectedItemPosition();
                if(pos!=0){
                    inCity.setText(in_citySpin.getSelectedItem().toString());
                    ArrayAdapter<String> districtList = new ArrayAdapter<>(context,
                            android.R.layout.simple_spinner_dropdown_item, districts[pos]);
                    in_districtSpin.setAdapter(districtList);
                }else{
                    ArrayAdapter<String> districtList = new ArrayAdapter<>(context,
                            android.R.layout.simple_spinner_dropdown_item, districts[0]);
                    in_districtSpin.setAdapter(districtList);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        in_districtSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = in_districtSpin.getSelectedItemPosition();
                if(pos!=0){
                    inDistrict.setText(in_districtSpin.getSelectedItem().toString());
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private int monthToInt(String month){
        switch (month){
            case "JANUARY":
                return 1;
            case "FEBRUARY":
                return 2;
            case "MARCH":
                return 3;
            case "APRIL":
                return 4;
            case "MAY":
                return 5;
            case "JUNE":
                return 6;
            case "JULY":
                return 7;
            case "AUGUST":
                return 8;
            case "SEPTEMBER":
                return 9;
            case "OCTOBER":
                return 10;
            case "NOVEMBER":
                return 11;
            case "DECEMBER":
                return 12;
            default:
                return 0;
        }
    }

    private void linking(){
        name_edit = findViewById(R.id.name_AO);
        cCity = findViewById(R.id.cCity);
        cDistrict = findViewById(R.id.cDistrict);
        cAddress = findViewById(R.id.cAddress);

        phone_edit = findViewById(R.id.phone_AO);

        outCity = findViewById(R.id.out_city);
        outDistrict = findViewById(R.id.out_district);
        outAddress = findViewById(R.id.out_address);

        inCity = findViewById(R.id.in_city);
        inDistrict = findViewById(R.id.in_district);;
        inAddress = findViewById(R.id.in_address);

        editFurnitureBtn = findViewById(R.id.edit_furniture_btn_AO);
        furniture_list = findViewById(R.id.furniture_list_AO);
        price_edit = findViewById(R.id.price_AO);
        worktime_edit = findViewById(R.id.worktime_AO);
        notice_edit = findViewById(R.id.notice_AO);
        movingDate_text = findViewById(R.id.pickDate_AO);
        movingTime_text = findViewById(R.id.pictTime_AO);
        genderRG = findViewById(R.id.genderRG_AO);
        addOrderBtn = findViewById(R.id.addOrder_btn_AO);
        contact_citySpin = findViewById(R.id.cCity_spin);
        contact_districtSpin = findViewById(R.id.cDistrcit_spin);
        in_citySpin = findViewById(R.id.inCity_spin);
        in_districtSpin = findViewById(R.id.inDistrict_spin);
    }

    private void globalNav(){
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        //底下nav
        valuation_btn.setOnClickListener(v -> {
            Intent valuation_intent = new Intent(Add_Order.this, Valuation.class);
            startActivity(valuation_intent);
        });
        order_btn.setOnClickListener(v -> {
            Intent order_intent = new Intent(Add_Order.this, Order.class);
            startActivity(order_intent);
        });
        calendar_btn.setOnClickListener(v -> {
            Intent calender_intent = new Intent(Add_Order.this, Calendar.class);
            startActivity(calender_intent);
        });
        system_btn.setOnClickListener(v -> {
            Intent system_intent = new Intent(Add_Order.this, System.class);
            startActivity(system_intent);
        });
        setting_btn.setOnClickListener(v -> {
            Intent setting_intent = new Intent(Add_Order.this, Setting.class);
            startActivity(setting_intent);
        });
    }
}
