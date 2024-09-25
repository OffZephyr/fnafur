package net.zephyr.fnafur.util;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class GoopyBlacklist {
    public static Map<String, String> Blacklist = new HashMap<>();
    public static Map<String, String> Whitelist = new HashMap<>();
    public static void addToBlacklist(String username, String UUID) {
        Blacklist.put(username, UUID);
    }
    public static void addToWhitelist(String username, String UUID) {
        Whitelist.put(username, UUID);
    }

    public static Map<String, String> getBlacklist() {
        return Blacklist;
    }
    public static Map<String, String> getWhitelist() {
        return Whitelist;
    }
}

