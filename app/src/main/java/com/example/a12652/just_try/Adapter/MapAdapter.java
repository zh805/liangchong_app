package com.example.a12652.just_try.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a12652.just_try.Data.MapPestData;
import com.example.a12652.just_try.R;

import java.util.List;

/**
 * Created by 12652 on 2020/2/27.
 */

public class MapAdapter extends BaseAdapter {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private final List<MapPestData> mlist_pest;

    public MapAdapter(Context context, List<MapPestData> list_pest) {
        mlist_pest =list_pest;
        mContext=context;
        mLayoutInflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mlist_pest.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist_pest.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= mLayoutInflater.inflate(R.layout.map_listview,null);
        ImageView map_png=convertView.findViewById(R.id.map_lv_png);
        TextView map_lv_name=convertView.findViewById(R.id.map_lv_name);
        TextView map_lv_family=convertView.findViewById(R.id.map_lv_family);
        TextView map_lv_category=convertView.findViewById(R.id.map_lv_category);

        Glide.with(mContext).load("file:///android_asset/"+mlist_pest.get(position).getPictureUrl()).into(map_png);
        map_lv_name.setText(mlist_pest.get(position).getName());
        map_lv_family.setText(mlist_pest.get(position).getFamily());
        map_lv_category.setText(mlist_pest.get(position).getCategory());

        return convertView;
    }
}
