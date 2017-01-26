import play.Application;
import play.GlobalSettings;
import play.Logger;
import utils.ScheduleWebCrawler;


public class Global extends GlobalSettings {

  public void onStart(Application app) {
    Logger.info("Application has started");
    ScheduleWebCrawler schedule = new ScheduleWebCrawler();
    schedule.schedule();
  }

  public void onStop(Application app) {
    Logger.info("Application shutdown...");
  }

}
