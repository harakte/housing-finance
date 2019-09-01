package com.kakaopay.finance.dto.finance;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class PredictFinance {
    @NotEmpty
    private String instituteCode;

    @NotNull
    @Min(0)
    private Integer year;

    @NotNull
    @Min(0)
    @Max(12)
    private Integer month;

    @NotNull
    @Min(0)
    private Integer amount;
}
