package cs151.view;

import cs151.controller.StudentProfileController;
import cs151.util.UICreator;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class SearchResultView {
    private final Stage stage;
    private final List<String> searchResults;

    public SearchResultView(Stage stage, List<String> searchResults) {
        this.stage = stage;
        this.searchResults = searchResults;
    }

    public void view() {
        HBox editRow = UICreator.createNavigationButtonBox();
        HBox navRow = UICreator.createNavigationButtonBox();
        double p = 0.18;

        TableColumn<String, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()));

        TableView<String> table = UICreator.tableView(searchResults, nameCol);

        if (searchResults.isEmpty()) {
            table.setPlaceholder(new Label("No students with this search criteria"));
        }

        Button deleteBtn = UICreator.button("Delete", editRow, p);
        Button editBtn = UICreator.button("Edit", editRow, p);
        Button addCommentBtn = UICreator.button("Add comment", editRow, p);
        Button allCommentsBtn = UICreator.button("All comments", editRow, p);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean noSelection = (newVal == null);
            deleteBtn.setDisable(noSelection);
            editBtn.setDisable(noSelection);
            addCommentBtn.setDisable(noSelection);
            allCommentsBtn.setDisable(noSelection);
        });

        StudentProfileController controller = new StudentProfileController();

        deleteBtn.setOnAction(e -> {
            String selectedName = table.getSelectionModel().getSelectedItem();
            if (selectedName != null) {
                try {
                    controller.deleteStudent(selectedName);
                    table.getItems().remove(selectedName);
                } catch (Exception ex) {
                    System.err.println("Error deleting student from search results: " + ex.getMessage());
                }
            }
        });

        editBtn.setOnAction(e -> {
            String selectedName = table.getSelectionModel().getSelectedItem();
            if (selectedName != null) {
                new EditStudentProfileView(stage, selectedName, () -> new SearchResultView(stage, searchResults).view())
                        .view();
            }
        });

        addCommentBtn.setOnAction(e -> {
            String selectedName = table.getSelectionModel().getSelectedItem();
            if (selectedName != null) {
                new AddCommentView(stage, selectedName, () -> new SearchResultView(stage, searchResults).view()).view();
            }
        });

        allCommentsBtn.setOnAction(e -> {
            String selectedName = table.getSelectionModel().getSelectedItem();
            if (selectedName != null) {
                new AllCommentsView(stage, selectedName,
                        () -> new SearchResultView(stage, searchResults).view()).view();
            }
        });

        table.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                String selectedName = table.getSelectionModel().getSelectedItem();
                if (selectedName != null) {
                    new OneStudentProfileView(stage, selectedName).view();
                }
            }
        });

        String titleText = "Search Results (" + searchResults.size() + " found)";
        Label title = UICreator.titleLabel(titleText, 28);

        Button homeBtn = UICreator.button("Home Page", navRow, 0.8);
        homeBtn.setOnAction(e -> new HomePageView(stage).view());

        navRow.getChildren().addAll(homeBtn);
        editRow.getChildren().addAll(editBtn, addCommentBtn, deleteBtn, allCommentsBtn);

        VBox root = UICreator.createStyledVBox(title, table, editRow, navRow);
        Runnable applyResponsiveLayout = UICreator.layoutBuilder(root);
        Scene scene = UICreator.createStandardScene(root, "Search Results", stage, applyResponsiveLayout);

        UICreator.addDeselectHandler(scene, table);
    }
}
