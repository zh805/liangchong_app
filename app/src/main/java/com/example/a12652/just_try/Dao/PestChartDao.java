package com.example.a12652.just_try.Dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.a12652.just_try.Data.ChartPestData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 12652 on 2020/2/21.
 */

public class PestChartDao {
    private SQLiteDatabase db;

    public PestChartDao(SQLiteDatabase db) {
        this.db = db;
    }

//    public static void createTable(SQLiteDatabase db, boolean NotExists) {
//        db.execSQL("CREATE TABLE IF NOT EXISTS pestnumdata_tb("
//                + "id INTEGER primary key autoincrement,"
//                + "date TEXT,"
//                + "fs_num INTEGER,"
//                + "zs_num INTEGER,"
//                + "all_num INTEGER)");
//    }


    public List<ChartPestData> query(String date_start, String date_stop) {
        List<ChartPestData> list_pestData = new ArrayList<ChartPestData>();
        //Cursor cursor = db.query("pestnumdata_tb", null, "date>=? and date<=?", new String[]{date_start , date_stop}, null, null, null);
        Cursor cursor=db.rawQuery("SELECT * FROM TEMP_CHART_DATA WHERE date BETWEEN ? AND ?",new String[]{date_start,date_stop});

        if (cursor.moveToFirst()) {
            ChartPestData first=new ChartPestData();
            first.setMdate(cursor.getString(1));
            first.setMfs_number(cursor.getInt(2));
            first.setMzs_number(cursor.getInt(3));
            first.setMall_number(cursor.getInt(4));
            list_pestData.add(first);
            while (cursor.moveToNext()) {
                ChartPestData pestData = new ChartPestData();
                pestData.setMdate(cursor.getString(1));
                pestData.setMfs_number(cursor.getInt(2));
                pestData.setMzs_number(cursor.getInt(3));
                pestData.setMall_number(cursor.getInt(4));
                list_pestData.add(pestData);
            }
            cursor.close();
            return list_pestData;
        } else {
            Log.i("pestdate","dao null");
            cursor.close();
            return null;
        }
    }
}
