package com.chuanskqi.note.window;

import com.chuanskqi.note.service.NoteContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import javax.swing.*;

public class NoteWindow implements ToolWindowFactory {

    /**
     *
     * Create the tool window content.
     * @param project    current project
     * @param toolWindow current tool window
     */
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        // 初始化笔记内容, 从笔记文件中读取笔记内容
        NoteContext.init(project);
        // 创建一个JComponent展示框,并填充笔记内容
        JComponent centerPanel = NoteContext.getShowNoteViewWapper().createCenterPanel();
        Content content = contentFactory.createContent(centerPanel, "my note", false);
        // 添加到IDEA窗口里就完成了插件窗口注册
        toolWindow.getContentManager().addContent(content);
    }

}