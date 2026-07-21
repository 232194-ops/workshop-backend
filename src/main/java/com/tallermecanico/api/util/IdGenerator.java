package com.tallermecanico.api.util;

import java.util.UUID;

/**
 * Helper to generate primary key identifiers as UUID (char(36)),
 * matching the database schema (dbmechanicalworkshop) where every
 * table uses a char(36) primary key instead of an auto-increment value.
 */
public final class IdGenerator {

    private IdGenerator() {
    }

    public static String generate() {
        return UUID.randomUUID().toString();
    }
}
