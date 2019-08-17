package com.kakaopay.finance.service;

import com.kakaopay.finance.entity.Institute;

import java.util.List;

public interface HousingFinanceService {
    public boolean upload();
    public List<Institute> findAll();
}
