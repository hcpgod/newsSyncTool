package com.message;


import com.pojo.News;

/**
 * 发送消息提醒
 * */
public abstract class MessageNotify {

    public static final long WranTime = 60000L;

    public abstract void sendMessage(News news);

    public abstract void sendMessage(String msg);

    public Long timeStamp = System.currentTimeMillis();

    public String formatMessage(String format,News news){
        return format.replaceAll("[{]url}", news.getNewsUrl())
                .replaceAll("[{]title}",news.getNewsTitle())
                .replaceAll("[{]type}",news.getType())
                .replaceAll("[{]site}",news.getSite());
    }

    public void sendErrorMsg(String msg){
        if (System.currentTimeMillis()-timeStamp > WranTime){
            this.sendMessage(msg);
        }
    }
}
