package com.example.a12652.just_try.Dao;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.a12652.just_try.Data.SamplePestData;

/**
 * Created by 12652 on 2020/3/3.
 */
public class PestSampleDao {
    private SQLiteDatabase db;

    public PestSampleDao(SQLiteDatabase db) {
        this.db = db;
    }

    public boolean insert(SamplePestData samplePestData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("warehouse", samplePestData.getWarehouse());
        contentValues.put("dev_place_id", samplePestData.getDev_place_id());
        contentValues.put("manual_image",samplePestData.getManual_image());
        contentValues.put("so_num",samplePestData.getSo_num());
        contentValues.put("sz_num",samplePestData.getSz_num());
        contentValues.put("rd_num",samplePestData.getRd_num());
        contentValues.put("cp_num",samplePestData.getCp_num());
        contentValues.put("ct_num",samplePestData.getCt_num());
        contentValues.put("cf_num",samplePestData.getCf_num());
        contentValues.put("tch_num",samplePestData.getTch_num());
        contentValues.put("tc_num",samplePestData.getTc_num());
        contentValues.put("os_num",samplePestData.getOs_num());
        contentValues.put("zs_num",samplePestData.getZs_num());
        contentValues.put("fs_num",samplePestData.getFs_num());
        contentValues.put("time",samplePestData.getTime());
        contentValues.put("teperature",samplePestData.getTemperature());
        contentValues.put("humidity",samplePestData.getHumidity());
        long inserResult=db.insert("manual",null,contentValues);
        if(inserResult==1){
            return false;
        }
        return true;
    }
}
