package me.itangqi.buildingblocks.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import me.itangqi.buildingblocks.R;
import me.itangqi.buildingblocks.ui.activity.NoticeActivity;

/**
 * Created by oreo on 2016/9/2.
 */
public class nongjifuwuListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static final int ITEM_TYPE_CONTEN0 = 1;
    public  static  final  String URL = "";

    public String [] title;
    public String [] news_text;
    public String [] image_url;
    public JSONObject json;
    public Pair[] pairs;

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private SharedPreferences sp;
    private Integer minID;
    private OnItemClickLitener mOnItemClickLitener;
    public nongjifuwuListAdapter(Context context) {
        mContext = context;
        sp = context.getSharedPreferences("userinfo", context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
        minID = sp.getInt("minNoticeID", 0);
        pairs = new Pair[1];
        pairs[0] = new Pair<String,String>("get_not_id",minID.toString());
        //得到JSON,回传的是公告
//        HttpThread httpThread = new HttpThread(URL,mLayoutInflater.getContext(),pairs);
//        httpThread.start();
//        while (httpThread.isAlive()) {}
//        String jsonString = httpThread.GetString();
//        try {
//            json = new JSONObject(jsonString);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        //      GetTitle();
        title = new String[]{
                context.getString(R.string.nongjifuwunews1_title),
                context.getString(R.string.nongjifuwunews2_title),
                context.getString(R.string.nongjifuwunews3_title),
                context.getString(R.string.nongjifuwunews4_title),
                context.getString(R.string.nongjifuwunews5_title),
                context.getString(R.string.nongjifuwunews6_title),
                context.getString(R.string.nongjifuwunews7_title),
                context.getString(R.string.nongjifuwunews8_title),
                context.getString(R.string.nongjifuwunews9_title),
                context.getString(R.string.nongjifuwunews10_title),
                context.getString(R.string.BCFZ_titles1),
                context.getString(R.string.JKXF_titles1),
                context.getString(R.string.JKXF_titles2)
    };

        //  GetNotice();
        news_text = new String[]{
                context.getString(R.string.nongjifuwunews1),
                context.getString(R.string.nongjifuwunews2),
                context.getString(R.string.nongjifuwunews3),
                context.getString(R.string.nongjifuwunews4),
                context.getString(R.string.nongjifuwunews5),
                context.getString(R.string.nongjifuwunews6),
                context.getString(R.string.nongjifuwunews7),
                context.getString(R.string.nongjifuwunews8),
                context.getString(R.string.nongjifuwunews9),
                context.getString(R.string.nongjifuwunews10),
                context.getString(R.string.BCFZnews1),
                context.getString(R.string.JKXFnews1),
                context.getString(R.string.JKXFnews2)
        };
        mLayoutInflater = LayoutInflater.from(context);
    }


/*    private void GetNotice() {
        try {
            JSONArray titleList = json.getJSONArray("college_notice_content");
            int length = titleList.length();
            for(int i = 0; i < length;i++)
            {
                JSONObject temp = titleList.getJSONObject(i);
                title[i] =temp.getString("college_notice_content");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

 /*   private void GetTitle() {
        try {
            JSONArray titleList = json.getJSONArray("college_notice_title");
            int length = titleList.length();
            for(int i = 0; i < length;i++)
            {
                JSONObject temp = titleList.getJSONObject(i);
                if(temp==null)
                {
                    title[i] = "";
                }
                else {
                    title[i] =temp.getString("college_notice_title");
                }
            }
            minID = minID+ length;
            sp.edit().putInt("minNoticeID",minID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

    //内容长度
    public int getContentItemCount(){
        return title.length;//得到item长度
    }
    //判断当前item是否是HeadView

    //判断当前item类型
    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE_CONTEN0;
    }

    @Override
    public int getItemCount() {
        return getContentItemCount();
    }

    //内容 ViewHolder
    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        // @Bind(R.id.coordinatorLayout) CoordinatorLayout mContainer;
        private TextView title;
        private TextView news;
        public ContentViewHolder(final View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.tv_item_title);
            news = (TextView) itemView.findViewById(R.id.tv_item_text);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent userActivity = new Intent(itemView.getContext(), NoticeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    userActivity.putExtra("notice", news.getText());
                    itemView.getContext().startActivity(userActivity);

                }
            });
        }
    }





    //这个方法主要生成为每个Item inflater出一个View，但是该方法返回的是一个ViewHolder。
    // 该方法把View直接封装在ViewHolder中，然后我们面向的是ViewHolder这个实例，当然这
    // 个ViewHolder需要我们自己去编写。直接省去了当初的convertView.setTag(holder)和conv
    // ertView.getTag()这些繁琐的步骤。
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentViewHolder(mLayoutInflater.inflate(R.layout.news_item, parent, false));
    }


    //点击事件
    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }



    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    //这个方法主要用于适配渲染数据到View中。方法提供给你了一个viewHolder，而不是原来的convertView。
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ((ContentViewHolder) holder).title.setText(title[position]);
        ((ContentViewHolder)holder).news.setText(news_text[position]);
        //  holder.tv.setText(mDatas.get(position));
       /* if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });*/


    }




    //这个方法就类似于BaseAdapter的getCount方法了，即总共有多少个条目。
    // 实例：接着来几个小的实例帮助大家更深入的了解RecyclerView的用法，首先来实现一个最简单的列表，效果如下

}
