/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NLPUtil;

import com.sun.jna.Native;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author hrp
 */
public class NLPIR {
    public static final int PKU_RANK_SECOND = 0;//计算所二级标注集
    public static final int PKU_RANK_FIRST = 1;  //计算所一级标注集 
    String errorMsg = "";
    //获取分词系统库
    private NLPLibrary Instance = (NLPLibrary)Native.loadLibrary("D:\\NLP\\NLPQA\\NLPIR", NLPLibrary.class);    
    public boolean init(){    
        
        int init_flag = Instance.NLPIR_Init(".", 1,  "0");     
        if (0 == init_flag) {
            errorMsg = Instance.NLPIR_GetLastErrorMsg();
            return false;
        }
        return true;
    }
    
    public boolean exit(){
        try {
           Instance.NLPIR_Exit();
        } catch (Exception e) {
            errorMsg = e.getMessage();
            return false;
        }
        return true;
    }
    
    public void setTagRank(int RankOfTag){    
       Instance.NLPIR_SetPOSmap(RankOfTag);
        
    }
    
    public String sentenceProcess(String sentence, int tag){
        String nativeBytes = "";  
        try {
            nativeBytes =Instance.NLPIR_ParagraphProcess(sentence, tag);     
            
        } catch (Exception ex) {
            errorMsg = ex.getMessage();
        }
        return nativeBytes;
    }
 
}
