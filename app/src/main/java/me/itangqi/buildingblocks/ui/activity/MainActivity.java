package me.itangqi.buildingblocks.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.v5kf.client.lib.V5ClientAgent;
import com.v5kf.client.lib.V5ClientConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.itangqi.buildingblocks.R;
import me.itangqi.buildingblocks.ui.activity.base.BaseActivity;
import me.itangqi.buildingblocks.ui.fragment.BaseFragment;
import me.itangqi.buildingblocks.ui.fragment.MarketFragment;
import me.itangqi.buildingblocks.ui.fragment.NongjifuwuListFragment;
import me.itangqi.buildingblocks.ui.fragment.NoticeListFragment;
import me.itangqi.buildingblocks.ui.fragment.PriceListFragment;
import me.itangqi.buildingblocks.utils.Constants;

public class MainActivity extends BaseActivity {

    @Bind(R.id.drawer_layout) DrawerLayout mDrawerLayout; //侧滑 实现抽屉效果
    @Bind(R.id.coordinatorLayout) CoordinatorLayout mContainer;// 作为顶层布局  调度协调子布局
    @Bind(R.id.navigation_view) NavigationView mNavigationView;// 侧滑菜单
    @Bind(R.id.tabs) TabLayout mTabLayout;
    @Bind(R.id.pager) ViewPager mViewPager; //不同layout容器
    @Bind(R.id.toolbar) Toolbar mToolbar;
    private long exitTime = 0;
    SharedPreferences sp;

    //网络加载
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResID = R.layout.activity_main;
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this); //findViewById
        sp = this.getSharedPreferences("userinfo", MODE_ENABLE_WRITE_AHEAD_LOGGING);
        IsFristLaunch();
        initV5Chat();


        if (mNavigationView != null) {
            setupDrawerContent();
        }

        setupActionBarToggle();

        if (mViewPager != null) {
            setupViewPager();
        }
    }

    private void initV5Chat() {
        // V5客服系统客户端配置
        V5ClientConfig config = V5ClientConfig.getInstance(MainActivity.this);
        V5ClientConfig.USE_HTTPS = true; // 使用加密连接，默认true
        V5ClientConfig.SOCKET_TIMEOUT = 20000; // 请求超时时间
        config.setShowLog(true); // 是否打印日志， 默认为true
        config.setHeartBeatEnable(true); // 是否允许发送心跳包保活
        config.setHeartBeatTime(30000); // 心跳包间隔时间ms
        config.setLogLevel(V5ClientConfig.LOG_LV_DEBUG); // 日志级别默认为全部显示

        //config.setUid("用户ID字符串"); // 【必须】 ,设置用户ID，区分APP登录的不同账号
        config.setNickname("android_Test1"); // 设置用户昵称
        config.setGender(1); // 设置用户性别
        config.setAvatar("http://debugimg-10013434.image.myqcloud.com/fe1382d100019cfb572b1934af3d2c04/thumbnail"); // 设置用户头像URL
        config.setDefaultServiceByWorker(false); // 是否默认转人工客服
        config.setVip(0);
    }

    private void IsFristLaunch() {

        SharedPreferences preferences = getSharedPreferences("APP_TAG",
                Context.MODE_PRIVATE);
        Boolean user_first = preferences.getBoolean("FIRST", true);
        if (user_first) {// 第一次
            preferences.edit().putBoolean("FIRST", false).commit();
            imporDatabase();
        }
    }

    private void imporDatabase() {
        // 存放数据库的目录
        //Log.v("MainActivity", "复制数据库1");
        String dirPath = "/data/data/me.itangqi.buildingblocks/databases";
        File dir = new File(dirPath);
        //Log.v("MainActivity", "复制数据库1"+dir.toString());
        if (!dir.exists()) {
            dir.mkdir();
            //Log.v("MainActivity", "复制数据库2");
        }
        // 数据库文件
        File file = new File(dir, "base.db");
        try {
            if (!file.exists()) {
                file.createNewFile();
                //Log.v("MainActivity", "复制数据库3");
            }
            // 加载需要导入的数据库
            InputStream is = this.getApplicationContext().getResources()
                    .openRawResource(R.raw.base20160728);
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffere = new byte[is.available()];
            is.read(buffere);
            fos.write(buffere);
            is.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //侧滑其他设置选项连接
    private void setupDrawerContent() {
        //选中监听
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        if (menuItem.isChecked()) menuItem.setChecked(false);
                        else menuItem.setChecked(true);
                        //选中后关闭侧滑界面
                        mDrawerLayout.closeDrawers();
                        switch (menuItem.getItemId()) {
                            case R.id.nav_settings:
                                return prepareIntent(PrefsActivity.class);
                            case R.id.nav_about:
                                return prepareIntent(AboutActivity.class);
                            case R.id.nav_ask:
                                 V5ClientAgent.getInstance().startV5ChatActivity(getApplicationContext());
                                return true;
                            case R.id.nav_video:
                                return prepareIntent(cn.edu.nwsuaf.cie.vs.SplashActivity.class);
                            default:
                                return true;
                        }
                    }
                });
    }

    //侧滑菜单
    private void setupActionBarToggle() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.openDrawerContentDescRes, R.string.closeDrawerContentDescRes) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();
    }

    private void setupViewPager() {
        mViewPager.setOffscreenPageLimit(Constants.PAGE_COUNT);
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }


    //快捷设置
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //操作选项
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.men_action_logout:
                Logout();
                return true;
            case R.id.menu_action_exit:
                finish();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }


    //注销
    private void Logout() {
       SharedPreferences.Editor editer = sp.edit();
        editer.putBoolean("AUTO_ISCHECK",false);//忘记密码
        editer.putString("PASSWORD","");
        editer.commit();

        finish();
        startActivity(new Intent(MainActivity.this,LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    private boolean prepareIntent(Class Activity) {
        startActivity(new Intent(MainActivity.this, Activity));
        return true;
    }

    /**
     * 实现再按一次退出提醒
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 3000) {
                Snackbar.make(mContainer, R.string.snack_exit_once_more, Snackbar.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                sp.edit().putBoolean("CONNECT",false).commit();
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        //标签下对应的项目
        @Override
        public Fragment getItem(int i) {
            Bundle bundle = new Bundle();
            Fragment newFragment = null;

            switch (i)
            {
                case 0:
                    newFragment = new NoticeListFragment();
                    break;
                case 1:
                    newFragment = new MarketFragment();
                    break;
            case 2:
                    newFragment = new BaseFragment();
                    break;
                case 3:
                    newFragment = new NongjifuwuListFragment();
                    break;
                case 4:
                    newFragment = new PriceListFragment();
                    break;
                default:
                    break;
            }
            return newFragment;
        }

        //设置标签数量
        @Override
        public int getCount() {
            return Constants.PAGE_COUNT;
        }

        //得到标签的标题
        @Override
        public CharSequence getPageTitle(int position) {
            String titles[] = new String[]{"    产业资讯    ", "    市场行情    ", "    示范站    ", "    农技服务    ","    价格云    "};

            return titles[position];
        }
    }
}
