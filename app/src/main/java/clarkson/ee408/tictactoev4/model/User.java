package clarkson.ee408.tictactoev4.model;

/**
 * Model class for a user that plays TicTacToe game
 *
 */
public class User {
    /**
     * User's unique username
     */
    private String username;

    /**
     * User's login password
     */
    private String password;

    /**
     * User's display name
     */
    private String displayName;

    /**
     * If the user is connected to the server
     */
    private boolean online;


    /**
     * Default constructor
     */
    public User() {
    }

    /**
     *
     * @param username User's unique username
     * @param password User's login password
     * @param displayName User's display name
     * @param online If the user is connected to the server
     */
    public User(String username, String password, String displayName, boolean online) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.online = online;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Getter function for {@link #username} attribute
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter function for {@link #username} attribute
     * @param username User's unique username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter function for {@link #password} attribute
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter function for {@link #password} attribute
     * @param password User's login password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter function for {@link #displayName} attribute
     * @return displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Setter function for {@link #displayName} attribute
     * @param displayName User's display name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Getter function for {@link #online} attribute
     * @return online
     */
    public Boolean isOnline() {
        return online;
    }

    /**
     * Setter function for {@link #online} attribute
     * @param online If the user is connected to the server
     */
    public void setOnline(boolean online) {
        this.online = online;
    }


    /**
     *
     * @param obj instance of the other User object
     * @return true if both objects have equal {@link #username}
     */
    @Override
    public boolean equals(Object obj) {
        try {
            User other = (User) obj;
            return this.username.equals(other.getUsername());
        } catch (ClassCastException e) {
            return false;
        }
    }
}
