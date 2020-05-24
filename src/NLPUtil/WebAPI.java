/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NLPUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Administrator
 */
public class WebAPI {
    
        /**
         * 正则匹配获取Web信息
         * @param source
         * @param regex
         * @return 
         */
        public static String extractFirst(String source, String regex){
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(source);        
        if(m.find()) return m.group();
        return "";
    }

    public static LinkedList<IRresult> getResult(String query, int numberRequired) {

        LinkedList<IRresult> resultList = new LinkedList<>();
        //每页有十个结果；
        for (int page = 0; page < numberRequired; page += 10) {
            try {
                //获取从web搜索到的内容
                URL url = new URL("http://www.baidu.com/s?wd=" + URLEncoder.encode(query, "gb2312") + "&pn=" + page);
                BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuilder html = new StringBuilder();
                String tmp;
                while((tmp = bf.readLine()) != null){
                    html.append(tmp);
                }
                //去除无关的代码文本，只获取文字文本
                tmp = extractFirst(html.toString(), "<div id=\"content_left\">.*?<div id=\"page");            
                String[] blocks = tmp.split("<div class=\"result");
                for(int j=1;j<blocks.length;j++){
                    String rAbstract = extractFirst(blocks[j], "<a.*?>.*?</div>").replaceAll("<.*?>|&.*?;", "");
                    resultList.addLast(new IRresult(rAbstract));
                }
            } catch ( IOException ex) {
                Logger.getLogger(WebAPI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return resultList;
    }
}
