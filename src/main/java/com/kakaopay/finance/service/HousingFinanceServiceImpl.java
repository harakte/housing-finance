package com.kakaopay.finance.service;

import com.kakaopay.finance.dao.FinanceRepository;
import com.kakaopay.finance.dao.InstituteRepository;
import com.kakaopay.finance.entity.Finance;
import com.kakaopay.finance.entity.Institute;
import com.kakaopay.finance.util.CsvUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class HousingFinanceServiceImpl implements HousingFinanceService{

    @Autowired
    private InstituteRepository instituteRepository;

    @Autowired
    private FinanceRepository financeRepository;

    @Value("${csv.file.path}")
    private String filePath;

    @Override
    public boolean upload() {
        try{
            List<String[]> list = CsvUtil.readCsvFile(filePath);
            String[] headerList = list.remove(0);
            List<Institute> institutes = new ArrayList<>();
            for(int i = 2; i<headerList.length; i++){
                if(headerList[i].trim().length() != 0){
                    institutes.add(new Institute(headerList[i].split("[]\\(\\)]")[0]));
                }
            }

            List<Finance> finances = new ArrayList<>();
            for(String[] dataRow : list){
                List<String> dataList = new ArrayList(Arrays.asList(dataRow));
                Integer year = Integer.parseInt(dataList.remove(0));
                Integer month = Integer.parseInt(dataList.remove(0));
                for(int i = 0 ; i < dataList.size(); i++){
                    if(dataList.get(i).trim().length() != 0) {
                        Finance finance = new Finance(institutes.get(i), year, month,
                                Integer.parseInt(dataList.get(i).replaceAll("[,\"]","")));
                        institutes.get(i).addFinance(finance);
                        finances.add(finance);
                    }
                }
            }

            instituteRepository.saveAll(institutes);
            financeRepository.saveAll(finances);
            return true;
        }catch (Exception ex){
            log.error("Error = {}", ex.getMessage());
            return false;
        }
    }

    @Override
    public List<Institute> findAll() {
        return instituteRepository.findAll();
    }
}
