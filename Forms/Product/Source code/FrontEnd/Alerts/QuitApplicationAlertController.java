package FrontEnd.Alerts;

import FrontEnd.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class QuitApplicationAlertController implements Initializable {
    @FXML
    private Button butQuit;
    @FXML
    private Button butStay;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        butQuit.setId("button-quit");
        butQuit.setOnAction(e -> {
            Main.closeAlert();
            Main.closeProgram();
        });
        butStay.setId("button-stay");
        butStay.setOnAction(e -> Main.closeAlert());
    }
}
