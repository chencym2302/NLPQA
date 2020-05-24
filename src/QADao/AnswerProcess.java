/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package QADao;

import java.util.*;
import NLPUtil.NLPIR;
import NLPUtil.QuestionType;

/**
 *
 * @author Administrator
 */
public class AnswerProcess {
    private int type;
    private HashSet<String> keywords;
    //答案分数
    private HashMap<String, Double> mapAnswerScore;
    private ArrayList<String> listAnswer;
    //获取的文本数目
    private final int limitNumber = 30;
    
    public AnswerProcess(int type, List<String> keywords){
        this.type = type;
        this.keywords = new HashSet<>(keywords);
        mapAnswerScore = new HashMap<>();
    }  
    //处理获取的页面内容，去除不相关的内容
    public void addText(String text, NLPIR nlpir){
        String[] sentences = text.split("[ ?!。！？]+");    
        nlpir.setTagRank(NLPIR.PKU_RANK_SECOND);
        for(String str : sentences){
            if(str.matches("[ ;:.,-_0-9a-zA-Z]+")) continue;
            String[] words = nlpir.sentenceProcess(str, 1).split(" +");
            //如果问题类型为TIME或者LOCATION，则调用extraProcess方法获取word
            if(type == QuestionType.TIME){
                words = extraProcess(words, QuestionType.answerPattern[type], "tg");
            }
            else if(type == QuestionType.LOCATION){
                words = extraProcess(words, QuestionType.answerPattern[type], "ns");
            } 
            int countKey = 1;
            ArrayList<Integer> pos = new ArrayList<>();
            for(int i=0;i<words.length;i++){
                String[] tmp = words[i].split("/");       
                if(keywords.contains(tmp[0])){
                    countKey++;
                    pos.add(i);
                }
            }          
            for(int i=0;i<words.length;i++){
                String[] tmp = words[i].split("/");
                
                if(tmp[0].matches("[ 0-9a-zA-Z.-_/:]+")) continue;         
                if(tmp.length>1 && tmp[1].matches( QuestionType.answerPattern[type]) && !keywords.contains(tmp[0])){
                    int mindis = 40;
                    for(Integer p : pos){
                        mindis = Math.min(mindis, Math.abs( p - i));
                    }
                    //使用置信度+相关度
                    double x = 1.0;
                     x = (1.0 * countKey)/(keywords.size()+1) / (mindis);   
                    if(mapAnswerScore.containsKey(tmp[0])){
                        x += mapAnswerScore.get(tmp[0]);
                    }
                    mapAnswerScore.put(tmp[0], x);
                }
            }            
        }        
    }

    private String[] extraProcess(String[] words, String tagRegex, String tag){
        LinkedList<String> tmpList = new LinkedList<>();
        String sw = "";
        boolean flag = false;      
        for(int i=0;i<words.length;i++){
            String[] tmp = words[i].split("/");
            if(tmp.length<2) continue;   
            if(tmp[1].matches(tagRegex)){
                if(!flag){
                    sw = tmp[0];
                    flag = true;
                }
                else{
                    sw += tmp[0];
                }
            }
            else{
                if(flag){
                   tmpList.addLast(sw + "/"+tag);
                   flag = false;
                }
                tmpList.addLast(words[i]);
            }
        }
        if(flag) tmpList.addLast(sw + "/"+tag);
        String[] ret = new String[tmpList.size()];
        Iterator<String> ite = tmpList.iterator();
        for(int i=0;ite.hasNext();i++){
            ret[i] = ite.next();
        }      
        return ret;
    }
    
    
    private void uniteAnswer(){
        //合并答案
        for(int i=0;i<listAnswer.size() && i<limitNumber;i++){
            for(int k=i+1;k<listAnswer.size() && k<limitNumber;k++){
                String wordA = listAnswer.get(i);
                String wordB = listAnswer.get(k);
                double score = mapAnswerScore.get(wordA) + mapAnswerScore.get(wordB);
                if(wordA.contains(wordB)){
                    mapAnswerScore.put(wordA, score);
                }
                else if(wordB.contains(wordA)){
                    mapAnswerScore.put(wordB, score);
                }
            }
        }
        rankAnswer();
    }
    
    private void rankAnswer(){
        ByValueComparator bvc = new ByValueComparator(mapAnswerScore);
        listAnswer = new ArrayList<>(mapAnswerScore.keySet());
        Collections.sort(listAnswer, bvc);

    }    
    
    //获取答案结果
    public ArrayList<String> getAnswerList(boolean f){
        rankAnswer();
        if(type != QuestionType.OTHER_ENTITY){
            uniteAnswer();
        }        
        return listAnswer;
    }
    //获取答案分数
    public HashMap<String, Double> getAnswerWithScore(){
        return mapAnswerScore;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    static class ByValueComparator implements Comparator<String> {
        HashMap<String, Double> base_map;
 
        public ByValueComparator(HashMap<String, Double> base_map) {
            this.base_map = base_map;
        }
 
        @Override
        public int compare(String arg0, String arg1) {
            if (!base_map.containsKey(arg0) || !base_map.containsKey(arg1)) {
                return 0;
            }
 
            if (base_map.get(arg0) < base_map.get(arg1)) {
                return 1;
            } else if (Math.abs(base_map.get(arg0) - base_map.get(arg1) ) < 1e-10) {
                return 0;
            } else {
                return -1;
            }
        }
    }

}
