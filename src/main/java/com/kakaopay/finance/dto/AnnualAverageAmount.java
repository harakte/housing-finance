package com.kakaopay.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnualAverageAmount {
    private Integer year;
    private Integer amount;
}
