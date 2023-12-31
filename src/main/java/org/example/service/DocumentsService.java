package org.example.service;


import org.example.model.vo.Document;
import org.example.model.vo.InternalSFile;
import org.example.model.vo.SimilarityParam;

import java.util.List;
import java.util.Map;

/**
 * @author Yin Zihang
 * @since 2023/12/28 13:08
 */
public interface DocumentsService {

    List<Document> findAllInternal();

    List<Document> findAllExternal();

    /**
     * 计算一篇外规与其他内规的相似度
     *
     * @param id 外规id
     */
    List<SimilarityParam> getSimilarityById(Long id);

    /**
     * 计算一篇外规与其他内规的相似度,得到内规相似度降序列表
     *
     * @param id 外规id
     */
    List<SimilarityParam> getSimilarityListDesc(Long id);
}
