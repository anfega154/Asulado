package co.com.asulado.model.scheduledpayment;


import java.io.Serializable;
import java.math.BigDecimal;

public class ScheduledPayment implements Serializable {
    private String paymentId;
    private String period;
    private String payType;
    private String identification;
    private String name;
    private String state;
    private BigDecimal amount;
    private String costumer;


    public String getPaymentId() {
        return paymentId;
    }

    public String getPeriod() {
        return period;
    }

    public String getPayType() {
        return payType;
    }

    public String getIdentification() {
        return identification;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCostumer() {
        return costumer;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCostumer(String costumer) {
        this.costumer = costumer;
    }

}
