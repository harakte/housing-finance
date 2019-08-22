package com.kakaopay.finance.util;

import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;


@Slf4j
public class CsvUtil {

    public static List<String[]> readCsvFile(String filePath) throws IOException{
        InputStream input = ClassLoader.getSystemResourceAsStream(filePath);
        if(input == null){
            input = CsvUtil.class.getResourceAsStream("/"+filePath);
        }
        CSVReader csvReader = new CSVReader(new InputStreamReader(input));
        List<String[]> list = csvReader.readAll();
        input.close();
        csvReader.close();
        return list;
    }
}
