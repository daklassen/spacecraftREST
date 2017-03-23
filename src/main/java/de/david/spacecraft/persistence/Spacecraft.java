package de.david.spacecraft.persistence;

import javax.persistence.*;
import java.util.Date;

/**
 * @author David Klassen
 */
@Entity
public class Spacecraft {

    @Id
    @GeneratedValue
    private Long id;

    private String identification;
    private String captain;
    private Date comissioning;
    private boolean available;

    @Enumerated(EnumType.STRING)
    private SpacecraftType type;

    public Long getId() {
        return id;
    }

    public String getIdentification() {
        return identification;
    }

    public String getCaptain() {
        return captain;
    }

    public Date getComissioning() {
        return comissioning;
    }

    public boolean isAvailable() {
        return available;
    }

    public SpacecraftType getType() {
        return type;
    }

    Spacecraft() {
    }

    public Spacecraft(String identification, String captain,
                      Date comissioning, boolean available, SpacecraftType type) {
        this.identification = identification;
        this.captain = captain;
        this.comissioning = comissioning;
        this.available = available;
        this.type = type;
    }
}
