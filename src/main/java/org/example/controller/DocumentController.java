package org.example.controller;

import org.example.service.DocumentsService;
import org.example.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yin Zihang
 */
@RestController
@RequestMapping
public class DocumentController {

    DocumentsService documentsService;

    @Autowired
    DocumentController(DocumentsService documentsService) {
        this.documentsService = documentsService;
    }

    @GetMapping("/getSimilarityById")
    public Result getSimilarityById(Long id) {
        return Result.success(documentsService.getSimilarityById(id));
    }
}
