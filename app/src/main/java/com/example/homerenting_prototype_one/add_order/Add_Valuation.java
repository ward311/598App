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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.system.System;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.order.Order;
import com.example.homerenting_prototype_one.setting.Setting;
import com.example.homerenting_prototype_one.valuation.Valuation;
import com.example.homerenting_prototype_one.valuation.Valuation_Booking;

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

public class Add_Valuation extends AppCompatActivity {

    EditText nameText,  phoneText, noticeText;
    EditText cCityText, cDistrictText, cAddressText;
    EditText outCityText, outDistrictText, outAddressText;
    EditText inCityText, inDistrictText, inAddressText;
    TextView dateText, timeText;
    RadioGroup genderRG;
    Button addBtn;
    Spinner contact_citySpin, contact_districtSpin;
    Spinner in_citySpin, in_districtSpin;

    String gender = "男";
    String time, time2;

    GregorianCalendar calendar = new GregorianCalendar();

    OkHttpClient okHttpClient = new OkHttpClient();

    Context context = Add_Valuation.this;
    String TAG = "Add_Valuation";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__valuation);

        linking();
        setSpinner();
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Taipei"));
        Log.d(TAG, "now: "+now.getYear()+"-"+monthToInt(String.valueOf(now.getMonth()))+"-"+now.getDayOfMonth());

        String now_month = String.valueOf(monthToInt(String.valueOf(now.getMonth())));
        if(monthToInt(String.valueOf(now.getMonth())) < 10) now_month = "0"+now_month;
        dateText.setText(now.getYear()+"-"+now_month+"-"+now.getDayOfMonth());
        dateText.setOnClickListener(v -> {
            DatePickerDialog datePicker;
            datePicker = new DatePickerDialog( Add_Valuation.this, (view, year, month, dayOfMonth) -> {
                if(year<now.getYear() ||
                        (year >= now.getYear() && (month+1)<monthToInt(String.valueOf(now.getMonth()))) ||
                        (year >= now.getYear() && (month+1)>=monthToInt(String.valueOf(now.getMonth())) && dayOfMonth<now.getDayOfMonth())
                    ) {
                    Toast.makeText(context, "請勿選擇過去的日期", Toast.LENGTH_SHORT).show();
                    return;
                }
                String monthStr = String.valueOf(month+1);
                if(month+1 < 10) monthStr = "0"+monthStr;
                String dayStr = String.valueOf(dayOfMonth);
                if(dayOfMonth < 10) dayStr = "0"+dayStr;
                dateText.setText(year + "-" + monthStr + "-" + dayStr);
            },calendar.get( GregorianCalendar.YEAR ),calendar.get( GregorianCalendar.MONTH ),calendar.get( GregorianCalendar.DAY_OF_MONTH));
            datePicker.getDatePicker().setMinDate(new Date().getTime());
            datePicker.show();
        });

        timeText.setText(now.getHour()+":00");
        time = now.getHour()+":00";
        timeText.setOnClickListener(v -> {
            TimePickerDialog time_picker = new TimePickerDialog( context, (view, hourOfDay, minute) -> {
                String mm = String.valueOf(minute);
                if(minute < 10) mm = "0"+mm;
                String hh = String.valueOf(hourOfDay);
                if(hourOfDay < 10) hh = "0"+hh;
                timeText.setText(hh+":"+mm);
                time = hourOfDay+":"+mm;
                time2 = (hourOfDay+1)+":"+mm;
            },calendar.get(GregorianCalendar.DAY_OF_MONTH ),calendar.get(GregorianCalendar.MINUTE ),true);
            time_picker.show();
        });
        cCityText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String cCity = cCityText.getText().toString();
                outCityText.setText(cCity);
            }
        });
        cDistrictText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String cDistrict = cDistrictText.getText().toString();
                outDistrictText.setText(cDistrict);
            }
        });
        cAddressText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {

                String cAddress = cAddressText.getText().toString();
                outAddressText.setText(cAddress);

            }
        });

        addBtn.setOnClickListener(v -> {
            if(checkEmpty()) return;

            add_valuation();

            Handler handler = new Handler();
            handler.postDelayed(() -> {
                Intent finish_valuation_intent = new Intent();
                finish_valuation_intent.setClass(context, Valuation_Booking.class);
                startActivity(finish_valuation_intent);
            }, 1000);
        });

        globleBtn();
    }

    private boolean checkEmpty(){
        boolean check = false;
        if(TextUtils.isEmpty(nameText.getText().toString())){
            nameText.setError("請輸入姓名");
            check = true;
        }
        if(TextUtils.isEmpty(phoneText.getText().toString())){
            phoneText.setError("請輸入電話");
            check = true;
        }
        if(TextUtils.isEmpty(cCityText.getText().toString())){
            cCityText.setError("請輸入聯絡地址");
            check = true;
        }
        if(TextUtils.isEmpty(cDistrictText.getText().toString())){
            cDistrictText.setError("請輸入聯絡地址");
            check = true;
        }
        if(TextUtils.isEmpty(cAddressText.getText().toString())){
            cAddressText.setError("請輸入聯絡地址");
            check = true;
        }
        if(TextUtils.isEmpty(outCityText.getText().toString())){
            outCityText.setError("請輸入搬出地址");
            check = true;
        }
        if(TextUtils.isEmpty(outDistrictText.getText().toString())){
            outDistrictText.setError("請輸入搬出地址");
            check = true;
        }
        if(TextUtils.isEmpty(outAddressText.getText().toString())){
            outAddressText.setError("請輸入搬出地址");
            check = true;
        }
        if(TextUtils.isEmpty(inCityText.getText().toString())){
            inCityText.setError("請輸入搬入地址");
            check = true;
        }
        if(TextUtils.isEmpty(inDistrictText.getText().toString())){
            inDistrictText.setError("請輸入搬入地址");
            check = true;
        }
        if(TextUtils.isEmpty(inAddressText.getText().toString())){
            inAddressText.setError("請輸入搬入地址");
            check = true;
        }

        if(TextUtils.isEmpty(dateText.getText().toString())){
            dateText.setError("請選擇日期");
            check = true;
        }
        if(time == null || time.equals("")){
            timeText.setError("請選擇時間");
            check = true;
        }
        if(contact_citySpin.getSelectedItem().equals("請選擇縣市")){
            Toast.makeText(context, "請選擇聯絡地址",Toast.LENGTH_SHORT);
        }
        if(contact_districtSpin.getSelectedItem().equals("請選擇鄉鎮市區")){
            Toast.makeText(context, "請選擇聯絡地址",Toast.LENGTH_SHORT).show();
        }
        if(in_citySpin.getSelectedItem().equals("請選擇縣市")){
            Toast.makeText(context, "請選擇搬入地址",Toast.LENGTH_SHORT).show();
        }
        if(in_districtSpin.getSelectedItem().equals("請選擇鄉鎮市區")){
            Toast.makeText(context, "請選擇搬入地址",Toast.LENGTH_SHORT).show();
        }

        return check;
    }

    private void add_valuation(){
        String name = nameText.getText().toString();
        String cCity = cCityText.getText().toString();
        String cDistrict = cDistrictText.getText().toString();
        String cAddress = cAddressText.getText().toString();
        String commSite = cCity + cDistrict + cAddress;
        String phone = phoneText.getText().toString();
        String outCity = outCityText.getText().toString();
        String outDistrict = outDistrictText.getText().toString();
        String outAddress = outAddressText.getText().toString();
        String outSite = outCity + outDistrict + outAddress;
        String inCity = inCityText.getText().toString();
        String inDistrict = inDistrictText.getText().toString();
        String inAddress = inAddressText.getText().toString();
        String inCitySite = inCity + inDistrict + inAddress;

        String notice = noticeText.getText().toString();
        String date = dateText.getText().toString();
        String valuation_time = time+"~"+time2;

        switch(genderRG.getCheckedRadioButtonId()){
            case R.id.male_rbtn_AV:
                gender = "男";
                break;
            case R.id.female_rbtn_AV:
                gender = "女";
                break;
            case R.id.other_rbtn_AV:
                gender = "其他";
                break;
        }
        Log.i(TAG, "gender = "+gender);

        String function_name = "add_valuation";
        String company_id = getCompany_id(context);
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", company_id)
                .add("member_name", name)
                .add("gender", gender)
                .add("contact_address", commSite)
                .add("phone", phone)
                .add("outcity", outCity)
                .add("outdistrict", outDistrict)
                .add("address1", outAddress)
                .add("incity", inCity)
                .add("indistrict", inDistrict)
                .add("address2", inAddress)
                .add("additional", notice)
                .add("valuation_date", date)
                .add("valuation_time", valuation_time)
                .build();
        Log.i(TAG,"function_name: " + function_name +
                ", member_name: " + name +
                ", gender: " + gender +
                ", contact_address: " + commSite +
                ", phone: " + phone +
                ", cCity: " + cCity+
                ", cDistrict: " + cDistrict +
                ", cAddress: " + cAddress +
                ", inCity: " + inCity +
                ", inDistrict: " + inDistrict +
                ", inAddress: " + inAddress +
                ", additional: " + notice +
                ", valuation_date: " + date +
                ", valuation_time: " + valuation_time);

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/functional.php")
                .post(body)
                .build();

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
                runOnUiThread(() -> {
                    if(responseData.contains("success"))
                        Toast.makeText(context, "新增估價單成功", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(context, "新增估價單失敗", Toast.LENGTH_LONG).show();
                });
                Log.d(TAG, "add_btn, responseData: " + responseData);
            }
        });
    }

    private void globleBtn(){
        ImageButton valuation_btn = findViewById(R.id.valuationBlue_Btn);
        ImageButton order_btn = findViewById(R.id.order_imgBtn);
        ImageButton calendar_btn = findViewById(R.id.calendar_imgBtn);
        ImageButton system_btn = findViewById(R.id.system_imgBtn);
        ImageButton setting_btn = findViewById(R.id.setting_imgBtn);

        //底下nav
        valuation_btn.setOnClickListener(v -> {
            Intent valuation_intent = new Intent(Add_Valuation.this, Valuation.class);
            startActivity(valuation_intent);
        });
        order_btn.setOnClickListener(v -> {
            Intent order_intent = new Intent(Add_Valuation.this, Order.class);
            startActivity(order_intent);
        });
        calendar_btn.setOnClickListener(v -> {
            Intent calender_intent = new Intent(Add_Valuation.this, Calendar.class);
            startActivity(calender_intent);
        });
        system_btn.setOnClickListener(v -> {
            Intent system_intent = new Intent(Add_Valuation.this, System.class);
            startActivity(system_intent);
        });
        setting_btn.setOnClickListener(v -> {
            Intent setting_intent = new Intent(Add_Valuation.this, Setting.class);
            startActivity(setting_intent);
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

    private void setSpinner(){
        final String[] cities= {"請選擇縣市", "臺北市", "基隆市", "新北市",
                "宜蘭縣", "新竹市", "新竹縣", "桃園市",
                "苗栗縣", "臺中市", "彰化縣", "南投縣",
                "嘉義市", "嘉義縣", "雲林縣", "臺南市",
                "高雄市",  "屏東縣", "臺東縣", "花蓮縣",
        "澎湖縣", "金門縣", "連江縣"};
        final String[][] districts = {{"請選擇鄉鎮市區"}, {"中正區", "大同區", "中山區", "萬華區", "信義區",
                "松山區", "大安區", "南港區", "北投區", "內湖區", "士林區", "文山區"},
                {"請選擇縣市","仁愛區", "中正區", "信義區", "中山區", "安樂區", "暖暖區", "七堵區"},
                {"請選擇縣市","板橋區", "新莊區", "泰山區", "林口區", "淡水區", "金山區", "八里區",
                "萬里區", "石門區", "三芝區", "瑞芳區", "汐止區", "平溪區", "貢寮區", "雙溪區",
                        "深坑區", "石碇區", "新店區", "坪林區", "烏來區", "中和區", "永和區",
                "土城區", "三峽區", "樹林區", "鶯歌區", "三重區", "蘆洲區", "五股區"},
                {"請選擇縣市","宜蘭市", "羅東鎮", "蘇澳鎮", "頭城鎮", "礁溪鄉", "壯圍鄉", "員山鄉", "冬山鄉",
                        "五結鄉", "三星鄉", "大同鄉", "南澳鄉"},
                {"請選擇縣市","東區", "北區", "香山區"},{"竹北市", "竹東鎮", "新埔鎮", "關西鎮", "峨眉鄉", "寶山鄉",
                "北埔鄉", "橫山鄉", "芎林鄉", "湖口鄉", "新豐鄉", "尖石鄉", "五峰鄉"},
                {"請選擇縣市","桃園區", "中壢區", "平鎮區", "八德區", "楊梅區", "蘆竹區", "龜山區", "龍潭區", "大溪區",
                        "大園區", "觀音區", "新屋區", "復興區"},
                {"請選擇縣市","苗栗市", "通霄鎮", "苑裡鎮", "竹南鎮", "頭份鎮", "後龍鎮", "卓蘭鎮", "西湖鄉", "頭屋鄉", "公館鄉", "銅鑼鄉", "三義鄉", "造橋鄉",
                        "三灣鄉", "南庄鄉", "大湖鄉", "獅潭鄉", "泰安鄉"},
                {"請選擇縣市","中區", "東區", "南區", "西區", "北區", "北屯區", "西屯區", "南屯區", "太平區", "大里區",
                        "霧峰區", "烏日區", "豐原區", "后里區", "東勢區", "石岡區", "新社區", "和平區",
                        "神岡區", "潭子區", "大雅區", "大肚區", "龍井區", "沙鹿區", "梧棲區", "清水區",
                        "大甲區", "外埔區", "大安區"},
                {"請選擇縣市","彰化市", "員林鎮", "和美鎮", "鹿港鎮", "溪湖鎮", "二林鎮", "田中鎮", "北斗鎮", "花壇鄉",
                        "芬園鄉", "大村鄉", "永靖鄉", "伸港鄉", "線西鄉", "福興鄉", "秀水鄉", "埔心鄉",
                        "埔鹽鄉", "大城鄉", "芳苑鄉", "竹塘鄉", "社頭鄉", "二水鄉", "田尾鄉", "埤頭鄉",
                        "溪州鄉"},
                {"請選擇縣市","南投市", "埔里鎮", "草屯鎮", "竹山鎮", "集集鎮", "名間鄉", "鹿谷鄉", "中寮鄉", "魚池鄉",
                        "國姓鄉", "水里鄉", "信義鄉", "仁愛鄉"},
                {"請選擇縣市","東區", "西區"},
                {"請選擇縣市","太保市", "朴子市", "布袋鎮", "大林鎮", "民雄鄉", "溪口鄉", "新港鄉", "六腳鄉", "東石鄉",
                        "義竹鄉", "鹿草鄉", "水上鄉", "中埔鄉", "竹崎鄉", "梅山鄉", "番路鄉", "大埔鄉",
                        "阿里山鄉"},
                {"請選擇縣市","斗六市", "斗南鎮", "虎尾鎮", "西螺鎮", "土庫鎮", "北港鎮", "莿桐鄉", "林內鄉", "古坑鄉",
                        "大埤鄉", "崙背鄉", "二崙鄉", "麥寮鄉", "臺西鄉", "東勢鄉", "褒忠鄉", "四湖鄉",
                        "口湖鄉", "水林鄉", "元長鄉"},
                {"請選擇縣市","中西區", "東區", "南區", "北區", "安平區", "安南區", "永康區", "歸仁區", "新化區",
                        "左鎮區", "玉井區", "楠西區", "南化區", "仁德區", "關廟區", "龍崎區", "官田區",
                        "麻豆區", "佳里區", "西港區", "七股區", "將軍區", "學甲區", "北門區", "新營區",
                        "後壁區", "白河區", "東山區", "六甲區", "下營區", "柳營區", "鹽水區", "善化區",
                        "大內區", "山上區", "新市區", "安定區"},
                {"請選擇縣市","楠梓區", "左營區", "鼓山區", "三民區", "鹽埕區", "前金區", "新興區", "苓雅區", "前鎮區",
                        "小港區", "旗津區", "鳳山區", "大寮區", "鳥松區", "林園區", "仁武區", "大樹區",
                        "大社區", "岡山區", "路竹區", "橋頭區", "梓官區", "彌陀區", "永安區", "燕巢區",
                        "田寮區", "阿蓮區", "茄萣區", "湖內區", "旗山區", "美濃區", "內門區", "杉林區",
                        "甲仙區", "六龜區", "茂林區", "桃源區", "那瑪夏區"},
                {"請選擇縣市","屏東市", "潮州鎮", "東港鎮", "恆春鎮", "萬丹鄉", "長治鄉", "麟洛鄉", "九如鄉",
                        "里港鄉", "鹽埔鄉", "高樹鄉", "萬巒鄉", "內埔鄉", "竹田鄉", "新埤鄉", "枋寮鄉",
                        "新園鄉", "崁頂鄉", "林邊鄉", "南州鄉", "佳冬鄉", "琉球鄉", "車城鄉", "滿州鄉",
                        "枋山鄉", "霧台鄉", "瑪家鄉", "泰武鄉", "來義鄉", "春日鄉", "獅子鄉", "牡丹鄉",
                        "三地門鄉"},
                {"請選擇縣市","臺東市", "成功鎮", "關山鎮", "長濱鄉", "海端鄉", "池上鄉", "東河鄉", "鹿野鄉", "延平鄉",
                        "卑南鄉", "金峰鄉", "大武鄉", "達仁鄉", "綠島鄉", "蘭嶼鄉", "太麻里鄉"},
                {"請選擇縣市","花蓮市", "鳳林鎮", "玉里鎮", "新城鄉", "吉安鄉", "壽豐鄉", "秀林鄉", "光復鄉", "豐濱鄉",
                        "瑞穗鄉", "萬榮鄉", "富里鄉", "卓溪鄉"},
                {"請選擇縣市","馬公市", "湖西鄉", "白沙鄉", "西嶼鄉", "望安鄉", "七美鄉"},
                {"請選擇縣市","金城鎮", "金湖鎮", "金沙鎮", "金寧鄉", "烈嶼鄉", "烏坵鄉"},
                {"請選擇縣市","南竿鄉", "北竿鄉", "莒光鄉", "東引鄉"}
        };
        ArrayAdapter<String> citiesList = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item, cities);
        contact_citySpin.setAdapter(citiesList);
        contact_citySpin.setSelection(0);
        in_citySpin.setSelection(0);
        in_citySpin.setAdapter(citiesList);

        contact_citySpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = contact_citySpin.getSelectedItemPosition();
                if(pos!=0){
                    cCityText.setText(contact_citySpin.getSelectedItem().toString());
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
                    cDistrictText.setText(contact_districtSpin.getSelectedItem().toString());
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
                    inCityText.setText(in_citySpin.getSelectedItem().toString());
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
                    inDistrictText.setText(in_districtSpin.getSelectedItem().toString());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void linking(){
        nameText = findViewById(R.id.name_AV);
        cAddressText = findViewById(R.id.c_city_edit);
        phoneText = findViewById(R.id.phone_AV);
        cCityText = findViewById(R.id.c_city_edit);
        cDistrictText = findViewById(R.id.c_district_edit);
        cAddressText = findViewById(R.id.c_address_edit);
        outCityText = findViewById(R.id.out_city_edit);
        outDistrictText = findViewById(R.id.out_district_edit);
        outAddressText = findViewById(R.id.out_address_edit);
        inCityText = findViewById(R.id.in_city_edit);
        inDistrictText = findViewById(R.id.in_district_edit);
        inAddressText = findViewById(R.id.in_address_edit);
        noticeText = findViewById(R.id.notice_editText_AV);
        dateText = findViewById(R.id.pickDate_AV);
        timeText = findViewById(R.id.pictTime_AV);
        genderRG = findViewById(R.id.genderRG_AV);
        addBtn = findViewById(R.id.add_valuation_btn_AV);
        contact_citySpin = findViewById(R.id.con_city_spinner);
        contact_districtSpin = findViewById(R.id.con_district_spinner);
        in_citySpin = findViewById(R.id.incity_spinner);
        in_districtSpin = findViewById(R.id.indistrict_spinner);
    }
}
