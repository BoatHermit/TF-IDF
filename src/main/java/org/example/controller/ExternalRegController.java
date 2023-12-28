package org.example.controller;

import org.example.service.ExternalRegService;
import org.example.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Yin Zihang
 */
@RestController
@RequestMapping("/external")
public class ExternalRegController {

    ExternalRegService externalRegService;

    @Autowired
    ExternalRegController(ExternalRegService externalRegService) {
        this.externalRegService = externalRegService;
    }

    @GetMapping("/all")
    public Result getAll() {
        return Result.success(externalRegService.findSimpleAll());
    }

    @GetMapping("/id")
    public Result getById(Long id) {
        return Result.success(externalRegService.findExternalRegById(id));
    }
}
