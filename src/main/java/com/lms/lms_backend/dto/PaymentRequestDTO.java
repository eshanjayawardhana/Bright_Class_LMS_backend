package com.lms.lms_backend.dto;

import com.lms.lms_backend.entity.enums.PaymentMethod;

public class PaymentRequestDTO {
    private Long enrollmentId;
    private Double amount;
    private PaymentMethod paymentMethod; // SLIP or ONLINE


    public PaymentRequestDTO() {
    }


    public PaymentRequestDTO(Long enrollmentId, Double amount, PaymentMethod paymentMethod) {
        this.enrollmentId = enrollmentId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}