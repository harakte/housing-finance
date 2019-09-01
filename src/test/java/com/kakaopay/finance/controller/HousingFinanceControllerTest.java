package com.kakaopay.finance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.finance.dto.*;
import com.kakaopay.finance.dto.finance.*;
import com.kakaopay.finance.entity.Finance;
import com.kakaopay.finance.entity.Institute;
import com.kakaopay.finance.service.HousingFinanceService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(HousingFinanceController.class)
public class HousingFinanceControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private HousingFinanceService housingFinanceService;

    @Test
    public void testUpload() throws Exception{
        Result result = new Result(Boolean.TRUE);
        given(housingFinanceService.upload()).willReturn(result);

        mvc.perform(post("/upload").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void testBanks() throws Exception{
        Institute bankA = new Institute("Bank A");
        Finance financeA1 = new Finance(bankA, 2019, 1, 100);
        bankA.addFinance(financeA1);

        Institute bankB = new Institute("Bank B");
        Finance financeB1 = new Finance(bankB, 2019, 1, 200);
        bankB.addFinance(financeB1);

        List<Institute> institutes = Arrays.asList(bankA, bankB);

        given(housingFinanceService.getAllInstitutes()).willReturn(institutes);

        mvc.perform(get("/banks").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[*].instituteName",
                        containsInAnyOrder(bankA.getInstituteName(), bankB.getInstituteName())));
    }

    @Test
    public void testYears() throws Exception{
        Map<String, Integer> detailAmounts = Stream.of(
                new AbstractMap.SimpleEntry<>("Bank A", 1000),
                new AbstractMap.SimpleEntry<>("Bank B", 1001)
        ).collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
        YearFinance yearFinance = new YearFinance(2019, 8, detailAmounts);
        List<YearFinance> yearFinances = Collections.singletonList(yearFinance);

        given(housingFinanceService.getYearFinances()).willReturn(yearFinances);

        mvc.perform(get("/years").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].year").value(yearFinance.getYear()))
                .andExpect(jsonPath("$[0].totalAmount").value(yearFinance.getTotalAmount()));
    }

    @Test
    public void testLargest() throws Exception{
        AnnualInstituteFinance annualInstFinance = new AnnualInstituteFinance("Bank A", 2019, 1000);

        given(housingFinanceService.getLargestAnnualFinance()).willReturn(annualInstFinance);

        mvc.perform(get("/largest").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("instituteName").value(annualInstFinance.getInstituteName()))
                .andExpect(jsonPath("year").value(annualInstFinance.getYear()));
    }

    @Test
    public void testSummary() throws Exception{
        String instituteCode = "bnk-1";
        AnnualAverageAmount least = new AnnualAverageAmount(2015, 1000);
        AnnualAverageAmount largest = new AnnualAverageAmount(2019, 2000);
        InstituteSupportFinance instSupportFinance
                = new InstituteSupportFinance("bnk-1", Arrays.asList(least, largest));

        given(housingFinanceService.getInstituteSummary(eq(instituteCode))).willReturn(instSupportFinance);

        mvc.perform(get("/summary/"+instituteCode).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("instituteName").value(instSupportFinance.getInstituteName()))
                .andExpect(jsonPath("supportAmount[0].year").value(least.getYear()))
                .andExpect(jsonPath("supportAmount[0].amount").value(least.getAmount()));
    }

    @Test
    public void testPredict() throws Exception{
        String instName = "Bank A";
        PredictFinanceRequest request = new PredictFinanceRequest(instName, 2);
        PredictFinance predictFinance = new PredictFinance("bnk-1", 2019, 2, 2000);

        given(housingFinanceService.predict(request.getInstituteName(), request.getMonth())).willReturn(predictFinance);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        mvc.perform(post("/predict").contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("instituteCode").value(predictFinance.getInstituteCode()))
                .andExpect(jsonPath("year").value(predictFinance.getYear()))
                .andExpect(jsonPath("month").value(predictFinance.getMonth()))
                .andExpect(jsonPath("amount").value(predictFinance.getAmount()));
    }
}
