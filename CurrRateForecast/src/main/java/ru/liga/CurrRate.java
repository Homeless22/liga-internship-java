package ru.liga;

import java.util.Date;

public class CurrRate {
    //todo поля в классе должны быть с модификаторами private
    // а доступ к ним через getter + setter
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
