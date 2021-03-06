package com.example.homerenting_prototype_one.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.homerenting_prototype_one.model.TableContract;


import static com.example.homerenting_prototype_one.model.TableContract.AnnouncementCompanyTable.SQL_CREATE_ANNOUNCEMENT_COMPANY;
import static com.example.homerenting_prototype_one.model.TableContract.AnnouncementCompanyTable.SQL_DELETE_ANNOUNCEMENT_COMPANY;
import static com.example.homerenting_prototype_one.model.TableContract.AnnouncementTable.SQL_CREATE_ANNOUNCEMENT;
import static com.example.homerenting_prototype_one.model.TableContract.AnnouncementTable.SQL_DELETE_ANNOUNCEMENT;
import static com.example.homerenting_prototype_one.model.TableContract.CompanyTable.SQL_CREATE_COMPANY;
import static com.example.homerenting_prototype_one.model.TableContract.CompanyTable.SQL_DELETE_COMPANY;
import static com.example.homerenting_prototype_one.model.TableContract.ChooseTable.SQL_CREATE_CHOOSE;
import static com.example.homerenting_prototype_one.model.TableContract.ChooseTable.SQL_DELETE_CHOOSE;
import static com.example.homerenting_prototype_one.model.TableContract.CommentsTable.SQL_CREATE_COMMENTS;
import static com.example.homerenting_prototype_one.model.TableContract.CommentsTable.SQL_DELETE_COMMENTS;
import static com.example.homerenting_prototype_one.model.TableContract.DiscountTable.SQL_CREATE_DISCOUNT;
import static com.example.homerenting_prototype_one.model.TableContract.DiscountTable.SQL_DELETE_DISCOUNT;
import static com.example.homerenting_prototype_one.model.TableContract.FurnitureTable.SQL_CREATE_FURNITURE;
import static com.example.homerenting_prototype_one.model.TableContract.FurnitureTable.SQL_DELETE_FURNITURE;
import static com.example.homerenting_prototype_one.model.TableContract.MemberTable.SQL_CREATE_MEMBER;
import static com.example.homerenting_prototype_one.model.TableContract.MemberTable.SQL_DELETE_MEMBER;
import static com.example.homerenting_prototype_one.model.TableContract.OrdersTable.SQL_CREATE_ORDERS;
import static com.example.homerenting_prototype_one.model.TableContract.OrdersTable.SQL_DELETE_ORDERS;
import static com.example.homerenting_prototype_one.model.TableContract.PeriodDiscountTable.SQL_CREATE_PERIOD_DISCOUNT;
import static com.example.homerenting_prototype_one.model.TableContract.PeriodDiscountTable.SQL_DELETE_PERIOD_DISCOUNT;
import static com.example.homerenting_prototype_one.model.TableContract.ServiceClassTable.SQL_CREATE_SERVICE_CLASS;
import static com.example.homerenting_prototype_one.model.TableContract.ServiceClassTable.SQL_DELETE_SERVICE_CLASS;
import static com.example.homerenting_prototype_one.model.TableContract.ServiceClassTable.getServiceClass;
import static com.example.homerenting_prototype_one.model.TableContract.ServiceItemTable.SQL_CREATE_SERVICE_ITEM;
import static com.example.homerenting_prototype_one.model.TableContract.ServiceItemTable.SQL_DELETE_SERVICE_ITEM;
import static com.example.homerenting_prototype_one.model.TableContract.StaffAssignmentTable.SQL_CREATE_STAFF_ASSIGNMENT;
import static com.example.homerenting_prototype_one.model.TableContract.StaffAssignmentTable.SQL_DELETE_STAFF_ASSIGNMENT;
import static com.example.homerenting_prototype_one.model.TableContract.StaffLeaveTable.SQL_CREATE_STAFF_LEAVE;
import static com.example.homerenting_prototype_one.model.TableContract.StaffLeaveTable.SQL_DELETE_SQL_STAFF_LEAVE;
import static com.example.homerenting_prototype_one.model.TableContract.StaffTable.SQL_CREATE_STAFF;
import static com.example.homerenting_prototype_one.model.TableContract.StaffTable.SQL_DELETE_STAFF;
import static com.example.homerenting_prototype_one.model.TableContract.VehicleMaintainTable.SQL_CREATE_VEHICLE_MAINTAIN;
import static com.example.homerenting_prototype_one.model.TableContract.VehicleMaintainTable.SQL_DELETE_VEHICLE_MAINTAIN;
import static com.example.homerenting_prototype_one.model.TableContract.VehicleTable.SQL_CREATE_VEHICLE;
import static com.example.homerenting_prototype_one.model.TableContract.VehicleTable.SQL_DELETE_VEHICLE;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "598moving";
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    public static DatabaseHelper databaseHelper;
    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) throws SQLiteCantOpenDatabaseException {
        Log.d(TAG, "create sqlite tables");
//        db.execSQL("PRAGMA foreign_keys = ON");
        db.execSQL(SQL_CREATE_ANNOUNCEMENT);
        db.execSQL(SQL_CREATE_ANNOUNCEMENT_COMPANY);
        db.execSQL(SQL_CREATE_MEMBER);
        db.execSQL(SQL_CREATE_COMPANY);
        db.execSQL(SQL_CREATE_ORDERS);
        db.execSQL(SQL_CREATE_COMMENTS);
        db.execSQL(SQL_CREATE_SERVICE_CLASS);
        db.execSQL(SQL_CREATE_SERVICE_ITEM);
        db.execSQL(SQL_CREATE_STAFF);
        db.execSQL(SQL_CREATE_STAFF_ASSIGNMENT);
//      db.execSQL(SQL_CREATE_STAFF_LEAVE);
        db.execSQL(SQL_CREATE_VEHICLE);
        db.execSQL(SQL_CREATE_VEHICLE_MAINTAIN);
        db.execSQL(SQL_CREATE_FURNITURE);
        db.execSQL(SQL_CREATE_DISCOUNT);
        db.execSQL(SQL_CREATE_PERIOD_DISCOUNT);
        db.execSQL(SQL_CREATE_CHOOSE);
    }

    @Override /*??????????????????TABLE???????????????????????????TABLE???????????????????????????*/
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade");
        db.execSQL(SQL_DELETE_ANNOUNCEMENT);
        db.execSQL(SQL_DELETE_ANNOUNCEMENT_COMPANY);
        db.execSQL(SQL_DELETE_MEMBER);
        db.execSQL(SQL_DELETE_COMPANY);
        db.execSQL(SQL_DELETE_ORDERS);
        db.execSQL(SQL_DELETE_COMMENTS);
        db.execSQL(SQL_DELETE_SERVICE_CLASS);
        db.execSQL(SQL_DELETE_SERVICE_ITEM);
        db.execSQL(SQL_DELETE_STAFF);
        db.execSQL(SQL_DELETE_STAFF_ASSIGNMENT);
//      db.execSQL(SQL_DELETE_SQL_STAFF_LEAVE);
        db.execSQL(SQL_DELETE_VEHICLE);
        db.execSQL(SQL_DELETE_VEHICLE_MAINTAIN);
        db.execSQL(SQL_DELETE_FURNITURE);
        db.execSQL(SQL_DELETE_DISCOUNT);
        db.execSQL(SQL_DELETE_PERIOD_DISCOUNT);

        db.execSQL(SQL_CREATE_CHOOSE);
        onCreate(db);
    }


    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
        onCreate(db);
    }

    public void onClose(SQLiteDatabase db){
        super.close();
    }

}
