package com.chuanskqi.note.window;

import com.chuanskqi.note.service.NoteContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;

public class NoteWindow implements ToolWindowFactory {

    /**
     * Create the tool window content.
     *
     * @param project    current project
     * @param toolWindow current tool window
     */
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();

        NoteContext.init(project);
        Content content = contentFactory.createContent(NoteContext.getShowNoteViewWapper().createCenterPanel(), "", false);
        toolWindow.getContentManager().addContent(content);
    }

}