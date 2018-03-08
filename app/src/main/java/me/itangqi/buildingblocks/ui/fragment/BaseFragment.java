package me.itangqi.buildingblocks.ui.fragment;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import me.itangqi.buildingblocks.R;
import me.itangqi.buildingblocks.utils.base.BaseInfo;
import me.itangqi.buildingblocks.utils.base.DBHelper;
import me.itangqi.buildingblocks.utils.base.ValueUtils;

/**
 * Created by SweetSalt on 2018/3/1.
 */

public class BaseFragment extends Fragment implements View.OnClickListener {

    private View v;
    private DBHelper dbHelper;
    private BaseInfo baseInfo;
    private List<BaseInfo> infos;

    //控件
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private FloatingActionButton fb;
    private Dialog addDialog, delDialog;
    EditText baseName, baseName1, basePosition, baseCode, baseLatitude, baseLongitude;
    //覆盖物相关
    private BitmapDescriptor mMaker;
    private RelativeLayout mMakerLy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SDKInitializer.initialize(getActivity().getApplicationContext());
        v = inflater.inflate(R.layout.fragment_base, container, false);
        //初始化地图
        initView();
        //初始化实验站位置
        initMaker();
        //初始化数据
        initData();

        showBase();
        //监听点击事件
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Bundle extraInfo = marker.getExtraInfo();
                BaseInfo info = (BaseInfo) extraInfo.getSerializable("info");
                ImageView iv = (ImageView) mMakerLy.findViewById(R.id.id_info_img);
                TextView name = (TextView) mMakerLy.findViewById(R.id.id_info_name);
                fb = (FloatingActionButton) getActivity().findViewById(R.id.fab);

                iv.setImageResource(R.mipmap.nanxiao);
                name.setText(info.getBaseName());

                final InfoWindow infoWindow;
                TextView tv = new TextView(getActivity());
                tv.setBackgroundResource(R.mipmap.popup);
                tv.setPadding(30, 20, 30, 50);
                tv.setText(info.getBaseName());

                final LatLng latlng = marker.getPosition();
                infoWindow = new InfoWindow(tv, latlng, -47);

