package rockets.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class LaunchServiceProviderTest {
    private LaunchServiceProvider provider;

    @BeforeEach
    public void setUp() {
        this.provider = new LaunchServiceProvider("Manufacturer A", 2019, "Australia");
    }

    @AfterEach
    public void tearDown() {

    }

    @DisplayName("should create launch service provider successfully when given right parameters to constructor")
    @Test
    public void shouldConstructorLaunchServiceProviderObject() {
        String aName = "provider A";
        int aYear = 2019;
        String aCountry = "Australia";
        LaunchServiceProvider provider = new LaunchServiceProvider(aName, aYear, aCountry);
        assertNotNull(provider);
    }

    @DisplayName("should throw exception when given invalid year to constructor")
    @Test
    public void shouldThrowExceptionWhenInvalidYearGiven() {
        String aName = "provider A";
        String aCountry = "Australia";
        assertThrows(IllegalArgumentException.class, () -> new LaunchServiceProvider(aName, 200, aCountry));
    }

    @DisplayName("should throw exception when given null name to constructor")
    @Test
    public void shouldThrowExceptionWhenNoNameGiven() {
        int aYear = 2019;
        String aCountry = "Australia";
        assertThrows(NullPointerException.class, () -> new LaunchServiceProvider(null, aYear, aCountry));
    }

    @DisplayName("should throw exception when given null country to constructor")
    @Test
    public void shouldThrowExceptionWhenNoCountryGiven() {
        String aName = "provider A";
        int aYear = 2019;
        assertThrows(NullPointerException.class, () -> new LaunchServiceProvider(aName, aYear, null));
    }

//    @DisplayName("should throw exception when the duplicated rocket is added to launch service provider")
//    @Test
//    public void shouldThrowExceptionWhenAddingSameRocketToSet() {
//
//        Rocket newRocekt = new Rocket("Rocket A", "Australia", manufacturer)
//    }

    @DisplayName("should throw exception when pass a empty to setName function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetServiceProviderNameToEmpty(String aName) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> provider.setName(aName));
        assertEquals("name cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setName function")
    @Test
    public void shouldThrowExceptionWhenSetServiceProviderNameToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> provider.setName(null));
        assertEquals("name cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a empty to setCountry function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetProviderCountryToEmpty(String aCountryName) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> provider.setCountry(aCountryName));
        assertEquals("country name cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setCountry function")
    @Test
    public void shouldThrowExceptionWhenSetProviderCountryNameToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> provider.setCountry(null));
        assertEquals("country name cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a empty headquarters name to setHeadquarters function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetHeadquartersToEmpty(String aHeadquarters) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> provider.setHeadquarters(aHeadquarters));
        assertEquals("headquarters cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a null headquarters name to setHeadquarters function")
    @Test
    public void shouldThrowExceptionWhenSetHeadquartersToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> provider.setHeadquarters(null));
        assertEquals("headquarters cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setYearFounded function")
    @Test
    public void shouldThrowExceptionWhenSetFoundedYearToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> provider.setYearFounded(null));
        assertEquals("year cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a empty to setYearFounded function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetFoundedYearToEmpty(String aYear) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> provider.setYearFounded(aYear));
        assertEquals("year cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should return false when year is not numeric")
    @ParameterizedTest
    @ValueSource(strings = {"abcd", "a018", "201c"})
    public void shouldReturnFalseWhenInputIsNotNumeric(String aYear) {
        assertFalse(provider.isYear(aYear));
    }

    @DisplayName("should return false when year is negative")
    @ParameterizedTest
    @ValueSource(strings = {"-1900", "-2099", "-2018"})
    public void shouldReturnFalseWhenInputIsNegative(String aYear) {
        assertFalse(provider.isYear(aYear));
    }

    @DisplayName("should return false when year is not four digits")
    @ParameterizedTest
    @ValueSource(strings = {"201", "20 8", "02019"})
    public void shouldReturnFalseWhenInputIsNotFourDigits(String aYear) {
        assertFalse(provider.isYear(aYear));
    }

    @DisplayName("should return false when year is not within 1900 - 2099")
    @ParameterizedTest
    @ValueSource(strings = {"1899", "2100"})
    public void shouldReturnFalseWhenInputIsWithinRange(String aYear) {
        assertFalse(provider.isYear(aYear));
    }

    @DisplayName("should return true when year is four digits, positive numbers and between 1900 and 2099")
    @ParameterizedTest
    @ValueSource(strings = {"1900", "2019", "2099"})
    public void shouldReturnTrueWhenInputIsValidated(String aYear) {
        assertTrue(provider.isYear(aYear));
    }

    @DisplayName("should return True when two service provider have the same name, founded year and headquarters")
    @Test
    public void shouldReturnTrueWhenLaunchServiceProviderHaveSameNameAndFoundedYearAndCountry() {
        String name = "Test provider";
        String year = "2019";
        String country = "Test country";
        provider.setName(name);
        provider.setCountry(country);
        provider.setYearFounded(year);

        LaunchServiceProvider anotherProvider = new LaunchServiceProvider("Manufacturer b", 2019,
                "China");
        anotherProvider.setName(name);
        anotherProvider.setCountry(country);
        anotherProvider.setYearFounded(year);
        assertTrue(provider.equals(anotherProvider));
    }

    @DisplayName("should return false when two service provider have the different founded year")
    @Test
    public void shouldReturnFalseWhenLaunchServiceProviderHaveDifferentFoundedYear() {
        provider.setYearFounded("2019");
        LaunchServiceProvider anotherProvider = new LaunchServiceProvider("Manufacturer b", 2019,
                "China");
        anotherProvider.setYearFounded("2018");
        assertFalse(provider.equals(anotherProvider));
    }

    @DisplayName("should return false when two service provider have the different Name")
    @Test
    public void shouldReturnFalseWhenLaunchServiceProviderHaveDifferentName() {
        provider.setName("Tester A");
        LaunchServiceProvider anotherProvider = new LaunchServiceProvider("Manufacturer b", 2019,
                "China");
        anotherProvider.setName("Tester B");
        assertFalse(provider.equals(anotherProvider));
    }

    @DisplayName("should return false when two service provider have the different country")
    @Test
    public void shouldReturnFalseWhenLaunchServiceProviderHaveDifferentCountry() {
        provider.setCountry("China");
        LaunchServiceProvider anotherProvider = new LaunchServiceProvider("Manufacturer b", 2019,
                "China");
        anotherProvider.setCountry("Australia");
        assertFalse(provider.equals(anotherProvider));
    }
}
