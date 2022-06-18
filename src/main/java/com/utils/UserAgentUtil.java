package com.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.NewsRun;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * @author hcp
 */
public class UserAgentUtil {
    private static final List<String> agentList = new ArrayList<>();
    private static Random random = new Random();

    public static String getAgent() {
        int i = random.nextInt(agentList.size());
        return agentList.get(i);
    }

    public static boolean init() throws Exception {

        HashSet<String> set = new HashSet<>();

        String path = new NewsRun().getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        String[] pathSplit = path.split("/");
        String jarName = pathSplit[pathSplit.length - 1];
        String jarPath = path.replace(jarName, "");
        String pathName = jarPath + "user_agent_list";
        File file = new File(pathName);
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fis, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String s = bufferedReader.readLine();
        while (s != null) {
            if (StringUtils.isNotEmpty(s)) {
                set.add(s);
            }
            s = bufferedReader.readLine();
        }
        agentList.addAll(set);
        return true;
    }
}
