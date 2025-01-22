package fr.pantheonsorbonne.dto;

public record TrajetPrincipalDTO(Long id, String villeDepart, String villeArrivee, String date, String horaire,
                                 Integer nombreDePlaces, Double prix, String conducteurMail) {
}

