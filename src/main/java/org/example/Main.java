package org.example;

import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import org.example.utils.FileReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main {

    //外规目录
    String external = "src/main/resources/外部输入候选集合";

    //内规目录
    String internal = "src/main/resources/匹配内规候选集合";

    //外规文件列表
    List<ExternalFile> externalFiles = new ArrayList<>();

    //内规文件列表
    List<InternalFile> internalFiles = new ArrayList<>();

    //处理后的外规文件列表
    List<Document> externalRegulations = new ArrayList<>();

    //处理后的内规文件列表
    List<Document> internalRegulations = new ArrayList<>();

    /**
     * 储存相似度
     */
    Map<Document, Map<Document, Integer>> similarities = new HashMap<>();
    int docNum;

    /**
     * 含有某分词的文档数
     */
    Map<String, Integer> termDocMap = new HashMap<>();

    /**
     * 输入内外规文件
     */
    public void loadFiles() {
        FileReader fileReader = new FileReader();
        externalFiles = fileReader.readExternalFile(external);
        internalFiles = fileReader.readInternalFile(internal);
    }

    /**
     * 调用分词
     */
    public void segmentation() {
        Segmentation segmentation = new Segmentation();
        externalRegulations = segmentation.splitExternal(externalFiles);
        internalRegulations = segmentation.splitInternal(internalFiles);
    }

    /**
     * 计算相似度
     * @param doc1 文档1
     * @param doc2 文档2
     * @return 相似度
     */
    public int calSimilarity(Document doc1, Document doc2) {
        return 0;
    }

    /**
     * 计算所有相似度
     */
    public void calculate() {

    }

    /**
     * 输出excel
     */
    public void outputExcel() {

    }

    public static void main(String[] args) {
        Main main = new Main();
        //读入文件
        main.loadFiles();
        //进行分词
        main.segmentation();
        System.out.println("Hello world!");
    }
}