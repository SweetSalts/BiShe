package me.itangqi.buildingblocks.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.itangqi.buildingblocks.R;

/**
 * 登陆界面
 */
public class LoginActivity extends AppCompatActivity {


    @Bind(R.id.username)
    EditText username;
    @Bind(R.id.userpassword)
    EditText userpassword;
   /* @Bind(R.id.remember)
    CheckBox remember;*/
    @Bind(R.id.autologin)
    CheckBox autologin;
    @Bind(R.id.mybtlogin)
    Button mybtlogin;
    @Bind(R.id.mybtregister)
    Button mybtregister;
    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        ButterKnife.bind(this);


        sp = this.getSharedPreferences("userinfo", MODE_ENABLE_WRITE_AHEAD_LOGGING);}//获得实例对象


    //登陆
    private void onClickListener_login() {
        String stUserName = username.getText().toString();
        String stPassWord = userpassword.getText().toString();

        //登陆
        if (TestLogin(stUserName, stPassWord)) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("USER_NAME", stUserName);
            editor.putString("PASSWORD", stPassWord);
            editor.putBoolean("CONNECT",true);
            editor.commit();
            //进入主界面
            Intent userActivity = new Intent(LoginActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(userActivity);
            finish();//结束当前页面
        } else {
            Snackbar.make(getWindow().getDecorView(), R.string.login_error_login, Snackbar.LENGTH_SHORT).show();
            //否则提示失败
        }
    }
    //注册界面
    private void onClickListener_register() {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private boolean TestLogin(String userName, String passWord) {

        return true;
    }

    //再按一次退出键退出程序
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Snackbar.make(getWindow().getDecorView(),R.string.snack_exit_once_more,Snackbar.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //触摸编辑框外时自动收起软键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    //按钮响应时间
    @OnClick({R.id.mybtlogin, R.id.mybtregister})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mybtlogin:  //登陆
                onClickListener_login();
                break;
            case R.id.mybtregister: //注册
                onClickListener_register();
                break;
        }
    }
}
