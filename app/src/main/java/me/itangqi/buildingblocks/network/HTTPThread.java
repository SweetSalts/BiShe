package me.itangqi.buildingblocks.network;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by 97279 on 2016/3/26.
 */
public class HTTPThread extends Thread {

    private String url;
    private TextView textView;
    private Handler handler;
    StringBuffer stringBuffer;
    private Message msg=new Message();

    //List<CollegeNotice>notices;

    public  HTTPThread(String url, TextView textView, Handler handler){
        this.url=url;
        this.textView=textView;
        this.handler=handler;

    }

//    public String returnString(){
//        String str;
//        str=stringBuffer.toString();
//        return str;
//    }

    public JSONObject createJSON(){
        JSONObject jsonObject=new JSONObject();

//        try {
//
//            //校园公告:
//           // jsonObject.put("get_col_not_id",30);
//
//
//
////            jsonObject.put("reg_mail", "2");
////            jsonObject.put("reg_name", "张汉1");
////            jsonObject.put("reg_passwd", "1231456");
////            jsonObject.put("reg_aca_ID", "10");
////            jsonObject.put("reg_pro_ID", "1");
////            jsonObject.put("reg_add_city", "衡水市");
////            jsonObject.put("reg_sex", "男");
////            jsonObject.put("reg_phone","1393112221");
//
//
//           // jsonObject.put("get_col_not_ID", "30");
//
//
//           /* jsonUtil.putData("login_mail","972794467@qq.com");
//            jsonUtil.putData("login_passwd","123456");
//           */
//        //  jsonUtil.putData("comment_type","1");
//            //jsonUtil.putData("notice_id","1");
//
//           // jsonUtil.putData("news_content","内容aaaaaaa");
//            //jsonUtil.putData("news_picture_url","0");
//           // jsonUtil.putData("hom_pro_id","1");
//            //jsonUtil.putData("aca_not_pic","1");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        return jsonObject;


    }
    @Override
    public void run() {
        try{
            URL httpUrl=new URL(url);
            HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            Log.i("textView","测试2");

           // JSONObject jsonData =createJSON();

            //String result=String.valueOf(jsonData);
            //System.out.println(result);
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            //connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/json");


//
//           PrintWriter pw = new PrintWriter(connection.getOutputStream());
//            pw.print(result);
//            pw.flush();
//            pw.close();
//

//            OutputStream outputStream=connection.getOutputStream();
//            outputStream.write(result.getBytes());
//            outputStream.close();


            if(connection.getResponseCode()==200){
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader
                        (connection.getInputStream())) ;
                String line=null;
                stringBuffer=new StringBuffer();
                while((line=bufferedReader.readLine())!=null){
                    stringBuffer.append(line);

                }

//                myJson json1=new myJson(stringBuffer.toString());
//
//                msg.obj=json1;
//                handler.sendMessage(msg);



                //System.out.println(stringBuffer);
              //final Gson gson=new Gson();
                //notices=GsonUtil.parseJsonArrayWithGson(stringBuffer.toString(),college_notice.class);


                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //webView.loadData(stringBuffer.toString(),"text/html;charset=utf-8",null);

                        textView.setText("");
                        System.out.println(stringBuffer.toString());
                        textView.append(stringBuffer.toString());
                        Log.i("textView", "textView传输成功"+stringBuffer.toString());
                       // List<CollegeNotice> options = gson.fromJson(stringBuffer.toString(),new TypeToken<List<CollegeNotice>>(){}.getType());

//                        for (Iterator it = options.iterator(); it.hasNext();) {
//                            CollegeNotice co = (CollegeNotice) it.next();
//                            System.out.println(co.getCollege_notice_title());
//                        }


                    }
                });


            }
            else{
                Log.i("textView","textView传输失败"+connection.getResponseCode());
                //textView.setText("传输失败");
               //System.out.println("传输失败");
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*@Override
    public void run() {
        try {
            URL httpUrl =new URL(url);
            HttpURLConnection connection= (HttpURLConnection) httpUrl.openConnection();
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            final StringBuffer stringBuffer =new StringBuffer();

            BufferedReader reader=new BufferedReader(new BufferedReader(new InputStreamReader(connection.getInputStream())));
            String str;
            while ((str =reader.readLine())!=null){
                stringBuffer.append(str+"\n");
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //webView.loadData(stringBuffer.toString(),"text/html;charset=utf-8",null);
                    textView.append(stringBuffer.toString());
                }
            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    class myJson{
        public String json;
        myJson(String json){
            this.json=json;
        }

        @Override
        public String toString() {
            return json.toString();

        }
    }

}
