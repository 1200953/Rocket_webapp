package rockets.model;

import org.neo4j.ogm.annotation.CompositeIndex;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;
import static org.neo4j.ogm.annotation.Relationship.INCOMING;

@NodeEntity
@CompositeIndex(properties = {"name", "country", "manufacturer"}, unique = true)
public class Rocket extends Entity {
    @Property(name = "name")
    private String name;

    @Property(name = "country")
    private String country;

    @Relationship(type = "MANUFACTURES", direction = INCOMING)
    private LaunchServiceProvider manufacturer;

    @Property(name = "massToLEO")
    private String massToLEO;

    @Property(name = "massToGTO")
    private String massToGTO;

    @Property(name = "massToOther")
    private String massToOther;

    public Rocket() {

    }

    /**
     * All parameters shouldn't be null.
     *
     * @param name
     * @param country
     * @param manufacturer
     */
    public Rocket(String name, String country, LaunchServiceProvider manufacturer) {
        notNull(name);
        notNull(country);
        notNull(manufacturer);

        this.name = name;
        this.country = country;
        this.manufacturer = manufacturer;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public LaunchServiceProvider getManufacturer() {
        return manufacturer;
    }

    public String getMassToLEO() {
        return massToLEO;
    }

    public String getMassToGTO() {
        return massToGTO;
    }

    public String getMassToOther() {
        return massToOther;
    }

    public void setName(String name) {
        notBlank(name, "name should not be null or empty");
        this.name = name;
    }

    public void setCountry(String country) {
        notBlank(country, "country should not be null or empty");
        this.country = country;
    }

    public void setManufacturer(LaunchServiceProvider manufacturer) {
        notNull(manufacturer, "manufacturer should not be null or empty");
        this.manufacturer = manufacturer;
    }


    public void setMassToLEO(String massToLEO) {
        if (massToLEO == null) {
            throw new NullPointerException("input cannot be null");
        }
        if (!isNumeric(massToLEO) || Integer.parseInt(massToLEO) < 0) {
            throw new IllegalArgumentException("input is not valid, non-negative numbers required");
        }

        this.massToLEO = massToLEO;
    }

    public void setMassToGTO(String massToGTO) {
        if (!isNumeric(massToGTO) || Integer.parseInt(massToGTO) < 0) {
            throw new IllegalArgumentException("input is not valid, non-negative numbers required");
        }
        this.massToGTO = massToGTO;
    }

    public void setMassToOther(String massToOther) {
        if (!isNumeric(massToOther) || Integer.parseInt(massToOther) < 0) {
            throw new IllegalArgumentException("input is not valid, non-negative numbers required");
        }
        this.massToOther = massToOther;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rocket rocket = (Rocket) o;
        return Objects.equals(name, rocket.name) &&
                Objects.equals(country, rocket.country) &&
                Objects.equals(manufacturer, rocket.manufacturer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, country, manufacturer);
    }

    @Override
    public String toString() {
        return "Rocket{" +
                "name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", massToLEO='" + massToLEO + '\'' +
                ", massToGTO='" + massToGTO + '\'' +
                ", massToOther='" + massToOther + '\'' +
                '}';
    }
}
