package de.david.spacecraft.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * @author David Klassen
 */
@Entity
public class Captain {

    @Id
    @GeneratedValue
    private Long id;

    private String firstName;
    private String lastName;

    @JsonIgnore
    @OneToMany(mappedBy = "captain")
    private Set<Spacecraft> spacecrafts = new HashSet<>();

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Set<Spacecraft> getSpacecrafts() {
        return spacecrafts;
    }

    Captain() {
    } // needed for JPA

    public Captain(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
