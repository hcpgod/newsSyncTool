package com;

import com.download.ChromeDownLoad;
import com.parser.PageParser;
import com.parser.parseImpl.BinnanceParser;
import com.parser.parseImpl.HooParser;
import com.parser.parseImpl.MexcParser;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.utils.UserAgentUtil;
import javafx.scene.control.ChoiceDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hcp
 */
public class NewsRun {
  private static Logger logger;
  public static int Intervals = 10;

  public static void main(String[] args) {
    logger = LoggerFactory.getLogger(NewsRun.class);
    // 发放任务任务
    init();
    ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(4);
    scheduled.scheduleAtFixedRate(() -> doSync(), 0, Intervals, TimeUnit.SECONDS);
  }

  private static void init(){
    logger.warn("初始化...");
    UserAgentUtil.init();
  }

  public static List<ChromeDownLoad> getDownList(){
    ArrayList<ChromeDownLoad> downList = new ArrayList<>();
    PageParser hooParser = new HooParser();
    PageParser binnanceParser = new BinnanceParser();
    PageParser mexcParser = new MexcParser();
//    downList.add(new ChromeDownLoad(hooParser.getUrl(),hooParser));
//    downList.add(new ChromeDownLoad(binnanceParser.getUrl(),binnanceParser));
    downList.add(new ChromeDownLoad(mexcParser.getUrl(),mexcParser));
    return downList;
  }

  private static void doSync() {
    List<ChromeDownLoad> downList = getDownList();
    downList.forEach(chromeDownLoad -> {
      new SyncTask(chromeDownLoad).run();
    });
  }


  static class SyncTask implements Runnable{
    public ChromeDownLoad downLoad;

    public SyncTask(ChromeDownLoad downLoad) {
      this.downLoad = downLoad;
    }

    @Override
    public void run() {
      downLoad.getPage();
    }
  }
}
