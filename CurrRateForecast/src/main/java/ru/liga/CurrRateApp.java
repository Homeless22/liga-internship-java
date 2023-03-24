package ru.liga;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class CurrRateApp {

    //количество последних курсов, которые используются для прогнозирования
    private final static int MAX_NUM_RECENT_RATES = 7;

    public static void main(String[] args){
        //Ввода данных из консоли
        Scanner scanner = new Scanner(System.in);
        CmdLineParser cmdLineParser = new CmdLineParser(scanner.nextLine());
        scanner.close();

        //Разбор команды
        if (!cmdLineParser.parseAndValidate()){
            System.out.println("Неверный формат команды");
            return;
        }
        //
        if ("rate".equals(cmdLineParser.getOptionValue("command"))){
            String currency = cmdLineParser.getOptionValue("currency");
            String period = cmdLineParser.getOptionValue("period");

            //загрузка исторических курсов валют из файла на текущую дату
            List<CurrRate> histCurrRates = CurrRatesReader.loadRatesFromFile(currency, new Date(), MAX_NUM_RECENT_RATES);
            if (histCurrRates.size() == 0 ){
                System.out.println("Курсы валют на текущую дату не найдены");
                return;
            }

            //прогноз курса валют на основе списка исторических курсов
            List<CurrRate> forecastRates = CurrRateForecaster.getForecastCurrRates(histCurrRates, period, new Date(), MAX_NUM_RECENT_RATES);

            //вывод курсов
            printCurrRates(forecastRates);
        }
        else if ("exit".equals(cmdLineParser.getOptionValue("command"))){
            System.exit(0);
        }
    }

    private static void printCurrRates(List<CurrRate> currRates){
        SimpleDateFormat dtFormat = new SimpleDateFormat("EE dd.MM.yyyy");
        for (CurrRate r:currRates){
            System.out.println(dtFormat.format(r.getRateDate())+" - "+String.format("%.2f", (double)Math.round(r.getRate() * 100)/100));
        }
    }
}
