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

public class NoticeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


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
    public NoticeListAdapter(Context context) {
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
                context.getString(R.string.news1_title),
                context.getString(R.string.news2_title),
                context.getString(R.string.news3_title),
                context.getString(R.string.news4_title),
                context.getString(R.string.news5_title),
                context.getString(R.string.news6_title),
                context.getString(R.string.news7_title),
                context.getString(R.string.laws1_title)};
      //  GetNotice();
        news_text = new String[]{
                context.getString(R.string.news1),
                context.getString(R.string.news2),
                context.getString(R.string.news3),
                context.getString(R.string.news4),
                context.getString(R.string.news5),
                context.getString(R.string.news6),
                context.getString(R.string.news7),
                context.getString(R.string.laws1)
        };
        mLayoutInflater = LayoutInflater.from(context);
    }

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
        }
}

