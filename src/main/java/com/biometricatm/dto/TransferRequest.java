package com.biometricatm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TransferRequest {
    @NotBlank(message = "Sender account number is required")
    private String senderAccountNO;

    @NotBlank(message = "Receiver account number is required")
    private String receiverAccountNO;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Integer amt;

    @NotNull(message = "PIN is required")
    private Integer pin;

    public String getSenderAccountNO() {
        return senderAccountNO;
    }

    public void setSenderAccountNO(String senderAccountNO) {
        this.senderAccountNO = senderAccountNO;
    }

    public String getReceiverAccountNO() {
        return receiverAccountNO;
    }

    public void setReceiverAccountNO(String receiverAccountNO) {
        this.receiverAccountNO = receiverAccountNO;
    }

    public Integer getAmt() {
        return amt;
    }

    public void setAmt(Integer amt) {
        this.amt = amt;
    }

    public Integer getPin() {
        return pin;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }
}
