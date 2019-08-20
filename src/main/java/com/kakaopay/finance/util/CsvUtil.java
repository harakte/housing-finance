package com.kakaopay.finance.util;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CsvUtil {

    public static List<String[]> readCsvFile(String filePath) throws URISyntaxException, IOException {
        Reader reader = Files.newBufferedReader(Paths.get(ClassLoader.getSystemResource(filePath).toURI()), Charset.forName("x-windows-949"));
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> list = csvReader.readAll();
        reader.close();
        csvReader.close();
        return list;
    }
}
