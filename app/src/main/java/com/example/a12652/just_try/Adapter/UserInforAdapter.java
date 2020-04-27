package com.example.a12652.just_try.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a12652.just_try.Activity.LoginActivity;
import com.example.a12652.just_try.R;

import java.util.ArrayList;

/**
 * Created by 12652 on 2020/1/21.
 */

public class UserInforAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private String[] item = {"用户名", "粮库名", "粮库地址", "粮仓数量"};
    private ArrayList<String> content;

    public UserInforAdapter(Context context) {
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
        content = new ArrayList<String>();
        content.add(0, LoginActivity.userInfoData.getUsermobile());
        content.add(1, LoginActivity.userInfoData.getDepot_name());
        content.add(2, LoginActivity.userInfoData.getDepot_address());
        content.add(3, String.valueOf(LoginActivity.userInfoData.getWarehouse_num()));
        convertView = mLayoutInflater.inflate(R.layout.userinfor_listview, null);
        TextView userinfor_textview = convertView.findViewById(R.id.tv_userinfor1);
        TextView userinfor_textview2 = convertView.findViewById(R.id.tv_userinfor2);
        userinfor_textview.setText(item[position]);
        userinfor_textview2.setText(content.get(position));

        return convertView;
    }
}
