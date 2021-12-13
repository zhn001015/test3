package com.example.ssrweather;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Weatherinfo extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "weather";
    public static final String CITY_NAME = "NAME";
    public static final String ADCODE = "ADCODE";
    public static final String CITYCODE = "CITYCODE";
    public static final String CITYNUM = "CITYNUM";
    public static final String CITYWEATHER = "CITYWEATHER";
    public static final String CITYTEMP = "CITYTEMP";
    public static final String WEATHERTIME = "WEATHERTIME";

    private static MySQLiteOpenHelper helper;

    public static final String DATABASE_NAME = "weatherdata.db";

    public static int DATABASE_VERSION = 1;



    public Weatherinfo(@Nullable Context context) {
        super(context, "weatherdata", null, 1);
    }
    public Weatherinfo(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public void initDataInfo(String code, String value) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("code", code);
        cv.put("value", value);
        db.insert(TABLE_NAME, null, cv);
    }


    public static MySQLiteOpenHelper getInstance(Context context) {
        if (helper == null) {
            helper = new MySQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        return helper;
    }
    //创建天气数据的数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "("
                + "id" + " INTEGER  PRIMARY KEY AUTOINCREMENT,"
                + CITY_NAME + " TEXT NOT NULL,"
                + ADCODE + " TEXT NOT NULL,"
                + CITYCODE + " TEXT NOT NULL,"
                + CITYNUM + " TEXT NOT NULL,"
                + CITYWEATHER + " TEXT NOT NULL,"
                + CITYTEMP + " TEXT NOT NULL,"
                + WEATHERTIME + " TEXT NOT NULL )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
