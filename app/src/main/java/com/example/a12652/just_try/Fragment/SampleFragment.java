package com.example.a12652.just_try.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.a12652.just_try.Activity.LoginActivity;
import com.example.a12652.just_try.Adapter.SampleAdapter;
import com.example.a12652.just_try.Dao.PestSampleDao;
import com.example.a12652.just_try.Data.SamplePestData;
import com.example.a12652.just_try.R;

import java.util.ArrayList;

/**
 * Created by 12652 on 2020/1/10.
 */

public class SampleFragment extends Fragment implements View.OnTouchListener, View.OnClickListener,
        AdapterView.OnItemSelectedListener, SampleAdapter.DeleteCallBack {
    public static Uri mPhotoUri;
    private final int IMAGE_CAMERA = 123;
    private final int PERMISSION_CODE = 122;
    public SamplePestData sampleData = new SamplePestData();
    private EditText et_sample_Time, et_sample_dialog_pestnum, et_sample_Temp, et_sample_RH,
            et_sample_warehouse, et_sample_dev_place_id, et_sample_fsnum, et_sample_zsnum;
    private Spinner sample_dialog_pesttype;
    private ImageView iv_sample_icon;
    private Button bt_sample_complete;
    private Button bt_sample_indata;
    private ArrayList msample = new ArrayList();
    private SampleAdapter sampleAdapter;
    private int sample_number = 0;
    private ListView lv_sample;
    private String mSample_pesttype;
    private String mSample_pestnum;
    private ArrayList<String> mListAdapterData = new ArrayList<String>();
    private LinearLayout mFragment_view;
    private ScrollView sl_sample;
    private com.example.a12652.just_try.Dao.PestSampleDao PestSampleDao;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mFragment_view = (LinearLayout) inflater.inflate(R.layout.sample_fragment, container, false);
        sl_sample = mFragment_view.findViewById(R.id.sl_sample);
        init();
        setListener();
        return mFragment_view;
    }

    private void init() {
        et_sample_Time = mFragment_view.findViewById(R.id.et_sample_time);
        et_sample_Temp = mFragment_view.findViewById(R.id.et_sample_Temp);
        et_sample_RH = mFragment_view.findViewById(R.id.et_sample_RH);
        et_sample_warehouse = mFragment_view.findViewById(R.id.et_sample_warehouse);
        et_sample_dev_place_id = mFragment_view.findViewById(R.id.et_sample_dev_place_id);
        et_sample_fsnum = mFragment_view.findViewById(R.id.et_sample_fsnum);
        et_sample_zsnum = mFragment_view.findViewById(R.id.et_sample_zsnum);

        bt_sample_indata = mFragment_view.findViewById(R.id.sample_bt_indata);
        bt_sample_complete = mFragment_view.findViewById(R.id.bt_sample_ok);

        iv_sample_icon = mFragment_view.findViewById(R.id.iv_sample_icon);
        lv_sample = mFragment_view.findViewById(R.id.lv_sample);
        lv_sample.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        sampleAdapter = new SampleAdapter(getActivity(), this);
        sampleAdapter.setData(mListAdapterData);
        lv_sample.setAdapter(sampleAdapter);

        //设置自动滚动在底部
        sl_sample.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mFragment_view.post(new Runnable() {
                    public void run() {
                        sl_sample.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        });
    }

    private void setListener() {
        et_sample_Time.setOnTouchListener(this);
        iv_sample_icon.setOnClickListener(this);
        bt_sample_indata.setOnClickListener(this);
        bt_sample_complete.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_sample_icon:
                if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.CAMERA}, 100);
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        ContentValues contentValues = new ContentValues(2);
                        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "PestManager" + (int) 100 * Math.random() + ".jpg");
                        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                        mPhotoUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                        Log.i("photo_uri", mPhotoUri.toString());
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
                        getActivity().startActivityForResult(intent, IMAGE_CAMERA);
                    }
                }
                break;
            case R.id.sample_bt_indata:
                inputPestDialog();
                break;
            case R.id.bt_sample_ok:
                assignSampleData();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void assignSampleData() {
        if (et_sample_warehouse.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "请输入粮仓号", Toast.LENGTH_SHORT).show();
        } else if (et_sample_dev_place_id.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "请输入设备监测号", Toast.LENGTH_SHORT).show();
        } else if (et_sample_Time.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "请输入时间", Toast.LENGTH_SHORT).show();
        } else if (et_sample_fsnum.getText().toString().equals("") || et_sample_zsnum.getText().toString().equals("")
                || et_sample_RH.getText().toString().equals("") || et_sample_Temp.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "请输入数据", Toast.LENGTH_SHORT).show();
        } else {
            sampleData.setWarehouse(Integer.parseInt(et_sample_warehouse.getText().toString()));
            sampleData.setDev_place_id(Integer.parseInt(et_sample_dev_place_id.getText().toString()));
            sampleData.setTime(et_sample_Time.getText().toString());
            sampleData.setFs_num(Integer.parseInt(et_sample_fsnum.getText().toString()));
            sampleData.setZs_num(Integer.parseInt(et_sample_zsnum.getText().toString()));
            sampleData.setTemperature(Float.parseFloat(et_sample_Temp.getText().toString()));
            sampleData.setHumidity(Float.parseFloat(et_sample_RH.getText().toString()));
            for (String data : mListAdapterData) {
                String[] data1 = data.split("\\.");
                switch (data1[0]) {
                    case "米象":
                        sampleData.setSo_num(Integer.parseInt(data1[1]));
                        break;
                    case "玉米象":
                        sampleData.setSz_num(Integer.parseInt(data1[1]));
                        break;
                    case "谷蠹":
                        sampleData.setRd_num(Integer.parseInt(data1[1]));
                        break;
                    case "长角扁谷盗":
                        sampleData.setCp_num(Integer.parseInt(data1[1]));
                        break;
                    case "土耳其扁谷盗":
                        sampleData.setCt_num(Integer.parseInt(data1[1]));
                        break;
                    case "锈赤扁谷盗":
                        sampleData.setCf_num(Integer.parseInt(data1[1]));
                        break;
                    case "赤拟谷盗":
                        sampleData.setTch_num(Integer.parseInt(data1[1]));
                        break;
                    case "杂拟谷盗":
                        sampleData.setTc_num(Integer.parseInt(data1[1]));
                        break;
                    case "锯谷盗":
                        sampleData.setOs_num(Integer.parseInt(data1[1]));
                        break;
                }
            }
            PestSampleDao = new PestSampleDao(LoginActivity.mDb);
            PestSampleDao.insert(sampleData);
        }
    }

    private void inputPestDialog() {
        final Dialog dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setContentView(R.layout.sample_dialog);
        Button bt_dialog = window.findViewById(R.id.sample_dialog_bt);
        et_sample_dialog_pestnum = window.findViewById(R.id.sample_dialog_pestnum);
        sample_dialog_pesttype = window.findViewById(R.id.sample_dialog_pesttype);
        sample_dialog_pesttype.setOnItemSelectedListener(this);
        bt_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_sample_dialog_pestnum.getText().toString().equals("")) {
                    mSample_pestnum = "0";
                } else {
                    mSample_pestnum = et_sample_dialog_pestnum.getText().toString();
                }
                mListAdapterData.add(mSample_pesttype + "." + mSample_pestnum);
                sampleAdapter.setData(mListAdapterData);
                sampleAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(lv_sample);
                dialog.cancel();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void timeDialog(MotionEvent event) {
        final Dialog time_dialog = new AlertDialog.Builder(getActivity()).create();
        time_dialog.show();
        Window window = time_dialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setContentView(R.layout.layout_time);

        final DatePicker datePicker = window.findViewById(R.id.date_picker);
        final TimePicker timePicker = window.findViewById(R.id.time_picker);
        final Button bt_ok = window.findViewById(R.id.time_dialog_bt);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);

        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(Calendar.MINUTE);

        final int inType = et_sample_Time.getInputType();
        timePicker.setVisibility(View.VISIBLE);
        datePicker.setVisibility(View.VISIBLE);
        et_sample_Time.setInputType(InputType.TYPE_NULL);
        et_sample_Time.onTouchEvent(event);
        et_sample_Time.setSelection(et_sample_Time.getText().length());

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer sb = new StringBuffer();
                sb.append(String.format("%d/%02d/%02d", datePicker.getYear(),
                        datePicker.getMonth() + 1,
                        datePicker.getDayOfMonth()));
                sb.append(" ");
                sb.append(timePicker.getCurrentHour()).append(":").append(timePicker.getCurrentMinute());
                et_sample_Time.setText(sb);
                time_dialog.cancel();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (v.getId() == R.id.et_sample_time) {
                timeDialog(event);
            }
        }
        return true;
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sample_dialog_pesttype:
                mSample_pesttype = (String) getResources().getStringArray(R.array.pest_type)[position];
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void deleteListViewItem(int savePositon) {
        mListAdapterData.remove(savePositon);
        sampleAdapter.setData(mListAdapterData);
        sampleAdapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(lv_sample);
    }
}