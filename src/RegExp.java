package com.grgbanking.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式匹配
 * @author kevin
 * @date 2018/09/09
 */
public class RegExp {
    public boolean match(String reg, String str) throws Exception {
        return Pattern.matches(reg, str);
    }

    public List<String> find(String reg, String str)  throws Exception{
        Matcher matcher = Pattern.compile(reg).matcher(str);
        List<String> list = new ArrayList<String>();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }

    public List<String> find(String reg, String str, int index)  throws Exception{
        Matcher matcher = Pattern.compile(reg).matcher(str);
        List<String> list = new ArrayList<String>();
        while (matcher.find()) {
            list.add(matcher.group(index));
        }
        return list;
    }

    public String findString(String reg, String str, int index)  throws Exception{
        String returnStr = null;
        List<String> list = this.find(reg, str, index);
        if (list.size() != 0) {
			returnStr = list.get(0);
		}
        return returnStr;
    }

    public String findString(String reg, String str)  throws Exception{
        String returnStr = null;
        List<String> list = this.find(reg, str);
        if (list.size() != 0) {
			returnStr = list.get(0);
		}
        return returnStr;
    }


    /**
     * 解析关键字
     * @param p
     * @return
     */
    public List<String> getKeywords(String p) throws Exception{
        String reg = "(?<=(?<!\\\\)\\$\\{)(.*?)(?=(?<!\\\\)\\})";
        RegExp re = new RegExp();
        List<String> list = re.find(reg, p);
        return list;
    }

}
