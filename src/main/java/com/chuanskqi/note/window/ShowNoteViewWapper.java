package com.chuanskqi.note.window;

import com.chuanskqi.note.pojo.Note;
import com.chuanskqi.note.service.NoteContext;
import com.chuanskqi.note.util.CodeUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.TreeSpeedSearch;
import com.intellij.ui.treeStructure.Tree;
import java.awt.*;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 笔记展示
 */
public class ShowNoteViewWapper extends DialogWrapper {
    private Tree tree;

    private DefaultTreeModel model;

    private Project project;

    /**
     * 记录左后一次选中的节点
     */
    private DefaultMutableTreeNode lastSelectedNode;

    public ShowNoteViewWapper(Project project) {
        super(true);
        setTitle("note_view");
        this.project = project;
        init();

    }



    private <T> DefaultMutableTreeNode newChildNode(DefaultMutableTreeNode parent, T childObj) {
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode();
        childNode.setUserObject(childObj);
        parent.add(childNode);
        return childNode;
    }

    @Override
    public JComponent createCenterPanel() {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        root.setUserObject(project.getName());

        List<Note> allNotes = NoteContext.getNoteService().getAllNotes();
        if (CollectionUtils.isNotEmpty(allNotes)) {
            // 按目录分组
            Map<String, List<Note>> noteMap = allNotes.stream()
                .collect(Collectors.groupingBy(Note::getCategory));

            noteMap.forEach((category, notes) -> {
                // 添加目录节点
                DefaultMutableTreeNode categoryNote = newChildNode(root, category);

                if (CollectionUtils.isNotEmpty(notes)) {
                    // 添加笔记节点到目录节点
                    notes.stream().filter(note -> StringUtils.isNotBlank(note.getFileName()))
                        .forEach(note -> newChildNode(categoryNote, note));
                }
            });
        }

        // 创建数据模型
        model = new DefaultTreeModel(root);
        tree = new Tree(model);
        tree.setDragEnabled(true);
        tree.setExpandableItemsEnabled(true);
        tree.setCellRenderer(customCellRenderer());
        // 点击目录树跳转到指定代码行
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
                lastSelectedNode = (DefaultMutableTreeNode)treeSelectionEvent.getPath().getLastPathComponent();
                if (lastSelectedNode.getUserObject() instanceof Note) {
                    Note note = (Note) lastSelectedNode.getUserObject();
                    // 代码跳转
                    CodeUtil.jump(project, note.getFileName(), note.getLineNumber());
                }
            }
        });
        // 可以在界面搜索 进行快速查找
        new TreeSpeedSearch(tree);

        // 工具栏
        ToolbarDecorator toolbarDecorator = ToolbarDecorator.createDecorator(tree);
        // 点击新增
        toolbarDecorator.setAddAction(anActionButton -> addAction(anActionButton));
        // 点击编辑
        toolbarDecorator.setEditAction(anActionButton -> editAction(anActionButton));
        // 点击删除
        toolbarDecorator.setRemoveAction(anActionButton -> removeAction(anActionButton));

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(toolbarDecorator.createPanel(), BorderLayout.CENTER);
        return panel;
    }

    /**
     * 自定义笔记目录的展示样式
     */
    public ColoredTreeCellRenderer customCellRenderer() {
        ColoredTreeCellRenderer coloredTreeCellRenderer = new ColoredTreeCellRenderer() {
            @Override
            public void customizeCellRenderer(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                // value.class = DefaultMutableTreeNode.class
                append(value + "");
            }
        };
        return coloredTreeCellRenderer;
    }

    /**
     * 添加按钮事件
     */
    private void addAction(AnActionButton anActionButton) {
        String newCategoryName = Messages.showInputDialog("", "new category", Messages.getInformationIcon());
        if (StringUtils.isNotBlank(newCategoryName)) {
            DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
            DefaultMutableTreeNode child = newChildNode(root, newCategoryName);

            Note newNote = Note.builder()
                .category(newCategoryName)
                .project(project.getName()).build();
            NoteContext.getNoteService().addNote(newNote);
            model.reload();
        }
    }


    /**
     * 添加笔记
     * @param category 笔记目录
     * @param note 笔记内容
     */
    public void addNote(String category, Note note) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        Enumeration<TreeNode> children = root.children();
        while (children.hasMoreElements()) {
            DefaultMutableTreeNode categoryNode = (DefaultMutableTreeNode) children.nextElement();
            if (StringUtils.equals((String) categoryNode.getUserObject(), category)) {
                newChildNode(categoryNode, note);
                NoteContext.getNoteService().addNote(note);
                model.reload();
                // 展开笔记节点
                expandCategoryNode(categoryNode);
                break;
            }
        }
    }

    /**
     * 展开目录节点
     */
    private void expandCategoryNode(DefaultMutableTreeNode categoryNode) {
        //
        tree.expandPath(new TreePath(new TreeNode[] {(TreeNode) model.getRoot(), categoryNode}));
    }

    private static void expandAll(JTree tree, TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }

        // Expansion or collapse must be done bottom-up
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }

    /**
     * 删除笔记
     */
    private void removeAction(AnActionButton anActionButton) {
        Object node = lastSelectedNode.getUserObject();

        if (node instanceof String) {
            if (lastSelectedNode.getChildCount() > 0) {
                Messages.showErrorDialog("exist note! delete first", "delete note");
            } else {
                if (Messages.YES == Messages.showYesNoCancelDialog((String) node, "dangerous delete, are you sure?", Messages.getWarningIcon())) {
                    // 没有笔记的时候,才允许删除目录
                    DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
                    root.remove(lastSelectedNode);
                    NoteContext.getNoteService().deleteNoteByCategory((String) node);
                    model.reload();
                }
            }
        } else if (node instanceof Note) {
            Note note = (Note) node;
            if (Messages.YES == Messages.showYesNoCancelDialog(note.toString(), "delete note?", Messages.getWarningIcon())) {
                // 删除笔记
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode)lastSelectedNode.getParent();
                parent.remove(lastSelectedNode);
                lastSelectedNode = parent;

                NoteContext.getNoteService().deleteNoteById(note.getId());
                model.reload();
            }
        }
    }

    /**
     * 编辑笔记
     */
    private void editAction(AnActionButton anActionButton) {
        Object categoryName = lastSelectedNode.getUserObject();
        if (categoryName instanceof String) {
            String newCategoryName = Messages.showInputDialog("", "modify category", Messages.getWarningIcon(), (String) categoryName, null);
            if (StringUtils.isNotBlank(newCategoryName)) {
                // 修改目录名称
                lastSelectedNode.setUserObject(newCategoryName);

                // 并把目录下的所有笔记目录都调整为新目录名称
                Enumeration<TreeNode> nodeEnumeration = lastSelectedNode.children();

                while (nodeEnumeration.hasMoreElements()) {
                    DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) nodeEnumeration.nextElement();
                    Note note = (Note) treeNode.getUserObject();
                    note.setCategory(newCategoryName);
                }

                NoteContext.getNoteService().editCategory((String) categoryName, newCategoryName);
                model.reload();
            }
        }
    }
}
