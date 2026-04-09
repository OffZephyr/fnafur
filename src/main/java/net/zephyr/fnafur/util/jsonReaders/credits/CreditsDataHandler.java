package net.zephyr.fnafur.util.jsonReaders.credits;

import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreditsDataHandler {
    public static final List<String> MAIN_CATEGORIES = new ArrayList<>();
    public static final Map<String, List<String>> SUB_PER_MAIN_CATEGORY = new HashMap<>();
    public static final Map<String, Map<String, List<CreditsDataHandler.CreditsEntry>>> ENTRIES = new HashMap<>();
    public static final Map<String, CreditsDataHandler.CreditsEntry> ALL_ENTRIES = new HashMap<>();

    public record CreditsEntry(String NAME, String LINK, Identifier LOGO, int COLOR, List<String> ROLES){};
}
