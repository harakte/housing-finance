package com.kakaopay.finance.service;

import com.kakaopay.finance.dto.*;
import com.kakaopay.finance.dto.finance.AnnualInstituteFinance;
import com.kakaopay.finance.dto.finance.InstituteSupportFinance;
import com.kakaopay.finance.dto.finance.PredictFinance;
import com.kakaopay.finance.dto.finance.YearFinance;
import com.kakaopay.finance.entity.Institute;

import java.util.List;

public interface HousingFinanceService {
    Result upload();
    List<Institute> getAllInstitutes();
    List<YearFinance> getYearFinances();
    AnnualInstituteFinance getLargestAnnualFinance();
    InstituteSupportFinance getInstituteSummary(String instituteCode);
    PredictFinance predict(String instituteName, int month);
}
