package cs151.view;

import cs151.controller.ProgrammingLanguageController;
import cs151.util.UICreator;
import cs151.util.AppUtils;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddProgrammingLanguageView {
    private final Stage stage;
    private final ProgrammingLanguageController controller;

    public AddProgrammingLanguageView(Stage stage) {
        this.stage = stage;
        this.controller = new ProgrammingLanguageController();
    }

    public void view() {
        final String titleMessage = "Please type the name of the programming language you would like to add below.";
        Label title = UICreator.titleLabel(titleMessage, 25);
        double p = 0.3;

        VBox topSection = UICreator.createFormColumn("");

        HBox languageBox = UICreator.createNavigationButtonBox();
        String prompt = "Programming language name (e.g., Python, Java).";
        TextField language = UICreator.textField(prompt, languageBox, 0.8);
        languageBox.getChildren().add(language);

        Label errorLabel = UICreator.createErrorLabel();
        topSection.getChildren().addAll(title, languageBox, errorLabel);

        HBox buttonBox = UICreator.createNavigationButtonBox();
        Button allLangsBtn = UICreator.button("View All Languages", buttonBox, p);
        Button submitBtn = UICreator.button("Submit", buttonBox, p);
        Button homeBtn = UICreator.button("Home Page", buttonBox, p);
        buttonBox.getChildren().addAll(allLangsBtn, submitBtn, homeBtn);

        VBox bottomSection = UICreator.createFormColumn(AppUtils.COLOR_BG_REGION);
        bottomSection.getChildren().add(buttonBox);

        VBox root = UICreator.createStyledVBox(topSection, bottomSection);
        Runnable applyResponsiveLayout = UICreator.layoutBuilder(root, topSection, bottomSection);

        VBox.setVgrow(topSection, Priority.ALWAYS);
        VBox.setVgrow(bottomSection, Priority.ALWAYS);

        submitBtn.setOnAction(event -> {
            String lang = language.getText().trim();
            String validationError = controller.validateLanguageName(lang);

            if (validationError != null) {
                UICreator.showError(errorLabel, validationError);
            } else {
                boolean success = controller.addLanguage(lang);
                if (success) {
                    language.clear();
                    UICreator.hideError(errorLabel);
                    new AllAddedProgrammingLanguagesView(stage, () -> new AddProgrammingLanguageView(stage).view())
                            .view();
                }
            }
        });

        allLangsBtn.setOnAction(
                e -> new AllAddedProgrammingLanguagesView(stage, () -> new AddProgrammingLanguageView(stage).view())
                        .view());

        homeBtn.setOnAction(e -> new HomePageView(stage).view());
        UICreator.createStandardScene(root, "Add Programming Language", stage, applyResponsiveLayout);
        Platform.runLater(applyResponsiveLayout);
    }
}
