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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.itangqi.buildingblocks.R;
import me.itangqi.buildingblocks.ui.activity.NewsActivity;

/**
 * Created by rain on 2016/4/13.
 */
public class NewListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public static final int ITEM_TYPE_CONTEN0 = 1;
    public  static  final  String URL = "";

    public String [] title;
    public String [] news_text;
    public String [] image_url;
    public JSONObject json;
    public Pair[] pairs;

    private Integer minID;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private SharedPreferences sp;
    public NewListAdapter(Context context) {

        sp = context.getSharedPreferences("userinfo", context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
        minID = sp.getInt("minNewsID", 0);
        pairs = new Pair[1];
        pairs[0] = new Pair<String,String>("get_news_id",minID.toString());
        //得到JSON,回传的是新闻
//        HttpThread httpThread = new HttpThread(URL,mLayoutInflater.getContext(),pairs);
//        httpThread.start();
//        while (httpThread.isAlive()) {}
//        String jsonString = httpThread.GetString();
//        try {
//            json = new JSONObject(jsonString);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        mContext = context;
       // GetTitle();
        title = new String[]{
                context.getString(R.string.news1_title),
                context.getString(R.string.news2_title),
                context.getString(R.string.news3_title),
                context.getString(R.string.news4_title),
                context.getString(R.string.news5_title)};
       // GetNews();
        news_text = new String[]{
                context.getString(R.string.news1),
                context.getString(R.string.news2),
                context.getString(R.string.news3),
                context.getString(R.string.news4),
                context.getString(R.string.news5)
        };
       // GetImage();
        mLayoutInflater = LayoutInflater.from(context);
    }

    private void GetImage() {
        try {
            JSONArray titleList = json.getJSONArray("news_picture_url");
            int length = titleList.length();
            for(int i = 0; i < length;i++)
            {
                JSONObject temp = titleList.getJSONObject(i);
                image_url[i] =temp.getString("news_picture_url");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void GetNews() {
        try {
            JSONArray titleList = json.getJSONArray("news_content");
            int length = titleList.length();
            for(int i = 0; i < length;i++)
            {
                JSONObject temp = titleList.getJSONObject(i);
                title[i] =temp.getString("news_content");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void GetTitle() {
        try {
            JSONArray titleList = json.getJSONArray("news_title");
            int length = titleList.length();
            for(int i = 0; i < length;i++)
            {
                JSONObject temp = titleList.getJSONObject(i);
                if(temp==null)
                {
                    title[i] = "";
                }
                else {
                    title[i] =temp.getString("news_title");
                }
            }
            minID = minID+ length;
            sp.edit().putInt("minNewsID",minID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
    //内容 ViewHolder
    public static class ContentViewHolder extends RecyclerView.ViewHolder {
       // @Bind(R.id.coordinatorLayout) CoordinatorLayout mContainer;
        private TextView title;
        private TextView news;
        private String imageAddress;
        public ContentViewHolder(final View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.tv_item_title);
            news = (TextView) itemView.findViewById(R.id.tv_item_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent userActivity = new Intent(itemView.getContext(), NewsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    userActivity.putExtra("news", news.getText());
                   // userActivity.putExtra("image",imageAddress);
                   itemView.getContext().startActivity(userActivity);
                }
            });
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ContentViewHolder(mLayoutInflater.inflate(R.layout.news_item, parent, false));
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ContentViewHolder) holder).title.setText(title[position]);
            ((ContentViewHolder)holder).news.setText(news_text[position]);
           // ((ContentViewHolder)holder).imageAddress = image_url[position];
    }

    @Override
    public int getItemCount() {
        return  getContentItemCount();
    }


}

