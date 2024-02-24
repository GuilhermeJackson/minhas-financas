package com.lamim.minhasfinancas.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lamim.minhasfinancas.model.entity.Lancamento;
import com.lamim.minhasfinancas.model.enums.StatusLancamento;
import com.lamim.minhasfinancas.model.enums.TipoLacamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
	
	@Query(value = "select sum(l.valor) from Lancamento l join l.usuario u " +
            "where u.id = :idUsuario and l.tipo =:tipo and l.status = :status group by u")
	BigDecimal obterSaldoPorTipoLancamentoEUsuarioEStatus(
			@Param("idUsuario") Long idUsuario,
			@Param("tipo") TipoLacamento tipo,
			@Param("status") StatusLancamento status
	);
}
