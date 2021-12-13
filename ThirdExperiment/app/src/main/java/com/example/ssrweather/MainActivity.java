package com.example.ssrweather;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;


public class MainActivity extends AppCompatActivity {


    TextView citytext,zhaungkuangtext,lowhigtext,timetext,wendutext;
    Button follow,showfollow;
    String citycodefind="";
    MySQLiteOpenHelper CityDB;
    SQLiteDatabase DBreader;
    private int count = 0;

    String nextname,nexttemp,nexttime,nextweather;

    Weatherinfo WeatherDB;
    SQLiteDatabase DBweather;

    public static final int SHOW_RESPONSE = 0;
    private Button select;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    // 在这⾥进⾏UI操作，将结果显示到界⾯上
                    parseJSONWithJOSNObject(response.toString());
            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        try {
            Class<?> clazz = Class.forName("android.support.v7.view.menu.MenuBuilder");
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            m.setAccessible(true);
            //MenuBuilder实现Menu接口，创建菜单时，传进来的menu其实就是MenuBuilder对象(java的多态特征)
            m.invoke(menu,true);//交出控制权
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onCreateOptionsMenu(menu);
    }

    //刷新方法
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.add:
                Toast.makeText(MainActivity.this,"刷新成功",Toast.LENGTH_SHORT).show();
                //刷新时即重新获取一遍天气信息
                sendRequestWithHttpURLConnection(citycodefind);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CityDB = new MySQLiteOpenHelper(this);
        DBreader = CityDB.getWritableDatabase();

