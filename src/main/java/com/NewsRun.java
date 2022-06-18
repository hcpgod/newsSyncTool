package com;

import com.download.ChromeDownLoad;
import com.message.MessageNotify;
import com.message.impl.ServerJiang;
import com.parser.PageParser;
import com.parser.parseImpl.BinnanceParser;
import com.parser.parseImpl.HooParser;
import com.parser.parseImpl.MexcParser;
import com.utils.NewsStore;
import com.utils.UserAgentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author hcp
 */
public class NewsRun {
  private static Logger logger;
  public static int Intervals = 10;
  public static Properties properties;

  public static void main(String[] args) {
    logger = LoggerFactory.getLogger(NewsRun.class);
    // 发放任务任务
    try {
      init();
    } catch (Exception e) {
      return;
    }
    ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(4);
    scheduled.scheduleAtFixedRate(NewsRun::doSync, 0, Intervals, TimeUnit.SECONDS);
  }

  private static void init() throws Exception {
    try{
      UserAgentUtil.init();
    }catch (Exception e){
      logger.error("加载UserAgent错误！");
      throw new Exception("");
    }
    // 读取配置文件
    readConfig();
    // 设置server酱配置
    String sendKey = properties.getProperty("SendKey");
    String titleFormat = properties.getProperty("TitleFormat");
    String contentFormat = properties.getProperty("ContentFormat");
    MessageNotify messageNotify = new ServerJiang(sendKey,titleFormat,contentFormat);
    NewsStore.setMessageNotify(messageNotify);
  }

  public static List<ChromeDownLoad> getDownList(){
    ArrayList<ChromeDownLoad> downList = new ArrayList<>();
    PageParser hooParser = new HooParser();
    PageParser binnanceParser = new BinnanceParser();
    PageParser mexcParser = new MexcParser();
    downList.add(new ChromeDownLoad(properties, hooParser.getUrl(),hooParser));
    downList.add(new ChromeDownLoad(properties, binnanceParser.getUrl(),binnanceParser));
    downList.add(new ChromeDownLoad(properties, mexcParser.getUrl(),mexcParser));
    return downList;
  }

  private static void doSync() {
    List<ChromeDownLoad> downList = getDownList();
    downList.forEach(chromeDownLoad -> new SyncTask(chromeDownLoad).run());
  }

  private static void readConfig() {
    String path = NewsRun.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    String[] pathSplit = path.split("/");
    String jarName = pathSplit[pathSplit.length - 1];
    String jarPath = path.replace(jarName, "");
    String pathName=jarPath+"config.properties";
    Properties properties = new Properties();
    File file = new File(pathName);
    FileInputStream fis = null;
    InputStreamReader inputStreamReader = null;
    try {
      fis = new FileInputStream(file);
      inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
      properties.load(inputStreamReader);
      NewsRun.properties = properties;
    } catch (Exception e) {
      logger.error("配置文件错误，请检查config.properties文件");
    }finally {
      try {
        inputStreamReader.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      try {
        fis.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    //获取配置文件数据
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
