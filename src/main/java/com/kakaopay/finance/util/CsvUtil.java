package com.kakaopay.finance.util;

import com.opencsv.CSVReader;

import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil {

    public static List<String[]> readCsvFile(String filePath) throws Exception{
        Reader reader = Files.newBufferedReader(Paths.get(ClassLoader.getSystemResource(filePath).toURI()), Charset.forName("x-windows-949"));
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> list = new ArrayList<>();
        list = csvReader.readAll();
        reader.close();
        csvReader.close();
        return list;
    }
}
