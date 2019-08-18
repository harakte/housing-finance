package com.kakaopay.finance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kakaopay.finance.entity.id.FinanceId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(FinanceId.class)
public class Finance {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instituteCode")
    @JsonIgnore
    private Institute institute;

    @Id
    @NotNull
    private Integer year;

    @Id
    @NotNull
    private Integer month;

    @NotNull
    private Integer amount;
}
