package BackEnd;

import javafx.scene.control.TextField;
import java.util.ArrayList;

public class Group {
    private ArrayList<String> members;
    private String leader;
    private TextField note;
    private int groupNum;
    private boolean leaderSet;

    public Group(int groupNum) {
        this.groupNum = groupNum;
        members = new ArrayList<>();
        leader = "Leader not set";
    }

    public void add(String name) {
        members.add(name);
    }

    public int getGroupNum() {
        return groupNum;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setLeader(String leader) {
        this.leader = leader;
        leaderSet = true;
    }

    public String getLeader() {
        if (!leaderSet) return leader;
        return "Leader: " + leader;
    }

    public void setNote(TextField note) {
        this.note = note;
    }

    public String getNote() {
        if (note.getText().isEmpty()) return "No note added";
        return "Note: " + note.getText();
    }
}
