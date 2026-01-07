package cs151.view;

import cs151.controller.StudentProfileController;
import cs151.controller.ProgrammingLanguageController;
import cs151.util.UICreator;
import cs151.util.AppUtils;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.*;

public class EditStudentProfileView {
    private final Stage stage;
    private final String studentName;
    private final Runnable backAction;
    private final StudentProfileController studentController;
    private final ProgrammingLanguageController languageController;

    // Form controls
    private TextField nameField;
    private VBox academicStatus;
    private RadioButton employed, unemployed;
    private TextField jobDetails;
    private VBox languageMenu, knownDatabases, preferredRole;
    private CheckBox whitelist, blacklist;
    private Label errorLabel;

    public EditStudentProfileView(Stage stage, String studentName, Runnable backAction) {
        this.stage = stage;
        this.studentName = studentName;
        this.backAction = backAction;
        this.studentController = new StudentProfileController();
        this.languageController = new ProgrammingLanguageController();
    }

    public void view() {
        Label title = UICreator.titleLabel("Edit Student Profile", 30);
        VBox leftCol = UICreator.createFormColumn("");
        double p = 0.9;

        // Name field
        nameField = UICreator.textField("Full Name", leftCol, p);
        nameField.setText(studentController.getStudentFullName(studentName));

        // Academic Status
        academicStatus = UICreator.createMultiSelectComponent("Academic Status", AppUtils.academicStatus, 3, leftCol,
                p);
        String currentAcademicStatus = studentController.getStudentAcademicStatus(studentName);
        if (currentAcademicStatus != null) {
            setMultiSelectValues(academicStatus, Set.of(currentAcademicStatus));
        }

        // Employment status
        HBox employmentBox = UICreator.createEmploymentRadioBox();
        employed = (RadioButton) employmentBox.getChildren().get(0);
        unemployed = (RadioButton) employmentBox.getChildren().get(1);
        if (studentController.isStudentEmployed(studentName)) {
            employed.setSelected(true);
        } else {
            unemployed.setSelected(true);
        }

        // Programming Languages
        languageMenu = UICreator.createMultiSelectComponent("Known Programming Languages",
                languageController.getAllLanguages(), 3, leftCol, 0.9);
        List<String> currentLanguages = studentController.getStudentProgrammingLanguages(studentName);
        if (currentLanguages != null) {
            setMultiSelectValues(languageMenu, new HashSet<>(currentLanguages));
        }

        leftCol.getChildren().addAll(nameField, academicStatus, employmentBox, languageMenu);
        VBox rightCol = UICreator.createFormColumn("");

        // Job Details
        jobDetails = UICreator.textField("Job Details", rightCol, 0.9);
        String currentJob = studentController.getStudentJobDetails(studentName);
        jobDetails.setText(currentJob != null ? currentJob : "");

        // Known Databases
        knownDatabases = UICreator.createMultiSelectComponent("Known Databases",
                List.of("MySQL", "Postgres", "MongoDB"), 3, rightCol, 0.9);
        List<String> currentDatabases = studentController.getStudentDatabasesKnown(studentName);
        if (currentDatabases != null) {
            setMultiSelectValues(knownDatabases, new HashSet<>(currentDatabases));
        }

        // Preferred Role
        preferredRole = UICreator.createMultiSelectComponent("Preferred Professional Role", AppUtils.profRole, 3,
                rightCol, 0.9);
        String currentRole = studentController.getStudentPreferredRole(studentName);
        if (currentRole != null) {
            setMultiSelectValues(preferredRole, Set.of(currentRole));
        }

        // Whitelist/Blacklist
        HBox whitelistBlacklistBox = UICreator.createWhitelistBlacklistBox();
        whitelist = (CheckBox) whitelistBlacklistBox.getChildren().get(0);
        blacklist = (CheckBox) whitelistBlacklistBox.getChildren().get(1);
        whitelist.setSelected(studentController.isStudentWhitelisted(studentName));
        blacklist.setSelected(studentController.isStudentBlacklisted(studentName));

        rightCol.getChildren().addAll(jobDetails, knownDatabases, preferredRole, whitelistBlacklistBox);

        // Layout and buttons
        HBox columns = UICreator.createTwoColumnLayout(leftCol, rightCol, 0.5);
        columns.spacingProperty().bind(columns.widthProperty().multiply(0.04));
        errorLabel = UICreator.createErrorLabel();

        // Create buttons
        HBox buttonBox = UICreator.createNavigationButtonBox();
        Button saveBtn = UICreator.button("Save", buttonBox, 0.3);
        Button backBtn = UICreator.button("Back", buttonBox, 0.3);
        Button homeBtn = UICreator.button("Home Page", buttonBox, 0.3);

        buttonBox.getChildren().addAll(saveBtn, backBtn, homeBtn);
        buttonBox.maxWidthProperty().bind(columns.widthProperty());

        saveBtn.setOnAction(e -> saveStudent());
        backBtn.setOnAction(e -> {
            if (backAction != null)
                backAction.run();
        });
        homeBtn.setOnAction(e -> new HomePageView(stage).view());

        // Create layout
        VBox topSection = UICreator.createFormColumn("");
        topSection.getChildren().addAll(title, columns, errorLabel);

        VBox bottomSection = UICreator.createFormColumn(AppUtils.COLOR_BG_REGION);
        bottomSection.getChildren().add(buttonBox);

        VBox.setVgrow(topSection, Priority.ALWAYS);
        VBox.setVgrow(bottomSection, Priority.ALWAYS);

        VBox root = UICreator.createStyledVBox(topSection, bottomSection);
        Runnable applyResponsiveLayout = UICreator.layoutBuilder(root, topSection, bottomSection, leftCol, rightCol);
        UICreator.createStandardScene(root, "Edit Student Profile", stage, applyResponsiveLayout);
        Platform.runLater(applyResponsiveLayout);
    }

