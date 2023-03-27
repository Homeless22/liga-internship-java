package ru.liga;

public enum ForecastPeriod {
    TOMORROW("tomorrow") {
        @Override
        public int getNumberOfDays() {
            return 1;
        }
    },
    WEEK("week") {
        @Override
        public int getNumberOfDays() {
            return 7;
        }
    };

    private String period;

    private ForecastPeriod(String period) {
        this.period = period;
    }

    public String getPeriod() {
        return period;
    }

    public abstract int getNumberOfDays();
}
