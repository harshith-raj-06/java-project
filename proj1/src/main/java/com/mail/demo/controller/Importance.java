package com.mail.demo.controller;

/**
 * Marker for how critical a mail item is. Higher ordinal = higher priority.
 */
public enum Importance {
    HIGH,
    MEDIUM,
    LOW,
    DEFAULT;

    public static Importance fromString(String value) {
        for (Importance importance : values()) {
            if (importance.name().equalsIgnoreCase(value)) {
                return importance;
            }
        }
        return DEFAULT;
    }
}

