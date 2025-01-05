package fr.pantheonsorbonne.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "sous_trajet")
public class SousTrajet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String villeDepart;

    @Column(nullable = false)
    private String villeArrivee;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date horaire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore // pour eviter vite d'inclure les sous trajets automatiquement
    @JoinColumn(name = "trajet_principal_id", nullable = false)
    private TrajetPrincipal trajetPrincipal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVilleDepart() {
        return villeDepart;
    }

    public void setVilleDepart(String villeDepart) {
        this.villeDepart = villeDepart;
    }

    public String getVilleArrivee() {
        return villeArrivee;
    }

    public void setVilleArrivee(String villeArrivee) {
        this.villeArrivee = villeArrivee;
    }

    public Date getHoraire() {
        return horaire;
    }

    public void setHoraire(Date horaire) {
        this.horaire = horaire;
    }

    public TrajetPrincipal getTrajetPrincipal() {
        return trajetPrincipal;
    }

    public void setTrajetPrincipal(TrajetPrincipal trajetPrincipal) {
        this.trajetPrincipal = trajetPrincipal;
    }
}

