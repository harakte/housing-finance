package com.kakaopay.finance.controller;

import com.kakaopay.finance.dto.*;
import com.kakaopay.finance.entity.Institute;
import com.kakaopay.finance.service.HousingFinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class HousingFinanceController {

    @Autowired
    HousingFinanceService service;

    @PostMapping("/upload")
    public Result upload(){
        return service.upload();
    }

    @GetMapping("/banks")
    public List<Institute> findAllBanks(){
        return service.getAllInstitutes();
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

    @PostMapping("/predict")
    public PredictFinance predictFinance(@Valid @RequestBody PredictFinanceRequest dto){
        return service.predict(dto.getInstituteName(), dto.getMonth());
    }

}
