package fr.pantheonsorbonne.service;


import fr.pantheonsorbonne.dao.ResaDAO;
import fr.pantheonsorbonne.dto.ResaDTO;
import fr.pantheonsorbonne.entity.Resa;
import fr.pantheonsorbonne.exception.PaymentException;
import fr.pantheonsorbonne.gateway.ResaGateway;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.camel.ProducerTemplate;

@ApplicationScoped
public class ResaService {

    @Inject
    private ResaGateway resaGateway;

    @Inject
    private ProducerTemplate producerTemplate;

    @Inject
    private ResaDAO resaDAO;

    public ResaDTO getResaById(Long id) {
    }

    @Transactional
    public Resa createResa(Long trajetNumber, Long amount, String cardHolderName, Long cardNumber, String expirationDate, int cvc, String paymentResponse) throws PaymentException {

        if (!resaGateway.processPayment(paymentResponse)) {
            throw new PaymentException("Le paiement a échoué. Veuillez vérifier vos informations.");
        }

        Resa resa = new Resa();
        resa.setTrajetNumber(trajetNumber);
        resa.setAmount(amount);
        resa.setCardHolderName(cardHolderName);
        resa.setCardNumber(cardNumber);
        resa.setExpirationDate(expirationDate);
        resa.setCvc(cvc);
        resa.setPaymentResponse(paymentResponse);

        resaDAO.save(resa);
        return resa;

    }
}
