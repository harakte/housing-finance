package com.kakaopay.finance.controller;

import com.kakaopay.finance.dto.YearFinance;
import com.kakaopay.finance.entity.Institute;
import com.kakaopay.finance.service.HousingFinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/finance")
public class HousingFinanceController {

    @Autowired
    HousingFinanceService service;

    @PostMapping("/upload")
    public boolean upload(){
        return service.upload();
    }

    @GetMapping("/get")
    public List<Institute> findAll(){
        return service.findAll();
    }

    @GetMapping("/years")
    public List<YearFinance> getYearFinances(){
        return service.getYearFinances();
    }
}
