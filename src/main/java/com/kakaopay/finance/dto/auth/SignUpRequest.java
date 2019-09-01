package com.kakaopay.finance.dto.auth;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SignUpRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
