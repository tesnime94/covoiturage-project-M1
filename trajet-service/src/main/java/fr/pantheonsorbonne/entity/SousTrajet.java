package fr.pantheonsorbonne.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

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

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime horaire;

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

    public LocalTime getHoraire() {
        return horaire;
    }

    public void setHoraire(LocalTime horaire) {
        this.horaire = horaire;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TrajetPrincipal getTrajetPrincipal() {
        return trajetPrincipal;
    }

    public void setTrajetPrincipal(TrajetPrincipal trajetPrincipal) {
        this.trajetPrincipal = trajetPrincipal;
    }
}

