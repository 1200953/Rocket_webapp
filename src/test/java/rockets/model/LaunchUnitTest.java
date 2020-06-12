package rockets.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
//test
public class LaunchUnitTest {
    private Launch newLaunch;

    @BeforeEach
    public void setUp() {
        this.newLaunch = new Launch();
    }

    @Test
    public void launchDateCanNotBeSetToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> newLaunch.setLaunchDate(null));
        assertEquals("cannot be null", exception.getMessage());
    }

    @Test
    public void getLaunchDateShouldEqualSetLaunchDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse("25/04/2019", formatter);
        newLaunch.setLaunchDate(date);
        LocalDate date2 = LocalDate.parse("25/04/2019", formatter);
        assertEquals(date2, newLaunch.getLaunchDate());
    }

    @Test
    public void launchServiceProviderCanNotBeSetToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> newLaunch.setLaunchServiceProvider(null));
        assertEquals("cannot be null", exception.getMessage());
    }

    @Test
    public void launchPayloadCanNotBeSetToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> newLaunch.setPayload(null));
        assertEquals("cannot be null", exception.getMessage());
    }

    @Test
    public void testGetPayloadShouldEqualSetPayload() {
        Set<String> payload = new HashSet<>();
        payload.add("1t");
        payload.add("2t");
        newLaunch.setPayload(payload);
        assertEquals(payload, newLaunch.getPayload());
    }

    @Test
    public void launchSiteCanNotBeSetToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> newLaunch.setLaunchSite(null));
        assertEquals("cannot be null", exception.getMessage());
    }

    @Test()
    public void getLaunchSiteShouldEqualSetLaunchSite() {
        String launchSite = "China";
        newLaunch.setLaunchSite(launchSite);
        String launchSite2 = "China";
        assertEquals(launchSite2, newLaunch.getLaunchSite());
    }

    @Test
    public void orbitCanNotBeSetToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> newLaunch.setOrbit(null));
        assertEquals("cannot be null", exception.getMessage());
    }

    @Test
    public void getOrbitShouldEqualSetOrbit() {
        String orbit = "Round";
        newLaunch.setOrbit(orbit);
        assertEquals(orbit, newLaunch.getOrbit());

    }

    @Test
    public void launchOutcomeCanNotBeSetToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> newLaunch.setLaunchOutcome(null));
        assertEquals("cannot be null", exception.getMessage());
    }

    @Test
    public void getLaunchOutcomeShouldEqualSetLaunchOutcome() {
        Launch.LaunchOutcome outcome = Launch.LaunchOutcome.FAILED;
        newLaunch.setLaunchOutcome(outcome);
        assertEquals(outcome, newLaunch.getLaunchOutcome());
    }

    @Test
    public void priceCanNotBeSetToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> newLaunch.setPrice(null));
        assertEquals("cannot be null", exception.getMessage());
    }

    @Test
    public void getPriceShouldEqualSetPrice() {
        BigDecimal price = new BigDecimal("10000");
        newLaunch.setPrice(price);
        assertEquals(price, newLaunch.getPrice());
    }
}


