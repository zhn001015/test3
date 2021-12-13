package com.example.ssrweather;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Cityselect extends AppCompatActivity {


    private MySQLiteOpenHelper CityDB;
    private SQLiteDatabase dbReader;
    private CAdapter adapter;
    private ListView lv;
    private String cityname, citynum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cityselect2);
        lv = (ListView) findViewById(R.id.citylistview);
        CityDB = new MySQLiteOpenHelper(this);
        dbReader = CityDB.getReadableDatabase();
        //获取省份名
        cityname = getIntent().getStringExtra("Provincename");
        System.out.println("city====" + cityname);

        String querySql = "select * from " + MySQLiteOpenHelper.TABLE_NAME + " where " + MySQLiteOpenHelper.CITY_NAME + "=?";
        String[] args = new String[]{cityname};
        // 返回值是一个Cursor对象，搜寻city数据库中的存储信息
        Cursor cursor = dbReader.rawQuery(querySql, args);
        while (cursor.moveToNext()) {
            //城市标志
            citynum = cursor.getString(4);
        }

        List<String> data = new ArrayList<String>();
        String querySql1 = "select * from " + MySQLiteOpenHelper.TABLE_NAME + " where citynum" + "=?";
        String[] args1 = new String[]{citynum};
        Cursor cursor1 = dbReader.rawQuery(querySql1, args1);
        while (cursor1.moveToNext()) {
            //获取对应的市级城市
            data.add(cursor1.getString(1));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("Range")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String a = data.get(position);
                Cursor cursor = dbReader.rawQuery("select * from city where NAME = '" + a + "'", null);
                cursor.moveToFirst();
                Intent i = new Intent(Cityselect.this, MainActivity.class);
                //将城市编码装入到intent中，发送至mainAcitivity
                i.putExtra("selectcode", cursor.getString(2));
                startActivity(i);
            }
        });
    }
}
