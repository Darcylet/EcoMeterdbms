package models;

public class User {
    private final int id;
    private final String username;
    private final String password;
    private final String email;
    private int houseSquareFootage;

    public User(int id, String username, String password, String email, int houseSquareFootage) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.houseSquareFootage = houseSquareFootage;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public int getHouseSquareFootage() { return houseSquareFootage; }

    public void setHouseSquareFootage(int houseSquareFootage) {
        this.houseSquareFootage = houseSquareFootage;
    }
}
