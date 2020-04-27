package com.example.a12652.just_try.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a12652.just_try.Dao.WarehouseInfoDao;
import com.example.a12652.just_try.R;

import java.util.ArrayList;

public class WarehouseInfo extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner Spinner;
    private TextView storage,type,time,dev_num;
    private int warehouse;
    private WarehouseInfoDao warehouseDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse_info);
        warehouseDao=new WarehouseInfoDao(LoginActivity.mDb);
        Spinner = (Spinner) findViewById(R.id.warehouse_info_spinner);
        storage= (TextView) findViewById(R.id.warehouse_info_storage);
        type= (TextView) findViewById(R.id.warehouse_info_type);
        time= (TextView) findViewById(R.id.warehouse_info_time);
        dev_num= (TextView) findViewById(R.id.warehouse_info_dev_num);

        ArrayAdapter<Integer> adapter=new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item,LoginActivity.userInfoData.getWarehouse_code());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner.setAdapter(adapter);
        Spinner.setVisibility(View.VISIBLE);
        Spinner.setOnItemSelectedListener(this);
    }

    private void setOtherItem() {
        if (warehouseDao.queryOne(String.valueOf(warehouse))!=null){
            ArrayList<String> warehouse_list=new ArrayList<String>();
            warehouse_list=warehouseDao.queryOne(String.valueOf(warehouse));
            storage.setText(warehouse_list.get(1));
            type.setText(warehouse_list.get(2));
            time.setText(warehouse_list.get(3));
            dev_num.setText(warehouse_list.get(4));
        }else{
            Toast.makeText(this,"数据库出现问题，数据不匹配",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.warehouse_info_spinner) {
            warehouse=LoginActivity.userInfoData.getWarehouse_code().get(position);
            setOtherItem();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
