package com.mycompany.oodjassignment.functions;
import com.mycompany.oodjassignment.classes.*;
import java.util.HashMap;

public class IDManager {
    private int highestInt = 0;

    public int generateNewID() {
        return highestInt++;
    }

    public int getHighestTaskID(HashMap<String, ?> Dataset) {
        for (String key : Dataset.keySet()) {
            int num = Integer.parseInt(key.substring(1));
            if (num > highestInt) {
                highestInt = num;
            }
        }
        return highestInt;
    }
}
