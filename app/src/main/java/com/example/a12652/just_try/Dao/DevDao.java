package com.example.a12652.just_try.Dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.a12652.just_try.Data.DevInfo;

/**
 * Created by 12652 on 2020/1/27.
 */

public class DevDao {
    private SQLiteDatabase db;

    public DevDao(SQLiteDatabase sqLiteDatabase) {
        this.db = sqLiteDatabase;
    }

    public DevInfo queryOne(int dev_id) {
        DevInfo devInfo = new DevInfo();
        Cursor cursor = db.query("DEV", null, "dev_id=?", new String[]{String.valueOf(dev_id)}, null, null, null);
        if (cursor.moveToFirst()) {
            devInfo.setDev_id(dev_id);
            devInfo.setDev_install_time(cursor.getString(2));
            devInfo.setDev_type(Integer.parseInt(cursor.getString(3)));
            devInfo.setDev_status(Integer.parseInt(cursor.getString(4)));
            cursor.close();
            return devInfo;
        } else {
            cursor.close();
            return null;
        }
    }
}
