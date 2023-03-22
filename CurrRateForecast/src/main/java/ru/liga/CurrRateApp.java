package ru.liga;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class CurrRateApp {
    //количество последних курсов, которые используются для прогнозирования
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
            CurrRatesReader currRatesReader = new CurrRatesReader();
            Date currentDate = new Date();
            List<CurrRate> histCurrRates = currRatesReader.loadRatesFromFile(currency, currentDate, maxNumRecentRates);
            if (histCurrRates.size() == 0 ){
                System.out.println("Курсы валют на текущую дату не найдены");
                return;
            }
            //прогноз курса валют на основе списка исторических курсов
            CurrRateForecaster forecaster = new CurrRateForecaster();
            List<CurrRate> forecastRates = forecaster.getForecastCurrRates(histCurrRates, period, currentDate, maxNumRecentRates);
            histCurrRates.clear();

            //вывод курсов
            printCurrRates(forecastRates);
            forecastRates.clear();
        }
    }
}
