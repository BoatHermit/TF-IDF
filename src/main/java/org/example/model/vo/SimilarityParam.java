package org.example.model.vo;

import lombok.Data;

/**
 * @author Yin Zihang
 */
@Data
public class SimilarityParam {
    Long id;

    String title;

    String department;

    double similarity;
}
