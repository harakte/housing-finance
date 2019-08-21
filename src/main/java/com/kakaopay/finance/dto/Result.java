package com.kakaopay.finance.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result {

    @NotNull
    @NotEmpty
    private boolean result;

    private String message;

    public Result(@NotNull @NotEmpty boolean result) {
        this.result = result;
    }
}
