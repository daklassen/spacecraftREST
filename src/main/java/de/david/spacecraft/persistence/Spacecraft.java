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

    @ManyToOne
    private Captain captain;
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

    public Captain getCaptain() {
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

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public void setCaptain(Captain captain) {
        this.captain = captain;
    }

    public void setComissioning(Date comissioning) {
        this.comissioning = comissioning;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setType(SpacecraftType type) {
        this.type = type;
    }

    protected Spacecraft() {
    } // needed for JPA

    public Spacecraft(String identification, Captain captain,
                      Date comissioning, boolean available, SpacecraftType type) {
        this.identification = identification;
        this.captain = captain;
        this.comissioning = comissioning;
        this.available = available;
        this.type = type;
    }
}
