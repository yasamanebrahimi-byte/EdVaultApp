package cs151.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import cs151.model.Student;
import cs151.model.Comment;
import org.json.JSONArray;
import org.json.JSONObject;

public class Database {
    private static final String PATH = "src/main/data/";

    /**
     * Makes sure that the data directory exists or creates it.
     * 
     * @throws IOException if the directory cannot be created
     */
    private static void ensureDataDirectoryExists() throws IOException {
        File dataDir = new File(PATH);
        if (!dataDir.exists()) {
            if (!dataDir.mkdirs()) {
                throw new IOException("Failed to create data directory: " + dataDir.getAbsolutePath());
            }
        }
    }

    /**
     * Makes sure that the language file exists or creates it.
     * 
     * @throws IOException if the file cannot be created
     */
    private static void ensureLanguagesFileExists() throws IOException {
        ensureDataDirectoryExists();
        File languagesFile = new File(PATH + "/languages.txt");
        if (!languagesFile.exists()) {
            if (!languagesFile.createNewFile()) {
                throw new IOException("Failed to create languages file: " + languagesFile.getAbsolutePath());
            }
        }
    }

    /**
     * Makes sure that the student info file exists or creates it.
     * 
     * @throws IOException if the file cannot be created or written to
     */
    private static void ensureStudentsFileExists() throws IOException {
        ensureDataDirectoryExists();
        File studentsFile = new File(PATH + "/students.json");
        if (!studentsFile.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH + "/students.json"))) {
                writer.write("{\n  \"students\": []\n}");
            }
        }
    }

    /**
     * Loads and returns all programming languages from the flat file storage.
     * 
     * @return a sorted list of programming language names
     */
    public static List<String> loadProgrammingLanguages() {
        List<String> languages = new ArrayList<>();
        for (String line : readLanguagesFile()) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty()) {
                languages.add(trimmed);
            }
        }
        languages.sort(String.CASE_INSENSITIVE_ORDER);
        return languages;
    }

    /**
     * Deletes a student from the database by their full name.
     * 
     * @param fullName the full name of the student to delete
     * @throws IOException if there's an error reading or writing from the database
     */
    public static void deleteStudentByName(String fullName) throws IOException {
        List<Student> students = loadStudents();
        students.removeIf(s -> s.getFullName() != null && s.getFullName().equalsIgnoreCase(fullName));
        updateStudents(students);
    }

    /**
     * Loads all student profiles from the database.
     * 
     * @return a list of all student profiles sorted alphabetically by name
     */
    public static List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        try {
            String json = readStudentsFile();
            JSONObject root = new JSONObject(json);
            JSONArray studentsArray = root.getJSONArray("students");
            for (int i = 0; i < studentsArray.length(); i++) {
                JSONObject s = studentsArray.getJSONObject(i);
                students.add(studentFromJson(s));
            }
            // Sort students alphabetically by name
            students.sort((s1, s2) -> {
                String name1 = s1.getFullName() != null ? s1.getFullName() : "";
                String name2 = s2.getFullName() != null ? s2.getFullName() : "";
                return name1.compareToIgnoreCase(name2);
            });
        } catch (Exception e) {
            System.err.println("Error loading students from database: " + e.getMessage());
        }
        return students;
    }

    /**
     * Saves a list of programming languages to the flat file storage
     * 
     * @param languages the list of programming languages to save
     * @throws IOException if there's an error writing to the file
     */
    public static void saveProgrammingLanguages(List<String> languages) throws IOException {
        List<String> sortedLanguages = new ArrayList<>(languages);
        sortedLanguages.sort(String.CASE_INSENSITIVE_ORDER);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH + "/languages.txt", false))) {
            for (String language : sortedLanguages) {
                writer.write(language);
                writer.newLine();
            }
        }
    }

    /**
     * Adds a new student profile to the database.
     *
     * @param student the student profile to add to the database
     * @throws IOException if there's an error reading or writing to the file
     */
    public static void addStudent(Student student) throws IOException {
        String json = readStudentsFile();
        JSONObject root = new JSONObject(json);
        JSONArray studentsArray = root.getJSONArray("students");
        studentsArray.put(studentToJson(student));
        writeStudentsFile(root.toString(2));
    }

    /**
     * Updates the entire student database with new students
     * 
     * @param students the complete list of students to save to the database
     * @throws IOException if there's an error writing to the file
     */
    public static void updateStudents(List<Student> students) throws IOException {
        JSONArray arr = new JSONArray();
        for (Student s : students) {
            arr.put(studentToJson(s));
        }
        JSONObject root = new JSONObject();
        root.put("students", arr);
        writeStudentsFile(root.toString(2));
    }

    /**
     * Reads all lines from the programming languages file.
     *
     * @return a list of all lines from the languages file
     */
    private static List<String> readLanguagesFile() {
        List<String> lines = new ArrayList<>();
        try {
            ensureLanguagesFileExists();
            try (BufferedReader reader = new BufferedReader(new FileReader(PATH + "/languages.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading languages file: " + e.getMessage());
        }
        return lines;
    }

    /**
     * Reads the entire contents of the students JSON file as a string.
     *
     * @return the complete JSON content of the students file
     * @throws IOException if there's an error reading the file
     */
    private static String readStudentsFile() throws IOException {
        ensureStudentsFileExists();
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH + "/students.json"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    /**
     * Writes content to the students JSON file
     * 
     * @param content the JSON content to write to the file
     * @throws IOException if there's an error writing to the file
     */
    private static void writeStudentsFile(String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH + "/students.json", false))) {
            writer.write(content);
        }
    }

    /**
     * Converts a Student object to its JSON representation for storage.
     * 
     * @param student the student object to convert to JSON
     * @return a JSONObject containing all student data
     */
    private static JSONObject studentToJson(Student student) {
        JSONObject s = new JSONObject();
        s.put("name", student.getFullName());
        s.put("academic_status", student.getAcademicStatus());
        s.put("employed", student.isEmployed());
        s.put("job_details", student.getJobDetails());
        s.put("programming_languages", student.getProgrammingLanguages());
        s.put("databases", student.getDatabasesKnown());
        s.put("preferred_role", student.getPreferredProfessionalRole());
        s.put("whitelist", student.isWhitelisted());
        s.put("blacklist", student.isBlacklisted());
        List<Comment> comments = student.getComments();
        if (comments != null) {
            s.put("comments", commentsToJsonArray(comments));
        } else {
            s.put("comments", JSONObject.NULL);
        }
        return s;
    }

    /**
     * Converts a list of Comment objects to a JSON array for storage.
     *
     * @param comments the list of comments to convert to JSON
     * @return a JSONArray containing all comment data
     */
    private static JSONArray commentsToJsonArray(List<Comment> comments) {
        JSONArray commentsArr = new JSONArray();
        for (Comment c : comments) {
            JSONObject cObj = new JSONObject();
            String createdAtString = c.getCreatedAt() != null ? c.getCreatedAt().toString() : "";
            cObj.put("created_at_date", createdAtString);
            cObj.put("comment_text", c.getText());
            commentsArr.put(cObj);
        }
        return commentsArr;
    }

    /**
     * Converts a JSON object to a Student object.
     * 
     * @param s the JSONObject containing student data
     * @return a Student object
     */
    private static Student studentFromJson(JSONObject s) {
        String name = s.optString("name", null);
        String academicStatus = s.optString("academic_status", null);
        boolean employed = s.optBoolean("employed", false);
        String jobDetails = s.isNull("job_details") ? null : s.optString("job_details", null);

        List<String> programmingLanguages = null;
        if (!s.isNull("programming_languages")) {
            JSONArray langs = s.getJSONArray("programming_languages");
            programmingLanguages = new ArrayList<>();
            for (int j = 0; j < langs.length(); j++) {
                programmingLanguages.add(langs.getString(j));
            }
        }

        List<String> databasesKnown = null;
        if (!s.isNull("databases")) {
            JSONArray dbs = s.getJSONArray("databases");
            databasesKnown = new ArrayList<>();
            for (int j = 0; j < dbs.length(); j++) {
                databasesKnown.add(dbs.getString(j));
            }
        }

        String preferredRole = s.optString("preferred_role", null);
        boolean whitelist = s.optBoolean("whitelist", false);
        boolean blacklist = s.optBoolean("blacklist", false);

        List<Comment> comments = null;
        if (!s.isNull("comments")) {
            JSONArray commentsArr = s.getJSONArray("comments");
            comments = new ArrayList<>();
            for (int j = 0; j < commentsArr.length(); j++) {
                JSONObject c = commentsArr.getJSONObject(j);
                String createdAt = c.optString("created_at_date", null);
                String commentText = c.optString("comment_text", null);
                comments.add(Comment.fromDatabase(createdAt, commentText));
            }
        }

        return new Student(name, academicStatus, employed, jobDetails, programmingLanguages,
                databasesKnown, preferredRole, comments, whitelist, blacklist);
    }
}
