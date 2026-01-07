package cs151.util;

import javafx.stage.Screen;

import java.util.List;

public final class AppUtils {
    public static final List<String> academicStatus =
        List.of("Freshman", "Sophomore", "Junior", "Senior", "Graduate");
    public static final List<String> databases = List.of("MySQL", "Postgres", "MongoDB");
    public static final List<String> profRole = List.of("Back-end", "Full-stack", "Front-End", "Data", "Other");

    // Color constants
    static final String COLOR_TEXT = "#5b6784ff";
    static final String COLOR_TEXT_RED = "#FF4C4C";
    static final String COLOR_BG = "#FFFFFF";
    static final String COLOR_BG_HOVER = "#E6E6E6";
    public static final String COLOR_BG_REGION = "#E4ECFF";
    static final String COLOR_OUTLINE = "#B0B0B0";
    static final String COLOR_PROMPT_TEXT = "#CCCCCC";
    static final String COLOR_TRANSPARENT = "transparent";

    // UI dimensions
    static final double BORDER_WIDTH = 2.0;
    static final double BORDER_RADIUS = 6.0;
    static final double WIDTH_PERCENT = 0.3;
    static final double BUTTON_HEIGHT = 40.0;
    static final double PADDING = 8.0;

    // Screen dimensions
    private static final double SCREEN_WIDTH = Screen.getPrimary().getVisualBounds().getWidth();
    private static final double SCREEN_HEIGHT = Screen.getPrimary().getVisualBounds().getHeight();

    // Responsive dimension
    public static final double DEFAULT_BUTTON_HEIGHT = SCREEN_HEIGHT * 0.057;
    public static final double APP_WIDTH = SCREEN_WIDTH * 0.6;
    public static final double APP_HEIGHT = SCREEN_HEIGHT * 0.75;

    // Text symbols
    static final String ARROW_DOWN = "⏷";
    static final String ARROW_UP = "⏶";

    public static final String FONT_FAMILY = "Segue UI";
    public static final double DEFAULT_FONT_SIZE = 18.0;

    /**
     * Generates the base style string
     * 
     * @return Style string containing font, size, and text-fill
     */
    static String baseStyle() {
        return String.format("-fx-font-family:'%s';-fx-font-size:%.0fpx;-fx-text-fill:%s;",
                FONT_FAMILY, DEFAULT_FONT_SIZE, COLOR_TEXT);
    }

    /**
     * Generates a background-color property string.
     * 
     * @param color the background color
     * @return Style string for background-color property
     */
    static String bg(String color) {
        return "-fx-background-color:" + color + ";";
    }

    /**
     * Generates a text fill property string.
     * 
     * @return Style string for text-fill property
     */
    static String text() {
        return "-fx-text-fill:" + COLOR_TEXT + ";";
    }

    /**
     * Generates properties for border styling and radius.
     *
     * @return Style string containing border and radius property
     */
    static String borderAndRadius() {
        return String.format(
                "-fx-border-color:%s;-fx-border-width:%.0fpx;-fx-background-radius:%.0f;-fx-border-radius:%.0f;",
                COLOR_OUTLINE, BORDER_WIDTH, BORDER_RADIUS, BORDER_RADIUS);
    }

    /**
     * Generates style string to change the default focus.
     *
     * @return Style string to change default focus.
     */
    static String focusTransparent() {
        return "-fx-focus-color:transparent;-fx-faint-focus-color:transparent;";
    }

    /**
     * Generates the style string that are used for buttons
     * 
     * @param bgColor the background color for the button
     * @return Style string for button styling
     */
    static String button(String bgColor) {
        return baseStyle() + bg(bgColor) + borderAndRadius() + "-fx-padding:0;";
    }

    /**
     * Generates a font size property string.
     *
     * @param size the font size
     * @return Style string for font-size property
     */
    static String fontSize(int size) {
        return "-fx-font-size:" + size + ";";
    }

    /**
     * Generates Styling for text input fields.
     * 
     * @return complete style string for text input field styling
     */
    static String textInput() {
        return baseStyle() + bg(COLOR_BG) + borderAndRadius() + focusTransparent() +
                String.format("-fx-padding:%.0f;-fx-prompt-text-fill:%s;", PADDING, COLOR_PROMPT_TEXT);
    }

    /**
     * Generates style string for radio buttons.
     *
     * @return Style string for radio button styling
     */
    static String radioButton() {
        return baseStyle() + bg(COLOR_TRANSPARENT) + ";-fx-border-color:" + COLOR_TRANSPARENT + ";-fx-border-width:0;";
    }

    /**
     * Generates style string for dropdown menu items.
     *
     * @param bgColor the background color for the dropdown item
     * @return Style string for dropdown item styling
     */
    static String dropdownItem(String bgColor) {
        return baseStyle() + bg(bgColor) + ";-fx-border-color:" + COLOR_TRANSPARENT + ";-fx-border-width:0;";
    }

    /**
     * Generates style string for TableView components.
     *
     * @return Style string for TableView styling
     */
    static String tableView() {
        return focusTransparent() + borderAndRadius();
    }
}
