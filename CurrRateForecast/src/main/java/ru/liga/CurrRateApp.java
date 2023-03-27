package ru.liga;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class CurrRateApp {

    //количество последних курсов, которые используются для прогнозирования
    private final static int MAX_NUM_RECENT_RATES = 7;
    private final static String COMMAND_RATE = "rate";
    private final static String COMMAND_EXIT = "exit";
    private final static DateFormat dtFormat = new SimpleDateFormat("EE dd.MM.yyyy");

    public static void main(String[] args) {
        //Ввода данных из консоли
        Scanner scanner = new Scanner(System.in);
        CmdLineParser cmdLineParser = new CmdLineParser(scanner.nextLine());
        scanner.close();

        //Разбор команды
        if (!cmdLineParser.parseAndValidate()) {
            System.out.println("Неверный формат команды");
            return;
        }
        //
        if (COMMAND_RATE.equals(cmdLineParser.getOptionValue(CommandOption.COMMAND))) {
            String currency = cmdLineParser.getOptionValue(CommandOption.CURRENCY);
            String period = cmdLineParser.getOptionValue(CommandOption.PERIOD);

            //загрузка исторических курсов валют из файла на текущую дату
            List<CurrRate> histCurrRates = CurrRatesReader.loadRatesFromFile(currency, new Date(), MAX_NUM_RECENT_RATES);
            if (histCurrRates.isEmpty()) {
                System.out.println("Курсы валют на текущую дату не найдены");
                return;
            }

            //прогноз курса валют на основе списка исторических курсов
            List<CurrRate> forecastRates = CurrRateForecaster.getForecastCurrRates(histCurrRates, ForecastPeriod.valueOf(period), new Date(), MAX_NUM_RECENT_RATES);

            //вывод курсов
            printCurrRates(forecastRates);
        } else if (COMMAND_EXIT.equals(cmdLineParser.getOptionValue(CommandOption.COMMAND))) {
            System.exit(0);
        }
    }

    private static void printCurrRate(CurrRate currRate) {
        System.out.println(String.format("%s - %.2f", dtFormat.format(currRate.getRateDate()), (double) Math.round(currRate.getRate() * 100) / 100));
    }

    private static void printCurrRates(List<CurrRate> currRates) {
        currRates.forEach(currRate -> printCurrRate(currRate));
    }
}
