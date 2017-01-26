package webcrawler;

public abstract class CalculateBatteryPercentageAndTime {

  protected double Vin;
  protected double Vout;
  protected double Vbat;
  protected double batteryPercent;
  protected String time;
  protected String mode;

  public double getBatteryPercent() {
    return batteryPercent;
  }

  public String getTime() {
    return time;
  }

  public String getMode() {
    return mode;
  }
}

