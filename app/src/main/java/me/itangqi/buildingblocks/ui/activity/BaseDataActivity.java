package me.itangqi.buildingblocks.ui.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import me.itangqi.buildingblocks.R;
import me.itangqi.buildingblocks.utils.base.BaseInfo;
import me.itangqi.buildingblocks.utils.base.DataInfo;

/**
 * Created by oreo on 2016/7/20.
 */
public class BaseDataActivity extends Activity {
    private TextView tv_update, tv_baseName, tv_basePosition, tv_taAvg,
            tv_taSoilAvg, tv_rhAvg, tv_vwcAvg, tv_rainTot, tv_pvaporAvg, tv_wd,
            tv_wsAvg, tv_slrwAvg, tv_ptemp, tv_battVoltMin;
    private ImageView iv_arraw;
    private BaseInfo baseInfo = null;
    private DataInfo dataInfo = null;
    private String url = "http://sf.nwsuaf.edu.cn/weather/realtime/";
    private ProgressDialog progressDialog;
    private ProgressBar raAvgProgressBar, ahAvgProgressBar, rainPotProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basedata);
        Intent intent = this.getIntent();
        baseInfo = (BaseInfo) intent.getSerializableExtra("BaseInfo");
        url = url + baseInfo.getBaseCode();
        Log.v("MainActivity"," "+url);
        initLayout();
        GetDataTask dataTask = new GetDataTask(url);
        dataTask.execute();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        progressDialog = null;
    }

    // 设置动画效果
    private void startAnim(float wd, double taAvg, double rhAvg, double rainTot) {
        Animation animation = new RotateAnimation(0f, wd,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        Log.v("taavg", taAvg + "");
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(raAvgProgressBar,
                "progress", 0, (int) (taAvg + 50));
        objectAnimator.setDuration(1000);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofInt(ahAvgProgressBar,
                "progress", 0, (int) rhAvg);
        objectAnimator1.setDuration(1000);
        objectAnimator1.setInterpolator(new AccelerateDecelerateInterpolator());
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofInt(
                rainPotProgressBar, "progress", 0, (int) rainTot);
        objectAnimator2.setDuration(1000);
        objectAnimator2.setInterpolator(new AccelerateDecelerateInterpolator());
        iv_arraw.startAnimation(animation);// 开始动画
        objectAnimator.start();
        objectAnimator1.start();
        objectAnimator2.start();
    }

    // 获取田间信息并将信息显示在界面上的异步任务
    class GetDataTask extends AsyncTask<Void, Void, DataInfo> {
        private String url;

        public GetDataTask(String url) {
            // TODO Auto-generated constructor stub
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
//            progressDialog.show();
        }


        @Override
        protected DataInfo doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            String str = getDataFormSocket(url);

            if (!str.equals("") && str.indexOf("{") == 0) {
               // str.indexOf("{") == 0;
                // 解决运营商网络下返回str并不是空的bug

            }
            dataInfo = readString(str);
            Log.v("MainActivity","return dataInfo");
            return dataInfo;
           // return null;
        }


        //onPostExecute(Result)  相当于Handler 处理UI的方式，在这里面可以使用在doInBackground 得到的
        // 结果处理操作UI。 此方法在主线程执行，任务执行的结果作为此方法的参数返回

        @Override
        protected void onPostExecute(DataInfo result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if(result==null){
                Log.v("BaseDateActivity","result为空");
            }
            if (result == null) {
                Toast.makeText(BaseDataActivity.this, "获取数据失败！",
                        Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            } else {
                tv_update.setText("更新时间:" + result.getUpdateTime());
                tv_taAvg.setText(result.getTaAvg() + "");
                tv_taSoilAvg.setText(result.getTaSoilAvg() + "");
                tv_rhAvg.setText(result.getRhAvg() + "");
                tv_vwcAvg.setText(result.getVwcAvg() + "");
                tv_rainTot.setText(result.getRainTot() + "");
                tv_pvaporAvg.setText(result.getPvaporAvg() + "");
                tv_wd.setText(result.getWd() + "");
                tv_wsAvg.setText(result.getWsAvg() + "");
                tv_slrwAvg.setText(result.getSlrwAvg() + "");
                tv_ptemp.setText(result.getPtemp() + "");
                tv_battVoltMin.setText(result.getBattVoltMin() + "");
                progressDialog.dismiss();
                startAnim((float) result.getWd(), result.getTaAvg(),
                        result.getRhAvg(), result.getRainTot());
            }
        }
    }

    private DataInfo readString(String str) {// 从String中解析出一个DataInfo对象
        DataInfo dataInfo = new DataInfo();
        //解析gson方法：
//        Type listType = new TypeToken<LinkedList<DataInfo>>(){}.getType();
//        Gson gson = new Gson();
//        LinkedList<DataInfo> users = gson.fromJson(str, listType);
//        for (Iterator iterator = users.iterator(); iterator.hasNext();) {
//            DataInfo user = (DataInfo) iterator.next();
//            System.out.println(user.getUsername());
//            System.out.println(user.getUserId());

        //String jsonData = "{\"username\":\"arthinking\",\"userId\":001}";
        Gson gson = new Gson();
        dataInfo = gson.fromJson(str, DataInfo.class);


        // 直接读取字符串方法
//        dataInfo.setUpdateTime(str.substring(str.indexOf("tmstamp") + 10,
//                str.indexOf("taAvg") - 3));
//        Log.v("MainActivity"," "+Double.valueOf(str.substring(
//                str.indexOf("taAvg") + 7, str.indexOf("rhAvg") - 2)));
//        dataInfo.setTaAvg(Double.valueOf(str.substring(
//                str.indexOf("taAvg") + 7, str.indexOf("rhAvg") - 2)));
//        dataInfo.setRhAvg(Double.valueOf(str.substring(
//                str.indexOf("rhAvg") + 7, str.indexOf("taSoilAvg") - 2)));
//        dataInfo.setTaSoilAvg(Double.valueOf(str.substring(
//                str.indexOf("taSoilAvg") + 11, str.indexOf("slrwAvg") - 2)));
//
//        dataInfo.setSlrwAvg(Double.valueOf(str.substring(
//                str.indexOf("slrwAvg") + 9, str.indexOf("wd") - 2)));
//
//        dataInfo.setWd(Double.valueOf(str.substring(str.indexOf("wd") + 4,
//                str.indexOf("wsAvg") - 2)));
//
//        dataInfo.setWsAvg(Double.valueOf(str.substring(
//                str.indexOf("wsAvg") + 7, str.indexOf("rainTot") - 2)));
//
//        dataInfo.setRainTot(Double.valueOf(str.substring(
//                str.indexOf("rainTot") + 9, str.indexOf("recnum") - 2)));
//
//        dataInfo.setVwcAvg(Double.valueOf(str.substring(
//                str.indexOf("vwcAvg") + 8, str.indexOf("tmstamp") - 2)));
//
//        dataInfo.setBattVoltMin(Double.valueOf(str.substring(
//                str.indexOf("battVoltMin") + 13, str.indexOf("ptemp") - 2)));
//        dataInfo.setPtemp(Double.valueOf(str.substring(
//                str.indexOf("ptemp") + 7, str.indexOf("pvaporAvg") - 2)));
//        dataInfo.setPvaporAvg(Double.valueOf(str.substring(
//                str.indexOf("pvaporAvg") + 11, str.indexOf("wsAvgStd") - 2)));
        return dataInfo;
    }
    private String getDataFormSocket(String url) {
        // TODO Auto-generated method stub
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            tv_update.setText("22");
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            if (result == "") {
                Toast.makeText(this, "服务器没有应答！", Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    private void initLayout() {
        // TODO Auto-generated method stub
       /* ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("田间监测信息");*/
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在获取数据...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        tv_baseName = (TextView) findViewById(R.id.tv_baseName1);
        tv_basePosition = (TextView) findViewById(R.id.tv_basePosition1);
        tv_update = (TextView) findViewById(R.id.tv_update);
        tv_taAvg = (TextView) findViewById(R.id.tv_taAvg1);
        tv_taSoilAvg = (TextView) findViewById(R.id.tv_taSoilAvg1);
        tv_rhAvg = (TextView) findViewById(R.id.tv_rhAvg1);
        tv_vwcAvg = (TextView) findViewById(R.id.tv_vwcAvg1);
        tv_rainTot = (TextView) findViewById(R.id.tv_rainTot1);
        tv_pvaporAvg = (TextView) findViewById(R.id.tv_pvaporAvg1);
        tv_wd = (TextView) findViewById(R.id.tv_wd1);
        tv_wsAvg = (TextView) findViewById(R.id.tv_wsAvg1);
        tv_slrwAvg = (TextView) findViewById(R.id.tv_slrwAvg1);
        tv_ptemp = (TextView) findViewById(R.id.tv_ptemp1);
        tv_battVoltMin = (TextView) findViewById(R.id.tv_battVoltMin1);
        tv_baseName.setText(baseInfo.getBaseName());
        tv_basePosition.setText(baseInfo.getBasePosition());
        iv_arraw = (ImageView) findViewById(R.id.iv_arraw);
        raAvgProgressBar = (ProgressBar) findViewById(R.id.vpb_taAvg);
        ahAvgProgressBar = (ProgressBar) findViewById(R.id.vpb_rhAvg);
        rainPotProgressBar = (ProgressBar) findViewById(R.id.vpb_rainPot);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.basedata, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.action_refresh:
                GetDataTask dataTask = new GetDataTask(url);
                dataTask.execute();
                break;
            case android.R.id.home:// 点击返回图标事件
                this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
