package fr.pantheonsorbonne.dto;



import java.util.List;
public record ResultatDTO( boolean success, boolean isTrajetPrincipal, String message, List<TrajetDTO> trajets
)

{

}