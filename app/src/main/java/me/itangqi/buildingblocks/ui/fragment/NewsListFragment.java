package me.itangqi.buildingblocks.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.itangqi.buildingblocks.R;
import me.itangqi.buildingblocks.adapter.NewListAdapter;
import me.itangqi.buildingblocks.widget.SimpleDividerItemDecoration;

/**
 * 新闻表界面
 */
public class NewsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipe_refresh_layout;
    private RecyclerView.LayoutManager mLayoutManager;
    @Bind(R.id.cardList)
    RecyclerView mRecyclerView;


    //链表
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        swipe_refresh_layout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mRecyclerView.setAdapter(new NewListAdapter(getActivity()));//设置Adapter
        return view;
    }



    //刷新
    private MyHandler handler = new MyHandler();
    @Override
    public void onRefresh() {
        new Thread(new Runnable() {//下拉触发的函数.
            @Override
            public void run() {
                try {
                    //刷新过程
                    Thread.sleep(1000);
                    Refresh();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    //接收刷新msg
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    swipe_refresh_layout.setRefreshing(false);
                    break;
                default:
                    swipe_refresh_layout.setRefreshing(false);
                    break;
            }
        }
    }
    private boolean Refresh() {
        return  true;
    }
}
