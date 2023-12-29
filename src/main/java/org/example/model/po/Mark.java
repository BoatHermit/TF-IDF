package org.example.model.po;

import lombok.Data;

@Data
public class Mark {
    private Long id;

    private Long ex_id;

    private Long in_id;

    private Integer relevance;

    private Double similarity;
}
