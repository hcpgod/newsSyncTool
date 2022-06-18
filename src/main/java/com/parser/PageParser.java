package com.parser;

/**
 * @author hcp
 */
public interface PageParser {

  int parserHtml(String pageSource);

  String getUrl();
}
