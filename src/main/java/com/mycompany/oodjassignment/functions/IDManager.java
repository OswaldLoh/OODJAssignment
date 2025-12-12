package com.mycompany.oodjassignment.functions;
import com.mycompany.oodjassignment.classes.*;
import java.util.HashMap;

public class IDManager {
    private int highestInt = 0;
    private HashMap<String, ?> sourceHashMap;

    // hashmap injector constructor
    public IDManager(HashMap<String, ?> sourceHashMap) {
        this.sourceHashMap = sourceHashMap;
    }

    // generate ID mechanism
    public int generateNewID() {
        highestInt++;
        return highestInt;
    }

    public int getHighestTaskID() {
        for (String key : sourceHashMap.keySet()) {
            int num = Integer.parseInt(key.substring(1));
            if (num > highestInt) {
                highestInt = num;
            }
        }
        return highestInt;
    }
}
