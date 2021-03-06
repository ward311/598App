package com.example.homerenting_prototype_one.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.homerenting_prototype_one.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

public class SessionManager {
    private String TAG = "SessionManager";

    SharedPreferences pref ;

    Editor editor;

    Context _context;

    @SuppressLint("StaticFieldLeak")
    private static SessionManager INSTANCE = null;

    private static User user = null;

    @SuppressLint("CommitPrefEdits")
    private SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences("MovingAppPref", Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static SessionManager getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = new SessionManager(context);
        }
        return INSTANCE;
    }

    public void createLoginSession(String id, String userJsonObjStr){
        editor.putBoolean("IsLoggedIn" , true);
        editor.putString("user_id", id);
        editor.putString("user", userJsonObjStr);
        editor.commit();
        editor.apply();
    }

    public User getUserDetail(){
        if(user == null) user = new User();
        String userJsonObjStr = pref.getString("user", null);
        if(userJsonObjStr != null){
            try {
                JSONObject obj = new JSONObject(userJsonObjStr);
                user.setId(obj.getString("user_id"));
                user.setCompany_id(obj.getString("company_id"));
                user.setName(obj.getString("user_name"));
                user.setEmail(obj.getString("user_email"));
                user.setPhone(obj.getString("user_phone"));
                user.setToken(obj.getString("token"));
                user.setVerifyCode(obj.getString("verify_code"));
                user.setTitle(obj.getString("title"));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(TAG, "user data is not json type");
            }
        }
        return user;
    }

    public void logout(){
        user = null;
    }

    public boolean isLogin(){
        return user != null;
    }
}
