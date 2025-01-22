package fr.pantheonsorbonne.dto;

// Ce Dto sert dans la liste de résultat affiché à l'utilisateur, avec des infos en moins qui ne sont pas nécessaire
public record TrajetDTO(
        Long id,
        String villeDepart,
        String villeArrivee,
        String date,
        String horaire,
        Integer placeDisponible,
        Double prix,
        String conducteurMail

) {
}
