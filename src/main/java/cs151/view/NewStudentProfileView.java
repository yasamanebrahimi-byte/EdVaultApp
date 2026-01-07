package cs151.view;

import cs151.controller.StudentProfileController;
import cs151.controller.ProgrammingLanguageController;
import cs151.util.AppUtils;
import cs151.util.UICreator;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NewStudentProfileView {
    private final Stage stage;
    private final StudentProfileController studentController;
    private final ProgrammingLanguageController languageController;

    public NewStudentProfileView(Stage stage) {
        this.stage = stage;
        this.studentController = new StudentProfileController();
        this.languageController = new ProgrammingLanguageController();
    }

    public void view() {
        Label errorLabel = UICreator.createErrorLabel();
        errorLabel.setWrapText(true);
        double p = 0.9;
        Label title = UICreator.titleLabel("Create Student Profile", 30);

        VBox leftCol = UICreator.createFormColumn("");
        TextField name = UICreator.textField("Name", leftCol, p);
        VBox academicStatus = UICreator.createMultiSelectComponent("Academic Status", AppUtils.academicStatus, 1, leftCol, p);

        HBox employmentBox = UICreator.createEmploymentRadioBox();
        RadioButton employed = (RadioButton) employmentBox.getChildren().get(0);
        RadioButton unemployed = (RadioButton) employmentBox.getChildren().get(1);

        TextField jobDetails = UICreator.textField("Job Details", leftCol, p);
        List<String> programmingLanguages = new ArrayList<>(languageController.getAllLanguages());
        VBox knownProgrammingLanguages = UICreator.createMultiSelectComponent("Known Programming Languages",
                programmingLanguages, 3, leftCol, p);

        VBox rightCol = UICreator.createFormColumn("");
        VBox knownDatabases = UICreator.createMultiSelectComponent("Known Databases", AppUtils.databases, 3, leftCol, p);
        VBox preferredRole = UICreator.createMultiSelectComponent("Preferred Professional Role", AppUtils.profRole, 1, rightCol, p);

        HBox whitelistBlacklistBox = UICreator.createWhitelistBlacklistBox();
        CheckBox whitelist = (CheckBox) whitelistBlacklistBox.getChildren().get(0);
        CheckBox blacklist = (CheckBox) whitelistBlacklistBox.getChildren().get(1);

        TextArea comments = UICreator.textArea("Comments", rightCol, p, 160);

        leftCol.getChildren().addAll(name, academicStatus, employmentBox, jobDetails, knownProgrammingLanguages,
                knownDatabases);


        rightCol.getChildren().addAll(preferredRole, whitelistBlacklistBox, comments);

        HBox columns = UICreator.createTwoColumnLayout(leftCol, rightCol, 0.5);

        HBox buttonBox = UICreator.createNavigationButtonBox();
        Button submit = UICreator.button("Submit", buttonBox, 0.45);
        Button homePage = UICreator.button("Home Page", buttonBox, 0.45);
        homePage.setOnAction(e -> new HomePageView(stage).view());
        buttonBox.getChildren().addAll(submit, homePage);

        submit.setOnAction(event -> {
            UICreator.hideError(errorLabel);

            String studentName = name.getText().trim();

            String academicStatusOption = UICreator.getMenuButtonValue(academicStatus);
            Set<String> selectedDatabases = UICreator.getMultiSelectValues(knownDatabases);
            String preferredRoleOption = UICreator.getMenuButtonValue(preferredRole);
            Set<String> selectedLanguages = UICreator.getMultiSelectValues(knownProgrammingLanguages);

            String validationError = studentController.validateStudentProfile(studentName,
                    academicStatusOption, selectedDatabases, preferredRoleOption, selectedLanguages,
                    employed.isSelected(), unemployed.isSelected(), jobDetails.getText(), false);

            if (validationError != null) {
                UICreator.showError(errorLabel, validationError);
                return;
            }

            try {
                boolean isEmployed = employed.isSelected();
                String jobDetailsVal = jobDetails.getText().trim();

                List<String> progLangs = new ArrayList<>(UICreator.getMultiSelectValues(knownProgrammingLanguages));

                // Get all selected databases
                List<String> dbs = new ArrayList<>(UICreator.getMultiSelectValues(knownDatabases));

                String preferredRoleVal = UICreator.getMenuButtonValue(preferredRole);
                boolean isWhitelisted = whitelist.isSelected();
                boolean isBlacklisted = blacklist.isSelected();
                String commentText = comments.getText().trim();

                // Use controller method to create and add student
                studentController.createAndAddStudent(
                        studentName,
                        academicStatusOption,
                        isEmployed,
                        jobDetailsVal.isEmpty() ? null : jobDetailsVal,
                        progLangs.isEmpty() ? null : progLangs,
                        dbs.isEmpty() ? null : dbs,
                        preferredRoleVal,
                        commentText.isEmpty() ? null : commentText,
                        isWhitelisted,
                        isBlacklisted);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Student profile saved!", ButtonType.OK);
                alert.showAndWait();

                new HomePageView(stage).view();
            } catch (Exception ex) {
                System.err.println("Error creating new student profile: " + ex.getMessage());
                UICreator.showError(errorLabel, "Failed to save student profile: " + ex.getMessage());
            }
        });

        VBox formBox = UICreator.createStyledVBox(title, columns, buttonBox, errorLabel);

        Runnable applyResponsiveLayout = UICreator.layoutBuilder(formBox);
        UICreator.createStandardScene(formBox, "StudentProfileView", stage, applyResponsiveLayout);
    }
}
