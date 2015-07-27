package net.ghostrealms.helpticket.enums;

public enum ReadStatusType {
    READ, UNREAD;
    
    public static ReadStatusType getFromString(String name) {
        if (READ.name().equalsIgnoreCase(name)) { return READ; }
        if (UNREAD.name().equalsIgnoreCase(name)) { return UNREAD; }
        return null;
    }
}