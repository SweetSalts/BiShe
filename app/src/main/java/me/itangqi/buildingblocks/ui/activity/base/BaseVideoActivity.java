package me.itangqi.buildingblocks.ui.activity.base;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import innoview.itouchviewp2p.dev_one.com.MsgConstantDevOne;
import innoview.itouchviewp2p.dev_six.DevSixXImplEx;
import innoview.itouchviewp2p.dev_six.com.MsgConstantDevSix;
import innoview.itouchviewp2p.idev.IBaseDev;
import innoview.itouchviewp2p.util.ConstantInfo;
import me.itangqi.buildingblocks.R;
import me.itangqi.buildingblocks.utils.base.BaseInfo;
import me.itangqi.buildingblocks.utils.base.BaseVideoUser;
import me.itangqi.buildingblocks.utils.base.CameraInfo;
import me.itangqi.buildingblocks.utils.base.DBHelper;
import me.itangqi.buildingblocks.ui.fragment.BaseList;

/**
 * Created by oreo on 2016/7/20.
 */
public class BaseVideoActivity extends ActionBarActivity implements
        OnClickListener {
    public final int DEF_SPEED = 20; // 云台速度（1系设备取值范围1-10，6系设备取值范围1-100）
    // 云台上下左右、自动和停止
    public final int PTZ_UP = 0;
    public final int PTZ_DOWN = 1;
    public final int PTZ_LEFT = 2;
    public final int PTZ_RIGHT = 3;
    public final int PTZ_AUTO = 4;
    public final int PTZ_STOP = 5;
    // 变倍、变焦、光圈
    public final int ZOOM_ADD = 10;
    public final int ZOOM_SUB = 11;
    public final int FOCUS_ADD = 12;
    public final int FOCUS_SUB = 13;
    public final int IRIS_ADD = 14;
    public final int IRIS_SUB = 15;
    private List<CameraInfo> cameraInfos = new ArrayList<CameraInfo>();
    private BaseInfo baseInfo = null;
    private TextView tv_baseName, tv_basePosition;
    private ArrayAdapter<String> adapter;
    private int VideoState = 0;// 视频状态，0关闭，1正在播放
    private Spinner spinner;
    private String curBaseCode;
    private IBaseDev mBaseDev;
    private DBHelper dBHelper;
    private Cursor cursor;
    private ArrayList<String> data = new ArrayList<String>();
    private ToggleButton toggleButton;
    private CameraInfo curCameraInfo = null;
    private ImageView videoview;
    private Dialog addDialog, delDialog;
    private EditText cameraPosition, cameraPosition1, cameraIP, cameraPort;
    private ImageButton stopButton, upButton, downButton, leftButton,
            rightButton, focusAddButton, focusSubButton, fullButton;

    /** 处理视频请求返回的消息 */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            // 消息类型--ConstantInfo.VIDEO_RESPOND_MSG_ID
            int what = msg.what;
            // 消息掩码--ConstantInfo.VIDEO_RESPOND_MSG_MASK
            int arg1 = msg.arg1;
            // 消息返回值--成功、失败
            int arg2 = msg.arg2;
            Log.v("BaseVideo",""+msg.arg2+" "+msg.arg1);
            // 同时匹配消息类型与消息掩码，才认识为自己定义的消息
            if ((arg1 == ConstantInfo.VIDEO_RESPOND_MSG_MASK)
                    && (what == ConstantInfo.VIDEO_RESPOND_MSG_ID)) {
                switch (arg2) {
                    case ConstantInfo.VIDEO_RESPOND_SUCCESS:
                        Toast.makeText(BaseVideoActivity.this, "视频请求成功！", Toast.LENGTH_SHORT).show();
                        break;
                    case ConstantInfo.VIDEO_RESPOND_FAILED:
                        Toast.makeText(BaseVideoActivity.this, "视频请求失败！",
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basevideo);
        Intent intent = this.getIntent();
        baseInfo = (BaseInfo) intent.getSerializableExtra("BaseInfo");
        Log.v("MainActivity", "baseinfo:" + baseInfo);
        curBaseCode = baseInfo.getBaseCode();
        Log.v("MainActivity", "baseinfo:" + baseInfo.getBaseCode());
        updataCamera();
        initLayout();
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        // Log.v("生命周期", "onRestart");
        // Log.v("生命周期", curCameraInfo.toString());
        // if (curCameraInfo != null && mBaseDev == null) {
        // Log.v("生命周期", "jinru li mian");
        // mBaseDev = new DevSixXImplEx(videoview, mHandler,
        // BaseVideoUser.userName, BaseVideoUser.psw,
        // curCameraInfo.getIp(), curCameraInfo.getPort(),
        // 0x100/* 码流类型 */, 0/* 通道号 */);
        // }
        if (VideoState == 1) {
            mBaseDev.startVideo();
        }
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        Log.v("生命周期", "onStop");
        if (this.mBaseDev != null) {
            this.mBaseDev.stopVideo();
            Log.v("BaseVideo", "mBaseDev != null");
            mBaseDev = null;
        }
    }

    private void updataCamera() {
        data.clear();
        cameraInfos.clear();
        if (dBHelper == null) {
            dBHelper = new DBHelper(getApplicationContext());
        }
        cursor = dBHelper.query(curBaseCode);
        while (cursor.moveToNext()) {
            CameraInfo cameraInfo = new CameraInfo();
            cameraInfo.setCameraPosition(cursor.getString(cursor
                    .getColumnIndex("cameraposition")));
            cameraInfo.setBaseCode(cursor.getString(cursor
                    .getColumnIndex("basecode")));
            cameraInfo.setIp(cursor.getString(cursor.getColumnIndex("ip")));
            cameraInfo.setPort(cursor.getInt(cursor.getColumnIndex("port")));
            Log.v("查找到的cameraInfo", cameraInfo.toString());
            cameraInfos.add(cameraInfo);
        }
        for (int i = 0; i < cursor.getCount(); i++) {
            data.add(cameraInfos.get(i).getCameraPosition());
            Log.v("BaseVideo",""+cameraInfos.get(i).getCameraPosition());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        MenuInflater inflater = new MenuInflater(getApplicationContext());
        inflater.inflate(R.menu.basevideo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.action_addCamera:
                // 添加相机位置对话框，最后插入数据库
                addCamera();
                break;
            case R.id.action_deleteCamera:
                // 删除相机位置对话框，从数据库中删除该数据
                delCamera();
                break;
            case android.R.id.home:// 点击返回图标事件
                this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addCamera() {
        // TODO Auto-generated method stub
        addDialog = new Dialog(this);
        addDialog.setTitle("填入详细数据");
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_addcamera, null);
        cameraPosition = (EditText) view.findViewById(R.id.et_cameraPosition);
        cameraIP = (EditText) view.findViewById(R.id.et_cameraIP);
        cameraPort = (EditText) view.findViewById(R.id.et_cameraPort);
        Button addbutton = (Button) view.findViewById(R.id.bt_addcamera);
        Button addbuttoncancel = (Button) view
                .findViewById(R.id.bt_addcameracancle);
        addbutton.setOnClickListener(this);
        addbuttoncancel.setOnClickListener(this);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        addDialog.addContentView(view, layoutParams);
        addDialog.show();
    }

    private void delCamera() {
        // TODO Auto-generated method stub
        delDialog = new Dialog(this);
        delDialog.setTitle("填入要删除的相机位置");
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_delcamera, null);
        cameraPosition1 = (EditText) view.findViewById(R.id.et_cameraPosition1);
        Button addbutton1 = (Button) view.findViewById(R.id.bt_delcamera1);
        Button addbuttoncancel1 = (Button) view
                .findViewById(R.id.bt_delcameracancle1);
        addbutton1.setOnClickListener(this);
        addbuttoncancel1.setOnClickListener(this);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        delDialog.addContentView(view, layoutParams);
        delDialog.show();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
            dBHelper.close();
        }
        if (mBaseDev != null) {
            mBaseDev.stopVideo();
            mBaseDev = null;
        }
    }

    private void initLayout() {
        // TODO Auto-generated method stub
  /*      ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);*/
   /*     actionBar.setTitle("生产现场视频");*/
        stopButton = (ImageButton) findViewById(R.id.ib_stop);
        upButton = (ImageButton) findViewById(R.id.ib_up);
        downButton = (ImageButton) findViewById(R.id.ib_down);
        leftButton = (ImageButton) findViewById(R.id.ib_left);
        rightButton = (ImageButton) findViewById(R.id.ib_right);
        focusAddButton = (ImageButton) findViewById(R.id.ib_focusadd);
        focusSubButton = (ImageButton) findViewById(R.id.ib_focussub);
        fullButton = (ImageButton) findViewById(R.id.ib_fullVideo);
        stopButton.setOnClickListener(this);
        upButton.setOnClickListener(this);
        downButton.setOnClickListener(this);
        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        focusAddButton.setOnClickListener(this);
        focusSubButton.setOnClickListener(this);
        fullButton.setOnClickListener(this);
        tv_baseName = (TextView) findViewById(R.id.tv_baseName2);
        tv_basePosition = (TextView) findViewById(R.id.tv_basePosition2);
        tv_baseName.setText(baseInfo.getBaseName());
        tv_basePosition.setText(baseInfo.getBasePosition());
        spinner = (Spinner) findViewById(R.id.sp_choose_camera_position);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
        videoview = (ImageView) findViewById(R.id.iv_videoview);
        toggleButton = (ToggleButton) findViewById(R.id.tb_switch_video);
        toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if (arg1 == true) {
                    // start
                    if (curCameraInfo != null) {
                        Log.v("BaseVideo", "准备播放，IP：" + curCameraInfo.getIp()
                                + ", Port:" + curCameraInfo.getPort());
                        mBaseDev = new DevSixXImplEx(videoview, mHandler,
                                BaseVideoUser.userName, BaseVideoUser.psw,
                                curCameraInfo.getIp(), curCameraInfo.getPort(),
                                0x100/* 码流类型 */, 0/* 通道号 */);
                        mBaseDev.startVideo();
                        Log.v("BaseVideo"," mBaseDev.startVideo();");
                        VideoState = 1;
                        spinner.setEnabled(false);
                    } else {
                        Toast.makeText(getApplicationContext(), "未找到可用的相机！",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // stop
                    if (mBaseDev != null) {
                        mBaseDev.stopVideo();
                        fullButton.setVisibility(View.INVISIBLE);
                        VideoState = 0;
                        spinner.setEnabled(true);
                    }
                }
            }
        });
    }

    class SpinnerSelectedListener implements OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            // TODO Auto-generated method stub
            // 根据data[arg2]找出cameraName，从数据库中查找ip和port
            curCameraInfo = cameraInfos.get(arg2);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        if (mBaseDev != null) {
            mBaseDev.stopVideo();
        }
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.ib_stop:
                if(VideoState==1){
                    mBaseDev.startCtrlPTZ(getPtzCmd(PTZ_STOP, 6), DEF_SPEED);
                    break;
                }
                Toast.makeText(BaseVideoActivity.this, "请先开始播放实时视频！", Toast.LENGTH_SHORT).show();
               break;
            case R.id.ib_up:
                if(VideoState==1){
                    mBaseDev.startCtrlPTZ(getPtzCmd(PTZ_UP, 6), DEF_SPEED);
                    break;
                }
                Toast.makeText(BaseVideoActivity.this, "请先开始播放实时视频！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_down:
                if(VideoState==1){
                    mBaseDev.startCtrlPTZ(getPtzCmd(PTZ_DOWN, 6), DEF_SPEED);
                    break;
                }
                Toast.makeText(BaseVideoActivity.this, "请先开始播放实时视频！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_left:
                if(VideoState==1){
                    mBaseDev.startCtrlPTZ(getPtzCmd(PTZ_LEFT, 6), DEF_SPEED);
                    break;
                }
                Toast.makeText(BaseVideoActivity.this, "请先开始播放实时视频！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_right:
                if(VideoState==1){
                    mBaseDev.startCtrlPTZ(getPtzCmd(PTZ_RIGHT, 6), DEF_SPEED);
                    break;
                }
                Toast.makeText(BaseVideoActivity.this, "请先开始播放实时视频！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_focusadd:
                if(VideoState==1){
                    mBaseDev.startCtrlPTZ(getPtzCmd(ZOOM_ADD, 6), DEF_SPEED);
                    break;
                }
                Toast.makeText(BaseVideoActivity.this, "请先开始播放实时视频！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_focussub:
                if(VideoState==1){
                    mBaseDev.startCtrlPTZ(getPtzCmd(ZOOM_SUB, 6), DEF_SPEED);
                    break;
                }
                Toast.makeText(BaseVideoActivity.this, "请先开始播放实时视频！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_addcamera:
                // 检查输入
                // 插入数据库
                // 更新数据和界面
                if (BaseList.isEmpty(cameraPosition, cameraIP, cameraPort)) {
                    if (dBHelper == null) {
                        dBHelper = new DBHelper(getApplicationContext());
                    }
                    ContentValues valu = new ContentValues();
                    valu.putNull("_cameraid");
                    valu.put("cameraposition", cameraPosition.getText().toString());
                    valu.put("ip", cameraIP.getText().toString());
                    valu.put("port",
                            Integer.valueOf(cameraPort.getText().toString()));
                    valu.put("basecode", curBaseCode);
                    dBHelper.insert("cameraInfoTb", valu);
                    Toast.makeText(this, "成功！", Toast.LENGTH_SHORT).show();
                    addDialog.dismiss();
                    updataCamera();
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "你还有数据没有填写！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_addcameracancle:
                addDialog.dismiss();
                break;
            case R.id.bt_delcamera1:
                // 检查输入
                // 从数据库中删除
                // 更新界面
                if (BaseList.isEmpty(cameraPosition1)) {
                    if (dBHelper == null) {
                        dBHelper = new DBHelper(getApplicationContext());
                    }
                    dBHelper.del("cameraInfoTb", cameraPosition1.getText()
                            .toString().trim());
                    delDialog.dismiss();
                    updataCamera();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "已删除！",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "你还有数据没有填写！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_delcameracancle1:
                delDialog.dismiss();
                break;
            // case R.id.ib_fullVideo:
            // Intent intent = new Intent();

            // intent.putExtra("curCameraInfo", curCameraInfo);
            // startActivity(intent);
            // break;
            default:
                break;
        }
    }

    public void setFullButton(View v) {
        if (VideoState == 1) {
            if (fullButton.getVisibility() == View.INVISIBLE) {
                fullButton.setVisibility(View.VISIBLE);
            } else {
                fullButton.setVisibility(View.INVISIBLE);
            }
        }
    }

    public int getPtzCmd(int cmd, int devTypes) {
        int ptzCmd = 0;
        switch (cmd) {
            case PTZ_UP: {
                if (devTypes == ConstantInfo.DEV_TYPES_SIX_X) {
                    ptzCmd = MsgConstantDevSix.YT_UP;
                } else if (devTypes == ConstantInfo.DEV_TYPES_ONE_X) {
                    ptzCmd = MsgConstantDevOne.PTZ_UP;
                }
            }
            break;
            case PTZ_DOWN: {
                if (devTypes == ConstantInfo.DEV_TYPES_SIX_X) {
                    ptzCmd = MsgConstantDevSix.YT_DOWN;
                } else if (devTypes == ConstantInfo.DEV_TYPES_ONE_X) {
                    ptzCmd = MsgConstantDevOne.PTZ_DOWN;
                }
            }
            break;
            case PTZ_LEFT: {
                if (devTypes == ConstantInfo.DEV_TYPES_SIX_X) {
                    ptzCmd = MsgConstantDevSix.YT_LEFT;
                } else if (devTypes == ConstantInfo.DEV_TYPES_ONE_X) {
                    ptzCmd = MsgConstantDevOne.PTZ_LEFT;
                }
            }
            break;

            case PTZ_RIGHT: {
                if (devTypes == ConstantInfo.DEV_TYPES_SIX_X) {
                    ptzCmd = MsgConstantDevSix.YT_RIGHT;
                } else if (devTypes == ConstantInfo.DEV_TYPES_ONE_X) {
                    ptzCmd = MsgConstantDevOne.PTZ_RIGHT;
                }
            }
            break;

            case PTZ_AUTO: {
                if (devTypes == ConstantInfo.DEV_TYPES_SIX_X) {
                    ptzCmd = MsgConstantDevSix.YT_AUTOOPEN;
                } else if (devTypes == ConstantInfo.DEV_TYPES_ONE_X) {
                    ptzCmd = MsgConstantDevOne.PTZ_AUTO;
                }
            }
            break;

            case PTZ_STOP: {
                if (devTypes == ConstantInfo.DEV_TYPES_SIX_X) {
                    ptzCmd = MsgConstantDevSix.YT_AUTOCLOSE;
                } else if (devTypes == ConstantInfo.DEV_TYPES_ONE_X) {
                    ptzCmd = MsgConstantDevOne.PTZ_STOP;
                }
            }
            break;

            case ZOOM_ADD: {
                if (devTypes == ConstantInfo.DEV_TYPES_SIX_X) {
                    ptzCmd = MsgConstantDevSix.YT_ZOOMADD;
                } else if (devTypes == ConstantInfo.DEV_TYPES_ONE_X) {
                    ptzCmd = MsgConstantDevOne.PTZ_ZOOMADD;
                }
            }
            break;
            case ZOOM_SUB: {
                if (devTypes == ConstantInfo.DEV_TYPES_SIX_X) {
                    ptzCmd = MsgConstantDevSix.YT_ZOOMSUB;
                } else if (devTypes == ConstantInfo.DEV_TYPES_ONE_X) {
                    ptzCmd = MsgConstantDevOne.PTZ_ZOOMDEC;
                }
            }
            break;
            case FOCUS_ADD: {
                if (devTypes == ConstantInfo.DEV_TYPES_SIX_X) {
                    ptzCmd = MsgConstantDevSix.YT_FOCUSADD;
                } else if (devTypes == ConstantInfo.DEV_TYPES_ONE_X) {
                    ptzCmd = MsgConstantDevOne.PTZ_FOCUSADD;
                }
            }
            break;

            case FOCUS_SUB: {
                if (devTypes == ConstantInfo.DEV_TYPES_SIX_X) {
                    ptzCmd = MsgConstantDevSix.YT_FOCUSSUB;
                } else if (devTypes == ConstantInfo.DEV_TYPES_ONE_X) {
                    ptzCmd = MsgConstantDevOne.PTZ_FOCUSDEC;
                }
            }
            break;

            case IRIS_ADD: {
                if (devTypes == ConstantInfo.DEV_TYPES_SIX_X) {
                    ptzCmd = MsgConstantDevSix.YT_IRISADD;
                } else if (devTypes == ConstantInfo.DEV_TYPES_ONE_X) {
                    ptzCmd = MsgConstantDevOne.PTZ_IRISADD;
                }
            }
            break;

            case IRIS_SUB: {
                if (devTypes == ConstantInfo.DEV_TYPES_SIX_X) {
                    ptzCmd = MsgConstantDevSix.YT_IRISSUB;
                } else if (devTypes == ConstantInfo.DEV_TYPES_ONE_X) {
                    ptzCmd = MsgConstantDevOne.PTZ_IRISDEC;
                }
            }
            break;

            default:
                if (devTypes == ConstantInfo.DEV_TYPES_SIX_X) {
                    ptzCmd = MsgConstantDevSix.YT_AUTOCLOSE;
                } else if (devTypes == ConstantInfo.DEV_TYPES_ONE_X) {
                    ptzCmd = MsgConstantDevOne.PTZ_STOP;
                }
                break;
        }

        return ptzCmd;
    }

}
