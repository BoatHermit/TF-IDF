package org.example.model.vo;

import lombok.Data;

/**
 * 外规文件类
 */
@Data
public class ExternalFile {

    Long id;

    //文件名称
    String name;

    //文件内容
    String content;

}
