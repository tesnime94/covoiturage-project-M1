package fr.pantheonsorbonne.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "historique_recherche")
public class HistoriqueRecherche {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String villeDepart;

    @Column(nullable = false)
    private String villeArrivee;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date rechercheEffectueeLe;

    @OneToMany(mappedBy = "historiqueRecherche", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResultatRecherche> resultats = new ArrayList<>();

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

    public Date getRechercheEffectueeLe() {
        return rechercheEffectueeLe;
    }

    public void setRechercheEffectueeLe(Date rechercheEffectueeLe) {
        this.rechercheEffectueeLe = rechercheEffectueeLe;
    }

    public List<ResultatRecherche> getResultats() {
        return resultats;
    }

    public void setResultats(List<ResultatRecherche> resultats) {
        this.resultats = resultats;
    }
}
