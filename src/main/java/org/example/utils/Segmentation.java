package org.example.utils;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.dictionary.stopword.StopWordDictionary;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import org.example.model.vo.Document;
import org.example.model.vo.ExternalFile;
import org.example.model.vo.InternalFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 分词类
 */
public class Segmentation {

    /**
     * 对外规文件分词
     * @param externalFiles 外规文件
     * @return 分词结果
     */
    public static List<Document> splitExternal(List<ExternalFile> externalFiles){
//        HanLPClient HanLP = new HanLPClient("https://www.hanlp.com/api", "MzM3MkBiYnMuaGFubHAuY29tOjdxbm0wYzZ2dWxVekROeHY=", "zh", 10); // auth不填则匿名，zh中文，mul多语种
        List<Document> externalRegulations = new ArrayList<>();
        //外规文件分词
        HanLP.Config.ShowTermNature = false;//去除词性
        for (ExternalFile file : externalFiles){
            Document document = new Document();
            document.setId(file.getId());
            document.setInternal(false);
            document.setTitle(file.getTitle());
            document.setDepartment(null);
            //选择停用词表
//            CoreStopWordDictionary.load("src/main/resources/stopwords/baidu_stopwords.txt", false);
//            CoreStopWordDictionary.load("src/main/resources/stopwords/cn_stopwords.txt", false);
//            CoreStopWordDictionary.load("src/main/resources/stopwords/hit_stopwords.txt", false);
//            CoreStopWordDictionary.load("src/main/resources/stopwords/scu_stopwords.txt", false);
            //分词
            document.setTerms(CoreStopWordDictionary.apply(NLPTokenizer.segment(file.getContent())));
            externalRegulations.add(document);
        }
        return externalRegulations;
    }

    /**
     * 对内规文件分词
     * @param internalFiles 内规文件
     * @return 分词结果
     */
    public static List<Document> splitInternal(List<InternalFile> internalFiles){
//        HanLPClient HanLP = new HanLPClient("https://www.hanlp.com/api", "MzM3MkBiYnMuaGFubHAuY29tOjdxbm0wYzZ2dWxVekROeHY=", "zh", 10); // auth不填则匿名，zh中文，mul多语种
        List<Document> internalRegulations = new ArrayList<>();
        //内规文件分词
        HanLP.Config.ShowTermNature = false;//去除词性
        for (InternalFile file : internalFiles){
            Document document = new Document();
            document.setId(file.getId());
            document.setInternal(true);
            document.setTitle(file.getTitle());
            document.setDepartment(file.getDepartment());
            //选择停用词表
//            CoreStopWordDictionary.load("src/main/resources/stopwords/baidu_stopwords.txt", false);
//            CoreStopWordDictionary.load("src/main/resources/stopwords/cn_stopwords.txt", false);
//            CoreStopWordDictionary.load("src/main/resources/stopwords/hit_stopwords.txt", false);
//            CoreStopWordDictionary.load("src/main/resources/stopwords/scu_stopwords.txt", false);
            //分词
            document.setTerms(CoreStopWordDictionary.apply(NLPTokenizer.segment(file.getContent())));
            internalRegulations.add(document);
        }
        return internalRegulations;
    }
}
