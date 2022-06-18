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
public class HooParser implements PageParser {

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
      news.setType(SiteEnum.HOO.getTypename());
      news.setSite(SiteEnum.HOO.getTypename());
      newsList.add(news);
    }
    NewsStore.addNewsList(newsList,SiteEnum.HOO.getTypeCode());
    return newsList.size();
  }

  @Override
  public String getUrl() {
    return "https://help.hoorhi.shop/hc/zh-cn/sections/6541592498201-%E6%96%B0%E5%B8%81%E4%B8%8A%E7%BA%BF";
  }
}
