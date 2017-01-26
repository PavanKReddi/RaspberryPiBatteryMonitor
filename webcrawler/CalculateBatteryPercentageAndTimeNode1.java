package webcrawler;

import java.util.List;

import models.ElectricalData;
import dao.ElectricalDataDAO;
import play.Logger;

public class CalculateBatteryPercentageAndTimeNode1 extends CalculateBatteryPercentageAndTime {
  public CalculateBatteryPercentageAndTimeNode1(String voltage_in, String voltage_out, String voltage_battery) {
    Vin = Double.parseDouble(voltage_in);
    Vout = Double.parseDouble(voltage_out);
    Vbat = Double.parseDouble(voltage_battery);
    calculate();
  }

  private void calculate() {
    double y = -1;
    double z;
    // node1----------------------------------------------------------
    if (Vin > 4.0 && Vout < 1.0) {
      mode = "C";
      Logger.debug("Node 1 Charge Mode");
      if (Vbat <= 3.70) {
        y = 0;
        time = "2:00";
      } else if (Vbat >= 4.30) {
        y = 100;
        time = "NA";
      } else if (3.70 < Vbat && Vbat < 4.30) {
        z = (Vbat - 4.00083) / 0.14655;
        y =
            0.65429 * Math.pow(z, 5) + 1.4239 * Math.pow(z, 4) - 6.0711 * Math.pow(z, 3) - 6.7685 * Math.pow(z, 2)
                + 38.554 * z + 54.069;
        if (Math.round(y) > 100.0) {
          y = 100;
          // System.out.println(100 + "%");
        } else if (Math.round(y) < 0) {
          y = 0;
          // System.out.println(0 + "%");
        } else {
          // System.out.println(Math.round(y) + "%");
        }
        float t = (float) (1.85 * (1 - y / 100) * 60);
        int hours = (int) (t / 60);
        int minutes = (int) (t % 60);
        time = hours + ":" + minutes;
        // System.out.println("Time left to charge  " + hours + " hours and " + minutes + " minutes.");
      }
      batteryPercent = y;
    } // end if
    else if (Vin < 1.0 && Vout > 4.0) {
      Logger.debug("Node 1 Discharge Mode");
      mode = "D";
      if (Vbat <= 3.70) {
        y = 0;
        time = "NA";
        // System.out.println(y + "%");
      } else if (Vbat >= 4.28) {
        y = 100;
        // System.out.println(y + "%");
        // System.out.println("Time left to discharge 3 hours 20 minutes");
        time = "3:20";
      } else if (3.70 < Vbat && Vbat < 4.28) {
        z = (Vbat - 3.9457) / 0.16004;
        y = 2.3771 * Math.pow(z, 4) - 2.3825 * Math.pow(z, 3) - 11.281 * Math.pow(z, 2) + 34.172 * z + 57.066;
        if (Math.round(y) > 100.0) {
          y = 100;
          // System.out.println(100 + "%");
        } else if (Math.round(y) < 0) {
          y = 0;
          // System.out.println(0 + "%");
        } else {
          // System.out.println(Math.round(y) + "%");
        }
        float t = (float) (y * 3.38 * 60) / 100;
        int hours = (int) t / 60;
        int minutes = (int) (t % 60);
        time = hours + ":" + minutes;
        // System.out.println(hours + " Hours " + minutes + " Minutes approx. left on charge");
      }
      batteryPercent = y;
    }// end discharge else if
    else if (Vin < 1.0 && Vout < 1.0) {
      // idle
      mode = "I";
      Logger.debug("Node 1 Idle mode");
      ElectricalDataDAO dao = new ElectricalDataDAO();
      List<ElectricalData> list = dao.getLatestElectricalData();
      for (ElectricalData data : list) {
        if (data.getNode_id().equalsIgnoreCase("1")) {
          try {
            batteryPercent = Double.parseDouble(data.getBattery_percentage());
            time = "NA";
          } catch (Exception e) {
            batteryPercent = y;
            time = "NA";
            Logger.error(e.getMessage(), e);
          }
        }
      }
    }else{
      Logger.debug("Node 1 Unknown mode");
    }

    /* END OF NODE 1 */
  }

}

