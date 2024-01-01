package org.example.service.impl;

import com.hankcs.hanlp.seg.common.Term;
import org.example.model.vo.*;
import org.example.service.DocumentsService;
import org.example.service.ExternalRegService;
import org.example.service.InternalRegService;
import org.example.utils.DoubleUtil;
import org.example.utils.Segmentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Yin Zihang
 * @since 2023/12/28 13:09
 */
@Service
public class DocumentsServiceImpl implements DocumentsService {

    //处理后的外规文件列表
    List<Document> externalRegulations = new ArrayList<>();
    //处理后的内规文件列表
    List<Document> internalRegulations = new ArrayList<>();
    //降序列表
    Map<Long, List<SimilarityParam>> simDescList = new HashMap<>();

    /**
     * 储存相似度
     */
    Map<Document, Map<Document, Double>> similarities = new HashMap<>();

    int docNum;

    /**
     * 含有某分词的文档数
     */
    Map<String, Integer> termDocMap = new HashMap<>();


    private final ExternalRegService externalRegService;
    private final InternalRegService internalRegService;

    @Autowired
    DocumentsServiceImpl(ExternalRegService externalRegService, InternalRegService internalRegService) {
        this.externalRegService = externalRegService;
        this.internalRegService = internalRegService;
        renewDoc();
    }

    public void renewDoc() {
        findAllExternal();
        findAllInternal();

        docNum = externalRegulations.size() + internalRegulations.size();

        for (Document d : externalRegulations) {
            Set<String> words = new HashSet<>();
            for (Term t : d.getTerms()) {
                String word = t.toString();
                if (!words.contains(word)) {
                    words.add(word);
                    if (termDocMap.containsKey(word)) {
                        Integer i = termDocMap.get(word) + 1;
                        termDocMap.put(word, i);
                    } else {
                        termDocMap.put(word, 1);
                    }
                }
            }
        }
        for (Document d : internalRegulations) {
            Set<String> words = new HashSet<>();
            for (Term t : d.getTerms()) {
                String word = t.toString();
                if (!words.contains(word)) {
                    words.add(word);
                    if (termDocMap.containsKey(word)) {
                        Integer i = termDocMap.get(word) + 1;
                        termDocMap.put(word, i);
                    } else {
                        termDocMap.put(word, 1);
                    }
                }
            }
        }

        for (Document d : externalRegulations) {
            calTF(d, true);
            calIDF(d);
            calTF_IDF(d);
        }
        for (Document d : internalRegulations) {
            calTF(d, true);
            calIDF(d);
            calTF_IDF(d);
        }
    }

    @Override
    public List<Document> findAllInternal() {
        List<InternalFile> inFiles = internalRegService.findAll();
        internalRegulations = Segmentation.splitInternal(inFiles);

        return internalRegulations;
    }

    @Override
    public List<Document> findAllExternal() {
        List<ExternalFile> exFiles = externalRegService.findAll();
        externalRegulations = Segmentation.splitExternal(exFiles);
        return externalRegulations;
    }

    public Document getExternalById(Long id) {
        for (Document d : externalRegulations) {
            if (Objects.equals(d.getId(), id)){
                return d;
            }
        }
        return null;
    }

    @Override
    public List<SimilarityParam> getSimilarityById(Long id) {
        if (getExternalById(id) == null) {
            return null;
        }

        return getSimilarity(getExternalById(id));
    }


    /**
     * 计算TF
     * @param standard 使用哪种标准进行计算，true表示除数为term总数
     */
    public void calTF(Document doc, boolean standard) {
        int maxFre = 0;
        for (Term term : doc.getTerms()) {
            String word = term.toString();
            if(doc.getTermMap().get(word) == null){
                doc.getTermMap().put(word, 1);
            } else{
                doc.getTermMap().put(word, doc.getTermMap().get(word) + 1);
                maxFre = Math.max(maxFre, doc.getTermMap().get(word));
            }
        }
        int wordLen = doc.getTerms().size();
        for (String word : doc.getTermMap().keySet()) {
            double tf;
            if (standard) {
                tf = (double) doc.getTermMap().get(word) / wordLen;
            } else {
                tf = (double) doc.getTermMap().get(word) / maxFre;
            }
            doc.getTFMap().put(word, tf);
        }
    }

