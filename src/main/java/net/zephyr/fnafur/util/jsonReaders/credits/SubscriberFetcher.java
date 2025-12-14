package net.zephyr.fnafur.util.jsonReaders.credits;

import com.google.gson.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SubscriberFetcher {

    /**
     * Fetches a JSON file from GitHub and extracts a map of usernames and their subscription ranks
     * from the "all_subscribers" array.
     *
     * @param githubUrl the direct URL to the raw JSON file (e.g., https://raw.githubusercontent.com/username/repo/main/subscribers.json)
     * @return a Map where keys are usernames and values are subscription ranks
     * @throws IOException if there’s a problem connecting or reading the file
     */
    public static Map<String, String> fetchSubscribers(String githubUrl) throws IOException {
        // Read the JSON content from the URL
        URL url = new URL(githubUrl);
        try (InputStream input = url.openStream();
             Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {

            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray allSubscribers = json.getAsJsonArray("all_subscribers");

            Map<String, String> userRankMap = new LinkedHashMap<>();

            for (JsonElement elem : allSubscribers) {
                JsonObject obj = elem.getAsJsonObject();
                String username = obj.get("username").getAsString();
                String rank = obj.get("subscription_rank").getAsString();
                userRankMap.put(username, rank);
            }

            return userRankMap;
        }
    }


    public static void GetSubscribers() {
        String githubJsonUrl = "https://raw.githubusercontent.com/OffZephyr/fnafur_data/refs/heads/main/kofi-complete-subscribers.json";
        try {
            Map<String, String> subscribers = fetchSubscribers(githubJsonUrl);
            subscribers.forEach((user, rank) ->
                    System.out.println(user + " → " + rank)
            );
        } catch (IOException e) {
            System.err.println("Error fetching subscribers: " + e.getMessage());
        }
    }
}
