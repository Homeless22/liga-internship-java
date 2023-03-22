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
import java.util.*;

/*
    Класс, реализующий чтение курсов из файла CSV
*/
public class CurrRatesReader {

    //todo лучше выынести в тдельный класс
    static class CSVRecordComparator implements Comparator<CSVRecord> {
        public int compare(CSVRecord r1, CSVRecord r2){
            Date d1 = null;
            //todo тут лучше весь метод поместить в один try-catch, будет легче читать
            try {
                //todo new SimpleDateFormat("dd.MM.yyyy") используется дважды, можно вынести в поле класса
                d1 = new SimpleDateFormat("dd.MM.yyyy").parse(r1.get("date"));
            } catch (ParseException e) {
                //todo если кидаешь exception, то надо еще писать message
                // минимальная конструкция -> throw new RuntimeException(e.getMessage(), e);
                //todo перед тем как кидаешь exception надо написать лог в консоль, описывающий что случилось
                //например, failed to parse date
                throw new RuntimeException(e);
            }
            Date d2 = null;
            try {
                d2 = new SimpleDateFormat("dd.MM.yyyy").parse(r2.get("date"));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            return d1.compareTo(d2) * -1;
        }
    }

    private InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = this.getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }

    /**
    * Загрузка данных из файлв
    *
    * @param currency Валюта
    * @param rateDateTo Максимальная дата курса
    * @param numRecentRates  Количество загружаемых курсов валют
    * @return Список курсов
    */
    //todo принято чтобы все public методы были вверху класа, а private снизу
    public List<CurrRate> loadRatesFromFile(String currency, Date rateDateTo, int numRecentRates){
        //todo CurrRate в правой части создания объекта писать необязательно
        List<CurrRate> listCurrRate = new LinkedList<CurrRate>();

        Reader reader =  new InputStreamReader(getFileFromResourceAsStream(currency.toUpperCase() + ".csv"), Charset.forName("windows-1251"));
        String header = "nominal;date;rate;cdx";
        //todo вынести в отдельный метод
        CSVFormat csvFormat = CSVFormat.Builder.create()
                                            .setSkipHeaderRecord(true)
                                            .setHeader(header.split(";"))
                                            .setAllowMissingColumnNames(true)
                                            .setDelimiter(";")
                                            .setRecordSeparator("\r\n")
                                            .setQuote('"')
                                            .build();
        //todo лучше написать try(..) в однку строку, чтобы легче читалось
        try (
                CSVParser csvParser = new CSVParser(reader,csvFormat);
        ){
            List<CSVRecord> csvRecords  = csvParser.getRecords();
            //todo можно заменить на csvRecords.sort(..)
            Collections.sort(csvRecords, new CSVRecordComparator());
            int counter = 0;
            SimpleDateFormat sDateFormat = new SimpleDateFormat("dd.MM.yyyy");
            for (CSVRecord csvRecord : csvRecords) {
                Date rateDate = sDateFormat.parse(csvRecord.get("date"));
                //todo условие надо вынести в отдельный метод и назвать по бизнесу
                if ((rateDateTo == null || rateDate.compareTo(rateDateTo) <= 0) && (counter <= numRecentRates || numRecentRates == -1)){
                    listCurrRate.add(
                        new CurrRate(//todo если переносишь параметр метода, переноси после запятой
                                     //  method(param1,
                                     //         param2,
                                     //         param3);

                                      Double.parseDouble(csvRecord.get("nominal").replace(",", ".").replace(" ", ""))
                                    , Double.parseDouble(csvRecord.get("rate").replace(",", ".").replace(" ", ""))
                                    , rateDate
                                    , csvRecord.get("cdx")));

                    counter++;
                }

                if (counter >= numRecentRates && numRecentRates >= 0){
                    break;
                }
            }
            csvRecords.clear();
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
        //todo блок ParseException можно объеденить с IOException
        // catch (IOException | ParseException e)
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return listCurrRate;
    }
}
