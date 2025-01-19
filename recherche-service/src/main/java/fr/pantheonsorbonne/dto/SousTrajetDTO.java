package fr.pantheonsorbonne.dto;

import java.time.LocalDate;

public record SousTrajetDTO(
     Long id, String villeDepart,String villeArrivee, LocalDate date) {}

