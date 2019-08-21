package com.kakaopay.finance.service;

import com.kakaopay.finance.dao.FinanceRepository;
import com.kakaopay.finance.dao.InstituteRepository;
import com.kakaopay.finance.dto.*;
import com.kakaopay.finance.entity.Finance;
import com.kakaopay.finance.entity.Institute;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class HousingFinanceImplTest {

    @TestConfiguration
    static class HousingFinanceImplTestContextConfiguration{
        @Bean
        public HousingFinanceService housingFinanceService(){
            return new HousingFinanceServiceImpl();
        }
    }

    @Autowired
    private HousingFinanceServiceImpl housingFinanceServiceImpl;

    @MockBean
    private InstituteRepository instituteRepository;

    @MockBean
    private FinanceRepository financeRepository;

    @Before
    public void setUp(){
        ReflectionTestUtils.setField(housingFinanceServiceImpl, "filePath", "csv/input.csv");

        Institute bankA = new Institute("Bank A");
        Finance financeA1 = new Finance(bankA, 2019, 1, 100);
        Finance financeA2 = new Finance(bankA, 2019, 2, 101);
        Finance financeA3 = new Finance(bankA, 2020, 1, 102);
        bankA.addFinance(financeA1);
        bankA.addFinance(financeA2);
        bankA.addFinance(financeA3);

        Institute bankB = new Institute("Bank B");
        Finance financeB1 = new Finance(bankB, 2019, 1, 200);
        Finance financeB2 = new Finance(bankB, 2019, 2, 201);
        bankB.addFinance(financeB1);
        bankB.addFinance(financeB2);

        List<Institute> institutes = Arrays.asList(bankA, bankB);
        List<Finance> finances = Arrays.asList(financeA1, financeA2, financeA3, financeB1, financeB2);

        Mockito.when(instituteRepository.findAll()).thenReturn(institutes);
        Mockito.when(financeRepository.findAll()).thenReturn(finances);
        Mockito.when(instituteRepository.findById("bnk-1")).thenReturn(Optional.of(bankA));
    }

    @Test
    public void testUpload(){
        Result result = housingFinanceServiceImpl.upload();

        assertThat(result)
                .isNotNull();
    }

    @Test
    public void testFindAll(){
        List<Institute> institutes = housingFinanceServiceImpl.getAllInstitutes();

        assertThat(institutes)
                .isNotEmpty()
                .hasSize(2);

        assertThat(institutes.get(0).getFinances())
                .isNotEmpty()
                .hasSize(3);
    }

    @Test
    public void testGetYearFinances(){
        Map<String, Integer> detailAmount = new HashMap<>();
        detailAmount.put("Bank A", 201);
        detailAmount.put("Bank B", 401);
        YearFinance check = new YearFinance(2019, 602, detailAmount);

        List<YearFinance> yearFinances = housingFinanceServiceImpl.getYearFinances();

        assertThat(yearFinances)
                .isNotEmpty()
                .hasSize(2);

        assertThat(yearFinances.get(0))
                .isEqualToComparingFieldByFieldRecursively(check);

        assertThat(yearFinances.get(1).getTotalAmount())
                .isEqualTo(102);
    }

    @Test
    public void testGetLargestAnnualFinance(){
        AnnualInstituteFinance check = new AnnualInstituteFinance("Bank B", 2019, 401);

        AnnualInstituteFinance finance = housingFinanceServiceImpl.getLargestAnnualFinance();

        assertThat(finance)
                .isNotNull()
                .isEqualToComparingFieldByFieldRecursively(check);
    }

    @Test
    public void testGetInstituteSummary() {
        List<AnnualAverageAmount> amounts = new ArrayList<>();
        amounts.add(new AnnualAverageAmount(2019, 101));
        amounts.add(new AnnualAverageAmount(2020, 102));
        InstituteSupportFinance check = new InstituteSupportFinance("Bank A", amounts);

        InstituteSupportFinance finance = housingFinanceServiceImpl.getInstituteSummary("bnk-1");

        assertThat(finance)
                .isNotNull()
                .isEqualToComparingFieldByFieldRecursively(check);
    }

    @Test
    public void testPredict() {
        String instituteName = "Bank A";
        Institute bankA = new Institute(instituteName);
        Finance financeA1 = new Finance(bankA, 2015, 1, 846);
        Finance financeA2 = new Finance(bankA, 2016, 1, 534);
        Finance financeA3 = new Finance(bankA, 2017, 1, 282);
        Finance financeA4 = new Finance(bankA, 2018, 1, 1832);
        Finance financeA5 = new Finance(bankA, 2019, 1, 383);
        Finance financeA6 = new Finance(bankA, 2020, 1, 1779);
        bankA.addFinance(financeA1);
        bankA.addFinance(financeA2);
        bankA.addFinance(financeA3);
        bankA.addFinance(financeA4);
        bankA.addFinance(financeA5);
        bankA.addFinance(financeA6);

        Mockito.when(instituteRepository.findByInstituteName(instituteName)).thenReturn(Optional.of(bankA));

        int predictMonth = 1;
        PredictFinance predictFinance = housingFinanceServiceImpl.predict("Bank A", predictMonth);

        assertThat(predictFinance.getYear())
                .isEqualTo(2021);
        assertThat(predictFinance.getMonth().intValue())
                .isEqualTo(predictMonth);
    }
}
