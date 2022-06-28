package com.chuanskqi.note.service;

import com.intellij.openapi.project.Project;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NoteContextHolder {

    /**
     * 每个项目拥有不同的NoteContext
     */
    private static Map<Project, NoteContext> projectNoteContextMap = new ConcurrentHashMap<>();

    public static NoteContext current(Project project) {
        return projectNoteContextMap.get(project);
    }

    /**
     * 初始化笔记上下文
     * @param project 当前项目
     * @return
     */
    public static void regist(Project project, NoteContext noteContext) {
        projectNoteContextMap.put(project, noteContext);
    }
}
