package me.itangqi.buildingblocks.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.itangqi.buildingblocks.R;
import me.itangqi.buildingblocks.utils.base.BaseInfo;
import me.itangqi.buildingblocks.utils.base.DBHelper;
import me.itangqi.buildingblocks.utils.base.ValueUtils;
import me.itangqi.buildingblocks.ui.activity.BaseDataActivity;
import me.itangqi.buildingblocks.ui.activity.base.BaseVideoActivity;

/**
 * Created by oreo on 2016/7/20.
 */
public class BaseList extends Fragment implements View.OnClickListener {
    private View v;
    // 对象
    private MyAdapter myAdapter = new MyAdapter();
    private BaseInfo baseInfo;
    DBHelper dbHelper;
    // 数据
    public static final String[] items = { "田间监测数据", "生产现场视频" };
    List<String> parent = null;
    Map<String, List<BaseInfo>> map = null;
    private static final int IS_SHANXI = 1;
    private static final int NOT_SHANXI = 0;
    // 控件
    EditText baseName, baseName1, basePosition, baseCode;
    private ExpandableListView expandableListView = null;
    private Dialog addDialog, delDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
      super.onCreate(savedInstanceState);



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_baselist, container, false);
        expandableListView = (ExpandableListView) v.findViewById(R.id.elv_base1);
        if(expandableListView==null){
            Log.d("debug","ExpandableListView NULL");
        }
        initData();
        initLayout();
        return v;

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    private void initData() {
        // TODO Auto-generated method stub
        parent = new ArrayList<String>();
        parent.add("省内基地");
        parent.add("省外基地");
        map = new HashMap<String, List<BaseInfo>>();
        dbHelper = new DBHelper(getActivity().getApplicationContext());
        List<BaseInfo> inBaseInfos = new ArrayList<BaseInfo>();
        Cursor cursor = null;
        cursor = dbHelper.query(IS_SHANXI);
        Log.v("shengnei", cursor.getCount() + "");
        while (cursor.moveToNext()) {
            baseInfo = new BaseInfo();
            baseInfo.setBaseName(cursor.getString(cursor
                    .getColumnIndex("basename")));
            baseInfo.setBasePosition(cursor.getString(cursor
                    .getColumnIndex("baseposition")));
            baseInfo.setBaseCode(cursor.getString(cursor
                    .getColumnIndex("basecode")));
            baseInfo.setShanXi(true);
            Log.v("shengnei", baseInfo.toString());
            inBaseInfos.add(baseInfo);
        }
        map.put("省内基地", inBaseInfos);
        List<BaseInfo> outBaseInfos = new ArrayList<BaseInfo>();
        Cursor cursor1 = null;
        cursor1 = dbHelper.query(NOT_SHANXI);
        Log.v("shengwai", cursor1.getCount() + "");
        while (cursor1.moveToNext()) {
            baseInfo = new BaseInfo();
            baseInfo.setBaseName(cursor1.getString(cursor1
                    .getColumnIndex("basename")));
            baseInfo.setBasePosition(cursor1.getString(cursor1
                    .getColumnIndex("baseposition")));
            baseInfo.setBaseCode(cursor1.getString(cursor1
                    .getColumnIndex("basecode")));
            baseInfo.setShanXi(false);
            Log.v("shengwai", baseInfo.toString());
            outBaseInfos.add(baseInfo);
        }
        map.put("省外基地", outBaseInfos);
        cursor.close();
        cursor1.close();
        dbHelper.close();
    }

    private void initLayout() {
        // TODO Auto-generated method stub

        expandableListView.setAdapter(myAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                baseInfo = (BaseInfo) myAdapter.getChild(groupPosition,
                        childPosition);
                chooseOption(baseInfo);
                return false;
            }
        });
    }

    private void chooseOption(final BaseInfo baseInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                getActivity());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                switch (arg1) {
                    case 0:
                        Intent intent = new Intent();
                        intent.setClass(getActivity(),
                                BaseDataActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("BaseInfo", baseInfo);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent();
                        intent1.setClass(getActivity(),
                                BaseVideoActivity.class);
                        Bundle bundle1 = new Bundle();
                        bundle1.putSerializable("BaseInfo", baseInfo);
                        intent1.putExtras(bundle1);
                        startActivity(intent1);
                        break;
                    default:
                        break;
                }
            }
        });
        builder.show();
    }

    class MyAdapter extends BaseExpandableListAdapter {

        // 得到子item需要关联的数据
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            String key = parent.get(groupPosition);
            return (map.get(key).get(childPosition));
        }

        // 得到子item的ID
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        // 设置子item的组件
        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent1) {
            String key = parent.get(groupPosition);
            BaseInfo info = map.get(key).get(childPosition);
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                convertView = inflater.inflate(
                        R.layout.layout_baselist_children, null);
            }
            TextView tv_name = (TextView) convertView
                    .findViewById(R.id.tv_baseName);
            tv_name.setText(info.getBaseName());
            TextView tv_position = (TextView) convertView
                    .findViewById(R.id.tv_basePosition);
            tv_position.setText(info.getBasePosition());
            return convertView;
        }

        // 获取当前父item下的子item的个数
        @Override
        public int getChildrenCount(int groupPosition) {
            String key = parent.get(groupPosition);
            int size = map.get(key).size();
            return size;
        }

        // 获取当前父item的数据
        @Override
        public Object getGroup(int groupPosition) {
            return parent.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return parent.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        // 设置父item组件
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent1) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.layout_baselist_parent,
                        null);
            }
            TextView tv = (TextView) convertView.findViewById(R.id.tv_isShanXi);
            tv.setText(parent.get(groupPosition));
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.baselist, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

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
        Button addbutton = (Button) view.findViewById(R.id.bt_addbase);
        Button addbuttoncancel = (Button) view
                .findViewById(R.id.bt_addbasecancle);
        addbutton.setOnClickListener(this);
        addbuttoncancel.setOnClickListener(this);
        ViewGroup.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        addDialog.addContentView(view, layoutParams);
        addDialog.show();
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
        // TODO Auto-generated method stub
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
                    if (basePosition.getText().toString().indexOf("陕西省") == -1) {
                        valu.put("isshanxi", 0);
                    } else {
                        valu.put("isshanxi", 1);
                    }
                    dbHelper.insert("baseInfoTb", valu);
                    addDialog.dismiss();
                    initData();
                    myAdapter.notifyDataSetChanged();
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
                    initData();
                    myAdapter.notifyDataSetChanged();
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



}
