package com.example.homerenting_prototype_one.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.homerenting_prototype_one.model.TableContract;
import com.example.homerenting_prototype_one.setting.Evaluation_Detail;

import static com.example.homerenting_prototype_one.model.TableContract.CompanyTable.SQL_CREATE_COMPANY;
import static com.example.homerenting_prototype_one.model.TableContract.CompanyTable.SQL_DELETE_COMPANY;
import static com.example.homerenting_prototype_one.model.TableContract.ChooseTable.SQL_CREATE_CHOOSE;
import static com.example.homerenting_prototype_one.model.TableContract.ChooseTable.SQL_DELETE_CHOOSE;
import static com.example.homerenting_prototype_one.model.TableContract.CommentsTable.SQL_CREATE_COMMENTS;
import static com.example.homerenting_prototype_one.model.TableContract.CommentsTable.SQL_DELETE_COMMENTS;
import static com.example.homerenting_prototype_one.model.TableContract.OrdersTable.SQL_CREATE_ORDERS;
import static com.example.homerenting_prototype_one.model.TableContract.OrdersTable.SQL_DELETE_ORDERS;
import static com.example.homerenting_prototype_one.model.TableContract.StaffTable.SQL_CREATE_STAFF;
import static com.example.homerenting_prototype_one.model.TableContract.StaffTable.SQL_DELETE_STAFF;
import static com.example.homerenting_prototype_one.model.TableContract.VehicleTable.SQL_CREATE_VEHICLE;
import static com.example.homerenting_prototype_one.model.TableContract.VehicleTable.SQL_DELETE_VEHICLE;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "598moving.db";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) throws SQLiteCantOpenDatabaseException {
        db.execSQL("PRAGMA foreign_keys = ON");
        db.execSQL(SQL_CREATE_COMPANY);
        db.execSQL(SQL_CREATE_COMMENTS);
        //db.execSQL(SQL_CREATE_ORDERS);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(SQL_DELETE_COMPANY);
        db.execSQL(SQL_DELETE_COMMENTS);
        //db.execSQL(SQL_DELETE_ORDERS);
        onCreate(db);

    }

}
