package fr.pantheonsorbonne.dto;

public record RequeteDTO(
        String villeDepart,
        String villeArrivee,
        String date,
        String horaire,
        Double prix
) {
}
