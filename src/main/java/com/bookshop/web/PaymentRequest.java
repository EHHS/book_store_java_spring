package com.bookshop.web;

import com.bookshop.validation.ExpiryDate;
import com.bookshop.validation.LuhnCheck;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PaymentRequest {

    @NotBlank
    @Size(max = 100)
    private String cardName;

    @NotBlank
    @LuhnCheck
    private String cardNumber;

    @NotBlank
    @ExpiryDate
    private String expiryDate; // MM/YY

    @NotBlank
    @Pattern(regexp = "^[0-9]{3,4}$", message = "CVV must be 3 or 4 digits")
    private String cvv;

    public String getCardName() { return cardName; }
    public void setCardName(String cardName) { this.cardName = cardName; }
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }
}