                mBaiduMap.showInfoWindow(infoWindow);
                mMakerLy.setVisibility(View.VISIBLE);
                fb.setVisibility(View.GONE);
                return true;
            }
        });
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMakerLy.setVisibility(View.GONE);
                fb.setVisibility(View.VISIBLE);
                mBaiduMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
        return v;
    }

    private void initData() {
        infos = new ArrayList<>();
        dbHelper = new DBHelper(getActivity().getApplicationContext());
        dbHelper.addJWDByBaseCode("meixian", 107.996799, 34.122927);
        Cursor cursor = null;
        cursor = dbHelper.queryAll();
        while (cursor.moveToNext()) {
            baseInfo = new BaseInfo();
            baseInfo.setBaseName(cursor.getString(cursor
                    .getColumnIndex("basename")));
            baseInfo.setBasePosition(cursor.getString(cursor
                    .getColumnIndex("baseposition")));
            baseInfo.setBaseCode(cursor.getString(cursor
                    .getColumnIndex("basecode")));
            baseInfo.setLatitude(cursor.getDouble(cursor
                    .getColumnIndex("latitude")));
            baseInfo.setLongitude(cursor.getDouble(cursor
                    .getColumnIndex("longitude")));
            //Log.v("BaseInfo", baseInfo.toString());
            if(cursor.getDouble(cursor.getColumnIndex("latitude")) != 0){
                infos.add(baseInfo);
            }
        }
        dbHelper.close();
    }

    private void initMaker() {
        mMaker = BitmapDescriptorFactory.fromResource(R.mipmap.icon_gcoding);
        mMakerLy = (RelativeLayout) v.findViewById(R.id.id_maker_ly);
    }

    private void initView() {
        mMapView = (MapView) v.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);
    }

    /**
     * 添加覆盖物
     */

    private void showBase() {
        mBaiduMap.clear();
        LatLng latlng = null;
        Marker marker = null;
        OverlayOptions options;
        for (BaseInfo info : infos) {
            //经纬度
            latlng = new LatLng(info.getLongitude(), info.getLatitude());
            //图标
            options = new MarkerOptions()
                    .position(latlng)
                    .icon(mMaker)
                    .zIndex(5);
            marker = (Marker) mBaiduMap.addOverlay(options);
            Bundle arg = new Bundle();
            arg.putSerializable("info", info);
            marker.setExtraInfo(arg);
        }
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latlng);
        mBaiduMap.setMapStatus(msu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.action_addBase:
                addBaseDialog();// 添加基地的数据到数据库中
                break;
            case R.id.action_deleteBase:
                deleteBaseDialog();// 删除基地数据
                break;
            case android.R.id.home:// 点击返回图标事件
                getActivity().finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addBaseDialog() {
        // TODO Auto-generated method stub
        // 填写所需数据的dialog，最后插入数据库
        addDialog = new Dialog(getActivity());
        addDialog.setTitle("填入详细数据");
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_addbase,
                null);
        baseName = (EditText) view.findViewById(R.id.et_baseName);
        basePosition = (EditText) view.findViewById(R.id.et_basePosition);
        baseCode = (EditText) view.findViewById(R.id.et_baseCode);
        baseLatitude = (EditText) view.findViewById(R.id.et_baseLatitude);
        baseLongitude = (EditText) view.findViewById(R.id.et_baseLongitude);
        Button addbutton = (Button) view.findViewById(R.id.bt_addbase);
        Button addbuttoncancel = (Button) view
                .findViewById(R.id.bt_addbasecancle);
        addbutton.setOnClickListener(this);
        addbuttoncancel.setOnClickListener(this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        addDialog.addContentView(view, layoutParams);
        addDialog.show();
    }

    private void deleteBaseDialog() {
        // TODO Auto-generated method stub
        // 填入基地名称，如果数据库中存在该条数据，删除
        delDialog = new Dialog(getActivity());
        delDialog.setTitle("填入要删除的示范站名称");
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_delbase,
                null);
        baseName1 = (EditText) view.findViewById(R.id.et_baseName1);
        Button addbutton1 = (Button) view.findViewById(R.id.bt_delbase);
        Button addbuttoncancel1 = (Button) view
                .findViewById(R.id.bt_delbasecancle);
        addbutton1.setOnClickListener(this);
        addbuttoncancel1.setOnClickListener(this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        delDialog.addContentView(view, layoutParams);
        delDialog.show();
    }

    public static boolean isEmpty(EditText et1, EditText et2, EditText et3) {
        String str1 = et1.getText().toString().trim();
        String str2 = et2.getText().toString().trim();
        String str3 = et3.getText().toString().trim();
        if (ValueUtils.isStrEmpty(str1)) {
            return false;
        } else if (ValueUtils.isStrEmpty(str2)) {
            return false;
        } else if (ValueUtils.isStrEmpty(str3)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isEmpty(EditText et1) {
        String str1 = et1.getText().toString().trim();
        if (ValueUtils.isStrEmpty(str1)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.bt_addbase:
                if (isEmpty(baseName, basePosition, baseCode)) {
                    if (dbHelper == null) {
                        dbHelper = new DBHelper(getActivity());
                    }
                    ContentValues valu = new ContentValues();
                    valu.putNull("_baseid");
                    valu.put("basename", baseName.getText().toString());
                    valu.put("baseposition", basePosition.getText().toString());
                    valu.put("basecode", baseCode.getText().toString());
                    valu.put("latitude", baseLatitude.getText().toString());
                    valu.put("longitude", baseLongitude.getText().toString());
                    dbHelper.insert("baseInfoTb", valu);
                    addDialog.dismiss();
                    showBase();
                } else {
                    Toast.makeText(getActivity(), "你还有数据没有填写！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_addbasecancle:
                addDialog.dismiss();
                break;
            case R.id.bt_delbase:// 删除示范站
                if (isEmpty(baseName1)) {
                    if (dbHelper == null) {
                        dbHelper = new DBHelper(getActivity());
                    }
                    String basecode = dbHelper.queryBaseCode(baseName1.getText()
                            .toString().trim());
                    dbHelper.relationdel(basecode);// 先删除该示范站下的相机
                    dbHelper.del("baseInfoTb", baseName1.getText().toString()
                            .trim());// 删除示范站
                    delDialog.dismiss();
                    showBase();
                    Toast.makeText(getActivity(), "已删除！",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "你还有数据没有填写！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_delbasecancle:
                delDialog.dismiss();
                break;
            default:
                break;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}
