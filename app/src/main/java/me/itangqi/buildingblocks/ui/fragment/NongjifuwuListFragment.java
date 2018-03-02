package me.itangqi.buildingblocks.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.itangqi.buildingblocks.R;
import me.itangqi.buildingblocks.adapter.LabelListAdapter;
import me.itangqi.buildingblocks.adapter.nongjifuwuListAdapter;
import me.itangqi.buildingblocks.widget.SimpleDividerItemDecoration;

/**
 * Created by oreo on 2016/9/2.
 */
public class NongjifuwuListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    @Bind(R.id.id_swipe_ly)
    DrawerLayout idSwipeLy;
    @Bind(R.id.mydrawer_layout)
    SwipeRefreshLayout swipe_refresh_layout;
    private LabelListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Bind(R.id.cardList)
    RecyclerView mRecyclerView;
    private nongjifuwuListAdapter mynongjifuwunoticeAdapter;


    //链表
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice, container, false);
        ButterKnife.bind(this, view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());//设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);//设置布局，这也是默认的
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));//添加分割线
        mynongjifuwunoticeAdapter=new nongjifuwuListAdapter(getActivity());
        mRecyclerView.setAdapter(mynongjifuwunoticeAdapter);//设置Adapter
        return view;
    }


    /* mynoticeAdapter.setOnItemClickLitener(new OnItemClickLitener()
     {

         @Override
         public void onItemClick(View view, int position)
         {
             Toast.makeText(HomeActivity.this, position + " click",
                     Toast.LENGTH_SHORT).show();
         }

         @Override
         public void onItemLongClick(View view, int position)
         {
             Toast.makeText(HomeActivity.this, position + " long click",
                     Toast.LENGTH_SHORT).show();
             mAdapter.removeData(position);
         }
     });
 */
    //刷新
    private MyHandler handler = new MyHandler();

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
                    break;
            }
        }
    }
    private boolean Refresh() {
        return  false;
    }
}
