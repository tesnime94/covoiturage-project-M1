package fr.pantheonsorbonne.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "trajet")
public class Trajet {

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
    private Integer nombreDePlaces; // Valeur constante, ne change jamais

    @Column(nullable = false)
    private Integer placeDisponible; // Nombre de places restantes, mis à jour

    @Column(nullable = false)
    private Double prix;

    @Column(nullable = false)
    private String conducteurMail;

    @OneToMany()
    @JsonIgnore // pour eviter vite d'inclure les sous trajets automatiquement
    private List<SousTrajet> sousTrajets;

    // Getters et Setters

    public Integer getNombreDePlaces() {
        return nombreDePlaces;
    }

    public void setNombreDePlaces(Integer nombreDePlaces) {
        this.nombreDePlaces = nombreDePlaces;
        this.placeDisponible = nombreDePlaces;
    }

    public Integer getPlaceDisponible() {
        return placeDisponible;
    }

    public void setPlaceDisponible(Integer placeDisponible) {
        this.placeDisponible = placeDisponible;
    }

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getHoraire() {
        return horaire;
    }

    public void setHoraire(LocalTime horaire) {
        this.horaire = horaire;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public String getConducteurMail() {
        return conducteurMail;
    }

    public void setConducteurMail(String conducteurMail) {
        this.conducteurMail = conducteurMail;
    }

    public List<SousTrajet> getSousTrajets() {
        return sousTrajets;
    }

    public void setSousTrajets(List<SousTrajet> sousTrajets) {
        this.sousTrajets = sousTrajets;
    }


}
