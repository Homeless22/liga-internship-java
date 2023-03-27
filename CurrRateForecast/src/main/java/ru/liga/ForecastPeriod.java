package ru.liga;

public enum ForecastPeriod {
    TOMORROW("tomorrow", 1),
    WEEK("week", 7) ;

    final private String period;
    final private int numberOfDays;

    ForecastPeriod(String period, int numberOfDays) {
        this.period = period;
        this.numberOfDays = numberOfDays;
    }

    public String getPeriod() {
        return period;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }
}
