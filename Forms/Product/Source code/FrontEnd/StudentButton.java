package FrontEnd;

import BackEnd.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class StudentButton extends Button {
    public StudentButton(String name, TextField captainName, Group group) {
        super(name);
        this.setId("");
        setId("button-student");
        setOnAction(e -> {
            captainName.setText("Captain: " + name);
            group.setLeader(name);
        });
    }
}
