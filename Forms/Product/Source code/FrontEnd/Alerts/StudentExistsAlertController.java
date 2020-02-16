package FrontEnd.Alerts;

import FrontEnd.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentExistsAlertController implements Initializable {
    @FXML
    private Button butOk;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        butOk.setId("button-alert");
        butOk.setOnAction(e -> Main.closeAlert());
    }
}
