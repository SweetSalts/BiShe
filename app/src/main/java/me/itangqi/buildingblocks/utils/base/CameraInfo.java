package me.itangqi.buildingblocks.utils.base;

/**
 * Created by oreo on 2016/7/20.
 */
public class CameraInfo {

    private String cameraPosition;
    private String baseCode;
    private String Ip;
    private int Port;

    public CameraInfo() {
        // TODO Auto-generated constructor stub
    }

    public String getCameraPosition() {
        return cameraPosition;
    }

    public void setCameraPosition(String cameraPosition) {
        this.cameraPosition = cameraPosition;
    }

    public String getBaseCode() {
        return baseCode;
    }

    public void setBaseCode(String baseCode) {
        this.baseCode = baseCode;
    }

    public String getIp() {
        return Ip;
    }

    public void setIp(String ip) {
        Ip = ip;
    }

    public int getPort() {
        return Port;
    }

    public void setPort(int port) {
        Port = port;
    }

    @Override
    public String toString() {
        return "CameraInfo [cameraPosition=" + cameraPosition + ", baseCode="
                + baseCode + ", Ip=" + Ip + ", Port=" + Port + "]";
    }

}
