package webcrawler;

import java.util.List;

import models.ElectricalData;
import dao.ElectricalDataDAO;
import play.Logger;

public class CalculateBatteryPercentageAndTimeNode0 extends CalculateBatteryPercentageAndTime {

  public CalculateBatteryPercentageAndTimeNode0(String voltage_in, String voltage_out, String voltage_battery) {
    Vin = Double.parseDouble(voltage_in);
    Vout = Double.parseDouble(voltage_out);
    Vbat = Double.parseDouble(voltage_battery);
    calculate();
  }

  private void calculate() {
    double y = -1;
    double z;
    // node0----------------------------------------------------
    if (Vin > 4.0 && Vout < 1.0) {
      mode = "C";
      Logger.debug("Node 0 Charge Mode");
      // Charge
      if (Vbat <= 3.80) {
        y = 0;
        time = "5:00";
      } else if (Vbat >= 4.34) {
        y = 100;
        time = "NA";
      } else if (3.80 < Vbat && Vbat < 4.34) {
        z = (Vbat - 4.0542) / 0.1342;
        y =
            0.50336 * Math.pow(z, 5) + 1.1644 * Math.pow(z, 4) - 5.2907 * Math.pow(z, 3) - 5.8996 * Math.pow(z, 2)
                + 37.568 * z + 53.873;
        if (Math.round(y) > 100.0) {
          y = 100;
        } else if (Math.round(y) < 0) {
          y = 0;
        } else {}
        float t = (float) (5.9 * (1 - y / 100) * 60);
        int hours = (int) (t / 60);
        int minutes = (int) (t % 60);
        time = hours + ":" + minutes;
        // System.out.println("Time left to charge  " + hours + " hours and " + minutes + " minutes.");
      }
      batteryPercent = y;
    } // end if
    else if (Vin < 1.0 && Vout > 4.0) {
      Logger.debug("Node 0 Discharge Mode");
      mode = "D";
      // Discharge
      if (Vbat <= 3.63) {
        y = 0;
        time = "NA";
        // System.out.println(y + "%");
      } else if (Vbat >= 4.23) {
        y = 100;
        // System.out.println(y + "%");
        // System.out.println("Time left to discharge 8 hours");
        time = "8:00";
      } else if (3.63 < Vbat && Vbat < 4.23) {
        z = (Vbat - 3.8726) / 0.15858;
        y =
            -1.4949 * Math.pow(z, 5) + 3.9983 * Math.pow(z, 4) + 2.7806 * Math.pow(z, 3) - 15.006 * Math.pow(z, 2)
                + 30.303 * z + 58.23;
        if (Math.round(y) > 100.0) {
          y = 100;
          // System.out.println(100 + "%");
        } else if (Math.round(y) < 0) {
          y = 0;
          // System.out.println(0 + "%");
        } else {
          // System.out.println(Math.round(y) + "%");
        }
        float t = (float) (y * 7.9 * 60) / 100;
        int hours = (int) t / 60;
        int minutes = (int) (t % 60);
        time = hours + ":" + minutes;
        // System.out.println(hours + " Hours " + minutes + " Minutes approx. left on charge");
      }
      batteryPercent = y;
    }// end else if
    else if (Vin < 1.0 && Vout < 1.0) {
      // idle
      mode = "I";
      Logger.debug("Node 0 Idle mode");
      ElectricalDataDAO dao = new ElectricalDataDAO();
      List<ElectricalData> list = dao.getLatestElectricalData();
      for (ElectricalData data : list) {
        if (data.getNode_id().equalsIgnoreCase("0")) {
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
      Logger.debug("Node 0 Unknown mode");
    }

    /* END OF NODE 0 */

  }

}

