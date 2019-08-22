package com.kakaopay.finance.entity.id;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FinanceId implements Serializable {
    private Integer year;
    private Integer month;
    private String institute;
}
