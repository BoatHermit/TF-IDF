package org.example;

import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import org.example.utils.FileReader;

import java.io.File;
import java.io.IOException;
import java.util.*;


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
    Map<Document, Map<Document, Double>> similarities = new HashMap<>();

    /**
     * 储存相似度降序内规列表
     */
    Map<Document, List<Document>> descDocLists = new HashMap<>();

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
     * 计算两篇文档之间相似度
     * @param doc1 文档1
     * @param doc2 文档2
     * @return 相似度
     */
    public double calSimilarity(Document doc1, Document doc2) {
        // 加入未重叠词语
        Map<String, Double> IF_IDFMap1 = new HashMap<>(doc1.IF_IDFMap);
        Map<String, Double> IF_IDFMap2 = new HashMap<>(doc2.IF_IDFMap);
        for(String term : IF_IDFMap1.keySet()) {
            IF_IDFMap2.putIfAbsent(term, 0d);
        }
        for(String term : IF_IDFMap2.keySet()) {
            IF_IDFMap1.putIfAbsent(term, 0d);
        }

        // 计算
        double numerator = 0d, denominator1 = 0d, denominator2 = 0d;
        for(String term : IF_IDFMap1.keySet()) {
            double wk1 = IF_IDFMap1.get(term);
            double wk2 = IF_IDFMap2.get(term);
            numerator += wk1 * wk2;

            denominator1 += wk1 * wk1;
            denominator2 += wk2 * wk2;
        }
        double denominator = Math.sqrt(denominator1 * denominator2);
        return numerator / denominator;
    }

    /**
     * 获得一篇外规和所有内规的相似度降序列表
     */
    private List<Document> getSimilarityList(Document external, List<Document> internalList) {
        Map<Document, Double> sims = new HashMap<>();
        for (Document in : internalList) {
            sims.put(in, calSimilarity(external, in));
        }
        similarities.put(external, sims);
        PriorityQueue<Document> descList = new PriorityQueue<>(new Comparator<Document>() {
            @Override
            public int compare(Document o1, Document o2) {
                if(sims.get(o1) - sims.get(o2) < 0) return 1;
                else if(sims.get(o1) - sims.get(o2) > 0) return -1;
                return 0;
            }
        });
        descList.addAll(internalList);
        List<Document> res = new ArrayList<>(descList);
        return res;
    }

    /**
     * 计算一篇外规的所有相似度
     */
    public void calculateExternalRegulationSimilarities(Document external) {
        if(descDocLists.get(external) == null) {
            descDocLists.put(external, getSimilarityList(external, internalRegulations));
        }
    }

    /**
     * 计算所有外规的所有相似度
     */
    public void calculateAllSimilarities() {
        for(Document external : externalRegulations) {
            calculateExternalRegulationSimilarities(external);
        }
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
