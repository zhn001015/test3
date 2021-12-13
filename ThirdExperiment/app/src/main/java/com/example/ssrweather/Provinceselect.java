package com.example.ssrweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.api.maps.offlinemap.Province;

public class Provinceselect extends AppCompatActivity {

    ListView listView;

    private String data[] = {"北京市","天津市","河北省","山西省","内蒙古自治区","辽宁省","吉林省","黑龙江省","上海市","江苏省","浙江省",
            "安徽省","福建省","江西省","山东省","河南省","湖北省","湖南省","广东省","广西壮族自治区","海南省","重庆省","四川省","贵州省","云南省",
            "西藏自治区","陕西省","甘肃省","青海省","宁夏回族自治区","新疆维吾尔自治区","台湾省","香港特别行政区","澳门特别行政区"};//假数据
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provinceselect);
        listView = (ListView) findViewById(R.id.listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Provinceselect.this,android.R.layout.simple_list_item_1,data);//新建并配置ArrayAapeter
        listView.setAdapter(adapter);

        //点击省份后，继续跳转到选择城市
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(Provinceselect.this,"你点击了"+data[i]+"按钮",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Provinceselect.this,Cityselect.class);
                intent.putExtra("Provincename",data[i]);
                startActivity(intent);
            }
        });
    }


}