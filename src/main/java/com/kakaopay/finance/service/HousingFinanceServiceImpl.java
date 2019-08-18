package com.kakaopay.finance.service;

import com.kakaopay.finance.dao.FinanceRepository;
import com.kakaopay.finance.dao.InstituteRepository;
import com.kakaopay.finance.dto.AnnualInstituteFinance;
import com.kakaopay.finance.dto.YearFinance;
import com.kakaopay.finance.entity.Finance;
import com.kakaopay.finance.entity.Institute;
import com.kakaopay.finance.util.CsvUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
                    institutes.add(new Institute(headerList[i].split("[]()]")[0]));
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

    @Override
    public List<YearFinance> getYearFinances() {
        /*
        1. 전체 finance 조회
        2. Map<Integer, Map<String, Integer>> 형태로 각 년도별 은행별 금액 grouping: Map<year, Map<instituteName, amount>>
        3. YearFinance class 로 mapping: YearFinance(year, Map<instituteName, amount>의 총합, Map<instituteName, amount>)
        4. year 를 기준으로 오름차순 정렬
        5. List<YearFinance> 형태로 리턴
         */
        return financeRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Finance::getYear,
                        Collectors.groupingBy(
                                s -> s.getInstitute().getInstituteName(),
                                Collectors.summingInt(Finance::getAmount))))
                .entrySet().stream()
                .map(s -> new YearFinance(
                        s.getKey(),
                        s.getValue().values().stream().mapToInt(i -> i).sum(),
                        s.getValue()))
                .sorted(Comparator.comparing(YearFinance::getYear))
                .collect(Collectors.toList());
    }

    @Override
    public AnnualInstituteFinance getLargestAnnualFinance() {
        /*
        1. 전체 institute 조회
        2. Map<String, Map<Integer,Integer>> 형태로 은행의 년도별 총합 grouping: Map<instituteName, Map<year, amount>>
        3. Map<year, amount> 에서 가장 큰 년도 총합을 검색: Map<instituteName, Map.Entry<year, amount>>
        4. 은행별 가장 큰 년도 총합 중 가장 큰 값을 검색: Map.Entry<instituteName, Map.Entry<year, amount>>
        5. AnnualInstituteFinance class 형태로 리턴
         */
        return instituteRepository.findAll().stream()
                .collect(
                        Collectors.toMap(
                                Institute::getInstituteName,
                                institute -> institute.getFinances().stream()
                                        .collect(
                                                Collectors.groupingBy(
                                                        Finance::getYear,
                                                        Collectors.summingInt(Finance::getAmount)))
                                        .entrySet().stream()
                                        .max(Comparator.comparing(Map.Entry::getValue))
                                        .orElseThrow(NoSuchElementException::new)))
                .entrySet().stream()
                .max(Comparator.comparing(entry -> entry.getValue().getValue()))
                .map(largest -> new AnnualInstituteFinance(
                        largest.getKey(),
                        largest.getValue().getKey(),
                        largest.getValue().getValue()))
                .orElseThrow(NoSuchElementException::new);
    }
}
