package fr.pantheonsorbonne.dto;

import java.time.LocalDate;
import java.time.LocalTime;

// Ce Dto sert dans la liste de résultat affiché à l'utilisateur, avec des infos en moins qui ne sont pas nécessaire
public record TrajetDTO(
        Long id,
        String villeDepart,
        String villeArrivee,
        LocalDate date,
        LocalTime horaire,
        Integer placeDisponible,
        Double prix,
        String conducteurMail

) {
}
