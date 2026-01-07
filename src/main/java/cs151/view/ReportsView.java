package cs151.view;

import cs151.controller.SearchController;
import cs151.util.UICreator;
import javafx.application.Platform;
import cs151.util.AppUtils;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;

public class ReportsView {
    private final Stage stage;
    private final SearchController searchController = new SearchController();

    public ReportsView(Stage stage) {
        this.stage = stage;
    }

    public void view() {
        Label title = UICreator.titleLabel("Whitelist/Blacklist Reports", 30);

        VBox topSection = UICreator.createFormColumn("");

        HBox whitelistBlacklistBox = UICreator.createWhitelistBlacklistBox();
        CheckBox whitelist = (CheckBox) whitelistBlacklistBox.getChildren().get(0);
        CheckBox blacklist = (CheckBox) whitelistBlacklistBox.getChildren().get(1);

        VBox searchBox = UICreator.createFormColumn("");
        searchBox.getChildren().addAll(whitelistBlacklistBox);
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
            String searchTerm = "";
            boolean wSelected = whitelist.isSelected();
            boolean bSelected = blacklist.isSelected();

            if (wSelected && !bSelected) {
                searchTerm = whitelist.getText();
            } else if (bSelected && !wSelected) {
                searchTerm = blacklist.getText();
            } else if (wSelected && bSelected) {
                searchTerm = whitelist.getText() + "," + blacklist.getText();
            }

            List<String> results = searchController.globalSearch(searchTerm);
            new SearchResultView(stage, results).view();
        });
    }
}
