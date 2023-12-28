package org.example.controller;

import org.example.service.DocumentsService;
import org.example.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yin Zihang
 * @since 2023/12/28 14:24
 */
@RestController
@RequestMapping("/document")
public class DocumentController {

    DocumentsService documentsService;

    @Autowired
    DocumentController(DocumentsService documentsService) {
        this.documentsService = documentsService;
    }

    @GetMapping
    public Result getSimilarityBy(Long id) {
        return Result.success(documentsService.getSimilarityById(id));

    }
}
