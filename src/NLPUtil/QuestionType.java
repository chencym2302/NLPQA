/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NLPUtil;

/**
 *
 * @author hrp
 */
public class QuestionType {
    public static final int PERSON = 0;
    public static final int LOCATION = 1;
    public static final int TIME = 2;
    public static final int OTHER_ENTITY = 3;
    
    public static final String[][] specialwords = {{"谁"}, {"哪里","哪儿","何处"}, {"何时","几时","几点","几号"}};   
    public static final String[] commonwords = {"什么", "哪个", "哪"};
    public static final String[][] questionfocus = {{"人"},
                                                {"地方"},
                                                {"时间", "时候", "日期", "天", "年", "月","年份","月份"}};
    public static final String[] extendKeywords = {"", "位于", "时间", ""};
    public static final String[] answerPattern = {"nr", "ns", "t|tg", "n\\w*"};
    
}
