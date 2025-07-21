package me.huanmeng.bacterium;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class BacteriumCache {
    private static final Set<Integer> currentUsedIds = new LinkedHashSet<>();
    public static boolean jammedAll;

    public static int freeId() {
        int id = ThreadLocalRandom.current().nextInt();
        while (currentUsedIds.contains(id)) {
            id = ThreadLocalRandom.current().nextInt();
        }
        return id;
    }

    // Server Stop
    public static void clearId() {
        currentUsedIds.clear();
    }

    public static boolean isUsed(int id) {
        return currentUsedIds.contains(id);
    }

    public static boolean markUsed(int id){
        return currentUsedIds.add(id);
    }
}
