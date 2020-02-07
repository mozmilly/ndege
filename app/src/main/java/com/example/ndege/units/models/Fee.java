package com.example.ndege.units.models;

public class Fee implements java.io.Serializable {
    private static final long serialVersionUID = -1202923830417199419L;
    private String fee_name;
    private double fee_amount;

    public String getFee_name() {
        return this.fee_name;
    }

    public void setFee_name(String fee_name) {
        this.fee_name = fee_name;
    }

    public double getFee_amount() {
        return this.fee_amount;
    }

    public void setFee_amount(double fee_amount) {
        this.fee_amount = fee_amount;
    }
}
