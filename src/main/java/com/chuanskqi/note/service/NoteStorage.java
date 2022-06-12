package com.chuanskqi.note.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import com.chuanskqi.note.notice.NoticeService;
import com.chuanskqi.note.pojo.Note;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.project.Project;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class NoteStorage  {

    String path;

    NoteService noteService;

    public NoteStorage(NoteService noteService, Project project) {
        this.noteService = noteService;
        this.path = project.getBasePath() + File.separator + project.getName() + "_note.iml";

        NoticeService.notice("读取代码笔记路径\n" + path, project);
        File noteListFile = new File(path);
        if (!noteListFile.exists()) {
            try {
                noteListFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        loadNotes(project);
    }

    public void loadNotes(Project project) {
        // 从磁盘读取配置文件
        String fileStr = FileUtil.readString(path, Charset.defaultCharset());
        List<Note> all = new LinkedList<>();
        if (StringUtils.isNotBlank(fileStr)) {
            all = new Gson().fromJson(fileStr, new TypeToken<List<Note>>() {}.getType());
        } else {
            Note defaultNote = Note.builder()
                .project(project.getName())
                .category("default")
                .build();
            all.add(defaultNote);
        }
        noteService.refresh(all);
    }

    public void saveAllNotes() {
        List<Note> allNotes = noteService.getAllNotes();
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        new FileWriter(path).write(builder.create().toJson(allNotes));
    }

}
