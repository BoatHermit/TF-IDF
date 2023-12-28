package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.dao.ExternalRegMapper;
import org.example.model.po.ExternalReg;
import org.example.model.vo.ExternalFile;
import org.example.model.vo.ExternalSFile;
import org.example.service.ExternalRegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yin Zihang
 */
@Service
public class ExternalRegServiceImpl implements ExternalRegService {
    private final ExternalRegMapper externalRegMapper;

    @Autowired
    ExternalRegServiceImpl(ExternalRegMapper externalRegMapper) {
        this.externalRegMapper = externalRegMapper;
    }

    @Override
    public List<ExternalSFile> findSimpleAll() {
        List<ExternalReg> exRegs = externalRegMapper.selectList(new LambdaQueryWrapper<>());
        List<ExternalSFile> exFiles = new ArrayList<>();
        for (ExternalReg exReg : exRegs) {
            ExternalSFile exFile = new ExternalSFile();
            exFile.setId(exReg.getId());
            exFile.setTitle(exReg.getTitle());
            exFiles.add(exFile);
        }
        return exFiles;
    }

    @Override
    public List<ExternalFile> findAll() {
        List<ExternalReg> exRegs = externalRegMapper.selectList(new LambdaQueryWrapper<>());
        List<ExternalFile> exFiles = new ArrayList<>();
        for (ExternalReg exReg : exRegs) {
            ExternalFile exFile = new ExternalFile();
            exFile.setId(exReg.getId());
            exFile.setTitle(exReg.getTitle());
            exFile.setContent(exReg.getContent());
            exFiles.add(exFile);
        }
        return exFiles;
    }

    @Override
    public ExternalFile findExternalRegById(Long id) {
        ExternalReg exReg = externalRegMapper.selectById(id);
        ExternalFile exFile = new ExternalFile();
        exFile.setId(exReg.getId());
        exFile.setTitle(exReg.getTitle());
        exFile.setContent(exReg.getContent());
        return exFile;
    }
}
