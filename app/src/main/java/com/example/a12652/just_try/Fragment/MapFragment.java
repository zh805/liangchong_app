package com.example.a12652.just_try.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a12652.just_try.Activity.LoginActivity;
import com.example.a12652.just_try.Adapter.MapAdapter;
import com.example.a12652.just_try.Dao.PestMapDao;
import com.example.a12652.just_try.Data.MapPestData;
import com.example.a12652.just_try.R;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by 12652 on 2020/1/10.
 */

public class MapFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {

    private SearchView mMap_search_input;
    private RadioGroup map_bt_group;
    private MapAdapter mapAdapter;
    private RadioButton bt_menu_all;
    private RadioButton bt_menu_cole;
    private RadioButton bt_menu_mites;
    private RadioButton bt_menu_moth;
    private List<MapPestData> list_pest = new ArrayList<MapPestData>();
    private MapPestData pest;
    private PestMapDao pestmapdao;
    private ListView map_listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        initView(view);
        map_bt_group.setOnCheckedChangeListener(this);
        mMap_search_input.setOnQueryTextListener(this);
        map_listView.setOnItemClickListener(this);
        return view;
    }

    private void initView(View view) {
        pestmapdao = new PestMapDao(LoginActivity.mDb);
        mMap_search_input = view.findViewById(R.id.map_search_input);
        mMap_search_input.setSubmitButtonEnabled(true);
        mMap_search_input.setIconifiedByDefault(false);
        map_bt_group = view.findViewById(R.id.map_button_group);
        bt_menu_all = view.findViewById(R.id.bt_menu_all);
        bt_menu_cole = view.findViewById(R.id.bt_menu_cole);
        bt_menu_mites = view.findViewById(R.id.bt_menu_mites);
        bt_menu_moth = view.findViewById(R.id.bt_menu_moth);
        map_listView = view.findViewById(R.id.lv_map);
        list_pest = pestmapdao.queryAllPest();
        mapAdapter = new MapAdapter(getActivity(), list_pest);
        map_listView.setAdapter(mapAdapter);
    }

    //选择种类点击事件
    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.bt_menu_all:
                list_pest.clear();
                list_pest = pestmapdao.queryAllPest();
                mapAdapter = new MapAdapter(getActivity(), list_pest);
                map_listView.setAdapter(mapAdapter);
                break;
            case R.id.bt_menu_cole:
                list_pest.clear();
                list_pest = pestmapdao.queryPest("甲虫类");
                mapAdapter = new MapAdapter(getActivity(), list_pest);
                map_listView.setAdapter(mapAdapter);
                break;
            case R.id.bt_menu_mites:
                list_pest.clear();
                list_pest = pestmapdao.queryPest("螨类");
                mapAdapter = new MapAdapter(getActivity(), list_pest);
                map_listView.setAdapter(mapAdapter);
                break;
            case R.id.bt_menu_moth:
                list_pest.clear();
                list_pest = pestmapdao.queryPest("蛾类");
                mapAdapter = new MapAdapter(getActivity(), list_pest);
                map_listView.setAdapter(mapAdapter);
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        pest = pestmapdao.queryOnePest(query);
        if (pest == null) {
            Toast.makeText(getActivity(), "您输入的害虫数据库中不存在，请重新输入", LENGTH_SHORT).show();
            return false;
        } else {
            list_pest.clear();
            list_pest.add(pest);
            mapAdapter = new MapAdapter(getActivity(), list_pest);
            map_listView.setAdapter(mapAdapter);
            mMap_search_input.clearFocus();
            return true;
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        pest = pestmapdao.queryOnePest(newText);
        if (pest == null) {
            return false;
        } else {
            list_pest.clear();
            list_pest.add(pest);
            if (pest.getCategory().equals("甲虫类")) {
                bt_menu_cole.setChecked(true);
            } else if (pest.getCategory().equals("螨类")) {
                bt_menu_mites.setChecked(true);
            } else if (pest.getCategory().equals("蛾类")) {
                bt_menu_moth.setChecked(true);
            }
            mapAdapter = new MapAdapter(getActivity(), list_pest);
            map_listView.setAdapter(mapAdapter);
            return true;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MapPestData pest = (MapPestData) parent.getAdapter().getItem(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        View view1 = View.inflate(this.getActivity(), R.layout.map_dialog, null);
        TextView family = view1.findViewById(R.id.map_dialog_family);
        family.setText(pest.getFamily());
        TextView category = view1.findViewById(R.id.map_dialog_category);
        category.setText(pest.getCategory());
        TextView feature = view1.findViewById(R.id.map_dialog_feature);
        feature.setText(pest.getFeature());
        TextView habit = view1.findViewById(R.id.map_dialog_habit);
        habit.setText(pest.getHabit());
        TextView damage = view1.findViewById(R.id.map_dialog_damage);
        damage.setText(pest.getDamage());
        TextView distribution = view1.findViewById(R.id.map_dialog_distribution);
        distribution.setText(pest.getDistribution());
        TextView prevention = view1.findViewById(R.id.map_dialog_prevention);
        prevention.setText(pest.getPrevention());
        ImageView png = view1.findViewById(R.id.map_dialog_png);
        Glide.with(this).load("file:///android_asset/" + pest.getPictureUrl()).into(png);
        builder.setTitle(pest.getName());
        builder.setView(view1);
        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }
}
