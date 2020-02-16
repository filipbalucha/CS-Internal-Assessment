package FrontEnd.Screens;


import BackEnd.Security;
import FrontEnd.Main;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField userNameField;
    @FXML
    private Text userNameTick;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField keyField;
    @FXML
    private Text passwordTick;
    @FXML
    private Button butSubmit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userNameField.setPromptText("User name");
        userNameField.setId("login-field");
        BooleanBinding userNameValid = Bindings.createBooleanBinding(() -> Security.verifyUsername(userNameField.getText()), userNameField.textProperty());
        userNameTick.visibleProperty().bind(userNameValid);

        passwordField.setPromptText("Password");
        passwordField.setId("login-field");
        BooleanBinding passwordValid = Bindings.createBooleanBinding(() -> Security.verifyPassword(passwordField.getText()), passwordField.textProperty());
        passwordTick.visibleProperty().bind(passwordValid);

        keyField.setPromptText("Unique key");
        keyField.setId("login-field");
        BooleanBinding keyValid = Bindings.createBooleanBinding(() -> Security.validateKey(keyField.getText()), keyField.textProperty());

        butSubmit.disableProperty().bind(userNameValid.not().or(passwordValid.not()).or(keyValid.not()));
        butSubmit.setOnAction(e -> {
            Main.getDataManager().loadData();
            Main.showScreen("mainMenu");
            Main.centerWindow();
        });

//        userNameField.setText("organizer");
//        passwordField.setText("password");
    }
}
