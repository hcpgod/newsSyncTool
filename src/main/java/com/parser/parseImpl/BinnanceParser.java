package com.parser.parseImpl;


import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
public class BinnanceParser implements PageParser {

  List<String> codeList = new ArrayList<>();

  @Override
  public int parserHtml(String pageSource) {
    List<News> newsList = new ArrayList<>();
    JSONArray jsonArray = JSONObject
        .parseObject(Jsoup.parse(pageSource).getElementById("__APP_DATA").html()).getJSONObject("routeProps").getJSONObject("b723").getJSONArray("catalogs");
    for (int i = 0; i < jsonArray.size(); i++) {
      JSONObject jsonObject = jsonArray.getJSONObject(i);
      String catalogId = jsonObject.getString("catalogId");
      if (! getCodeList().contains(catalogId)){
        continue;
      }
      String catalogName = jsonObject.getString("catalogName");
      JSONArray articles = jsonObject.getJSONArray("articles");
      for (int i1 = 0; i1 < articles.size(); i1++) {
        JSONObject data = articles.getJSONObject(i1);
        String id = data.getString("id");
        String code = data.getString("code");
        String title = data.getString("title");
        String type = data.getString("type");
        String releaseDate = data.getString("releaseDate");

        News news = new News();
        String url = String.format("https://www.binance.com/zh-CN/support/announcement/%s", code);
        news.setNewsUrl(url);
        news.setNewsTitle(title);
        news.setType(catalogName);
        news.setSite(SiteEnum.BINANCE.getSiteName());
        newsList.add(news);
      }
    }
    NewsStore.addNewsList(newsList,SiteEnum.BINANCE.getTypeCode());
    return newsList.size();
  }

  @Override
  public String getUrl() {
    return "https://www.binance.com/zh-CN/support/announcement/c-48?navId=48";
  }

  private List<String> getCodeList(){
    if(CollectionUtil.isEmpty(codeList)){
      codeList = new ArrayList<>();
      codeList.add("48");
      codeList.add("49");
      codeList.add("50");
    }
    return codeList;
  }
}
