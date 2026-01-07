package cs151.view;

import cs151.controller.CommentController;
import cs151.controller.StudentProfileController;
import cs151.util.UICreator;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Pair;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextArea;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OneStudentProfileView {
    private final Stage stage;
    private final String studentName;
    private final CommentController commentController;
    private final StudentProfileController studentController;

    public OneStudentProfileView(Stage stage, String studentName) {
        this.stage = stage;
        this.studentName = studentName;
        this.commentController = new CommentController();
        this.studentController = new StudentProfileController();
    }

    public void view() {
        Stage popup = new Stage();
        popup.initOwner(stage);
        popup.initModality(Modality.WINDOW_MODAL);

        Label title = UICreator.titleLabel("Student Details", 28);
        VBox contentBox = createStudentDetailsBox(popup);
        ScrollPane scrollPane = UICreator.basicScrollPane(contentBox);
        scrollPane.setPrefViewportHeight(500);
        scrollPane.setPrefViewportWidth(640);

        VBox root = UICreator.createStyledVBox(title, scrollPane);
        Runnable applyResponsiveLayout = UICreator.layoutBuilder(root);

        Scene scene = new Scene(root, 640, 560);
        root.widthProperty().addListener((a, b, c) -> applyResponsiveLayout.run());
        root.heightProperty().addListener((a, b, c) -> applyResponsiveLayout.run());
        popup.setScene(scene);
        popup.setTitle("Student Details");
        popup.showAndWait();
    }

    private VBox createStudentDetailsBox(Stage popupOwner) {
        VBox contentBox = new VBox(12);
        contentBox.setPadding(new Insets(8));
        GridPane form = new GridPane();
        int row = 0;
        for (Map.Entry<String, String> entry : buildFieldMap().entrySet()) {
            Label nameLabel = new Label(entry.getKey() + ":");

            Label valueLabel = new Label(safe(entry.getValue()));

            form.add(nameLabel, 0, row);
            form.add(valueLabel, 1, row);
            row++;
        }

        TableColumn<Pair<String, String>, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKey()));
        dateCol.setMaxWidth(200);
        TableColumn<Pair<String, String>, String> commentCol = new TableColumn<>("Comment");
        commentCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getValue()));
        commentCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(truncate(item));
                }
            }
        });

        List<String> commentTexts = commentController.getStudentCommentTexts(studentName);
        List<String> commentDates = commentController.getStudentCommentDates(studentName);
        ObservableList<Pair<String, String>> rows = FXCollections.observableArrayList();
        if (commentTexts != null) {
            for (int i = 0; i < commentTexts.size(); i++) {
                String date = (commentDates != null && i < commentDates.size()) ? commentDates.get(i) : "";
                rows.add(new Pair<>(date, safe(commentTexts.get(i))));
            }
        }

        TableView<Pair<String, String>> table = UICreator.tableView(rows, dateCol, commentCol);
        table.setPrefHeight(220);

        table.setRowFactory(tv -> {
            TableRow<Pair<String, String>> tableRow = new TableRow<>();
            tableRow.setOnMouseClicked(e -> {
                if (!tableRow.isEmpty()) {
                    Pair<String, String> pair = tableRow.getItem();

                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                            javafx.scene.control.Alert.AlertType.INFORMATION);
                    alert.initOwner(popupOwner != null ? popupOwner : stage);
                    alert.setTitle("Full Comment");
                    alert.setHeaderText(pair.getKey());

                    TextArea ta = new TextArea(pair.getValue());
                    ta.setWrapText(true);
                    ta.setEditable(false);
                    ta.setPrefSize(600, 320);

                    alert.getDialogPane().setContent(ta);
                    alert.showAndWait();
                }
            });
            return tableRow;
        });

        contentBox.getChildren().addAll(form, table);
        return contentBox;
    }

    private String truncate(String s) {
        if (s == null)
            return "";
        if (s.length() <= 120)
            return s;
        return s.substring(0, Math.max(0, 120 - 3)) + "...";
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

    private String yesNo(boolean b) {
        return b ? "Yes" : "No";
    }

    private String join(List<String> items) {
        return items == null ? "" : String.join(", ", items.stream().filter(Objects::nonNull).toList());
    }

    private Map<String, String> buildFieldMap() {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("Full Name", safe(studentController.getStudentFullName(studentName)));
        fields.put("Academic Status", safe(studentController.getStudentAcademicStatus(studentName)));
        fields.put("Employed", yesNo(studentController.isStudentEmployed(studentName)));
        fields.put("Job Details", safe(studentController.getStudentJobDetails(studentName)));
        fields.put("Programming Languages", join(studentController.getStudentProgrammingLanguages(studentName)));
        fields.put("Databases Known", join(studentController.getStudentDatabasesKnown(studentName)));
        fields.put("Preferred Role", safe(studentController.getStudentPreferredRole(studentName)));
        fields.put("Whitelisted", yesNo(studentController.isStudentWhitelisted(studentName)));
        fields.put("Blacklisted", yesNo(studentController.isStudentBlacklisted(studentName)));
        return fields;
    }
}
