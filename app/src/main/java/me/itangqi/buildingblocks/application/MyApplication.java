package me.itangqi.buildingblocks.application;

import android.app.Application;
import android.util.Log;

import com.v5kf.client.lib.V5ClientAgent;
import com.v5kf.client.lib.callback.V5InitCallback;

/**
 * Created by oreo on 2016/8/28.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        V5ClientAgent.init(this, new V5InitCallback() {
            @Override
            public void onSuccess(String response) {
// TODO Auto-generated method stub
                Log.i("MyApplication", "init success: " + response);
            }

            @Override
            public void onFailure(String response) {
// TODO Auto-generated method stub
                Log.e("MyApplication", "init failed: " + response);
            }
        });



    }
}
