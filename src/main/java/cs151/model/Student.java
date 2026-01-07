package cs151.model;

import java.util.*;

public class Student {
    private String fullName;
    private String academicStatus;
    private boolean employed;
    private String jobDetails;
    private List<String> programmingLanguages;
    private List<String> databasesKnown;
    private String preferredProfessionalRole;
    private final List<Comment> comments;
    private boolean whitelist;
    private boolean blacklist;

    /**
     * Creates a new Student with the specified information.
     * 
     * @param fullName                  the student's full name
     * @param academicStatus            the student's current academic status
     * @param employed                  whether the student is currently employed
     * @param jobDetails                details about the student's job
     * @param programmingLanguages      list of programming languages known
     * @param databasesKnown            list of databases known
     * @param preferredProfessionalRole the student's preferred professional role
     * @param comments                  list of comments
     * @param whitelist                 whether the student is whitelisted
     * @param blacklist                 whether the student is blacklisted
     */
    public Student(String fullName, String academicStatus, boolean employed, String jobDetails,
            List<String> programmingLanguages, List<String> databasesKnown, String preferredProfessionalRole,
            List<Comment> comments, boolean whitelist, boolean blacklist) {
        this.fullName = fullName;
        this.academicStatus = academicStatus;
        this.employed = employed;
        this.jobDetails = jobDetails;
        this.programmingLanguages = programmingLanguages != null ? programmingLanguages : new ArrayList<>();
        this.databasesKnown = databasesKnown != null ? databasesKnown : new ArrayList<>();
        this.preferredProfessionalRole = preferredProfessionalRole;
        this.comments = comments != null ? comments : new ArrayList<>();
        this.whitelist = whitelist;
        this.blacklist = blacklist;
    }

    /**
     * Returns a string representation of the student, their full name.
     * 
     * @return the student's full name
     */
    @Override
    public String toString() {
        return this.getFullName();
    }

    /**
     * Gets the student's full name
     * 
     * @return the full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Gets the student's academic status.
     * 
     * @return the academic status
     */
    public String getAcademicStatus() {
        return academicStatus;
    }

    /**
     * Checks if the student is currently employed.
     * 
     * @return true if employed, false otherwise
     */
    public boolean isEmployed() {
        return employed;
    }

    /**
     * Gets details about the student's job.
     * 
     * @return job details, or null if not employed
     */
    public String getJobDetails() {
        return jobDetails;
    }

    /**
     * Gets the list of programming languages the student knows.
     * 
     * @return list of programming languages
     */
    public List<String> getProgrammingLanguages() {
        return programmingLanguages;
    }

    /**
     * Gets the list of databases the student knows.
     * 
     * @return list of databases known
     */
    public List<String> getDatabasesKnown() {
        return databasesKnown;
    }

    /**
     * Gets the student's preferred professional role.
     * 
     * @return the preferred professional role
     */
    public String getPreferredProfessionalRole() {
        return preferredProfessionalRole;
    }

    /**
     * Gets the list of comments associated with this student.
     * 
     * @return list of comments
     */
    public List<Comment> getComments() {
        return comments;
    }

    /**
     * Checks if the student is on the whitelist.
     * 
     * @return true if whitelisted, false otherwise
     */
    public boolean isWhitelisted() {
        return whitelist;
    }

    /**
     * Checks if the student is on the blacklist.
     * 
     * @return true if blacklisted, false otherwise
     */
    public boolean isBlacklisted() {
        return blacklist;
    }

    /**
     * Adds a new comment with the date to this student's profile.
     *
     * @param text the comment text to add
     */
    public void addComment(String text) {
        Comment comment = new Comment(text);
        this.comments.add(comment);
    }

    /**
     * Sets the student's full name.
     * 
     * @param fullName the new full name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Sets the student's academic status.
     * 
     * @param academicStatus the new academic status
     */
    public void setAcademicStatus(String academicStatus) {
        this.academicStatus = academicStatus;
    }

    /**
     * Sets the student's employment status.
     * 
     * @param employed true if the student is employed, false otherwise
     */
    public void setEmployed(boolean employed) {
        this.employed = employed;
    }

    /**
     * Sets details about the student's job.
     * 
     * @param jobDetails the job details, or null if not employed
     */
    public void setJobDetails(String jobDetails) {
        this.jobDetails = jobDetails;
    }

    /**
     * Sets the list of programming languages the student knows.
     * 
     * @param programmingLanguages the list of programming languages
     */
    public void setProgrammingLanguages(List<String> programmingLanguages) {
        this.programmingLanguages = programmingLanguages;
    }

    /**
     * Sets the list of databases the student is familiar with.
     * 
     * @param databasesKnown the list of databases known
     */
    public void setDatabasesKnown(List<String> databasesKnown) {
        this.databasesKnown = databasesKnown;
    }

    /**
     * Sets the student's preferred professional role.
     * 
     * @param preferredProfessionalRole the preferred professional role
     */
    public void setPreferredProfessionalRole(String preferredProfessionalRole) {
        this.preferredProfessionalRole = preferredProfessionalRole;
    }

    /**
     * Sets whether the student is on the whitelist.
     * 
     * @param whitelist true to whitelist the student, false otherwise
     */
    public void setWhitelisted(boolean whitelist) {
        this.whitelist = whitelist;
    }

    /**
     * Sets whether the student is on the blacklist.
     * 
     * @param blacklist true to blacklist the student, false otherwise
     */
    public void setBlacklisted(boolean blacklist) {
        this.blacklist = blacklist;
    }
}
