package ru.liga;

import java.util.Comparator;

class CurrRateComparator implements Comparator<CurrRate> {
    public int compare(CurrRate r1, CurrRate r2) {
        return r1.getRateDate().compareTo(r2.getRateDate()) * -1;
    }
}

