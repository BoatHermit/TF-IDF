package org.example.service.impl;

import javafx.util.Pair;
import org.example.dao.ExternalRegMapper;
import org.example.dao.InternalRegMapper;
import org.example.dao.TagMapper;
import org.example.model.vo.ExternalSFile;
import org.example.model.vo.SimilarityParam;
import org.example.service.AssessService;
import org.example.service.DocumentsService;
import org.example.service.ExternalRegService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

@Service
public class AssessServiceImpl implements AssessService {

    @Resource
    ExternalRegMapper externalRegMapper;
    @Resource
    InternalRegMapper internalRegMapper;
    @Resource
    TagMapper tagMapper;

    @Resource
    ExternalRegService externalRegService;
    @Resource
    DocumentsService documentsService;

    @Override
    public double getAP() {
        // 合并排序
        PriorityQueue<Pair<String, SimilarityParam>> queue = new PriorityQueue<>(
                new Comparator<Pair<String, SimilarityParam>>() {
            @Override
            public int compare(Pair<String, SimilarityParam> o1, Pair<String, SimilarityParam> o2) {
                double s1 = o1.getValue().getSimilarity();
                double s2 = o2.getValue().getSimilarity();
                return Double.compare(s1, s2);
            }
        });

        for(ExternalSFile externalSFile : externalRegService.findSimpleAll()) {
            List<SimilarityParam> simList = documentsService.getSimilarityById(externalSFile.getId());
            String exTitle = externalSFile.getTitle();
            for(SimilarityParam simPar : simList) {
                queue.add(new Pair<>(exTitle, simPar));
            }
        }

        // 计算AP
        int k = 0, n = 0;
        double res = 0d;
        for(Pair<String, SimilarityParam> pair : queue) {
            n ++;
            if(tagMapper.findByExternalTitleAndInternalId(pair.getKey(), pair.getValue().getId())
                    .getIsMatch() == 1) {
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
        String exTitle = externalRegMapper.selectById(externalId).getTitle();
        int k = 0, n = 0;
        double res = 0d;
        for(SimilarityParam similarityParam : simList) {
            n ++;
            if(tagMapper.findByExternalTitleAndInternalId(exTitle, similarityParam.getId())
                    .getIsMatch() == 1) {
                k ++;
            }
            res += ((double) k) / ((double) n);
        }
        return res / k;
    }

}
