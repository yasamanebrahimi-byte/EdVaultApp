package cs151.controller;

import cs151.model.Comment;
import cs151.model.Student;
import cs151.model.StudentRepository;
import cs151.util.ValidationUtils;

import java.util.List;
import java.util.ArrayList;

public class CommentController {
    private final StudentRepository studentRepository;

    /**
     * Constructs a new CommentController with a default StudentRepository.
     */
    public CommentController() {
        this.studentRepository = new StudentRepository();
    }

    /**
     * Adds a comment to a student's profile.
     *
     * @param studentName The name of the student to add the comment to
     * @param commentText The text content of the comment
     * @return true if the comment was successfully added, false if validation
     *         failed or student not found
     */
    public boolean addCommentToStudent(String studentName, String commentText) {
        if (ValidationUtils.isStringEmpty(studentName) || ValidationUtils.isStringEmpty(commentText)) {
            return false;
        }
        Student student = studentRepository.findByName(studentName);
        if (student == null) {
            return false;
        }
        student.addComment(ValidationUtils.safeTrim(commentText));
        studentRepository.updateStudent(studentName, student);
        return true;
    }

    /**
     * Retrieves the creation date of a comment as a string.
     * 
     * @param comment The comment to get the date from
     * @return The creation date as a string, or empty string if comment/date is null
     */
    public String getCommentDate(Comment comment) {
        if (comment == null || comment.getCreatedAt() == null) {
            return "";
        }
        return comment.getCreatedAt().toString();
    }

    /**
     * Retrieves a student with their comments.
     * 
     * @param studentName The name of the student to retrieve
     * @return The student object with comments, or null if not found/has none
     */
    private Student getStudentWithComments(String studentName) {
        Student student = studentRepository.findByName(studentName);
        if (student == null || student.getComments() == null) {
            return null;
        }
        return student;
    }

    /**
     * Retrieves all comment texts for a specific student.
     * 
     * @param studentName The name of the student
     * @return A list of comment text strings, or empty list if student not found/has none
     */
    public List<String> getStudentCommentTexts(String studentName) {
        Student student = getStudentWithComments(studentName);
        if (student == null) {
            return new ArrayList<>();
        }

        return student.getComments().stream()
                .map(Comment::getText)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Retrieves all comment creation dates for a specific student.
     * 
     * @param studentName The name of the student
     * @return A list of comment date strings, or empty list if student not found/has none
     */
    public List<String> getStudentCommentDates(String studentName) {
        Student student = getStudentWithComments(studentName);
        if (student == null) {
            return new ArrayList<>();
        }
        return student.getComments().stream()
                .map(this::getCommentDate)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Returns the list of Comment objects for a given student.
     *
     * @param studentName the student's name
     * @return list of Comment objects (empty list if none or student not found)
     */
    public List<Comment> getStudentComments(String studentName) {
        Student student = getStudentWithComments(studentName);
        if (student == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(student.getComments());
    }
}