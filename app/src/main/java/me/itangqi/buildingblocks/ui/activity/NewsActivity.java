package me.itangqi.buildingblocks.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;
import me.itangqi.buildingblocks.R;


public class NewsActivity extends AppCompatActivity implements SwipeBackActivityBase {

    @Bind(R.id.backdrop)//背景
    ImageView backdrop;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.newsactivity_news)//产业资讯
    TextView newsactivityNews;
    private SwipeBackActivityHelper mSwipeBackLayout;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        intent = getIntent();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSwipeBackLayout = new SwipeBackActivityHelper(this);
        mSwipeBackLayout.onActivityCreate();

       // setNews();
       // setImage();


    }


    //设置图片
    private void setImage() {
        String imageAddress = intent.getStringExtra("image");
        backdrop.setImageURI(Uri.parse(imageAddress));
    }
    //设置新闻
    private void setNews() {
        String news = intent.getStringExtra("news");
        newsactivityNews.setText(news);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mSwipeBackLayout.onPostCreate();
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackLayout.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

}