    private void saveStudent() {
        // Collect form data
        String name = nameField.getText().trim();
        Set<String> academicStatusValues = UICreator.getMultiSelectValues(academicStatus);
        String academicStatusValue = academicStatusValues.isEmpty() ? null : academicStatusValues.iterator().next();
        boolean employmentStatus = employed.isSelected();
        String jobDetailsText = jobDetails.getText().trim();
        Set<String> languageValues = UICreator.getMultiSelectValues(languageMenu);
        Set<String> databaseValues = UICreator.getMultiSelectValues(knownDatabases);
        Set<String> roleValues = UICreator.getMultiSelectValues(preferredRole);
        String preferredRoleValue = roleValues.isEmpty() ? null : roleValues.iterator().next();

        // Validate
        String validationError = studentController.validateStudentProfile(name, academicStatusValue,
            databaseValues, preferredRoleValue, languageValues, employed.isSelected(), unemployed.isSelected(), jobDetailsText, true);

        if (validationError != null) {
            errorLabel.setText(validationError);
            errorLabel.setVisible(true);
            return;
        }

        // Update student using controller method
        studentController.updateStudentByName(studentName, name, academicStatusValue,
                employmentStatus,
                jobDetailsText.isEmpty() ? null : jobDetailsText,
                languageValues.isEmpty() ? null : new ArrayList<>(languageValues),
                databaseValues.isEmpty() ? null : new ArrayList<>(databaseValues),
                preferredRoleValue, whitelist.isSelected(), blacklist.isSelected());

        if (backAction != null)
            backAction.run();
    }

    private void setMultiSelectValues(VBox container, Set<String> values) {
        if (container.getChildren().isEmpty() || values == null)
            return;

        StackPane buttonContainer = (StackPane) container.getChildren().getFirst();
        Button mainButton = (Button) buttonContainer.getChildren().getFirst();

        @SuppressWarnings("unchecked")
        Set<String> selectedItems = (Set<String>) mainButton.getUserData();
        if (selectedItems != null) {
            selectedItems.clear();
            selectedItems.addAll(values);

            if (selectedItems.isEmpty()) {
                mainButton.setText("Select option(s)");
            } else {
                String text = selectedItems.size() <= 3 ? String.join(", ", selectedItems)
                        : selectedItems.size() + " items selected";
                mainButton.setText(text);
            }
        }
    }
}
