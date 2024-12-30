package fr.pantheonsorbonne.dto;

import java.util.Date;

public class CreateTrajetRequest {
    private String villeDepart;
    private String villeArrivee;
    private Date horaire;
    private Double prix;
    private Long conducteurId;

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

    public Date getHoraire() {
        return horaire;
    }

    public void setHoraire(Date horaire) {
        this.horaire = horaire;
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
}
