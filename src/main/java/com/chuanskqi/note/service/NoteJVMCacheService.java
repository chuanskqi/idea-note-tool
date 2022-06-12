package com.chuanskqi.note.service;

import com.chuanskqi.note.pojo.Note;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class NoteJVMCacheService implements NoteService {

    List<Note> allNotes = new ArrayList<>();

    public void refresh(List<Note> all) {
        this.allNotes = all;
    }

    @Override
    public List<Note> getAllNotes() {
        return allNotes;
    }

    @Override
    public void addNote(Note note) {
        allNotes.add(note);
    }

    @Override
    public void deleteNoteByCategory(String category) {
        List<Note> collect = allNotes.stream()
            .filter(note -> StringUtils.equals(note.getCategory(), category))
            .collect(Collectors.toList());
        allNotes.removeAll(collect);
    }

    @Override
    public void deleteNoteById(String id) {
        List<Note> collect = allNotes.stream()
            .filter(note -> StringUtils.equals(note.getId(), id))
            .collect(Collectors.toList());
        allNotes.removeAll(collect);
    }

    @Override
    public Note getNote(String id) {
        List<Note> collect = allNotes.stream()
            .filter(e -> id.equals(e.getId()))
            .collect(Collectors.toList());
        return collect.get(0);
    }

    @Override
    public void editCategory(String oriCategory, String newCategory) {
        allNotes.stream()
            .filter(note -> StringUtils.equals(oriCategory, note.getCategory()))
            .forEach(note -> {
                note.setCategory(newCategory);
            });
    }
}
