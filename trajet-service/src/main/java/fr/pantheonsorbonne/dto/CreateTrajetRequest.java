package fr.pantheonsorbonne.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class CreateTrajetRequest {
    private String villeDepart;
    private String villeArrivee;
    private LocalDate date;
    private LocalTime horaire;
    private Integer nombreDePlaces;
    private Double prix;
    private String conducteurMail;

    // Getters et Setters
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

    public Integer getNombreDePlaces() {
        return nombreDePlaces;
    }

    public void setNombreDePlaces(Integer nombreDePlaces) {
        this.nombreDePlaces = nombreDePlaces;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
