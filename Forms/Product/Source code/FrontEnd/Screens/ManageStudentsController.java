package FrontEnd.Screens;


import BackEnd.DataManager;
import BackEnd.Gender;
import BackEnd.StudentExistsException;
import FrontEnd.Main;
import FrontEnd.Student;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ManageStudentsController implements Initializable {
    @FXML
    private TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, String> nameColumn;
    @FXML
    private TableColumn<Student, String> classColumn;
    @FXML
    private TableColumn<Student, Gender> genderColumn;
    @FXML
    private HBox hbox;
    @FXML
    private Button butBack;
    @FXML
    private Button butAdd;
    @FXML
    private Button butDelete;
    @FXML
    private TextField nameInput;
    @FXML
    private TextField classInput;
    @FXML
    private ComboBox existingClasses;
    @FXML
    private ComboBox genderInput;

    private DataManager dataManager;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        studentTable.setEditable(true);
        dataManager = Main.getDataManager();


        // Initialize table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(e -> {
            if (!isEmpty(e.getNewValue())) {
                Student s = e.getTableView().getItems().get(e.getTablePosition().getRow());
                s.setName(e.getNewValue());
            } else {
                Student s = e.getTableView().getItems().get(e.getTablePosition().getRow());
                s.setName(e.getOldValue());
                nameColumn.setVisible(false);
                nameColumn.setVisible(true);
            }
        });

        classColumn.setCellValueFactory(new PropertyValueFactory<>("previousClass"));
        classColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        classColumn.setOnEditCommit(e -> {
            if (!isEmpty(e.getNewValue())) {
                dataManager.updateClassCount(e.getOldValue(), e.getNewValue());
                Student s = e.getTableView().getItems().get(e.getTablePosition().getRow());
                s.setPreviousClass(e.getNewValue());
            } else {
                Student s = e.getTableView().getItems().get(e.getTablePosition().getRow());
                s.setPreviousClass(e.getOldValue());
                classColumn.setVisible(false);
                classColumn.setVisible(true);
            }
        });

        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));

        ObservableList<Gender> genderList = FXCollections.observableArrayList(Gender.values());
        genderColumn.setCellFactory(ComboBoxTableCell.forTableColumn(genderList));

        genderColumn.setOnEditCommit((TableColumn.CellEditEvent<Student, Gender> e) -> {
            Student s = e.getTableView().getItems().get(e.getTablePosition().getRow());
            s.setGender(e.getNewValue().getText());
        });


        // Initialize table
        studentTable.setItems(dataManager.getStudents());
        studentTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        studentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        studentTable.getSortOrder().add(nameColumn);
        studentTable.sort();

            // Disable column drag
        studentTable.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
            if(e.getButton() == MouseButton.PRIMARY) e.consume();
        });

        // Initialize input fields
        nameInput.setPromptText("Name");
        nameInput.setId("text-field-hbox");

        existingClasses.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null){
                return;
            } else if (newVal.equals("Add...")) {
                classInput.setDisable(false);
            } else {
                classInput.setText("");
                classInput.setDisable(true);
            }
        });
        existingClasses.setId("combo-box-classes");

        classInput.setPromptText("Previous class");
        classInput.setId("text-field-hbox");

        loadClasslist();

        // Initialize objects for boolean binding
        BooleanBinding nameValid = Bindings.createBooleanBinding(() -> {
            return nameInput.textProperty().getValue().matches("\\s*");
        }, nameInput.textProperty());

        BooleanBinding addingClass = Bindings.createBooleanBinding(() -> {
            return existingClasses.getValue().equals("Add...");
        }, existingClasses.valueProperty());

        BooleanBinding classValid = Bindings.createBooleanBinding(() -> {
            return classInput.textProperty().getValue().matches("\\s*");
        }, classInput.textProperty());

        BooleanBinding selectionValid = Bindings.createBooleanBinding(() -> {
            return studentTable.getSelectionModel().getSelectedItems().size() == 0;
        }, studentTable.getSelectionModel().getSelectedItems());

        genderInput.setItems(FXCollections.observableArrayList(Gender.values()));
        genderInput.getSelectionModel().select(1);

        // Initialize buttons
        butAdd.setOnAction(e -> butAddClicked());
        butAdd.disableProperty().bind(
            nameValid.or(addingClass.and(classValid))
        );
        butAdd.setTooltip(new Tooltip("Add new student using provided information"));
        butDelete.setOnAction(e -> butDeleteClicked());
        butDelete.disableProperty().bind(selectionValid);
        butDelete.setTooltip(new Tooltip("Remove selected student"));
        butBack.setOnAction(e -> Main.showScreen("mainMenu"));
        butBack.setTooltip(new Tooltip("Return to main menu"));


        // Initialize horizontal box and increment elements to it
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.setSpacing(5);

    }

    public void butAddClicked() {
        String name = nameInput.getText().trim();
        String previousClass;
        String gender = genderInput.getValue().toString().trim().toLowerCase();

        if (existingClasses.getValue().equals("Add...")) {
            previousClass = classInput.getText().trim();
            classInput.clear();
        } else {
            previousClass = existingClasses.getValue().toString();
        }

        try {
            dataManager.addStudent(name, previousClass, gender);
            nameInput.clear();
            classInput.clear();
            genderInput.getSelectionModel().select(1);
            studentTable.sort();
        } catch (StudentExistsException e) {
            Main.showAlert("studentExists");
        }
    }

    public void butDeleteClicked() {
        ObservableList<Student> selectedStudents = studentTable.getSelectionModel().getSelectedItems();
        dataManager.removeStudent(selectedStudents.get(0));
        studentTable.getSelectionModel().clearSelection();
    }

    public void loadClasslist() {
        List classes = dataManager.getClasses();
        existingClasses.getItems().clear();
        existingClasses.setItems(FXCollections.observableList(classes));
        existingClasses.getSelectionModel().selectLast();
    }

    private boolean isEmpty(String text) {
        if (text.matches("\\s*")) return true;
        return false;
    }
}
