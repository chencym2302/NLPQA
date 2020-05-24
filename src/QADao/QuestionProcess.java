/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package QADao;


import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import NLPUtil.NLPIR;
import NLPUtil.QuestionType;

/**
 *
 * @author Administrator
 */
public class QuestionProcess {
    private HashSet<String> keywordTypeSet;  
    private int type;
    private LinkedList<String> keywords;
    public QuestionProcess() {
        init();
    }
    private void init(){
        String[] typeKeep = {"n", "v", "a", "d", "m", "ns", "nr", "t"};
        keywordTypeSet = new HashSet<>();
        keywordTypeSet.addAll(Arrays.asList(typeKeep));
    }
    
    public boolean process(String question, NLPIR nlpir,String questionType){
        if(questionType.indexOf("人")!=-1) 
            this.type = 0;
        else if(questionType.indexOf("地点")!=-1) 
            this.type = 1;
        else if(questionType.indexOf("时间")!=-1) 
            this.type = 2;
        nlpir.sentenceProcess(question, 0);
        //type = judgeType( nlpir.sentenceProcess(question, 0) );
        //if(type == -1) return false;
        nlpir.setTagRank( NLPIR.PKU_RANK_FIRST);
        String[] words = nlpir.sentenceProcess(question, 1).split(" ");   
        keywords = new LinkedList<>();
        for(String s : words){
            String[] tmp = s.split("/");
            if( keywordTypeSet.contains(tmp[1])){
                keywords.addLast(tmp[0]);
            }
        }  
        keywords.addLast(questionType);    
        return true;
    }
    /*
    //判断问题类型
    private int judgeType(String words){    
        String[][] tmp = QuestionType.specialwords;
        for(int i=0;i<tmp.length;i++){
            for(int j=0;j<tmp[i].length;j++){
                if(words.contains(tmp[i][j])){
                    return i;
                }
            }
        }        
        boolean flag = false;
        int[] score = new int[tmp.length + 1];
        for(int i=0;i<score.length;i++){
            score[i] = 100;
        }
        score[score.length-1] = 5;
        String[] r = QuestionType.commonwords;
        for(int i=0;i<r.length;i++){
            if(words.contains(r[i])){
                flag = true;       
                int pos = words.indexOf(r[i]);
                tmp = QuestionType.questionfocus;
                for(int j=0;j<tmp.length;j++){
                    for(int k=0;k<tmp[j].length;k++){
                        if(words.contains(tmp[j][k])){
                            int pos2 = words.indexOf(tmp[j][k], pos);
                            score[j] = Math.min(score[j], Math.abs(pos2 - pos));
                        }
                    }
                }
            }
        }      
        if(!flag)
        return -1;     
        int ret = 0;
        int min = score[0];
        for(int i=1;i<score.length;i++){
            if(min > score[i]){
                min = score[i];
                ret = i;
            }
        }
        return ret;
    }
    */
    //获取问题类型
    public int getType(){
        return type;
    }
    //获取关键词
    public LinkedList<String> getKeywords(){
        return keywords;
    }  
}
