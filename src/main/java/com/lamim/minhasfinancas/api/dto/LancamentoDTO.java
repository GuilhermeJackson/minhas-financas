package com.lamim.minhasfinancas.api.dto;

import java.math.BigDecimal;

import com.lamim.minhasfinancas.model.enums.TipoLacamento;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LancamentoDTO {
	private Long id;
	private String descricao;
	private Integer mes;
	private Integer ano;
	private BigDecimal valor;
	private Long usuario_id;
	private String tipo;
	private String status;
}
