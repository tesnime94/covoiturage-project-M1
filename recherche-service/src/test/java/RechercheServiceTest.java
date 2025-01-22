import java.time.LocalDate;
import java.time.LocalTime;

import fr.pantheonsorbonne.dao.SousTrajetDAO;
import fr.pantheonsorbonne.dao.TrajetDAO;
import fr.pantheonsorbonne.entity.SousTrajet;
import fr.pantheonsorbonne.entity.Trajet;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

public class RechercheServiceTest {



    @Inject
    private TrajetDAO trajetDAO;

    @Inject
    private SousTrajetDAO sousTrajetDAO;
    @Transactional
public void create() {
    // Trajet 1 : Paris → Marseille (passe par Lyon)
    Trajet trajet1 = new Trajet();
    trajet1.setVilleDepart("Paris");
    trajet1.setVilleArrivee("Marseille");
    trajet1.setDate(LocalDate.of(2025, 1, 16)); // Même date
    trajet1.setHoraire(LocalTime.of(14, 30));  // Même heure
    trajet1.setNombreDePlaces(3);
    trajet1.setPlaceDisponible(3);
    trajet1.setPrix(50.0);
    trajet1.setConducteurMail("conducteur1@mail.com");
    trajetDAO.save(trajet1);

    // Sous-trajets pour Trajet 1
    SousTrajet sousTrajet1 = new SousTrajet();
    sousTrajet1.setVilleDepart("Paris");
    sousTrajet1.setVilleArrivee("Lyon"); // Lyon comme étape
    sousTrajet1.setDate(LocalDate.of(2025, 1, 16)); // Même date
    sousTrajet1.setTrajet(trajet1);
    sousTrajetDAO.save(sousTrajet1);

    SousTrajet sousTrajet2 = new SousTrajet();
    sousTrajet2.setVilleDepart("Lyon");
    sousTrajet2.setVilleArrivee("Marseille");
    sousTrajet2.setDate(LocalDate.of(2025, 1, 16)); // Même date
    sousTrajet2.setTrajet(trajet1);
    sousTrajetDAO.save(sousTrajet2);

    // Trajet 2 : Paris → Nice (passe par Lyon)
    Trajet trajet2 = new Trajet();
    trajet2.setVilleDepart("Paris");
    trajet2.setVilleArrivee("Nice");
    trajet2.setDate(LocalDate.of(2025, 1, 16)); // Même date
    trajet2.setHoraire(LocalTime.of(14, 30));  // Même heure
    trajet2.setNombreDePlaces(4);
    trajet2.setPlaceDisponible(4);
    trajet2.setPrix(70.0);
    trajet2.setConducteurMail("conducteur2@mail.com");
    trajetDAO.save(trajet2);

    // Sous-trajets pour Trajet 2
    SousTrajet sousTrajet3 = new SousTrajet();
    sousTrajet3.setVilleDepart("Paris");
    sousTrajet3.setVilleArrivee("Lyon"); // Lyon comme étape
    sousTrajet3.setDate(LocalDate.of(2025, 1, 16)); // Même date
    sousTrajet3.setTrajet(trajet2);
    sousTrajetDAO.save(sousTrajet3);

    SousTrajet sousTrajet4 = new SousTrajet();
    sousTrajet4.setVilleDepart("Lyon");
    sousTrajet4.setVilleArrivee("Nice");
    sousTrajet4.setDate(LocalDate.of(2025, 1, 16)); // Même date
    sousTrajet4.setTrajet(trajet2);
    sousTrajetDAO.save(sousTrajet4);

    // Trajet 3 : Bordeaux → Toulouse (ne passe pas par Lyon)
    Trajet trajet3 = new Trajet();
    trajet3.setVilleDepart("Bordeaux");
    trajet3.setVilleArrivee("Toulouse");
    trajet3.setDate(LocalDate.of(2025, 1, 18));
    trajet3.setHoraire(LocalTime.of(9, 0));
    trajet3.setNombreDePlaces(2);
    trajet3.setPlaceDisponible(2);
    trajet3.setPrix(40.0);
    trajet3.setConducteurMail("conducteur3@mail.com");
    trajetDAO.save(trajet3);

    // Sous-trajets pour Trajet 3
    SousTrajet sousTrajet5 = new SousTrajet();
    sousTrajet5.setVilleDepart("Bordeaux");
    sousTrajet5.setVilleArrivee("Agen"); // Ne passe pas par Lyon
    sousTrajet5.setDate(LocalDate.of(2025, 1, 18));
    sousTrajet5.setTrajet(trajet3);
    sousTrajetDAO.save(sousTrajet5);

    SousTrajet sousTrajet6 = new SousTrajet();
    sousTrajet6.setVilleDepart("Agen");
    sousTrajet6.setVilleArrivee("Toulouse");
    sousTrajet6.setDate(LocalDate.of(2025, 1, 18));
    sousTrajet6.setTrajet(trajet3);
    sousTrajetDAO.save(sousTrajet6);

    // Log pour confirmer
    System.out.println("Données initiales insérées dans la base de données avec deux trajets passant par Lyon à la même date et heure.");
}

    
       
    
    @Transactional
    public void clearDatabase() {
        System.out.println("Suppression de toutes les données dans la base de données...");

        // Supprimer tous les sous-trajets
        sousTrajetDAO.deleteAll();

        // Supprimer tous les trajets
        trajetDAO.deleteAll();

        System.out.println("Toutes les données ont été supprimées.");
    }
}

    


    

