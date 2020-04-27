package com.example.a12652.just_try.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a12652.just_try.R;

/**
 * Created by 12652 on 2020/1/17.
 */

public class ItemAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private String[] item = {"用户信息", "粮仓信息", "预警设置", "修改密码"};

    public ItemAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return item.length;
    }

    @Override
    public Object getItem(int position) {
        return item[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mLayoutInflater.inflate(R.layout.menu_listview, null);
        TextView item_text_view = convertView.findViewById(R.id.tv_item);
        item_text_view.setText(item[position]);
        return convertView;
    }
}
