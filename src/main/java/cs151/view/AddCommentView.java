package cs151.view;

import cs151.controller.CommentController;
import cs151.util.AppUtils;
import cs151.util.UICreator;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddCommentView {
    private final Stage stage;
    private final String studentName;
    private final CommentController commentController;
    private final Runnable backAction;

    public AddCommentView(Stage stage, String studentName, Runnable backAction) {
        this.stage = stage;
        this.studentName = studentName;
        this.commentController = new CommentController();
        this.backAction = backAction;
    }

    public void view() {
        String prompt = "Student Name: " + studentName;
        Label title = UICreator.titleLabel(prompt, 28);
        double p = 0.3;

        VBox topSection = UICreator.createFormColumn("");
        TextArea commentBox = UICreator.textArea("Enter new comment.", topSection, 0.8, 120);
        Label messageLabel = UICreator.createErrorLabel();
        topSection.getChildren().addAll(title, commentBox, messageLabel);

        HBox buttonBox = UICreator.createNavigationButtonBox();
        Button submitBtn = UICreator.button("Submit", buttonBox, p);
        Button backBtn = UICreator.button("Back", buttonBox, p);
        Button homeBtn = UICreator.button("Home Page", buttonBox, p);
        homeBtn.setOnAction(e -> new HomePageView(stage).view());
        buttonBox.getChildren().addAll(backBtn, submitBtn, homeBtn);

        HBox.setHgrow(backBtn, Priority.ALWAYS);
        HBox.setHgrow(submitBtn, Priority.ALWAYS);
        HBox.setHgrow(homeBtn, Priority.ALWAYS);

        VBox bottomSection = UICreator.createFormColumn(AppUtils.COLOR_BG_REGION);
        bottomSection.getChildren().add(buttonBox);

        VBox root = UICreator.createStyledVBox(topSection, bottomSection);

        Runnable applyResponsiveLayout = UICreator.layoutBuilder(root, topSection, bottomSection);

        VBox.setVgrow(topSection, Priority.ALWAYS);
        VBox.setVgrow(bottomSection, Priority.ALWAYS);
        submitBtn.setOnAction(e -> {
            String commentText = commentBox.getText().trim();
            if (!commentText.isEmpty()) {
                boolean success = commentController.addCommentToStudent(studentName, commentText);
                if (success) {
                    commentBox.clear();
                    Alert alert = new Alert(
                            Alert.AlertType.INFORMATION, "Comment added successfully!",
                            ButtonType.OK);
                    alert.showAndWait();
                    new OneStudentProfileView(stage, studentName
                    )
                            .view();
                }
            } else {
                UICreator.showError(messageLabel, "Comment cannot be empty.");
            }
        });
        backBtn.setOnAction(e -> backAction.run());
        homeBtn.setOnAction(e -> new HomePageView(stage).view());

        UICreator.createStandardScene(root, "Add Comment", stage, applyResponsiveLayout);
        Platform.runLater(applyResponsiveLayout);
    }
}
