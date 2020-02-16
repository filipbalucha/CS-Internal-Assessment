package FrontEnd.Screens;


import BackEnd.Group;
import BackEnd.GroupGenerator;
import FrontEnd.Main;
import FrontEnd.StudentButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class CreateGroupsController implements Initializable {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox groupList;
    @FXML
    private ComboBox comboBox;
    @FXML
    private Slider slider;
    @FXML
    private TextField numberOutput;
    @FXML
    private ToggleButton genderToggle;
    @FXML
    private ToggleButton classToggle;
    @FXML
    private Button butCreate;
    @FXML
    private Button butExport;
    @FXML
    private Button butClear;
    @FXML
    private Button butBack;
    @FXML
    private Text noGroupsMessage;

    private TextField activityName;


    @Override
    public void initialize(URL url, ResourceBundle rb){
        GroupGenerator groupGenerator = new GroupGenerator();

        scrollPane.setFitToWidth(true);

        // Populate ComboBox
        comboBox.getItems().addAll("Number of groups is:", "Group size is:");
        comboBox.getSelectionModel().selectFirst();
        comboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateSliderRange();
        });

        // Modify Slider properties
        slider.setBlockIncrement(1);
        slider.setMinorTickCount(0);
        slider.setMajorTickUnit(1);
        slider.setMin(2);
        slider.setSnapToTicks(true);

            // Make Slider round to the nearest int
        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            slider.setValue(Math.round(newVal.doubleValue()));
        });

        // Modify TextField properties
            // Update the text in numberOutput TextField as the valueProperty of Slider changes
        numberOutput.textProperty().bindBidirectional(slider.valueProperty(), NumberFormat.getNumberInstance());
        numberOutput.setEditable(false);
        numberOutput.setStyle("-fx-text-fill: #E2B258;");
        numberOutput.setId("text-field-uneditable");

        // Modify ToggleButtons properties
        //genderText.setTooltip(new Tooltip("Make sure the distribution of boys and girls \nper group is approximately the same?"));
        genderToggle.setOnAction(e -> {
            if (genderToggle.isSelected()) {
                genderToggle.setText("YES");
            } else {
                genderToggle.setText("NO");
            }
        });

        //classText.setTooltip(new Tooltip("Disperse students who went to the \nsame class into different groups?"));
        classToggle.setOnAction(e -> {
            if (classToggle.isSelected()) {
                classToggle.setText("YES");
            } else {
                classToggle.setText("NO");
            }
        });

        // Modify Button properties
        butCreate.setOnAction(e -> {
            int num = (int)slider.getValue();
            boolean isGroupSize;
            boolean filterGender = genderToggle.isSelected();
            boolean filterClass = classToggle.isSelected();
            if (comboBox.getSelectionModel().getSelectedItem().equals("Number of groups is:")) {
                isGroupSize = false;
            } else {
                isGroupSize = true;
            }

            setCreationToolsDisable(true);
            populateGroupPanel(groupGenerator.generate(Main.getDataManager(), num, isGroupSize, filterGender, filterClass));
        });
        butCreate.setTooltip(new Tooltip("Create groups based on selected criteria"));

        butExport.setOnAction(e -> groupGenerator.exportCreatedGroups(getActivityName()));
        butExport.setTooltip(new Tooltip("Export created groups to a PDF file"));

        butClear.setOnAction(e -> showMessage());
        butClear.setTooltip(new Tooltip("Clear the list of created groups"));

        butBack.setOnAction(e -> Main.showScreen("mainMenu"));
        butBack.setTooltip(new Tooltip("Return to main menu"));

        showMessage();
    }

    public void updateSliderRange() {
        slider.setMax(Main.getDataManager().getNumOfStudents()/2);
        int average = (int)Math.floor((slider.getMax()-slider.getMin())/2);
        slider.setValue(average);
    }

    private void populateGroupPanel(ArrayList<Group> groups) {
        showScrollPane();

        HBox activityContainer = new HBox();

        TextField activityPermanent = new TextField();
        activityPermanent.setText("Activity name: ");
        activityPermanent.setId("text-field-uneditable");
        activityPermanent.setEditable(false);
        activityPermanent.setMaxWidth(135);
        activityContainer.getChildren().add(activityPermanent);

        activityName = new TextField();
        activityContainer.getChildren().add(activityName);
        groupList.getChildren().add(activityContainer);

        for (Group group: groups) {
            VBox groupContainer = new VBox();
            groupContainer.setSpacing(5);

            TextField captainField = new TextField("No captain selected");
            captainField.setId("text-field-uneditable");
            captainField.setEditable(false);
            groupContainer.getChildren().add(captainField);

            HBox noteContainer = new HBox();

            TextField notePermanent = new TextField();
            notePermanent.setId("text-field-uneditable");
            notePermanent.setText("Note: ");
            notePermanent.setEditable(false);
            notePermanent.setMaxWidth(65);
            noteContainer.getChildren().add(notePermanent);

            TextField note = new TextField();
            group.setNote(note);
            noteContainer.getChildren().add(note);

            groupContainer.getChildren().add(noteContainer);

            for(String name: group.getMembers()) {
                groupContainer.getChildren().add(new StudentButton(name, captainField, group));
            }

            groupList.getChildren().add(new TitledPane("Group " + group.getGroupNum(), groupContainer));
        }
    }

    public void showMessage() {
        butExport.setDisable(true);
        butClear.setDisable(true);
        noGroupsMessage.setVisible(true);
        scrollPane.setVisible(false);
        setCreationToolsDisable(false);
    }

    private void showScrollPane() {
        butExport.setDisable(false);
        butClear.setDisable(false);
        groupList.getChildren().clear();
        noGroupsMessage.setVisible(false);
        scrollPane.setVisible(true);
    }

    public String getActivityName() {
        if (activityName.getText().isEmpty()) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH-mm-ss");
            return String.format("Groups from %s", dateFormat.format(new Date()));
        } else {
            return activityName.getText();
        }
    }

    private void setCreationToolsDisable(boolean bool) {
        butCreate.setDisable(bool);
        slider.setDisable(bool);
        numberOutput.setDisable(bool);
        genderToggle.setDisable(bool);
        classToggle.setDisable(bool);
        comboBox.setDisable(bool);
    }
}