            //判断是否是第一次初始化数据库
            SharedPreferences share = this.getSharedPreferences("datainfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = share.edit();
            boolean checkIsFirst = share.getBoolean("isFirst",true);
            if( !checkIsFirst){
                ;
            }else{
                editor.putBoolean("isFirst",false);
                editor.commit();
                Toast.makeText(this, "初始化数据库", Toast.LENGTH_SHORT).show();
                importSheet();
            }

        WeatherDB = new Weatherinfo(this);
        DBweather = WeatherDB.getReadableDatabase();

        citytext = (TextView) findViewById(R.id.tv_city);
        wendutext = (TextView) findViewById(R.id.tv_temperature);
        //lowhigtext = (TextView) findViewById(R.id.tv_low_height);
        zhaungkuangtext = (TextView) findViewById(R.id.tv_tianqizhuangkuang);
        timetext = (TextView) findViewById(R.id.tv_old_time);

        //获取到cityselect界面发来的地区编码
        citycodefind = getIntent().getStringExtra("selectcode");
        //默认为110105
        if (citycodefind == null) {
            citycodefind = "110105";
        }

        try{
            //在天气数据库中查询当前地区编码的信息,query查询一条数据的方法
            Cursor cursor = DBweather.query("weather", new String[]{"NAME,CITYWEATHER,CITYTEMP,WEATHERTIME"}, "ADCODE like ?", new String[]{"%"+citycodefind+"%"}, null, null, null, null);
            int checknext=0;
            while (cursor.moveToNext()) {
                nextname = cursor.getString(0);//获取城市名
                nextweather = cursor.getString(1);//获取天气
                nexttemp = cursor.getString(2);//获取温度
                nexttime = cursor.getString(3);//获取预报时间
                checknext++;
            }
            //如果数据库中没有信息，则从网站获取信息
            if(checknext==0){
                sendRequestWithHttpURLConnection(citycodefind);
            }else{
                citytext.setText(nextname);
                wendutext.setText(nexttemp);
                zhaungkuangtext.setText(nextweather);
                timetext.setText(nexttime);
            }
        }catch (SQLiteException ss){
            System.out.println("catch "+ss);
        }

        //查看关注的列表
        showfollow = (Button)findViewById(R.id.showfollow);
        showfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(MainActivity.this,ShowFollow.class);
                startActivity(i1);
            }
        });

        //点击关注该城市,就运用sharedpreference技术保存起来
        follow = (Button)findViewById(R.id.follow);
        follow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences("followdata", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                if(sp.contains(citytext.getText().toString())){
                    Toast.makeText(getApplicationContext(), "已经关注过", Toast.LENGTH_SHORT).show();
                }else{
                    //每加入一个,num加一
                    int numbbb = sp.getInt("num",0)+1;
                    editor.putInt("num",numbbb);
                    editor.putString(""+numbbb,""+citytext.getText().toString());
                    editor.putString(""+citytext.getText().toString(),"" + citycodefind);
                    editor.apply();
                    Toast.makeText(getApplicationContext(), "关注成功", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //选择城市的界面
        select = (Button)findViewById(R.id.select);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(MainActivity.this,Provinceselect.class);
                startActivity(i1);
            }
        });

    }

    //从网站获取天气的信息
    private void sendRequestWithHttpURLConnection(String citycodefind) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL("https://restapi.amap.com/v3/weather/weatherInfo?city="+citycodefind+"&key=be3b8dfa39582f3b015c8c083c443d8c");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    //读取响应
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new
                            InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null)
                        response.append(line);
                    //实现异步处理，显示数据，封装后发送
                    Message message = new Message();
                    message.what = SHOW_RESPONSE;
                    message.obj = response.toString();
                    handler.sendMessage(message);
                    System.out.println(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null)
                        connection.disconnect();
                }
            }
        }).start();
    }

    //解析JSON格式
    private void parseJSONWithJOSNObject(String jsonData){
        try {
            JSONObject jsonObj = new JSONObject(jsonData);
              String infocode = jsonObj.getString("infocode");

            JSONArray lives = jsonObj.getJSONArray("lives");
            for (int i = 0; i < lives.length(); i++) {
                //将需要显示的数据全部解析出来
                JSONObject comment = lives.getJSONObject(i);
                String city = comment.getString("city");
                String adcode = comment.getString("adcode");
                String weather = comment.getString("weather");
                String temperature = comment.getString("temperature");
                String reporttime = comment.getString("reporttime");
                //显示数据
                citytext.setText(city);
                wendutext.setText(temperature);
                zhaungkuangtext.setText(weather);
                timetext.setText(reporttime);

                //将解析出的数据存到天气数据库中，以便后续查看
                WeatherDB = new Weatherinfo(this);
                DBweather = WeatherDB.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(Weatherinfo.CITY_NAME, city);
                cv.put(Weatherinfo.ADCODE, adcode);
                cv.put(Weatherinfo.CITYCODE, citycodefind);
                cv.put(Weatherinfo.CITYNUM, "num");
                cv.put(Weatherinfo.CITYWEATHER, weather);
                cv.put(Weatherinfo.CITYTEMP, temperature);
                cv.put(Weatherinfo.WEATHERTIME, reporttime);

                DBweather.insert(Weatherinfo.TABLE_NAME, null, cv);
            }
            System.out.println("indocode"+infocode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //初始化city数据库
    private void importSheet() {

        if(count != 0){
            return;
        }
        try {
            InputStream is = getResources().getAssets().open("aacityexcel.xls");//将表中数据读取到Java中
            Workbook book = Workbook.getWorkbook(is);
            Sheet sheet = book.getSheet(0);
            for (int j = 0; j < sheet.getRows(); ++j) {
                initDataInfo(sheet.getCell(0, j).getContents(), sheet.getCell(1, j).getContents(), sheet.getCell(2, j).getContents(), sheet.getCell(3, j).getContents());
            }
            book.close();
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }
        count ++;
    }


    //将城市数据全部加入到city数据库中
    public void initDataInfo(String name, String adcode, String citycode,String citynum) {
        CityDB = new MySQLiteOpenHelper(this);
        SQLiteDatabase db = CityDB.getWritableDatabase();
        ContentValues cv = new ContentValues();
        System.out.println("cvint");
        cv.put("name", name);
        cv.put("adcode", adcode);
        cv.put("citycode", citycode);
        cv.put("citynum", citynum);
        db.insert(MySQLiteOpenHelper.TABLE_NAME, null, cv);
        db.close();
    }
}