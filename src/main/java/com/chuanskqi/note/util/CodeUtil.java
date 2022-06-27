package com.chuanskqi.note.util;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;

/**
 * 工具类
 */
public class CodeUtil {

    private static final Logger LOG = Logger.getInstance(CodeUtil.class);

    /**
     * 实现代码跳转到目标文件的指定代码行
     *
     * @param project 项目
     * @param fileName 文件名称
     * @param lineNumber 行号
     */
    public static void jump(Project project, String fileName, int lineNumber) {

        // 根据文件名称查找文件
        PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, fileName, GlobalSearchScope.projectScope(project));

        if (psiFiles != null && psiFiles.length > 0) {

            OpenFileDescriptor descriptor = new OpenFileDescriptor(project, psiFiles[0].getVirtualFile());
            // 打开文件
            Editor editor = FileEditorManager.getInstance(project)
                .openTextEditor(descriptor, true);
            if (editor == null) {
                return;
            }

            CaretModel caretModel = editor.getCaretModel();
            LogicalPosition logicalPosition = caretModel.getLogicalPosition();
            logicalPosition.leanForward(true);
            // 移动到目标行
            caretModel.moveToLogicalPosition(new LogicalPosition(lineNumber, 0));
            // 将目标行滚动到屏幕中央
            editor.getScrollingModel().scrollToCaret(ScrollType.CENTER);
            // 移除如果有选择的内容
            //editor.getSelectionModel().removeSelection();
        }
    }
}
