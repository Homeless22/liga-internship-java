package ru.liga;

import java.util.Date;

public class CurrRate {

    private final double nominal;
    private final double rate;
    private final Date rateDate;
    private final String currencyName;

    public CurrRate(double nominal, double rate, Date rateDate, String currencyName) {
        this.nominal = nominal;
        this.rate = rate;
        this.rateDate = rateDate;
        this.currencyName = currencyName;
    }

    public double getNominal() {
        return nominal;
    }

    public double getRate() {
        return rate;
    }

    public Date getRateDate() {
        return rateDate;
    }

    public String getCurrencyName() {
        return currencyName;
    }
}

