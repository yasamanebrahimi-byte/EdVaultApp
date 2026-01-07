package cs151.util;

public class ValidationUtils {

    /**
     * Checks if a string is null or empty after trimming whitespace.
     * 
     * @param str The string to check
     * @return true if the string is null or empty/whitespace only, otherwise false
     */
    public static boolean isStringEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Checks if a collection is null or empty.
     * 
     * @param collection The collection to check
     * @return true if the collection is null or empty, otherwise false
     */
    public static boolean isCollectionEmpty(java.util.Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Validates that at least one employment status option is selected.
     * 
     * @param employedSelected   Whether the "employed" option is selected
     * @param unemployedSelected Whether the "unemployed" option is selected
     * @return true if at least one option is selected, otherwise false
     */
    public static boolean isEmploymentStatusSelected(boolean employedSelected, boolean unemployedSelected) {
        return employedSelected || unemployedSelected;
    }

    /**
     * Validates job details are provided when the student is employed.
     * 
     * @param employedSelected Whether the student is marked as employed
     * @param jobDetails       The job details string
     * @return true if validation passes, otherwise false
     */
    public static boolean isJobDetailsValid(boolean employedSelected, String jobDetails) {
        return !employedSelected || !isStringEmpty(jobDetails);
    }

    /**
     * Checks if a student value contains the search value.
     *
     * @param studentValue The student's field value to search in
     * @param searchValue  The value to search for
     * @return true if studentValue contains searchValue or searchValue is empty, otherwise false
     */
    public static boolean matchesStringContains(String studentValue, String searchValue) {
        if (isStringEmpty(searchValue))
            return true;
        return studentValue != null && studentValue.toLowerCase().contains(searchValue.toLowerCase().trim());
    }

    /**
     * Checks if a student's boolean value matches the search criteria.
     *
     * @param studentValue The student's boolean field value
     * @param searchValue  The boolean value to match against
     * @return true if values match or searchValue is null, otherwise false
     */
    public static boolean matchesBooleanValue(boolean studentValue, Boolean searchValue) {
        if (searchValue == null) {
            return true;
        }
        return studentValue == searchValue;
    }

    /**
     * Checks if a student flag matches the search criteria for filtering.
     *
     * @param studentFlag The student's flag value
     * @param searchFlag  The filter flag
     * @return true if criteria matches, otherwise false
     */
    public static boolean matchesBooleanFlag(boolean studentFlag, Boolean searchFlag) {
        if (searchFlag == null) {
            return true;
        }
        return !searchFlag || studentFlag;
    }

    /**
     * Safely trims a string
     * 
     * @param str The string to trim
     * @return The trimmed string, or null if input was null
     */
    public static String safeTrim(String str) {
        return str != null ? str.trim() : null;
    }

    /**
     * Validates that a full name contains at least first and last name.
     *
     * @param fullName The full name to validate
     * @return true if contains at least 2 non-empty name parts, otherwise false
     */
    public static boolean isValidFullName(String fullName) {
        if (isStringEmpty(fullName))
            return false;
        String[] parts = fullName.trim().split("\\s+");
        return parts.length >= 2 && !isStringEmpty(parts[0]) && !isStringEmpty(parts[1]);
    }
}