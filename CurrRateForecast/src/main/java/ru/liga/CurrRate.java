package ru.liga;

import java.util.Date;

public class CurrRate {
    final double nominal;
    final double rate;
    final Date rateDate;
    final String currencyName;

    public CurrRate(double nominal, double rate, Date rateDate, String currencyName){
        this.nominal = nominal;
        this.rate = rate;
        this.rateDate = rateDate;
        this.currencyName = currencyName;
    }
}
