import java.util.ArrayList;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

class User {
    public String name;
    private byte[] passwordHash;

    User(String name, String password) {
        this.name = name;
        this.passwordHash = getSHA(password);
    }

    private byte[] getSHA(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(input.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkPassword(String password) {
        byte[] hashedPassword = getSHA(password);
        return Arrays.equals(passwordHash, hashedPassword);
    }

    public boolean checkName(String name) {
        return this.name.equals(name);
    }
}

class UserHandler {
    private final ArrayList<User> users;

    UserHandler() {
        users = new ArrayList<User>();
    }

    public boolean register(String name, String password) {
        for (User user : users) {
            if (user.checkName(name)) {
                System.out.println("User exists");
                return false;
            }
        }
        users.add(new User(name, password));
        System.out.printf("Added user: %s%n", name);
        return true;
    }

    public boolean login(String name, String password) {
        for (User user : users) {
            if (user.checkName(name)) {
                if (user.checkPassword(password)) {
                    System.out.println("Login successful");
                    return true;
                } else {
                    System.out.println("Invalid password");
                    return false;
                }
            }
        }
        System.out.println("User not found");
        return false;
    }

    public User get(String name) {
        for (User user : users) {
            if (user.checkName(name)) {
                return user;
            }
        }
        System.out.println("No such user exists");
        return null;
    }
}
