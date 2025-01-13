package fr.pantheonsorbonne.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record SousTrajetDTO(
        Long id,
        String villeDepart,
        String villeArrivee,
        LocalDate date,
        LocalTime horaire
) {
}
