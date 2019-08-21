package com.kakaopay.finance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Institute {

    @Id
    @GeneratedValue(generator = "bnk-generator")
    @GenericGenerator(name = "bnk-generator",
            parameters = @Parameter(name = "prefix", value = "bnk"),
            strategy = "com.kakaopay.finance.config.StringIdentifierGenerator")
    private String instituteCode;

    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String instituteName;

    @OneToMany(mappedBy = "institute", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Finance> finances;

    public Institute(String instituteName) {
        this.instituteName = instituteName;
    }

    public void addFinance(Finance finance){
        if(finances == null){
            finances = new HashSet<>();
        }
        finances.add(finance);
    }
}
