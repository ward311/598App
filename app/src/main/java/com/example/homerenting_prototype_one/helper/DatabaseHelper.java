package com.example.homerenting_prototype_one.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.homerenting_prototype_one.model.TableContract.CompanyTable.SQL_CREATE_COMPANY;
import static com.example.homerenting_prototype_one.model.TableContract.CompanyTable.SQL_DELETE_COMPANY;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "598moving";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_COMPANY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_COMPANY);
        onCreate(db);
    }
}
