package com.chuanskqi.note.service;

import com.chuanskqi.note.window.ShowNoteViewWapper;
import com.intellij.openapi.project.Project;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Getter;

@Getter
public class NoteContext {


    private static NoteService noteService;

    private static NoteStorage noteStorage;

    private static AtomicBoolean hasInited = new AtomicBoolean(false);

    /**
     * 笔记工具栏
     */
    private static ShowNoteViewWapper showNoteViewWapper;


    public static synchronized void init(Project project) {
        if (hasInited.get()) {
            return;
        }
        // 初始化.
        noteService = new NoteJVMCacheService();
        noteStorage = new NoteStorage(noteService, project);
        showNoteViewWapper = new ShowNoteViewWapper(project);
        hasInited.set(true);
    }

    public static NoteService getNoteService() {
        return noteService;
    }

    public static NoteStorage getNoteStorage() {
        return noteStorage;
    }

    public static ShowNoteViewWapper getShowNoteViewWapper() {
        return showNoteViewWapper;
    }
}
