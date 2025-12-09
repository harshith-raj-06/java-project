package com.mail.demo.model;

/**
 * Marker for how critical a mail item is. Higher ordinal = higher priority.
 */
public enum Importance {
    DEFAULT,
    LOW,
    MEDIUM,
    HIGH;

    public static Importance fromString(String value) {
        for (Importance importance : values()) {
            if (importance.name().equalsIgnoreCase(value)) {
                return importance;
            }
        }
        return DEFAULT;
    }
}

