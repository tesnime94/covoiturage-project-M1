package fr.pantheonsorbonne.dto;


import java.time.LocalDate;
import java.time.LocalTime;

public record TrajetDTO(
    Long id,
    String villeDepart,
    String villeArrivee,
    LocalDate date,
    LocalTime horaire,
    Integer placeDisponible, 
    Double prix,
    String conducteurMail
   
) {}


