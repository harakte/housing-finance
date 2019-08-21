package com.kakaopay.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PredictFinanceDto {

    @NotNull
    @NotEmpty
    private String instituteName;

    @NotNull
    @Min(0)
    @Max(12)
    private Integer month;
}
