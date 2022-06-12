package com.chuanskqi.note.util;

import java.util.UUID;

public class IdGenerator {

    public static String gen() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
