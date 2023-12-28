package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.dao.InternalRegMapper;
import org.example.model.po.InternalReg;
import org.example.model.vo.InternalFile;
import org.example.service.InternalRegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yin Zihang
 */
@Service
public class InternalRegServiceImpl implements InternalRegService {
    private final InternalRegMapper internalRegMapper;

    @Autowired
    InternalRegServiceImpl(InternalRegMapper internalRegMapper) {
        this.internalRegMapper = internalRegMapper;
    }

    @Override
    public List<InternalFile> findAll() {
        List<InternalReg> inRegs = internalRegMapper.selectList(new LambdaQueryWrapper<>());
        List<InternalFile> inFiles = new ArrayList<>();
        for (InternalReg inReg : inRegs) {
            InternalFile inFile = new InternalFile();
            inFile.setId(inReg.getId());
            inFile.setName(inReg.getTitle());
            inFile.setDepartment(inReg.getDepartment());
            inFile.setContent(inReg.getContent());
            inFiles.add(inFile);
        }
        return inFiles;
    }

    @Override
    public InternalFile findInternalRegById(int id) {
        InternalReg inReg = internalRegMapper.selectById(id);
        InternalFile inFile = new InternalFile();
        inFile.setId(inReg.getId());
        inFile.setName(inReg.getTitle());
        inFile.setDepartment(inReg.getDepartment());
        inFile.setContent(inReg.getContent());

        return inFile;
    }
}
