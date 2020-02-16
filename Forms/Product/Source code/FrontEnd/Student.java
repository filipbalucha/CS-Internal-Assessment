package FrontEnd;


import javafx.beans.property.SimpleStringProperty;

public class Student {
    private SimpleStringProperty name, previousClass, gender;
    public Student(String name, String previousClass, String gender) {
        this.name = new SimpleStringProperty(name);
        this.previousClass = new SimpleStringProperty(previousClass);
        this.gender = new SimpleStringProperty(gender);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getPreviousClass() {
        return previousClass.get();
    }

    public void setPreviousClass(String previousClass) {
        this.previousClass.set(previousClass);
    }

    public String getGender() {
        return gender.get();
    }

    public boolean isMale() {
        return gender.equals("male");
    }

    public void setGender(String gender) {
        this.gender = new SimpleStringProperty(gender);
    }
}
