package com.example.a12652.just_try.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a12652.just_try.Activity.LoginActivity;
import com.example.a12652.just_try.Dao.PestChartDao;
import com.example.a12652.just_try.Data.ChartPestData;
import com.example.a12652.just_try.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/*
 * Created by 12652 on 2020/1/10.
 */

@RequiresApi(api = Build.VERSION_CODES.N)
public class ChartFragment extends Fragment implements View.OnTouchListener, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {
    Spinner s_deport_number, s_sample_time;
    public String sample_time = "天";
    int deport_number = 0;
    LineChartView line_chart_all;
    EditText et_start, et_stop;
    private CheckBox cb_chart_all, cb_chart_fs, cb_chart_zs;
    public Calendar start_time = Calendar.getInstance(), stop_time = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

    ArrayList<String> X_Date = new ArrayList<String>();
    ArrayList<Integer> Y_allnum = new ArrayList<Integer>();
    ArrayList<Integer> Y_fsnum = new ArrayList<Integer>();
    ArrayList<Integer> Y_zsnum = new ArrayList<Integer>();
    private List<PointValue> mPointValues_allnum = new ArrayList<PointValue>();
    private List<PointValue> mPointValues_fsnum = new ArrayList<PointValue>();
    private List<PointValue> mPointValues_zsnum = new ArrayList<PointValue>();

