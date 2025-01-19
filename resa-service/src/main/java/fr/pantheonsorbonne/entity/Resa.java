package fr.pantheonsorbonne.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "resa")
public class Resa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resaNumber;

    @Column(nullable = false)
    private Long trajetNumber;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private String cardHolderName;

    @Column(nullable = false)
    private Long cardNumber;

    @Column(nullable = false)
    private String expirationDate;

    @Column(nullable = false)
    private int cvc;


    public Long getResaNumber() {
        return resaNumber;
    }

    public void setResaNumber(Long reservationNumber) {
        this.resaNumber = resaNumber;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public Long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getCvc() {
        return cvc;
    }

    public void setCvc(int cvc) {
        this.cvc = cvc;
    }


    public Long getTrajetNumber() {
        return trajetNumber;
    }

    public void setTrajetNumber(Long trajetNumber) {
        this.trajetNumber = trajetNumber;
    }
}


