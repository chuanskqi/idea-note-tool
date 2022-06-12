package com.chuanskqi.note.service;

import com.chuanskqi.note.pojo.Note;
import java.util.List;

public interface NoteService {

    public void refresh(List<Note> all);

    /**
     * 获取所有笔记
     * @return
     */
    public List<Note> getAllNotes();

    /**
     * 新增一条笔记
     * @return
     */
    public void addNote(Note note);

    public void deleteNoteByCategory(String category);

    public void deleteNoteById(String id);


    public void editCategory(String oriCategory, String newCategory);


    /**
     * 查询一条笔记
     * @return
     */
    public Note getNote(String id);
}
