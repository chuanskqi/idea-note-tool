package com.chuanskqi.note.service;

import cn.hutool.core.collection.CollectionUtil;
import com.chuanskqi.note.pojo.Note;
import java.util.ArrayList;
import java.util.List;

public class NoteDelegateService implements NoteService {

    NoteService noteService;

    public NoteDelegateService(NoteService noteService) {
        this.noteService = noteService;
    }
    List<NodeChangeListener> nodeChangeListeners = new ArrayList<>();

    public NoteService addNodeChangeListener(NodeChangeListener nodeChangeListener) {
        nodeChangeListeners.add(nodeChangeListener);
        return this;
    }

    private void notifyNodeChangeBefore() {
        if (CollectionUtil.isNotEmpty(nodeChangeListeners)) {
            nodeChangeListeners.forEach(listener -> listener.onChangeBefore());
        }
    }

    private void notifyNodeChangeAfter() {
        if (CollectionUtil.isNotEmpty(nodeChangeListeners)) {
            nodeChangeListeners.forEach(listener -> listener.onChangeAfter());
        }
    }

    @Override
    public void refresh(List<Note> all) {
        noteService.refresh(all);
    }

    @Override
    public List<Note> getAllNotes() {
        return noteService.getAllNotes();
    }

    @Override
    public void addNote(Note note) {
        notifyNodeChangeBefore();
        noteService.addNote(note);
        notifyNodeChangeAfter();
    }

    @Override
    public void deleteNoteByCategory(String category) {
        notifyNodeChangeBefore();
        noteService.deleteNoteByCategory(category);
        notifyNodeChangeAfter();
    }

    @Override
    public void deleteNoteById(String id) {
        notifyNodeChangeBefore();
        noteService.deleteNoteById(id);
        notifyNodeChangeAfter();
    }

    @Override
    public void editCategory(String oriCategory, String newCategory) {
        notifyNodeChangeBefore();
        noteService.editCategory(oriCategory, newCategory);
        notifyNodeChangeAfter();
    }

    @Override
    public Note getNote(String id) {
        return noteService.getNote(id);
    }
}
