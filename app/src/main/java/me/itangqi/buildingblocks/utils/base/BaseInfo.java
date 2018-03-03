package me.itangqi.buildingblocks.utils.base;

import java.io.Serializable;

/**
 * Created by oreo on 2016/7/20.
 */
public class BaseInfo implements Serializable {
    private String baseName;// 名字
    private String basePosition;// 位置
    private String baseCode;// 编码
    private double latitude;//经度
    private double longitude;//纬度
    private int imgId;//实验站图片
    private boolean isShanXi;// 省内标志

    public BaseInfo(String baseName, String basePosition, String baseCode,
                    boolean isShanXi) {
        // TODO Auto-generated constructor stub
        this.baseName = baseName;
        this.basePosition = basePosition;
        this.baseCode = baseCode;
        this.isShanXi = isShanXi;
    }

    public BaseInfo(String baseName, String basePosition, String baseCode, double latitude, double longitude, int imgId, boolean isShanXi) {
        this.baseName = baseName;
        this.basePosition = basePosition;
        this.baseCode = baseCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imgId = imgId;
        this.isShanXi = isShanXi;
    }

    public BaseInfo() {
        // TODO Auto-generated constructor stub
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public String getBasePosition() {
        return basePosition;
    }

    public void setBasePosition(String basePosition) {
        this.basePosition = basePosition;
    }

    public boolean isShanXi() {
        return isShanXi;
    }

    public void setShanXi(boolean isShanXi) {
        this.isShanXi = isShanXi;
    }

    public String getBaseCode() {
        return baseCode;
    }

    public void setBaseCode(String baseCode) {
        this.baseCode = baseCode;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        String string = this.baseName + "--" + this.basePosition + "--"
                + this.baseCode + "--" + this.isShanXi;
        return string;
    }
}
