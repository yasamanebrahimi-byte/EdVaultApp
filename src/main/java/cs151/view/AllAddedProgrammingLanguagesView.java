package cs151.view;

import cs151.controller.ProgrammingLanguageController;
import cs151.util.UICreator;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AllAddedProgrammingLanguagesView {
    private final Stage stage;
    private final Runnable backAction;
    private final ProgrammingLanguageController controller = new ProgrammingLanguageController();

    public AllAddedProgrammingLanguagesView(Stage stage) {
        this(stage, () -> new HomePageView(stage).view());
    }

    public AllAddedProgrammingLanguagesView(Stage stage, Runnable backAction) {
        this.stage = stage;
        this.backAction = backAction;
    }

    public void view() {
        TableView<String> tableView = UICreator.tableView(controller.getAllLanguages());
        double p = 0.3;
        HBox buttonBox = UICreator.createNavigationButtonBox();
        Button backBtn = UICreator.button("Back", buttonBox, p);
        backBtn.setOnAction(e -> backAction.run());
        Button homeBtn = UICreator.button("Home Page", buttonBox, p);
        homeBtn.setOnAction(e -> new HomePageView(stage).view());
        buttonBox.getChildren().addAll(backBtn, homeBtn);

        VBox root = UICreator.createStyledVBox(UICreator.titleLabel("All added programming languages", 28), tableView, buttonBox);
        UICreator.createStandardScene(root, "Programming Languages List", stage, UICreator.layoutBuilder(root));
    }
}
