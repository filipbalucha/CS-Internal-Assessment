package BackEnd;

import FrontEnd.Main;
import FrontEnd.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.*;

public class DataManager {
    private ObservableList<Student> students;
    private MyHashMap studentCounts;
    private boolean dataLoaded;

    public ObservableList<Student> getStudents() {
        return students;
    }

    public DataManager() {
        students = FXCollections.observableArrayList();
        studentCounts = new MyHashMap();
    }

    public void addStudent(String name, String previousClass, String gender) throws StudentExistsException {
        // Check if student already exists
        for(Student s: students) {
            if (s.getName().equals(name) &&
                s.getPreviousClass().equals(previousClass) &&
                s.getGender().equals(gender)) throw new StudentExistsException();
        }
        // Student does not exist, proceed to adding
        students.add(new Student(name, previousClass, gender));
        if (studentCounts.classAdded(previousClass)) {
            Main.getManageStudentsController().loadClasslist();
        }
    }

    public void removeStudent(Student selectedStudent) {
        if (selectedStudent == null) return;
        students.remove(selectedStudent);
        String previousClass = selectedStudent.getPreviousClass();
        if (studentCounts.classRemoved(previousClass)) {
            Main.getManageStudentsController().loadClasslist();
        }
    }

    public void loadData() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream
                    ("_students.txt"),"UTF-8"));
            input.mark(4);
            // Handle BOM
            if ('\ufeff' != input.read()) input.reset();
            String line, name, previousClass, gender;
            while((line = input.readLine()) != null){
                String[] temp = line.split("\t");
                gender = Security.decrypt(temp[0]);
                previousClass = Security.decrypt(temp[1]);
                name = Security.decrypt(temp[2]);
                try {
                    addStudent(name, previousClass, gender);
                } catch (StudentExistsException e) {
                }
            }
            Main.getCreateGroupsController().updateSliderRange();
            input.close();
            dataLoaded = true;
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void storeData() {
        if (!dataLoaded) return;
        try{
            PrintWriter output = new PrintWriter(new OutputStreamWriter(new FileOutputStream
                    ("_students.txt"),"UTF-8"),false);
            students.forEach(student -> {
                String line = Security.encrypt(student.getGender()) + "\t" +
                              Security.encrypt(student.getPreviousClass()) + "\t" +
                              Security.encrypt(student.getName());
                output.println(line);
            });
            output.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public List getClasses() {
        return studentCounts.getClassList();
    }

    public void updateClassCount(String oldVal, String newVal) {
        if (studentCounts.updateClass(oldVal, newVal)) {
            Main.getManageStudentsController().loadClasslist();
        }
    }

    public int getNumOfStudents() {
        return students.size();
    }

}
