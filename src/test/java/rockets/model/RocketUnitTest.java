package rockets.model;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class RocketUnitTest {
    private Rocket target;


    @BeforeEach
    public void setUp() {
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("Manufacturer A", 2018,
                "Australia");
        target = new Rocket("Leo", "US", manufacturer);
    }

    @AfterEach
    public void tearDown() {
    }

    @DisplayName("should create rocket successfully when given right parameters to constructor")
    @Test
    public void shouldConstructRocketObject() {
        String name = "BFR";
        String country = "USA";
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("SpaceX", 2002, "USA");
        Rocket bfr = new Rocket(name, country, manufacturer);
        assertNotNull(bfr);
    }

//    @DisplayName("should throw exception when given null manufacturer to constructor")
//    @Test
//    public void shouldThrowExceptionWhenNoManufacturerGiven() {
//        String name = "BFR";
//        String country = "USA";
//        assertThrows(NullPointerException.class, () -> new Rocket(name, country, null));
//    }

    @DisplayName("should throw exception when given null name to constructor")
    @Test
    public void shouldThrowExceptionWhenNoNameGiven() {
        String country = "USA";
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("SpaceX", 2002, "USA");
        assertThrows(NullPointerException.class, () -> new Rocket(null, country, manufacturer));
    }

    @DisplayName("should throw exception when given null country to constructor")
    @Test
    public void shouldThrowExceptionWhenNoCountryGiven() {
        String name = "BFR";
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("SpaceX", 2002, "USA");
        assertThrows(NullPointerException.class, () -> new Rocket(name, null, manufacturer));
    }

    @DisplayName("should set rocket massToLEO value")
    @ParameterizedTest
    @ValueSource(strings = {"10000", "15000"})
    public void shouldSetMassToLEOWhenGivenCorrectValue(String massToLEO) {
        String name = "BFR";
        String country = "USA";
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("SpaceX", 2002, "USA");

        Rocket bfr = new Rocket(name, country, manufacturer);

        bfr.setMassToLEO(massToLEO);
        assertEquals(massToLEO, bfr.getMassToLEO());
    }

    @DisplayName("should throw exception when set massToLEO to null")
    @Test
    public void shouldThrowExceptionWhenSetMassToLEOToNull() {
        String name = "BFR";
        String country = "USA";
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("SpaceX", 2002, "USA");
        Rocket bfr = new Rocket(name, country, manufacturer);
        assertThrows(NullPointerException.class, () -> bfr.setMassToLEO(null));
    }

    @DisplayName("should throw exception when pass null to setName function")
    @Test
    public void shouldThrowExceptionWhenSetRocketNameToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setName(null));
        assertEquals("name should not be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when set rocket name to empty")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetRocketNameToEmpty(String aName) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setName(aName));
        assertEquals("name should not be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when set manufacturer to null")
    @Test
    public void shouldThrowExceptionWhenSetManufacturerToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setManufacturer(null));
        assertEquals("manufacturer should not be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when set country name to null")
    @Test
    public void shouldThrowExceptionWhenSetCountryToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setCountry(null));
        assertEquals("country should not be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when set country name to empty")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSeCountryToEmpty(String aName) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> target.setCountry(aName));
        assertEquals("country should not be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when MassToLEO set to invalid number")
    @ParameterizedTest
    @ValueSource(strings = {"Ray", "-98", "", " "})
    void shouldThrowExceptionWhenSetMassToLEOToNonPositiveNumber(String mass) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> target.setMassToLEO(mass));
        assertEquals("input is not valid, non-negative numbers required", exception.getMessage());
    }

    @DisplayName("should throw exception when MassToGTO set to invalid number")
    @ParameterizedTest
    @ValueSource(strings = {"Ray", "-98", "", " "})
    void shouldThrowExceptionWhenSetMassToGTOToNonPositiveNumber(String mass) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> target.setMassToGTO(mass));
        assertEquals("input is not valid, non-negative numbers required", exception.getMessage());
    }

    @DisplayName("should  throw exception when MassToOther set to invalid number")
    @ParameterizedTest
    @ValueSource(strings = {"Ray", "-98", "", " "})
    void shouldThrowExceptionWhenSetMassToOtherToNonPositiveNumber(String mass) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> target.setMassToOther(mass));
        assertEquals("input is not valid, non-negative numbers required", exception.getMessage());
    }

    @DisplayName("should return false when two rockets have same name")
    @Test
    public void shouldReturnFalseWhenTwoRocketsHaveSameName() {
        LaunchServiceProvider manufacturerB = new LaunchServiceProvider("Manufacturer B", 2018,
                "Australia");
        Rocket AnotherRocket = new Rocket("Leo", "UK", manufacturerB);
        assertFalse(target.equals(AnotherRocket));
    }
}