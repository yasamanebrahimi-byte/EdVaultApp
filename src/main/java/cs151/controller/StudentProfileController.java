package cs151.controller;

import cs151.model.Comment;
import cs151.model.Student;
import cs151.model.StudentRepository;
import cs151.util.ValidationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StudentProfileController {
    private final StudentRepository repo;

    /**
     * Constructs a new StudentProfileController with a default StudentRepository.
     */
    public StudentProfileController() {
        this.repo = new StudentRepository();
    }

    /**
     * Creates and adds a new student to the repository using individual parameters.
     *
     * @param name                 The student's full name
     * @param academicStatus       The student's academic status
     * @param employed             Whether the student is employed
     * @param jobDetails           Job details
     * @param programmingLanguages List of programming languages known
     * @param databasesKnown       List of databases known
     * @param preferredRole        Preferred professional role
     * @param commentText          Optional comment text
     * @param whitelist            Whether student is whitelisted
     * @param blacklist            Whether student is blacklisted
     */
    public void createAndAddStudent(String name, String academicStatus, boolean employed, 
            String jobDetails, List<String> programmingLanguages, List<String> databasesKnown,
            String preferredRole, String commentText, boolean whitelist, boolean blacklist) {
        
        List<Comment> commentList = null;
        if (commentText != null && !commentText.trim().isEmpty()) {
            commentList = new ArrayList<>();
            commentList.add(new Comment(commentText.trim()));
        }

        Student student = new Student(name, academicStatus, employed, jobDetails,
            programmingLanguages, databasesKnown, preferredRole, commentList, whitelist, blacklist);
        repo.addStudent(student);
    }

    /**
     * Deletes a student from the repository by full name.
     * 
     * @param fullName The full name of the student to delete
     */
    public void deleteStudent(String fullName) {
        repo.deleteStudent(fullName);
    }

    /**
     * Checks if a student name is unique within the existing student list.
     *
     * @param name        The name to check for uniqueness
     * @param allStudents The list of existing students
     * @return true if the name is unique, false if it already exists or is empty
     */
    private boolean isStudentNameUnique(String name, List<Student> allStudents) {
        if (ValidationUtils.isStringEmpty(name)) {
            return false;
        }
        for (Student student : allStudents) {
            if (student.getFullName().equalsIgnoreCase(ValidationUtils.safeTrim(name))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates all required fields for a student profile.
     * 
     * @param name               The student's full name
     * @param academicStatus     The student's academic status
     * @param preferredRole      The student's preferred professional role
     * @param employedSelected   Whether the "employed" option is selected
     * @param unemployedSelected Whether the "unemployed" option is selected
     * @param jobDetails         Job details
     * @return null if validation passes, error message string if validation fails
     */
    public String validateStudentProfile(String name, String academicStatus, Set<String> knownDatabases, String preferredRole,
                 Set<String> knownLanguages, boolean employedSelected, boolean unemployedSelected, String jobDetails, boolean edit) {
        List<Student> allStudents = repo.getAllStudents();
        if (edit) {
            allStudents = allStudents.stream().filter(s -> !s.getFullName().equals(name)).collect(Collectors.toList());
        }

        String trimmedName = ValidationUtils.safeTrim(name);
        String trimmedJobDetails = ValidationUtils.safeTrim(jobDetails);

        // Name is present and correctly formatted
        if (ValidationUtils.isStringEmpty(trimmedName)) {
            return "Please submit a valid name.";
        } else if (!ValidationUtils.isValidFullName(trimmedName)) {
            return "Please enter a first and last name.";
        } else if (!isStudentNameUnique(trimmedName, allStudents)) {
            return "A student with this name already exists.";
        }

        // Academic status is selected
        if (ValidationUtils.isStringEmpty(academicStatus)) {
            return "Please select an academic status.";
        }

        // Validate employment status is properly selected
        if (!ValidationUtils.isEmploymentStatusSelected(employedSelected, unemployedSelected)) {
            return "Please select Employed or Unemployed.";
        } else if (!ValidationUtils.isJobDetailsValid(employedSelected, trimmedJobDetails)) {
            return "Please enter job details for employed students.";
        }

        // Validate at least one option is selected for multi-select fields
        if (ValidationUtils.isCollectionEmpty(knownLanguages)) {
            return "Please select at least one programming language option.";
        } else if (ValidationUtils.isCollectionEmpty(knownDatabases)) {
            return "Please select at least one known database.";
        } else if (ValidationUtils.isStringEmpty(preferredRole)) {
            return "Please select a preferred role.";
        }
        return null;
    }

    /**
     * Updates an existing student record using individual parameters.
     *
     * @param originalName         The original name of the student to update
     * @param newName              The updated full name
     * @param academicStatus       The updated academic status
     * @param employed             The updated employment status
     * @param jobDetails           Updated job details
     * @param programmingLanguages Updated list of programming languages known
     * @param databasesKnown       Updated list of databases known
     * @param preferredRole        Updated preferred professional role
     * @param whitelist            Updated whitelist status
     * @param blacklist            Updated blacklist status
     */
    public void updateStudentByName(String originalName, String newName, String academicStatus, 
            boolean employed, String jobDetails, List<String> programmingLanguages, 
            List<String> databasesKnown, String preferredRole, boolean whitelist, boolean blacklist) {
        
        Student student = repo.findByName(originalName);
        if (student != null) {
            updateStudentData(student, newName, academicStatus, employed, jobDetails, 
                    programmingLanguages, databasesKnown, preferredRole, whitelist, blacklist);
            repo.updateStudent(originalName, student);
        }
    }


    /**
     * Updates all fields of a student object with new values.
     *
     * @param student              The student object to update
     * @param name                 The new full name
     * @param academicStatus       The new academic status
     * @param employed             The new employment status
     * @param jobDetails           The new job details
     * @param programmingLanguages The new list of programming languages
     * @param databasesKnown       The new list of known databases
     * @param preferredRole        The new preferred professional role
     * @param whitelist            The new whitelist status
     * @param blacklist            The new blacklist status
     */
    public void updateStudentData(Student student, String name, String academicStatus,
            boolean employed, String jobDetails, List<String> programmingLanguages,
            List<String> databasesKnown, String preferredRole, boolean whitelist, boolean blacklist) {
        String trimmedName = ValidationUtils.safeTrim(name);
        String trimmedJobDetails = ValidationUtils.safeTrim(jobDetails);

        student.setFullName(trimmedName);
        student.setAcademicStatus(academicStatus);
        student.setEmployed(employed);
        student.setJobDetails(ValidationUtils.isStringEmpty(trimmedJobDetails) ? null : trimmedJobDetails);
        student.setProgrammingLanguages(programmingLanguages);
        student.setDatabasesKnown(databasesKnown);
        student.setPreferredProfessionalRole(preferredRole);
        student.setWhitelisted(whitelist);
        student.setBlacklisted(blacklist);
    }

    /**
     * Retrieves a specific field from a student by name.
     *
     * @param <T>         The type of the field to retrieve
     * @param studentName The name of the student
     * @param fieldGetter Function that extracts the desired field from a Student object
     * @return The field value, or null if student not found
     */
    private <T> T getStudentField(String studentName, Function<Student, T> fieldGetter) {
        Student student = repo.findByName(studentName);
        return student != null ? fieldGetter.apply(student) : null;
    }

    /**
     * Retrieves a student's full name.
     * 
     * @param studentName The name of the student to look up
     * @return The student's full name, or null if not found
     */
    public String getStudentFullName(String studentName) {
        return getStudentField(studentName, Student::getFullName);
    }

    /**
     * Retrieves a student's academic status.
     * 
     * @param studentName The name of the student to look up
     * @return The student's academic status, or null if not found
     */
    public String getStudentAcademicStatus(String studentName) {
        return getStudentField(studentName, Student::getAcademicStatus);
    }

    /**
     * Retrieves a student's job details.
     * 
     * @param studentName The name of the student to look up
     * @return The student's job details, or null if not found or unemployed
     */
    public String getStudentJobDetails(String studentName) {
        return getStudentField(studentName, Student::getJobDetails);
    }

    /**
     * Retrieves a student's programming languages.
     * 
     * @param studentName The name of the student to look up
     * @return List of programming languages, or null if student not found
     */
    public List<String> getStudentProgrammingLanguages(String studentName) {
        return getStudentField(studentName, Student::getProgrammingLanguages);
    }

    /**
     * Retrieves a student's known databases.
     * 
     * @param studentName The name of the student to look up
     * @return List of known databases, or null if student not found
     */
    public List<String> getStudentDatabasesKnown(String studentName) {
        return getStudentField(studentName, Student::getDatabasesKnown);
    }

    /**
     * Retrieves a student's preferred professional role.
     * 
     * @param studentName The name of the student to look up
     * @return The student's preferred role, or null if not found
     */
    public String getStudentPreferredRole(String studentName) {
        return getStudentField(studentName, Student::getPreferredProfessionalRole);
    }

    /**
     * Checks if a student is employed.
     * 
     * @param studentName The name of the student to look up
     * @return true if student is employed, false if unemployed or not found
     */
    public boolean isStudentEmployed(String studentName) {
        return Boolean.TRUE.equals(getStudentField(studentName, Student::isEmployed));
    }

    /**
     * Checks if a student is whitelisted.
     * 
     * @param studentName The name of the student to look up
     * @return true if student is whitelisted, false if not whitelisted or not found
     */
    public boolean isStudentWhitelisted(String studentName) {
        return Boolean.TRUE.equals(getStudentField(studentName, Student::isWhitelisted));
    }

    /**
     * Checks if a student is blacklisted.
     * 
     * @param studentName The name of the student to look up
     * @return true if student is blacklisted, false if not blacklisted or not found
     */
    public boolean isStudentBlacklisted(String studentName) {
        return Boolean.TRUE.equals(getStudentField(studentName, Student::isBlacklisted));
    }
}