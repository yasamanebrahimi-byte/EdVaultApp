package cs151.controller;

import cs151.model.Student;
import cs151.model.StudentRepository;
import java.util.ArrayList;
import java.util.List;

public class SearchController {
    private final StudentRepository studentRepository;

    /**
     * Constructs a new SearchController with a default StudentRepository.
     */
    public SearchController() {
        this.studentRepository = new StudentRepository();
    }

    /**
     * Performs a search across all student fields using a string
     *
     * @param searchTerm The term to search for
     * @return List of student names matching the search term
     */
    public List<String> globalSearch(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return studentRepository.getAllStudents().stream()
                    .map(Student::getFullName)
                    .toList();
        }
        String normalizedSearchTerm = searchTerm.toLowerCase().trim();
        List<Student> allStudents = studentRepository.getAllStudents();
        List<Student> matchingStudents = new ArrayList<>();

        for (Student student : allStudents) {
            if (matchesGlobalSearch(student, normalizedSearchTerm)) {
                matchingStudents.add(student);
            }
        }
        return matchingStudents.stream()
                .map(Student::getFullName)
                .toList();
    }

    /**
     * Checks if a student matches the global search term across any field.
     */
    private boolean matchesGlobalSearch(Student s, String term) {
        if (containsIgnoreCase(s.getFullName(), term)
                || containsIgnoreCase(s.getAcademicStatus(), term)
                || containsIgnoreCase(s.getJobDetails(), term)
                || containsIgnoreCase(s.getPreferredProfessionalRole(), term)
                || ("employed".contains(term) && s.isEmployed())
                || ("unemployed".contains(term) && !s.isEmployed())
                || ("whitelist".contains(term) && s.isWhitelisted())
                || ("blacklist".contains(term) && s.isBlacklisted())) {
            return true;
        }
        if (s.getProgrammingLanguages() != null &&
                s.getProgrammingLanguages().stream().anyMatch(lang -> containsIgnoreCase(lang, term))) {
            return true;
        }

        return s.getDatabasesKnown() != null &&
                s.getDatabasesKnown().stream().anyMatch(db -> containsIgnoreCase(db, term));
    }

    private boolean containsIgnoreCase(String text, String searchTerm) {
        return text != null && text.toLowerCase().contains(searchTerm);
    }
}