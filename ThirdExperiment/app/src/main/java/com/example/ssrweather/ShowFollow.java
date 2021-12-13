package com.example.ssrweather;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ShowFollow extends AppCompatActivity {

    ListView showlv;
    private String data[] = {"关注信息"," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "
            ," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "
            ," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "
            ," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "};//空数据
    private String codedata[] = {"地区信息"," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "
            ," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "
            ," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "
            ," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "};//空数据
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_follow);

        showlv = (ListView)findViewById(R.id.showfollowlistview);
        intdata();
        //将关注的城市名显示出来
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        showlv.setAdapter(adapter);

        showlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("Range")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(!data[position].equals("关注信息")){
                    Intent i = new Intent(ShowFollow.this,MainActivity.class);
                    //将该地区的编码发送到主页面接收
                    i.putExtra("selectcode",codedata[position]);
                    startActivity(i);
                }else{
                }
            }
        });
    }

    //获取保存的城市和编码
    private void intdata(){
        SharedPreferences editor = getSharedPreferences("followdata", Context.MODE_PRIVATE);
        //num为关注的数量
        int numbb = editor.getInt("num",0);
        //将城市信息和编码都存到数组里
        for(int i=1;i<=numbb;i++) {
            data[i]=editor.getString(""+i,"data[i]");
            codedata[i]=editor.getString(""+data[i],"data[i]");
        }
    }
}