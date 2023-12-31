package org.example.service.impl;

import org.example.dao.ExternalRegMapper;
import org.example.dao.InternalRegMapper;
import org.example.dao.MarkMapper;
import org.example.model.vo.ExternalSFile;
import org.example.model.vo.SimilarityParam;
import org.example.service.AssessService;
import org.example.service.DocumentsService;
import org.example.service.ExternalRegService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;

@Service
public class AssessServiceImpl implements AssessService {

    @Resource
    ExternalRegMapper externalRegMapper;
    @Resource
    InternalRegMapper internalRegMapper;
    @Resource
    MarkMapper markMapper;

    @Resource
    ExternalRegService externalRegService;
    @Resource
    DocumentsService documentsService;

    @Override
    public double getAP() {
        // 合并排序
        PriorityQueue<Entry<Long, SimilarityParam>> queue = new PriorityQueue<>(
                new Comparator<Entry<Long, SimilarityParam>>() {
            @Override
            public int compare(Entry<Long, SimilarityParam> o1, Entry<Long, SimilarityParam> o2) {
                double s1 = o1.getValue().getSimilarity();
                double s2 = o2.getValue().getSimilarity();
                return Double.compare(s1, s2);
            }
        });

        for(ExternalSFile externalSFile : externalRegService.findSimpleAll()) {
            List<SimilarityParam> simList = documentsService.getSimilarityById(externalSFile.getId());
            for(SimilarityParam simPar : simList) {
//                queue.add(new Entry<>(externalSFile.getId(), simPar));
                HashMap<Long, SimilarityParam> tmp = new HashMap<>();
                tmp.put(externalSFile.getId(), simPar);
                for(Entry<Long, SimilarityParam> e : tmp.entrySet()) queue.add(e);
            }
        }

        // 计算AP
        int k = 0, n = 0;
        double res = 0d;
        for(Entry<Long, SimilarityParam> pair : queue) {
            n ++;
            if(markMapper.findByExternalIdAndInternalId(pair.getKey(), pair.getValue().getId())
                    .getRelevance() == 1) {
                k ++;
            }
            res += ((double) k) / ((double) n);
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
        List<SimilarityParam> simList = documentsService.getSimilarityById(externalId);
        int k = 0, n = 0;
        double res = 0d;
        for(SimilarityParam similarityParam : simList) {
            n ++;
            if(markMapper.findByExternalIdAndInternalId(externalId, similarityParam.getId())
                    .getRelevance() == 1) {
                k ++;
            }
            res += ((double) k) / ((double) n);
        }
        return res / k;
    }

}
