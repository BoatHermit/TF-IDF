package org.example.model.po;

import lombok.Data;

/**
 * 对应表 internal_reg
 *
 * @author Yin Zihang
 */
@Data
public class InternalReg {
    private Long id;

    private String department;

    private String title;

    private String content;
}
