package rockets.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Sets;
import org.neo4j.ogm.annotation.CompositeIndex;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;
import static org.neo4j.ogm.annotation.Relationship.OUTGOING;

@NodeEntity
@CompositeIndex(properties = {"name", "yearFounded", "country"}, unique = true)
public class LaunchServiceProvider extends Entity {
    @Property(name = "name")
    private String name;

    @Property(name = "yearFounded")
    private int yearFounded;

    @Property(name = "country")
    private String country;

    @Property(name = "headquarters")
    private String headquarters;

    @Relationship(type = "MANUFACTURES", direction = OUTGOING)
    @JsonIgnore
    private Set<Rocket> rockets;

    public LaunchServiceProvider() {
        rockets = Sets.newLinkedHashSet();
    }

    public LaunchServiceProvider(String name, int yearFounded, String country) {
        notNull(name);
        notNull(country);
        if (!isYear(Integer.toString(yearFounded))) {
            throw new IllegalArgumentException("year founded is invalid");
        }
        this.name = name;
        this.yearFounded = yearFounded;
        this.country = country;

        rockets = Sets.newLinkedHashSet();
    }

    public String getName() {
        return name;
    }

    public int getYearFounded() {
        return yearFounded;
    }

    public String getCountry() {
        return country;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public Set<Rocket> getRockets() {
        return rockets;
    }

    public void setName(String name) {
        notBlank(name, "name cannot be null or empty");
        this.name = name;
    }

    public void setCountry(String country) {
        notBlank(country, "country name cannot be null or empty");
        this.country = country;
    }

    public void setHeadquarters(String headquarters) {
        notBlank(headquarters, "headquarters cannot be null or empty");
        this.headquarters = headquarters;
    }

    public void setYearFounded(String aYear) {
        notBlank(aYear, "year cannot be null or empty");
        if (isYear(aYear)) {
            int year = Integer.parseInt(aYear);
            yearFounded = year;
        } else {
            throw new IllegalArgumentException("year founded is invalid");
        }
    }

    /**
     * This method is used to validate a year is between 1900 and 2099.
     *
     * @param str
     * @return boolean
     */
    public boolean isYear(String str) {
        return str.trim().matches("^(19|20)\\d{2}$");
    }

    public void setRockets(Set<Rocket> rockets) {
        this.rockets = rockets;
    }

    public void addRocketToSet(Rocket aRocket) {
        for (Rocket rocket : rockets) {
            if (rocket.equals(aRocket)) {
                throw new IllegalArgumentException("rocket already exists in the set");
            }
        }
        rockets.add(aRocket);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LaunchServiceProvider that = (LaunchServiceProvider) o;
        return yearFounded == that.yearFounded &&
                Objects.equals(name, that.name) &&
                Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, yearFounded, country);
    }
}
