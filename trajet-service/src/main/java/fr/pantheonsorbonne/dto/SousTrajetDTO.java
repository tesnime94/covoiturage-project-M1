package fr.pantheonsorbonne.dto;

public record SousTrajetDTO(
        Long id,
        String villeDepart,
        String villeArrivee,
        String date,
        String horaire
) {
}
