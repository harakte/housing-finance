package com.kakaopay.finance.dao;

import com.kakaopay.finance.entity.Finance;
import com.kakaopay.finance.entity.id.FinanceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinanceRepository extends JpaRepository<Finance, FinanceId> {
}
