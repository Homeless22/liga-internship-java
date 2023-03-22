package ru.liga;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class CurrRateApp {
    //количество последних курсов, которые используются для прогнозирования
    //todo всегда надо ставить модификатор доступа для поля (private)
    //todo названия констант всегда пишутся заглавными буквами snake_case (MAX_NUM_RECENT_RATES)
    final static int maxNumRecentRates = 7;


    private static void printCurrRates(List<CurrRate> currRates){
        SimpleDateFormat dtFormat = new SimpleDateFormat("EE dd.MM.yyyy");
        for (CurrRate r:currRates){
            System.out.println(dtFormat.format(r.rateDate)+" - "+String.format("%.2f", (double)Math.round(r.rate * 100)/100));
        }
    }

    public static void main(String[] args){
        //Ввода данных из консоли
        Scanner scanner = new Scanner(System.in);
        CmdLineParser cmdLineParser = new CmdLineParser(scanner.nextLine());
        scanner.close();

        //Разбор команды
        if (!cmdLineParser.parse()){
            System.out.println("Неверный формат команды");
            return;
        }
        //
        if ("rate".equals(cmdLineParser.getOptionValue("command"))){
            String currency = cmdLineParser.getOptionValue("currency");
            String period = cmdLineParser.getOptionValue("period");

            //загрузка исторических курсов валют из файла на текущую дату
            //todo тут ты создаешь объект класса, чтоб вызвать у него один метод, попробуй сделать метод loadRatesFromFile статическим
            CurrRatesReader currRatesReader = new CurrRatesReader();
            //todo можно не прописывать здесь переменную, а сразу написать new Date() в параметрах метода loadRatesFromFile
            // будет компактнее
            Date currentDate = new Date();
            List<CurrRate> histCurrRates = currRatesReader.loadRatesFromFile(currency, currentDate, maxNumRecentRates);
            if (histCurrRates.size() == 0 ){
                System.out.println("Курсы валют на текущую дату не найдены");
                return;
            }
            //прогноз курса валют на основе списка исторических курсов
            //todo тут ты создаешь объект класса, чтоб вызвать у него один метод, попробуй сделать метод getForecastCurrRates статическим
            CurrRateForecaster forecaster = new CurrRateForecaster();
            List<CurrRate> forecastRates = forecaster.getForecastCurrRates(histCurrRates, period, currentDate);
            //todo после использования списка не нужно его чистить, лишняя операция
            histCurrRates.clear();

            //вывод курсов
            printCurrRates(forecastRates);
            forecastRates.clear();
        }
    }
}
