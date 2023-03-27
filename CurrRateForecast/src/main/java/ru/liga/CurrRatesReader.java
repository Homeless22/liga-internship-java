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

    final private static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");

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
        String sFileName = currency.toUpperCase() + ".csv";

        Reader reader = new InputStreamReader(getFileFromResourceAsStream(sFileName), Charset.forName("windows-1251"));

        try (CSVParser csvParser = new CSVParser(reader, buildCSVFormat())) {
            List<CSVRecord> csvRecords = csvParser.getRecords();

            csvRecords.sort(new CsvRecordComparator());

            for (CSVRecord csvRecord : csvRecords) {
                //достаточное ли количество курсов для прогнозирования
                if (isEnoughRatesForForecast(listCurrRate.size(), numRecentRates)) {
                    break;
                }

                CurrRate currRate= newCurrRate(csvRecord);

                //Отбираем курсы валют с датой не больше даты начала прогноза
                if (useCurrRateForForecast(currRate.getRateDate(), rateDateTo)) {
                    listCurrRate.add(newCurrRate(csvRecord));
                }
            }
        } catch (IOException e) {
            System.out.println("Could not find file " + sFileName);
            throw new RuntimeException(e.getMessage(), e);
        }

        return listCurrRate;
    }

    private static CSVFormat buildCSVFormat() {
        final boolean skipHeaderRecord = true;
        final String header = "nominal;date;rate;cdx";
        final String delim = ";";
        final String recordSeparator = "\n";
        final char quote = '"';
        final boolean allowMissingColumnNames = true;

        return CSVFormat.Builder.create()
                .setSkipHeaderRecord(skipHeaderRecord)
                .setHeader(header.split(delim))
                .setAllowMissingColumnNames(allowMissingColumnNames)
                .setDelimiter(delim)
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

    private static CurrRate newCurrRate(CSVRecord csvRecord) {
        try {
            return new CurrRate(
                    Double.parseDouble(csvRecord.get("nominal").replace(",", ".").replace(" ", "")),
                    Double.parseDouble(csvRecord.get("rate").replace(",", ".").replace(" ", "")),
                    dateFormatter.parse(csvRecord.get("date")),
                    csvRecord.get("cdx"));
        } catch (ParseException e) {
            System.out.println("Invalid date format");
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    private static boolean isEnoughRatesForForecast(int numCurrRate, int numRecentRates) {
        return (numCurrRate >= numRecentRates && numRecentRates >= 0);
    }

    private static boolean useCurrRateForForecast(Date rateDate, Date forecastDate) {
        return (rateDate != null && (rateDate.compareTo(forecastDate) <= 0 || forecastDate == null));
    }
}
