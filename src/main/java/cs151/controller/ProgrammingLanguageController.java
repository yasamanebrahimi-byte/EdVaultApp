package cs151.controller;

import cs151.model.ProgrammingLanguageRepository;
import cs151.util.ValidationUtils;
import java.util.List;

public class ProgrammingLanguageController {
    private final ProgrammingLanguageRepository repo;

    /**
     * Constructs a new ProgrammingLanguageController with a default repository.
     */
    public ProgrammingLanguageController() {
        this.repo = new ProgrammingLanguageRepository();
    }

    /**
     * Adds a new programming language to the repository.
     *
     * @param name The name of the programming language to add
     * @return true if the language was successfully added, false if validation failed
     */
    public boolean addLanguage(String name) {
        if (ValidationUtils.isStringEmpty(name)) {
            return false;
        }

        String trimmedName = ValidationUtils.safeTrim(name);
        if (languageExists(trimmedName)) {
            return false;
        }
        return repo.addLanguage(trimmedName);
    }

    /**
     * Retrieves all programming languages from the repository.
     * 
     * @return List of all programming language names
     */
    public List<String> getAllLanguages() {
        return repo.getAllLanguages();
    }

    /**
     * Checks if a programming language already exists in the repository.
     *
     * @param name The name of the language to check
     * @return true if the language exists, false if it doesn't exist or empty name
     */
    public boolean languageExists(String name) {
        if (ValidationUtils.isStringEmpty(name)) {
            return false;
        }
        return repo.containsLanguage(ValidationUtils.safeTrim(name));
    }

    /**
     * Validates a programming language name for addition.
     *
     * @param name The name to validate
     * @return null if validation passes, error message string if validation fails
     */
    public String validateLanguageName(String name) {
        if (ValidationUtils.isStringEmpty(name)) {
            return "Please enter a programming language name.";
        }
        if (languageExists(name)) {
            return "This language already exists in the database.";
        }
        return null;
    }
}
