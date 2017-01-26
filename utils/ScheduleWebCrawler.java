package utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import play.libs.Akka;
import scala.concurrent.duration.Duration;
import webcrawler.WebCrawler;

public class ScheduleWebCrawler {

  public void schedule() {
    Akka.system().scheduler().schedule(Duration.create(0, TimeUnit.MILLISECONDS), // Initial delay 0 milliseconds
        Duration.create(10, TimeUnit.SECONDS), new Runnable() {
          public void run() {
            try {
              WebCrawler.crawl();
            } catch (IOException e) {}
          }
        }, Akka.system().dispatcher());
  }

}
