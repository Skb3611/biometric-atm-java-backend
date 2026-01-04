package com.biometricatm.dto;

import jakarta.validation.constraints.NotBlank;

public class FingerprintVerifyRequest {
    @NotBlank(message = "Fingerprint ID is required")
    private String fingerprintId;

    public String getFingerprintId() {
        return fingerprintId;
    }

    public void setFingerprintId(String fingerprintId) {
        this.fingerprintId = fingerprintId;
    }
}
