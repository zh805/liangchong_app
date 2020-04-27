package com.example.a12652.just_try.Dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by 12652 on 2020/1/27.
 */

public class WarehouseInfoDao {
    private SQLiteDatabase db;

    public WarehouseInfoDao(SQLiteDatabase sqLiteDatabase) {
        this.db = sqLiteDatabase;
    }

    public ArrayList<String> queryOne(String warehouse) {
        ArrayList<String> warehouse_list = new ArrayList<String>();
        Cursor cursor = db.query("TEMP_WAREHOUSE_INFO", null, "WAREHOUSE_CODE=?", new String[]{String.valueOf(warehouse)}, null, null, null);
        if (cursor.moveToFirst()) {
            warehouse_list.add(warehouse);
            warehouse_list.add(cursor.getString(2));
            warehouse_list.add(cursor.getString(3));
            warehouse_list.add(cursor.getString(4));
            warehouse_list.add(cursor.getString(5));
            cursor.close();
            return warehouse_list;
        } else {
            cursor.close();
            return null;
        }
    }
}
