package fr.pantheonsorbonne.dao;

import fr.pantheonsorbonne.entity.Payment;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class PaymentDAO {

    @Inject
    EntityManager em;
    @Transactional

    public void savePayment(Payment payment) {
        em.persist(payment);
    }

    public Payment getById(Long id) {
        return em.find(Payment.class, id);
    }


}
