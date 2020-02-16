package BackEnd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class GroupGenerator {

    private int numOfGroups;
    private ArrayList<Group> groups;
    private ArrayList<String> studentList;
    private DataManager dataManager;

    public GroupGenerator() {
    }

    public ArrayList<Group> generate (DataManager dataManager, int num, boolean isGroupSize,
                                                  boolean filterGender, boolean filterClass) {
        this.dataManager = dataManager;
        if (isGroupSize) {
            numOfGroups = (int)dataManager.getNumOfStudents()/num;
        } else {
            numOfGroups = num;
        }

        studentList = new ArrayList<>(dataManager.getNumOfStudents());
        if (!filterGender) {
            if (!filterClass) {
                algorithmA();
            } else {
                algorithmB();
            }
        } else {
            if (!filterClass) {
                algorithmC();
            } else {
                algorithmD();
            }
        }
        return groupify();
    }

    private void algorithmA() {
        dataManager.getStudents().forEach(s -> {
            studentList.add(s.getName());
        });
        Collections.shuffle(studentList);
    }

    private void algorithmB() {
        HashMap<String, ArrayList<String>> hm = new HashMap<>();
        dataManager.getStudents().forEach(s -> {
            // Initialize new array list
            if (!hm.containsKey(s.getPreviousClass())) {
                hm.put(s.getPreviousClass(), new ArrayList<>());
            }
            hm.get(s.getPreviousClass()).add(s.getName());
        });
        for (String key: hm.keySet()) {
            Collections.shuffle(hm.get(key));
            studentList.addAll(hm.get(key));
        }
    }

    private void algorithmC() {
        ArrayList<String> males = new ArrayList<>();
        ArrayList<String> females = new ArrayList<>();
        dataManager.getStudents().forEach(s -> {
            if (s.isMale()) {
                males.add(s.getName());
            } else {
                females.add(s.getName());
            }
        });

        Collections.shuffle(males);
        studentList.addAll(males);
        Collections.shuffle(females);
        studentList.addAll(females);
    }

    private void algorithmD() {
        HashMap<String, ArrayList<ArrayList<String>>> hm = new HashMap<>();
        dataManager.getStudents().forEach(s -> {
            // Initialize new array list
            if (!hm.containsKey(s.getPreviousClass())) {
                hm.put(s.getPreviousClass(), new ArrayList<>(2));
                hm.get(s.getPreviousClass()).add(new ArrayList<>());
                hm.get(s.getPreviousClass()).add(new ArrayList<>());
            }
            if (s.isMale()) {
                hm.get(s.getPreviousClass()).get(0).add(s.getName());
            } else {
                hm.get(s.getPreviousClass()).get(1).add(s.getName());
            }
        });

        for (String key: hm.keySet()) {
            ArrayList<String> males = hm.get(key).get(0);
            ArrayList<String> females = hm.get(key).get(1);
            Collections.shuffle(males);
            studentList.addAll(males);
            Collections.shuffle(females);
            studentList.addAll(females);
        }
    }

    private ArrayList<Group> groupify() {
        groups = new ArrayList<Group>();

        int groupNum = 0,
            studentNum = 0;

        for (int i = 0; i < numOfGroups; i++) groups.add(new Group(i+1));
        while (studentNum < dataManager.getNumOfStudents()) {
            groups.get(groupNum).add(studentList.get(studentNum));
            studentNum += 1;
            groupNum = (groupNum + 1) % numOfGroups;
        }
        return groups;
    }

    public void exportCreatedGroups(String activityName) {
        try{
            PrintWriter output = new PrintWriter(new OutputStreamWriter(new FileOutputStream
                    (System.getProperty("user.dir") + File.separator + "Created groups" + File.separator + activityName + ".txt"),"UTF-8"),false);
            for (Group group : groups) {
                output.println("Group " + group.getGroupNum());
                output.println("\t" + group.getLeader());
                output.println("\t" + group.getNote());
                output.println();

                output.println("\tStudents:");
                for (String name: group.getMembers()) {
                    output.println("\t" + name);
                }
                output.println();
            }
            output.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
