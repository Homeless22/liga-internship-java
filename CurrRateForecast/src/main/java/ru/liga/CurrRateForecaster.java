package ru.liga;

import java.util.*;

public class CurrRateForecaster {

    //todo лучше вынести в отдельный класс
     class CurrRateComparator implements Comparator<CurrRate> {
          public int compare(CurrRate r1, CurrRate r2){
               return r1.rateDate.compareTo(r2.rateDate) * -1;
          }
     }

     private double getAvgRate(List<CurrRate> histCurrRates){
          Double sumRates = 0.0;
          for (CurrRate r:histCurrRates){
               sumRates += r.rate/r.nominal;
          }
          //todo не хватает пробелов, попробуй форматирование кода, в идее это ctrl+alt+L
          return (sumRates==0.0)?sumRates:(sumRates/histCurrRates.size());
     }

     private Date nextDate(Date date){
         Calendar instance = Calendar.getInstance();
         instance.setTime(date); //устанавливаем дату, с которой будет производить операции
         instance.add(Calendar.DAY_OF_MONTH, 1);// прибавляем 3 дня к установленной дате
         return instance.getTime(); // получаем измененную дату
     }

     private int getNumForecatRatesForPeriod(String period){
         int retVal = 0;
         if ("tomorrow".equals(period)){
             retVal = 1;
         }
         else if ("week".equals(period)) {
             retVal = 7;
         }
         return retVal;
     }

    /**
     * Расчет прогнозных курсов валют
     *
     * @param histCurrRates Список исторических курсов
     * @param period Период прогнозирования(tommorow, week)
     * @param refDate  Дата, от которой выполяется прогноз
     * @return Список прогнозируемых курсов
     */
    public List<CurrRate> getForecastCurrRates(List<CurrRate> histCurrRates, String period, Date refDate){
        //todo переменная нигде не используется
          int counter = 1;
          List<CurrRate> forecastRates = new LinkedList<CurrRate>();
          //todo можно заменить конструктором - new LinkedList<>(histCurrRates);
          List<CurrRate> histRates = new LinkedList<CurrRate>();
          histRates.addAll(histCurrRates);

          //сортировка курсов по убыванию даты курса
          Collections.sort(histRates, new CurrRateForecaster.CurrRateComparator());
          //отбор предыдущих курсов на дату refDate
          Date forecastDate = nextDate(refDate);
          int numForecatRates = getNumForecatRatesForPeriod(period);
           for(int i =0; i < numForecatRates; i++){
               CurrRate currRate = new CurrRate(1, getAvgRate(histRates), forecastDate, "");
               //добавляем прогнозный курс в список с прогнозными курсами
               forecastRates.add(currRate);
               //удаляем самый ранний исторический курс из списка
               histRates.remove(0);
               //добавляем прогнозный курс для следующего расчета
               histRates.add(currRate);
               //следущая прогнозная дата
               forecastDate = nextDate(forecastDate);
          }
           //todo а зачем ты здесь список чистишь, он же больше неиспользуется, лишняя операция
          histRates.clear();

          return forecastRates;
     }
}
