package cs151.model;

import java.time.LocalDate;

public class Comment {
    private final String text;
    private LocalDate createdAt;

    /**
     * Creates a new comment with the specified text, timestamped today
     *
     * @param text the content of the comment
     */
    public Comment(String text) {
        this.text = text;
        this.createdAt = LocalDate.now();
    }

    /**
     * Creates a comment from database data with a specific date.
     *
     * @param createdAtString the creation date as a string
     * @param text            the content of the comment
     * @return a new Comment instance with the specified date and text
     */
    public static Comment fromDatabase(String createdAtString, String text) {
        Comment comment = new Comment(text);
        try {
            comment.createdAt = LocalDate.parse(createdAtString);
        } catch (Exception e) {
            comment.createdAt = LocalDate.now();
        }
        return comment;
    }

    /**
     * Gets the text content of the comment.
     * 
     * @return the comment text
     */
    public String getText() {
        return text;
    }

    /**
     * Gets the date when the comment was created.
     * 
     * @return the creation date
     */
    public LocalDate getCreatedAt() {
        return createdAt;
    }
}