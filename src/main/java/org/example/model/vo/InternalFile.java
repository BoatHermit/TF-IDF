package org.example.model.vo;

import lombok.Data;

/**
 * 内规文件类
 */
@Data
public class InternalFile {

    Long id;

    //文件名称
    String title;

    //文件内容
    String content;

    //所属部门（内规）
    String department;

}
