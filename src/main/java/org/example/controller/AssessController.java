package org.example.controller;

import org.example.service.AssessService;
import org.example.utils.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/assess")
public class AssessController {
    @Resource
    AssessService assessService;
    private Double AP;
    private Double MAP;

    @GetMapping("/ap")
    public Result getAP() {
        if(AP == null)
            AP = assessService.getAP();
        return Result.success(AP);
    }

    @GetMapping("/map")
    public Result getMAP() {
        if(MAP == null)
            MAP = assessService.getMAP();
        return Result.success(AP);
    }
}
