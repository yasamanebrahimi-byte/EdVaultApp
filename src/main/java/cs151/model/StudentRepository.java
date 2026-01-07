package cs151.model;

import cs151.util.Database;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {
    private List<Student> allStudents;

    /**
     * Creates a new StudentRepository and loads initial data from the database.
     */
    public StudentRepository() {
        this.allStudents = new ArrayList<>();
        refreshFromDatabase();
    }

    /**
     * Refreshes the internal student list from the database
     */
    private void refreshFromDatabase() {
        this.allStudents = Database.loadStudents();
    }

    /**
     * Retrieves all students from the database, sorted alphabetically by name.
     *
     * @return a new list containing all students sorted by name
     */
    public List<Student> getAllStudents() {
        refreshFromDatabase();
        List<Student> sortedStudents = new ArrayList<>(this.allStudents);
        sortedStudents.sort((s1, s2) -> {
            String name1 = s1.getFullName() != null ? s1.getFullName() : "";
            String name2 = s2.getFullName() != null ? s2.getFullName() : "";
            return name1.compareToIgnoreCase(name2);
        });
        return sortedStudents;
    }

    /**
     * Adds a new student to the database.
     *
     * @param student the student to add
     * @throws IllegalArgumentException if student is null
     * @throws RuntimeException         if the database operation fails
     */
    public void addStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        try {
            Database.addStudent(student);
            refreshFromDatabase();
        } catch (Exception e) {
            throw new RuntimeException("Failed to add student: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a student from the database by their full name.
     *
     * @param fullName the full name of the student to delete
     * @throws IllegalArgumentException if fullName is null or empty
     * @throws RuntimeException         if the database operation fails
     */
    public void deleteStudent(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be null or empty");
        }
        try {
            Database.deleteStudentByName(fullName);
            refreshFromDatabase();
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete student: " + e.getMessage(), e);
        }
    }

    /**
     * Finds a student by their full name.
     *
     * @param fullName the full name to search for
     * @return the matching student, or null if not found
     */
    public Student findByName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return null;
        }
        refreshFromDatabase();
        return allStudents.stream()
                .filter(student -> student.getFullName() != null &&
                        student.getFullName().equalsIgnoreCase(fullName.trim()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Updates an existing student's information in the database.
     * 
     * @param originalName   the current full name of the student to update
     * @param updatedStudent the updated student information
     * @throws IllegalArgumentException if name or updated student are null/empty or not found
     * @throws RuntimeException         if the save operation fails
     */
    public void updateStudent(String originalName, Student updatedStudent) {
        if (originalName == null || originalName.trim().isEmpty()) {
            throw new IllegalArgumentException("Original name cannot be null or empty");
        }
        if (updatedStudent == null) {
            throw new IllegalArgumentException("Updated student cannot be null");
        }

        try {
            refreshFromDatabase();
            boolean found = false;
            for (int i = 0; i < allStudents.size(); i++) {
                if (allStudents.get(i).getFullName().equalsIgnoreCase(originalName.trim())) {
                    allStudents.set(i, updatedStudent);
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new IllegalArgumentException("Student with name '" + originalName + "' not found");
            }
            Database.updateStudents(allStudents);
            refreshFromDatabase();
        } catch (Exception e) {
            throw new RuntimeException("Failed to update student: " + e.getMessage(), e);
        }
    }
}