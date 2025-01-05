package fr.pantheonsorbonne.dto;

import java.util.Date;

public record TrajetPrincipalDTO(Long id, String villeDepart, String villeArrivee, Date horaire, Double prix,
                                 Long conducteurId) {
}
