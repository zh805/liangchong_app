package com.example.a12652.just_try.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a12652.just_try.R;

import java.util.ArrayList;

/**
 * Created by 12652 on 2020/1/31.
 */

public class SampleAdapter extends BaseAdapter implements View.OnClickListener {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int savePosition;
    private ArrayList<String> mitem;
    private DeleteCallBack mDeleteCallBack;

    public SampleAdapter(Context context, DeleteCallBack deleteCallBack) {
        mDeleteCallBack = deleteCallBack;
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mitem.size();
    }

    @Override
    public Object getItem(int position) {
        return mitem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.sample_listview, null);
            holder.pest_type = convertView.findViewById(R.id.pest_type);
            holder.pest_number = convertView.findViewById(R.id.pest_number);
            holder.viewIm = convertView.findViewById(R.id.pest_sub);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String[] pest = mitem.get(position).toString().split("\\.");
        holder.pest_type.setText(pest[0]);
        holder.pest_number.setText(pest[1]);
        holder.viewIm.setTag(position);
        holder.viewIm.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pest_sub) {
            savePosition = Integer.parseInt(v.getTag().toString());
            mDeleteCallBack.deleteListViewItem(savePosition);
            Log.i("LogAdapter", "ok" + savePosition);
        }
    }

    public void setDeleteCallBack(DeleteCallBack deleteCallBack) {
        this.mDeleteCallBack = deleteCallBack;
    }

    public void setData(ArrayList<String> item) {
        this.mitem = item;
    }

    public interface DeleteCallBack {
        void deleteListViewItem(int savePositon);
    }

    public final class ViewHolder {
        public ImageView viewIm;
        public TextView pest_type;
        public TextView pest_number;
    }
}
