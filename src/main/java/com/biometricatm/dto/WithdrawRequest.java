package com.biometricatm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class WithdrawRequest {
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Integer amt;

    @NotBlank(message = "Bank name is required")
    private String bankName;

    @NotNull(message = "PIN is required")
    private Integer pin;

    public Integer getAmt() {
        return amt;
    }

    public void setAmt(Integer amt) {
        this.amt = amt;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Integer getPin() {
        return pin;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }
}
