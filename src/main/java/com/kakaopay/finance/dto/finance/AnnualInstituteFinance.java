package com.kakaopay.finance.dto.finance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnualInstituteFinance {
    private String instituteName;
    private Integer year;
    @JsonIgnore
    private Integer amount;
}
