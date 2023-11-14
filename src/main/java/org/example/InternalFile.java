package org.example;

import lombok.Data;

/**
 * 内规文件类
 */
@Data
public class InternalFile {

    //文件名称
    String name;

    //文件内容
    String content;

    //所属部门（内规）
    String department;

}
