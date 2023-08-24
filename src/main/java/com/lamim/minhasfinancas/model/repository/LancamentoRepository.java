package com.lamim.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lamim.minhasfinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
	
}
