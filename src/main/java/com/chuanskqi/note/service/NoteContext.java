package com.chuanskqi.note.service;

import cn.hutool.core.date.DateUtil;
import com.chuanskqi.note.notice.NoticeService;
import com.chuanskqi.note.pojo.Note;
import com.chuanskqi.note.window.ShowNoteViewWapper;
import com.google.gson.Gson;
import com.intellij.openapi.project.Project;
import java.util.List;
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
        this.noteService =  new NoteDelegateService(new NoteJVMCacheService()).addNodeChangeListener(new NodeChangeListener() {
            @Override
            public void onChangeBefore() {
                // 保存快照
                List<Note> allNotes = noteService.getAllNotes();
                System.out.println("***********save snapshot start*********** " + DateUtil.now());
                System.out.println(new Gson().toJson(allNotes));
                System.out.println("***********save snapshot end  *********** " + DateUtil.now());
            }

            @Override
            public void onChangeAfter() {
                noteStorage.saveAllNotes();
            }
        });
        this.noteStorage = new NoteStorage(this.noteService, project);
        this.showNoteViewWapper = new ShowNoteViewWapper(project, noteService);
        NoticeService.notice("init note plugin end", project);
    }
}
