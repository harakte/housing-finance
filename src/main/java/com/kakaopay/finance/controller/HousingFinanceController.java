package com.kakaopay.finance.controller;

import com.kakaopay.finance.dto.AnnualInstituteFinance;
import com.kakaopay.finance.dto.InstituteSupportFinance;
import com.kakaopay.finance.dto.YearFinance;
import com.kakaopay.finance.entity.Institute;
import com.kakaopay.finance.service.HousingFinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HousingFinanceController {

    @Autowired
    HousingFinanceService service;

    @PostMapping("/upload")
    public boolean upload(){
        return service.upload();
    }

    @GetMapping("/banks")
    public List<Institute> findAllBanks(){
        return service.findAll();
    }

    @GetMapping("/years")
    public List<YearFinance> getYearFinances(){
        return service.getYearFinances();
    }

    @GetMapping("/largest")
    public AnnualInstituteFinance getLargestYear(){
        return service.getLargestAnnualFinance();
    }

    @GetMapping("/summary/{instituteCode}")
    public InstituteSupportFinance getInstituteSummary(@PathVariable String instituteCode){
        return service.getInstituteSummary(instituteCode);
    }

}
