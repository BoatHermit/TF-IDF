package org.example.service;

import org.example.model.vo.ExternalFile;
import org.example.model.vo.ExternalSFile;

import java.util.List;

/**
 * @author Yin Zihang
 */
public interface ExternalRegService {

    /**
     * 返回全部外规列表
     * @return 外规列表
     */
    List<ExternalSFile> findSimpleAll();

    List<ExternalFile> findAll();

    ExternalFile findExternalRegById(Long id);
}
