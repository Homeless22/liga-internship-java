package ru.liga;

public enum ForecastPeriod {
    //todo здесь абстрактный метод не нужен, лучше добавить еще одно приватное поле для числа дней
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

    //todo можно сделать final
    private String period;

    //todo модификатор private излишен для онструктора енума
    private ForecastPeriod(String period) {
        this.period = period;
    }

    //todo если что в идее есть автогенерация конструкторов/геттеров/сеттеров вызывается хоткеем alt+insert
    public String getPeriod() {
        return period;
    }

    public abstract int getNumberOfDays();
}
