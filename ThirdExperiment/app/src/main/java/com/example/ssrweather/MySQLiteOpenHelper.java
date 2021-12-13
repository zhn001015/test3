package com.example.ssrweather;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "city";
    public static final String CITY_NAME = "NAME";
    public static final String ADCODE = "ADCODE";
    public static final String CITYCODE = "CITYCODE";
    private static final String CITYNUM = "CITYNUM";

    private static MySQLiteOpenHelper helper;

    public static final String DATABASE_NAME = "citydata.db";

    public static int DATABASE_VERSION = 1;



    public MySQLiteOpenHelper(@Nullable Context context) {
        super(context, "citydata", null, 1);
    }
    public MySQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static MySQLiteOpenHelper getInstance(Context context) {
        if (helper == null) {
            helper = new MySQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        return helper;
    }
    //创建城市数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "("
                + "id" + " INTEGER  PRIMARY KEY AUTOINCREMENT,"
                + CITY_NAME + " TEXT NOT NULL,"
                + ADCODE + " TEXT NOT NULL,"
                + CITYCODE + " TEXT NOT NULL,"
                + CITYNUM + " TEXT NOT NULL )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
