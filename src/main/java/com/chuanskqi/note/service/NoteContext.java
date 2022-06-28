package com.chuanskqi.note.service;

import com.chuanskqi.note.notice.NoticeService;
import com.chuanskqi.note.window.ShowNoteViewWapper;
import com.intellij.openapi.project.Project;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteContext {

    private Project project;

    /**
     * 笔记 增删改查
     */
    private NoteService noteService;

    /**
     * 笔记保存
     */
    private NoteStorage noteStorage;

    /**
     * 笔记展示
     */
    private ShowNoteViewWapper showNoteViewWapper;


    public NoteContext(Project project) {
        NoticeService.notice("init note plugin start", project);
        this.project = project;
        this.noteService = new NoteJVMCacheService();
        this.noteStorage = new NoteStorage(this.noteService, project);
        this.showNoteViewWapper = new ShowNoteViewWapper(project, noteService);
        NoticeService.notice("init note plugin end", project);
    }
}
