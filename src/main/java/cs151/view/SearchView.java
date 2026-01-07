package cs151.view;

import cs151.controller.SearchController;
import cs151.util.UICreator;
import javafx.application.Platform;
import cs151.util.AppUtils;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;

public class SearchView {
    private final Stage stage;
    private final SearchController searchController = new SearchController();

    public SearchView(Stage stage) {
        this.stage = stage;
    }

    public void view() {
        Label title = UICreator.titleLabel("Search Student", 30);

        VBox topSection = UICreator.createFormColumn("");
        TextArea searchArea = UICreator.textArea("Search term", topSection, 0.8, 130);

        VBox searchBox = UICreator.createFormColumn("");
        searchBox.getChildren().addAll(searchArea);
        topSection.getChildren().addAll(title, searchBox);

        HBox buttonBox = UICreator.createNavigationButtonBox();
        double p = 0.5;
        Button searchBtn = UICreator.button("Search", buttonBox, p);
        Button homeBtn = UICreator.button("Home Page", buttonBox, p);
        buttonBox.getChildren().addAll(searchBtn, homeBtn);

        HBox.setHgrow(searchBtn, Priority.ALWAYS);
        HBox.setHgrow(homeBtn, Priority.ALWAYS);

        VBox bottomSection = UICreator.createFormColumn(AppUtils.COLOR_BG_REGION);
        bottomSection.getChildren().add(buttonBox);

        VBox root = UICreator.createStyledVBox(topSection, bottomSection);
        VBox.setVgrow(topSection, Priority.ALWAYS);
        VBox.setVgrow(bottomSection, Priority.ALWAYS);

        Runnable applyResponsiveLayout = UICreator.layoutBuilder(root, topSection, bottomSection);

        UICreator.createStandardScene(root, "Search Student", stage, applyResponsiveLayout);
        Platform.runLater(applyResponsiveLayout);

        homeBtn.setOnAction(e -> new HomePageView(stage).view());
        searchBtn.setOnAction(e -> {
            String searchTerm = searchArea.getText();
            List<String> results = searchController.globalSearch(searchTerm);
            new SearchResultView(stage, results).view();
        });
    }
}
