package FrontEnd;

import BackEnd.DataManager;
import FrontEnd.Screens.CreateGroupsController;
import FrontEnd.Screens.LoginController;
import FrontEnd.Screens.MainMenuController;
import FrontEnd.Screens.ManageStudentsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {
    private static Stage window, alert;
    private static Scene login, mainMenu, manageStudents, createGroups, studentExists, quitApplication, removeStudent;
    private static LoginController loginController;
    private static ManageStudentsController manageStudentsController;
    private static CreateGroupsController createGroupsController;
    private static MainMenuController mainMenuController;
    private static DataManager dataManager;

    public static void main(String[] args) {
        dataManager = new BackEnd.DataManager();
        launch(args);
    }

    @Override
    public void start(Stage window) throws Exception {
        this.window = window;

        // Load screens + access to their controllers
            // Log in screen
        URL location = getClass().getResource("Screens/Login.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        Parent root = (Parent) fxmlLoader.load(location.openStream());
        login = new Scene(root);
        loginController = (LoginController) fxmlLoader.getController();

            // FrontEnd.Main Menu
        location = getClass().getResource("Screens/MainMenu.fxml");
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        root = (Parent) fxmlLoader.load(location.openStream());
        mainMenu = new Scene(root);
        mainMenuController = (MainMenuController) fxmlLoader.getController();

            // Manage Students
        location = getClass().getResource("Screens/ManageStudents.fxml");
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        root = (Parent) fxmlLoader.load(location.openStream());
        manageStudents = new Scene(root);
        manageStudentsController = (ManageStudentsController) fxmlLoader.getController();

            // Create Groups
        location = getClass().getResource("Screens/CreateGroups.fxml");
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        root = (Parent) fxmlLoader.load(location.openStream());
        createGroups = new Scene(root);
        createGroupsController = (CreateGroupsController) fxmlLoader.getController();


        // Load alerts
        root = FXMLLoader.load(getClass().getResource("Alerts/StudentExistsAlert.fxml"));
        studentExists = new Scene(root);

        root = FXMLLoader.load(getClass().getResource("Alerts/QuitApplicationAlert.fxml"));
        quitApplication = new Scene(root);

        alert = new Stage();

        // Add first scene to stage, make stage visible
        window.setOnCloseRequest(e -> closeProgram());
        window.setTitle("Group creator");
        window.setScene(login);
        window.show();
    }

    public static void closeProgram() {
        dataManager.storeData();
        window.close();
    }

    public static void showScreen(String screenTitle) {
        switch (screenTitle) {
            case "mainMenu":
                window.setScene(mainMenu);
                break;
            case "manageStudents":
                window.setScene(manageStudents);
                break;
            case "createGroups":
                createGroupsController.showMessage();
                window.setScene(createGroups);
                break;
        }
    }

    public static void showAlert(String alertTitle) {
        switch (alertTitle) {
            case "studentExists":
                alert.setScene(studentExists);
                alert.show();
                break;
            case "quitApplication":
                alert.setScene(quitApplication);
                alert.show();
                break;
        }
    }

    public static void closeAlert() {
        alert.close();
    }

    public static DataManager getDataManager() {
        return dataManager;
    }

    public static ManageStudentsController getManageStudentsController() {
        return manageStudentsController;
    }

    public static CreateGroupsController getCreateGroupsController() {
        return createGroupsController;
    }

    public static void centerWindow() {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        window.setX((screenBounds.getWidth() - window.getWidth()) / 2);
        window.setY((screenBounds.getHeight() - window.getHeight()) / 2);
    }
}
