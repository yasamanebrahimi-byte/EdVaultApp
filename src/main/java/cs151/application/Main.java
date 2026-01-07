package cs151.application;

import javafx.application.Application;
import javafx.stage.Stage;
import cs151.view.HomePageView;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        HomePageView homePageView = new HomePageView(stage);
        homePageView.view();
    }
}
