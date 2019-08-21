package com.kakaopay.finance.service;

import com.kakaopay.finance.dto.AnnualInstituteFinance;
import com.kakaopay.finance.dto.InstituteSupportFinance;
import com.kakaopay.finance.dto.PredictFinance;
import com.kakaopay.finance.dto.YearFinance;
import com.kakaopay.finance.entity.Institute;

import java.util.List;

public interface HousingFinanceService {
    boolean upload();
    List<Institute> getAllInstitutes();
    List<YearFinance> getYearFinances();
    AnnualInstituteFinance getLargestAnnualFinance();
    InstituteSupportFinance getInstituteSummary(String instituteCode);
    PredictFinance predict(String instituteName, int month);
}
