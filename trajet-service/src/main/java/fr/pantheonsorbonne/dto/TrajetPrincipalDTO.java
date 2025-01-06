package fr.pantheonsorbonne.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record TrajetPrincipalDTO(Long id, String villeDepart, String villeArrivee, LocalDate date, LocalTime horaire,
                                 Integer nombreDePlaces, Double prix, Long conducteurId) {
}

