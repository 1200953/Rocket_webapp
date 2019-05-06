package rockets.model;

import org.neo4j.ogm.annotation.CompositeIndex;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.Validate.notNull;
import static org.neo4j.ogm.annotation.Relationship.INCOMING;

@NodeEntity
@CompositeIndex(properties = {"launchDate", "launchVehicle", "launchSite", "orbit"}, unique = true)
public class Launch extends Entity {
    public enum LaunchOutcome {
        FAILED, SUCCESSFUL
    }


    private LocalDate launchDate;

    @Relationship(type = "PROVIDES", direction = INCOMING)
    private Rocket launchVehicle;

    private LaunchServiceProvider launchServiceProvider;

    private Set<String> payload;

    @Property(name = "launchSite")
    private String launchSite;

    @Property(name = "orbit")
    private String orbit;

    @Property(name = "function")
    private String function;

    @Property(name = "price")
    private BigDecimal price;

    @Property(name = "launchOutcome")
    private LaunchOutcome launchOutcome;

    public Launch(LocalDate launchDate, Rocket launchVehicle, String launchSite, String orbit, LaunchOutcome launchOutcome, Set<String> payload) {
        notNull(launchDate);
        notNull(launchVehicle);
        notNull(launchSite);
        notNull(orbit);
        notNull(launchOutcome);
        notNull(payload);

        this.launchDate = launchDate;
        this.launchVehicle = launchVehicle;
        this.launchSite = launchSite;
        this.orbit = orbit;
        this.launchOutcome = launchOutcome;
        this.payload = payload;
    }

    public Launch() {

    }

    public LocalDate getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(LocalDate launchDate) {
        notNull(launchDate, "cannot be null");
        this.launchDate = launchDate;
    }

    public Rocket getLaunchVehicle() {
        return launchVehicle;
    }

    public void setLaunchVehicle(Rocket launchVehicle) {
        notNull(launchVehicle, "cannot be null");
        this.launchVehicle = launchVehicle;
    }

    public LaunchServiceProvider getLaunchServiceProvider() {
        return launchServiceProvider;
    }

    public void setLaunchServiceProvider(LaunchServiceProvider launchServiceProvider) {
        notNull(launchServiceProvider, "cannot be null");
        this.launchServiceProvider = launchServiceProvider;
    }

    public Set<String> getPayload() {
        return payload;
    }

    public void setPayload(Set<String> payload) {
        notNull(payload, "cannot be null");
        this.payload = payload;
    }

    public String getLaunchSite() {
        return launchSite;
    }

    public void setLaunchSite(String launchSite) {
        notNull(launchSite, "cannot be null");
        this.launchSite = launchSite;
    }

    public String getOrbit() {
        return orbit;
    }

    public void setOrbit(String orbit) {

        notNull(orbit, "cannot be null");
        this.orbit = orbit;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        notNull(price, "cannot be null");
        this.price = price;
    }

    public LaunchOutcome getLaunchOutcome() {
        return launchOutcome;
    }

    public void setLaunchOutcome(LaunchOutcome launchOutcome) {
        notNull(launchOutcome, "cannot be null");
        this.launchOutcome = launchOutcome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Launch launch = (Launch) o;
        return Objects.equals(launchDate, launch.launchDate) &&
                Objects.equals(launchVehicle, launch.launchVehicle) &&
                Objects.equals(launchServiceProvider, launch.launchServiceProvider) &&
                Objects.equals(orbit, launch.orbit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(launchDate, launchVehicle, launchServiceProvider, orbit);
    }
}
