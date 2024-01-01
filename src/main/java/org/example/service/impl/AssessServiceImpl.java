package org.example.service.impl;

import org.example.dao.MarkMapper;
import org.example.model.vo.ExternalSFile;
import org.example.model.vo.SimilarityParam;
import org.example.service.AssessService;
import org.example.service.DocumentsService;
import org.example.service.ExternalRegService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.Map.Entry;

@Service
public class AssessServiceImpl implements AssessService {

    @Resource
    MarkMapper markMapper;
    @Resource
    ExternalRegService externalRegService;
    @Resource
    DocumentsService documentsService;

    @Override
    public double getAP() {
        // 合并
        Entry<Long, SimilarityParam>[] array = new Entry[52845];
        int ptr = 0;
        for(ExternalSFile externalSFile : externalRegService.findSimpleAll()) {
            List<SimilarityParam> simList = documentsService.getSimilarityListDesc(externalSFile.getId());
            for(SimilarityParam simPar : simList) {
                HashMap<Long, SimilarityParam> tmp = new HashMap<>();
                tmp.put(externalSFile.getId(), simPar);
                for(Entry<Long, SimilarityParam> e : tmp.entrySet()) array[ptr] = e;
                ptr ++;
            }
        }

        // 排序
        Arrays.sort(array, new Comparator<Entry<Long, SimilarityParam>>() {
            @Override
            public int compare(Entry<Long, SimilarityParam> o1, Entry<Long, SimilarityParam> o2) {
                double s1 = o1.getValue().getSimilarity();
                double s2 = o2.getValue().getSimilarity();
                return Double.compare(s2, s1);
            }
        });

        // 计算AP
        int k = 0, n = 0;
        double res = 0d;
        for(Entry<Long, SimilarityParam> pair : array) {
            n ++;
            if(markMapper.findByExternalIdAndInternalId(pair.getKey(), pair.getValue().getId())
                    .getRelevance() == 1) {
                k ++;
                res += k / ((double) n);
            }
        }
        return res / k;
    }

    @Override
    public double getMAP() {
        double APSum = 0d;
        int n = 0;
        for(ExternalSFile externalSFile : externalRegService.findSimpleAll()) {
            APSum += getAPById(externalSFile.getId());
            n ++;
        }
        return APSum / n;
    }

    private double getAPById(Long externalId) {
        List<SimilarityParam> simList = documentsService.getSimilarityListDesc(externalId);
        int k = 0, n = 0;
        double res = 0d;
        for(SimilarityParam similarityParam : simList) {
            n ++;
            if(markMapper.findByExternalIdAndInternalId(externalId, similarityParam.getId())
                    .getRelevance() == 1) {
                k ++;
                res += (k / (double) n);
            }
        }
        if(k == 0) return 0d;
        return res / k;
    }

}
