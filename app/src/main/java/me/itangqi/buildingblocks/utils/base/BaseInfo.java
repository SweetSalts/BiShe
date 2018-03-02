package me.itangqi.buildingblocks.utils.base;

import java.io.Serializable;

/**
 * Created by oreo on 2016/7/20.
 */
public class BaseInfo implements Serializable {
    private String baseName;// 名字
    private String basePosition;// 位置
    private String baseCode;// 编码
    private boolean isShanXi;// 省内标志

    public BaseInfo(String baseName, String basePosition, String baseCode,
                    boolean isShanXi) {
        // TODO Auto-generated constructor stub
        this.baseName = baseName;
        this.basePosition = basePosition;
        this.baseCode = baseCode;
        this.isShanXi = isShanXi;
    }

    public BaseInfo() {
        // TODO Auto-generated constructor stub
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
