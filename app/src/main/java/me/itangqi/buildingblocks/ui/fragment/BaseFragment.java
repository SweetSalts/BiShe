package me.itangqi.buildingblocks.ui.fragment;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import me.itangqi.buildingblocks.R;
import me.itangqi.buildingblocks.utils.base.BaseInfo;
import me.itangqi.buildingblocks.utils.base.DBHelper;

/**
 * Created by SweetSalt on 2018/3/1.
 */

public class BaseFragment extends Fragment implements View.OnClickListener {

    private View v;
    private DBHelper dbHelper;
    private BaseInfo baseInfo;

    //控件
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    //覆盖物相关
    private BitmapDescriptor mMaker;
    private RelativeLayout mMakerLy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getActivity().getApplicationContext());

        //申请权限
        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission
                .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.
                READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }

        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(getActivity(), permissions, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0){
                    for(int result : grantResults){
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(getActivity(), "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                            return;
                        }
                    }
                }else {
                    Toast.makeText(getActivity(), "发生未知错误", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
                break;
            default:
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_base, container, false);
        //初始化地图
        initView();
        //初始化实验站位置
        initMaker();
        return v;
    }

    private void initMaker() {
        mMaker = BitmapDescriptorFactory.fromResource(R.mipmap.icon_gcoding);
        mMakerLy = (RelativeLayout) v.findViewById(R.id.id_maker_ly);
    }

    private void initView() {
        mMapView = (TextureMapView ) v.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);
    }

    /**
     * 添加覆盖物
     */

    private void showBase() {
        mBaiduMap.clear();
        List<BaseInfo> infos = new ArrayList<>();
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
            infos.add(baseInfo);
        }
        LatLng latlng = null;
        Marker marker = null;
        OverlayOptions options;
        for(BaseInfo info : infos){
            //经纬度
            latlng = new LatLng(info.getLatitude(), info.getLongitude());
            //图标
            options = new MarkerOptions()
                    .position(latlng)
                    .icon(mMaker)
                    .zIndex(5);
            marker = (Marker) mBaiduMap.addOverlay(options);
            Bundle arg = new Bundle();
            arg.putSerializable("info",info);
            marker.setExtraInfo(arg);
        }
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latlng);
        mBaiduMap.setMapStatus(msu);
    }

    @Override
    public void onClick(View view) {

    }
}
