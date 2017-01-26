package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import models.ElectricalData;
import play.Logger;
import play.db.DB;

public class ElectricalDataDAO {

  private final String insertQuery = "insert into electricaldata values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
  private final String selectLatestNodes =
      "select * from electricaldata where creation_date = (Select max(creation_date) from electricaldata)";

  public void insert(int node_id, String voltage_in, String voltage_battery, String voltage_out, String current_in,
      String current_out, Timestamp timestamp, double batteryPercentage, String time, String mode) {
    Logger.debug(node_id + "-" + voltage_in + "-" + voltage_battery + "-" + voltage_out + "-" + current_in + "-"
        + current_out + "-" + batteryPercentage + "-" + mode + "-" + time);
    Connection connection = null;
    PreparedStatement statement = null;
    try {
      connection = DB.getConnection();
      statement = connection.prepareStatement(insertQuery);
      statement.setString(1, String.valueOf(node_id));
      statement.setString(2, voltage_in);
      statement.setString(3, voltage_out);
      statement.setString(4, voltage_battery);
      statement.setString(5, current_in);
      statement.setString(6, current_out);
      statement.setTimestamp(7, timestamp);
      statement.setString(8, String.valueOf(new Double(batteryPercentage).intValue()));
      statement.setString(9, time);
      statement.setString(10, mode);
      statement.executeUpdate();
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
    } finally {
      try {
        if (statement != null) {
          statement.close();
        }
        if (connection != null) {
          connection.close();
        }
      } catch (Exception e) {
        Logger.error(e.getMessage(), e);
      }

    }
  }

  public List<ElectricalData> getLatestElectricalData() {
    List<ElectricalData> list = new ArrayList<>();
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;
    try {
      connection = DB.getConnection();
      statement = connection.prepareStatement(selectLatestNodes);
      rs = statement.executeQuery();
      while (rs.next()) {
        ElectricalData data = new ElectricalData();
        data.setNode_id(rs.getString(1));
        data.setVoltage_in(getDoubleData(rs.getString(2)));
        data.setVoltage_out(getDoubleData(rs.getString(3)));
        data.setVoltage_battery(getDoubleData(rs.getString(4)));
        data.setCurrent_in(getDoubleData(rs.getString(5)));
        data.setCurrent_out(getDoubleData(rs.getString(6)));
        data.setCreation_date(rs.getTimestamp(7));
        data.setBattery_percentage(rs.getString(8));
        data.setTime(rs.getString(9));
        data.setMode(rs.getString(10));
        list.add(data);
      }
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
        if (statement != null) {
          statement.close();
        }
        if (connection != null) {
          connection.close();
        }
      } catch (Exception e) {
        Logger.error(e.getMessage(), e);
      }

    }
    return list;
  }

  private double getDoubleData(String str) {
    try {
      return Double.parseDouble(str);
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
      return 0.0;
    }
  }


}

