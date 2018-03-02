package me.itangqi.buildingblocks.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import me.itangqi.buildingblocks.R;

/**
 * Created by oreo on 2016/7/19.
 */
public class MarketFragment extends Fragment {

    private static String[] m={"苹果","猕猴桃","蔬菜","核桃","水产","葡萄","茶叶","粮油"};
    private View v;
    private TextView text;

    private Spinner spinner;

    private ArrayAdapter<String> adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override   //container参数代表该Fragment在Activity中的父控件
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_markettest, container, false);
        spinner= (Spinner) v.findViewById(R.id.fragment_spinner_select);

        adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_expandable_list_item_1,m);
        spinner.setAdapter(adapter);

        return v;
        //    inflate()方法的三个参数：
        //　　第一个是resource ID，指明了当前的Fragment对应的资源文件；
        //　　第二个参数是父容器控件；
       // 　　第三个布尔值参数表明是否连接该布局和其父容器控件，在这里的情况设置为false，因为系统已经插入了这个布局到父控件，设置为true将会产生多余的一个View Group。
    }
}
