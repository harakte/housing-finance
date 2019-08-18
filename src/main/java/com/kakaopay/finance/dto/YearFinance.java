package com.kakaopay.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YearFinance {
    private Integer year;
    private Integer totalAmount;
    private Map<String, Integer> detailAmount;
}
