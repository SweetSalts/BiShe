package me.itangqi.buildingblocks.utils.base;

/**
 * Created by oreo on 2016/7/20.
 */
public class DataInfo {
    private String tmstamp;// 更新时间1
    private double taAvg;// 空气温度1
    private double taSoilAvg;// 土壤温度1
    private double rhAvg;// 空气湿度1
    private double vwcAvg;// 土壤湿度1
    private double rainTot;// 累积降雨量1
    private double pvaporAvg;// 水汽压1
    private double wd;// 风向1
    private double wsAvg;// 风速1
    private double slrwAvg;// 总辐射1
    private double ptemp;// 数采面板温度1
    private double battVoltMin;// 电池电压最小值1

    public DataInfo() {
        // TODO Auto-generated constructor stub
    }

    public DataInfo(String tmstamp, double taAvg, double taSoilAvg,
                    double rhAvg, double vwcAvg, double rainTot, double pvaporAvg,
                    double wd, double wsAvg, double slrwAvg, double ptemp,
                    double battVoltMin) {
        // TODO Auto-generated constructor stub
        this.tmstamp = tmstamp;
        this.taAvg = taAvg;
        this.taSoilAvg = taSoilAvg;
        this.rhAvg = rhAvg;
        this.vwcAvg = vwcAvg;
        this.rainTot = rainTot;
        this.pvaporAvg = pvaporAvg;
        this.wd = wd;
        this.wsAvg = wsAvg;
        this.slrwAvg = slrwAvg;
        this.ptemp = ptemp;
        this.battVoltMin = battVoltMin;
    }

    public double getTaAvg() {
        return taAvg;
    }

    public void setTaAvg(double taAvg) {
        this.taAvg = taAvg;
    }

    public double getTaSoilAvg() {
        return taSoilAvg;
    }

    public void setTaSoilAvg(double taSoilAvg) {
        this.taSoilAvg = taSoilAvg;
    }

    public double getRhAvg() {
        return rhAvg;
    }

    public void setRhAvg(double rhAvg) {
        this.rhAvg = rhAvg;
    }

    public double getVwcAvg() {
        return vwcAvg;
    }

    public void setVwcAvg(double vwcAvg) {
        this.vwcAvg = vwcAvg;
    }

    public double getRainTot() {
        return rainTot;
    }

    public void setRainTot(double rainTot) {
        this.rainTot = rainTot;
    }

    public double getPvaporAvg() {
        return pvaporAvg;
    }

    public void setPvaporAvg(double pvaporAvg) {
        this.pvaporAvg = pvaporAvg;
    }

    public double getWd() {
        return wd;
    }

    public void setWd(double wd) {
        this.wd = wd;
    }

    public double getWsAvg() {
        return wsAvg;
    }

    public void setWsAvg(double wsAvg) {
        this.wsAvg = wsAvg;
    }

    public double getSlrwAvg() {
        return slrwAvg;
    }

    public void setSlrwAvg(double slrwAvg) {
        this.slrwAvg = slrwAvg;
    }

    public double getPtemp() {
        return ptemp;
    }

    public void setPtemp(double ptemp) {
        this.ptemp = ptemp;
    }

    public double getBattVoltMin() {
        return battVoltMin;
    }

    public void setBattVoltMin(double battVoltMin) {
        this.battVoltMin = battVoltMin;
    }

    public String getUpdateTime() {
        return tmstamp;
    }

    public void setUpdateTime(String updateTime) {
        this.tmstamp = updateTime;
    }

    @Override
    public String toString() {
        return "DataInfo [updateTime=" + tmstamp + ", taAvg=" + taAvg
                + ", taSoilAvg=" + taSoilAvg + ", rhAvg=" + rhAvg + ", vwcAvg="
                + vwcAvg + ", rainTot=" + rainTot + ", pvaporAvg=" + pvaporAvg
                + ", wd=" + wd + ", wsAvg=" + wsAvg + ", slrwAvg=" + slrwAvg
                + ", ptemp=" + ptemp + ", battVoltMin=" + battVoltMin + "]";
    }
}
