package fr.pantheonsorbonne.dto;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

// Ce DTO sert lors de la récupération du trajet du microservice trajet

public record TrajetCompletDTO(
        Long id,
        String villeDepart,
        String villeArrivee,
        LocalDate date,
        LocalTime horaire,
        Integer nombreDePlaces,
        Integer placeDisponible,
        Double prix,
        String conducteurMail,
        List<SousTrajetDTO> sousTrajets //  Les Sous-trajets associés au trajet
) {
}


