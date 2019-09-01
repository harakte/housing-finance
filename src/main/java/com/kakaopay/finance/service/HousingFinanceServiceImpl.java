package com.kakaopay.finance.service;

import com.kakaopay.finance.dao.FinanceRepository;
import com.kakaopay.finance.dao.InstituteRepository;
import com.kakaopay.finance.dto.*;
import com.kakaopay.finance.dto.finance.*;
import com.kakaopay.finance.entity.Finance;
import com.kakaopay.finance.entity.Institute;
import com.kakaopay.finance.exception.NotFoundException;
import com.kakaopay.finance.exception.NoDataException;
import com.kakaopay.finance.util.CsvUtil;
import com.kakaopay.finance.util.LinearRegression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
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
    public Result upload() {
        List<String[]> list;
        try {
            list = CsvUtil.readCsvFile(filePath);
        }catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }

        if(list == null || list.size() < 1){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No data in csv file.");
        }

        String[] headerList = list.remove(0);
        List<Institute> institutes = new ArrayList<>();
        for(int i = 2; i<headerList.length; i++){
            if(headerList[i].trim().length() != 0){
                institutes.add(new Institute(headerList[i].split("[1-9()]")[0]));
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

        List<Institute> saveInstitutes = instituteRepository.saveAll(institutes);
        if(saveInstitutes.size() < 1){
            return new Result(Boolean.FALSE, "No data inserted in database.");
        }

        return new Result(Boolean.TRUE);
    }

    @Override
    public List<Institute> getAllInstitutes() {
        try {
            List<Institute> institutes = instituteRepository.findAll();

            if(institutes.size() < 1)
                throw new NoDataException("No institute data in Repository");

            return institutes;
        }catch (NoDataException ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
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
    public AnnualInstituteFinance getLargestAnnualFinance(){
        /*
        1. 전체 institute 조회
        2. Map<String, Map<Integer,Integer>> 형태로 은행의 년도별 총합 grouping: Map<instituteName, Map<year, amount>>
        3. Map<year, amount> 에서 가장 큰 년도 총합을 검색: Map<instituteName, Map.Entry<year, amount>>
        4. 은행별 가장 큰 년도 총합 중 가장 큰 값을 검색: Map.Entry<instituteName, Map.Entry<year, amount>>
        5. AnnualInstituteFinance class 형태로 리턴
         */
        try {
            //각 기관별 가장 큰 금액의 년도와 금액
            Map<String, Map.Entry<Integer, Integer>> largestInInstitute =
                    instituteRepository.findAll().stream()
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
                                                    .orElse(new AbstractMap.SimpleEntry<Integer, Integer>(0, 0))));
            //가장 큰 금액의 기관
            return largestInInstitute.entrySet().stream()
                    .max(Comparator.comparing(entry -> entry.getValue().getValue()))
                    .map(largest -> new AnnualInstituteFinance(
                            largest.getKey(),
                            largest.getValue().getKey(),
                            largest.getValue().getValue()))
                    .orElseThrow(() -> new NoDataException("No institute data in Repository"));

        }catch(NoDataException ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @Override
    public InstituteSupportFinance getInstituteSummary(String instituteCode) {
        /*
        1. id인 instituteCode로 institute 조회
        2. List<AnnualAverageAmount> 형태로 은행의 년도별 평균 금액
        3. List<AnnualAverageAmount>를 amount 기준으로 정렬
        4. 은행의 년도별 평균 금액 중 최대값과 최소값을 추출
        5. InstituteSupportFinance class 형태로 리턴
        */
        try {
            Institute institute = instituteRepository.findById(instituteCode)
                    .orElseThrow(() -> new NotFoundException("Provide correct institute code"));

            List<AnnualAverageAmount> averageAmounts =
                    institute.getFinances().stream()
                            .collect(
                                    Collectors.groupingBy(
                                            Finance::getYear,
                                            Collectors.averagingInt(Finance::getAmount)))
                            .entrySet().stream()
                            .map(e -> new AnnualAverageAmount(e.getKey(), (int) Math.round(e.getValue())))
                            .sorted(Comparator.comparing(AnnualAverageAmount::getAmount))
                            .collect(Collectors.toList());

            int size = averageAmounts.size();
            if (size < 1) throw new NoDataException("No finance data in Repository");

            return new InstituteSupportFinance(
                    institute.getInstituteName(),
                    Arrays.asList(averageAmounts.get(0), averageAmounts.get(size - 1)));

        }catch(NotFoundException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }catch (NoDataException ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @Override
    public PredictFinance predict(String instituteName, int month) {
        // 기관 이름을 기준으로 조회
        Institute institute;
        try {
            institute = instituteRepository.findByInstituteName(instituteName).orElseThrow(() -> new NotFoundException("Not found " + instituteName));
        } catch (NotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }

        // Map<Integer, Integer> 형태로 finance 데이터를 mapping : Map<x, y>
        Map<Integer, Integer> monthAmounts =
        institute.getFinances().stream()
                .collect(Collectors.toMap(
                        finance -> finance.getYear() * 12 + finance.getMonth(),
                        Finance::getAmount));

        // 예측 변수
        List<Integer> months = monthAmounts.keySet().stream()
                .mapToInt(integer -> integer)
                .boxed().collect(Collectors.toList());
        // 결과 변수
        List<Integer> amounts = monthAmounts.values().stream()
                .mapToInt(integer -> integer).boxed()
                .collect(Collectors.toList());

        // Linear regression 초기화
        LinearRegression linearRegression = new LinearRegression(months, amounts);
        // 예측해야하는 다음 년도
        int predictYear = institute.getFinances().stream().mapToInt(Finance::getYear).max().orElse(0) + 1;
        int predictX = predictYear * 12 + month;

        // 예측 계산
        Integer predictAmount = linearRegression.predict(predictX).intValue();
        return new PredictFinance(institute.getInstituteCode(), predictYear, month, predictAmount);
    }
}
