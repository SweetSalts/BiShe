package me.itangqi.buildingblocks.utils;

/**
 * Created by oreo on 2016/10/7.
 */
public class PriceInfo {
    private float forecastprice1;
    private float forecastprice2;

    public float getForecastprice1() {
        return forecastprice1;
    }

    public void setForecastprice1(float forecastprice1) {
        this.forecastprice1 = forecastprice1;
    }

    public float getForecastprice2() {
        return forecastprice2;
    }

    public void setForecastprice2(float forecastprice2) {
        this.forecastprice2 = forecastprice2;
    }

    public float getForecastprice3() {
        return forecastprice3;
    }

    public void setForecastprice3(float forecastprice3) {
        this.forecastprice3 = forecastprice3;
    }

    public float getForecastprice4() {
        return forecastprice4;
    }

    public void setForecastprice4(float forecastprice4) {
        this.forecastprice4 = forecastprice4;
    }

    public float getWholesaleprice1() {
        return wholesaleprice1;
    }

    public void setWholesaleprice1(float wholesaleprice1) {
        this.wholesaleprice1 = wholesaleprice1;
    }

    public float getWholesaleprice2() {
        return wholesaleprice2;
    }

    public void setWholesaleprice2(float wholesaleprice2) {
        this.wholesaleprice2 = wholesaleprice2;
    }

    public float getWholesaleprice3() {
        return wholesaleprice3;
    }

    public void setWholesaleprice3(float wholesaleprice3) {
        this.wholesaleprice3 = wholesaleprice3;
    }

    public float getWholesaleprice4() {
        return wholesaleprice4;
    }

    public void setWholesaleprice4(float wholesaleprice4) {
        this.wholesaleprice4 = wholesaleprice4;
    }

    public PriceInfo(float forecastprice1, float forecastprice2, float forecastprice3, float forecastprice4, float wholesaleprice1, float wholesaleprice2, float wholesaleprice3, float wholesaleprice4) {
        this.forecastprice1 = forecastprice1;
        this.forecastprice2 = forecastprice2;
        this.forecastprice3 = forecastprice3;
        this.forecastprice4 = forecastprice4;
        this.wholesaleprice1 = wholesaleprice1;
        this.wholesaleprice2 = wholesaleprice2;
        this.wholesaleprice3 = wholesaleprice3;
        this.wholesaleprice4 = wholesaleprice4;
    }

    private float forecastprice3;
    private float forecastprice4;

    @Override
    public String toString() {
        return "PriceInfo{" +
                "forecastprice1=" + forecastprice1 +
                ", forecastprice2=" + forecastprice2 +
                ", forecastprice3=" + forecastprice3 +
                ", forecastprice4=" + forecastprice4 +
                ", wholesaleprice1=" + wholesaleprice1 +
                ", wholesaleprice2=" + wholesaleprice2 +
                ", wholesaleprice3=" + wholesaleprice3 +
                ", wholesaleprice4=" + wholesaleprice4 +
                '}';
    }

    private float wholesaleprice1;
    private float wholesaleprice2;
    private float wholesaleprice3;
    private float wholesaleprice4;
}
