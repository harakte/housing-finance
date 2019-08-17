package com.kakaopay.finance.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kakaopay.finance.entity.id.FinanceId;
import lombok.*;

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
