package com.kakaopay.finance.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Institute {

    @Id
    @GeneratedValue(generator = "bnk-generator")
    @GenericGenerator(name = "bnk-generator",
            parameters = @Parameter(name = "prefix", value = "bnk"),
            strategy = "com.kakaopay.finance.config.StringIdentifierGenerator")
    private String institueCode;

    @NotNull
    private String institueName;
}
