package com.kakaopay.finance.dto.finance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstituteSupportFinance {
    private String instituteName;
    private List<AnnualAverageAmount> supportAmount;
}
