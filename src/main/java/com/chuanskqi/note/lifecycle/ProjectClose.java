package com.chuanskqi.note.lifecycle;

import com.chuanskqi.note.notice.NoticeService;
import com.chuanskqi.note.service.NoteContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectCloseHandler;
import org.jetbrains.annotations.NotNull;

public class ProjectClose implements ProjectCloseHandler {

    @Override
    public boolean canClose(@NotNull Project project) {
        try {
            NoteContext.getNoteStorage().saveAllNotes();
        } catch (Exception e) {
            e.printStackTrace();
            NoticeService.notice(e.getMessage(), project);
            return true;
        }
        return true;
    }
}
