package FrontEnd.Screens;


import FrontEnd.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {
    @FXML
    private Button butManageStudents;
    @FXML
    private Button butCreateGroups;
    @FXML
    private Button butQuit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize tooltips
        butManageStudents.setTooltip(new Tooltip("Add and remove students or edit student data"));
        butCreateGroups.setTooltip(new Tooltip("Create groups based on a range of criteria"));
        butQuit.setTooltip(new Tooltip("Store changes and leave the application"));
        // Add button listeners
        butManageStudents.setOnAction(e -> Main.showScreen("manageStudents"));
        butCreateGroups.setOnAction(e -> Main.showScreen("createGroups"));
        butQuit.setOnAction(e -> Main.showAlert("quitApplication"));
    }
}
