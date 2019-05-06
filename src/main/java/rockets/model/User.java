package rockets.model;

import org.neo4j.ogm.annotation.NodeEntity;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

@NodeEntity
public class User extends Entity {
    private String firstName;

    private String lastName;

    private String email;

    private String password;

    public User(String lastName, String password, String email) {
        notNull(lastName);
        notNull(password);
        notNull(email);
        if (!isEmailValid(email)) {
            throw new IllegalArgumentException("email is not valid.");
        }
        if (!isPasswordValid(password)) {
            throw new IllegalArgumentException("password is not valid.");
        }
        if (!isNameValid(lastName)) {
            throw new IllegalArgumentException("last name is not valid.");
        }
        this.lastName = lastName;
        this.password = password;
        this.email = email;
    }

    public User() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        notBlank(firstName, "first name should not be null or empty");
        if (isNameValid(firstName)) {
            this.firstName = firstName;
        }
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        notBlank(lastName, "last name should not be null or empty");
        if (isNameValid(lastName)) {
            this.lastName = lastName;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        notBlank(email, "email cannot be null or empty");
        if (isEmailValid(email))
            this.email = email;
    }

    /**
     * The name should be within 20 characters and the first and last character
     * should not be whitespace.
     *
     * @param aName
     * @return
     */
    public boolean isNameValid(String aName) {
        return aName.trim().length() <= 20 && aName.charAt(0) != ' ' && aName.charAt(aName.length() - 1) != ' ';

    }

    public boolean isEmailValid(String address) {
        String regex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(address);
        return m.matches();
    }

    //a digit occur at least once
    //no whitespace allowed in the password
    //at least 8 characters are needed
    //maximum 20 characters are accepted
    public boolean isPasswordValid(String password) {
        String regex = "^(?=.*[0-9])(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        return m.matches();

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        notBlank(password, "password cannot be null or empty");
        if (isPasswordValid(password))
            this.password = password;
    }

    // match the given password against user's password and return the result
    public boolean isPasswordMatch(String password) {

        return this.password.equals(password.trim());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
