package com.chuanskqi.note.service;

import com.chuanskqi.note.pojo.Note;
import java.util.ArrayList;
import java.util.List;

public class NoteJVMCacheMockService extends NoteJVMCacheService {

    @Override
    public List<Note> getAllNotes() {
        List<Note> list = new ArrayList<>();
        Note n1 = Note.builder()
            .project("aaaaaaaa")
            .category("fuction 1")
            .fileName("Test.java")
            .lineNumber(7)
            .build();

        Note n2 = Note.builder()
            .project("aaaaaaaa")
            .category("fuction 2")
            .fileName("Test.java")
            .lineNumber(2)
            .build();

        list.add(n1);
        list.add(n2);

        return list;
    }
}
