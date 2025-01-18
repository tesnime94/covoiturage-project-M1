## Objectifs du système à modéliser

Application : Plateforme de gestion de covoiturage

Créer une plateforme de covoiturage où les utilisateurs peuvent publier des trajets, rechercher des trajets disponibles et réserver une place.

Fonctionnalités principales :

Publication de trajets : Les conducteurs peuvent publier des trajets avec des détails comme la ville de départ, la destination, les horaires, le prix, et les places disponibles ou supprimer leurs trajets si ils changent d'avis.

Creation de sous-trajets: Lorsqu'un conducteur créer un trajet, des sous-trajets sont automatiquement créer qui passe entre cette ville de déaprt et d'arrivée.

Recherche et réservation de trajets : Recherche de trajets (par localisation, date, prix, etc.)

Réservation de places disponibles.

Notifications : Notifications pour les confirmations de réservation, les annulations et la demande de trajet au conducteur.


## Interfaces

```

title Plateforme de covoiturage 

actor Passager
participant Système
participant Réservation
participant Paiement
database BaseDeDonnées

note over Passager: Recherche et réservation
Passager->Système: Recherche de trajets (localisation, date, prix)
Système->BaseDeDonnées: Requête sur les trajets disponibles
BaseDeDonnées-->Système: Liste des trajets trouvés
Système-->Passager: Résultats de la recherche
Passager->Système: Sélection d'un trajet et début réservation

note over Système: Processus de paiement
Système->Paiement: Créer une transaction
Paiement->Paiement: Traitement sécurisé
Paiement-->Système: Paiement réussi ou échec

alt Paiement réussi
    Système->Réservation: Confirmer la réservation
    Réservation->BaseDeDonnées: Mise à jour des places disponibles
    BaseDeDonnées-->Réservation: Confirmation
    Réservation-->Système: Réservation confirmée
    Système-->Passager: Réservation et paiement confirmés
else Paiement échoué
    Système-->Passager: Échec du paiement
end

note over Système: Notifications en temps réel
Système->Passager: Notifications (confirmation, annulation, rappels)

```


## Schéma relationnel



## Exigences fonctionnelles

* Un utilisateur peut publier un trajet
* Un  utilisateur peut rechercher des trajets qui correspondent à ses besoins ( ville départ, ville arrivée, heure )
* Un utilisateur peut réserver un trajet
* Un utilisateur doit payer pour réserver son trajet.
* Lorsqu'un trajet est réservé le nombre de place du trajet doit être mis à jour
* Quand le nombre de place est atteint la publication est archivée
* L'utilisateur reçoit une notifications lorsque le trajet est bien réserver 



