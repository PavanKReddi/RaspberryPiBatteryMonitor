package webcrawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import play.Logger;
import dao.ElectricalDataDAO;


public class WebCrawler {

  public static void crawl() throws IOException {
    try {
      int val = 0;
      ArrayList<String> addr = new ArrayList<String>();
      addr.add("http://172.26.50.67:8192/0");
      addr.add("http://172.26.50.67:8192/1");
      addr.add("http://172.26.50.67:8192/2");
      addr.add("http://172.26.50.67:8192/3");
      Timestamp timestamp = new Timestamp(new Date().getTime());
      for (val = 0; val < addr.size(); val++) {
        URL my_url = new URL(addr.get(val));
        BufferedReader br = new BufferedReader(new InputStreamReader(my_url.openStream()));
        String temp = br.readLine();
        Scanner sc = new Scanner(temp);
        ElectricalDataDAO dao = new ElectricalDataDAO();
        String voltage_in = sc.next();
        String voltage_battery = sc.next();
        String voltage_out = sc.next();
        String current_in = sc.next();
        String current_out = sc.next();
        Logger.debug("--------------------------------------------------------------");
        CalculateBatteryPercentageAndTime calc = null;
        if (val == 0) {
          calc = new CalculateBatteryPercentageAndTimeNode0(voltage_in, voltage_out, voltage_battery);
        } else if (val == 1) {
          calc = new CalculateBatteryPercentageAndTimeNode1(voltage_in, voltage_out, voltage_battery);
        } else if (val == 2) {
          calc = new CalculateBatteryPercentageAndTimeNode2(voltage_in, voltage_out, voltage_battery);
        } else if (val == 3) {
          calc = new CalculateBatteryPercentageAndTimeNode3(voltage_in, voltage_out, voltage_battery);
        }
        dao.insert(val, voltage_in, voltage_battery, voltage_out, current_in, current_out, timestamp,
            calc.getBatteryPercent(), calc.getTime(), calc.getMode());
        Logger.debug("###############################################################");
      }
    } catch (Exception e) {

    }

  }
}

