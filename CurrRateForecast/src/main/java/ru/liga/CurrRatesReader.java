package ru.liga;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/*
    Класс, реализующий чтение курсов из файла CSV
*/
public class CurrRatesReader {

    /**
     * Загрузка данных из файлв
     *
     * @param currency       Валюта
     * @param rateDateTo     Максимальная дата курса
     * @param numRecentRates Количество загружаемых курсов валют
     * @return Список курсов
     */
    public static List<CurrRate> loadRatesFromFile(String currency, Date rateDateTo, int numRecentRates) {
        List<CurrRate> listCurrRate = new LinkedList<>();

        //todo можно вынести в метод и назвать по бизнесу
        Reader reader = new InputStreamReader(getFileFromResourceAsStream(currency.toUpperCase() + ".csv"), Charset.forName("windows-1251"));

        //todo в "buildCSVFormat" всегда одинаковые параметры, лучше их убрать и вынести в константы
        try (CSVParser csvParser = new CSVParser(reader, buildCSVFormat("nominal;date;rate;cdx", ";", "\r\n", true, '"'));) {
            List<CSVRecord> csvRecords = csvParser.getRecords();

            csvRecords.sort(new CsvRecordComparator());

            //todo лучше вынести в финальное поле класса
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");

            for (CSVRecord csvRecord : csvRecords) {
                //достаточное ли количество курсов для прогнозирования
                if (isEnoughRatesForForecast(listCurrRate.size(), numRecentRates)){
                    break;
                }

                Date rateDate = dateFormatter.parse(csvRecord.get("date"));

                //Отбираем курсы валют с датой не больше даты начала прогноза
                if (useCurrRateForForecast(rateDate, rateDateTo)) {
                    listCurrRate.add(
                            //todo лучше вынести в метод, сложно читать
                            //можно использовать паттерн builder для создания объекта
                            new CurrRate(
                                    Double.parseDouble(csvRecord.get("nominal").replace(",", ".").replace(" ", "")),
                                    Double.parseDouble(csvRecord.get("rate").replace(",", ".").replace(" ", "")),
                                    rateDate,
                                    csvRecord.get("cdx")));
                }
            }
        } catch (IOException | ParseException e) {
            //todo надо вывести в консоль краткое инфо по ошибке
            throw new RuntimeException(e.getMessage(), e);
        }

        return listCurrRate;
    }

    private static CSVFormat buildCSVFormat(String header, String delimitter, String recordSeparator, boolean skipHeaderRecord, char quote) {
        return CSVFormat.Builder.create()
                .setSkipHeaderRecord(skipHeaderRecord)
                .setHeader(header.split(";"))
                .setAllowMissingColumnNames(true)
                .setDelimiter(delimitter)
                .setRecordSeparator(recordSeparator)
                .setQuote(quote)
                .build();
    }

    private static InputStream getFileFromResourceAsStream(String fileName) {

        InputStream inputStream = CurrRatesReader.class.getClassLoader().getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }

    private static boolean isEnoughRatesForForecast(int numCurrRate, int numRecentRates) {
        return (numCurrRate >= numRecentRates && numRecentRates >= 0);
    }

    private static boolean useCurrRateForForecast(Date rateDate, Date forecastDate) {
        return (rateDate != null && (rateDate.compareTo(forecastDate) <= 0 || forecastDate == null));
    }
}
