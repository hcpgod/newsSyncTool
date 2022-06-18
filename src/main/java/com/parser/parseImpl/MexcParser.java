package com.parser.parseImpl;

import com.enums.SiteEnum;
import com.parser.PageParser;
import com.pojo.News;
import com.utils.NewsStore;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author hcp
 */
public class MexcParser implements PageParser {

  @Override
  public int parserHtml(String pageSource) {
    Document document = Jsoup.parse(pageSource);
    Elements elements = document.select(".article-list .article-list-item");
    List<News> newsList = new ArrayList<>();
    for (Element element : elements) {
      String title = element.getElementsByTag("a").text();
      String href = element.getElementsByTag("a").attr("href");
      News news = new News();
      news.setNewsUrl(href);
      news.setNewsTitle(title);
      news.setType(SiteEnum.MEXC.getTypename());
      news.setSite(SiteEnum.MEXC.getSiteName());
      newsList.add(news);
    }
    NewsStore.addNewsList(newsList,SiteEnum.MEXC.getTypeCode());
    return newsList.size();

  }

  @Override
  public String getUrl() {
    return "https://support.mexc.com/hc/en-001/sections/360000547811-New-Listings";
  }
}
