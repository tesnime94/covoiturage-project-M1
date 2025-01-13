package fr.pantheonsorbonne.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record TrajetAvecSousTrajetsDTO(Long id,
                                       String villeDepart,
                                       String villeArrivee,
                                       LocalDate date,
                                       LocalTime horaire,
                                       Integer nombreDePlaces,
                                       Double prix,
                                       String conducteurMail,
                                       List<SousTrajetDTO> sousTrajets) {
}
