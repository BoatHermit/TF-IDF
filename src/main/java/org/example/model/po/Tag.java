package org.example.model.po;

import lombok.Data;

@Data
public class Tag {
    private Long id;

    private String externalTitle;

    private Long internalId;

    private Integer isMatch;
}
