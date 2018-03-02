package me.itangqi.buildingblocks.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import me.itangqi.buildingblocks.ui.view.ScrollTableView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.itangqi.buildingblocks.R;
import me.itangqi.buildingblocks.utils.base.DataInfo;
import me.itangqi.buildingblocks.utils.PriceInfo;

/**
 * Created by oreo on 2016/9/25.
 */
public class PriceTableActicity extends Activity{

    private static final String[] topTitles = new String[] {"尺寸", "储存方式", "零售价格", "预测价格"};

    private ScrollTableView scrollTableView;
    private String url;
    private String logmin;
    private String logmax;
    private Handler handler=new Handler();
    private TextView et2;
    private ProgressDialog progressDialog;
    private String str;
    private PriceInfo priceinfo;
    ArrayList<String> leftTitle;
    private int BIAOZHI=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_scrollview);
        et2= (TextView) findViewById(R.id.pricetable_et);


        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        url = bundle.getString("url");
        logmin=bundle.getString("logmin");
        logmax=bundle.getString("logmax");
        initLayout();
        GetPriceTask dataTask = new GetPriceTask(url);
        dataTask.execute();
//        new HTTPThread(url, et2,handler).start();
//        Log.i("textView", "测试");
        scrollTableView = (ScrollTableView) findViewById(R.id.scroll_table_view);
        leftTitle = createLeftTitle();
        ArrayList<String>  topTitles = createTopTitles();
       scrollTableView.setDatas(createTopTitles(), createLeftTitle(), createContent(leftTitle.size(), topTitles.size()));
    }

    private void initLayout() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在获取数据...");


    }

    private ArrayList<ArrayList<String>> createContent(int row, int column) {
        ArrayList<ArrayList<String>> results = new ArrayList<>();
        Log.i("aaaa","BIAOZHI"+BIAOZHI);
        if((BIAOZHI==0)||(BIAOZHI==2) ){
            for (int i = 0; i < row; i++) {
                ArrayList<String> strings = new ArrayList<>();
                for (int j = 0; j < column; j++) {
                    if ((j == 0) && (i == 0)) {
                        strings.add("70#-75#");
                    } else if ((j == 0) && (i == 1)) {
                        strings.add("70#以下");
                    } else if ((j == 0) && (i == 2)) {
                        strings.add("75#-80#");
                    } else if ((j == 0) && (i == 3)) {
                        strings.add("80#-85#");
                    } else if ((j == 0) && (i == 4)) {
                        strings.add("70#-75#");
                    } else if ((j == 0) && (i == 5)) {
                        strings.add("70#以下");
                    } else if ((j == 0) && (i == 6)) {
                        strings.add("75#-80#");
                    } else if ((j == 0) && (i == 7)) {
                        strings.add("80#-85#");
                    } else if (j == 1) {
                        strings.add("冷藏");
                    } else {
                        strings.add("暂无数据");
                    }


                }
                results.add(strings);
            }
            return results;
        }else if(BIAOZHI==1){
//            str=str.substring(str.indexOf("[") , str.indexOf("]")+1 );
//            Log.i("aaaa","现在str"+str);
//            Gson gson = new Gson();
//            List<Priceinfotest> listJsonBooks = gson.fromJson(gson.toJson(str),
//                    new TypeToken<List<Priceinfotest>>() {
//                    }.getType());
          //  for (Priceinfotest book : listJsonBooks) {

//            final GsonBuilder gsonBuilder = new GsonBuilder();
//            final Gson gson = gsonBuilder.create();
//
//            Priceinfotest[] testCase = gson.fromJson(str, Priceinfotest[].class);

              //  Log.i("aaaa","Wholesaleprice:" + listJsonBooks.toString() + "Forecastprice:"

           // }

            String jiage;
            for (int i = 0; i < row; i++) {
                ArrayList<String> strings = new ArrayList<>();
                for (int j = 0; j < column; j++) {
                    if ((j == 0) && (i == 0)) {
                        strings.add("70#-75#");
                    } else if ((j == 0) && (i == 1)) {
                        strings.add("70#以下");
                    } else if ((j == 0) && (i == 2)) {
                        strings.add("75#-80#");
                    } else if ((j == 0) && (i == 3)) {
                        strings.add("80#-85#");
                    } else if ((j == 0) && (i == 4)) {
                        strings.add("70#-75#");
                    } else if ((j == 0) && (i == 5)) {
                        strings.add("70#以下");
                    } else if ((j == 0) && (i == 6)) {
                        strings.add("75#-80#");
                    } else if ((j == 0) && (i == 7)) {
                        strings.add("80#-85#");
                    } else if (j == 1) {
                        strings.add("冷藏");
                    }else if((i==0)&&(j==2)){         //dataInfo.setTaAvg(Double.valueOf(str.substring(
                                                    //     str.indexOf("taAvg") + 7, str.indexOf("rhAvg") - 2)));
                        Log.i("aaaa","(i==0)&&(j==2)"+str);
                        jiage=str.substring(str.indexOf("wholesaleprice") + 17, str.indexOf("wholesaleprice") +20);
                        strings.add(jiage);
                    }
                    else if((i==0)&&(j==3)){         //dataInfo.setTaAvg(Double.valueOf(str.substring(
                        //     str.indexOf("taAvg") + 7, str.indexOf("rhAvg") - 2)));

                        jiage=str.substring(str.indexOf("forecastprice") + 16, str.indexOf("forecastprice") +19);
                        str=str.substring(str.indexOf("wholesaleprice") +3 , str.indexOf("]")+1 );
                        Log.i("aaaa","new str"+str);
                        strings.add(jiage);
                    }else if((i==1)&&(j==2)){         //dataInfo.setTaAvg(Double.valueOf(str.substring(
                        //     str.indexOf("taAvg") + 7, str.indexOf("rhAvg") - 2)));
                        jiage=str.substring(str.indexOf("wholesaleprice") + 17, str.indexOf("wholesaleprice") +20);
                        strings.add(jiage);

                    }else if((i==1)&&(j==3)){         //dataInfo.setTaAvg(Double.valueOf(str.substring(
                        //     str.indexOf("taAvg") + 7, str.indexOf("rhAvg") - 2)));
                        jiage=str.substring(str.indexOf("forecastprice") + 16, str.indexOf("forecastprice") +19);
                        str=str.substring(str.indexOf("wholesaleprice") +3 , str.indexOf("]")+1 );
                        strings.add(jiage);
                    }else if((i==2)&&(j==2)){         //dataInfo.setTaAvg(Double.valueOf(str.substring(
                        //     str.indexOf("taAvg") + 7, str.indexOf("rhAvg") - 2)));
                        jiage=str.substring(str.indexOf("wholesaleprice") + 17, str.indexOf("wholesaleprice") +20);
                        strings.add(jiage);
                    }else if((i==2)&&(j==3)){         //dataInfo.setTaAvg(Double.valueOf(str.substring(
                        //     str.indexOf("taAvg") + 7, str.indexOf("rhAvg") - 2)));
                        jiage=str.substring(str.indexOf("forecastprice") + 16, str.indexOf("forecastprice") +19);
                        str=str.substring(str.indexOf("wholesaleprice") +3 , str.indexOf("]")+1 );
                        strings.add(jiage);
                    }else if((i==3)&&(j==2)){         //dataInfo.setTaAvg(Double.valueOf(str.substring(
                        //     str.indexOf("taAvg") + 7, str.indexOf("rhAvg") - 2)));
                        jiage=str.substring(str.indexOf("wholesaleprice") + 17, str.indexOf("wholesaleprice") +20);
                        strings.add(jiage);
                    }else if((i==3)&&(j==3)){         //dataInfo.setTaAvg(Double.valueOf(str.substring(
                        //     str.indexOf("taAvg") + 7, str.indexOf("rhAvg") - 2)));
                        jiage=str.substring(str.indexOf("forecastprice") + 16, str.indexOf("forecastprice") +19);
                        str=str.substring(str.indexOf("wholesaleprice") +3 , str.indexOf("]")+1 );
                        strings.add(jiage);
                    }else if((i==4)&&(j==2)){         //dataInfo.setTaAvg(Double.valueOf(str.substring(
                        //     str.indexOf("taAvg") + 7, str.indexOf("rhAvg") - 2)));
                        jiage=str.substring(str.indexOf("wholesaleprice") + 17, str.indexOf("wholesaleprice") +20);
                        strings.add(jiage);
                    }else if((i==4)&&(j==3)){         //dataInfo.setTaAvg(Double.valueOf(str.substring(
                        //     str.indexOf("taAvg") + 7, str.indexOf("rhAvg") - 2)));
                        jiage=str.substring(str.indexOf("forecastprice") + 16, str.indexOf("forecastprice") +19);
                        str=str.substring(str.indexOf("wholesaleprice") +3 , str.indexOf("]")+1 );
                        strings.add(jiage);
                    }else if((i==5)&&(j==2)){         //dataInfo.setTaAvg(Double.valueOf(str.substring(
                        //     str.indexOf("taAvg") + 7, str.indexOf("rhAvg") - 2)));
                        jiage=str.substring(str.indexOf("wholesaleprice") + 17, str.indexOf("wholesaleprice") +20);
                        strings.add(jiage);
                    }else if((i==5)&&(j==3)){         //dataInfo.setTaAvg(Double.valueOf(str.substring(
                        //     str.indexOf("taAvg") + 7, str.indexOf("rhAvg") - 2)));
                        jiage=str.substring(str.indexOf("forecastprice") + 16, str.indexOf("forecastprice") +19);
                        str=str.substring(str.indexOf("wholesaleprice") +3 , str.indexOf("]")+1 );
                        strings.add(jiage);
                    }else if((i==6)&&(j==2)){         //dataInfo.setTaAvg(Double.valueOf(str.substring(
                        //     str.indexOf("taAvg") + 7, str.indexOf("rhAvg") - 2)));
                        jiage=str.substring(str.indexOf("wholesaleprice") + 17, str.indexOf("wholesaleprice") +20);
                        strings.add(jiage);
                    }
                    else if((i==6)&&(j==3)){         //dataInfo.setTaAvg(Double.valueOf(str.substring(
                        //     str.indexOf("taAvg") + 7, str.indexOf("rhAvg") - 2)));
                        jiage=str.substring(str.indexOf("forecastprice") + 16, str.indexOf("forecastprice") +19);
                        str=str.substring(str.indexOf("wholesaleprice") +3 , str.indexOf("]")+1 );
                        strings.add(jiage);
                    }else if((i==7)&&(j==2)){         //dataInfo.setTaAvg(Double.valueOf(str.substring(
                        //     str.indexOf("taAvg") + 7, str.indexOf("rhAvg") - 2)));
                        jiage=str.substring(str.indexOf("wholesaleprice") + 17, str.indexOf("wholesaleprice") +20);
                        strings.add(jiage);
                    }else if((i==7)&&(j==3)){         //dataInfo.setTaAvg(Double.valueOf(str.substring(
                        //     str.indexOf("taAvg") + 7, str.indexOf("rhAvg") - 2)));
                        jiage=str.substring(str.indexOf("forecastprice") + 16, str.indexOf("forecastprice") +19);
                        str=str.substring(str.indexOf("wholesaleprice") +3 , str.indexOf("]")+1 );
                        strings.add(jiage);
                    }

                }
                results.add(strings);
            }
            return results;
        }
        return results;

    }

    private ArrayList<String> createTopTitles() {
        ArrayList<String> results = new ArrayList<>();
        for (String string : topTitles) {
            results.add(string);
        }
        return results;
    }

    private ArrayList<String> createLeftTitle() {
        ArrayList<String> results = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            results.add(logmin);
        }
        for (int i = 0; i < 4; i++) {
            results.add(logmax);
        }
        return results;
    }


    public class GetPriceTask extends AsyncTask<Void, Void, PriceInfo> {
        private String url;

        public GetPriceTask(String url) {
            // TODO Auto-generated constructor stub
            this.url = url;
            Log.i("aaaa",""+url);
        }

        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
//            progressDialog.show();
        }

        @Override
        public PriceInfo doInBackground(Void... params) {

            str = getDataFormSocket(url);

            if (!str.equals("") && str.indexOf("{") == 0) {
                // str.indexOf("{") == 0;
                // 解决运营商网络下返回str并不是空的bug

            }
            //dataInfo = readString(str);
           // Log.v("MainActivity","return dataInfo");
           // return dataInfo;
            // return null;

            return priceinfo;

        }


        protected void onPostExecute(PriceInfo result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Log.i("aaaab",""+str);
            Log.i("aaaa",""+str.toString());

//            if(str.toString().equals(null)){
//                Log.i("aaaa","判断出来了"+str);
//            }
//
            if((!str.contains("暂无数据"))&&(str.contains("wholesaleprice"))){
                BIAOZHI=1;
            }
            Log.i("aaaab", "" + BIAOZHI);
            scrollTableView.setDatas(createTopTitles(), createLeftTitle(), createContent(leftTitle.size(), 4));

            }
        }



    private String getDataFormSocket(String url) {
        // TODO Auto-generated method stub
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
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
                // Toast.makeText(, "服务器没有应答！", Toast.LENGTH_SHORT).show();

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


}
