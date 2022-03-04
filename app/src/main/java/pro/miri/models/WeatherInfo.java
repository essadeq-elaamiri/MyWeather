package pro.miri.models;

public class WeatherInfo {
    private Long date;
    private String timeZone;
    private double temperature;
    private String icon;

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public WeatherInfo(Long date, String timeZone, double temperature, String icon) {
        this.date = date;
        this.timeZone = timeZone;
        this.temperature = temperature;
        this.icon = icon;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }


    public double getTemperature() {
        return temperature;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
