package me.itangqi.buildingblocks.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.v5kf.client.lib.V5ClientAgent;
import com.v5kf.client.lib.callback.V5InitCallback;

/**
 * Created by tangqi on 7/20/15.
 */
public class App extends Application {
    private int mUserID;				//本地用户Id
    private int CurrentAreaId;			//当前营业厅的Id
    private int CurrentQueueId;			//当前队列Id
    private String selfUserName;		//本地用户名字
    private String targetUserName;		//对方用户名字
    private int RoomId;					//进入房间号
    private  int TargetUserId;			//对方用户Id
    private  String priceJson;          //价格json数据

    private static App instance;
    private int userType;
    final private String mStrIP = "219.245.196.57";
    final private int mSPort = 8906;
    final private String wsuri = "ws://219.245.196.57:8080/ExpertConsultation/websocket";

    public String getAnyChatIP() {
        return mStrIP;
    }

    public int getAnyChatPort() {
        return mSPort;
    }

    public String getWsuri() {
        return wsuri;
    }
    public String getPriceJson() {
        return priceJson;
    }

    public void setPriceJson(String priceJson) {
        this.priceJson = priceJson;
    }

    public void setUserID(int sUserID)
    {
        mUserID = sUserID;
    }

    public int getUserID()
    {
        return mUserID;
    }


    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getCurrentAreaId() {
        return CurrentAreaId;
    }

    public void setCurrentAreaId(int currentAreaId) {
        CurrentAreaId = currentAreaId;
    }

    public int getCurrentQueueId() {
        return CurrentQueueId;
    }

    public void setCurrentQueueId(int currentQueueId) {
        CurrentQueueId = currentQueueId;
    }

    public String getSelfUserName() {
        return selfUserName;
    }

    public void setSelfUserName(String selfUserName) {
        this.selfUserName = selfUserName;
    }

    public int getRoomId() {
        return RoomId;
    }

    public void setRoomId(int roomId) {
        RoomId = roomId;
    }

    public int getTargetUserId() {
        return TargetUserId;
    }

    public void setTargetUserId(int targetUserId) {
        TargetUserId = targetUserId;
    }

    public String getTargetUserName() {
        return targetUserName;
    }

    public void setTargetUserName(String targetUserName) {
        this.targetUserName = targetUserName;
    }

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        instance = this;
        /*
        userType = Integer.parseInt( UserType.NORMAL );

            //*****************************************************************************************
            registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

                }

                @Override
                public void onActivityStarted(Activity activity) {

                }

                @Override
                public void onActivityResumed(Activity activity) {
                    if(WebSocketProcess.getInstance()!=null)
                        WebSocketProcess.getInstance().setActivity(activity);
                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {

                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {

                }
            });
            */
            //**********************************************************************************************


        V5ClientAgent.init(this, new V5InitCallback() {
            @Override
            public void onSuccess(String response) {
// TODO Auto-generated method stub
                Log.i("MyApplication", "init success: " + response);
                Log.i("aa", "init success: " + response);
            }

            @Override
            public void onFailure(String response) {
// TODO Auto-generated method stub
                Log.e("MyApplication", "init failed: " + response);
            }
        });

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static App getInstance() {

        return instance;
    }
}
