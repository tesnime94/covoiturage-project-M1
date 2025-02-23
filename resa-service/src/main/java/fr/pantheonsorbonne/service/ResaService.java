package fr.pantheonsorbonne.service;


import fr.pantheonsorbonne.dao.ResaDAO;
import fr.pantheonsorbonne.dto.ResaDTO;
import fr.pantheonsorbonne.entity.Resa;
import fr.pantheonsorbonne.exception.PaymentException;
import fr.pantheonsorbonne.exception.ResaNotFoundException;
import fr.pantheonsorbonne.gateway.ResaGateway;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.camel.ProducerTemplate;

import java.util.Map;

@ApplicationScoped
public class ResaService {

    @Inject
    private ResaGateway resaGateway;

    @Inject
    private ProducerTemplate producerTemplate;

    @Inject
    private ResaDAO resaDAO;

    public Resa getResaById(Long id) throws ResaNotFoundException {

        Resa resa = resaDAO.findById(id);
        if (resa == null) {
            throw new ResaNotFoundException("Resa avec ID " + id + " non trouvée.");
        }
        return resa;
    }



    @Transactional
    public Resa createResa(Long trajetNumber, Long amount, String cardHolderName, Long cardNumber, String expirationDate, int cvc, String userEmail) throws PaymentException {


        if (!resaGateway.processPayment(trajetNumber, amount, cardHolderName, cardNumber, expirationDate, cvc)) {
            throw new PaymentException("Le paiement a échoué. Veuillez vérifier vos informations.");
        }

        Resa resa = new Resa();
        resa.setTrajetNumber(trajetNumber);
        resa.setAmount(amount);
        resa.setCardHolderName(cardHolderName);
        resa.setCardNumber(cardNumber);
        resa.setExpirationDate(expirationDate);
        resa.setCvc(cvc);

        resaDAO.save(resa);

        resaGateway.sendConfirmationNotification(userEmail, resa.getResaNumber());

        return resa;

    }

    public void createResaFromMap(Map<String, Object> trajetMap) {
        // Extraction des informations du trajet
        String userEmail = (String) trajetMap.get("userEmail");
        Long trajetNumber = (Long) trajetMap.get("trajetNumber");

        // Logique de création de réservation
        // Par exemple, enregistrer dans une base de données ou effectuer une autre action
        Resa resa = new Resa();
        resa.setTrajetNumber(trajetNumber);

        resaGateway.sendDriverNotificationFromMap(userEmail, trajetNumber);

        // Sauvegarder la réservation dans une base de données (par exemple)
        resaDAO.save(resa);


    }
}
