package fr.pantheonsorbonne.dto;



import java.util.List;
public record ResultatDTO( boolean success, String message, List<TrajetDTO> trajets
)

{

}