package me.itangqi.buildingblocks.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * 新闻Adapter
 */
public class LabelListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> mNewsList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private static final int ITEM_TYPE_IMAGE = 1;
    private static final int ITEM_TYPE_TEXT = 2;

    //初始化Adapter
    public LabelListAdapter(Context mContext, List<String> mNewsList) {
        this.mContext = mContext;
        this.mNewsList = mNewsList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    //得到总数目
    @Override
    public int getItemCount() {
        return mNewsList.size();
    }
}
