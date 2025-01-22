package fr.pantheonsorbonne.dto;

import java.util.List;

public record TrajetAvecSousTrajetsDTO(Long id,
                                       String villeDepart,
                                       String villeArrivee,
                                       String date,
                                       String horaire,
                                       Integer nombreDePlaces,
                                       Double prix,
                                       String conducteurMail,
                                       List<SousTrajetDTO> sousTrajets) {
}
