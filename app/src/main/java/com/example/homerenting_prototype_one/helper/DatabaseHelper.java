package com.example.homerenting_prototype_one.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



import static com.example.homerenting_prototype_one.model.TableContract.CompanyTable.SQL_CREATE_COMPANY;
import static com.example.homerenting_prototype_one.model.TableContract.CompanyTable.SQL_DELETE_COMPANY;
import static com.example.homerenting_prototype_one.model.TableContract.ChooseTable.SQL_CREATE_CHOOSE;
import static com.example.homerenting_prototype_one.model.TableContract.ChooseTable.SQL_DELETE_CHOOSE;
import static com.example.homerenting_prototype_one.model.TableContract.CommentsTable.SQL_CREATE_COMMENTS;
import static com.example.homerenting_prototype_one.model.TableContract.CommentsTable.SQL_DELETE_COMMENTS;
import static com.example.homerenting_prototype_one.model.TableContract.StaffTable.SQL_CREATE_STAFF;
import static com.example.homerenting_prototype_one.model.TableContract.StaffTable.SQL_DELETE_STAFF;
import static com.example.homerenting_prototype_one.model.TableContract.VehicleTable.SQL_CREATE_VEHICLE;
import static com.example.homerenting_prototype_one.model.TableContract.VehicleTable.SQL_DELETE_VEHICLE;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "598moving";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) throws SQLiteCantOpenDatabaseException {
        db.execSQL(SQL_CREATE_COMPANY);
        /*db.execSQL(SQL_CREATE_CHOOSE);
        db.execSQL(SQL_CREATE_COMMENTS);
        db.execSQL(SQL_CREATE_STAFF);
        db.execSQL(SQL_CREATE_VEHICLE);*/
    }

    @Override /*此方法會將舊TABLE全部刪除再新增新的TABLE，資料庫版本要更新*/
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion){
            db.beginTransaction();
            boolean success = false;

            switch (oldVersion){
                case 1:
                 /*更新table欄位SQL指令
                 *
                 * UPDATE指令
                 *
                 * */
                 oldVersion++; //資料庫版本更新
                 success = true;
                 break;
            }
             if(success){
                 db.setTransactionSuccessful();
             }
             db.endTransaction();
        }
        else{
            onCreate(db);
        }
        /*db.execSQL(SQL_DELETE_COMPANY);
        db.execSQL(SQL_DELETE_CHOOSE);
        db.execSQL(SQL_CREATE_COMMENTS);
        db.execSQL(SQL_DELETE_COMMENTS);
        db.execSQL(SQL_DELETE_STAFF);
        db.execSQL(SQL_DELETE_VEHICLE);*/

    }


    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
    }

    public void onClose(SQLiteDatabase db){
        super.close();
    }
}
