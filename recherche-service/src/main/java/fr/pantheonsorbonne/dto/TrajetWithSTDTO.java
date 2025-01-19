package fr.pantheonsorbonne.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record TrajetWithSTDTO(Long id,
String villeDepart,
String villeArrivee,
LocalDate date,
LocalTime horaire,
Integer placeDisponible, 
Double prix,
String conducteurMail,
List<SousTrajetDTO> sousTrajets )
//  Les Sous-trajets associés au trajet
{}



