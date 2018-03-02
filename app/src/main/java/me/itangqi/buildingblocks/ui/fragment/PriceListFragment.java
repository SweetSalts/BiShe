package me.itangqi.buildingblocks.ui.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import me.itangqi.buildingblocks.R;
import me.itangqi.buildingblocks.utils.base.WheelView;
import me.itangqi.buildingblocks.network.HTTPThread;
import me.itangqi.buildingblocks.ui.activity.MainActivity;
import me.itangqi.buildingblocks.ui.activity.PriceTableActicity;

/**
 * Created by oreo on 2016/9/20.
 */
public class PriceListFragment extends Fragment {
    private View v;
    private Button data_begin;
    private TextView et1;
    private Button data_end;
    private TextView et2;
    private int year_begin;
    private int month_begin;
    private int day_begin;
    private int year_end;
    private int month_end;
    private int day_end;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String[] PLANETS1 = new String[]{"陕西白水","陕西洛川","陕西千阳"};
    private static final String[] PLANETS2 = new String[]{"秦冠","红富士","瑞雪"};
    private WheelView wva1;
    private WheelView wva2;
    private int area=1;
    private int kind=1;
    private int onedayafter;//每次查询1天
    private String url;
    private HTTPThread httpThread;
    private Message msg;
    private String json;
    private String str;
    private String logmin;
    private String logmax;



    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            json=msg.obj.toString();
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       v=inflater.inflate(R.layout.fragment_price_list_test,container,false);
        data_begin= (Button) v.findViewById(R.id.fragment_price_list_test_chosebt1);
       data_end= (Button) v.findViewById(R.id.fragment_price_list_test_chosebt2);
        //et1= (TextView) v.findViewById(R.id.fragment_price_list_test_choseet1);

        wva1= (WheelView) v.findViewById(R.id.main_wv1);
        wva2= (WheelView) v.findViewById(R.id.main_wv2);
        initURL();

        Calendar c = Calendar.getInstance();
        Date myDate=new Date();
        c.setTime(myDate);
        year_begin=c.get(Calendar.YEAR); //获取Calendar对象中的年
        month_begin=c.get(Calendar.MONTH);//获取Calendar对象中的月
        day_begin=c.get(Calendar.DAY_OF_MONTH)-5;//获取这个月的第几天
        onedayafter=day_begin+1;
        year_end=c.get(Calendar.YEAR); //获取Calendar对象中的年
        month_end=c.get(Calendar.MONTH);//获取Calendar对象中的月
        day_end=c.get(Calendar.DAY_OF_MONTH);//获取这个月的第几天

        url="http://219.245.196.56:8080/Apple/dh/service1/AreaApple?area=1&kind=1&logmin="+year_begin+"-"+(month_begin+1)+"-"+day_begin+"&logmax="+year_begin+"-"+(month_begin+1)+"-"+onedayafter;
        logmin=""+year_begin+"-"+(month_begin+1)+"-"+day_begin;
        logmax=""+year_begin+"-"+(month_begin+1)+"-"+onedayafter;

       // et1.setText("当前日期:" + year_begin + "-" + (month_begin + 1) + "-" + day_begin); //显示当前的年月日
       // et2.setText("当前日期:" + year_end + "-" + (month_end + 1) + "-" + day_end);
        data_begin.setText("点击选择起始日期:" + year_begin + "-" + (month_begin + 1) + "-" + day_begin);
        data_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        year_begin = year;
                        month_begin = monthOfYear;
                        day_begin = dayOfMonth;
                        year_end=year_begin;
                        month_end=month_begin;
                        day_end=day_begin+1;

                        //更新日期
                        updateDateURL();
                        initURL();
                    }
                }, year_begin, month_begin, day_begin);
                dpd.show();


            }
        });


        data_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(PriceListFragment.this.getActivity(), PriceTableActicity.class);
                Bundle bundle=new Bundle();
                bundle.putString("url",url);
                bundle.putString("logmin",logmin);
                bundle.putString("logmax",logmax);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        wva1.setOffset(1);
        wva1.setItems(Arrays.asList(PLANETS1));
        wva1.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                area = selectedIndex;

                Log.d(TAG, "selectedIndex: " + selectedIndex + ", item: " + item);
                initURL();
            }
        });

        wva2.setOffset(1);
        wva2.setItems(Arrays.asList(PLANETS2));
        wva2.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {

                if(selectedIndex==1)
                    kind = 1;
                else if(selectedIndex==2)
                    kind=4;
                else if(selectedIndex==3)
                    kind=12;
                Log.d(TAG, "selectedIndex: " + selectedIndex + ", item: " + item);
                Log.d("priceList", "selectedIndex: " + selectedIndex + ", item: " + item);
                initURL();
            }
        });

        return v;
    }



    private void updateDateURL() {
      //  et1.setText("当前日期："+year_begin+"-"+(month_begin+1)+"-"+day_begin);
//        et2.setText("当前日期："+year_end+"-"+(month_end+1)+"-"+day_end);
       // url=url+"?area="+area+"&kind="+kind+"&logmin="+year_begin+"-"+(month_begin+1)+"-"+day_begin+"&logmax="+year_begin+"-"+(month_begin+1)+"-"+day_begin;
       // et2.setText(url.toString());
        data_begin.setText("起始日期:" + year_begin + "-" + (month_begin + 1) + "-" + day_begin);

       Log.i("textView", "" + url);
        logmin=""+year_begin+"-"+(month_begin+1)+"-"+day_begin;
        logmax=""+year_begin+"-"+(month_begin+1)+"-"+day_end;
    }
   // private String url="http://219.245.196.56:8080/Apple/dh/service1/AreaApple";//?area=1&kind=4&logmin=2016-07-01&logmax=2016-07-21
   private void initURL() {
       url="http://219.245.196.56:8080/Apple/dh/service1/AreaApple";
       url=url+"?area="+area+"&kind="+kind+"&logmin="+year_begin+"-"+(month_begin+1)+"-"+day_begin+"&logmax="+year_begin+"-"+(month_begin+1)+"-"+day_end;
        logmin=""+year_begin+"-"+(month_begin+1)+"-"+day_begin;
         logmax=""+year_begin+"-"+(month_begin+1)+"-"+day_end;

   }

}
