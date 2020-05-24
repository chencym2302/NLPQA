
package NLPUtil;

import com.sun.jna.Library;
import com.sun.jna.Native;

//接口NLPLibrary
public interface NLPLibrary extends Library {
    // 定义并初始化接口的静态变量
    public int NLPIR_Init(String sDataPath, int encoding,  String sLicenceCode);          
    public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);
    public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit, boolean bWeightOut);
    public String NLPIR_GetFileKeyWords(String sLine, int nMaxKeyLimit, boolean bWeightOut);
    public String NLPIR_GetLastErrorMsg();
    public void NLPIR_Exit();  
    public int NLPIR_SetPOSmap(int nPOSmap);
}