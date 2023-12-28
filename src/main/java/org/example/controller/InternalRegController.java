package org.example.controller;

import org.example.service.ExternalRegService;
import org.example.service.InternalRegService;
import org.example.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yin Zihang
 */
@RestController
@RequestMapping("/internal")
public class InternalRegController {

    InternalRegService internalRegService;

    @Autowired
    InternalRegController(InternalRegService internalRegService) {
        this.internalRegService = internalRegService;
    }

    @GetMapping("/all")
    public Result getAll() {
        return Result.success(internalRegService.findSimpleAll());
    }

    @GetMapping("/id")
    public Result getById(Long id) {
        return Result.success(internalRegService.findInternalRegById(id));
    }
}
