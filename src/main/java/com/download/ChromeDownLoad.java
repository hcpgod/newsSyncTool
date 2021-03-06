package com.download;

import com.NewsRun;
import com.parser.PageParser;
import com.utils.NewsStore;

import java.util.Properties;
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

  public final Properties properties;

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
      System.out.println(String.format("开始下载页面: %s",url));
      webDriver.get(url);
      System.out.println(String.format("下载成功: %s",url));
      String pageSource = webDriver.getPageSource();
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
    String linuxDriverPath = properties.getProperty("linuxDriver");
    String winDriverPath = properties.getProperty("winDriver");
    String linuxChromePath = properties.getProperty("linuxChromePath");
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
    System.out.println("开始获取浏览器对象！");
    ChromeDriver webDriver = new ChromeDriver(chromeOptions);
    System.out.println("浏览器对象获取成功！");
    Dimension dimension = new Dimension(1, 1);
    webDriver.manage().window().setSize(dimension);
    Point p = new Point(100,0);
    webDriver.manage().window().setPosition(p);
    webDriver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
    webDriver.manage().timeouts().setScriptTimeout(5,TimeUnit.SECONDS);
    webDriver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
    return webDriver;
  }

}