    List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private PestChartDao pestnumdatadao;
    private List<ChartPestData> list_pestdata = new ArrayList<ChartPestData>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chart_fragment, container, false);
        try {
            initView(view);
        } catch (ParseException e) {

        }
        return view;
    }

    private void initView(View view) throws ParseException {
        cb_chart_all = view.findViewById(R.id.chart_cb_all);
        cb_chart_fs = view.findViewById(R.id.chart_cb_fs);
        cb_chart_zs = view.findViewById(R.id.chart_cb_zs);
        line_chart_all = view.findViewById(R.id.line_chart_all);
        et_start = view.findViewById(R.id.et_time_start);
        et_stop = view.findViewById(R.id.et_time_stop);
        start_time.add(Calendar.MONTH, -1);
        et_start.setText(sdf.format(start_time.getTime()));
        et_stop.setText(sdf.format(stop_time.getTime()));
        s_sample_time = view.findViewById(R.id.sp_time_sample);
        s_deport_number = view.findViewById(R.id.sp_deport_number);
        pestnumdatadao = new PestChartDao(LoginActivity.mDb);

        list_pestdata = pestnumdatadao.query(et_start.getText().toString(), et_stop.getText().toString());
        getAxisXLables(list_pestdata, sample_time);//获取x轴的标注
        getAxisPoints();//获取坐标点
        initLineChart(start_time, stop_time);//初始化
        String[] mItem_deport_number = getResources().getStringArray(R.array.deport_number);
        String[] mItem_sample_time = getResources().getStringArray(R.array.time_sample);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mItem_deport_number);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s_deport_number.setAdapter(adapter1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mItem_sample_time);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s_sample_time.setAdapter(adapter2);
        et_start.setOnTouchListener(this);
        et_stop.setOnTouchListener(this);
        s_deport_number.setOnItemSelectedListener(this);
        s_sample_time.setOnItemSelectedListener(this);
        cb_chart_all.setOnCheckedChangeListener(this);
        cb_chart_fs.setOnCheckedChangeListener(this);
        cb_chart_zs.setOnCheckedChangeListener(this);

    }

    private void initLineChart(Calendar start, Calendar stop) {
        List<Line> all_lines = new ArrayList<Line>();
        Line allnum_line = new Line(mPointValues_allnum).setColor(Color.GRAY);  //折线的颜色（灰色）
        Line fsnum_line = new Line(mPointValues_fsnum).setColor(Color.rgb(158, 167, 245));   //蓝色——粉蚀性数量
        Line zsnum_line = new Line(mPointValues_zsnum).setColor(Color.rgb(212, 47, 215));   //黄色——蛀蚀性数量

        //allnum_line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        allnum_line.setCubic(true);//曲线是否平滑，即是曲线还是折线
        allnum_line.setFilled(true);
        allnum_line.setPointRadius(4);
        //allnum_line.setHasLabels(true);//曲线的数据坐标是否加上备注
        //allnum_line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        //allnum_line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        //allnum_line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）

        fsnum_line.setPointRadius(4);
        fsnum_line.setCubic(true);
        fsnum_line.setFilled(true);

        zsnum_line.setPointRadius(4);
        zsnum_line.setFilled(true);
        zsnum_line.setCubic(true);
        if (cb_chart_all.isChecked() && cb_chart_fs.isChecked() && cb_chart_zs.isChecked()) {
            allnum_line.setHasLabels(false);
            fsnum_line.setHasLabels(false);
            zsnum_line.setHasLabels(false);
            all_lines.add(allnum_line);
            all_lines.add(fsnum_line);
            all_lines.add(zsnum_line);
        } else if (!(cb_chart_all.isChecked() || cb_chart_fs.isChecked() || cb_chart_zs.isChecked())) {
            cb_chart_all.setChecked(true);
            cb_chart_fs.setChecked(true);
            cb_chart_zs.setChecked(true);
            allnum_line.setHasLabels(false);
            fsnum_line.setHasLabels(false);
            zsnum_line.setHasLabels(false);
            all_lines.add(allnum_line);
            all_lines.add(fsnum_line);
            all_lines.add(zsnum_line);
        } else if (cb_chart_all.isChecked() ^ cb_chart_fs.isChecked() ^ cb_chart_zs.isChecked()) {
            allnum_line.setHasLabels(true);
            fsnum_line.setHasLabels(true);
            zsnum_line.setHasLabels(true);
            if (cb_chart_all.isChecked()) {
                all_lines.add(allnum_line);
            } else {
                all_lines.remove(allnum_line);
            }
            if (cb_chart_fs.isChecked()) {
                all_lines.add(fsnum_line);
            } else {
                all_lines.remove(fsnum_line);
            }
            if (cb_chart_zs.isChecked()) {
                all_lines.add(zsnum_line);
            } else {
                all_lines.remove(zsnum_line);
            }
        } else {
            allnum_line.setHasLabels(false);
            fsnum_line.setHasLabels(false);
            zsnum_line.setHasLabels(false);
            if (cb_chart_all.isChecked()) {
                all_lines.add(allnum_line);
            } else {
                all_lines.remove(allnum_line);
            }
            if (cb_chart_fs.isChecked()) {
                all_lines.add(fsnum_line);
            } else {
                all_lines.remove(fsnum_line);
            }
            if (cb_chart_zs.isChecked()) {
                all_lines.add(zsnum_line);
            } else {
                all_lines.remove(zsnum_line);
            }
        }

        LineChartData all_data = new LineChartData();
        all_data.setLines(all_lines);
        all_data.setValueLabelsTextColor(Color.rgb(0, 0, 0));
        all_data.setValueLabelTextSize(15);
        all_data.setValueLabelBackgroundEnabled(false);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        axisX.setName("日期 (单位：" + sample_time + ")");  //表格名称
        axisX.setTextSize(13);//设置字体大小
        axisX.setMaxLabelChars(5); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        all_data.setAxisXBottom(axisX); //x 轴在底部
        axisX.setHasLines(true); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴

        all_data.setAxisYLeft(axisY);  //Y轴设置在左边
        //设置行为属性，支持缩放、滑动以及平移
        line_chart_all.setInteractive(true);
        line_chart_all.setZoomType(ZoomType.HORIZONTAL);
        line_chart_all.setMaxZoom((float) 3);//最大方法比例
        line_chart_all.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        line_chart_all.setLineChartData(all_data);

        if (start.after(stop) || start.equals(stop)) {
            Toast.makeText(getActivity(), "时间输入有误,请重新输入", Toast.LENGTH_SHORT).show();
            line_chart_all.setVisibility(View.INVISIBLE);
        } else {
            line_chart_all.setVisibility(View.VISIBLE);
        }
        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        Viewport v = new Viewport(line_chart_all.getMaximumViewport());
        v.left = 0;
        v.right = 7;
        line_chart_all.setCurrentViewport(v);
    }

    private void getAxisPoints() {
        for (int i = 0; i < Y_allnum.size(); i++) {
            mPointValues_allnum.add(new PointValue(i, Y_allnum.get(i)));
            mPointValues_fsnum.add(new PointValue(i, Y_fsnum.get(i)));
            mPointValues_zsnum.add(new PointValue(i, Y_zsnum.get(i)));
        }
    }

    private void getAxisXLables(List<ChartPestData> list_pestdata, String sample_time) throws ParseException {

        mAxisXValues.clear();
        mPointValues_fsnum.clear();
        mPointValues_zsnum.clear();
        mPointValues_allnum.clear();
        X_Date.clear();
        Y_allnum.clear();
        Y_fsnum.clear();
        Y_zsnum.clear();

        if (sample_time.equals("天")) {
            for (ChartPestData pestData : list_pestdata) {
                X_Date.add(pestData.getMdate());
                Y_allnum.add(pestData.getMall_number());
                Y_fsnum.add(pestData.getMfs_number());
                Y_zsnum.add(pestData.getMzs_number());
            }
        }
        try {
            for (int i = 0; i < X_Date.size(); i++) {
                mAxisXValues.add(new AxisValue(i).setLabel(X_Date.get(i).substring(5)));
            }
        } catch (NullPointerException npe) {
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            final Dialog time_dialog = new AlertDialog.Builder(getActivity()).create();
            time_dialog.show();
            Window window = time_dialog.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            window.setBackgroundDrawable(new BitmapDrawable());
            window.setContentView(R.layout.chart_dialog_time);
            final DatePicker datePicker = window.findViewById(R.id.chart_date_picker);
            final TextView dialog_title=window.findViewById(R.id.chart_dialog_time_title);
            final Button chart_dialog_ok=window.findViewById(R.id.chart_dialog_bt);

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);

            if (v.getId() == R.id.et_time_start) {
                dialog_title.setText("选择开始时间");
                show_dialog(datePicker, event,  et_start, 1,chart_dialog_ok,time_dialog);
            } else if (v.getId() == R.id.et_time_stop) {
                dialog_title.setText("选择截至时间");
                show_dialog(datePicker, event,  et_stop, 2,chart_dialog_ok,time_dialog);
            }

        }
        return true;
    }

    private void show_dialog(final DatePicker datePicker, MotionEvent event, final EditText editText, final int flag, final Button ok, final Dialog dialog) {
        final int inType = editText.getInputType();
        datePicker.setVisibility(View.VISIBLE);
        editText.setInputType(InputType.TYPE_NULL);
        editText.onTouchEvent(event);
        editText.setSelection(editText.getText().length());

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer sb = new StringBuffer();
                sb.append(String.format("%d/%02d/%02d", datePicker.getYear(),
                        datePicker.getMonth() + 1,
                        datePicker.getDayOfMonth()));
                editText.setText(sb);
                if (flag == 1) {
                    start_time.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                } else if (flag == 2) {
                    stop_time.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                }
                try {
                    list_pestdata = pestnumdatadao.query(et_start.getText().toString(), et_stop.getText().toString());
                    getAxisXLables(list_pestdata, sample_time);//获取x轴的标注
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                getAxisPoints();//获取坐标点
                initLineChart(start_time, stop_time);//初始化
                dialog.cancel();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.sp_deport_number) {
            deport_number = Integer.parseInt(getResources().getStringArray(R.array.deport_number)[position]);
        } else if (parent.getId() == R.id.sp_time_sample) {
            sample_time = getResources().getStringArray(R.array.time_sample)[position];
        }
        try {
            getAxisXLables(list_pestdata, sample_time);//获取x轴的标注
        } catch (ParseException e) {
            e.printStackTrace();
        }
        getAxisPoints();//获取坐标点
        initLineChart(start_time, stop_time);//初始化
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        initLineChart(start_time, stop_time);//初始化
    }
}
