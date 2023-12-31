package org.example.service;

import org.example.model.vo.InternalFile;
import org.example.model.vo.InternalSFile;

import java.util.List;

/**
 * @author Yin Zihang
 */
public interface InternalRegService {

    /**
     * 返回全部内规列表
     * @return 内规列表
     */
    List<InternalSFile> findSimpleAll();

    List<InternalFile> findAll();

    InternalFile findInternalRegById(Long id);
}
