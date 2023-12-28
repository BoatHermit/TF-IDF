package org.example.model.vo;

import com.hankcs.hanlp.seg.common.Term;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Document {
    //是否是内规文件（若为否，department为空）
    boolean isInternal;
    Long id;
    //文件名称
    String title;
    //所属部门（内规）
    String department;
    //分词列表
    List<Term> terms = new ArrayList<>();

    Map<String, Integer> termMap = new HashMap<>();
    Map<String, Double> TFMap = new HashMap<>();
    Map<String, Double> IDFMap = new HashMap<>();
    Map<String, Double> TF_IDFMap = new HashMap<>();
}
