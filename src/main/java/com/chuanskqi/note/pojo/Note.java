package com.chuanskqi.note.pojo;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;


/**
 * 笔记对象
 */
@Data
@Builder
public class Note {

    /**
     * 唯一id
     */
    final String id = UUID.randomUUID().toString();

    /**
     * 项目名称:当前项目的目录
     */
    String project;

    /**
     * 目录,用于自定义文件夹
     */
    String category;

    /**
     * 文件名称：*.java
     */
    @Builder.Default
    String fileName = "";

    /**
     * 代码行号
     */
    int lineNumber;

    /**
     * 别名
     */
    @Builder.Default
    String alias = "";

    /**
     * 代码内容
     */
    @Builder.Default
    String content = "";

    public boolean isCategory() {
        return StringUtils.isBlank(getFileName()) && StringUtils.isNotBlank(getCategory());
    }

    public String toString() {
        if (StringUtils.isBlank(getFileName())) {
            return getCategory();
        }
        return String.format("%s (%s:%s)  ==> %s", getAlias(), getFileName(), getLineNumber() + 1, getContent());
    }

}
