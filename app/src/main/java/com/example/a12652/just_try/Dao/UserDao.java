package com.example.a12652.just_try.Dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.a12652.just_try.Data.UserInfoData;

import java.util.ArrayList;

/**
 * Created by 12652 on 2020/1/27.
 */

public class UserDao {
    private SQLiteDatabase db;

    public UserDao(SQLiteDatabase sqLiteDatabase) {
        this.db = sqLiteDatabase;
    }

    //    public static void createTable(SQLiteDatabase db,boolean NotExists){
//        db.execSQL("CREATE TABLE IF NOT EXISTS user_tb("
//                + "id INTEGER primary key autoincrement,"
//                + "mobile TEXT,"
//                + "password TEXT)");
//    }
    public boolean insert(UserInfoData userInfoData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("MOBILE", userInfoData.getUsermobile());
        contentValues.put("PASSWORD", userInfoData.getPassword());
        long inserResult = db.insert("USER_INFO", null, contentValues);
        if (inserResult == 1) {
            return false;
        }
        return true;
    }

    public UserInfoData queryOne(String userMobile) {
        UserInfoData userInfoData = new UserInfoData();
        Cursor cursor = db.query("USER_INFO", null, "MOBILE=?", new String[]{String.valueOf(userMobile)}, null, null, null);
        if (cursor.moveToFirst()) {
            userInfoData.setUsermobile(cursor.getString(1));
            userInfoData.setPassword(cursor.getString(2));
            userInfoData.setDepot_name(cursor.getString(3));
            userInfoData.setDepot_address(cursor.getString(5));
            userInfoData.setDepot_manager(cursor.getString(6));
            userInfoData.setWarehouse_num(Integer.parseInt(cursor.getString(7)));
            String[] s_warehouse_code = cursor.getString(8).split("\\,");
            ArrayList<Integer> warehouse_code = new ArrayList<Integer>();
            for (int i = 0; i < s_warehouse_code.length; i++) {
                warehouse_code.add(Integer.valueOf(s_warehouse_code[i]));
            }
            userInfoData.setWarehouse_code(warehouse_code);
            cursor.close();
            return userInfoData;
        } else {
            cursor.close();
            return null;
        }
    }
}
