package com.kakaopay.finance.service;

import com.kakaopay.finance.dao.HousingFinanceRepository;
import com.kakaopay.finance.entity.Institute;
import com.kakaopay.finance.util.CsvUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class HousingFinanceServiceImpl implements HousingFinanceService{

    @Autowired
    private HousingFinanceRepository housingFinanceRepository;

    @Value("${csv.file.path}")
    private String filePath;

    @Override
    public boolean upload() {
        try{
            List<String[]> list = CsvUtil.readCsvFile(filePath);
            String[] headerList = list.get(0);
            List<Institute> institutes = new ArrayList<>();
            for(int i = 2; i < headerList.length; i++){
                if(headerList[i].trim().length() != 0){
                    institutes.add(new Institute(Integer.toString(i), headerList[i].split("[]\\(\\)]")[0]));
                }
            }
            housingFinanceRepository.saveAll(institutes);
            return true;
        }catch (Exception ex){
            log.error("Error = {}", ex.getMessage());
            return false;
        }
    }

    @Override
    public List<Institute> findAll() {
        return housingFinanceRepository.findAll();
    }
}
