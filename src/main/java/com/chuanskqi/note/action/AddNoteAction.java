package com.chuanskqi.note.action;

import com.chuanskqi.note.pojo.Note;
import com.chuanskqi.note.service.NoteContextHolder;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.ui.Messages;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * 添加笔记操作
 */
public class AddNoteAction extends AnAction {
    private static final Logger LOG = Logger.getInstance(AddNoteAction.class);

    // 保留最新一次选择的目录
    private String lastSelectCategory = "";

    @Override
    public void update(@NotNull AnActionEvent e) {
        // 这里设置成false,鼠标右键时将不会显示AddNoteAction菜单, 也就没有入口可以调用 actionPerformed方法
        //e.getPresentation().setEnabledAndVisible(select);
        super.update(e);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        // 1.项目名称
        String projectName = e.getProject().getName();

        // 2.文件名称 Test.Java
        String fileName = e.getRequiredData(CommonDataKeys.PSI_FILE)
            .getViewProvider().getVirtualFile().getName();

        // 3.获取代码行数
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        CaretModel caretModel = editor.getCaretModel();
        Caret primaryCaret = caretModel.getPrimaryCaret();
        // 获取代码行数(这里返回的是编辑器的坐标 x,y 从0开始,即代码行数实际显示的是 x + 1 行)
        LogicalPosition logicalPos = primaryCaret.getLogicalPosition();
        // 可视行()
        //VisualPosition visualPos = primaryCaret.getVisualPosition();

        List<String> allCategory = NoteContextHolder.current(e.getProject()).getNoteService().getAllNotes().stream()
            .map(Note::getCategory)
            .distinct()
            .collect(Collectors.toList());

        // 让最近一次选择的目录优先显示
        Collections.sort(allCategory, (o1, o2) -> lastSelectCategory.equals(o1) ? -1 : 0);

        String selectCode = editor.getDocument().getText().split("\n")[logicalPos.line].trim();
        String category = Messages.showEditableChooseDialog(selectCode,
            "add note", Messages.getInformationIcon(),
            allCategory.toArray(new String[allCategory.size()]), allCategory.get(0), null);

        if (StringUtils.isNotBlank(category)) {
            lastSelectCategory = category;
            String alias = Messages.showInputDialog(fileName, "add alias", Messages.getInformationIcon());
            Note newNote = Note.builder()
                .fileName(fileName)
                .lineNumber(logicalPos.line)
                .content(selectCode)
                .alias(alias)
                .project(projectName)
                .build();
            newNote.setCategory(category);
            NoteContextHolder.current(e.getProject()).getShowNoteViewWapper().addNote(category, newNote);
        }
    }
}
