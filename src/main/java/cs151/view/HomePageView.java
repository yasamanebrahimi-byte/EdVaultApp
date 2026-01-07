package cs151.view;

import cs151.controller.SearchController;
import cs151.util.UICreator;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import cs151.util.AppUtils;

import java.util.List;

public class HomePageView {
    private final Stage stage;
    private final SearchController searchController = new SearchController();

    public HomePageView(Stage stage) {
        this.stage = stage;
    }

    public void view() {
        Label welcome = UICreator.titleLabel("Welcome to", 48);
        Label edVault = UICreator.titleLabel("EdVault", 70);

        var url = getClass().getResource("/images/logo.png");
        ImageView logoView = new ImageView();
        if (url != null) {
            Image logoImage = new Image(url.toExternalForm(), true);
            logoView.setImage(logoImage);
        }

        VBox buttonBox = UICreator.createFormColumn(AppUtils.COLOR_BG_REGION);
        buttonBox.spacingProperty().bind(stage.heightProperty().multiply(0.07));

        double p = 0.8;
        Button programmingLanguages = UICreator.button("Define programming language", buttonBox, p, 16);
        Button allLanguages = UICreator.button("All programming languages", buttonBox, p);

        Button students = UICreator.button("Define student profile", buttonBox, p);
        Button allStudents = UICreator.button("All student profiles", buttonBox, p);
        allStudents.setOnAction(e -> {
            List<String> results = searchController.globalSearch(null);
            new SearchResultView(stage, results).view();
        });

        Button search = UICreator.button("Search Students Profiles", buttonBox, p);
        Button reports = UICreator.button("Reports", buttonBox, p);

        programmingLanguages.setOnAction(event -> new AddProgrammingLanguageView(stage).view());
        students.setOnAction(event -> new NewStudentProfileView(stage).view());
        allLanguages.setOnAction(event -> new AllAddedProgrammingLanguagesView(stage).view());
        search.setOnAction(event -> new SearchView(stage).view());
        reports.setOnAction(event -> new ReportsView(stage).view());

        buttonBox.getChildren().addAll(programmingLanguages, allLanguages, students, allStudents, search, reports);

        VBox titleBox = UICreator.createStyledVBox(welcome, edVault, logoView);

        HBox root = UICreator.createTwoColumnLayout(buttonBox, titleBox, 0.55);
        logoView.setFitWidth(200);
        logoView.setFitHeight(200);

        Runnable applyResponsiveLayout = UICreator.layoutBuilder(root);
        UICreator.createStandardScene(root, "HomePageView", stage, applyResponsiveLayout);
    }
}
