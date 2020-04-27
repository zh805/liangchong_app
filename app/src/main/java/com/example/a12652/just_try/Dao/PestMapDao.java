package com.example.a12652.just_try.Dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.a12652.just_try.Data.MapPestData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 12652 on 2020/2/27.
 */

public class PestMapDao {
    private SQLiteDatabase db;

    public PestMapDao(SQLiteDatabase db) {
        this.db = db;
    }

    //根据输入查询一种害虫
    public MapPestData queryOnePest(String name) {
        MapPestData pest = new MapPestData();
        Cursor cursor = db.query("PEST", null, "NAME=?", new String[]{String.valueOf(name)}, null, null, null);
        if (cursor.moveToFirst()) {
            pest.setName(name);
            pest.setFamily(cursor.getString(2));
            pest.setCategory(cursor.getString(3));
            pest.setFeature(cursor.getString(4));
            pest.setHabit(cursor.getString(5));
            pest.setDamage(cursor.getString(6));
            pest.setDistribution(cursor.getString(7));
            pest.setPictureUrl(cursor.getString(8));
            pest.setPrevention(cursor.getString(9));
            cursor.close();
            return pest;
        } else {
            cursor.close();
            return null;
        }
    }

    //根据类别查询多种害虫
    public List<MapPestData> queryPest(String category){
        List<MapPestData> list_pest=new ArrayList<MapPestData>();
        Cursor cursor=db.query("PEST",null,"CATEGORY=?",new String[]{String.valueOf(category)},null,null,null);
        if (cursor.moveToFirst()){
            MapPestData first=new MapPestData();
            first.setName(cursor.getString(1));
            first.setFamily(cursor.getString(2));
            first.setCategory(category);
            first.setFeature(cursor.getString(4));
            first.setHabit(cursor.getString(5));
            first.setDamage(cursor.getString(6));
            first.setDistribution(cursor.getString(7));
            first.setPictureUrl(cursor.getString(8));
            first.setPrevention(cursor.getString(9));
            list_pest.add(first);
            while (cursor.moveToNext()){
                MapPestData pest=new MapPestData();
                pest.setName(cursor.getString(1));
                pest.setFamily(cursor.getString(2));
                pest.setCategory(category);
                pest.setFeature(cursor.getString(4));
                pest.setHabit(cursor.getString(5));
                pest.setDamage(cursor.getString(6));
                pest.setDistribution(cursor.getString(7));
                pest.setPictureUrl(cursor.getString(8));
                pest.setPrevention(cursor.getString(9));
                list_pest.add(pest);
            }
            cursor.close();
            return list_pest;
        }else {
            cursor.close();
            return null;
        }
    }

    //查询全部
    public List<MapPestData> queryAllPest(){
        List<MapPestData> list_pest=new ArrayList<MapPestData>();
        Cursor cursor=db.query("PEST",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            MapPestData first=new MapPestData();
            first.setName(cursor.getString(1));
            first.setFamily(cursor.getString(2));
            first.setCategory(cursor.getString(3));
            first.setFeature(cursor.getString(4));
            first.setHabit(cursor.getString(5));
            first.setDamage(cursor.getString(6));
            first.setDistribution(cursor.getString(7));
            first.setPictureUrl(cursor.getString(8));
            first.setPrevention(cursor.getString(9));
            list_pest.add(first);
            while (cursor.moveToNext()){
                MapPestData pest=new MapPestData();
                pest.setName(cursor.getString(1));
                pest.setFamily(cursor.getString(2));
                pest.setCategory(cursor.getString(3));
                pest.setFeature(cursor.getString(4));
                pest.setHabit(cursor.getString(5));
                pest.setDamage(cursor.getString(6));
                pest.setDistribution(cursor.getString(7));
                pest.setPictureUrl(cursor.getString(8));
                pest.setPrevention(cursor.getString(9));
                list_pest.add(pest);
            }
            cursor.close();
            return list_pest;
        }else {
            cursor.close();
            return null;
        }
    }

}
