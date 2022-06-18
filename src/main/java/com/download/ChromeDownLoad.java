package com.download;

import com.parser.PageParser;
import com.utils.NewsStore;
import java.util.concurrent.TimeUnit;

import com.utils.UserAgentUtil;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hcp
 */
@RequiredArgsConstructor
public class ChromeDownLoad {
  public static Logger logger = LoggerFactory.getLogger(ChromeDownLoad.class);

  private final String url;

  private final PageParser pageParser;

  /**
   * 通用chromeDriver获取方法
   *
   *
   */
  public void getPage() {
    WebDriver webDriver = getWebDriver();
    try {
      webDriver.get(url);
      String pageSource = webDriver.getPageSource();
//      System.out.println(pageSource);
      int size = pageParser.parserHtml(pageSource);
      if (size == 0){
        NewsStore.doNotifyError("请求错误,页面加载失败!url:"+url);
      }
    }catch (Exception e){
      logger.error("页面加载超时,url:{}",url);
      webDriver.close();
      webDriver = getWebDriver();
    }finally {
      webDriver.close();
    }
  }

  public ChromeDriver getWebDriver(){
    String user_agent = UserAgentUtil.getAgent();
    String userAgent = String.format("User-Agent=%s", user_agent);
    String osName = System.getProperty("os.name");
    logger.info("当前系统类型，{}",osName);
    ClassLoader classLoader = this.getClass().getClassLoader();
    String linuxDriverPath = classLoader.getResource("driver/chromedriver").getPath();
    String winDriverPath = classLoader.getResource("driver/chromedriver.exe").getPath();
    String linuxChromePath = classLoader.getResource("driver/google-chrome-stable_current_amd64_71.0.3578.80.deb").getPath();
    System.out.println(linuxDriverPath);
    System.out.println(linuxChromePath);
    ChromeOptions chromeOptions = new ChromeOptions();
    if (osName.toUpperCase().indexOf("WIN") > -1){
      System.setProperty("webdriver.chrome.driver", winDriverPath);
    }else{
      System.setProperty("webdriver.chrome.driver", linuxDriverPath);
      chromeOptions.setBinary(linuxChromePath);
    }

    chromeOptions.addArguments("--no-sandbox");
    chromeOptions.addArguments("start-maximized");
    chromeOptions.addArguments("disable-infobars");
    chromeOptions.addArguments("--disable-dev-shm-usage");
    chromeOptions.addArguments("--test-type");
    chromeOptions.addArguments("--disable-extensions");
    chromeOptions.addArguments("--headless");
    chromeOptions.setExperimentalOption("useAutomationExtension", false);
    chromeOptions.addArguments("--disable-dev-shm-usage");
    chromeOptions.addArguments(userAgent);

    ChromeDriver webDriver = new ChromeDriver(chromeOptions);
    Dimension dimension = new Dimension(1, 1);
    webDriver.manage().window().setSize(dimension);
    Point p = new Point(100,0);
    webDriver.manage().window().setPosition(p);
    webDriver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
    webDriver.manage().timeouts().setScriptTimeout(20,TimeUnit.SECONDS);
    webDriver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);
    return webDriver;
  }

}
