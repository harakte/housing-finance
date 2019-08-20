package com.kakaopay.finance.controller;

import com.kakaopay.finance.service.HousingFinanceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(HousingFinanceController.class)
public class HousingFinanceControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private HousingFinanceService housingFinanceService;

}
