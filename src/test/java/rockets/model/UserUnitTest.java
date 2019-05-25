package rockets.model;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class UserUnitTest {
    private User target;

    @BeforeEach
    public void setUp() {
        target = new User("first", "last", "Abc12345678", "abc@gmail.com");
    }

    @AfterEach
    public void tearDown() {

    }

    @DisplayName("should create user successfully when given right parameters to constructor")
    @Test
    public void shouldConstructorUserObject() {
        String aFirstName = "First";
        String aLastName = "Last";
        String aPassword = "Password123";
        String aEmail = "email@gmail.com";
        User testUser = new User(aFirstName, aLastName, aPassword, aEmail);
        assertNotNull(testUser);
    }

    @DisplayName("should throw exception when given invalid email to constructor")
    @Test
    public void shouldThrowExceptionWhenInvalidEmailGiven() {
        String aFirstName = "First";
        String aLastName = "Last";
        String aPassword = "Password123";
        assertThrows(IllegalArgumentException.class, () -> new User(aFirstName, aLastName, aPassword, "abc@"));
    }

    @DisplayName("should throw exception when given invalid last name to constructor")
    @Test
    public void shouldThrowExceptionWhenInvalidLastNameGiven() {
        String aEmail = "email@gmail.com";
        String aPassword = "Password123";
        assertThrows(IllegalArgumentException.class, () -> new User("first", " last", aPassword,
                aEmail));
    }

    @DisplayName("should throw exception when given invalid password to constructor")
    @Test
    public void shouldThrowExceptionWhenInvalidPasswordGiven() {
        String aFirstName = "First";
        String aLastName = "Last";
        String aEmail = "email@gmail.com";
        assertThrows(IllegalArgumentException.class, () -> new User(aFirstName, aLastName, "1234", aEmail));
    }

    @DisplayName("should throw exception when given null last name to constructor")
    @Test
    public void shouldThrowExceptionWhenNoLastNameGiven() {
        String aFirstName = "first";
        String aPassword = "Password123";
        String aEmail = "email@gmail.com";
        assertThrows(NullPointerException.class, () -> new User( aFirstName,null, aPassword, aEmail));
    }

    @DisplayName("should throw exception when given null password to constructor")
    @Test
    public void shouldThrowExceptionWhenNoPasswordGiven() {
        String aFirstName = "First";
        String aLastName = "Last";
        String aEmail = "email@gmail.com";
        assertThrows(NullPointerException.class, () -> new User(aFirstName, aLastName, null, aEmail));
    }

    @DisplayName("should throw exception when given null email to constructor")
    @Test
    public void shouldThrowExceptionWhenNoEmailGiven() {
        String aFirstName = "First";
        String aLastName = "Last";
        String aPassword = "Password123";
        assertThrows(NullPointerException.class, () -> new User(aFirstName, aLastName, aPassword, null));
    }

    @DisplayName("should return true when passwords are matched")
    @Test
    public void shouldReturnTrueWhenPasswordsAreSame() {
        String aPassword = "Abc123123";
        target.setPassword(aPassword);
        assertTrue(target.isPasswordMatch(aPassword));
    }

    @DisplayName("should return false when passwords are not matched")
    @Test
    public void shouldReturnFalseWhenPasswordsAreNotSame() {
        String aPassword = "Abc123123";
        target.setPassword(aPassword);
        assertFalse(target.isPasswordMatch("abc123123"));
    }

    @DisplayName("should throw exception when pass a empty email address to setEmail function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetEmailToEmpty(String email) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setEmail(email));
        assertEquals("email cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setEmail function")
    @Test
    public void shouldThrowExceptionWhenSetEmailToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setEmail(null));
        assertEquals("email cannot be null or empty", exception.getMessage());
    }


    @DisplayName("should throw exceptions when pass a null password to setPassword function")
    @Test
    public void shouldThrowExceptionWhenSetPasswordToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setPassword(null));
        assertEquals("password cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a empty password to setPassword function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetPasswordToEmpty(String password) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> target.setPassword(password));
        assertEquals("password cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should return true when two users have the same email")
    @Test
    public void shouldReturnTrueWhenUsersHaveSameEmail() {
        String email = "abc@example.com";
        target.setEmail(email);
        User anotherUser = new User("first", "last", "Abc123123", "tester@gmail.com");
        anotherUser.setEmail(email);
        assertTrue(target.equals(anotherUser));
    }

    @DisplayName("should return false when two users have different emails")
    @Test
    public void shouldReturnFalseWhenUsersHaveDifferentEmails() {
        target.setEmail("abc@example.com");
        User anotherUser = new User("first", "last", "Abc123123", "tester@gmail.com");
        anotherUser.setEmail("def@example.com");
        assertFalse(target.equals(anotherUser));
    }


    @DisplayName("should return true when the email address is valid")
    @Test
    public void shouldReturnTrueWhenEmailAddressIsValid() {
        String email = "abc@example.com";

        assertTrue(target.isEmailValid(email));
    }

    @DisplayName("should return false when name is greater than 20 characters")
    @ParameterizedTest
    @ValueSource(strings = {"aaaaabbbbbcccccddddde"})
    public void shouldReturnFalseWhenNameIsGreaterThanTwentyCharacters(String name) {
        assertFalse(target.isNameValid(name));
    }

    @DisplayName("should return true when name is not greater than 20 characters")
    @ParameterizedTest
    @ValueSource(strings = {"Test name", "a", "aaaaabbbbbccccceeeee"})
    public void shouldReturnTrueWhenNameIsNotGreaterThanTwentyCharacters(String name) {
        assertTrue(target.isNameValid(name));
    }

    @DisplayName("should return false when the first or last character of name is whitespace")
    @ParameterizedTest
    @ValueSource(strings = {" Tester Name", " Tester Name ", "Tester Name "})
    public void shouldReturnFalseWhenTheFirstOrLastCharacterOfNameIsWhitespace(String name) {
        assertFalse(target.isNameValid(name));
    }

    @DisplayName("should return true when the first or last character of name is not whitespace")
    @ParameterizedTest
    @ValueSource(strings = {"Tester Name"})
    public void shouldReturnTrueWhenTheFirstOrLastCharacterOfNameIsNotWhitespace(String name) {
        assertTrue(target.isNameValid(name));
    }

    @DisplayName("should return false when the email address is invalid")
    @ParameterizedTest
    @ValueSource(strings = {"abs@", "a@@.com", "ab1@.com"})
    public void shouldReturnFalseWhenEmailAddressIsInvalid(String email) {
        assertFalse(target.isEmailValid(email));
    }

    @DisplayName("should throw exception when pass an empty first name to setFirstName function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetFirstNameToEmpty(String firstName) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> target.setFirstName(firstName));
        assertEquals("first name should not be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setFirstName function")
    @Test
    public void shouldThrowExceptionWhenSetFirstNameToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setFirstName(null));
        assertEquals("first name should not be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass an empty last name to setLastName function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetLastNameToEmpty(String lastName) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> target.setLastName(lastName));
        assertEquals("last name should not be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setLastName function")
    @Test
    public void shouldThrowExceptionWhenSetLastNameToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setLastName(null));
        assertEquals("last name should not be null or empty", exception.getMessage());
    }

    @DisplayName("should return true when the password is valid")
    @ParameterizedTest
    @ValueSource(strings = {"123abbc2", "12345678901234567890"})
    public void shouldReturnTrueWhenPasswordIsValid(String password) {
        assertTrue(target.isPasswordValid(password));
    }

    @DisplayName("should return false when the password is invalid")
    @ParameterizedTest
    @ValueSource(strings = {"1234", "abschdty", "123 fgfg", "123456789012345678901"})
    public void shouldReturnFalseWhenPasswordIsInvalid(String password) {
        assertFalse(target.isPasswordValid(password));
    }
}