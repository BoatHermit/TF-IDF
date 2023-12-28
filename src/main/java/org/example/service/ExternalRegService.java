package org.example.service;

import org.example.model.vo.ExternalFile;

import java.util.List;

/**
 * @author Yin Zihang
 */
public interface ExternalRegService {

    /**
     * 返回全部外规列表
     * @return 外规列表
     */
    List<ExternalFile> findAll();

    ExternalFile findExternalRegById(int id);
}
