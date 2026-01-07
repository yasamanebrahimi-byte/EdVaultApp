package cs151.view;

import cs151.controller.CommentController;
import cs151.util.UICreator;
import cs151.util.AppUtils;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;

public class AllCommentsView {
    private final Stage stage;
    private final String studentName;
    private final Runnable backAction;
    private final CommentController commentController;

    public AllCommentsView(Stage stage, String studentName, Runnable backAction) {
        this.stage = stage;
        this.studentName = studentName;
        this.backAction = backAction;
        this.commentController = new CommentController();
    }

    public void view() {
        Label title = UICreator.titleLabel("Student Details", 28);
        VBox contentBox = createStudentDetailsBox();
        ScrollPane scrollPane = UICreator.basicScrollPane(contentBox);
        scrollPane.setPrefViewportHeight(500);
        scrollPane.setPrefViewportWidth(640);

        HBox buttonBox = createNavigationButtons();
        VBox root = UICreator.createStyledVBox(title, scrollPane, buttonBox);
        Runnable applyResponsiveLayout = UICreator.layoutBuilder(root);
        UICreator.createStandardScene(root, "Student Details", stage, applyResponsiveLayout);
    }

    private VBox createStudentDetailsBox() {
        VBox contentBox = new VBox(12);

        // Add comments
        List<String> commentTexts = commentController.getStudentCommentTexts(studentName);
        List<String> commentDates = commentController.getStudentCommentDates(studentName);

        if (commentTexts != null && !commentTexts.isEmpty()) {
            contentBox.getChildren().add(createFieldLabel());
            for (int i = 0; i < commentTexts.size(); i++) {
                String date = (i < commentDates.size()) ? commentDates.get(i) : "";
                String commentWithDate = safe(commentTexts.get(i)) + (date.isEmpty() ? "" : " (" + date + ")");
                contentBox.getChildren().add(createCommentLabel(commentWithDate));
            }
        }

        return contentBox;
    }

    private Label createFieldLabel() {
        Label label = new Label("Comments: ");
        label.setFont(Font.font(AppUtils.FONT_FAMILY, FontWeight.NORMAL, AppUtils.DEFAULT_FONT_SIZE));
        label.setWrapText(true);
        return label;
    }

    private Label createCommentLabel(String comment) {
        Label label = new Label("• " + comment);
        label.setFont(Font.font(AppUtils.FONT_FAMILY, FontWeight.NORMAL, AppUtils.DEFAULT_FONT_SIZE));
        label.setWrapText(true);
        label.setStyle("-fx-padding: 0 0 0 20;");
        return label;
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

    private HBox createNavigationButtons() {
        HBox box = UICreator.createNavigationButtonBox();
        Button backBtn = UICreator.button("Back", box, 0.3);
        backBtn.setOnAction(e -> {
            if (backAction != null)
                backAction.run();
        });
        Button homeBtn = UICreator.button("Home Page", box, 0.3);
        homeBtn.setOnAction(e -> new HomePageView(stage).view());
        box.getChildren().addAll(backBtn, homeBtn);
        return box;
    }
}
