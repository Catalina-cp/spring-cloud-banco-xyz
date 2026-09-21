package com.bancoxyz.api_cuentas.repository;

import com.bancoxyz.api_cuentas.model.AnnualStatementSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnualStatementSummaryRepository extends JpaRepository<AnnualStatementSummary, Long> {
}