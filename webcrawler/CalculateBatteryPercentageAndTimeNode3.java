package webcrawler;

import java.util.List;

import models.ElectricalData;
import dao.ElectricalDataDAO;
import play.Logger;

public class CalculateBatteryPercentageAndTimeNode3 extends CalculateBatteryPercentageAndTime {
  public CalculateBatteryPercentageAndTimeNode3(String voltage_in, String voltage_out, String voltage_battery) {
    Vin = Double.parseDouble(voltage_in);
    Vout = Double.parseDouble(voltage_out);
    Vbat = Double.parseDouble(voltage_battery);
    calculate();
  }

  private void calculate() {
    double y = -1;
    double z;
    // node3------------------------------------------------------
    //Logger.debug(Vin + "-" + Vout);
    if (Vin > 4.0 && Vout < 1.0) {
      mode = "C";
      Logger.debug("Node 3 Charge Mode");
      // Charge
      if (Vbat <= 3.6) {
        y = 0;
        time = "1:30";
      } else if (Vbat >= 4.28) {
        y = 100;
        time = "NA";
      } else if (3.60 < Vbat && Vbat < 4.28) {
        z = (Vbat - 4.0619) / 0.14742;
        y =
            0.88484 * Math.pow(z, 5) + 3.3276 * Math.pow(z, 4) - 1.7287 * Math.pow(z, 3) - 7.5599 * Math.pow(z, 2)
                + 28.13 * z + 55.174;
        if (Math.round(y) > 100.0) {
          y = 100;
        } else if (Math.round(y) < 0) {
          y = 0;
        } else {}
        float t = (float) (1.5 * (1 - y / 100) * 60);
        int hours = (int) (t / 60);
        int minutes = (int) (t % 60);
        time = hours + ":" + minutes;
      }
      batteryPercent = y;
    } // end if
    else if (Vin < 1.0 && Vout > 4.0) {
      Logger.debug("Node 3 Discharge Mode");
      mode = "D";
      // Discharge
      if (Vbat <= 3.66) {
        y = 0;
        time = "NA";
      } else if (Vbat >= 4.23) {
        y = 100;
        time = "1:48";
      } else if (3.66 < Vbat && Vbat < 4.23) {
        z = (Vbat - 3.9081) / 0.15426;
        y = 2.0299 * Math.pow(z, 4) - 2.6668 * Math.pow(z, 3) - 9.5125 * Math.pow(z, 2) + 34.48 * z + 55.855;
        if (Math.round(y) > 100.0) {
          y = 100;
        } else if (Math.round(y) < 0) {
          y = 0;
        } else {}
        float t = (float) (y * 1.8 * 60) / 100;
        int hours = (int) t / 60;
        int minutes = (int) (t % 60);
        time = hours + ":" + minutes;
      }
      batteryPercent = y;
    }// end else if
    else if (Vin < 1.0 && Vout < 1.0) {
      // idle
      mode = "I";
      Logger.debug("Node 3 Idle mode");
      ElectricalDataDAO dao = new ElectricalDataDAO();
      List<ElectricalData> list = dao.getLatestElectricalData();
      for (ElectricalData data : list) {
        if (data.getNode_id().equalsIgnoreCase("3")) {
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
      Logger.debug("Node 3 Unknown mode");
    }

  }
}

