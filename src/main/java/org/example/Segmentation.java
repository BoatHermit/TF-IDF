package org.example;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * 分词类
 */
public class Segmentation {

    private List<Document> externalRegulations = new ArrayList<>();

    private List<Document> internalRegulations = new ArrayList<>();

    /**
     * 对外规文件分词
     * @param externalFiles 外规文件
     * @return 分词结果
     */
    public List<Document> splitExternal(List<ExternalFile> externalFiles){
//        HanLPClient HanLP = new HanLPClient("https://www.hanlp.com/api", "MzM3MkBiYnMuaGFubHAuY29tOjdxbm0wYzZ2dWxVekROeHY=", "zh", 10); // auth不填则匿名，zh中文，mul多语种
        //外规文件分词
        for (ExternalFile file : externalFiles){
            Document document = new Document();
            document.setInternal(false);
            document.setText(file.getName());
            document.setDepartment(null);
            document.setTerms(NLPTokenizer.segment(file.getContent()));
            externalRegulations.add(document);
        }
        return externalRegulations;
    }

    /**
     * 对内规文件分词
     * @param internalFiles 内规文件
     * @return 分词结果
     */
    public List<Document> splitInternal(List<InternalFile> internalFiles){
//        HanLPClient HanLP = new HanLPClient("https://www.hanlp.com/api", "MzM3MkBiYnMuaGFubHAuY29tOjdxbm0wYzZ2dWxVekROeHY=", "zh", 10); // auth不填则匿名，zh中文，mul多语种
        //内规文件分词
        for (InternalFile file : internalFiles){
            Document document = new Document();
            document.setInternal(true);
            document.setText(file.getName());
            document.setDepartment(file.getDepartment());
            document.setTerms(NLPTokenizer.segment(file.getContent()));
            internalRegulations.add(document);
        }
        return internalRegulations;
    }
}
