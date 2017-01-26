package models;

import java.util.Date;

public class ElectricalData {
  private String node_id;
  private double voltage_in;
  private double voltage_out;
  private double voltage_battery;
  private double current_in;
  private double current_out;
  private Date creation_date;
  private String battery_percentage;
  private String time;
  private String mode;

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getMode() {
    return mode;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public String getNode_id() {
    return node_id;
  }

  public void setNode_id(String node_id) {
    this.node_id = node_id;
  }

  public double getVoltage_in() {
    return voltage_in;
  }

  public void setVoltage_in(double voltage_in) {
    this.voltage_in = voltage_in;
  }

  public double getVoltage_out() {
    return voltage_out;
  }

  public void setVoltage_out(double voltage_out) {
    this.voltage_out = voltage_out;
  }

  public double getVoltage_battery() {
    return voltage_battery;
  }

  public void setVoltage_battery(double voltage_battery) {
    this.voltage_battery = voltage_battery;
  }

  public double getCurrent_in() {
    return current_in;
  }

  public void setCurrent_in(double current_in) {
    this.current_in = current_in;
  }

  public double getCurrent_out() {
    return current_out;
  }

  public void setCurrent_out(double current_out) {
    this.current_out = current_out;
  }

  public Date getCreation_date() {
    return creation_date;
  }

  public void setCreation_date(Date creation_date) {
    this.creation_date = creation_date;
  }

  public String getBattery_percentage() {
    return battery_percentage;
  }

  public void setBattery_percentage(String battery_percentage) {
    this.battery_percentage = battery_percentage;
  }


}

