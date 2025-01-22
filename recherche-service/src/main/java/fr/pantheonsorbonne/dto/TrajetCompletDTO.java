package fr.pantheonsorbonne.dto;


import java.util.List;

// Ce DTO sert lors de la récupération du trajet du microservice trajet

public record TrajetCompletDTO(
        Long id,
        String villeDepart,
        String villeArrivee,
        String date,
        String horaire,
        Integer nombreDePlaces,
        Integer placeDisponible,
        Double prix,
        String conducteurMail,
        List<SousTrajetDTO> sousTrajets //  Les Sous-trajets associés au trajet
) {
}


