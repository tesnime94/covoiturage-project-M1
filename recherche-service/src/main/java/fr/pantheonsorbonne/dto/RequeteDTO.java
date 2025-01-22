package fr.pantheonsorbonne.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record RequeteDTO(
    String villeDepart,
    String villeArrivee,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") LocalDate date,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm") LocalTime horaire,
    Double prix
) {}
