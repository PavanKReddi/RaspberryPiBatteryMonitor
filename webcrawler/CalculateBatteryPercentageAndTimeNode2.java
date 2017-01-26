package webcrawler;

import java.util.List;

import models.ElectricalData;
import dao.ElectricalDataDAO;
import play.Logger;

public class CalculateBatteryPercentageAndTimeNode2 extends CalculateBatteryPercentageAndTime {
  public CalculateBatteryPercentageAndTimeNode2(String voltage_in, String voltage_out, String voltage_battery) {
    Vin = Double.parseDouble(voltage_in);
    Vout = Double.parseDouble(voltage_out);
    Vbat = Double.parseDouble(voltage_battery);
    calculate();
  }

  private void calculate() {
    double y = -1;
    double z;
    // node2-----------------------------------------------------------
    if (Vin > 4.0 && Vout < 1.0) {
      mode = "C";
      Logger.debug("Node 0 Charge Mode");
      // Charge
      if (Vbat <= 3.6) {
        y = 0;
        time = "5:00";
      } else if (Vbat >= 4.34) {
        y = 100;
        time = "NA";
      } else if (3.80 < Vbat && Vbat < 4.34) {
        z = (Vbat - 4.0542) / 0.1342;
        y =
            0.60171 * Math.pow(z, 5) - 1.5033 * Math.pow(z, 4) - 3.2509 * Math.pow(z, 3) - 6.0626 * Math.pow(z, 2)
                + 28.715 * z + 47.432;
        if (Math.round(y) > 100.0) {
          y = 100;
        } else if (Math.round(y) < 0) {
          y = 0;
        } else {}
        float t = (float) (3.20 * (1 - y / 100) * 60);
        int hours = (int) (t / 60);
        int minutes = (int) (t % 60);
        time = hours + ":" + minutes;
      }
      batteryPercent = y;
    } else if (Vin < 1.0 && Vout > 4.0) {
      Logger.debug("Node 2 Discharge Mode");
      mode = "D";
      // Discharge
      if (Vbat <= 3.60) {
        y = 0;
        time = "NA";
      } else if (Vbat >= 4.22) {
        y = 100;
        time = "8:00";
      } else if (3.60 < Vbat && Vbat < 4.22) {
        z = (Vbat - 3.863) / 0.17467;
        y = -3.1947 * Math.pow(z, 2) + 29.424 * z + 53.138;
        if (Math.round(y) > 100.0) {
          y = 100;
        } else if (Math.round(y) < 0) {
          y = 0;
        } else {}
        float t = (float) (y * 3.10 * 60) / 100;
        int hours = (int) t / 60;
        int minutes = (int) (t % 60);
        time = hours + ":" + minutes;
      }
      batteryPercent = y;
    }// end else if
    else if (Vin < 1.0 && Vout < 1.0) {
      // idle
      mode = "I";
      Logger.debug("Node 2 Idle mode");
      ElectricalDataDAO dao = new ElectricalDataDAO();
      List<ElectricalData> list = dao.getLatestElectricalData();
      for (ElectricalData data : list) {
        if (data.getNode_id().equalsIgnoreCase("2")) {
          try {
            batteryPercent = Double.parseDouble(data.getBattery_percentage());
            time = "NA";
          } catch (Exception e) {
            Logger.error(e.getMessage(), e);
            batteryPercent = y;
            time = "NA";
          }
        }
      }
    }else{
      Logger.debug("Node 2 Unknown mode");
    }

    /* END OF NODE 2 */
  }
}

