package cs151.model;

import cs151.util.Database;
import java.util.List;
import java.util.ArrayList;

public class ProgrammingLanguageRepository {
    private List<String> programmingLanguages;

    /**
     * Creates a new ProgrammingLanguageRepository and loads data from the txt file.
     */
    public ProgrammingLanguageRepository() {
        refreshFromDatabase();
    }

    /**
     * Refreshes the internal programming languages list from the database.
     */
    private void refreshFromDatabase() {
        this.programmingLanguages = Database.loadProgrammingLanguages();
    }

    /**
     * Retrieves all programming languages, sorted alphabetically
     * 
     * @return a new sorted list containing all programming languages
     */
    public List<String> getAllLanguages() {
        refreshFromDatabase();
        List<String> sortedLanguages = new ArrayList<>(this.programmingLanguages);
        sortedLanguages.sort(String.CASE_INSENSITIVE_ORDER);
        return sortedLanguages;
    }

    /**
     * Adds a new programming language to the repository.
     * 
     * @param language the programming language to add
     * @return true if the language was added, false if it already existed
     * @throws IllegalArgumentException if language is null or empty
     * @throws RuntimeException         if saving to database fails
     */
    public boolean addLanguage(String language) {
        if (language == null || language.trim().isEmpty()) {
            throw new IllegalArgumentException("Language cannot be null or empty");
        }
        String trimmedLanguage = language.trim();
        if (this.programmingLanguages.stream()
                .anyMatch(lang -> lang.equalsIgnoreCase(trimmedLanguage))) {
            return false;
        }

        this.programmingLanguages.add(trimmedLanguage);
        this.programmingLanguages.sort(String.CASE_INSENSITIVE_ORDER);
        saveToDatabase();
        return true;
    }

    /**
     * Checks if a programming language exists in the repository.
     * 
     * @param language the programming language
     * @return true if the language exists, otherwise false
     */
    public boolean containsLanguage(String language) {
        if (language == null || language.trim().isEmpty()) {
            return false;
        }
        return this.programmingLanguages.stream()
                .anyMatch(lang -> lang.equalsIgnoreCase(language.trim()));
    }

    /**
     * Saves the current programming languages list to the database.
     * 
     * @throws RuntimeException if the save from the database fails
     */
    private void saveToDatabase() {
        try {
            Database.saveProgrammingLanguages(this.programmingLanguages);
        } catch (Exception e) {
            throw new RuntimeException("Error saving programming languages: " + e.getMessage(), e);
        }
    }
}