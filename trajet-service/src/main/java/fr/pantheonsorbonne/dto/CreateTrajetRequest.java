package fr.pantheonsorbonne.dto;

public record CreateTrajetRequest(
        String villeDepart,
        String villeArrivee,
        String date,
        String horaire,
        Integer nombreDePlaces,
        Double prix,
        String conducteurMail
) {
}