    /**
     * 计算目标文档的IDF
     * @param doc 文档
     */
    public void calIDF(Document doc) {
        Map<String, Double> IDFMap = new HashMap<>();
        for (Term term : doc.getTerms()) {
            int docNumHasTerm = 0;
            String word = term.toString();
            if (termDocMap.containsKey(word)) {
                docNumHasTerm = termDocMap.get(word);
            } else {
                for (Document d : externalRegulations) {
                    if (d.getTerms().contains(term)) {
                        docNumHasTerm++;
                    }
                }
                for (Document d : internalRegulations) {
                    if (d.getTerms().contains(term)) {
                        docNumHasTerm++;
                    }
                }
                termDocMap.put(word, docNumHasTerm);
            }
            double idf = Math.log((double) docNum / docNumHasTerm);
            IDFMap.put(word, idf);
        }
        doc.setIDFMap(IDFMap);
    }

    /**
     * 计算目标文档的TF_IDF
     * @param doc 文档
     * @return 包含word与TF_IDF值的Map
     */
    public Map<String, Double> calTF_IDF(Document doc) {
        for (String word : doc.getTFMap().keySet()) {
            if (doc.getIDFMap().containsKey(word)) {
                Double valueTF = doc.getTFMap().get(word);
                Double valueIDF = doc.getIDFMap().get(word);
                doc.getTF_IDFMap().put(word, valueIDF * valueTF);
            } else {
                System.err.println("This word exist in TFMap but don't exist in IDFMap:" + word);
            }
        }
        assert doc.getTFMap().size() == doc.getTF_IDFMap().size() : "size of TFMap is not equal TF_IDFMap.size";
        return doc.getTF_IDFMap();
    }

    /**
     * 计算两篇文档之间相似度
     * @param doc1 文档1
     * @param doc2 文档2
     * @return 相似度
     */
    public double calSimilarity(Document doc1, Document doc2) {
        // 加入未重叠词语
        Map<String, Double> IF_IDFMap1 = new HashMap<>(doc1.getTF_IDFMap());
        Map<String, Double> IF_IDFMap2 = new HashMap<>(doc2.getTF_IDFMap());
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
//            wk1 = DoubleUtil.shortDouble(wk1, 5);
            double wk2 = IF_IDFMap2.get(term);
//            wk2 = DoubleUtil.shortDouble(wk2, 5);

            numerator += wk1 * wk2;
//            numerator = DoubleUtil.shortDouble(numerator, 5);

            denominator1 += wk1 * wk1;
//            denominator1 = DoubleUtil.shortDouble(denominator1, 5);
            denominator2 += wk2 * wk2;
//            denominator2 = DoubleUtil.shortDouble(denominator2, 5);
        }
        double denominator = Math.sqrt(denominator1 * denominator2);
        return numerator / denominator;
    }

    public List<SimilarityParam> getSimilarity(Document external) {
        List<SimilarityParam> sims = new ArrayList<>();
        for (Document in : internalRegulations) {
            SimilarityParam similarityParam = new SimilarityParam();
            similarityParam.setId(in.getId());
            similarityParam.setTitle(in.getTitle());
            similarityParam.setDepartment(in.getDepartment());
            similarityParam.setSimilarity(calSimilarity(external, in));
            sims.add(similarityParam);
        }
        return sims;
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

    @Override
    public List<SimilarityParam> getSimilarityListDesc(Long id) {
        if(simDescList.get(id) == null) {
            Document ex = getExternalById(id);
            List<Document> inDesc = getSimilarityList(ex, internalRegulations);
            List<SimilarityParam> ret = new ArrayList<>();
            for(Document inDoc : inDesc) {
                SimilarityParam similarityParam = new SimilarityParam();
                similarityParam.setId(inDoc.getId());
                similarityParam.setTitle(inDoc.getTitle());
                similarityParam.setDepartment(inDoc.getDepartment());
                similarityParam.setSimilarity(similarities.get(ex).get(inDoc));
                ret.add(similarityParam);
            }
            simDescList.put(id, ret);
            return ret;
        }
        return simDescList.get(id);
    }
}
