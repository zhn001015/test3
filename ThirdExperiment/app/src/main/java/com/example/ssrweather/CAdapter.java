package com.example.ssrweather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CAdapter extends BaseAdapter {
    private Context context;
    private Cursor cursor;
    private LinearLayout layout;

    public  CAdapter(Context context,Cursor cursor){
        this.context=context;
        this.cursor=cursor;

    }
    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        layout = (LinearLayout)inflater.inflate(R.layout.cell,null);
        TextView cityname = (TextView) layout.findViewById(R.id.city_name);
        cursor.moveToPosition(position);
        @SuppressLint("Range") String cname = cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.CITY_NAME));
        cityname.setText(cname);
        return layout;
    }
}
