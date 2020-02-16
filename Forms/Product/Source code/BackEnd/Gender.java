package BackEnd;

public enum Gender {

    FEMALE("female"), MALE("male");

    String text;

    Gender(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}