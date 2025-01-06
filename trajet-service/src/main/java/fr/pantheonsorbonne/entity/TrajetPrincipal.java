package fr.pantheonsorbonne.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trajet_principal")
public class TrajetPrincipal {
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

    @Column(nullable = false)
    private Double prix;

    public LocalTime getHoraire() {
        return horaire;
    }

    public void setHoraire(LocalTime horaire) {
        this.horaire = horaire;
    }

    public Integer getNbPlaces() {
        return nbPlaces;
    }

    public void setNbPlaces(Integer nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    @Column(nullable = false)
    private Integer nbPlaces;

    @Column(name = "conducteur_id", nullable = false)
    private Long conducteurId; // ID de l'utilisateur conducteur

    @OneToMany(mappedBy = "trajetPrincipal", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore // pour eviter vite d'inclure les sous trajets automatiquement
    private List<SousTrajet> sousTrajets = new ArrayList<>();

    // Getters et Setters
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

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public Long getConducteurId() {
        return conducteurId;
    }

    public void setConducteurId(Long conducteurId) {
        this.conducteurId = conducteurId;
    }

    public List<SousTrajet> getSousTrajets() {
        return sousTrajets;
    }

    public void setSousTrajets(List<SousTrajet> sousTrajets) {
        this.sousTrajets = sousTrajets;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
