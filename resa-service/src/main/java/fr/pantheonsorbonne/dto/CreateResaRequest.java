package fr.pantheonsorbonne.dto;

public class CreateResaRequest {

    private Long trajetNumber;

    private Long amount;

    private String cardHolderName;

    private Long cardNumber;

    private String expirationDate;

    private int cvc;

    private String userEmail;

    public Long getTrajetNumber() {
        return trajetNumber;
    }

    public void setTrajetNumber(Long trajetNumber) {
        this.trajetNumber = trajetNumber;
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
