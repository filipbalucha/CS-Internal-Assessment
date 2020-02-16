package BackEnd;

import java.io.Serializable;
import java.util.*;

public class MyHashMap extends HashMap<String,Integer> implements Map<String,Integer>, Cloneable, Serializable {
    public MyHashMap() {
        super();
    }

    public boolean classAdded(String className) {
        if (containsKey(className)) {
            put(className, get(className) + 1);
            return false;
        } else {
            put(className, 1);
            return true;
        }
    }

    public boolean classRemoved(String className) {
        if (get(className) == 1) {
            remove(className);
            return true;
        } else {
            this.put(className, this.get(className)-1);
            return false;
        }
    }

    public boolean updateClass(String oldVal, String newVal) {
        if (classAdded(newVal) | classRemoved(oldVal)) {
            return true;
        }
        return false;
    }

    public List<String> getClassList() {
        List<String> classes;
        if (this.size() == 0) {
            classes = new ArrayList<>();
        } else {
            classes = new ArrayList<>(keySet());
        }
        classes.add("Add...");
        return classes;
    }
}
