package cs151.util;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Popup;
import javafx.stage.Stage;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class UICreator {

    /**
     * Creates a styled button with responsive width binding
     * 
     * @param name         the text to display on the button
     * @param parent       the parent region to bind width to
     * @param widthPercent the percentage of parent width to use (0.0-1.0)
     * @param fonts        the font size
     * @return a styled Button with hover effects and responsive width
     */
    public static Button button(String name, Region parent, double widthPercent, int... fonts) {
        Button button = new Button(name);
        StringBuilder styleMain = new StringBuilder(AppUtils.button(AppUtils.COLOR_BG));
        StringBuilder styleHover = new StringBuilder(AppUtils.button(AppUtils.COLOR_BG_HOVER));
        if (fonts.length > 0) {
            styleMain.append(AppUtils.fontSize(fonts[0]));
            styleHover.append(AppUtils.fontSize(fonts[0]));
        }
        button.setStyle(styleMain.toString());
        button.setMaxWidth(Double.MAX_VALUE);
        button.setMinHeight(AppUtils.BUTTON_HEIGHT);
        button.setPrefHeight(AppUtils.BUTTON_HEIGHT);
        bindRegionWidthToParent(button, parent, widthPercent < 0.15 ? AppUtils.WIDTH_PERCENT : widthPercent);
        button.setAlignment(Pos.CENTER);
        button.setOnMouseEntered(e -> button.setStyle(styleHover.toString()));
        button.setOnMouseExited(e -> button.setStyle(styleMain.toString()));
        return button;
    }

    /**
     * Creates a layout builder that dynamically adjusts padding and spacing
     * 
     * @param box   the main container pane to apply layout to
     * @param boxes optional VBox containers to apply spacing and padding to
     * @return a Runnable that can be executed to apply responsive layout
     */
    public static Runnable layoutBuilder(Pane box, VBox... boxes) {
        return () -> {
            double w = box.getWidth();
            double h = box.getHeight();

            double padding = Math.max(w * 0.02, 16);
            box.setPadding(new Insets(padding));
            if (boxes.length >= 2) {
                double spacing = Math.max(h * 0.025, 18.0);
                boxes[0].setSpacing(spacing);
                boxes[1].setSpacing(spacing);
                boxes[0].setPadding(new Insets(0, padding, 0, padding));
                boxes[1].setPadding(new Insets(0, padding, 0, padding));
            }
        };
    }

    /**
     * Creates a styled error label for displaying error messages
     * 
     * @return a Label configured for error display with red text and word wrapping
     */
    public static Label createErrorLabel() {
        Label errorLabel = new Label("");
        errorLabel.setTextFill(Color.web(AppUtils.COLOR_TEXT_RED));
        errorLabel.setStyle(String.format("-fx-font-size: %dpx; -fx-font-family: '%s'; -fx-text-fill: %s;",
                16, AppUtils.FONT_FAMILY, AppUtils.COLOR_TEXT_RED));
        errorLabel.setVisible(false);
        errorLabel.setWrapText(true);
        return errorLabel;
    }

    /**
     * Creates a styled title label
     * 
     * @param name     the text to display in the title
     * @param fontSize the font size for the title text
     * @return a Label with bold font styling and word wrapping
     */
    public static Label titleLabel(String name, double fontSize) {
        Label title = new Label(name);
        title.setFont(Font.font(AppUtils.FONT_FAMILY, FontWeight.BOLD, fontSize));
        title.setStyle(AppUtils.text());
        title.setWrapText(true);
        return title;
    }

    /**
     * Creates a styled text field with responsive width
     * 
     * @param prompt       the placeholder text to display when field is empty
     * @param parent       the parent region to bind width to
     * @param widthPercent the percentage of parent width to use (0.0-1.0)
     * @return a styled TextField with focus handling and responsive width
     */
    public static TextField textField(String prompt, Region parent, double widthPercent) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle(AppUtils.textInput());
        field.setMaxWidth(Double.MAX_VALUE);
        field.setEditable(true);
        field.setFocusTraversable(true);
        if (parent != null)
            bindRegionWidthToParent(field, parent, widthPercent < 0.15 ? AppUtils.WIDTH_PERCENT : widthPercent);
        return field;
    }

    /**
     * Creates a styled VBox container
     * 
     * @param children  the child nodes to add to the VBox
     * @return a styled VBox with padding and background styling
     */
    public static VBox createStyledVBox(Node... children) {
        VBox box = new VBox(16, children);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(12));
        box.setStyle(AppUtils.bg(AppUtils.COLOR_BG));
        return box;
    }

    /**
     * Creates a styled RadioButton
     * 
     * @param name the text label for the radio button
     * @return a styled RadioButton with consistent font and height
     */
    public static RadioButton radioButton(String name) {
        RadioButton button = new RadioButton(name);
        button.setFont(Font.font(AppUtils.FONT_FAMILY, FontWeight.NORMAL, AppUtils.DEFAULT_FONT_SIZE));
        button.setPrefHeight(AppUtils.BUTTON_HEIGHT);
        button.setStyle(AppUtils.radioButton());
        return button;
    }

    /**
     * Creates a styled TableView with configurable columns
     * 
     * @param <T>     the type of items in the list
     * @param items   the list of items to display
     * @param columns the columns to add to the table (if no columns provided,
     *                creates a default single column)
     * @return a styled TableView with the provided items and columns
     */
    @SafeVarargs
    public static <T> TableView<T> tableView(List<T> items, TableColumn<T, ?>... columns) {
        TableView<T> table = new TableView<>();
        styleTableView(table);

        if (columns.length == 0) {
            // Default single column for simple cases (like programming languages)
            TableColumn<T, String> defaultColumn = new TableColumn<>("Programming Languages");
            defaultColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toString()));
            defaultColumn.setPrefWidth(500);
            table.getColumns().add(defaultColumn);
        } else {
            // Add provided columns
            for (TableColumn<T, ?> column : columns) {
                table.getColumns().add(column);
            }
        }

        if (items != null) {
            table.getItems().setAll(items);
        }

        table.setFocusTraversable(false);
        table.setStyle(AppUtils.tableView());
        return table;
    }

    /**
     * Creates a styled TextArea with responsive width binding
     * 
     * @param prompt       the placeholder text to display when area is empty
     * @param parent       the parent region to bind width to
     * @param widthPercent the percentage of parent width to use (0.0-1.0)
     * @param height       the fixed height in pixels
     * @return a styled TextArea with word wrapping and responsive width
     */
    public static TextArea textArea(String prompt, Region parent, double widthPercent, int height) {
        TextArea text = new TextArea();
        text.setPromptText(prompt);
        text.setStyle(AppUtils.textInput());
        text.setWrapText(true);
        text.setMaxWidth(Double.MAX_VALUE);
        bindRegionWidthToParent(text, parent, widthPercent < 0.15 ? AppUtils.WIDTH_PERCENT : widthPercent);
        text.setMinHeight(height);
        text.setPrefHeight(height);
        text.setEditable(true);
        text.setFocusTraversable(true);
        text.setOnMouseClicked(e -> text.requestFocus());
        return text;
    }

    /**
     * Creates a styled CheckBox with selection state handling
     * 
     * @param name the text label for the checkbox
     * @return a styled CheckBox with consistent font and height
     */
    public static CheckBox checkBox(String name) {
        CheckBox checkBox = new CheckBox(name);
        checkBox.setFont(Font.font(AppUtils.FONT_FAMILY, FontWeight.NORMAL, AppUtils.DEFAULT_FONT_SIZE));
        checkBox.setStyle(AppUtils.text());
        checkBox.setPrefHeight(AppUtils.BUTTON_HEIGHT);
        checkBox.setMinHeight(AppUtils.BUTTON_HEIGHT);
        return checkBox;
    }

    /**
     * Creates a standard scene with specified dimensions and title
     * 
     * @param root  the root node for the scene
     * @param title the window title to display
     * @param stage the stage to set the scene on
     * @param r     the responsive layout runnable to execute on size changes
     * @return the created Scene object
     */
    public static Scene createStandardScene(Region root, String title, Stage stage, Runnable r) {
        Scene scene = new Scene(root, AppUtils.APP_WIDTH, AppUtils.APP_HEIGHT);
        root.widthProperty().addListener((a, b, c) -> r.run());
        root.heightProperty().addListener((a, b, c) -> r.run());
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
        return scene;
    }

    /**
     * Creates an HBox container for navigation buttons
     * 
     * @return an HBox with centered alignment and consistent spacing and padding
     */
    public static HBox createNavigationButtonBox() {
        HBox navBox = new HBox(24);
        navBox.setAlignment(Pos.CENTER);
        navBox.setPadding(new Insets(24, 0, 0, 0));
        return navBox;
    }

    /**
     * Creates a form column container
     * 
     * @param color    the background color, or empty string for default
     * @return a VBox configured as a form column with alignment and background
     */
    public static VBox createFormColumn(String color) {
        VBox column = new VBox(12);
        column.setAlignment(Pos.CENTER);
        column.setStyle(AppUtils.bg(ValidationUtils.isStringEmpty(color) ? AppUtils.COLOR_BG : color));
        return column;
    }

    /**
     * Creates a two-column layout with responsive width binding
     * 
     * @param leftCol  the VBox for the left column
     * @param rightCol the VBox for the right column
     * @return an HBox with both columns configured for equal width distribution
     */
    public static HBox createTwoColumnLayout(VBox leftCol, VBox rightCol, double ratio) {
        HBox columns = new HBox(leftCol, rightCol);
        HBox.setHgrow(leftCol, Priority.ALWAYS);
        HBox.setHgrow(rightCol, Priority.ALWAYS);
        if (ratio >= 1 || ratio <= 0)
            ratio = 0.5;
        bindRegionWidthToParent(leftCol, columns, 1 - ratio);
        bindRegionWidthToParent(rightCol, columns, ratio);
        columns.setPadding(new Insets(12));
        return columns;
    }

    /**
     * Creates a radio button group for employment status selection
     * 
     * @return an HBox containing mutually exclusive radio buttons for employment
     */
    public static HBox createEmploymentRadioBox() {
        RadioButton employed = radioButton("Employed");
        RadioButton unemployed = radioButton("Unemployed");
        ToggleGroup group = new ToggleGroup();
        employed.setToggleGroup(group);
        unemployed.setToggleGroup(group);
        HBox employmentBox = new HBox(10, employed, unemployed);
        employmentBox.setAlignment(Pos.CENTER);
        return employmentBox;
    }

    /**
     * Creates mutually exclusive checkboxes for whitelist/blacklist selection
     * 
     * @return an HBox containing "Whitelist" and "Blacklist" checkboxes
     */
    public static HBox createWhitelistBlacklistBox() {
        CheckBox whitelist = checkBox("Whitelist");
        CheckBox blacklist = checkBox("Blacklist");
        whitelist.setAlignment(Pos.CENTER);
        blacklist.setAlignment(Pos.CENTER);
        whitelist.selectedProperty().addListener((obs, w, isSelected) -> {
            if (isSelected)
                blacklist.setSelected(false);
        });
        blacklist.selectedProperty().addListener((obs, w, isSelected) -> {
            if (isSelected)
                whitelist.setSelected(false);
        });
        HBox listBox = new HBox(12, whitelist, blacklist);
        listBox.setAlignment(Pos.CENTER);
        listBox.setPrefHeight(AppUtils.DEFAULT_BUTTON_HEIGHT);
        listBox.setMinHeight(AppUtils.DEFAULT_BUTTON_HEIGHT);
        return listBox;
    }

    /**
     * Creates a basic scroll pane with a VBox content
     * 
     * @param box the VBox content to make scrollable
     * @return a ScrollPane with vertical scrolling and width fitting enabled
     */
    public static ScrollPane basicScrollPane(VBox box) {
        ScrollPane scrollPane = new ScrollPane(box);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setStyle(AppUtils.bg(AppUtils.COLOR_BG));
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        return scrollPane;
    }

    /**
     * Creates a simplified multi-select dropdown component
     * 
     * @param prompt          the text to display when no items are selected
     * @param availableItems  the list of items that can be selected
     * @param maxDisplayItems the maximum number of items to display in button text
     * @return a VBox container with a dropdown button for multi-selection
     */
    public static VBox createMultiSelectComponent(String prompt, List<String> availableItems, int maxDisplayItems,
            VBox parent, double widthPercent) {
        VBox mainContainer = new VBox();
        mainContainer.setAlignment(Pos.TOP_LEFT);

        Button mainButton = button(prompt, parent, widthPercent);
        Label arrowLabel = new Label(AppUtils.ARROW_DOWN);
        arrowLabel.setStyle(AppUtils.baseStyle());
        arrowLabel.setTranslateX(-25);

        StackPane buttonContainer = new StackPane(mainButton, arrowLabel);
        StackPane.setAlignment(arrowLabel, Pos.CENTER_RIGHT);

        Popup popup = new Popup();
        popup.setAutoHide(true);
        VBox dropdownContent = new VBox(0);
        ScrollPane scrollPane = basicScrollPane(dropdownContent);
        scrollPane.setMaxHeight(AppUtils.APP_HEIGHT * 0.5);
        scrollPane.setStyle(AppUtils.bg(AppUtils.COLOR_BG) + AppUtils.borderAndRadius());
        popup.getContent().add(scrollPane);

        Set<String> selectedItems = new HashSet<>();
        Map<String, Label> itemLabels = new HashMap<>();
        boolean[] isOpen = { false };

        Runnable updateText = () -> {
            if (ValidationUtils.isCollectionEmpty(selectedItems)) {
                mainButton.setText(prompt);
                mainButton.setStyle(AppUtils.button(AppUtils.COLOR_BG));
            } else {
                String text = selectedItems.size() <= maxDisplayItems ? String.join(", ", selectedItems)
                        : selectedItems.size() + " items selected";
                mainButton.setText(text);
            }
        };

        String unselected = AppUtils.dropdownItem(AppUtils.COLOR_BG);
        String selected = AppUtils.dropdownItem(AppUtils.COLOR_BG_HOVER);

        for (String item : availableItems) {
            Label label = new Label(item);
            label.setMaxWidth(Double.MAX_VALUE);
            label.setPadding(new Insets(AppUtils.PADDING));
            label.setStyle(unselected);
            itemLabels.put(item, label);

            label.setOnMouseEntered(e -> {
                if (!selectedItems.contains(item))
                    label.setStyle(selected);
            });
            label.setOnMouseExited(e -> label.setStyle(selectedItems.contains(item) ? selected : unselected));

            label.setOnMouseClicked(e -> {
                if (maxDisplayItems == 1) {
                    selectedItems.forEach(s -> itemLabels.get(s).setStyle(unselected));
                    selectedItems.clear();
                    selectedItems.add(item);
                    label.setStyle(selected);
                } else if (selectedItems.contains(item)) {
                    selectedItems.remove(item);
                    label.setStyle(unselected);
                }  else {
                    selectedItems.add(item);
                    label.setStyle(selected);
                }
                updateText.run();
            });
            dropdownContent.getChildren().add(label);
        }

        mainButton.setOnAction(e -> {
            if (isOpen[0]) {
                popup.hide();
                arrowLabel.setText(AppUtils.ARROW_DOWN);
                isOpen[0] = false;
            } else {
                Bounds bounds = buttonContainer.localToScreen(buttonContainer.getBoundsInLocal());
                if (bounds != null) {
                    scrollPane.setPrefWidth(bounds.getWidth());
                    popup.show(buttonContainer, bounds.getMinX(), bounds.getMaxY());
                }
                arrowLabel.setText(AppUtils.ARROW_UP);
                isOpen[0] = true;
            }
        });

        popup.setOnAutoHide(e -> {
            arrowLabel.setText(AppUtils.ARROW_DOWN);
            isOpen[0] = false;
        });

        mainContainer.getChildren().add(buttonContainer);
        mainButton.setUserData(selectedItems);
        return mainContainer;
    }

    /**
     * Retrieves the selected values from a multi-select component
     * 
     * @param multiSelectContainer the VBox container
     * @return a Set of selected item strings
     */
    @SuppressWarnings("unchecked")
    public static Set<String> getMultiSelectValues(VBox multiSelectContainer) {
        if (multiSelectContainer.getChildren().isEmpty()) {
            return new HashSet<>();
        }
        StackPane buttonContainer = (StackPane) multiSelectContainer.getChildren().getFirst();
        Button mainButton = (Button) buttonContainer.getChildren().getFirst();
        Object userData = mainButton.getUserData();
        return userData instanceof Set ? new HashSet<>((Set<String>) userData) : new HashSet<>();
    }

    /**
     * Gets the selected value from a menu button
     * 
     * @param menuButtonContainer the VBox container created by menuButton
     * @return the selected item string
     */
    public static String getMenuButtonValue(VBox menuButtonContainer) {
        Set<String> values = getMultiSelectValues(menuButtonContainer);
        return values.isEmpty() ? null : values.iterator().next();
    }

    /**
     * Binds region's width to parent width
     * 
     * @param region       the region to bind the width for
     * @param parent       the parent region to bind to, must not be null
     * @param widthPercent the percentage of parent width (0.0-1.0)
     */
    public static void bindRegionWidthToParent(Region region, Region parent, double widthPercent) {
        if (parent != null && widthPercent >= 0.0 && widthPercent <= 1.0) {
            region.prefWidthProperty().bind(parent.widthProperty().multiply(widthPercent));
            region.maxWidthProperty().bind(parent.widthProperty().multiply(widthPercent));
        }
    }

    /**
     * Shows an error message in the specified error label
     * 
     * @param errorLabel the label to display the error message in
     * @param message    the error message text to display
     */
    public static void showError(Label errorLabel, String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    /**
     * Hides the error label
     * 
     * @param errorLabel the label to hide
     */
    public static void hideError(Label errorLabel) {
        errorLabel.setVisible(false);
    }

    /**
     * Applies styling to TableView components
     * 
     * @param table the TableView to apply styling to
     */
    public static void styleTableView(TableView<?> table) {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setMaxHeight(300);
        table.setStyle(AppUtils.tableView());
    }

    /**
     * Adds a click handler to deselect table rows when clicking outside the table
     * 
     * @param scene the scene to add the click handler to
     * @param table the table to deselect when clicking outside
     */
    public static void addDeselectHandler(Scene scene, TableView<?> table) {
        scene.setOnMouseClicked(event -> {
            Node current = (Node) event.getTarget();
            boolean clickedInTable = false;
            while (current != null) {
                if (current == table) {
                    clickedInTable = true;
                    break;
                }
                current = current.getParent();
            }
            if (!clickedInTable) {
                table.getSelectionModel().clearSelection();
            }
        });
    }
}
