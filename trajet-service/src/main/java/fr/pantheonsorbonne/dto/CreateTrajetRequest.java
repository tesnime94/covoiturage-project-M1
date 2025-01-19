package fr.pantheonsorbonne.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateTrajetRequest(
        String villeDepart,
        String villeArrivee,
        LocalDate date,
        LocalTime horaire,
        Integer nombreDePlaces,
        Double prix,
        String conducteurMail
) {
}
