package org.example;

import com.hankcs.hanlp.seg.common.Term;
import lombok.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Document {

    //是否是内规文件（若为否，department为空）
    boolean isInternal;
    //文件名称
    String text;
    //所属部门（内规）
    String department;
    //分词列表
    List<Term> terms = new ArrayList<>();

    Map<String, Integer> termMap = new HashMap<>();
    Map<String, Integer> IF_IDFMap = new HashMap<>();


    /**
     * 预处理
     */
    public void preHandle() {

    }

    /**
     * 计算IF-IDF
     * @return IF-IDF
     */
    public int calIF_IDF() {
        return 0;
    }

}
