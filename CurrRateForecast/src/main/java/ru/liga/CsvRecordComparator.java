package ru.liga;

import org.apache.commons.csv.CSVRecord;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

class CsvRecordComparator implements Comparator<CSVRecord> {
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
    private final String csvHdrColumnDate = "date";

    public int compare(CSVRecord r1, CSVRecord r2) {
        try {
            return dateFormatter.parse(r1.get(csvHdrColumnDate)).compareTo(dateFormatter.parse(r2.get(csvHdrColumnDate))) * -1;
        } catch (ParseException e) {
            System.out.println("invalid date format");
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}