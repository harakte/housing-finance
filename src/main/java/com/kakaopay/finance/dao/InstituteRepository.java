package com.kakaopay.finance.dao;

import com.kakaopay.finance.entity.Institute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstituteRepository extends JpaRepository<Institute, String> {
    Optional<Institute> findByInstituteName(String instituteName);
}
